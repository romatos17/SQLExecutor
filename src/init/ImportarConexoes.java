package init;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import model.BancoDadosVO;

import org.jdom2.Element;

import constantes.ConstantesXML;

import arquivo.ArquivoXML;
import utils.LogUtil;

public class ImportarConexoes {

	private static final String NOMEARQUIVO = "config-banco.xml";

	public static List<BancoDadosVO> importarBancos() throws Exception {

		try {
			LogUtil.Info("INICIANDO IMPORTACAO DE BANCO DE DADOS: " + NOMEARQUIVO + " ...");

			List<BancoDadosVO> listaBancos = new ArrayList<BancoDadosVO>();

			Element mural = ArquivoXML.lerXML(NOMEARQUIVO);

			List elements = ArquivoXML.getFilhos(mural, ConstantesXML.ELEMENTO_BANCO_DADOS);
			Iterator i = elements.iterator();
			BancoDadosVO banco = null;
			Map<String,String> filho;
			while (i.hasNext()) {
				Element element = (Element) i.next();
				filho = ArquivoXML.getConteudoDoFilho(element);
				banco = convertElementXMLtoBancoDadoVO(filho);
				if (banco != null) {
					listaBancos.add(banco);
				}
			}
			LogUtil.Info("IMPORTACAO DE BANCO DE DADOS REALIZADA COM SUCESSO (" + listaBancos.size() + " BANCO(S) IMPORTADO(S)).");
			return listaBancos;
		} catch (Exception e) {
			LogUtil.Error("ERRO NO PROCESSO DE IMPORTAÇÃO DE BANCOS DE DADOS: " + e.getMessage());
			throw e;
		}
	}

	private static BancoDadosVO convertElementXMLtoBancoDadoVO(Map<String,String> element) {

		try {
			BancoDadosVO banco = new BancoDadosVO();
			banco.setAtivo(new Boolean(element.get(ConstantesXML.ELEMENTO_ATIVO)));
			if (!banco.getAtivo()) {
				return null;
			}
			banco.setChave(element.get(ConstantesXML.ELEMENTO_CHAVE));
			banco.setUrlConexao(element.get(ConstantesXML.ELEMENTO_URL));
			banco.setClassForName(element.get(ConstantesXML.ELEMENTO_DRIVER));
			banco.setUsuario(element.get(ConstantesXML.ELEMENTO_USUARIO));
			banco.setSenha(element.get(ConstantesXML.ELEMENTO_SENHA));
			String valorInteiro = element.get(ConstantesXML.ELEMENTO_QTD_CONEXOES);
			if (valorInteiro == null || "".equals(valorInteiro)) {
				banco.setQtdConexoes(1);
			} else {
				banco.setQtdConexoes(Integer.parseInt(valorInteiro));
			}
			valorInteiro = element.get(ConstantesXML.ELEMENTO_QTD_PARTICOES);
			if (valorInteiro == null || "".equals(valorInteiro)) {
				banco.setQtdParticoes(1);
			} else {
				banco.setQtdParticoes(Integer.parseInt(valorInteiro));
			}
			if (validarBancoDados(banco)) {
				return banco;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Boolean validarBancoDados(BancoDadosVO banco) {

		// VALIDAR CAMPOS OBRIGATORIOS
		if (   banco.getChave() == null || "".equals(banco.getChave())
			|| banco.getUrlConexao() == null || "".equals(banco.getUrlConexao())
			|| banco.getClassForName() == null || "".equals(banco.getClassForName())
			|| banco.getUsuario() == null || "".equals(banco.getUsuario())
			|| banco.getSenha() == null || "".equals(banco.getSenha())
				) {
			LogUtil.Warn("UM BANCO NAO SERA IMPORTADO POIS EXISTE CAMPO(S) OBRIGATORIO(S) NAO PREENCHIDO(S).");
			return false;
		} else {
			return true;
		}
	}

}
