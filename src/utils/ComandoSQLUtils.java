package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.ComandoVO;


public class ComandoSQLUtils {

	private static List<String> prefixoComandosSQL = new ArrayList<String>(
			Arrays.asList("INSERT", "UPDATE", "ALTER", "SELECT", "CREATE",
						  "GRANT", "DROP", "COMMENT", "DELETE"));

//	private static List<String> prefixoComandosPLSQL = new ArrayList<String>(
//			Arrays.asList("DECLARE"));

	private static String patternQuebraLinha = "\n|\r\n";

	public static List<ComandoVO> prepararComando(String comandos) {

		List<ComandoVO> listaComandos = new ArrayList<>();
		String patternPLSQL = "declare+(.|" + patternQuebraLinha + ")*?end;";
		String patternSQL = criarPatterSQL();
		String patternGeral = "(?i)((" + patternPLSQL + ")|(" + patternSQL + "))";

		Pattern sql = Pattern.compile(patternGeral);
		Matcher matcher = sql.matcher(comandos);
		String comando;
		String texto = null;
		while (matcher.find()) {
			texto = matcher.group();
			if (texto == null || "".equals(texto.trim())) {
				LogUtil.Warn("UM COMANDO NÃO FOI ENCONTRADO."); 
				continue;
			}
			comando = texto.trim();
			LogUtil.Info("COMANDO ENCONTRADO: " + comando); 
			try {
				listaComandos.add(new ComandoVO(comando, isPLSQL(comando)));
			} catch (Exception e) {
				LogUtil.Error("ERRO AO PREPARAR COMANDO: " + e.getMessage()); 
			}
		}
		return listaComandos;
	}

	private static Boolean isPLSQL(String comando) throws Exception {

		if (comando != null && !"".equals(comando)) {
			if (comando.toUpperCase().startsWith("DECLARE")) {
				return true;
			} else {
				return false;
			}
		} else {
			throw new Exception("Comando chegou null no metodo \"isPLSQL\".");
		}
	}
	
	public static String removerPontoVirgulaFinal(String comando) {
		if (comando != null && comando.endsWith(";")) {
			comando = comando.substring(0, comando.length() - 1);
		}
		return comando;
	}

	private static String criarPatterSQL() {

		String pattern = "";
		String patternFim = "($|" + patternQuebraLinha + ")";
		for (String temp : prefixoComandosSQL) {
			pattern = pattern + "(" + temp + "(.|" + patternQuebraLinha + ")*?;" + patternFim + ")|";
		}
		pattern = pattern.substring(0, pattern.length() - 1);
		return pattern;
	}

}
