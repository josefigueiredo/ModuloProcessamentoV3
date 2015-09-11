package digitalSignalProcessing;

import java.util.List;

public class CalculoFi {

	private List<Double> valoresCorrenteLidos;
	private List<Double> valoresTensaoLidos;

	public CalculoFi(List<Double> valoresCorrenteLidos, List<Double> valoresTensaoLidos) {
		this.valoresCorrenteLidos = valoresCorrenteLidos;
		this.valoresTensaoLidos = valoresTensaoLidos;
	}

	public double calcular() {
		int indiceVolts = 0, indiceCorrente = 0,diferenca=0;
		int amostras = valoresTensaoLidos.size();
		double fi,angulo;
		//testa o vetor de tensão para saber se a busca será crescente/descrecente
		String sentido = identificaSentido(valoresTensaoLidos);

		indiceVolts = buscaIndice(valoresTensaoLidos,valoresTensaoLidos.size(),sentido);
		indiceCorrente = buscaIndice(valoresCorrenteLidos,valoresCorrenteLidos.size(),sentido);
		diferenca = indiceVolts - indiceCorrente;
		diferenca = diferenca - 10; //tirei 10 da diferenca porque não sabemos de onde vem.
		
		angulo = (360*diferenca)/amostras;
		//remover estes sysouts assi que confirmar 
		System.out.println(angulo + " graus");
		fi = Math.cos(angulo);
		System.out.println(fi);
		return fi;
	}

	private String identificaSentido(List<Double> valoresTensaoLidos) {
		String t = null;
		if(valoresTensaoLidos.get(valoresTensaoLidos.size()-1) > 0) {
			t = "d";
		}else if(valoresTensaoLidos.get(valoresTensaoLidos.size()-1) < 0) {
			t =  "c";
		}
		return t;
	}

	private int buscaIndice(List<Double> valores, int tam, String sentido) {
		int posicaoAtual = tam-1;
		if(sentido.equals("d")) {
			for(int i = posicaoAtual; i>0;i--) {
				if(valores.get(i) < 0 ) {
					System.out.println("D: "+i);
					return i;
				}
			}
		}else if(sentido.equals("c")) {
			for(int i = posicaoAtual; i>0;i--) {
				if(valores.get(i) > 0 ) {
					System.out.println("C: "+i);
					return  i;
				}
			}
		}
		return 0;
	} 

}
