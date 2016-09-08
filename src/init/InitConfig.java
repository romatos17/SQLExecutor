package init;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;

import utils.LogUtil;
import arquivo.ArquivoXML;
import constantes.ConfigContantes;
import constantes.ConstantesXML;

public abstract class InitConfig {

	private static final String NOMEARQUIVO = "config.xml";

	public static void init() {

		try {
			LogUtil.Info("INICIANDO IMPORTACAO DE CONFIGURACAO: " + NOMEARQUIVO + " ...");

			Element mural = ArquivoXML.lerXML(NOMEARQUIVO);

			loadConfigThread(ArquivoXML.getFilhos(mural, ConstantesXML.ELEMENTO_CONFIG_THREAD));

			LogUtil.Info("IMPORTACAO DE CONFIGURACOES REALIZADA COM SUCESSO.");
		} catch (Exception e) {
			LogUtil.Error("ERRO NO PROCESSO DE IMPORTAÇÃO DE CONFIGURACOES: " + e.getMessage());
		}
	}

	private static void loadConfigThread(List elements) {

		LogUtil.Info("IMPORTANDO CONFIGURACAO DE THREAD ...");
		Iterator i = elements.iterator();
		Map<String,String> filho;
		while (i.hasNext()) {
			Element element = (Element) i.next();
			filho = ArquivoXML.getConteudoDoFilho(element);
			if (filho.containsKey(ConstantesXML.ELEMENTO_QTD_THREAD)) {
				ConfigContantes.QTD_THREAD = Integer.parseInt(filho.get(ConstantesXML.ELEMENTO_QTD_THREAD));
			}
			if (filho.containsKey(ConstantesXML.ELEMENTO_TIME_THREAD)) {
				ConfigContantes.TIME_THREAD = Integer.parseInt(filho.get(ConstantesXML.ELEMENTO_TIME_THREAD));
			}
		}
		LogUtil.Info("CONFIGURACAO DE THREAD IMPORTADA.");
	}

}
