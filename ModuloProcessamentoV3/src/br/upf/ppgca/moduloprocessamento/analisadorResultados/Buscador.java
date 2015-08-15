package br.upf.ppgca.moduloprocessamento.analisadorResultados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Buscador {

	private Connection con;

	public Buscador(Connection con) {
		this.con = con;
	}

	public Set<Map<String, Object>> porHarmonica(Double harmBuscada, double margem, int harmonica) {
		double valMin =  harmBuscada - (harmBuscada * margem);
		double valMax = harmBuscada + (harmBuscada * margem);
		Set <Map<String,Object>> identificados = new HashSet<>();
		
		String sql = "SELECT * FROM equipamento E, leitura_eq L "
				+ "WHERE e.codigo = L.codigo AND L.leitura_cod IN "
				+ "(SELECT leitura_cod FROM harm_amostra WHERE valor > ? and valor < ? AND harmonica = ?)";
		try (PreparedStatement pstmt = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setDouble(1, valMin);
			pstmt.setDouble(2, valMax);
			pstmt.setInt(3, harmonica);
			if(pstmt.execute()) {
				ResultSet result = pstmt.getResultSet();
				while(result.next()){
					HashMap<String, Object> row = new HashMap<>();
					row.put("Código", result.getInt(1));
					row.put("Nome", result.getString(2));
					row.put("Descrição", result.getString(3));
					identificados.add(row);
				}
			}else {
				return null;
			}
		} catch (SQLException e) {
			System.out.println("Erro em Buscador().porHarmonica(): " + e);
		}
		return identificados;
	}

	public Set<Map<String, Object>> porRMS(Double rmsBuscado, double margem) {
		double valMin =  rmsBuscado - (rmsBuscado * margem);
		double valMax = rmsBuscado + (rmsBuscado * margem);
		Set <Map<String,Object>> identificados = new HashSet<>();
		String sql = "SELECT * FROM equipamento "
				+ "WHERE equipamento.codigo IN "
				+ "(SELECT E.codigo FROM equipamento E, leitura_eq L "
				+ "WHERE L.codigo = E.codigo AND L.rms > ? AND L.rms < ?)";
		
		try(PreparedStatement pstmt = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
			pstmt.setDouble(1, valMin);
			pstmt.setDouble(2, valMax);
			if(pstmt.execute()) {
				ResultSet result = pstmt.getResultSet();
				while(result.next()) {
					HashMap<String, Object> row = new HashMap<>();
					row.put("Código", result.getInt(1));
					row.put("Nome", result.getString(2));
					row.put("Descrição", result.getString(3));
					identificados.add(row);
				}	
			}else {
				return null;
			}
			
		}catch (SQLException e) {
			System.out.println("Erro em Buscador().porRMS(): "+e);
		}
		// TODO Auto-generated method stub
		return identificados;
	}
	
	
	

}
