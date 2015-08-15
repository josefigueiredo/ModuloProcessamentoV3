package br.upf.ppgca.moduloprocessamento.tipos;
import java.util.LinkedList;
import java.util.Queue;


public class FilaLeituras{
	private static Queue<Leitura> filaLeiturasAnterioresEvt = new LinkedList<Leitura>();
	private Queue<Leitura> filaParaMediaRMS = new LinkedList<Leitura>();
	
	public Queue<Leitura> getFilaLeiturasAnterioresEvt() {
		return filaLeiturasAnterioresEvt;
	}
	public void insereFilaLeiturasAnteriores(Leitura leitura) {
		if(FilaLeituras.filaLeiturasAnterioresEvt.size() == 5) {
			FilaLeituras.filaLeiturasAnterioresEvt.remove(); //isto remove o elemento primeiro da fila
		}
		FilaLeituras.filaLeiturasAnterioresEvt.add(leitura);
	}
	
	public Leitura getUltimaLeitura(){
		return FilaLeituras.filaLeiturasAnterioresEvt.element();
	}
	public Integer getTamFila() {
		// TODO Auto-generated method stub
		return FilaLeituras.filaLeiturasAnterioresEvt.size();
	}
	public Queue<Leitura> getFilaParaMediaRMS() {
		return filaParaMediaRMS;
	}
	
	
	
	/**
	 * Insere nova leitura na fila de leituras para analise
	 * @param leitura: recebe a leitura a ser armazenada
	 */
	public void setFilaParaMediaRMS(Leitura leitura) {
		this.filaParaMediaRMS.add(leitura);
	}
	
	

		
	
}