package mx.gob.segob.dgtic.web.mvc.dto;

import java.sql.Time;

public class Horario {

	private Integer idHorario;
	private String nombre;
	private Time horaEntrada;
	private Time horaSalida;
	private Boolean activo;
	private String mensaje;
	
	public Horario () {
		
	}
	
	public Horario(Integer idHorario, String nombre, Time horaEntrada, Time horaSalida, Boolean activo, String mensaje) {
		super();
		this.idHorario = idHorario;
		this.nombre = nombre;
		this.horaEntrada = horaEntrada;
		this.horaSalida = horaSalida;
		this.activo = activo;
		this.mensaje = mensaje;
	}

	/**
	 * @return the idHorario
	 */
	public Integer getIdHorario() {
		return idHorario;
	}

	/**
	 * @param idHorario the idHorario to set
	 */
	public void setIdHorario(Integer idHorario) {
		this.idHorario = idHorario;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the horaEntrada
	 */
	public Time getHoraEntrada() {
		return horaEntrada;
	}

	/**
	 * @param horaEntrada the horaEntrada to se
	 * t
	 */
	public void setHoraEntrada(Time horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	/**
	 * @return the horaSalida
	 */
	public Time getHoraSalida() {
		return horaSalida;
	}

	/**
	 * @param horaSalida the horaSalida to set
	 */
	public void setHoraSalida(Time horaSalida) {
		this.horaSalida = horaSalida;
	}

	/**
	 * @return the activo
	 */
	public Boolean getActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
}
