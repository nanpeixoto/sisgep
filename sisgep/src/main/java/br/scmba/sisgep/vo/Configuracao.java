package br.scmba.sisgep.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.scmba.arquitetura.BaseEntity;
import br.scmba.arquitetura.util.Constantes;

@Entity
@Table(name = "CONFIGURACAO", schema = Constantes.SCHEMADB_NAME)
public class Configuracao extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SEQUENCIA")
	private Long sequencia;

	@Column(name = "EMAIL_ENDERECO")
	private String emailEndereco;

	@Column(name = "EMAIL_SENHA")
	private String emailSenha;

	@Column(name = "EMAIL_PORTA_SMTP")
	private Long emailPortaSmtp;

	@Column(name = "EMAIL_STS_TLS")
	private String emailStsTls;

	@Column(name = "EMAIL_HOST_SMTP")
	private String emailHostSmtp;

	@Column(name = "EMAIL_SSL_PORTA_SMTP")
	private String emailSslPortaSmtp;

	@Column(name = "STS_ENVIA_EMAIL")
	private String stsEnviaEmail;

	@Column(name = "EMAIL_ASSUNTO_UNIDADE")
	private String emailAssuntoUnidade;

	@Column(name = "EMAIL_ASSUNTO_SEM_UNIDADE")
	private String emailAssuntoSemUnidade;

	@Column(name = "EMAIL_CORPO_UNIDADE")
	private String emailCorpoUnidade;

	@Column(name = "EMAIL_CORPO_SEM_UNIDADE")
	private String emailCorpoSemUnidade;

	public String getEmailEndereco() {
		return emailEndereco;
	}

	public void setEmailEndereco(String emailEndereco) {
		this.emailEndereco = emailEndereco;
	}

	public String getEmailSenha() {
		return emailSenha;
	}

	public void setEmailSenha(String emailSenha) {
		this.emailSenha = emailSenha;
	}

	public Long getEmailPortaSmtp() {
		return emailPortaSmtp;
	}

	public void setEmailPortaSmtp(Long emailPortaSmtp) {
		this.emailPortaSmtp = emailPortaSmtp;
	}

	public String getEmailStsTls() {
		return emailStsTls;
	}

	public void setEmailStsTls(String emailStsTls) {
		this.emailStsTls = emailStsTls;
	}

	public String getEmailHostSmtp() {
		return emailHostSmtp;
	}

	public void setEmailHostSmtp(String emailHostSmtp) {
		this.emailHostSmtp = emailHostSmtp;
	}

	public String getEmailSslPortaSmtp() {
		return emailSslPortaSmtp;
	}

	public void setEmailSslPortaSmtp(String emailSslPortaSmtp) {
		this.emailSslPortaSmtp = emailSslPortaSmtp;
	}

	public String getStsEnviaEmail() {
		return stsEnviaEmail;
	}

	public void setStsEnviaEmail(String stsEnviaEmail) {
		this.stsEnviaEmail = stsEnviaEmail;
	}

	@Override
	public Object getId() {
		return sequencia;
	}

	@Override
	public void setId(Object id) {
		this.sequencia = (Long) id;

	}

	@Override
	public String getColumnOrderBy() {
		return "sequencia";
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

	@Override
	public Usuario getUsuarioCadastro() {
		return null;
	}

	@Override
	public Date getDataCadastro() {
		return null;
	}

	public Long getSequencia() {
		return sequencia;
	}

	public void setSequencia(Long sequencia) {
		this.sequencia = sequencia;
	}

	public String getEmailAssuntoUnidade() {
		return emailAssuntoUnidade;
	}

	public void setEmailAssuntoUnidade(String emailAssuntoUnidade) {
		this.emailAssuntoUnidade = emailAssuntoUnidade;
	}

	public String getEmailAssuntoSemUnidade() {
		return emailAssuntoSemUnidade;
	}

	public void setEmailAssuntoSemUnidade(String emailAssuntoSemUnidade) {
		this.emailAssuntoSemUnidade = emailAssuntoSemUnidade;
	}

	public String getEmailCorpoUnidade() {
		return emailCorpoUnidade;
	}

	public void setEmailCorpoUnidade(String emailCorpoUnidade) {
		this.emailCorpoUnidade = emailCorpoUnidade;
	}

	public String getEmailCorpoSemUnidade() {
		return emailCorpoSemUnidade;
	}

	public void setEmailCorpoSemUnidade(String emailCorpoSemUnidade) {
		this.emailCorpoSemUnidade = emailCorpoSemUnidade;
	}

}