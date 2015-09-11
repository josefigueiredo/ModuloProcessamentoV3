package modelo;

public class Harmonica {

	private Integer event_cod;
	private double[] arrayHarmonicas;
	private double[] arrayAngulos;

	public Harmonica(Integer event_cod, double[] arrayHarmonicas, double[] arrayAngulos) {
		this.setEvent_cod(event_cod);
		this.setArrayHarmonicas(arrayHarmonicas);
		this.setArrayAngulos(arrayAngulos);
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
	 * @return the arrayHarmonicas
	 */
	public double[] getArrayHarmonicas() {
		return arrayHarmonicas;
	}

	/**
	 * @param arrayHarmonicas the arrayHarmonicas to set
	 */
	public void setArrayHarmonicas(double[] arrayHarmonicas) {
		this.arrayHarmonicas = arrayHarmonicas;
	}

	/**
	 * @return the arrayAngulos
	 */
	public double[] getArrayAngulos() {
		return arrayAngulos;
	}

	/**
	 * @param arrayAngulos the arrayAngulos to set
	 */
	public void setArrayAngulos(double[] arrayAngulos) {
		this.arrayAngulos = arrayAngulos;
	}

}
