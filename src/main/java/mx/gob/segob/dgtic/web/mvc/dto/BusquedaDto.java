package mx.gob.segob.dgtic.web.mvc.dto;

import java.io.Serializable;

public class BusquedaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8611085951194757255L;
	
	private String claveUsuario;
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String idEstatus;
	private String idUnidad;
	private String fechaInicio;
	private String fechaFin;
	private String idPeriodo;
	public String getIdPeriodo() {
		return idPeriodo;
	}
	public void setIdPeriodo(String idPeriodo) {
		this.idPeriodo = idPeriodo;
	}
	public String getClaveUsuario() {
		return claveUsuario;
	}
	public void setClaveUsuario(String claveUsuario) {
		this.claveUsuario = claveUsuario;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidoPaterno() {
		return apellidoPaterno;
	}
	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}
	public String getApellidoMaterno() {
		return apellidoMaterno;
	}
	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}
	public String getIdEstatus() {
		return idEstatus;
	}
	public void setIdEstatus(String idEstatus) {
		this.idEstatus = idEstatus;
	}
	public String getIdUnidad() {
		return idUnidad;
	}
	public void setIdUnidad(String idUnidad) {
		this.idUnidad = idUnidad;
	}
	public String getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public String getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

}
 