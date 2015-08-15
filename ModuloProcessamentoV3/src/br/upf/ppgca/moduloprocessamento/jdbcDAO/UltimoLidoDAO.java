package br.upf.ppgca.moduloprocessamento.jdbcDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UltimoLidoDAO {
	private Connection con;
	
	public UltimoLidoDAO(Connection con) {
		this.con = con;
	}
	
	public int ler() throws SQLException {
		try(Statement stmt = con.createStatement()) {
			String sql = "SELECT ult_leitura_cod FROM ultimolido";
			ResultSet res = stmt.executeQuery(sql);
			res.next();
			return 0;
		}
	}
}
