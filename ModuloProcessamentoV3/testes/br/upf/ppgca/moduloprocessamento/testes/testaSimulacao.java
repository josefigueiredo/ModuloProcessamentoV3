package br.upf.ppgca.moduloprocessamento.testes;

public class testaSimulacao {
	public static void main(String[] args) {
		SimuladorModuloColetor s1 = new SimuladorModuloColetor(64,60,"2","60");
		SimuladorModuloColetor s2 = new SimuladorModuloColetor(64,60,"2","120");
		SimuladorModuloColetor s3 = new SimuladorModuloColetor(64,60,"2","60+180+300");
		try{
			System.out.println(s1.simulacao((float)0.0, (float)0.45,60,(float)0.0));
			System.out.println(s2.simulacao((float)0.0, (float)0.45,120,(float)0.0));
			
			
			float amplitude[] = {(float)0.2,(float)0.15,(float)0.1,(float)0.0,(float)0.0,(float)0.0,(float)0.0};
			int freq[] = {60,180,300,0,0,0,0};
			System.out.println(s3.simuladorMultiForma((float) 0.0, amplitude, freq, (float) 0.0));
			
		}catch(Exception e){
			System.out.println("Erro: "+e);
		}	
	}
	
}
