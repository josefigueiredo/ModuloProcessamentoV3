package modelo;

import java.util.List;

public class Coleta {

	private Integer event_cod;
	private List<Double> valoresLidos;
	
	public Coleta(Integer event_cod, List<Double> valoresLidos) {
				this.setEvent_cod(event_cod);
				this.setValoresLidos(valoresLidos);
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
	 * @return the valoresLidos
	 */
	public List<Double> getValoresLidos() {
		return valoresLidos;
	}

	/**
	 * @param valoresLidos the valoresLidos to set
	 */
	public void setValoresLidos(List<Double> valoresLidos) {
		this.valoresLidos = valoresLidos;
	}

	
	
}
