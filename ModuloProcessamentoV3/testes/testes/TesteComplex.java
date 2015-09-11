package testes;

import org.apache.commons.math3.complex.Complex;

public class TesteComplex {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Complex num = new Complex(60,45);
		long tempoInicial  = System.nanoTime();
		  
		System.out.println(Math.sqrt(Math.pow(num.getReal(),2) + Math.pow(num.getImaginary(),2)));
		long elpased = System.nanoTime() - tempoInicial;
		
		System.out.println("O metodo executou em "+elpased);  
		
		tempoInicial = System.nanoTime();
		System.out.println(num.abs());
		elpased = System.nanoTime() - tempoInicial;
		System.out.println("o metodo executou em "+elpased);  
		
	}

}
