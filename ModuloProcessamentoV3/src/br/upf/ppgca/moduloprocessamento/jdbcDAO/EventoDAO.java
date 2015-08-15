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
		String sqlToLeitura = "INSERT INTO evento(sensor_name,rms,tipo,datahora) VALUES (?,?,?,?)";
		Integer codev = null;
		try(PreparedStatement stmp = con.prepareStatement(sqlToLeitura,Statement.RETURN_GENERATED_KEYS)){
			stmp.setString(1, toInsert.getNomeSensor());
			stmp.setDouble(2, rmsCalculado);
			stmp.setBoolean(3, tipo);
			stmp.setTimestamp(4, toInsert.getHorarioLeitura());
			stmp.execute();
			try(ResultSet res = stmp.getGeneratedKeys()){
				if(res.next()) {
					codev = res.getInt("codev");
				}
			}
		}catch (SQLException e) {
			System.out.println("Erro em EventoDAO().inserir(): "+e);
		}
		return codev;
	}



	/**
	 * Consulta os registros que são maiores que a última leitura feita e devolve um resultset
	 * @param rmsMinimo 
	 * @return ResultSet com todos os registros que ainda não foram analisados
	 * @throws SQLException
	 */
	public ResultSet listar(int ultimoRegistro, double rmsMinimo) {
		try (PreparedStatement pstmt = con.prepareStatement("SELECT * FROM evento "
				+ "WHERE codev > ? "
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
