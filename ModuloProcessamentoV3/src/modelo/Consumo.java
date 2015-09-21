package modelo;

import java.sql.Timestamp;
import java.util.Date;

public class Consumo {
	private Integer event_cod;
	private Timestamp fimEvento;
	private Timestamp inicioEvento;
	private double kw;
	private double deltaT;

	public Consumo(Integer event_cod,Timestamp inicioEvento, Timestamp fimEvento, double kwEvento) {
		this.setEvent_cod(event_cod);
		this.setInicioEvento(inicioEvento);
		this.setFimEvento(fimEvento);
		this.setKw(kwEvento);
		this.setDeltaT(calculaDelta(fimEvento,inicioEvento));
		}
	
	public Consumo(Integer event_cod,Timestamp inicioEvento, Timestamp fimEvento, double kwEvento,double deltaT) {
		this(event_cod,inicioEvento,fimEvento,kwEvento);
		this.setDeltaT(calculaDelta(fimEvento,inicioEvento));
	}

	private double calculaDelta(Timestamp datahoraAgora, Timestamp datahoraAnterior) {
		long elapsed = (datahoraAgora.getTime() - datahoraAnterior.getTime());
		//esta fórmula de conversão é retirada do Mr. Google.
		return ((double)elapsed * 2.7777778E-7);
	}

	public static Object mostrar() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the kw
	 */
	public double getKw() {
		return kw;
	}


	/**
	 * @param kw the kw to set
	 */
	public void setKw(double kw) {
		this.kw = kw;
	}

	/**
	 * @return the event_cod
	 */
	public Integer getEvent_cod() {
		return event_cod;
	}

	/**
	 * @param event_cod the event_cod to set
	 */
	public void setEvent_cod(Integer event_cod) {
		this.event_cod = event_cod;
	}

	/**
	 * @return the fimEvento
	 */
	public Timestamp getFimEvento() {
		return fimEvento;
	}

	/**
	 * @param fimEvento the fimEvento to set
	 */
	public void setFimEvento(Timestamp fimEvento) {
		this.fimEvento = fimEvento;
	}

	/**
	 * @return the inicioEvento
	 */
	public Timestamp getInicioEvento() {
		return inicioEvento;
	}

	/**
	 * @param inicioEvento the inicioEvento to set
	 */
	public void setInicioEvento(Timestamp inicioEvento) {
		this.inicioEvento = inicioEvento;
	}

	/**
	 * @return the deltaT
	 */
	public double getDeltaT() {
		return deltaT;
	}

	/**
	 * @param deltaT the deltaT to set
	 */
	public void setDeltaT(double deltaT) {
		this.deltaT = deltaT;
	}

}
