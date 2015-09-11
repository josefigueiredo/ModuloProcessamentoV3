package testes;

import java.util.ArrayList;

public class SimuladorModuloColetor {
	private int amostras, freqFundamental;
	private float periodo, tAmostra;
	private ArrayList<Float> valores = new ArrayList<Float>();
	private String sensor, contTempo;

	public SimuladorModuloColetor(int amostras, int freqFundamental,
			String nome, String hora) {
		super();
		// TODO Auto-generated constructor stub
		this.amostras = amostras;
		this.freqFundamental = freqFundamental;
		this.setPeriodo();
		this.settAmostra();
		this.sensor = nome;
		this.contTempo = hora;

	}

	public String simulacao(float adc, float amplitude, Integer freq,
			float theta) {
		String simulacao = null;
		float tmpVal;

		for (int i = 0; i < this.amostras; i++) {
			tmpVal = (float) (adc + amplitude
					* Math.sin(2 * Math.PI * freq * (i * this.gettAmostra())
							+ theta));
			valores.add(tmpVal);
		}
		simulacao = sensor + ":" + contTempo + ":";
		for (Float val : valores) {
			simulacao = simulacao + val.toString() + ",";
		}
		return simulacao;
	}

	public String simuladorMultiForma2(float adc, float[] amplitude,
			int[] freq, float theta) {
		String simulacao = null;
		float tmpVal = 0;
		
		for (int comp = 0; comp <  amplitude.length; comp++) {
			for (int i = 0; i < this.amostras; i++) {
				tmpVal = (float) (adc + amplitude[comp]* Math.sin(2 * Math.PI * freq[comp]* (i * this.gettAmostra()) + theta)) + valores.get(i);
				valores.add(tmpVal);
			}
		}
		simulacao = sensor + ":" + contTempo + ":";
		for (Float val : valores) {
			simulacao = simulacao + val.toString() + ",";
		}
		return simulacao;
	}

	public String simuladorMultiForma(float adc, float[] amplitude, int[] freq,
			float theta) {
		String simulacao = null;
		float tmpVal = 0;
		for (int i = 0; i < this.amostras; i++) {
			tmpVal = (float) (adc + amplitude[0]
					* Math.sin(2 * Math.PI * freq[0] * (i * this.gettAmostra())
							+ theta))
					+ (float) (adc + amplitude[1]
							* Math.sin(2 * Math.PI * freq[1]
									* (i * this.gettAmostra()) + theta))
					+ (float) (adc + amplitude[2]
							* Math.sin(2 * Math.PI * freq[2]
									* (i * this.gettAmostra()) + theta))
					+ (float) (adc + amplitude[3]
							* Math.sin(2 * Math.PI * freq[3]
									* (i * this.gettAmostra()) + theta))
					+ (float) (adc + amplitude[4]
							* Math.sin(2 * Math.PI * freq[4]
									* (i * this.gettAmostra()) + theta))
					+ (float) (adc + amplitude[5]
							* Math.sin(2 * Math.PI * freq[5]
									* (i * this.gettAmostra()) + theta))
					+ (float) (adc + amplitude[6]
							* Math.sin(2 * Math.PI * freq[6]
									* (i * this.gettAmostra()) + theta));
			valores.add(tmpVal);
		}
		simulacao = sensor + ":" + contTempo + ":";
		for (Float val : valores) {
			simulacao = simulacao + val.toString() + ",";
		}
		return simulacao;
	}

	public Float getPeriodo() {
		return periodo;
	}

	public void setPeriodo() {
		this.periodo = (float) 1 / this.freqFundamental;
	}

	public Float gettAmostra() {
		return tAmostra;
	}

	public void settAmostra() {
		this.tAmostra = this.periodo / this.amostras;
	}

}
