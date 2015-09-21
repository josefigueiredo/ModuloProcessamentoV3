package digitalSignalProcessing;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.math3.complex.Complex;

import jdbcDAO.ColetaDAO;
import jdbcDAO.ConsumoDAO;
import jdbcDAO.EventoDAO;
import jdbcDAO.FFTDAO;
import jdbcDAO.HarmonicaDAO;
import jdbcDAO.MeuPoolConection;
import jdbcDAO.RmsDAO;
import jdbcDAO.UltimoEventoDAO;
import modelo.Coleta;
import modelo.Consumo;
import modelo.Evento;
import modelo.Harmonica;
import modelo.Leitura;
import modelo.UltimoEvento;
import programa.ModuloProcessamento;

public class ProcessamentoSinal {
	// para pegar o valor do meio.;
	private static FFT fft = new FFT();
	private static Evento eventoAnterior = null; 
	private static Integer codEventAnterior = null; //considerar passar isto para dentro do modelo (de Evento)
	//private static Timestamp datahoraAnterior = null;

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
			
			Evento eventoAgora = new Evento(leituraCapturada.getHorarioLeitura(),leituraCapturada.getCodigoSensor(),leituraCapturada.getTipoEvento(),tensaoRMS,correnteRMS, fi.calcular());
			EventoDAO eventoDAO = new EventoDAO(con);
			Integer event_cod = eventoDAO.inserir(eventoAgora);
			
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
			
			Consumo consumoEvento = null;
			ConsumoDAO consumoDAO = new ConsumoDAO(con);
			
			if(eventoAnterior != null) {
				consumoEvento = new Consumo(codEventAnterior,getEventoAnterior().getHorarioLeitura(), eventoAgora.getHorarioLeitura(), eventoAnterior.getKw());
				consumoDAO.inserir(consumoEvento);
				
				System.out.println("Dia: "+consumoEvento.getInicioEvento()+ " delta t: "+consumoEvento.getDeltaT()+" kw: "+consumoEvento.getKw());
			}
			//salva evento anterior
			setEventoAnterior(eventoAgora,event_cod);	
			
			//guarda ultimo evento inserido
			UltimoEvento ultimoEvt = new UltimoEvento(0,event_cod);
			UltimoEventoDAO ultimoEvtDao = new UltimoEventoDAO( con);
			ultimoEvtDao.updateUltimoInserido(ultimoEvt);
		
			con.commit();
			System.out.println("Feito inserção...");
		} 
	}

//	/**
//	 * @return the datahoraAnterior
//	 */
//	public static Timestamp getDatahoraAnterior() {
//		return datahoraAnterior;
//	}
//
//	/**
//	 * @param datahoraNova the datahoraAnterior to set
//	 */
//	public static void setDatahoraAnterior(Timestamp datahoraNova) {
//		ProcessamentoSinal.datahoraAnterior = datahoraNova;
//	}

	/**
	 * @return the eventoAnterior
	 */
	public static Evento getEventoAnterior() {
		return eventoAnterior;
	}

	/**
	 * @param evento the eventoAnterior to set
	 * @param event_cod 
	 */
	public static void setEventoAnterior(Evento evento, Integer event_cod) {
		ProcessamentoSinal.eventoAnterior = evento;
		ProcessamentoSinal.codEventAnterior = event_cod;
	}
}
