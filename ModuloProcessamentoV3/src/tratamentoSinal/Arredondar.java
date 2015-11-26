package tratamentoSinal;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Arredondar {
	public static double exec(double valor,int escala) {
		return new BigDecimal(valor).setScale(escala, RoundingMode.HALF_EVEN).doubleValue();
	}

}
