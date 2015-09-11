package programa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;

import jdbcDAO.MeuPoolConection;

public class FFTNosAparelhosConhecidos {
	private FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);

	public static void main(String[] args) throws SQLException {
		// conecta ao banco, pega uma FFT
		try (Connection con = new MeuPoolConection().getConnection()) {
			Statement stmtLeituraEq = con.createStatement();
			if (stmtLeituraEq.execute("select leitura_cod from leitura_eq")) {
				ResultSet resLeituraEq = stmtLeituraEq.getResultSet();
				while (resLeituraEq.next()) {
					try (PreparedStatement stmtFFT = con.prepareStatement(
							"Select * from fft_amostra where leitura_cod = ?", Statement.RETURN_GENERATED_KEYS)) {
						stmtFFT.setInt(1, resLeituraEq.getInt(1));
						if (stmtFFT.execute()) {
							ResultSet resFFT = stmtFFT.getResultSet();
							while (resFFT.next()) {
								Double harm = Math.sqrt(Math.pow(resFFT.getDouble(3), 2)
										+ Math.pow(resFFT.getDouble(4), 2));
								int harmonica = resFFT.getInt(2);
								int leitura_cod = resFFT.getInt(5);

								try (PreparedStatement stmtInsertHarmonicas = con.prepareStatement(
										"INSERT INTO harm_amostra (harmonica,valor,leitura_cod) VALUES(?,?,?)",
										Statement.RETURN_GENERATED_KEYS)) {
									stmtInsertHarmonicas.setInt(1, harmonica);
									stmtInsertHarmonicas.setDouble(2, harm);
									stmtInsertHarmonicas.setInt(3, leitura_cod);
									if(stmtInsertHarmonicas.execute()) {
										System.out.println("Inseridos "+stmtInsertHarmonicas.RETURN_GENERATED_KEYS+" registros");
									}
								}

							}
						}

					}
				}

			}

		}

	}

}
