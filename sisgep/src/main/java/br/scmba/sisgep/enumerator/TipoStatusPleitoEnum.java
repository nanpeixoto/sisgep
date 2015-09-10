package br.scmba.sisgep.enumerator;



public enum TipoStatusPleitoEnum  {
	
	ABERTO("A", "Aberto"),
	PENDENTE("P", "Pendente"),
	FINALIZADO("F", "Finalizado");
	
	private String valor;
	private String descricao;
	
	private TipoStatusPleitoEnum(String valor, String descricao){
		this.descricao = descricao;
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static String getDescricaoByValor(String chave){
		for (TipoStatusPleitoEnum status : TipoStatusPleitoEnum.values()) {
			if(status.getValor().equals(chave))
				return status.getDescricao();
		}
		return "";
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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
