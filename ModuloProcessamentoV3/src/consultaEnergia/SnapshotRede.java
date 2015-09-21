package consultaEnergia;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import jdbcDAO.ConsumoDAO;
import jdbcDAO.EventoDAO;
import jdbcDAO.MeuPoolConection;
import jdbcDAO.UltimoEventoDAO;
import modelo.Consumo;
import modelo.Evento;

public class SnapshotRede {
	private Consumo consumoUltimoEvento;
	private double EnergiaAgora, energiaDia, energiaMes;
	private Evento ultimoEvento;
	private Calendar data;
	private int ultimoEvent_codAnterior =0;

	public SnapshotRede() {
		this.consultarEnergiaDia();
		this.consultarEnergiaMes();
	}

	private void consultarEnergiaMes() {
		Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.DAY_OF_MONTH, 1);
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    
		try (Connection con = new MeuPoolConection().getConnection()) {
			ConsumoDAO consumoDAO = new ConsumoDAO(con);
			setEnergiaMes(consumoDAO.getConsumoMes(new Timestamp(cal.getTimeInMillis())));
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
		try (Connection con = new MeuPoolConection().getConnection()) {
			UltimoEventoDAO ultimoevtDao = new UltimoEventoDAO(con);
			int ultimoEvent_cod = ultimoevtDao.consultaUltimoEvtInserido();
			
			// testa se hove mudança de codigo se sim faz novas consultas ao banco senão faz nada
			if (ultimoEvent_cod != ultimoEvent_codAnterior) {
				EventoDAO eventoDao = new EventoDAO(con);
				setUltimoEvento(eventoDao.getEvento(ultimoEvent_cod));

				ConsumoDAO consumoDAO = new ConsumoDAO(con);
				setConsumoUltimoEvento(consumoDAO.getConsumoAgora(ultimoEvent_cod));
				setEnergiaAgora(consumoUltimoEvento.getDeltaT() * consumoUltimoEvento.getKw());
				setEnergiaDia(getEnergiaDia() + getEnergiaAgora());
				setEnergiaMes(getEnergiaMes() + getEnergiaAgora());
				
				ultimoEvent_codAnterior = ultimoEvent_cod;
			}else {
				//System.out.println("aguarda novo evento");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
}
