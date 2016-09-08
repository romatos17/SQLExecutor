package dao;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import model.BancoDadosVO;
import utils.LogUtil;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class ConexaoPool {

	private static Map<String,BoneCP> connectionsPool = null;

	static {
		connectionsPool = new HashMap<String, BoneCP>();
	}

	public static void criarPool(BancoDadosVO configBanco) {
		try {
			LogUtil.Info("CRIANDO POOL DE CONEXAO: " + configBanco.getChave() + " ...");
			initDataSource(configBanco);
			LogUtil.Info("POOL DE CONEXAO CRIADO COM SUCESSO (" + configBanco.getChave() + ").");
		} catch (Exception e) {
			LogUtil.Error("ERRO AO CRIAR POOL DE CONEXAO (" + configBanco.getChave() + "): " + e.getMessage());
		}
	}

	private static BoneCP getPool(String nomePool) {
		BoneCP pool = connectionsPool.get(nomePool);
		if (pool != null) {
			return pool;
		} else {
			return null;
		}
	}

	public static void finalizarPool(String nomePool) {
		LogUtil.Info("FINALIZANDO POOL: " + nomePool);
		BoneCP connFinalizar = getPool(nomePool);
		if (connFinalizar != null) {
			connFinalizar.close();
			LogUtil.Info("POOL FINALIZADO COM SUCESSO: " + nomePool);
		} else {
			LogUtil.Warn("POOL DE CONEXAO NAO ENCONTRADO: " + nomePool);
		}
	}

	public static Connection getConnection(String nomePool) throws Exception {
		BoneCP pool = getPool(nomePool);
		if (pool != null) {
			return pool.getConnection();
		} else {
			throw new Exception("POOL NÃO INICIALIZADO.");
		}
	}

	private static void initDataSource(BancoDadosVO banco) throws Exception {
		try {
			Class.forName(banco.getClassForName());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		try {
			// setup the connection pool
			BoneCPConfig config = new BoneCPConfig();
			config.setJdbcUrl(banco.getUrlConexao());
			config.setUsername(banco.getUsuario());
			config.setPassword(banco.getSenha());

			config.setMinConnectionsPerPartition(banco.getQtdConexoes());
			config.setMaxConnectionsPerPartition(banco.getQtdConexoes());
			config.setPartitionCount(banco.getQtdParticoes());
			config.setDefaultAutoCommit(true);

			connectionsPool.put(banco.getChave(), new BoneCP(config));
		} catch (Exception ex) {
			LogUtil.Error("AO CRIAR POOL DE CONEXÕES: " + ex.getMessage());
			throw ex;
		}
	}

}