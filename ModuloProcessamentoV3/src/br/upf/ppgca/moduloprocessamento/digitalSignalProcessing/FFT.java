package br.upf.ppgca.moduloprocessamento.digitalSignalProcessing;

import java.util.List;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.*;

import br.upf.ppgca.moduloprocessamento.programa.ModuloProcessamento;
import br.upf.ppgca.moduloprocessamento.tipos.Leitura;

public class FFT {
		private int numHarmonicas = 20; //quem sabe inicializar isto por construtor??? ou outro metodo mais automatico.
		private static double[] harmonicasAnteriores = null;
		private double[] harmonicasAgora = null;
		private double[] harmonicasDiferenca = null;
	
		/**
	 * converteToDouble: Método que recebe uma leitura feita e devolve um array
	 * de double com os valores de corrente medidos.
	 * 
	 * @param valoresLidos
	 *            : é uma Leitura, que contém as medidas (armazenadas em um
	 *            List<Double>).
	 * @return: Array de double com as correntes medidas.
	 */
	private double[] converteToDouble(List<Double> valoresLidos) {
		double[] valores = new double[valoresLidos.size()];
		for (int i = 0; i < valoresLidos.size(); i++) {
			valores[i] = valoresLidos.get(i);
		}
		return valores;
	}

	/**
	 * calculaFFT: Método para calcular a FFT (modo Unitary) a partir de um
	 * array de double.
	 * 
	 * @param val
	 *            : Array de double com as medidas de corrente da leitura
	 *            escolhida.
	 * @return o retonro é um Array de números complexos - que é o resultado da
	 *         FFT.
	 */
	private Complex[] calculaFFT(double[] val) {
		FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.UNITARY);
		Complex[] result = fft.transform(val, TransformType.FORWARD);
		return result;
	}

	/**
	 * calculaHarmonicas é um metodo que recebe um array de numeros complexos e
	 * retorna as Harmonicas encontradas - o cálculo é feito conforme planilhas
	 * fornecidas pelo prof. Spalding.
	 * 
	 * @param res
	 *            : Entrada do array de numeros complexos, resultante da FFT
	 *            aplicada.
	 * @return: retorna um array de doubles com os valores das harmônicas
	 *          econtradas
	 */
	public double[] calculaHarmonicas(Complex[] res) {
		int i = 0;
		double[] resultado = new double[res.length];
		for (Complex complex : res) {
			resultado[i] = (Math.sqrt(Math.pow(complex.getReal(), 2) + Math.pow(complex.getImaginary(), 2)));
			i++;
			if(i >= this.numHarmonicas) {
				break;
			}
		}
		return resultado;
	}

	/**
	 * calculaAngulos: Retorna um array de double com o angulo de cada
	 * componente (a pedido do Spalding)
	 * 
	 * @param resFFT
	 *            : Array de complexos com o resultado da FFT.
	 * @return: array de double com os angulos de cada leitura da FFT.
	 */
	public double[] calculaAngulos(Complex[] resFFT) {
		double[] angulos = new double[resFFT.length];
		int i = 0;
		for (Complex complex : resFFT) {
			angulos[i] = (Math.atan(complex.getImaginary() / complex.getReal()));
			i++;
		}
		return angulos;
	}

	/**
	 * reconstrucaoOnda: Aplica a FFT inversa para reconstruir a forma de onda
	 * original, a partir dos dados da FFT
	 * 
	 * @param resFFT
	 *            : Array de complexos com o resultado da FFT para a leitura em
	 *            análise
	 * @return: array de double com a forma de onda reconstruida
	 */ // parece que deve ser deltado
	private FastFourierTransformer fftStandard = new FastFourierTransformer(DftNormalization.STANDARD);
	public double[] reconstrucaoOnda(Complex[] resFFT) {
		double[] ondaReconstruida = new double[resFFT.length];
		Complex[] inversa = fftStandard.transform(resFFT, TransformType.INVERSE);
		int i = 0;
		for (Complex complex : inversa) {
			ondaReconstruida[i] = (complex.getReal() + complex.getImaginary());
			i++;
		}
		return ondaReconstruida;
	}

/*	* obsoleto
 * //**
	 * Metodo que calcula a Harmonica de uma captura. 
	 * Faz uso dos métodos converteToDouble e calculaFFT e calculaHarmonicas
	 * @param leituraCapturada - é uma leitura
	 * @return harmonicasAgora - é um array de harmonicas
	 *//*
	public double[] calcHarmoncias(Leitura leituraCapturada) {
		Complex[] resFFT = calculaFFT(converteToDouble(leituraCapturada));
		this.harmonicasAgora = new double[resFFT.length];
		this.harmonicasDiferenca = new double[resFFT.length];
		this.harmonicasAgora = calculaHarmonicas(resFFT);
		return this.harmonicasAgora;
	}*/

	/**
	 * Calcula a diferença entre o conjunto de harmonicas calculado agora e o que tinha no evento anterior. 
	 * O resultado deve ser a harmônicoa do que foi ligado/desligado
	 * @return array de double com as harmonicas
	 */
	public double[] difEntreHarmonicas() {
		int i = 0;
		if(FFT.harmonicasAnteriores != null) {
			for (double harmAgora : harmonicasAgora) {
				this.harmonicasDiferenca[i] = Math.abs(harmAgora - FFT.harmonicasAnteriores[i]);
				i++;
			}
			FFT.harmonicasAnteriores = this.harmonicasAgora;
			return this.harmonicasDiferenca;
		}else {
			FFT.harmonicasAnteriores = new double[this.harmonicasAgora.length];
		}
		return null;
	}

	/**
	 * Aplicar a FFT na Leitura Caputrada
	 * 
	 * @param leituraCapturada
	 * @return
	 */
	public Complex[] aplicaFFT(List<Double> valoresLidos) {
		double[] formaOriginal = converteToDouble(valoresLidos);
		if (ModuloProcessamento.dbReconstucaoFFT) {
			System.out.println("->Original");
			for (double d : formaOriginal) {
				System.out.printf("%.2f \n", d);}
		}
		Complex[] resultadoFFT = calculaFFT(formaOriginal);
		return resultadoFFT;
	}
	
	

}
