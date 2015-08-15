package br.upf.ppgca.moduloprocessamento.testes;

import java.util.ArrayList;
import java.util.List;

import br.upf.ppgca.moduloprocessamento.tipos.Leitura;

public class DiferencaEntreLeituras {
	
	public static Leitura mostraDiferenca(Leitura atual, Leitura anterior) {
		List<Double> diferencaEntreValores = new ArrayList<Double>();
		
		for(int i=0;i<atual.getLeitura().size();i++) {
			diferencaEntreValores.add(atual.getLeitura().get(i) - anterior.getLeitura().get(i)); 
		}
		return new Leitura(atual.getCodigoSensor(),atual.getHorarioLeitura(),atual.getVolts(),diferencaEntreValores);
	}
	

}
