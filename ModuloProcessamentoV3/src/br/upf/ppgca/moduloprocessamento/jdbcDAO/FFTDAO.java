package br.upf.ppgca.moduloprocessamento.jdbcDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.math3.complex.Complex;

public class FFTDAO {
	private Connection con;

	public FFTDAO(Connection con) {
		this.con = con;
	}

	public void insere(Integer event_cod, Complex[] resultFFT) {
		String sqlToFFT = "INSERT INTO fft_evento(event_cod,POS_VETOR,SEN_VAL,COS_VAL) VALUES (?,?,?,?)";
		try (PreparedStatement stmp = con.prepareStatement(sqlToFFT, Statement.RETURN_GENERATED_KEYS)) {
			int pos = 0;
			for (Complex inserir : resultFFT) {
				stmp.setInt(1, event_cod);
				stmp.setInt(2, pos++);
				stmp.setDouble(3, inserir.getImaginary());
				stmp.setDouble(4, inserir.getReal());
				stmp.execute();
			}
		}catch (SQLException e) {
			// TODO: handle exception
			System.out.println("Erro do FFTDAO:"+e);
		}
	}

}
