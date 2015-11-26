package modelo;

import java.sql.Timestamp;

public class SobreCorrente {

	private Integer event_cod;
	private Timestamp horarioLeitura;
	private Double correnteRMS;

	public SobreCorrente(Integer event_cod, Timestamp horarioLeitura, Double correnteRMS) {
		this.setEvent_cod(event_cod);
		// TODO Auto-generated constructor stub
		this.setHorarioLeitura(horarioLeitura);
		this.setCorrenteRMS(correnteRMS);
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
	 * @return the horarioLeitura
	 */
	public Timestamp getHorarioLeitura() {
		return horarioLeitura;
	}

	/**
	 * @param horarioLeitura the horarioLeitura to set
	 */
	public void setHorarioLeitura(Timestamp horarioLeitura) {
		this.horarioLeitura = horarioLeitura;
	}

	/**
	 * @return the correnteRMS
	 */
	public Double getCorrenteRMS() {
		return correnteRMS;
	}

	/**
	 * @param correnteRMS the correnteRMS to set
	 */
	public void setCorrenteRMS(Double correnteRMS) {
		this.correnteRMS = correnteRMS;
	}

}
