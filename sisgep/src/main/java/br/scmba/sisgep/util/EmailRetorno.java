package br.scmba.sisgep.util;


public class EmailRetorno {

	private boolean sucesso;
	private String mensagemErro;
	private String emailEnvio;
	public static String RETORNO_USUARIO = "Erro no Envio do Email||";

	public boolean isSucesso() {
		return sucesso;
	}

	public void setSucesso(boolean sucesso) {
		this.sucesso = sucesso;
	}

	public String getMensagemErro() {
		return mensagemErro;
	}

	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
	}

	public String getEmailEnvio() {
		return emailEnvio;
	}

	public void setEmailEnvio(String emailEnvio) {
		this.emailEnvio = emailEnvio;
	}

}
