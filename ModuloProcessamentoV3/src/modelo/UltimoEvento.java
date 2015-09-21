package modelo;

public class UltimoEvento {

	private Integer ultEvtLido;
	private Integer ultEvtInserido;

	public UltimoEvento(Integer  ultEvtLido, Integer ultEvtInserido) {
		this.setUltEvtLido(ultEvtLido);
		// TODO Auto-generated constructor stub
		this.setUltEvtInserido(ultEvtInserido);
	}

	/**
	 * @return the ultEvtLido
	 */
	public Integer getUltEvtLido() {
		return ultEvtLido;
	}

	/**
	 * @param ultEvtLido the ultEvtLido to set
	 */
	public void setUltEvtLido(Integer ultEvtLido) {
		this.ultEvtLido = ultEvtLido;
	}

	/**
	 * @return the ultEvtInserido
	 */
	public Integer getUltEvtInserido() {
		return ultEvtInserido;
	}

	/**
	 * @param ultEvtInserido the ultEvtInserido to set
	 */
	public void setUltEvtInserido(Integer ultEvtInserido) {
		this.ultEvtInserido = ultEvtInserido;
	}

}
