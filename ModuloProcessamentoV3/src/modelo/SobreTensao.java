package modelo;

import java.sql.Timestamp;

public class SobreTensao {

	private Integer event_cod;
	private Timestamp horarioLeitura;
	private Double tensaoRMS;
	

	public SobreTensao(Integer event_cod, Timestamp horarioLeitura, Double tensaoRMS) {
		this.setEvent_cod(event_cod);
		this.setHorarioLeitura(horarioLeitura);
		this.setTensaoRMS(tensaoRMS);
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
	 * @return the tensaoRMS
	 */
	public Double getTensaoRMS() {
		return tensaoRMS;
	}

	/**
	 * @param tensaoRMS the tensaoRMS to set
	 */
	public void setTensaoRMS(Double tensaoRMS) {
		this.tensaoRMS = tensaoRMS;
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

}
