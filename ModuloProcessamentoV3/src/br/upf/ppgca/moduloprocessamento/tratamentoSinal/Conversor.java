package br.upf.ppgca.moduloprocessamento.tratamentoSinal;

import java.util.ArrayList;
import java.util.List;

public class Conversor {
	public static List<Double> converterCorrente(List<Double> valoresLidosSemDC, double ganho) {
		// TODO Auto-generated method stub
		List<Double> listaTemporaria = new ArrayList<Double>();

		// TODO Auto-generated method stub
		for (Double val : valoresLidosSemDC) {
			listaTemporaria.add(val / ganho);
		}
		return listaTemporaria;
	}

	public static List<Double> converterTensao(List<Double> valoresLidosSemDC, double ganho, int valResistor) {
		// TODO Auto-generated method stub
		List<Double> listaTemporaria = new ArrayList<Double>();

		// TODO Auto-generated method stub
		for (Double val : valoresLidosSemDC) {
			listaTemporaria.add((val / ganho)*valResistor);
		}
		return listaTemporaria;
	}
}
