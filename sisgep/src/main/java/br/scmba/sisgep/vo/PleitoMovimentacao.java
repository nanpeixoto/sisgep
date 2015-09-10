package br.scmba.sisgep.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.scmba.arquitetura.BaseEntity;
import br.scmba.arquitetura.util.Constantes;
import br.scmba.sisgep.enumerator.ExtensaoArquivoEnum;
import br.scmba.sisgep.enumerator.TipoStatusEnvioEmailPleitoMovimentacaoEnum;
import br.scmba.sisgep.util.EmailRetorno;

/**
 * The persistent class for the PLEITO_MOVIMENTACAO database table.
 * 
 */
@Entity
@Table(name = "PLEITO_MOVIMENTACAO", schema = Constantes.SCHEMADB_NAME)
@SequenceGenerator(name = "SEQ_PLEITO_MOVIMENTACAO", sequenceName = "SEQ_PLEITO_MOVIMENTACAO", allocationSize = 1)
public class PleitoMovimentacao extends BaseEntity implements Comparable<PleitoMovimentacao> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PLEITO_MOVIMENTACAO")
	@Column(name = "SEQUENCIA")
	private Long sequencia;

	@Column(name = "ACAO")
	private String acao;

	@Column(name = "DATA_CADASTRO")
	private Date dataCadastro;

	@ManyToOne
	@JoinColumn(name = "SEQ_USUARIO_CADASTRO")
	private Usuario usuarioCadastro;

	@ManyToOne
	@JoinColumn(name = "SEQ_PLEITO")
	private Pleito pleito;

	@ManyToOne
	@JoinColumn(name = "SEQ_TIPO_STATUS")
	private TipoStatus tipoStatus;

	@ManyToOne
	@JoinColumn(name = "SEQ_UNIDADE_NEGOCIO")
	private UnidadeNegocio unidadeNegocio;

	// E - ERRO / S - SUCESSO
	@Column(name = "STS_ENVIO_EMAIL")
	private String stsEnvioEmail;

	@Column(name = "DESCRICAO_ERRO_ENVIO_EMAIL")
	private String descricaoErroEnvioEmail;

	@Column(name = "EMAIL_DE_ENVIO")
	private String emailDeEnvio;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "ARQUIVO")
	private byte[] arquivo;

	@Column(name = "NOME_ARQUIVO")
	private String nomeArquivo;

	public String getAcao() {
		if (acao != null)
			return acao.toUpperCase();
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao.toUpperCase();
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Usuario getUsuarioCadastro() {
		return usuarioCadastro;
	}

	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

	public TipoStatus getTipoStatus() {
		return tipoStatus;
	}

	public void setTipoStatus(TipoStatus tipoStatus) {
		this.tipoStatus = tipoStatus;
	}

	public UnidadeNegocio getUnidadeNegocio() {
		return unidadeNegocio;
	}

	public void setUnidadeNegocio(UnidadeNegocio unidadeNegocio) {
		this.unidadeNegocio = unidadeNegocio;
	}

	public String getDataCadastroFormatada() {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return format.format(getDataCadastro());
	}

	@Override
	public Object getId() {
		return sequencia;
	}

	@Override
	public void setId(Object id) {
		sequencia = (Long) id;

	}

	@Override
	public String getColumnOrderBy() {
		return "dataCadastro desc";
	}

	@Override
	public String getAuditoria() {
		return null;
	}

	@Override
	public Usuario getUsuarioAlteracao() {
		return null;
	}

	@Override
	public Date getDataAlteracao() {
		return null;
	}

	public Pleito getPleito() {
		return pleito;
	}

	public void setPleito(Pleito pleito) {
		this.pleito = pleito;
	}

	@Override
	public int compareTo(PleitoMovimentacao o) {
		if (getDataCadastro() == null || o.getDataCadastro() == null)
			return 0;
		if (getDataCadastro().before(o.getDataCadastro()))
			return -1;
		return 1;
	}

	public String getIconeRetornoEnvio() {
		if (stsEnvioEmail != null || !stsEnvioEmail.equalsIgnoreCase("")) {
			return TipoStatusEnvioEmailPleitoMovimentacaoEnum.getTipoStatusEmailPleitoMovimentacaoEnumByValor(stsEnvioEmail).getIcone();
		}
		return "";
	}

	public String getRetornoEnvio() {
		if (stsEnvioEmail != null || !stsEnvioEmail.equalsIgnoreCase("")) {
			if (stsEnvioEmail.equalsIgnoreCase("S"))// ENVIADO COM SUCESSO
				return "Enviado para:" + emailDeEnvio;
			else {
				return "Erro:" + EmailRetorno.RETORNO_USUARIO + "|| E-mail:" + (emailDeEnvio == null ? "Sem e-mail cadastrado." : emailDeEnvio);
			}
		}

		return "";
	}

	public String getIconExtensaoArquivoEnviado() {
		if (this.nomeArquivo != null && !nomeArquivo.equalsIgnoreCase("")) {
			String[] lista = this.nomeArquivo.split(".");
			String extensaoStr = "";
			for (int i = 0; i < nomeArquivo.length(); i++) {
				if(nomeArquivo.charAt(i) == '.'){
					extensaoStr = nomeArquivo.substring(i+1, nomeArquivo.length());
					break;
				}

			}			ExtensaoArquivoEnum extensao = null;
			try {
				extensao = ExtensaoArquivoEnum.getEnumByValor(extensaoStr);
			} catch (Exception e) {
				extensao = null;
			}

			if (extensao == null)
				return ExtensaoArquivoEnum.OUTROS.getIcone();
			else
				return extensao.getIcone();

		}
		return "";
	

	}

	public Boolean isEmailEnviado() {
		if (stsEnvioEmail == null || stsEnvioEmail.equalsIgnoreCase("")) {
			return false;
		}
		return true;
	}
	
	public Boolean isArquivoAnexado() {
		if (nomeArquivo == null || nomeArquivo.equalsIgnoreCase("")) {
			return false;
		}
		return true;
	}


	public String getStsEnvioEmail() {
		return stsEnvioEmail;
	}

	public void setStsEnvioEmail(String stsEnvioEmail) {
		this.stsEnvioEmail = stsEnvioEmail;
	}

	public String getDescricaoErroEnvioEmail() {
		return descricaoErroEnvioEmail;
	}

	public void setDescricaoErroEnvioEmail(String descricaoErroEnvioEmail) {
		this.descricaoErroEnvioEmail = descricaoErroEnvioEmail;
	}

	public String getEmailDeEnvio() {
		return emailDeEnvio;
	}

	public void setEmailDeEnvio(String emailDeEnvio) {
		this.emailDeEnvio = emailDeEnvio;
	}

	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

}