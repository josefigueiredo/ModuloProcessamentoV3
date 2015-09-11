package digitalSignalProcessing;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.complex.Complex;

import jdbcDAO.ColetaDAO;
import jdbcDAO.EventoDAO;
import jdbcDAO.FFTDAO;
import jdbcDAO.HarmonicaDAO;
import jdbcDAO.MeuPoolConection;
import jdbcDAO.RmsDAO;
import modelo.Coleta;
import modelo.Evento;
import modelo.Harmonica;
import modelo.Leitura;
import programa.ModuloProcessamento;

public class ProcessamentoSinal {
	// para pegar o valor do meio.;
	private static FFT fft = new FFT();

	public static void executar(Leitura leituraCapturada) throws SQLException {
		//calcula o RMS da amostra e mostra na tela
		Double correnteRMS = CalculoRMS.calcularRMS(leituraCapturada.getValoresCorrenteLidos());
		Double tensaoRMS = CalculoRMS.calcularRMS(leituraCapturada.getValoresTensaoLidos());
		if (ModuloProcessamento.dbValoresRMS== true) {
			System.out.println(tensaoRMS);
			System.out.println(correnteRMS);		}
		
		// aqui vai calcular a FFT dos valores lidos
		Complex[] resFFTCorrente = fft.aplicaFFT(leituraCapturada.getValoresCorrenteLidos());
		Complex[] resFFTTensao = fft.aplicaFFT(leituraCapturada.getValoresTensaoLidos());
		
		try (Connection con = new MeuPoolConection().getConnection()) {
			con.setAutoCommit(false);
			//calculo do fi - resultado é armazenado na tabela do evento
			CalculoFi fi =  new CalculoFi(leituraCapturada.getValoresCorrenteLidos(),leituraCapturada.getValoresTensaoLidos());
			
			Evento evento = new Evento(leituraCapturada.getHorarioLeitura(),leituraCapturada.getCodigoSensor(),leituraCapturada.getTipoEvento(),tensaoRMS,correnteRMS, fi.calcular());
			EventoDAO eventoDAO = new EventoDAO(con);
			Integer event_cod = eventoDAO.inserir(evento);
			
			ColetaDAO coletasDAO = new ColetaDAO(con);
			
			Coleta coletaTensao = new Coleta(event_cod,leituraCapturada.getValoresTensaoLidos());
			coletasDAO.inserirTensao(new Coleta(event_cod,leituraCapturada.getValoresTensaoLidos()));
			Coleta coletaCorrente = new Coleta(event_cod,leituraCapturada.getValoresCorrenteLidos());
			coletasDAO.inserirCorrente(coletaCorrente);
			
			//trabalhando as harmonicas e seus angulos
			HarmonicaDAO harmDAO = new HarmonicaDAO(con);
			Harmonica harmonicaCorrente = new Harmonica(event_cod,fft.calculaHarmonicas(resFFTCorrente), fft.calculaAngulos(resFFTCorrente) );
			harmDAO.inserirHarmonicCorrente(harmonicaCorrente);
			Harmonica harmonicaTensao = new Harmonica(event_cod,fft.calculaHarmonicas(resFFTTensao), fft.calculaAngulos(resFFTTensao) );
			harmDAO.inserirHarmonicTensao(harmonicaTensao);
			
			con.commit();
			System.out.println("Feito inserção...");
		}
	}
}
