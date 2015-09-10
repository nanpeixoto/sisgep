package br.scmba.sisgep.enumerator;

public enum TipoStatusEnvioEmailPleitoMovimentacaoEnum {

	ERRO("E", "icon_advertencia.png"), 
	SUCESSO("S", "icon_info.png");

	private String valor;
	private String icone;

	private TipoStatusEnvioEmailPleitoMovimentacaoEnum(String valor, String icone) {
		this.icone = icone;
		this.valor = valor;
	}

	public static String getDescricaoByValor(String chave) {
		for (TipoStatusEnvioEmailPleitoMovimentacaoEnum status : TipoStatusEnvioEmailPleitoMovimentacaoEnum.values()) {
			if (status.getValor().equals(chave))
				return status.getIcone();
		}
		return "";
	}
	
	public static TipoStatusEnvioEmailPleitoMovimentacaoEnum getTipoStatusEmailPleitoMovimentacaoEnumByValor(String valor) {
		for (TipoStatusEnvioEmailPleitoMovimentacaoEnum status : TipoStatusEnvioEmailPleitoMovimentacaoEnum.values()) {
			if (status.getValor().equals(valor))
				return status;
		}
		return null;
	}

	public String getIcone() {
		return icone;
	}

	public void setIcone(String icone) {
		this.icone = icone;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String recreateString() {
		return valor;
	}

}
