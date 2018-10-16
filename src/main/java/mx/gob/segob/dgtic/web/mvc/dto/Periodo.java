package mx.gob.segob.dgtic.web.mvc.dto;

import java.util.Date;

public class Periodo {

	private Integer idPeriodo;
	private String fechaInicio;
	private String fechaFin;
	private String descripcion;
//	private String fechaIni;
//	private String fechaFi;
	private Boolean activo;
	public Periodo(){
		
	}
	
	public Periodo(Integer idPeriodo, String fechaInicio, String fechaFin, String descripcion, Boolean activo) {
		super();
		this.idPeriodo = idPeriodo;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.descripcion = descripcion;
		this.activo = activo;
	} 

	public Integer getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(Integer idPeriodo) {
		this.idPeriodo = idPeriodo;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

//	public String getFechaIni() {
//		return fechaIni;
//	}
//
//	public void setFechaIni(String fechaIni) {
//		this.fechaIni = fechaIni;
//	}
//
//	public String getFechaFi() {
//		return fechaFi;
//	}
//
//	public void setFechaFi(String fechaFi) {
//		this.fechaFi = fechaFi;
//	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	
}
