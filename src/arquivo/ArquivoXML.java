package arquivo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public abstract class ArquivoXML {

	private final static String DIRETORIO = System.getProperty("user.dir") + System.getProperty("file.separator");

	public static Element lerXML(String nomeArquivo) throws JDOMException, IOException {

		File fXmlFile = new File(DIRETORIO + nomeArquivo);
		SAXBuilder sb = new SAXBuilder();
		Document d = sb.build(fXmlFile);
		Element conteudo = d.getRootElement();
		return conteudo;
	}

	public static List getFilhos(Element conteudo, String no) {
		return conteudo.getChildren(no);
	}

	public static Map<String, String> getConteudoDoFilho(Element filho) {

		Map<String, String> conteudo = new HashMap<String, String>();
		List<Element> listaFilho = filho.getChildren();

		if (listaFilho != null && !listaFilho.isEmpty()) {
			for (Element temp : listaFilho) {
				if (temp.getValue() != null && !"".equals(temp.getValue())) {
					conteudo.put(temp.getName(), temp.getValue());
				}
			}
		}
		return conteudo;
	}

}
