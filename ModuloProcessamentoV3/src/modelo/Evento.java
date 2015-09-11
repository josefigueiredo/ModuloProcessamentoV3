package modelo;

import java.sql.Timestamp;

public class Evento {

	private Timestamp horarioLeitura;
	private int codigoSensor;
	private char tipoEvento;
	private Double voltsRMS;
	private Double correnteRMS;
	private double fi;
	
	public Evento(Timestamp horarioLeitura, int codigoSensor, char tipoEvento, Double voltsRMS,
			Double correnteRMS, double fi) {
				this.setFi(fi);
				this.setHorarioLeitura(horarioLeitura);
				this.setCodigoSensor(codigoSensor);
				this.setTipoEvento(tipoEvento);
				this.setVoltsRMS(voltsRMS);
				this.setCorrenteRMS(correnteRMS);			
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
	 * @return the codigoSensor
	 */
	public int getCodigoSensor() {
		return codigoSensor;
	}

	/**
	 * @param codigoSensor the codigoSensor to set
	 */
	public void setCodigoSensor(int codigoSensor) {
		this.codigoSensor = codigoSensor;
	}

	/**
	 * @return the tipoEvento
	 */
	public char getTipoEvento() {
		return tipoEvento;
	}

	/**
	 * @param tipoEvento2 the tipoEvento to set
	 */
	public void setTipoEvento(char tipoEvento2) {
		this.tipoEvento = tipoEvento2;
	}

	/**
	 * @return the voltsRMS
	 */
	public Double getVoltsRMS() {
		return voltsRMS;
	}

	/**
	 * @param voltsRMS the voltsRMS to set
	 */
	public void setVoltsRMS(Double voltsRMS) {
		this.voltsRMS = voltsRMS;
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

	/**
	 * @return the fi
	 */
	public double getFi() {
		return fi;
	}

	/**
	 * @param fi the fi to set
	 */
	public void setFi(double fi) {
		this.fi = fi;
	}

}
