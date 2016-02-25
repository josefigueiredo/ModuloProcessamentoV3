package digitalSignalProcessing;

import java.util.List;

import programa.ModuloProcessamento;
import tratamentoSinal.AjustarNumero;


public class CalculoFi {

	private List<Double> valoresCorrenteLidos;
	private List<Double> valoresTensaoLidos;
	private Double correnteRMS;

	public CalculoFi(List<Double> valoresCorrenteLidos, List<Double> valoresTensaoLidos, Double correnteRMS) {
		this.valoresCorrenteLidos = valoresCorrenteLidos;
		this.valoresTensaoLidos = valoresTensaoLidos;
		this.correnteRMS = correnteRMS;
	}

	public double calcular() {
		int indiceTensao = 0, indiceCorrente = 0;
		int amostras = valoresTensaoLidos.size();
		double fi, defasagem;

		// testa o vetor de tensão para saber se a busca será
		// crescente/descrecente
		boolean sentido = identificaSentido(valoresTensaoLidos);

		indiceTensao = buscaIndice(valoresTensaoLidos, sentido);
		double anguloTensao = (double) (360.0 / amostras) * (indiceTensao);

		indiceCorrente = buscaIndice(valoresCorrenteLidos, sentido);
		// subtraimos 7 do indice da corrente para corrigir a defasagem de (40)
		// graus  'injetada' no sistema pelo toroide que mede a corrente
		double numeroIndicesParaDescontar = 40/(360.0 / amostras);
		double anguloCorrente = (double) (360.0 / amostras) * (indiceCorrente - numeroIndicesParaDescontar);

		if(this.correnteRMS < 0.005) {
			anguloCorrente = anguloTensao;
		}
		
		defasagem = AjustarNumero.setScale( anguloCorrente - anguloTensao, 1);

		// sysouts para debug
		System.out.print("Indice Corrente = ");
		System.out.println(indiceCorrente);
		System.out.print("Indice volts = ");
		System.out.println(indiceTensao);
		System.out.println(defasagem);
		System.out.println(Math.cos(Math.toRadians(defasagem)));
		
		return Math.cos(Math.toRadians(defasagem));
		
	}

	/**
	 * Identifica o sentido da tensão no evento. Necessária para procurar a
	 * corrente no mesmo sentido.
	 * 
	 * @param valoresTensaoLidos
	 * @return true quando sinal da tensão começa positivo (transição para o
	 *         negativo) false qando sinal da tensão começa negativo (transição
	 *         para o positivo)
	 */
	private boolean identificaSentido(List<Double> valoresTensaoLidos) {
		if (valoresTensaoLidos.get(0) > 0) {
			return true;
		} else if (valoresTensaoLidos.get(0) < 0) {
			return false;
		}
		// este return não será chamado nunca
		return false;
	}

	private int buscaIndice(List<Double> valores, boolean sentido) {
		// se sentido é true então devo buscar transiçãopara o negativo, porque
		// o sinal está positivo e vai passar para o negativo
		//retorna o indice em que sinal passou para negativo
		if (sentido) {
			for (Double val : valores) {
				if(valores.indexOf(val) + 1 == valores.size() - 1) {
					return valores.indexOf(val);
				}else if (val > 0 &&  valores.get(valores.indexOf(val)+1) < 0) {
					return valores.indexOf(val) + 1;
				}
			}
		}
		// se sentido é false então devo buscar transiçãopara o positivo, porque
		// o sinal está negativo e vai passar para o positivo
		//retorna o indice em que sinal passou para positivo
		else {
			for (Double val : valores) {
				if(valores.indexOf(val) + 1 == valores.size() - 1) {
					return valores.indexOf(val);
				}else if(val < 0  &&  valores.get(valores.indexOf(val)+1) > 0){
				//f(valores.get(valores.indexOf(val)+1)) {
					return valores.indexOf(val) + 1;
				}
			}
		}
		return 0;
	}
}
