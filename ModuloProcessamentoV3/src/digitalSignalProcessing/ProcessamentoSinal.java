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
import jdbcDAO.SobreCorrenteDAO;
import jdbcDAO.SobreTensaoDAO;
import jdbcDAO.UltimoEventoDAO;
import modelo.Coleta;
import modelo.Consumo;
import modelo.Evento;
import modelo.Harmonica;
import modelo.Leitura;
import modelo.SobreCorrente;
import modelo.SobreTensao;
import modelo.UltimoEvento;
import programa.ModuloProcessamento;
import tratamentoSinal.Arredondar;

public class ProcessamentoSinal {
	// para pegar o valor do meio.;
	private static FFT fft = new FFT();
	private static Evento eventoAnterior = null;
	private static Integer codEventAnterior = null; // considerar passar isto
													// para dentro do modelo (de
													// Evento)

	public static void executar(Leitura leituraCapturada) throws SQLException {
		// calcula o RMS da amostra e mostra na tela
		Double correnteRMS = Arredondar.exec(CalculoRMS.calcularRMS(leituraCapturada.getValoresCorrenteLidos()), 3);
		Double tensaoRMS = Arredondar.exec(CalculoRMS.calcularRMS(leituraCapturada.getValoresTensaoLidos()), 1);
		if (ModuloProcessamento.dbValoresRMS == true) {
			System.out.println("Tensão RMS no evento: "+tensaoRMS);
			System.out.println("Corrente RMS do evento: "+correnteRMS);
		}

		// aqui vai calcular a FFT dos valores lidos
		Complex[] resFFTCorrente = fft.aplicaFFT(leituraCapturada.getValoresCorrenteLidos());
		Complex[] resFFTTensao = fft.aplicaFFT(leituraCapturada.getValoresTensaoLidos());

		try (Connection con = new MeuPoolConection().getConnection()) {
			con.setAutoCommit(false);

			// calculo do fi - resultado é armazenado na tabela do evento
			CalculoFi fi = new CalculoFi(leituraCapturada.getValoresCorrenteLidos(),
					leituraCapturada.getValoresTensaoLidos());

			Evento eventoAgora = new Evento(leituraCapturada.getHorarioLeitura(), leituraCapturada.getCodigoSensor(),
					leituraCapturada.getTipoEvento(), tensaoRMS, correnteRMS, fi.calcular());
			EventoDAO eventoDAO = new EventoDAO(con);
			Integer event_cod = eventoDAO.inserir(eventoAgora);

			// testar se houve sobrecorrente (se sim deve registrat na tabela)
			if (testarSobreCorrente(eventoAgora.getCorrenteRMS(), ModuloProcessamento.capacidadeDisjuntor)) {
				SobreCorrente sobreCorrente = new SobreCorrente(event_cod, eventoAgora.getHorarioLeitura(),
						eventoAgora.getCorrenteRMS());
				SobreCorrenteDAO sobreCorrenteDao = new SobreCorrenteDAO(con);
				sobreCorrenteDao.inserir(sobreCorrente);
			}
			// se for artefato (então tem sobretensao)
			if (leituraCapturada.getTipoEvento() == 'a') {
				SobreTensao sobretensao = new SobreTensao(event_cod, leituraCapturada.getHorarioLeitura(),tensaoRMS);
				SobreTensaoDAO sobreTensaoDao = new SobreTensaoDAO(con);
				sobreTensaoDao.insert(sobretensao);
				con.commit();
				System.out.println("Feito inserção de sobre tensão...");
			}
			ColetaDAO coletasDAO = new ColetaDAO(con);

			Coleta coletaTensao = new Coleta(event_cod, leituraCapturada.getValoresTensaoLidos());
			coletasDAO.inserirTensao(new Coleta(event_cod, leituraCapturada.getValoresTensaoLidos()));
			Coleta coletaCorrente = new Coleta(event_cod, leituraCapturada.getValoresCorrenteLidos());
			coletasDAO.inserirCorrente(coletaCorrente);

			// trabalhando as harmonicas e seus angulos
			HarmonicaDAO harmDAO = new HarmonicaDAO(con);
			Harmonica harmonicaCorrente = new Harmonica(event_cod, fft.calculaHarmonicas(resFFTCorrente),
					fft.calculaAngulos(resFFTCorrente));
			harmDAO.inserirHarmonicCorrente(harmonicaCorrente);
			Harmonica harmonicaTensao = new Harmonica(event_cod, fft.calculaHarmonicas(resFFTTensao),
					fft.calculaAngulos(resFFTTensao));
			harmDAO.inserirHarmonicTensao(harmonicaTensao);

			Consumo consumoEvento = null;
			ConsumoDAO consumoDAO = new ConsumoDAO(con);

			if (eventoAnterior != null) {
				consumoEvento = new Consumo(codEventAnterior, getEventoAnterior().getHorarioLeitura(),
						eventoAgora.getHorarioLeitura(), eventoAnterior.getKw());
				consumoDAO.inserir(consumoEvento);
				if (ModuloProcessamento.dbCalculoConsumo) {
					System.out.println("Dia: " + consumoEvento.getInicioEvento() + " delta t: "
							+ consumoEvento.getDeltaT() + " kw: " + consumoEvento.getKw());
				}
			}
			// salva evento anterior
			setEventoAnterior(eventoAgora, event_cod);

			// guarda ultimo evento inserido
			UltimoEvento ultimoEvt = new UltimoEvento(0, event_cod);
			UltimoEventoDAO ultimoEvtDao = new UltimoEventoDAO(con);
			ultimoEvtDao.updateUltimoInserido(ultimoEvt);

			con.commit();
			System.out.println("Feito inserção de evento...");
		}
	}

	private static boolean testarSobreCorrente(Double correnteRMS, int capacidadedisjuntor) {
		if (correnteRMS >= (capacidadedisjuntor * .9)) {
			return true;
		}
		return false;
	}

	/**
	 * @return the eventoAnterior
	 */
	public static Evento getEventoAnterior() {
		return eventoAnterior;
	}

	/**
	 * @param evento
	 *            the eventoAnterior to set
	 * @param event_cod
	 */
	public static void setEventoAnterior(Evento evento, Integer event_cod) {
		ProcessamentoSinal.eventoAnterior = evento;
		ProcessamentoSinal.codEventAnterior = event_cod;
	}
}
