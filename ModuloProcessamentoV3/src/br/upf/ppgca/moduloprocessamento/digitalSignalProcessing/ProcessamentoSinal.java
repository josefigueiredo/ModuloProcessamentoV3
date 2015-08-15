package br.upf.ppgca.moduloprocessamento.digitalSignalProcessing;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.complex.Complex;
import br.upf.ppgca.moduloprocessamento.jdbcDAO.ColetaDAO;
import br.upf.ppgca.moduloprocessamento.jdbcDAO.EventoDAO;
import br.upf.ppgca.moduloprocessamento.jdbcDAO.FFTDAO;
import br.upf.ppgca.moduloprocessamento.jdbcDAO.HarmonicaDAO;
import br.upf.ppgca.moduloprocessamento.jdbcDAO.MeuPoolConection;
import br.upf.ppgca.moduloprocessamento.jdbcDAO.RmsDAO;
import br.upf.ppgca.moduloprocessamento.tipos.Leitura;

public class ProcessamentoSinal {
	private static final double limiteNivelErro = 0.15; // erro de 15%
	private static final double limiteVariacaoRMS = 0.20; // limite de 200mA
	private static List<Leitura> amostrasEmAnalise = new ArrayList<Leitura>();
	private static int tamListaAmostras = 3; // utilizar sempre numeros impares
												// para pegar o valor do meio.;
	private static FFT fft = new FFT();

	public static void executar(Leitura leituraCapturada) throws SQLException {
		// TODO Auto-generated method stub
		amostrasEmAnalise.add(leituraCapturada);
		// se fila de amostra atingiu tamanho maximo então analisa as amostras
		if (amostrasEmAnalise.size() == tamListaAmostras) {
			// este if testa se a amostra passa pelo limite de erro, e se houver
			// variação de RMS
			// caso verdadeiro temos um evento real
			// caso falso, temos um erro ou um falso evento (ver
			// limiteVAriaçãoRSM

			// primeiro testa se amostra é confiável (se sim testa se houve
			// disparo de gatilho)

			if (CalculoRMS.analiseAmostras(amostrasEmAnalise, limiteNivelErro)) {
				// agora testa se houve dispardo do gatilho (para chamar FFT)
				Leitura leituraParaTesteGatilho = amostrasEmAnalise.get((amostrasEmAnalise.size() / 2) + 1);
				boolean resultado[] = CalculoRMS.verificarGatilho(leituraParaTesteGatilho, limiteVariacaoRMS);
				if (resultado[0]) { // resultdo[0] significa que houve variação
									// de RMS então o gatilho foi disparado.
									// resultado[1] é para dizer se foi
									// liga/desliga
					// aqui vai chamar a FFT
					// fft.calculaUnitary(amostrasEmAnalise.get((amostrasEmAnalise.size()/2)+1));
					Leitura amostraParaFFT = amostrasEmAnalise.get((amostrasEmAnalise.size() / 2) + 1);

					Double rmsCalculado = CalculoRMS.calcularRMS(amostraParaFFT);

					Complex[] resultadoFFT = fft.aplicaFFT(amostraParaFFT);

					// buscando achar a diferença entre o estado anteriior e o
					// estado atual = o que foi ligado/desligado
					double[] dominioFrequenca = fft.calcHarmoncias(amostraParaFFT);
					double[] diferenca = fft.difEntreHarmonicas();

					try (Connection con = new MeuPoolConection().getConnection()) {
						con.setAutoCommit(false);
						EventoDAO eventoDAO = new EventoDAO(con);
						Integer event_cod = eventoDAO.inserir(amostraParaFFT, rmsCalculado, resultado[1]);

						HarmonicaDAO harmDAO = new HarmonicaDAO(con);
						if (diferenca != null) {
							harmDAO.inserir(event_cod, diferenca);
						} else {
							harmDAO.inserir(event_cod, dominioFrequenca);
						}

						RmsDAO rmsDAO = new RmsDAO(con);
						rmsDAO.inserir(event_cod, CalculoRMS.getVariacaoRMS());

						// acho que é desnecessario armazenar Coletas
						ColetaDAO coletasDAO = new ColetaDAO(con);
						coletasDAO.inserir(event_cod, amostraParaFFT.getLeitura());

						// acho que é desnecessario armazenar fft_evento
						FFTDAO fftDAO = new FFTDAO(con);
						fftDAO.insere(event_cod, resultadoFFT);

						con.commit();
						System.out.println("Feito inserção...");
					}
				}
			}
			amostrasEmAnalise.clear();
		}
	}

}
