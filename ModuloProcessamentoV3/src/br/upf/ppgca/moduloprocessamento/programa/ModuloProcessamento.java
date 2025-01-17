package br.upf.ppgca.moduloprocessamento.programa;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;

import br.upf.ppgca.moduloprocessamento.digitalSignalProcessing.ProcessamentoSinal;
import br.upf.ppgca.moduloprocessamento.tratamentoSinal.TratamentoSinal;

public class ModuloProcessamento extends Thread {
	public static boolean dbValores,dbValorCorrente,dbValorTensao,dbRawData,dbAnaliseErros,dbReconstucaoFFT;
	public static long  contaErros=0, contaAcertos=0;
	private Socket conexao;

	
	public static void main(String[] args) throws IOException {
		Integer PORTA = 10002;
		ModuloProcessamento.dbRawData = false; //habilita amostragem dos valores brutos lidos pelo socket
		ModuloProcessamento.dbValores = false; //habilita mostrar valores Lidos Antes da conversão
		ModuloProcessamento.dbValorCorrente = false; //habilita mostrar valores Convertidos para corrente
		ModuloProcessamento.dbValorTensao = false; //habilita mostrar valores Convertidos para corrente
		ModuloProcessamento.dbAnaliseErros = false; //habilita mostrar valores Convertidos para corrente
		ModuloProcessamento.dbReconstucaoFFT= false; //habilita mostrar valores Convertidos para corrente

		try {
			ServerSocket socketServidor = new ServerSocket(PORTA);
			System.out.println("Servidor rodando na porta: " + PORTA);

			while (true) {
				Socket newCon = socketServidor.accept();

				Thread th = new ModuloProcessamento(newCon);
				th.start();
			}
		} catch (IOException e) {
			System.out.println("IOException: " + e);
		}
	}

	public ModuloProcessamento(Socket s) {
		this.conexao = s;
	}

	public void run() {
		String strRecebidaPeloSocket;
		//aqui tenho que pegar a voltagem.... e ai tratar o ganho 
		
		
		double ganhoFaseB = 6.1;
		//este ganho foi calculado conforme planilha lendoTensão.ods [ganho = lido pelo arduino / amostrado multimetro RMS]  
		int ganhoDiferencial = 59100;  // preciso de mais precisão neste ganho
		int valorResistor = 224900;
		
		try {
			Scanner scanner = new Scanner(conexao.getInputStream());

			while (scanner.hasNext()) {
				strRecebidaPeloSocket = scanner.nextLine();
				//System.out.println(strRecebidaPeloSocket);
				//System.out.println("fator "+fatorCalculoTensao);
				
				//Tratamento do sinal retorna um tipo Leitura, que será então processado em Processamento do sinal
				ProcessamentoSinal.executar(TratamentoSinal.executarTratamentos(strRecebidaPeloSocket,ganhoFaseB, ganhoDiferencial,valorResistor));				
				
			}
			//System.out.printf("Acertos: %d, Erros: %d \n",contaAmostras,contaAcertos, contaErros);
			conexao.close();
		} catch (IOException | SQLException e) {
			System.out.println("IOException: " + e);
		}
	}
}
