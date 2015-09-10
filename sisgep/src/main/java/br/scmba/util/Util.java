package br.scmba.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classse utilizadas para colocar o objeto na sessão e ser utilizado.
 * 
 * @author esouzaa
 * @author jteixeira
 * @author jsouzasa
 * @author rmotal
 * 
 */

public class Util {

	public static final String STRING_VAZIA = "";

	private static final String WILDCARD = "%";

	public static final String LETRAS_COM_ACENTUACAO = "ÁÀÃÂÄÉÈÊËÍÌÏÎÓÒÕÔÖÚÙÛÜÇÑÝŸáàãâäéèêëíìïîóòõôöúùûüçñýÿ";
	public static final String LETRAS_SEM_ACENTUACAO = "AAAAAEEEEIIIIOOOOOUUUUCNYYaaaaaeeeeiiiiooooouuuucnyy";

	/**
	 * Método para verificar se um objeto do tipo Boolean é nulo ;
	 * 
	 * 
	 */

	public static Boolean isNull(Object objeto) {
		return (objeto == null) ? Boolean.TRUE : Boolean.FALSE;
	}

	/**
	 * Método para verificar se um objeto do tipo String é nulo;
	 * 
	 * 
	 */

	public static Boolean isEmptyString(String str) {
		return (!Util.isNull(str) && str.length() == 0) ? Boolean.TRUE : Boolean.FALSE;
	}

	/**
	 * Método para verificar se um objeto é nulo ou vazio;
	 * 
	 * 
	 */

	@SuppressWarnings("rawtypes")
	public static boolean isNullOuVazio(Object pObjeto) {

		if (pObjeto == null) {

			return true;

		} else if (pObjeto instanceof Collection) {

			return ((Collection) pObjeto).isEmpty();

		} else if (pObjeto instanceof String) {

			return ((String) pObjeto).trim().equals(STRING_VAZIA);

		} else if (pObjeto instanceof Integer) {

			return ((Integer) pObjeto).intValue() == 0;
		}

		return false;
	}

	public static boolean isNullOuVazio(Integer integer) {
		return integer == null;
	}

	/**
	 * Remove acentos de uma String
	 * 
	 * @param String
	 *            string
	 */

	public static String removeAcentos(String string) {
		return Normalizer.normalize(string.trim(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}

	public static String pesquisaPorNome(String nomeParametroPesquisa, String campoBanco) {
		StringBuilder hql = new StringBuilder();
		// String nomeModificado = WILDCARD +
		// translate(nomeParametroPesquisa.trim().toUpperCase()) + WILDCARD;
		String nomeModificado = WILDCARD + removeAcentos(nomeParametroPesquisa.trim().toUpperCase()) + WILDCARD;
		hql.append(" AND TRANSLATE(UPPER(" + campoBanco + "), '" + LETRAS_COM_ACENTUACAO + "', '" + LETRAS_SEM_ACENTUACAO + "') like '"
				+ nomeModificado + "'");
		return hql.toString();
	}

	/*
	 * public static String formatData(Date data, String pFormato){
	 * SimpleDateFormat sdate = new SimpleDateFormat(pFormato); return
	 * sdate.format(data); }
	 */

	public static String formatData(Date data, String pFormato) {
		SimpleDateFormat sdate = new SimpleDateFormat(pFormato);
		return sdate.format(data);
	}

	public static String formatDataHora(Date data) {
		SimpleDateFormat sdate = new SimpleDateFormat("dd/MM/yyyy/HH:mm");
		return sdate.format(data);
	}

	/* MÉTODO PARA INSERIR ZEROS A ESQUERDA */

	public static String lpad(String input, char padding, int length) {

		String output = "";

		if (input != null) {

			output = input;

		}

		if (output.length() >= length) {

			return output;

		} else {

			StringBuffer result = new StringBuffer();

			int numChars = length - output.length();

			for (int i = 0; i < numChars; i++) {

				result.append(padding);

			}

			result.append(output);

			return result.toString();

		}

	}

	/**
	 * Método para formatação de CPF;
	 * 
	 * 
	 */

	public static String formataCPF(Long cpf) {
		String cpfString = lpad(Long.toString(cpf), '0', 11);

		if (cpfString != null) {

			// cpfString = cpfString.substring(0, 9) + "-" +
			// cpfString.substring(9, 11);
			cpfString = cpfString.substring(0, 3) + "." + cpfString.substring(3, 6) + "." + cpfString.substring(6, 9) + "-"
					+ cpfString.substring(9, 11);

		}

		return cpfString;
	}

	/**
	 * Método para formatação de cnpj
	 * 
	 * @param pCnpj
	 * @return String
	 */
	public static String formataCnpj(Long pCnpj) {
		String cnpjString = lpad(Long.toString(pCnpj), '0', 14);

		if (!isNullOuVazio(cnpjString)) {
			cnpjString = cnpjString.substring(0, 2) + "." + cnpjString.substring(2, 5) + "." + cnpjString.substring(5, 8) + "/"
					+ cnpjString.substring(8, 12) + "-" + cnpjString.substring(12, 14);
		}

		return cnpjString;
	}

	/**
	 * Metodo valida se determinado nome esta de acordo com o padrao da
	 * expressao regular
	 * 
	 * @param pExpressaoRegular
	 * @param pNome
	 * @return boolean
	 */
	public static boolean validarNomePorExpressaoRegular(String pExpressaoRegular, String pNome) {
		// Obtem expressao regular informada
		Pattern pattern = Pattern.compile(pExpressaoRegular);
		// Obtem o nome informado
		Matcher matcher = pattern.matcher(pNome);
		// Verifica se o nome informado esta de acordo com expressao regular
		if (matcher.find()) {
			// Nome e valido segundo a expressao regular
			return true;
		} else {
			// Nome nao e valido segundo a expressao regular
			return false;
		}
	}

	public static String formataCodigo(Integer codigo) {

		String codigoFormatado = lpad(Integer.toString(codigo), '0', 4);
		if (codigoFormatado != null) {
			return codigoFormatado;
		} else {
			return "";
		}

	}

	/**
	 * Valida CNPJ do usuário.
	 * 
	 * @param cnpj
	 *            String valor com 14 dígitos
	 */
	public static Boolean validaCNPJ(String cnpj) {

		if (cnpj == null || cnpj.length() != 14) {
			return false;
		}

		try {
			Long.parseLong(cnpj);
		} catch (NumberFormatException e) { // CNPJ não possui somente números
			return false;
		}

		int soma = 0;
		String cnpj_calc = cnpj.substring(0, 12);

		char chr_cnpj[] = cnpj.toCharArray();

		for (int i = 0; i < 4; i++)
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9)
				soma += (chr_cnpj[i] - 48) * (6 - (i + 1));

		for (int i = 0; i < 8; i++)
			if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9)
				soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));

		int dig = 11 - soma % 11;
		cnpj_calc = (new StringBuilder(String.valueOf(cnpj_calc))).append(dig != 10 && dig != 11 ? Integer.toString(dig) : "0").toString();
		soma = 0;

		for (int i = 0; i < 5; i++)
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9)
				soma += (chr_cnpj[i] - 48) * (7 - (i + 1));

		for (int i = 0; i < 8; i++)
			if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9)
				soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));

		dig = 11 - soma % 11;
		cnpj_calc = (new StringBuilder(String.valueOf(cnpj_calc))).append(dig != 10 && dig != 11 ? Integer.toString(dig) : "0").toString();

		if (!cnpj.equals(cnpj_calc) || cnpj.equals("00000000000000")) {
			// JsfUtil.addErrorMessage("CNPJ Inválido!");
			return false;
		}

		return true;
	}

	public static String convertStringToMD5(String input) {

		String md5 = null;

		if (null == input)
			return null;

		try {

			// Create MessageDigest object for MD5
			MessageDigest digest = MessageDigest.getInstance("MD5");

			// Update input string in message digest
			digest.update(input.getBytes(), 0, input.length());

			// Converts message digest value in base 16 (hex)
			md5 = new BigInteger(1, digest.digest()).toString(64);

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		return md5;
	}

	public static String formataLista(List<String> lista) {
		String r = "";
		if (lista.size() >= 1) {
			r += lista.get(0);
			if (lista.size() > 1) {
				for (int i = 1; i < (lista.size() - 1); i++) {
					r += ", " + lista.get(i);
				}
				r += " e " + lista.get(lista.size() - 1);
			}
		}
		return r;
	}

	/**
	 * Método responsável por formatar a mensagem para ser exibida no p:message
	 * do JSF o metódo vai criar um paragrafo para cada "/n" encontrado no
	 * parâmetro informado.
	 * 
	 * @param textoASerExibido
	 */
	public static void formatarMensagemDeValidacao(String textoASerExibido) {
		String[] vetor = textoASerExibido.split("/n");
		for (int i = 0; i < vetor.length; i++) {
			// JsfUtil.addErrorMessage(vetor[i]);
		}
	}

	public static String calculaTempoEntreDatas(Date d1, Date d2) {

		if (!isNullOuVazio(d1) && !isNullOuVazio(d2)) {

			long segundos = (d2.getTime() / 1000) - (d1.getTime() / 1000);
			long segundo = segundos % 60;
			long minutos = segundos / 60;
			long minuto = minutos % 60;
			long hora = minutos / 60;
			String hms = String.format("%02d:%02d:%02d", hora, minuto, segundo);
			return hms;

		}

		return "";
	}
}