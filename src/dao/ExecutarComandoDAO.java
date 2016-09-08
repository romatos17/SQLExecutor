package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;

import model.ComandoVO;
import model.ScriptVO;
import utils.ComandoSQLUtils;
import utils.LogUtil;
import utils.SQLUtil;

public class ExecutarComandoDAO extends DAO {

	public static void executarScript(ScriptVO script) {

		int numero = 1;
		if (script.getComandos() != null && script.getArquivo()) {
			LogUtil.Info("EXECUTANDO SCRIPT: " + script.getNomeArquivo() + " ...");
			for (ComandoVO comando : script.getComandos()) {
				try {
					if (comando.getPlSQL()) {
						executarComandoPLSQL(comando, script.getChave());
					} else {
						executarComando(comando, script.getChave());
					}
				} catch (Exception e) {
					LogUtil.Error("ERRO AO EXECUTAR O COMANDO (NUMERO: " + numero + " | CHAVE: " + script.getChave() + " | ARQUIVO: " + script.getNomeArquivo() + "): " + e.getMessage() );
				}
				numero++;
			}
			LogUtil.Info("SCRIPT EXECUTADO: " + script.getNomeArquivo() + " ...");
		} else if (script.getListaScript() != null && !script.getArquivo()) {
			for (ScriptVO scriptTemp : script.getListaScript()) {
				executarScript(scriptTemp);
			}
		}
	}

	private static void executarComando(ComandoVO comando, String chave) throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConexaoPool.getConnection(chave);
			stmt = conn.prepareStatement(ComandoSQLUtils.removerPontoVirgulaFinal(comando.getComando()));
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			SQLUtil.closeStatement(stmt);
			SQLUtil.closeConnection(conn);
		}
	}

	private static void executarComandoPLSQL(ComandoVO comando, String chave) throws Exception {

		Connection conn = null;
		CallableStatement cs= null;
		try {
			conn = ConexaoPool.getConnection(chave);
			cs = conn.prepareCall(comando.getComando());
			cs.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			cs.close();
			SQLUtil.closeConnection(conn);
		}
	}

}
