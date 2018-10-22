package mx.gob.segob.dgtic.web.mvc.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Periodo {

	private Integer idPeriodo;
	private String fechaInicio;
	private String fechaFin;
	private String descripcion;
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
		if(fechaInicio.length()>13){
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss z");
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		    try {
				date = sdf.parse(fechaInicio);
				fechaInicio = sdf1.format(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(fechaInicio.length()>10){
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		    try {
				date = sdf.parse(fechaInicio);
				fechaInicio = sdf1.format(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		if(fechaFin.length()>13){
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss z");
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		    try {
				date = sdf.parse(fechaFin);
				fechaFin = sdf1.format(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(fechaFin.length()>10){
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		    try {
				date = sdf.parse(fechaFin);
				fechaFin = sdf1.format(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	
}
