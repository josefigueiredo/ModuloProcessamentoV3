package br.upf.ppgca.moduloprocessamento.tipos;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class Leitura {
	//nomeSenor deve identificar qual sensor foi usado para a medida (no caso poderemos ter muitos sensores)
	private int CodigoSensor;
	private char tipoEvento;
	//este horiarioLeitura deve evoluir para um timestamp(para saber horario da leitura)
	private Timestamp  horarioLeitura = new Timestamp(0);
	//este horiarioLeitura deve evoluir para um timestamp(para saber horario da leitura)
	//private int temperaturaAmbiente;
	//leitura corresponde ao vetor de leituras enviado pelo modulo coleta
	private List<Double> valoresCorrenteLidos = new ArrayList<Double>();
	private List<Double> valoresTensaoLidos = new ArrayList<Double>();

	public Leitura(int sensor, Timestamp horarioLeitura, char tipoEvento, List<Double> valoresCorrenteTratados, List<Double> valoresTensaoTratados) {
		super();
		this.setCodigoSensor(sensor);
		this.setHorarioLeitura(horarioLeitura);
		this.setTipoEvento(tipoEvento);
		this.setValoresCorrenteLidos(valoresCorrenteTratados);
		this.setValoresTensaoLidos(valoresTensaoTratados);
	}

	public List<Double> getValoresCorrenteLidos() {
		return valoresCorrenteLidos;
	}

	public void setValoresCorrenteLidos(List<Double> leitura) {
		this.valoresCorrenteLidos = leitura;
	}

	public List<Double> getValoresTensaoLidos() {
		return valoresTensaoLidos;
	}

	public void setValoresTensaoLidos(List<Double> leitura) {
		this.valoresTensaoLidos = leitura;
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

	public char getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(char tipoEvento) {
		this.tipoEvento = tipoEvento;
	}
	}
