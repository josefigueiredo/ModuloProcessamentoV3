package consultaEnergia;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import jdbcDAO.ConsumoDAO;
import jdbcDAO.EventoDAO;
import jdbcDAO.MeuPoolConection;
import jdbcDAO.SobreCorrenteDAO;
import jdbcDAO.SobreTensaoDAO;
import jdbcDAO.UltimoEventoDAO;
import modelo.Consumo;
import modelo.Evento;

public class SnapshotRede {
	private Consumo consumoUltimoEvento;
	private double EnergiaAgora, energiaDia, energiaMes;
	private double valPicoP=270,valPicoN=-270; //valores hipotéticos (calcular um realista)
	private Evento ultimoEvento;
	private Calendar data;
	private int ultimoEvent_codAnterior = 0, contadorSobreCorrente = 0;
	private int contadorPicosTensao =0, contadorSobreTensaoP = 0,contadorSobreTensaoN = 0;

	public SnapshotRede() {
		this.consultarEnergiaDia();
		this.consultarEnergiaMes();
	}

	public void zerarAcumuladoDia() {
		setEnergiaDia(0.0);
	}

	public void zerarAcumuladoMes() {
		setEnergiaMes(0.0);
	}

	private void consultarEnergiaMes() {
		// cal representa o primeiro dia de um mes qualquer (então o SQL será do
		// primeiro dia do mês até o dia atual)
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		try (Connection con = new MeuPoolConection().getConnection()) {
			ConsumoDAO consumoDAO = new ConsumoDAO(con);
			setEnergiaMes(consumoDAO.getConsumoMes(new Timestamp(cal.getTimeInMillis())));

			SobreCorrenteDAO sobreCorrenteDao = new SobreCorrenteDAO(con);
			setContadorSobreCorrente(getContadorSobreCorrente()
					+ sobreCorrenteDao.getSobreCorrenteMes(new Timestamp(cal.getTimeInMillis())));

			//contatem dos picos de tensão
			SobreTensaoDAO sobreTensaoDao = new SobreTensaoDAO(con);
			setContadorPicosTensao(sobreTensaoDao.getSobreTensaoMes(new Timestamp(cal.getTimeInMillis())));
			setContadorSobreTensaoN(sobreTensaoDao.getSobreTensaoMes(new Timestamp(cal.getTimeInMillis()),'n',valPicoN));
			setContadorSobreTensaoP(sobreTensaoDao.getSobreTensaoMes(new Timestamp(cal.getTimeInMillis()),'p',valPicoP));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void consultarEnergiaDia() {
		try (Connection con = new MeuPoolConection().getConnection()) {
			ConsumoDAO consumoDAO = new ConsumoDAO(con);
			setEnergiaDia(consumoDAO.getConsumoDia());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void atualizarConsulta() {
		// confere data e hora para zerar contadores de dia e de mes
		Calendar now = Calendar.getInstance();

		// se agora estiver no primeiro minuto do dia zera o acumulado do dia
		zerarContadoresEnergia(now);

		try (Connection con = new MeuPoolConection().getConnection()) {
			UltimoEventoDAO ultimoevtDao = new UltimoEventoDAO(con);
			int ultimoEvent_cod = ultimoevtDao.consultaUltimoEvtInserido();

			// testa se hove mudança de codigo 
			// se sim faz novas consultas banco senão faz nada
			if (ultimoEvent_cod != ultimoEvent_codAnterior) {
				EventoDAO eventoDao = new EventoDAO(con);
				setUltimoEvento(eventoDao.getEvento(ultimoEvent_cod));

				ConsumoDAO consumoDAO = new ConsumoDAO(con);
				setConsumoUltimoEvento(consumoDAO.getConsumoAgora(ultimoEvent_cod));
				setEnergiaAgora(consumoUltimoEvento.getDeltaT() * consumoUltimoEvento.getKw());
				setEnergiaDia(getEnergiaDia() + getEnergiaAgora());
				setEnergiaMes(getEnergiaMes() + getEnergiaAgora());

				//consultar sobre tensão no último evento
				SobreTensaoDAO sobreTensaoDao = new SobreTensaoDAO(con);
				setContadorPicosTensao(getContadorPicosTensao() + sobreTensaoDao.getSobreTensaoAgora(ultimoEvent_cod));
				setContadorSobreTensaoN(getContadorSobreTensaoN() + sobreTensaoDao.getSobrePicosTensaoAgora(ultimoEvent_cod,'n',valPicoN)); 
				setContadorSobreTensaoP(getContadorSobreTensaoP() + sobreTensaoDao.getSobrePicosTensaoAgora(ultimoEvent_cod,'n',valPicoP));
				
				
				//consultar sobre corrente
				SobreCorrenteDAO sobreCorrenteDao = new SobreCorrenteDAO(con);
				setContadorSobreCorrente(getContadorSobreCorrente() + sobreCorrenteDao.getSobreCorrenteAgora(ultimoEvent_cod));
				
				ultimoEvent_codAnterior = ultimoEvent_cod;
			} else {
				// System.out.println("aguarda novo evento");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void zerarContadoresEnergia(Calendar now) {
		if (now.get(Calendar.HOUR_OF_DAY) == 0 && now.get(Calendar.MINUTE) == 0 && now.get(Calendar.SECOND) < 10) {
			zerarAcumuladoDia();
			System.out.println("Zerando contador de energia acumulada do dia");
		} else if (now.get(Calendar.DAY_OF_MONTH) == 1 && now.get(Calendar.HOUR_OF_DAY) == 0
				&& now.get(Calendar.MINUTE) == 0 && now.get(Calendar.SECOND) < 10) {
			zerarAcumuladoDia();
			zerarAcumuladoMes();
			System.out.println("Zerando contador de energia acumulada do dia e do mês");
		}
	}

	/**
	 * @return the ultimoEvento
	 */
	public Evento getUltimoEvento() {
		return ultimoEvento;
	}

	/**
	 * @param ultimoEvento
	 *            the ultimoEvento to set
	 */
	public void setUltimoEvento(Evento ultimoEvento) {
		this.ultimoEvento = ultimoEvento;
	}

	/**
	 * @return the consumoUltimoEvento
	 */
	public Consumo getConsumoUltimoEvento() {
		return consumoUltimoEvento;
	}

	/**
	 * @param consumoUltimoEvento
	 *            the consumoUltimoEvento to set
	 */
	public void setConsumoUltimoEvento(Consumo consumoUltimoEvento) {
		this.consumoUltimoEvento = consumoUltimoEvento;
	}

	/**
	 * @return the energiaAgora
	 */
	public Double getEnergiaAgora() {
		return EnergiaAgora;
	}

	/**
	 * @param energiaAgora
	 *            the energiaAgora to set
	 */
	public void setEnergiaAgora(Double energiaAgora) {
		EnergiaAgora = energiaAgora;
	}

	/**
	 * @return the data
	 */
	public Calendar getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Calendar data) {
		this.data = data;
	}

	/**
	 * @return the energiaDia
	 */
	public double getEnergiaDia() {
		return energiaDia;
	}

	/**
	 * @param energiaDia
	 *            the energiaDia to set
	 */
	public void setEnergiaDia(double energiaDia) {
		this.energiaDia = energiaDia;
	}

	/**
	 * @return the energiaMes
	 */
	public double getEnergiaMes() {
		return energiaMes;
	}

	/**
	 * @param energiaMes
	 *            the energiaMes to set
	 */
	public void setEnergiaMes(double energiaMes) {
		this.energiaMes = energiaMes;
	}

	/**
	 * @return the contadorSobreCorrente
	 */
	public int getContadorSobreCorrente() {
		return contadorSobreCorrente;
	}

	/**
	 * @param contadorSobreCorrente
	 *            the contadorSobreCorrente to set
	 */
	public void setContadorSobreCorrente(int contadorSobreCorrente) {
		this.contadorSobreCorrente = contadorSobreCorrente;
	}

	/**
	 * @return the contadorSobreTensaoN
	 */
	public int getContadorSobreTensaoN() {
		return contadorSobreTensaoN;
	}

	/**
	 * @param contadorSobreTensaoN the contadorSobreTensaoN to set
	 */
	public void setContadorSobreTensaoN(int contadorSobreTensaoN) {
		this.contadorSobreTensaoN = contadorSobreTensaoN;
	}

	/**
	 * @return the contadorSobreTensaoP
	 */
	public int getContadorSobreTensaoP() {
		return contadorSobreTensaoP;
	}

	/**
	 * @param contadorSobreTensaoP the contadorSobreTensaoP to set
	 */
	public void setContadorSobreTensaoP(int contadorSobreTensaoP) {
		this.contadorSobreTensaoP = contadorSobreTensaoP;
	}

	/**
	 * @return the contadorPicosTensao
	 */
	public int getContadorPicosTensao() {
		return contadorPicosTensao;
	}

	/**
	 * @param contadorPicosTensao the contadorPicosTensao to set
	 */
	public void setContadorPicosTensao(int contadorPicosTensao) {
		this.contadorPicosTensao = contadorPicosTensao;
	}





}
