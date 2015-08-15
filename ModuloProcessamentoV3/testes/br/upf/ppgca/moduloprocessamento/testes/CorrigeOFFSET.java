package br.upf.ppgca.moduloprocessamento.testes;

import java.util.ArrayList;
import java.util.List;

public class CorrigeOFFSET {

	public static List<Double> corrige(String[] valores, double offset) {
		// TODO Auto-generated method stub
		List<Double> valoresCorrigidos = new ArrayList<Double>();
		for (String val :valores) {
			valoresCorrigidos.add(Double.parseDouble(val) - offset );
		}
		return valoresCorrigidos;
	}
	

}
