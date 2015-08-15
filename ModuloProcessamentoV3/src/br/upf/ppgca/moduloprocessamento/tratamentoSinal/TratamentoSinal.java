package br.upf.ppgca.moduloprocessamento.tratamentoSinal;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.upf.ppgca.moduloprocessamento.programa.ModuloProcessamento;
import br.upf.ppgca.moduloprocessamento.tipos.Leitura;

public class TratamentoSinal {

	public static Leitura executarTratamentos(String strRecebidaPeloSocket, double ganhoSensorFase, int ganhoDiferencial, int valorResistor) {
		// TODO Auto-generated method stub
		String[] partes, valores;
		String valor = null;
		int sensor;
		Timestamp timestamp;
		double volts;
		Calendar calendario = Calendar.getInstance();
		Date agora = calendario.getTime();
		partes = strRecebidaPeloSocket.split(":");

		sensor = Integer.parseInt(partes[0].toString()); // primeiro campo enviado pelo arduino corresponde ao sensor.
		//converte para voltagem da leitura
		volts = (double)Integer.parseInt(partes[1].toString()) /10 / ganhoDiferencial * valorResistor; // segundo campo corresponde aos volts (deve dividir por 10 para converter para double)
		timestamp = new Timestamp(agora.getTime());
		valor = partes[2].toString();
		valores = valor.split(",");
		
		if(ModuloProcessamento.dbValorTensao) {
			System.out.println("Voltagem lida: "+volts);
		}
			
		//converter para double antes de remover DC
		List<Double> valoresOriginais = new ArrayList<Double>();
		for (String string : valores) {
			valoresOriginais.add(Double.parseDouble(string));
		}
		if(ModuloProcessamento.dbRawData) {
			System.out.println("RawData:"+valoresOriginais);
		}
		//remover o nível DC (isto equivale a remover o OFFSET tmb)
		List<Double> valoresSemNivelDC = RemoveDC.remover(valoresOriginais);
		if(ModuloProcessamento.dbValores) {
			System.out.println("Valores: "+valoresSemNivelDC);	
		}
		
		//converter para corrente	
		List<Double> valoresTratados = ConverterCorrente.converter(valoresSemNivelDC,ganhoSensorFase);
		if(ModuloProcessamento.dbValorCorrente == true) {
			System.out.println("Corrente: "+valoresTratados);	
		}
		
		return new Leitura(sensor,timestamp,volts,valoresTratados);
	}


	
	
	
	

}
