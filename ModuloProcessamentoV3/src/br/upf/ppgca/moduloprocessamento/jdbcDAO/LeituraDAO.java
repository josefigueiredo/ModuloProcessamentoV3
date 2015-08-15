package br.upf.ppgca.moduloprocessamento.jdbcDAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.upf.ppgca.moduloprocessamento.tipos.Leitura;

// O DAO para Leitura envolve as tabelas Leitura e Valores da leitura,
public class LeituraDAO {
	private Connection con;
	
	public LeituraDAO(Connection con) {
		this.con = con;
	}

	public Integer inserir(Leitura toInsert, double rmsCalculado, boolean tipo) throws SQLException {
		String sqlToLeitura = "INSERT INTO leitura(sensor_name,timestamp,rms,tipo) VALUES (?,?,?,?)";
		Integer codGerado = null;
		try(PreparedStatement stmp = con.prepareStatement(sqlToLeitura,Statement.RETURN_GENERATED_KEYS)){
			stmp.setString(1, toInsert.getNomeSensor());
			stmp.setTimestamp(2, toInsert.getHorarioLeitura());
			stmp.setDouble(3, rmsCalculado);
			stmp.setBoolean(4, tipo);
			stmp.execute();
			try(ResultSet res = stmp.getGeneratedKeys()){
				if(res.next()) {
					codGerado = res.getInt("LEITURA_COD");
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("Erro em LeituraDAO: "+e);
		}
		return codGerado;
	}
}