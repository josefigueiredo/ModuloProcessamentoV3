package br.upf.ppgca.moduloprocessamento.tratamentoSinal;

import java.util.ArrayList;
import java.util.List;

public class RemoveDC {

	public static List<Double> remover(List<Double> valores) {
		// TODO Auto-generated method stub
		double sum=0;
		List<Double> valoresSemDC = new ArrayList<Double>();
		for (Double val : valores) {
			sum += val;
		}
		//System.out.println("Soma: "+sum);
		double media = (double)sum/valores.size();
		//System.out.println("Media:"+media);
		for (Double val: valores) {
			valoresSemDC.add(val - media);
		}
		return valoresSemDC;
	}
	
	
}
