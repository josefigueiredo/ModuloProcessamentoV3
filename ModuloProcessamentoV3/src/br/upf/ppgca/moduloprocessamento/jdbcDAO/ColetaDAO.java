package br.upf.ppgca.moduloprocessamento.jdbcDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import br.upf.ppgca.moduloprocessamento.tipos.Coleta;

public class ColetaDAO {
	private Connection con;
	public ColetaDAO(Connection con) {
		// TODO Auto-generated constructor stub
		this.con = con;
	}
	
	public void inserirTensao(Coleta coleta) {
		String sqlToColetas = "INSERT INTO coletastensao(event_cod,pos_vetor,tensao_lida) VALUES (?,?,?)";
		inserir(coleta, sqlToColetas);		
	}

	public void inserirCorrente(Coleta coleta) {
		String sqlToColetas = "INSERT INTO coletascorrente(event_cod,pos_vetor,corrente_lida) VALUES (?,?,?)";
		inserir(coleta, sqlToColetas);		
	}
	
	private void inserir(Coleta coleta, String sqlToColetas) {
		int pos_vetor=0;
		for (Double valor : coleta.getValoresLidos()) {
			try(PreparedStatement stmp =con.prepareStatement(sqlToColetas,Statement.RETURN_GENERATED_KEYS)){
				stmp.setInt(1, coleta.getEvent_cod());
				stmp.setInt(2, pos_vetor++);
				stmp.setDouble(3, valor);
				stmp.execute();
			}catch (SQLException e) {
				System.out.println("Erro em ColetaDAO:"+e);
			}
		}
	}
}
