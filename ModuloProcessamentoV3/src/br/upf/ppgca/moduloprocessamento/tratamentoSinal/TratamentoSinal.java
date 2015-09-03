package br.upf.ppgca.moduloprocessamento.tratamentoSinal;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.upf.ppgca.moduloprocessamento.programa.ModuloProcessamento;
import br.upf.ppgca.moduloprocessamento.tipos.Leitura;

public class TratamentoSinal {

	public static Leitura executarTratamentos(String strRecebidaPeloSocket, double ganhoSensormA,
		double ganhoSensorA, int ganhoSensorV, int valorResistor) {
		// TODO Auto-generated method stub
		String[] partes, valoresCorrente, valoresTensao;
		String parteCorrente = null, parteTensao = null;
		int codSensorCorrente;
		char tipoEvento;
		Timestamp horarioColeta;
		
		Calendar calendario = Calendar.getInstance();
		Date agora = calendario.getTime();
		partes = strRecebidaPeloSocket.split(":");
		// l:502,491,480,471,461,:1:439,440,444,450,455,461,
		// d:502,491,480,471,461,:1:439,440,444,450,455,461,
		// 0 - > marcador que informa se ligou (l) /desligou (d) 
		// 1 -> leitura tensão
		// 2 -> codigo sensor corrente
		// 3 -> leitura corrente

		tipoEvento = partes[0].charAt(0); // primeiro campo enviado pelo arduino corresponde tipo evento l-ligado, d-desligado.
		codSensorCorrente = Integer.parseInt(partes[2].toString()); // terceiro campo enviado pelo arduino corresponde ao sensor.
		// converte para voltagem da leitura
		horarioColeta = new Timestamp(agora.getTime());
		parteTensao = partes[1].toString();
		parteCorrente = partes[3].toString();
		valoresCorrente = parteCorrente.split(",");
		valoresTensao = parteTensao.split(",");

		// converter CORRENTE para double antes de remover DC
		List<Double> valoresCorrenteOriginais = new ArrayList<Double>();
		for (String string : valoresCorrente) {
			valoresCorrenteOriginais.add(Double.parseDouble(string));
		}
		if (ModuloProcessamento.dbRawData) {
			System.out.println("RawData:" + valoresCorrenteOriginais);
		}
		// remover o nível DC Corrente (isto equivale a remover o OFFSET tmb)
		List<Double> valoresCorrenteSemDC = RemoveDC.remover(valoresCorrenteOriginais);
		if (ModuloProcessamento.dbValores) {
			System.out.println("Valores: " + valoresCorrenteSemDC);
		}

		// converter TENSAO para double antes de remover DC
		List<Double> valoresTensaoOriginais = new ArrayList<Double>();
		for (String string : valoresTensao) {
			valoresTensaoOriginais.add(Double.parseDouble(string));
		}
		if (ModuloProcessamento.dbRawData) {
			System.out.println("RawData:" + valoresTensaoOriginais);
		}
		// remover o nível DC Tensao (isto equivale a remover o OFFSET tmb)
		List<Double> valoresTensaoSemDC = RemoveDC.remover(valoresTensaoOriginais);
		if (ModuloProcessamento.dbValores) {
			System.out.println("Valores: " + valoresTensaoSemDC);
		}
		
		// converter para corrente - ajusta o ganho conforme o codigo do sensor.
		List<Double> valoresCorrenteTratados = null;
		if(codSensorCorrente == 1) {
			valoresCorrenteTratados  = Conversor.converterCorrente(valoresCorrenteSemDC, ganhoSensormA);
			if (ModuloProcessamento.dbValorCorrente == true) {
				System.out.println("Corrente: " + valoresCorrenteTratados);
			}			
		}else if(codSensorCorrente == 2) {
			valoresCorrenteTratados = Conversor.converterCorrente(valoresCorrenteSemDC, ganhoSensorA);
			if (ModuloProcessamento.dbValorCorrente == true) {
				System.out.println("Corrente: " + valoresCorrenteTratados);
			}
		}
		List<Double> valoresTensaoTratados = Conversor.converterTensao(valoresTensaoSemDC, ganhoSensorV,valorResistor);
		if (ModuloProcessamento.dbValorTensao == true) {
			System.out.println("Tensão: " + valoresTensaoTratados);
		}

		return new Leitura(codSensorCorrente, horarioColeta, tipoEvento, valoresCorrenteTratados,valoresTensaoTratados);
	}

}
