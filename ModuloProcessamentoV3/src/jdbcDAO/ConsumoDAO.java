package jdbcDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;

import modelo.Consumo;
import modelo.Evento;

public class ConsumoDAO {

	private Connection con;

	public ConsumoDAO(Connection con) {
		this.con = con;
	}

	public Integer inserir(Consumo consumoAcumulado){
		String sql = "INSERT INTO consumo(event_cod,inicio_evt,fim_evt,kw,delta_t) values (?,?,?,?,?)";
		try(PreparedStatement stmp = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
			stmp.setInt(1, consumoAcumulado.getEvent_cod());
			stmp.setTimestamp(2, consumoAcumulado.getInicioEvento());
			stmp.setTimestamp(3, consumoAcumulado.getFimEvento());
			stmp.setDouble(4, consumoAcumulado.getKw());
			stmp.setDouble(5, consumoAcumulado.getDeltaT());
			stmp.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Consumo getConsumoAgora(int event_cod) {
		String sql = "SELECT * FROM consumo WHERE event_cod = ? ";
		try(PreparedStatement stmp = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
			stmp.setInt(1, event_cod-1);
			stmp.execute();
			
			try(ResultSet result = stmp.getResultSet()){
				result.next();
				return new Consumo(result.getInt(1), result.getTimestamp(2), result.getTimestamp(3), result.getDouble(4),result.getDouble(5)); 	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;	
	}

	public double getConsumoDia() {
		Double temp = 0.0;
		String sql = "SELECT * FROM consumo WHERE inicio_evt >= curdate() "
				+ "and inicio_evt < curdate() + interval 1 DAY";
		try(PreparedStatement stmp = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
			stmp.execute();
			try(ResultSet result = stmp.getResultSet()){
				while(result.next()) {
					temp += (result.getDouble(4) * result.getDouble(5));
				}
				return temp;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}

	public double getConsumoMes(Timestamp timestamp) {
		Double temp = 0.0;
		String sql = "SELECT * FROM consumo WHERE inicio_evt between ? AND CURDATE() + INTERVAL 1 DAY";
		try(PreparedStatement stmp = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
			stmp.setTimestamp(1, timestamp);
			stmp.execute();
			try(ResultSet result = stmp.getResultSet()){
				while(result.next()) {
					temp += (result.getDouble(4) * result.getDouble(5));
				}
				return temp;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

}
