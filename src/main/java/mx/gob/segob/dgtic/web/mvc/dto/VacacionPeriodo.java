package mx.gob.segob.dgtic.web.mvc.dto;

import java.util.Date;

public class VacacionPeriodo {
	private Integer idVacacion;
	private Usuario idUsuario;
	private Periodo idPeriodo;
	private Estatus idEstatus;
	private Date fechaInicio;
	private Integer dias;
	private Boolean activo;
	public Integer getIdVacacion() {
		return idVacacion;
	}
	public void setIdVacacion(Integer idVacacion) {
		this.idVacacion = idVacacion;
	}
	public Usuario getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}
	public Periodo getIdPeriodo() {
		return idPeriodo;
	}
	public void setIdPeriodo(Periodo idPeriodo) {
		this.idPeriodo = idPeriodo;
	}
	public Estatus getIdEstatus() {
		return idEstatus;
	}
	public void setIdEstatus(Estatus idEstatus) {
		this.idEstatus = idEstatus;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Integer getDias() {
		return dias;
	}
	public void setDias(Integer dias) {
		this.dias = dias;
	}
	public Boolean getActivo() {
		return activo;
	}
	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
	

}
