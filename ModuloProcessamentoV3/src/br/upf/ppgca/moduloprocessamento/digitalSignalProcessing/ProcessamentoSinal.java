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
import br.upf.ppgca.moduloprocessamento.programa.ModuloProcessamento;
import br.upf.ppgca.moduloprocessamento.tipos.Leitura;

public class ProcessamentoSinal {
	// para pegar o valor do meio.;
	private static FFT fft = new FFT();

	public static void executar(Leitura leituraCapturada) throws SQLException {
		// TODO Auto-generated method stub
	
		//calcula o RMS da amostra
		Double correnteRMS = CalculoRMS.calcularRMS(leituraCapturada.getValoresCorrenteLidos());
		Double tensaoRMS = CalculoRMS.calcularRMS(leituraCapturada.getValoresTensaoLidos());
		if (ModuloProcessamento.dbValoresRMS== true) {
			System.out.println(tensaoRMS);
			System.out.println(correnteRMS);		}
		
		
		// aqui vai calcular a FFT dos valores lidos
		Complex[] resFFTCorrente = fft.aplicaFFT(leituraCapturada.getValoresCorrenteLidos());
		Complex[] resFFTTensao = fft.aplicaFFT(leituraCapturada.getValoresTensaoLidos());

		// buscando achar a diferença entre o estado anteriior e o
		// estado atual = o que foi ligado/desligado
		double[] harmonicasCorrente = fft.calculaHarmonicas(resFFTCorrente);
		
		try (Connection con = new MeuPoolConection().getConnection()) {
			con.setAutoCommit(false);
			EventoDAO eventoDAO = new EventoDAO(con);
			/*Integer event_cod = eventoDAO.inserir(amostraParaFFT, correnteRMS, resultado[1]);

			HarmonicaDAO harmDAO = new HarmonicaDAO(con);
			if (diferenca != null) {
				harmDAO.inserir(event_cod, diferenca);
			} else {
				harmDAO.inserir(event_cod, harmonicasCorrente);
			}

			RmsDAO rmsDAO = new RmsDAO(con);
			rmsDAO.inserir(event_cod, CalculoRMS.getVariacaoRMS());

			// acho que é desnecessario armazenar Coletas
			ColetaDAO coletasDAO = new ColetaDAO(con);
			coletasDAO.inserir(event_cod, amostraParaFFT.getLeitura());

			// acho que é desnecessario armazenar fft_evento
			FFTDAO fftDAO = new FFTDAO(con);
			fftDAO.insere(event_cod, resFFTCorrente);*/

			con.commit();
			System.out.println("Feito inserção...");
		}
	}	
}
