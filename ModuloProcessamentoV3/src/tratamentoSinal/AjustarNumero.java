package tratamentoSinal;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AjustarNumero{
	public static double setScale(double valor,int escala) {
		return new BigDecimal(valor).setScale(escala, RoundingMode.HALF_EVEN).doubleValue();
	}
	public static double setPrecision(double valor,int numDigitos) {
		//return new BigDecimal(valor).pr
		return new BigDecimal(valor).setScale(numDigitos, RoundingMode.HALF_EVEN).doubleValue();
	}
	
	public static double ajustaEscala(double valor) {
		int numDigitos = new BigDecimal(new Double(valor).toString()).precision();
		int escala = new BigDecimal(new Double(valor).toString()).scale();
		int parteInteira = numDigitos - escala;
		int novaEscala = escala - parteInteira;
		
		if(escala <= 2 && parteInteira > escala ) {
			novaEscala = 0;
		}
		else if(numDigitos >= 4 && escala == 4 ) {
			novaEscala-=1;
			}
		else if(numDigitos >= 5 && escala <= 2  ) {
			novaEscala-= 1;
		}
		else if(numDigitos >= 5) {
			novaEscala-= 1;
			}
		//System.out.println("novaEscala: "+novaEscala);
		return new BigDecimal(new Double(valor)).setScale(novaEscala, RoundingMode.HALF_EVEN).doubleValue();
	}

}
