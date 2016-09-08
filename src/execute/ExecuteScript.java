package execute;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import model.ComandoVO;
import model.ScriptVO;

import org.jdom2.Element;

import thread.ThreadDinamica;
import utils.ComandoSQLUtils;
import utils.LogUtil;
import arquivo.ArquivoXML;
import constantes.ConfigContantes;
import constantes.ConstantesXML;
import dao.ExecutarComandoDAO;

public class ExecuteScript {

	private static final String NOMEARQUIVO = "scripts.xml";
	private List<ScriptVO> listaScripts;
	private List<ScriptVO> listaScriptsThread;
	private ThreadPoolExecutor multiThread;

	public ExecuteScript() {
		this.listaScripts = new ArrayList<ScriptVO>();
		this.listaScriptsThread = new ArrayList<ScriptVO>();
	}

	public void executar() throws Exception {

		this.importarScripts();
		this.executarScript(this.listaScriptsThread, true);
		this.executarScript(this.listaScripts, false);
		if (!this.listaScriptsThread.isEmpty()) {
			if (this.multiThread != null) {
				while (this.multiThread.getActiveCount() > 0);
				this.multiThread.shutdown();
			}
		}
	}

	private void importarScripts() throws Exception {

		try {
			LogUtil.Info("INICIANDO IMPORTACAO DE SCRIPT: " + NOMEARQUIVO + " ...");

			Element mural = ArquivoXML.lerXML(NOMEARQUIVO);
			List elements = ArquivoXML.getFilhos(mural, ConstantesXML.ELEMENTO_SCRIPT_ARQUIVO);
			Iterator i = elements.iterator();
			ScriptVO script = null;
			Element element = null;
			while (i.hasNext()) {
				element = (Element) i.next();
				script = this.convertElementXMLtoScriptVO(ArquivoXML.getConteudoDoFilho(element));
				if (script != null) {
					if (script.getThread()) {
						listaScriptsThread.add(script);
					} else {
						listaScripts.add(script);
					}
				}
			}
			Collections.sort(listaScripts);
			LogUtil.Info("IMPORTACAO DE SCRIPT REALIZADA COM SUCESSO (" + (listaScripts.size() + listaScriptsThread.size()) + " SCRIPT IMPORTADO(S)).");
		} catch (Exception e) {
			LogUtil.Error("ERRO NO PROCESSO DE IMPORTAÇÃO DE SCRIPT: " + e.getMessage());
			throw e;
		}
	}

	private ScriptVO convertElementXMLtoScriptVO(Map<String, String> element) {

		ScriptVO script = new ScriptVO();
		
		script.setAtivo(new Boolean(element.get(ConstantesXML.ELEMENTO_ATIVO)));
		if (script.getAtivo() == null || !script.getAtivo()) {
			return null;
		}

		script.setChave(element.get(ConstantesXML.ELEMENTO_CHAVE));
		if ("".equals(script.getChave())) {
			return null;
		}

		script.setThread(new Boolean(element.get(ConstantesXML.ELEMENTO_THREAD)));

		String caminhoArquivo = element.get(ConstantesXML.ELEMENTO_CAMINHO_ARQUIVO);
		if (caminhoArquivo == null || "".equals(caminhoArquivo)) {
			return null;
		}

		if (!prepararComandosScript(script, caminhoArquivo)) {
			return null;
		}

		if ((script.getComandos() != null && !script.getComandos().isEmpty())
				|| (script.getListaScript() != null && !script.getListaScript().isEmpty())) {
			return script;
		} else {
			return null;
		}
	}

	private Boolean prepararComandosScript(ScriptVO script, String dirArquivo) {

		File file = new File(dirArquivo);
		if (file.exists()) {
			if (file.isFile()) {
				// LER ARQUIVO
				script.setArquivo(true);
				script.setExtensao(dirArquivo.substring(dirArquivo.lastIndexOf(".") + 1, dirArquivo.length()));
				if (!ConfigContantes.EXTENSOES_PERMITIDAS.contains(script.getExtensao().toLowerCase())) {
					return false;
				}
				script.setNomeArquivo(file.getName());
				script.setComandos(this.obterComandosArquivo(dirArquivo));
			} else {
				// LER DIRETORIO
				script.setArquivo(false);
				if (script.getListaScript() == null) {
					script.setListaScript(new ArrayList<ScriptVO>());
				}
				File[] listaDir = file.listFiles();
				Arrays.sort(listaDir);
				ScriptVO scriptTemp;
				for (File temp : listaDir) {
					scriptTemp = new ScriptVO();
					if (prepararComandosScript(scriptTemp, temp.getAbsolutePath())) {
						scriptTemp.setAtivo(script.getAtivo());
						scriptTemp.setChave(script.getChave());
						scriptTemp.setThread(script.getThread());
						script.getListaScript().add(scriptTemp);
					}
				}
			}
		} else {
			LogUtil.Warn("DIRETORIO NAO ENCONTRADO: " + dirArquivo);
			return false;
		}
		return true;
	}

	private List<ComandoVO> obterComandosArquivo(String diretorio) {

		try {
			List<ComandoVO> comandos = new ArrayList<ComandoVO>();
			Scanner scan = new Scanner(new File(diretorio)).useDelimiter("\\Z");
			if (scan.hasNext()) {
				String conteudo = scan.next();
				comandos = ComandoSQLUtils.prepararComando(conteudo);
			}
			scan.close();
			return comandos;
		} catch (Exception e) {
			LogUtil.Error("ERRO AO OBTER LER ARQUIVO (" + diretorio + "): " + e.getMessage());
			return null;
		}
	}

	public void executarScript(ScriptVO script) {
		ExecutarComandoDAO.executarScript(script);
		LogUtil.Info("FIM DA EXECUCAO DO SCRIPT (CHAVE: " + script.getChave() + ")");
	}

	public void executarScript(List<ScriptVO> listaScript, Boolean modoThread) {

		if (!listaScript.isEmpty()) {
			if (modoThread) {
				this.multiThread = new ThreadPoolExecutor(ConfigContantes.QTD_THREAD, ConfigContantes.QTD_THREAD, ConfigContantes.TIME_THREAD, TimeUnit.MICROSECONDS, new ArrayBlockingQueue<Runnable>(listaScript.size()));
				LogUtil.Info("INICIANDO EXECUCAO DO SCRIPT COM THREAD (CHAVE: " + listaScript.get(0).getChave() + ").");
			} else {
				LogUtil.Info("INICIANDO EXECUCAO DO SCRIPT SEM THREAD (CHAVE: "	+ listaScript.get(0).getChave() + ").");
			}
		} else {
			LogUtil.Info("NENHUM SCRIPT " + (modoThread ? "COM" : "SEM") + " THREAD FOI LOCALIZADO.");
			return;
		}

		for (ScriptVO script : listaScript) {
			if (modoThread != null && modoThread) {
				this.multiThread.execute(new ThreadDinamica(this, "executarScript", script));
			} else {
				this.executarScript(script);
			}
		}
	}

}
