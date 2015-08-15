package br.upf.ppgca.moduloprocessamento.jdbcDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RmsDAO {
	private Connection con;
	public RmsDAO(Connection con) {
		this.con = con;
	}

	public void inserir(Integer codev, double variacaoRMS) {
		String sql = "INSERT INTO rms_identific(codev,rms_identific) VALUES(?,?)";
		try(PreparedStatement pstmt = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
			pstmt.setInt(1,codev);
			pstmt.setDouble(2, variacaoRMS);
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public Double consultarRMS(int codev) {
		try (PreparedStatement pstmt = con.prepareStatement("SELECT rms_identific FROM rms_identific WHERE codev = ?", Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, codev);
			pstmt.execute();

			ResultSet result = pstmt.getResultSet();
			result.next();
			return result.getDouble(1);
		}catch (SQLException e) {
			System.out.println("Erro em RmsDAO().consultaRMS(): "+e);
		}
		return null;
	}

}
