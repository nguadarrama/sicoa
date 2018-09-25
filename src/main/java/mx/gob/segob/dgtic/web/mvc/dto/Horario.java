package mx.gob.segob.dgtic.web.mvc.dto;

import java.sql.Time;

public class Horario {

	private Integer idHorario;
	private Time horaEntrada;
	private Time horaSalida;
	private boolean activo;
	
	public Horario () {
		
	}
	
	public Horario(Integer idHorario, Time horaEntrada, Time horaSalida, boolean activo) {
		super();
		this.idHorario = idHorario;
		this.horaEntrada = horaEntrada;
		this.horaSalida = horaSalida;
		this.activo = activo;
	}

	public Integer getIdHorario() {
		return idHorario;
	}
	public void setIdHorario(Integer idHorario) {
		this.idHorario = idHorario;
	}
	public Time getHoraEntrada() {
		return horaEntrada;
	}
	public void setHoraEntrada(Time horaEntrada) {
		this.horaEntrada = horaEntrada;
	}
	public Time getHoraSalida() {
		return horaSalida;
	}
	public void setHoraSalida(Time horaSalida) {
		this.horaSalida = horaSalida;
	}
	public boolean getActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
}
