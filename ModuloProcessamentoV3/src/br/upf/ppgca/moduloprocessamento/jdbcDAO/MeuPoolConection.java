package br.upf.ppgca.moduloprocessamento.jdbcDAO;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hsqldb.jdbc.JDBCPool;

public class MeuPoolConection {
	private DataSource dataSource;

	public MeuPoolConection() {
		JDBCPool pool = new JDBCPool();
		pool.setUrl("jdbc:hsqldb:hsql://localhost/bancoV4");
		pool.setUser("SA");
		pool.setPassword("");
		this.dataSource = pool;
	}
	public Connection getConnection() throws SQLException {
		//System.out.println("Abrindo conexão com o banco de dados");
		//Connection connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/loja-virtual", "SA", "");
		Connection con = dataSource.getConnection();
		return con;
	}	

}
