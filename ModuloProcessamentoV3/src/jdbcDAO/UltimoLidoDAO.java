package jdbcDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UltimoLidoDAO {
	private Connection con;

	public UltimoLidoDAO(Connection con) {
		this.con = con;
	}

	public int ler() throws SQLException {
		try (Statement stmt = con.createStatement()) {
			String sql = "SELECT ultimolido FROM globalparam";
			ResultSet res = stmt.executeQuery(sql);
			res.next();
			return res.getInt(1);
		}
	}

	public void atualizaUltimoLido(int lidoAgora) {
		String sql = "UPDATE globalparam SET ultimolido = ?";
		try (PreparedStatement stmp = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmp.setInt(1, lidoAgora);
			stmp.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
