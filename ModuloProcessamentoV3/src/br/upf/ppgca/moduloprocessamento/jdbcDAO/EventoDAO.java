package br.upf.ppgca.moduloprocessamento.jdbcDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.upf.ppgca.moduloprocessamento.tipos.Leitura;

public class EventoDAO {
	private Connection con;
	public EventoDAO(Connection con) {
		this.con = con;
	}

	public Integer inserir(Leitura toInsert, Double rmsCalculado, boolean tipo) {
		String sqlToLeitura = "INSERT INTO evento(rms,tipo,volts,datahora,sensor_cod) VALUES (?,?,?,?,?)";
		Integer event_cod = null;
		try(PreparedStatement stmp = con.prepareStatement(sqlToLeitura,Statement.RETURN_GENERATED_KEYS)){
			stmp.setDouble(1, rmsCalculado);
			stmp.setBoolean(2, tipo);
			stmp.setDouble(3, toInsert.getVolts());
			stmp.setTimestamp(4, toInsert.getHorarioLeitura());
			stmp.setInt(5, toInsert.getCodigoSensor());
			stmp.execute();
			try(ResultSet res = stmp.getGeneratedKeys()){
				if(res.next()) {
					event_cod = res.getInt("event_cod");
				}
			}
		}catch (SQLException e) {
			System.out.println("Erro em EventoDAO().inserir(): "+e);
		}
		return event_cod;
	}



	/**
	 * Consulta os registros que são maiores que a última leitura feita e devolve um resultset
	 * @param rmsMinimo 
	 * @return ResultSet com todos os registros que ainda não foram analisados
	 * @throws SQLException
	 */
	public ResultSet listar(int ultimoRegistro, double rmsMinimo) {
		try (PreparedStatement pstmt = con.prepareStatement("SELECT * FROM evento "
				+ "WHERE event_cod > ? "
				+ "AND rms > ?", Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, ultimoRegistro);
			pstmt.setDouble(2, rmsMinimo);
			pstmt.execute();

			ResultSet result = pstmt.getResultSet();
			return result;
		}catch (SQLException e) {
			System.out.println("Erro em EventoDAO().listar(): "+e);
		}
		return null;
	}
}
