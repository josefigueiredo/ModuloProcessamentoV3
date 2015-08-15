package br.upf.ppgca.moduloprocessamento.programa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.upf.ppgca.moduloprocessamento.jdbcDAO.MeuPoolConection;

public class BuscarDadosEquipamentos {

	public static void main(String[] args) throws SQLException {
		try (Connection con = new MeuPoolConection().getConnection()) {
			// buscar os dados
			String sqlLeitura = "select * from leitura where leitura_cod = ?";

			int[] codigos = new int[] {148,119,117,137,146};
			for (int i : codigos) {
				try (PreparedStatement stmp = con.prepareStatement(sqlLeitura, Statement.RETURN_GENERATED_KEYS)) {
					stmp.setInt(1, i);
					stmp.execute();
					ResultSet res = stmp.getResultSet();
					while (res.next()) {
						//insere as leituras dos equipamentos
						String sqlInsert = "INSERT INTO leitura_eq(RMS,codigo) VALUES(?,?)";
						try(PreparedStatement stmpInsert = con.prepareStatement(sqlInsert,Statement.RETURN_GENERATED_KEYS)){
							stmpInsert.setDouble(1, res.getDouble(4));
							stmpInsert.setInt(2, 0);
							stmpInsert.execute();
							System.out.println(stmpInsert.getGeneratedKeys());
						}
					}
					res.close();
					stmp.close();
				}	
			}
			
		}
		// inserir os dados

	}
}
