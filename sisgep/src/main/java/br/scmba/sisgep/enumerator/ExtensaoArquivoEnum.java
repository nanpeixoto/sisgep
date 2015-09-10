package br.scmba.sisgep.enumerator;

public enum ExtensaoArquivoEnum {

	GIF("gif", "icon_img.png"),
	JPEG("jpeg", "icon_img.png"),
	JPG("jpg", "icon_img.png"),
	PNG("png", "icon_img.png"),
	PDF("pdf", "icon_pdf.png"),
	DOC("doc", "icon_word.png"),
	DOCX("docx", "icon_word.png"),
	XLS("xls", "icon_excel.png"),
	XLSX("xlsx", "icon_excel.png"),
	OUTROS("ARQUIVO", "icon_arquivo.png");
	

	private String valor;
	private String icone;

	private ExtensaoArquivoEnum(String valor, String icone) {
		this.icone = icone;
		this.valor = valor;
	}

	public static String getDescricaoByValor(String chave) {
		for (ExtensaoArquivoEnum status : ExtensaoArquivoEnum.values()) {
			if (status.getValor().equals(chave))
				return status.getIcone();
		}
		return "";
	}
	
	public static ExtensaoArquivoEnum getEnumByValor(String valor) {
		for (ExtensaoArquivoEnum status : ExtensaoArquivoEnum.values()) {
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
