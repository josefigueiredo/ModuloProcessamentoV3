package jdbcDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.spi.DirStateFactory.Result;

import modelo.UltimoEvento;

public class UltimoEventoDAO {

	private Connection con;

	public UltimoEventoDAO(Connection con) {
		this.con = con;
	}

	public void updateUltimoInserido(UltimoEvento ultimoEvt) {
		String sql = "UPDATE ultimoevt SET ultimoEvtInserido = ?";
		try(PreparedStatement stmp = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
			stmp.setInt(1, ultimoEvt.getUltEvtInserido());
			stmp.execute();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public int consultaUltimoEvtInserido() {
		String sql = "SELECT ultimoEvtInserido FROM ultimoevt";
		try(PreparedStatement stmp = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
			stmp.execute();
			try(ResultSet res = stmp.getResultSet()){
				res.next();
				return res.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

}
