package dao;

import java.sql.Connection;

public class DAO {

	public Connection getConnection(String nomeConn) throws Exception {
		return ConexaoPool.getConnection(nomeConn);
	}

}
