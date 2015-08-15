package br.upf.ppgca.moduloprocessamento.jdbcDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HarmonicaDAO {
	private Connection con;

	public HarmonicaDAO(Connection con) {
		this.con = con;
	}

	public void inserir(Integer codev, double[] diferenca) {
		String sqlToColetas = "INSERT INTO harm_identificada(codev,pos_vetor_harm,valor_harm_ident) VALUES (?,?,?)";
		int pos_vetor = 0;
		for (Double valor : diferenca) {
			try (PreparedStatement stmp = con.prepareStatement(sqlToColetas, Statement.RETURN_GENERATED_KEYS)) {
				stmp.setInt(1, codev);
				stmp.setInt(2, pos_vetor++);
				stmp.setDouble(3, valor);
				stmp.execute();
			} catch (SQLException e) {
				// TODO: handle exception
				System.out.println("Erro em HarmonicaDAO:" + e);
			}
		}
	}

	public Double getHarmonica(int codev, int harm) {
		try (PreparedStatement pstmt = con.prepareStatement(
				"SELECT valor_harm_ident FROM harm_identificada WHERE codev = ? AND pos_vetor_harm = ?",
				Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, codev);
			pstmt.setInt(2, harm);
			pstmt.execute();

			ResultSet result = pstmt.getResultSet();
			result.next();
			return result.getDouble(1);
		} catch (SQLException e) {
			System.out.println("Erro em RmsDAO().consultaRMS(): " + e);
		}
		return null;
	}
}
