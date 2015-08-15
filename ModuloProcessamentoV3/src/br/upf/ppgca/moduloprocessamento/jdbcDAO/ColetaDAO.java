package br.upf.ppgca.moduloprocessamento.jdbcDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ColetaDAO {
	private Connection con;
	public ColetaDAO(Connection con) {
		// TODO Auto-generated constructor stub
		this.con = con;
	}
	
	public void inserir(Integer event_cod, List<Double> list){
		String sqlToColetas = "INSERT INTO coletas_evento(event_cod,pos_vetor,valor_lido) VALUES (?,?,?)";
		int pos_vetor=0;
		for (Double valor : list) {
			try(PreparedStatement stmp =con.prepareStatement(sqlToColetas,Statement.RETURN_GENERATED_KEYS)){
				stmp.setInt(1, event_cod);
				stmp.setInt(2, pos_vetor++);
				stmp.setDouble(3, valor);
				stmp.execute();
			}catch (SQLException e) {
				System.out.println("Erro em ColetaDAO:"+e);
			}
		}
	}

}
