package jdbcDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import modelo.Consumo;
import modelo.SobreTensao;

public class SobreTensaoDAO {

	private Connection con;

	public SobreTensaoDAO(Connection con) {
		this.con = con;
	}

	public void insert(SobreTensao sobretensao) {
		String sql = "INSERT INTO sobretensao(event_cod,datahora,tensao_rms) VALUES(?,?,?)";
		try (PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, sobretensao.getEvent_cod());
			pstmt.setTimestamp(2, sobretensao.getHorarioLeitura());
			pstmt.setDouble(3, sobretensao.getTensaoRMS());
			
			pstmt.execute();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//metodo para quantificar picos positivos
	//metodo para quantifiar picos negativos
	
	public int getSobreTensaoMes(Timestamp timestamp) {
		int temp = 0;
			String sql = "SELECT count(*) FROM sobretensao WHERE datahora between ? AND CURDATE() + INTERVAL 1 DAY";
		try(PreparedStatement stmp = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
			stmp.setTimestamp(1, timestamp);
			stmp.execute();
			try(ResultSet result = stmp.getResultSet()){
				while(result.next()) {
					temp = result.getInt(1);
				}
				return temp;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public int getSobreTensaoMes(Timestamp timestamp, char c, double valPico) {
		int cont=0;
		String sql = null;
		if(valPico > 0 ) {
			sql = "SELECT count(event_cod) FROM evento E, coletastensao C "
					+ "WHERE e.event_cod = c.event_cod AND c.tensao_lida > ? AND e.tipo = 'a' "
					+ "AND datahora BETWEEN ? AND CURDATE() + INTERVAL 1 DAY";	
			
		}else if(valPico < 0) {
			sql = "SELECT count(event_cod) FROM evento E, coletastensao C "
					+ "WHERE e.event_cod = c.event_cod AND c.tensao_lida < ? AND e.tipo = 'a' "
					+ "AND datahora BETWEEN ? AND CURDATE() + INTERVAL 1 DAY";
		}
		try(PreparedStatement stmp = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
			stmp.setDouble(1, valPico);
			stmp.setTimestamp(2, timestamp);
			stmp.execute();
			ResultSet res = stmp.getResultSet();
			while(res.next()) {
				cont = res.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cont;
	}

	public int getSobreTensaoAgora(int ultimoEvent_cod) {
		String sql = "SELECT count(*) FROM sobretensao WHERE event_cod = ? ";
		try(PreparedStatement stmp = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
			stmp.setInt(1, ultimoEvent_cod);
			stmp.execute();

			// o teste é para ver se este evento foi inserido no registro de sobretensão (se sim retorna 1 para incrementar
			// senão retorna 0 )
			try(ResultSet result = stmp.getResultSet()){
				result.next();
				if(result.getInt(1) == 1) {
					return 1;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public int getSobrePicosTensaoAgora(int ultimoEvent_cod, char c, double valPico) {
		int cont=0;
		String sql = null;
		if(valPico > 0 ) {
			sql = "SELECT count(event_cod) FROM evento E, coletastensao C "
					+ "WHERE e.event_cod = c.event_cod AND c.tensao_lida > ? AND e.tipo = 'a' "
					+ "AND e.event_cod = ?";	
			
		}else if(valPico < 0) {
			sql = "SELECT count(event_cod) FROM evento E, coletastensao C "
					+ "WHERE e.event_cod = c.event_cod AND c.tensao_lida < ? AND e.tipo = 'a' "
					+ "AND e.event_cod = ?";
		}
		try(PreparedStatement stmp = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
			stmp.setDouble(1, valPico);
			stmp.setInt(2, ultimoEvent_cod);
			stmp.execute();
			ResultSet res = stmp.getResultSet();
			while(res.next()) {
				cont = res.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cont;
	}

}
