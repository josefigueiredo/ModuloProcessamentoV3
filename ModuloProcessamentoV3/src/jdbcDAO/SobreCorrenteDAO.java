package jdbcDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import modelo.SobreCorrente;

public class SobreCorrenteDAO {

	private Connection connection;

	public SobreCorrenteDAO(Connection con) {
		this.connection = con;
	}

	public void inserir(SobreCorrente sobreCorrente) {
		String sql = "INSERT INTO sobrecorrente(event_cod,datahora,corrente_rms) VALUES(?,?,?)";
		try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, sobreCorrente.getEvent_cod());
			pstmt.setTimestamp(2, sobreCorrente.getHorarioLeitura());
			pstmt.setDouble(3, sobreCorrente.getCorrenteRMS());
			pstmt.execute();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getSobreCorrenteMes(Timestamp timestamp) {
		int temp = 0;
		String sql = "SELECT count(*) FROM sobrecorrente WHERE datahora between ? AND CURDATE() + INTERVAL 1 DAY";
		try(PreparedStatement stmp = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
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

	public int getSobreCorrenteAgora(int ultimoEvent_cod) {
		int cont=0;
		String sql = "SELECT count(*) FROM sobrecorrente WHERE event_cod = ?";
		try(PreparedStatement stmp = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
			stmp.setInt(1, ultimoEvent_cod);
			stmp.execute();
			try(ResultSet result = stmp.getResultSet()){
				while(result.next()) {
					cont = result.getInt(1);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return cont;	
	}

}
