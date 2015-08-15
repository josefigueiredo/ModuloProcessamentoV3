package br.upf.ppgca.moduloprocessamento.analisadorResultados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.upf.ppgca.moduloprocessamento.jdbcDAO.EventoDAO;
import br.upf.ppgca.moduloprocessamento.jdbcDAO.HarmonicaDAO;
import br.upf.ppgca.moduloprocessamento.jdbcDAO.MeuPoolConection;

/**
 * Objetivo desta classe é varrer o banco de dados de leituras feitas procurando
 * por leituras que batam com a assinatura que está no banco de equipamentos.
 * 
 * @author jose
 * 
 */
public class AnalisadorResultados {
	private static int ultimoRegistro = 5;
	private Connection con;

	public static void main(String[] args) throws SQLException, InterruptedException {
		double margem = 0.1; // 10%;
		double rmsMinimo = 0.5;
		ResultSet eventosParaAnalisar;

		while (true) {
			//System.out.println("Nova rodada de análise agora: ");
			// este método é para reinciar a busca de onde parou na base de
			// leituras.
			// UltimoLidoDAO ultimoLidoDao = new UltimoLidoDAO(new
			// MeuPoolConection().getConnection());
			// ultimoRegistro = ultimoLidoDao.ler();

			try (Connection con = new MeuPoolConection().getConnection()) {
				EventoDAO eventoDAO = new EventoDAO(con);
				eventosParaAnalisar = eventoDAO.listar(ultimoRegistro,rmsMinimo);
				if (eventosParaAnalisar != null) {
					while (eventosParaAnalisar.next()) {

						int codev = eventosParaAnalisar.getInt(1);

						HarmonicaDAO harmonicDAO = new HarmonicaDAO(con);
						Double harmBuscada = harmonicDAO.getHarmonica(codev,1); //1 para pegar apenas a fundamental
						Double rmsBuscado = eventosParaAnalisar.getDouble(3);
						System.out.println();						
						System.out.print(""+eventosParaAnalisar.getInt(1) +","+eventosParaAnalisar.getTimestamp(5)+","+eventosParaAnalisar.getDouble(3)+",");
						
						// buscar no banco se tem alguem com este valore
						Buscador buscador = new Buscador(con);
						Set <Map<String,Object >> resPorHarmonica = buscador.porHarmonica(harmBuscada,margem,1);
						if(!resPorHarmonica.isEmpty()) {
							System.out.print(";harmonica; 1;");
							if(eventosParaAnalisar.getBoolean(4)) {
								//System.out.print("	;Harmonica ligado; "+resPorHarmonica.toString());
							}else {
								//System.out.print("	;Harmonica desligado; "+resPorHarmonica.toString());
							}
						}else {
							System.out.print(";harmonica; 0;");
							//System.out.print("		;Harmonica não encontrou;");
						}
						
						Set<Map<String,Object>> resPorRMS = buscador.porRMS(rmsBuscado,margem);
						if(!resPorRMS.isEmpty()) {
							System.out.print("; RMS; 1;");
							if(eventosParaAnalisar.getBoolean(4)) {
								//System.out.print("	;RMS ligado; "+resPorRMS.toString());
							}else {
								//System.out.print("	;RMS desligado; "+resPorRMS.toString());
							}
						}else {
							System.out.print("; RMS; 0;");
							//System.out.println("		;RMS não encontrou;");
						}
												// se estiver no último registro salva em ultimoRegistro
						if (eventosParaAnalisar.isLast()) {
							ultimoRegistro = eventosParaAnalisar.getInt(1);
						}
					}
				}
				// bota a trhead parada por 1 minuto
				//System.out.println("ultimo registro lido: " + ultimoRegistro);
				Thread.currentThread().sleep(1000 * 60);
				//System.out.println("Fim da rodada....");
			}
		}

	}

	private static List<Integer> buscarRMS(Double rms, Double margem) throws SQLException {
		double minRMS = rms - (rms * margem);
		double maxRMS = rms + (rms * margem);
		List<Integer> codigos = new ArrayList<>();
		// buscando sql
		String sql = "SELECT codigo FROM leitura_eq L " + "WHERE L.rms > ? AND L.rms < ?  ";

		try (Connection con = new MeuPoolConection().getConnection()) {
			try (PreparedStatement pstmt = con.prepareStatement(sql)) {
				pstmt.setDouble(1, minRMS);
				pstmt.setDouble(2, maxRMS);
				pstmt.execute();
				try (ResultSet res = pstmt.getResultSet()) {
					while (res.next()) {
						if (!codigos.contains(res.getInt(1)))
							codigos.add(res.getInt(1));
					}
				}
			}
		}
		return codigos;
	}

	private static List<Integer> buscarHarmonica(Double harmBuscada, Double margem) throws SQLException {
		double minHarm = harmBuscada - (harmBuscada * margem);
		double maxHarm = harmBuscada + (harmBuscada * margem);
		List<Integer> codigos = new ArrayList<>();
		// buscando sql
		String sql = "SELECT codigo FROM harm_amostra H,leitura_eq L " + "WHERE H.harmonica = 1  "
				+ "AND H.leitura_cod = L.leitura_cod " + "AND H.valor > ? and H.valor < ? ";
		try (Connection con = new MeuPoolConection().getConnection()) {
			try (PreparedStatement pstmt = con.prepareStatement(sql)) {
				pstmt.setDouble(1, minHarm);
				pstmt.setDouble(2, maxHarm);
				pstmt.execute();
				try (ResultSet res = pstmt.getResultSet()) {
					while (res.next()) {
						if (!codigos.contains(res.getInt(1)))
							codigos.add(res.getInt(1));
					}
				}
			}
		}
		return codigos;
	}

	/**
	 * Calcula uma harmonica de determinada leitura
	 * 
	 * @param leitura_cod
	 *            : escolha da leitura
	 * @param harmonica
	 *            : escolha da harmonica
	 * @return: valor calculado
	 * @throws SQLException
	 */
	private static Double calcHarmonica(int leitura_cod, int harmonica) throws SQLException {
		Double resultado = null;
		try (Connection con = new MeuPoolConection().getConnection()) {
			try (PreparedStatement pstmt = con
					.prepareStatement("select * from fft where leitura_cod_fk = ? and pos_vetor = ?")) {
				pstmt.setInt(1, leitura_cod);
				pstmt.setInt(2, harmonica);
				pstmt.execute();
				ResultSet res = pstmt.getResultSet();
				while (res.next()) {
					Double cos = res.getDouble(4);
					Double sen = res.getDouble(3);
					resultado = Math.sqrt(Math.pow(sen, 2) + Math.pow(cos, 2));
				}
			}
		}
		return resultado;
	}
}
