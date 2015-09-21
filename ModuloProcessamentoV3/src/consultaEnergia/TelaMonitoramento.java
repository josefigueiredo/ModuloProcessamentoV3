package consultaEnergia;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class TelaMonitoramento extends JFrame {//implements ActionListener{
	private JPanel contentPane;
	private static SnapshotRede snapshot = new SnapshotRede();
	private Timer timer;
	private JLabel lblMsgValoresAgora = new JLabel();
	private JLabel lblVolts = new JLabel();
	private JLabel lblCorrente = new JLabel();
	private JLabel lblPotencia = new JLabel();
	private JLabel lblFatorPotencia = new JLabel();
	private JLabel lblEnergiaGastaAgora = new JLabel();
	private JLabel lblEnergiaGastaDia = new JLabel();
	private JLabel lblEnergiaGastaMes = new JLabel();
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaMonitoramento frame = new TelaMonitoramento();
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
				
		lblVolts.setText("Volts: "+" V");
		lblVolts.setBounds(30, 55, 120, 15);
		contentPane.add(lblVolts);
		
		lblCorrente.setText("Corrente: "+" A");
		lblCorrente.setBounds(200,55,150, 15);
		contentPane.add(lblCorrente);
		
		lblFatorPotencia.setText("F.P.: ");
		lblFatorPotencia.setBounds(30, 85,100, 15);
		contentPane.add(lblFatorPotencia);
		
		lblPotencia.setText("Potência: "+" VA");
		lblPotencia.setBounds(200,85,150, 15);
		contentPane.add(lblPotencia);
		
		lblEnergiaGastaAgora.setText("Consumo agora: "+" kw/h");
		lblEnergiaGastaAgora.setBounds(30, 115,250, 15);
		contentPane.add(lblEnergiaGastaAgora);
		
		lblEnergiaGastaDia.setText("Consumo no dia: "+" kw/h");
		lblEnergiaGastaDia.setBounds(30, 145,250, 15);
		contentPane.add(lblEnergiaGastaDia);
		
		lblEnergiaGastaMes.setText("Consumo no mês: "+" kw/h" );
		lblEnergiaGastaMes.setBounds(30, 175,250, 15);
		contentPane.add(lblEnergiaGastaMes);
		
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
		snapshot.atualizarConsulta(); 
		
		lblMsgValoresAgora.setText("Valores do evento "+snapshot.getUltimoEvento().getEvent_cod() +" em: "+formataData(snapshot.getUltimoEvento().getHorarioLeitura()));
		lblVolts.setText("Volts: "+formata2Decimais(snapshot.getUltimoEvento().getVoltsRMS())+" V");
		lblCorrente.setText("Corrente: "+formata2Decimais(snapshot.getUltimoEvento().getCorrenteRMS())+" A");
		lblFatorPotencia.setText("F.P.: "+formata2Decimais(snapshot.getUltimoEvento().getFi()));
		lblPotencia.setText("Potência: "+formata2Decimais(snapshot.getUltimoEvento().getPotencia())+" VA");		
		lblEnergiaGastaAgora.setText("Consumo agora: "+formata4Decimais(snapshot.getEnergiaAgora())+" kw/h");
		lblEnergiaGastaDia.setText("Consumo no dia: "+formata4Decimais(snapshot.getEnergiaDia())+" kw/h");
		lblEnergiaGastaMes.setText("Consumo no mês: "+formata4Decimais(snapshot.getEnergiaMes())+" kw/h" );
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
		
		timer = new Timer(10, timerListener);
		timer.start();
	}
	
	ActionListener timerListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			atualizaValores();
		}
		
	};
	
	/*@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == timer) {
			montaTela();
		}		
	}*/
		
}
