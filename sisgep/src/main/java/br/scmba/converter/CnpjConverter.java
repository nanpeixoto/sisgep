package br.scmba.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import br.scmba.arquitetura.util.Constantes;
import br.scmba.util.Util;

@FacesConverter("converter.CnpjConverter")
public class CnpjConverter implements Converter {
	
	/**
	 * Método para remover os pontos e hifens do CNPJ;
	 * 
	 */
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		String cnpj = value;

		if (value!= null && !value.equals("") && !value.equals(Constantes.PADRAO_FORMATO_CNPJ) ){
			cnpj = value.replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", "");
		
		}else{
			cnpj = null;
		}

		return cnpj;
	}

    /**
	 * Método para adicionar pontos e hifens no CNPJ além de colocar zeros a esquerda;
	 * 
	 */
	
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		String cnpj= value.toString();
		
		cnpj = Util.lpad(cnpj, '0', 14);
		
		if (cnpj != null && cnpj.length() == 11)
			cnpj = cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5, 8) + "/" 
		+ cnpj.substring(8, 12) + "-" + cnpj.substring(12, 14);

		return cnpj;
	}

}
