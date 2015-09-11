package jdbcDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import modelo.Harmonica;

public class HarmonicaDAO {
	private Connection con;

	public HarmonicaDAO(Connection con) {
		this.con = con;
	}
	public void inserirHarmonicCorrente(Harmonica harms) {
		String sqlToColetas = "INSERT INTO harmonicasCorrente(event_cod,pos_vetor,harmonica,tetha) VALUES (?,?,?,?)";
		inserir(harms, sqlToColetas);
	}
	public void inserirHarmonicTensao(Harmonica harms) {
		String sqlToColetas = "INSERT INTO harmonicasTensao(event_cod,pos_vetor,harmonica,tetha) VALUES (?,?,?,?)";
		inserir(harms, sqlToColetas);
	}
	
	private void inserir(Harmonica harms, String sqlToColetas) {
		double[] arrayAngulos= harms.getArrayAngulos();
		int pos_vetor = 0,pos_array = 0;
		for (double h : harms.getArrayHarmonicas()) {
			try (PreparedStatement stmp = con.prepareStatement(sqlToColetas, Statement.RETURN_GENERATED_KEYS)) {
				stmp.setInt(1, harms.getEvent_cod());
				stmp.setInt(2, pos_vetor++);
				stmp.setDouble(3, h);
				stmp.setDouble(4, arrayAngulos[pos_array++]); 
				stmp.execute();
			} catch (SQLException e) {
				// TODO: handle exception
				System.out.println("Erro em HarmonicaDAO:" + e);
			}
		}
	}
	

}
