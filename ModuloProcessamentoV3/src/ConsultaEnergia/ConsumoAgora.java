package ConsultaEnergia;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import jdbcDAO.EventoDAO;
import jdbcDAO.MeuPoolConection;
import jdbcDAO.UltimoLidoDAO;

public class ConsumoAgora {
	public static void main(String[] args) throws InterruptedException{
		HashMap<String,Double> ultimosValores = null;
		double tensao,corrente,fp;
		int lidoAgora = 0;
		
		//loop infinito da coisa
		while(true) {
			//limpar a tela
			for (int i = 0; i < 50; ++i) System.out.println();
			System.out.println("Programa de monitoramento de rede elétrica.... Módulo de processamento");
			System.out.println("Agora:" + getDateTime());
			
			try (Connection con = new MeuPoolConection().getConnection()) {
				//consultao ultimo registro e faz todos os calculos a partir deste ultimo evento
				ultimosValores = lerUltimosValores(con,new UltimoLidoDAO(con).ler());
				lidoAgora = (int) (ultimosValores.get("lidoAgora")/1); //o hasmap vem como double (precisei converter para integer aqui)
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tensao = ultimosValores.get("tensao");
			corrente = ultimosValores.get("corrente");
			fp = ultimosValores.get("fi");
			
			//atualiza o ultimolido na tablea globalparam
			try (Connection con = new MeuPoolConection().getConnection()) {
				UltimoLidoDAO atualizaUlimoLido = new UltimoLidoDAO(con);
				atualizaUlimoLido.atualizaUltimoLido(lidoAgora);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("-----------  Registro: " + lidoAgora + "----------------------------------");
			
			System.out.println("------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("Valores instantâneos:");
			System.out.println("Tensão: "+String.format("%.2f", tensao)+ "V");
			System.out.println("Corrente: "+String.format("%.2f", corrente)+ "A");
			System.out.println("Fator Potencia: "+String.format("%.2f", fp));
			System.out.println("------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("Consumo estimado neste momento: "+String.format("%.2f", (tensao * corrente * fp)) + "VA");
			
			

			
			
			
		Thread.sleep(1000);
		}
	}
	
	
	private static HashMap<String, Double> lerUltimosValores(Connection con, int ultimoLido) {
		EventoDAO eventoDAO = new EventoDAO(con);
		return eventoDAO.lerUltimoEvento(ultimoLido);
	}


	private static String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
}
