package br.scmba.security.dto;

public class Email {

	private String enderecoOrigem;
	private String enderecoDestino;
	private String senha;
	private Long portaSmtp;
	private String stsTls;
	private String hostSmtp;
	private String sslPortaSmtp;
	private String Assunto;
	private String corpo;

	public boolean isTls() {
		if (stsTls != null)
			if (stsTls.equalsIgnoreCase("S"))
				return true;
			else
				return false;
		return false;
	}

	public String getEnderecoOrigem() {
		return enderecoOrigem;
	}

	public void setEnderecoOrigem(String enderecoOrigem) {
		this.enderecoOrigem = enderecoOrigem;
	}

	public String getEnderecoDestino() {
		return enderecoDestino;
	}

	public void setEnderecoDestino(String enderecoDestino) {
		this.enderecoDestino = enderecoDestino;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Long getPortaSmtp() {
		return portaSmtp;
	}

	public void setPortaSmtp(Long portaSmtp) {
		this.portaSmtp = portaSmtp;
	}

	public String getStsTls() {
		return stsTls;
	}

	public void setStsTls(String stsTls) {
		this.stsTls = stsTls;
	}

	public String getHostSmtp() {
		return hostSmtp;
	}

	public void setHostSmtp(String hostSmtp) {
		this.hostSmtp = hostSmtp;
	}

	public String getSslPortaSmtp() {
		return sslPortaSmtp;
	}

	public void setSslPortaSmtp(String sslPortaSmtp) {
		this.sslPortaSmtp = sslPortaSmtp;
	}

	public String getAssunto() {
		return Assunto;
	}

	public void setAssunto(String assunto) {
		Assunto = assunto;
	}

	public String getCorpo() {
		return corpo;
	}

	public void setCorpo(String corpo) {
		this.corpo = corpo;
	}

}
