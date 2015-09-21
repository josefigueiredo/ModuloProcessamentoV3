package jdbcDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import modelo.Evento;
import modelo.Leitura;

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

	public HashMap<String, Object> lerUltimoEvento(int ultimoLido) {
		HashMap<String,Object> resultado = new HashMap<>();
		String sql = "SELECT event_cod,volts_rms,corrente_rms,fi,datahora FROM evento WHERE event_cod > ?";
		try (PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, ultimoLido);
			pstmt.execute();
			
			ResultSet result = pstmt.getResultSet();
			result.next();
			
			resultado.put("lidoAgora", result.getInt(1));
			resultado.put("tensao", result.getDouble(2));
			resultado.put("corrente", result.getDouble(3));
			resultado.put("fi", result.getDouble(4));			
			resultado.put("datahora", result.getTimestamp(5).getTime());
			
		}catch (SQLException e) {
			System.out.println("Erro em EventoDAO().listar(): "+e);
		}
		return resultado;
	}

	public Evento getEvento(int ultimoEvtInserido) {
		// select dados do evento 
		String sql = "SELECT * FROM evento WHERE event_cod = ?";
		try (PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, ultimoEvtInserido);
			pstmt.execute();
			
			try(ResultSet result = pstmt.getResultSet()){
				result.next();
				return new Evento(result.getInt(1),result.getTimestamp(2),result.getInt(3),
						result.getString(4).charAt(0),result.getDouble(5),result.getDouble(6),result.getDouble(7));	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
