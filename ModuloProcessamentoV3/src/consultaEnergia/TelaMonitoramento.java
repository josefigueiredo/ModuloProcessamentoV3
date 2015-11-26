package consultaEnergia;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import org.apache.commons.math3.util.ContinuedFraction;
import javax.swing.JTable;
import java.awt.Panel;
import java.awt.Rectangle;

public class TelaMonitoramento extends JFrame {//implements ActionListener{
	private JPanel contentPane;
	private static SnapshotRede snapshot = new SnapshotRede();
	private Timer timer;
	private JLabel lblMsgValoresAgora = new JLabel();
	private JLabel lblMsgDataHoraAgora = new JLabel();
	private JLabel lblVolts = new JLabel();
	private JLabel lblCorrente = new JLabel();
	private JLabel lblPotencia = new JLabel();
	private JLabel lblFatorPotencia = new JLabel();
	private JLabel lblEnergiaGastaDia = new JLabel();
	private JLabel lblEnergiaGastaMes = new JLabel();
	private JLabel lblContadorPicosNegativosTensao = new JLabel();
	private JLabel lblContadorPicosPositivosTensao = new JLabel();
	private JLabel lblContadorPicosTensao = new JLabel();
	private JLabel lblContadorSobreC = new JLabel();
	private JLabel lblAlertas = new JLabel();
	private SimpleDateFormat formatoData= new SimpleDateFormat("dd / MM /yyyy  HH:mm:ss");
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaMonitoramento frame = new TelaMonitoramento();
					frame.setTitle("Monitor de Consumo, Corrente e Tensão - MCCT");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected void montaTela() {
		
		//setBounds(localEmColuna, Linha, Largura, Altura)
		lblMsgValoresAgora.setText("");
		lblMsgValoresAgora.setBounds(10, 10, 400, 28);
		contentPane.add(lblMsgValoresAgora);
				
		lblVolts.setText("Tensão: "+" V");
		lblVolts.setBounds(40, 55, 120, 15);
		contentPane.add(lblVolts);
		
		lblCorrente.setText("Corrente: "+" A");
		lblCorrente.setBounds(210,55,150, 15);
		contentPane.add(lblCorrente);
		
		lblFatorPotencia.setText("Fator de Potência: ");
		lblFatorPotencia.setBounds(350,55,180, 15);
		contentPane.add(lblFatorPotencia);
		
		lblPotencia.setText("Potência: "+" VA");
		lblPotencia.setBounds(30, 115,250, 15);
		contentPane.add(lblPotencia);
		
		lblEnergiaGastaDia.setText("Consumo no dia: "+" kWh");
		lblEnergiaGastaDia.setBounds(30, 145,250, 15);
		contentPane.add(lblEnergiaGastaDia);
		
		lblEnergiaGastaMes.setText("Consumo no mês: "+" kWh" );
		lblEnergiaGastaMes.setBounds(30, 175,250, 15);
		contentPane.add(lblEnergiaGastaMes);
		
		lblMsgDataHoraAgora.setText("" );
		lblMsgDataHoraAgora.setBounds(200, 240,250, 15);
		contentPane.add(lblMsgDataHoraAgora);
		/*
		Panel panel = new Panel();
		panel.setBounds(300, 100, 280, 100);
		panel.setBackground(Color.GRAY);
		contentPane.add(panel);*/
		
		//setBounds(localEmColuna, Linha, Largura, Altura)
		lblAlertas.setText("Alertas:");
		lblAlertas.setBounds(350, 95, 100, 15);
		contentPane.add(lblAlertas);
		
		lblContadorSobreC.setText("Sobrecorrente: ");
		lblContadorSobreC.setBounds(350,115,250, 15);
		contentPane.add(lblContadorSobreC);
		
		lblContadorPicosTensao.setText("Eventos com picos: ");
		lblContadorPicosTensao.setBounds(350,135,250, 15);
		contentPane.add(lblContadorPicosTensao);
		
		lblContadorPicosNegativosTensao.setText("* Picos positivos: ");
		lblContadorPicosNegativosTensao.setBounds(370, 155,250, 15);
		contentPane.add(lblContadorPicosNegativosTensao);
		
		lblContadorPicosPositivosTensao.setText("*  Picos negativos: ");
		lblContadorPicosPositivosTensao.setBounds(370, 175,250, 15);
		contentPane.add(lblContadorPicosPositivosTensao);
	}
	
	
	/**
	 * Formata os valores para mostrar com apenas quatro casas decimais.
	 * @param val
	 * @return String formatada com duas casas decimais
	 */
	private String formata4Decimais(Double val) {
		// TODO Auto-generated method stub
		return String.format("%.4f", val);  
	}

	/**
	 * Formata os valores para mostrar com apenas duas casas decimais.
	 * @param val
	 * @return String formatada com duas casas decimais
	 */
	private String formata2Decimais(Double val) {
		// TODO Auto-generated method stub
		return String.format("%.2f", val);  
	}

	private void atualizaValores() {
		Calendar agora = Calendar.getInstance();
		
		snapshot.atualizarConsulta(); 
		
		lblMsgValoresAgora.setText("Último evento em: "+formataData(snapshot.getUltimoEvento().getHorarioLeitura()));
		lblVolts.setText("Tensão: "+formata2Decimais(snapshot.getUltimoEvento().getVoltsRMS())+" V");
		lblCorrente.setText("Corrente: "+formata2Decimais(snapshot.getUltimoEvento().getCorrenteRMS())+" A");
		lblFatorPotencia.setText("Fator de Potência: "+formata2Decimais(snapshot.getUltimoEvento().getFi()));
		lblPotencia.setText("Potência: "+formata2Decimais(snapshot.getUltimoEvento().getPotencia())+" VA");		
		lblEnergiaGastaDia.setText("Consumo no dia: "+formata4Decimais(snapshot.getEnergiaDia())+" kWh");
		lblEnergiaGastaMes.setText("Consumo no mês: "+formata4Decimais(snapshot.getEnergiaMes())+" kWh" );
		lblContadorPicosTensao.setText("Eventos com sobretensao: "+snapshot.getContadorPicosTensao());
		lblContadorPicosNegativosTensao.setText("-> picos positivos: "+snapshot.getContadorSobreTensaoP());
		lblContadorPicosPositivosTensao.setText("-> picos negativos: "+snapshot.getContadorSobreTensaoN());
		lblContadorSobreC.setText("Sobrecorrente no mês: "+snapshot.getContadorSobreCorrente());
		
		lblMsgDataHoraAgora.setText(formatoData.format(agora.getTime()));

	}

	private String formataData(Timestamp horarioLeitura) {
		// TODO Auto-generated method stub
		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(horarioLeitura);
	}

	/**
	 * Create the frame.
	 */
	public TelaMonitoramento() {
		setTitle("Monitor de Consumo de Energia");
		setSize(300, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		montaTela();
		
		timer = new Timer(100, timerListener);
		timer.start();
	}
	
	ActionListener timerListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			atualizaValores();
		}
		
	};
}
