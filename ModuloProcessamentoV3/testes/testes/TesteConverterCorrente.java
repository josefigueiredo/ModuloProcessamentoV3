package testes;

import java.io.ObjectInputStream.GetField;

public class TesteConverterCorrente {
	private static float ganho = (float) 443469.38;

	public static void main(String[] args) {
		int valorLeituras[] = new int[] { 409, 399, 391, 384, 376, 367, 358, 351, 344, 335, 327, 323, 319, 315, 312,
				307, 305, 302, 300, 299, 300, 300, 301, 304, 307, 313, 318, 322, 327, 330, 335, 337, 345, 355, 363,
				371, 380, 388, 396, 404, 411, 418, 425, 432, 435, 440, 443, 446, 449, 452, 455, 455, 455, 454, 454,
				451, 447, 441, 436, 432, 428, 423, 420, 416 };
		float[] valorSemDC = new float[valorLeituras.length];
		float[] convertido = new float[valorLeituras.length];
		eliminaDC(valorLeituras, valorSemDC);
		converterParaCorrente(valorSemDC,convertido);
		for (float f : convertido) {
			System.out.println(f);	
		}
		
	}

	private static void converterParaCorrente(float[] valorSemDC, float[] convertido) {
		// TODO Auto-generated method stub
		for(int i=0;i<valorSemDC.length;i++) {
			convertido[i] = valorSemDC[i]/ getGanho();
		}
	}

	private static void eliminaDC(int[] valorLeituras, float[] valorSemDC) {
		// TODO Auto-generated method stub
		int acc = 0;
		float media = (float) 0.0;
		for (int i : valorLeituras) {
			acc += i;
		}
		media = (float) acc / valorLeituras.length;
		for (int i = 0; i < valorLeituras.length; i++) {
			valorSemDC[i] = (float) valorLeituras[i] - media;
		}
		
	}

	public static float getGanho() {
		return ganho;
	}

	public void setGanho(float ganho) {
		this.ganho = ganho;
	}
}
