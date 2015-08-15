package br.upf.ppgca.moduloprocessamento.tratamentoSinal;

import java.util.ArrayList;
import java.util.List;

public class ConverterCorrente {
	
	public static List<Double> converter(List<Double> valoresLidosSemDC,
			double ganho) {
		// TODO Auto-generated method stub
List<Double> listaTemporaria = new ArrayList<Double>();
		
 		// TODO Auto-generated method stub
		for (Double val : valoresLidosSemDC) {
			listaTemporaria.add(val / ganho); 
		}
		return listaTemporaria;
	}
	

}
