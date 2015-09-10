package br.scmba.sisgep.util;

import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import br.scmba.security.dto.Email;
import br.scmba.sisgep.vo.Configuracao;
import br.scmba.sisgep.vo.PleitoMovimentacao;

public class EnvioEmail {

	public class MyAuthenticator extends Authenticator {
		private String enderecoOrigem;
		private String senha;

		public MyAuthenticator(String enderecoOrigem, String senha) {
			this.enderecoOrigem = enderecoOrigem;
			this.senha = senha;
		}

		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(enderecoOrigem, senha);
		}
	}

	public static void bypassSSL() {
		final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public void checkClientTrusted(final java.security.cert.X509Certificate[] certs, final String authType) {
			}

			@Override
			public void checkServerTrusted(final java.security.cert.X509Certificate[] certs, final String authType) {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		} };
		try {
			final SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			SSLContext.setDefault(sc);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public EmailRetorno enviaEmailSimples(Email email, String caminhoArquivoAnexo) throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT-3:00"));
		EmailRetorno retorno = new EmailRetorno();

		try {
			if (!emailValido(email)) {
				retorno.setEmailEnvio(email.getEnderecoDestino());
				retorno.setMensagemErro("Campos Obrigatórios não preenhidos");
				retorno.setSucesso(false);
			}

			System.out.println("Iniciando envio...");

			final Properties props = new Properties();

			final String smtp = "smtp.office365.com";

			props.put("mail.smtp.host", smtp);
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtps.auth", "true");
			props.put("mail.debug", "true");

			props.put("mail.smtp.port", "587");

			final Session session = Session.getInstance(props, new MyAuthenticator(email.getEnderecoOrigem(), email.getSenha()));

			// criando a mensagem
			final MimeMessage message = new MimeMessage(session);

			final Address from = new InternetAddress(email.getEnderecoOrigem());
			final Address to = new InternetAddress(email.getEnderecoDestino());
			message.setFrom(from);
			message.addRecipient(RecipientType.TO, to);
			message.setSentDate(new Date());
			message.setSubject(email.getAssunto());

			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setText(email.getCorpo());
			
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);

			MimeBodyPart mbp2 = new MimeBodyPart();
			// enviando anexo
			if (caminhoArquivoAnexo != null && !caminhoArquivoAnexo.equalsIgnoreCase("")) {
				DataSource fds = new FileDataSource(caminhoArquivoAnexo);
				mbp2.setDisposition(Part.ATTACHMENT);
				mbp2.setDataHandler(new DataHandler(fds));
				mbp2.setFileName(fds.getName());
				mp.addBodyPart(mbp2);
			}

			
			
			
			message.setContent(mp);

			bypassSSL();
			Transport.send(message);

			retorno.setEmailEnvio(email.getEnderecoDestino());
			retorno.setSucesso(true);
			System.out.println("Enviado!");

			return retorno;

		} catch (Exception e) {
			e.printStackTrace();
			retorno.setEmailEnvio(email.getEnderecoDestino());
			retorno.setMensagemErro(e.getCause().toString().substring(0, (e.getCause().toString().length() > 4000 ? 4000 : e.getCause().toString().length())));
			retorno.setSucesso(false);
			return retorno;
		}

	}

	private static boolean emailValido(Email email) {
		if (email.getHostSmtp() == null)
			return false;
		if (email.getPortaSmtp() == null)
			return false;
		if (email.getSslPortaSmtp() == null)
			return false;
		if (email.getEnderecoOrigem() == null)
			return false;
		if (email.getSenha() == null)
			return false;
		if (email.getEnderecoDestino() == null)
			return false;
		if (email.getAssunto() == null)
			return false;
		if (email.getCorpo() == null)
			return false;
		return true;
	}

	public static Email converterPleitoConfiguracaoEmail(Configuracao configuracao, PleitoMovimentacao pleitoMovimentacao) {
		Email email = new Email();
		email.setHostSmtp(configuracao.getEmailHostSmtp());
		email.setPortaSmtp(configuracao.getEmailPortaSmtp());
		email.setSslPortaSmtp(configuracao.getEmailSslPortaSmtp());
		email.setEnderecoOrigem(configuracao.getEmailEndereco());
		email.setSenha(configuracao.getEmailSenha());
		email.setStsTls(configuracao.getEmailStsTls());

		if (pleitoMovimentacao.getTipoStatus().getStsExigeUnidadeNegocio().equalsIgnoreCase("S")) {
			email.setEnderecoDestino(pleitoMovimentacao.getUnidadeNegocio().getUsuarioResponsavel().getEmail());
			email.setAssunto(configuracao.getEmailAssuntoUnidade());
			email.setCorpo(montarTexto(configuracao.getEmailCorpoUnidade(), pleitoMovimentacao));
		} else {
			email.setEnderecoDestino(pleitoMovimentacao.getPleito().getUsuarioCadastro().getEmail());
			email.setAssunto(configuracao.getEmailAssuntoSemUnidade());
			email.setCorpo(montarTexto(configuracao.getEmailCorpoSemUnidade(), pleitoMovimentacao));
		}

		return email;

	}

	public static String montarTexto(String corpo, PleitoMovimentacao movimento) {
		String texto = "", variavel = "";
		int aux = 0;
		char[] teste = corpo.toCharArray();
		for (int i = 0; i < teste.length; i++) {
			if (teste[i] != '$') {
				texto += teste[i];
			} else {
				aux = i;
				while (aux < teste.length && teste[aux] != ' ' && teste[aux] != '\n' && teste[aux] != '\r' && teste[aux] != '.' && teste[aux] != ')') {
					variavel += teste[aux];
					aux++;

				}

				i = aux;
				if (variavel.equalsIgnoreCase("$objetoPleito$")) {
					texto = movimento.getPleito().getObjeto();
					corpo = corpo.replace(variavel, texto.toUpperCase());
					variavel = "";
				}

				if (variavel.equalsIgnoreCase("$seqpleito$")) {
					texto = movimento.getPleito().getId().toString();
					corpo = corpo.replace(variavel, texto);
					variavel = "";
				}

				if (variavel.equalsIgnoreCase("$nomesolicitante$")) {
					texto = movimento.getPleito().getSolicitante().getNome();
					corpo = corpo.replace(variavel, texto);
					variavel = "";
				}

				if (variavel.equalsIgnoreCase("$acao$")) {
					texto = movimento.getAcao();
					corpo = corpo.replace(variavel, texto);
					variavel = "";
				}

				if (variavel.equalsIgnoreCase("$nomemovimentacao$")) {
					texto = movimento.getUsuarioCadastro().getNome();
					corpo = corpo.replace(variavel, texto);
					variavel = "";
				}

				if (variavel.equalsIgnoreCase("$statusmovimentacao$")) {
					texto = movimento.getTipoStatus().getDescricao();
					corpo = corpo.replace(variavel, texto);
					variavel = "";
				}
				variavel = "";
			}
		}
		return corpo;
	}

}
