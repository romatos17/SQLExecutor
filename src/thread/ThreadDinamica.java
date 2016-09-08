package thread;

import java.lang.reflect.Method;

import utils.LogUtil;

/**
 * @author romatos
 * @version 1.0
 */

public class ThreadDinamica extends Thread {

	private Object[] parametros;
	private Object entidade;
	private String metodo;

	/**
	 * @param Object - Inst�ncia do objeto que possui o m�todo que ser� executado pela thread.
	 * @param String - Nome do m�todo (existente na entidade) que ser� executado.
	 * @param Object... - Par�metros para o m�todo que ser� executado.
	 * @author romatos
	 */
	public ThreadDinamica(Object entidade, String metodo, Object... parametros) {
		this.entidade = entidade;
		this.metodo = metodo;
		this.parametros = parametros;
	}

	@Override
	public void run() {
		try {
			LogUtil.Info("[THREAD] - EXECUTANDO METODO (" + this.entidade.getClass() + " -> " + this.metodo + ") ...");
			Class[] classParametros = null;
			if (this.parametros != null) {
				classParametros = new Class[this.parametros.length];
				for (int i = 0; i < this.parametros.length; i++) {
					classParametros[i] = this.parametros[i].getClass();
				}
			}
			Method metodoEx = this.entidade.getClass().getMethod(this.metodo, classParametros);
			metodoEx.invoke(this.entidade, this.parametros);
			LogUtil.Info("[THREAD] - METODO EXECUTADO COM SUCESSO (" + this.entidade.getClass() + " -> " + this.metodo + ").");
		} catch (Exception e) {
			LogUtil.Error("[THREAD] - ERRO AO EXECUTAR METODO (" + this.entidade.getClass() + " -> " + this.metodo + "): " + e.getMessage());
		}
	}

}
