package br.upf.ppgca.moduloprocessamento.tipos;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class Leitura {
	//nomeSenor deve identificar qual sensor foi usado para a medida (no caso poderemos ter muitos sensores)
	private int CodigoSensor;
	
	//voltagem da rede no momento da leitura
	private double volts;
		
	//este horiarioLeitura deve evoluir para um timestamp(para saber horario da leitura)
	private Timestamp  horarioLeitura = new Timestamp(0);
	//este horiarioLeitura deve evoluir para um timestamp(para saber horario da leitura)
	//private int temperaturaAmbiente;
	//leitura corresponde ao vetor de leituras enviado pelo modulo coleta
	private List<Double> leitura = new ArrayList<Double>();

	public Leitura(int sensor, Timestamp horarioLeitura, Double volts, List<Double> leitura) {
		super();
		this.setCodigoSensor(sensor);
		this.setHorarioLeitura(horarioLeitura);
		this.setVolts(volts);
		this.leitura = leitura;
	}

	public List<Double> getLeitura() {
		return leitura;
	}

	public void setLeitura(List<Double> leitura) {
		this.leitura = leitura;
	}

	public int  getCodigoSensor() {
		return CodigoSensor;
	}

	public void setCodigoSensor(int sensor) {
		this.CodigoSensor = sensor;
	}

	public Timestamp getHorarioLeitura() {
		return horarioLeitura;
	}

	public void setHorarioLeitura(Timestamp horarioLeitura2) {
		this.horarioLeitura = horarioLeitura2;
	}

	public double getVolts() {
		return volts;
	}

	public void setVolts(double volts) {
		this.volts = volts;
	}
}
