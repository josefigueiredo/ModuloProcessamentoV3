package br.upf.ppgca.moduloprocessamento.jdbcDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.upf.ppgca.moduloprocessamento.tipos.Evento;
import br.upf.ppgca.moduloprocessamento.tipos.Leitura;

public class EventoDAO {
	private Connection con;
	public EventoDAO(Connection con) {
		this.con = con;
	}
	
	public Integer inserir(Evento evento) {
		String sqlToLeitura = "INSERT INTO evento(datahora,sensor_cod,tipo,volts_rms,corrente_rms,fi) VALUES (?,?,?,?,?,?)";
		Integer event_cod = null;
		try(PreparedStatement stmp = con.prepareStatement(sqlToLeitura,Statement.RETURN_GENERATED_KEYS)){
			stmp.setTimestamp(1, evento.getHorarioLeitura());
			stmp.setInt(2, evento.getCodigoSensor());
			stmp.setString(3, Character.toString(evento.getTipoEvento())); //converto de char para String para adequar ao PreparedStatement
			stmp.setDouble(4, evento.getVoltsRMS());
			stmp.setDouble(5, evento.getCorrenteRMS());
			stmp.setDouble(6, evento.getFi());
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
