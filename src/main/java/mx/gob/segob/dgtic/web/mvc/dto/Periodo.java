package mx.gob.segob.dgtic.web.mvc.dto;

import java.util.Date;

public class Periodo {

	private Integer idPeriodo;
	private Date fechaInicio;
	private Date fechaFin;
	private String fechaIni;
	private String fechaFi;
	private Boolean activo;
	public Periodo(){
		
	}
	
	public Periodo(Integer idPeriodo, Date fechaInicio, Date fechaFin, Boolean activo,String fechaIni, String fechaFi){
		
	}

	public Integer getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(Integer idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public String getFechaIni() {
		return fechaIni;
	}

	public void setFechaIni(String fechaIni) {
		this.fechaIni = fechaIni;
	}

	public String getFechaFi() {
		return fechaFi;
	}

	public void setFechaFi(String fechaFi) {
		this.fechaFi = fechaFi;
	}
}
