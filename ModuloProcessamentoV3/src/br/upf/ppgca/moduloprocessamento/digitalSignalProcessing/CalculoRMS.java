package br.upf.ppgca.moduloprocessamento.digitalSignalProcessing;

import java.util.ArrayList;
import java.util.List;
import br.upf.ppgca.moduloprocessamento.programa.ModuloProcessamento;
import br.upf.ppgca.moduloprocessamento.tipos.Leitura;

public class CalculoRMS {
	 /**
	 * Calcular o RMS de uma leitura
	 * 
	 * @param leituraemProcessamento
	 * @return O RMS de uma leitura
	 */
	public static Double calcularRMS(List<Double> valoresLidos) {
		double sum = 0.0;
		for (Double val : valoresLidos) {
			sum += Math.pow(val, 2);
		}
		return Math.sqrt(sum) / Math.sqrt(valoresLidos.size());
	}
	
/*	*//**
	 * avaliarRMSMedidas: Avalia as amostras armazenadas na fila para saber se o
	 * erro em relação à media é maior que um lmite
	 * @param leiturasEmAnalise
	 *            : Fila com as leituras a serem analisadas
	 * @param limiteNivelErro
	 *            : Nível estabelecido para corte
	 * @return resposta: True para RMS 'bom'; False para RMS 'ruim'
	 *//*
	public static boolean analiseAmostras(List<Leitura> leiturasEmAnalise, double limiteNivelErro) {
		// TODO Auto-generated method stub
		List<Double> rmsIsolados = new ArrayList<Double>();
		double rmsMedio = 0.0;
		boolean resposta = false;
		for (Leitura leitura : leiturasEmAnalise) {
			rmsIsolados.add(CalculoRMS.calcularRMS_old(leitura));
			rmsMedio += CalculoRMS.calcularRMS_old(leitura);
		}
		rmsMedio /= leiturasEmAnalise.size();

		for (Double var : rmsIsolados) {
			double erroEncontrado = Math.abs((var - rmsMedio) / rmsMedio);
			if (Double.compare(erroEncontrado, limiteNivelErro) < 0) { // erro econtrado menor que limiteErro
				// System.out.println("Sem erro");
				ModuloProcessamento.contaAcertos++;
				resposta = true;
			} else if (Double.compare(erroEncontrado, limiteNivelErro) > 0) { // erro econtrado maior que limiteErro
				ModuloProcessamento.contaErros++;
				if (ModuloProcessamento.dbAnaliseErros) {
					System.out.printf("Amostra descartada. Encontrado erro de:  %.2f\n ", erroEncontrado*100);
					for (Leitura leitura : leiturasEmAnalise) {
						System.out.println(leitura.getValoresCorrenteLidos());
					}
				}
				//se achou um erro acima do limite encerra esta execução e jár retorna false.
				return resposta = false;
			}
		}
		return resposta;
	}

	*//**
	 * Calcular o RMS de uma leitura
	 * 
	 * @param leituraemProcessamento
	 * @return O RMS de uma leitura
	 *//*
	public static double calcularRMS_old(Leitura leituraemProcessamento) {
		List<Double> listaLeituras = leituraemProcessamento.getValoresCorrenteLidos();
		double sum = 0.0;
		for (Double val : listaLeituras) {
			sum += Math.pow(val, 2);
		}
		return Math.sqrt(sum) / Math.sqrt(listaLeituras.size());
	}

	*//**
	 * verificarGatilho: Testar se o RMS de uma Leitura sofreu modificação
	 * (conforme um limite) em relação ao RMS anteriormente calculado
	 * 
	 * @param leitura
	 *            : Leitura a ser testada
	 * @param limiteVariacaoRMS
	 *            : Limite para disparar o gatilho
	 * @return true para limite atingido; false para não atingido
	 *//*
	public static boolean[] verificarGatilho(Leitura leitura, double limiteVariacaoRMS) {
		// TODO Auto-generated method stub
		boolean resposta[] = {false,false}; //[0] - PARA CHAMAR FFT, [1]-> PARA DIZER SE LIGA/DESLIGA
		double rmsCalculado = CalculoRMS.calcularRMS_old(leitura);
		double variacao  = Math.abs(rmsCalculado - CalculoRMS.rmsAnterior);
		if (Double.compare(variacao, limiteVariacaoRMS) > 0) { // variacao e maior que limite
			if (Double.compare(rmsCalculado, CalculoRMS.rmsAnterior) > 0) {
				System.out.printf("Anterior: %.2f , Atual: %.2f , Aumentou %.2f \n", CalculoRMS.rmsAnterior, rmsCalculado, variacao);
				resposta[1] = true; //resposta[1] é true (significa que aparelho foi ligado)
			} else if (Double.compare(rmsCalculado, CalculoRMS.rmsAnterior) < 0) {
				System.out.printf("Anterior: %.2f , Atual: %.2f , Diminuiu %.2f \n", CalculoRMS.rmsAnterior, rmsCalculado, variacao);
				resposta[1] = false; //resposta[1] é false (significa que aparelho foi desligado)
			}
			resposta[0] = true;// resposta[0] retorna true (ira chamar fft)
			CalculoRMS.rmsAnterior = rmsCalculado;
		} else if (Double.compare(variacao, limiteVariacaoRMS) < 0) { // varicao é menor que limite
			resposta[0] = false; // resposta[0] retorna false (NÃO chamar fft)
			System.out.println("RMS em: "+CalculoRMS.rmsAnterior);
		}
		CalculoRMS.setVariacaoRMS(variacao);
		return resposta;
	}*/

	


	/*
	/**
	 * Método responsável pela delegação da analise de Amostras e verificação de
	 * gatilho -- este método apenas chama outros métodos para facilitar a
	 * reutilização do codigo
	 * 
	 * @param amostrasEmAnalise
	 *            : Lista de amostras que serão analisadas
	 * @param limiteNivelErro
	 *            : Erro máximo permitido para aceitar estas amostras como
	 *            válidas
	 * @param limiteVariacaoRMS
	 *            : Limite de variação de RMS para considerar um evento
	 * @return true se ambos os testes resultarem verdadeiro; false se um dos
	 *         testes resultar falso
	 */
/*	public static boolean[] analisar(List<Leitura> amostrasEmAnalise, double limiteNivelErro, double limiteVariacaoRMS) {
		// TODO Auto-generated method stub
		boolean resultado[] = {false,false};
		if (CalculoRMS.analiseAmostras(amostrasEmAnalise, limiteNivelErro)) { //metodo para testar se amostra está no limite de erro
			Leitura leituraParaTesteGatilho = amostrasEmAnalise.get((amostrasEmAnalise.size() / 2)+1); // se amostra está boa então pega a Leiutra do meio (por isso usar sempre valores impares
			resultado = CalculoRMS.verificarGatilho(leituraParaTesteGatilho, limiteVariacaoRMS); 
			
			if (resultado[1]) { // testa se esta leiutra execeu o gatilho, se sim retorna true
				return resultado;
			} else {
				return resultado;
			}
		} else {
			return resultado;
		}
	}
*/
}
