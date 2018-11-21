package mx.gob.segob.dgtic.web.mvc.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import mx.gob.segob.dgtic.web.mvc.views.controller.constants.ConstantsController;

public class Periodo {
	
	private static final Logger logger = Logger.getLogger(Periodo.class);
	private Integer idPeriodo;
	private String fechaInicio;
	private String fechaFin;
	private String descripcion;
	private Boolean activo;
	private String mensaje;
	private static final String DD_MM_YYYY = "dd-MM-yyyy";
	public Periodo(){
		
	}
	
	public Periodo(Integer idPeriodo, String fechaInicio, String fechaFin, String descripcion, Boolean activo, String mensaje) {
		super();
		this.idPeriodo = idPeriodo;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.descripcion = descripcion;
		this.activo = activo;
		this.mensaje = mensaje;
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
			SimpleDateFormat sdf1 = new SimpleDateFormat(DD_MM_YYYY);
		    try {
				date = sdf.parse(fechaInicio);
				fechaInicio = sdf1.format(date);
			} catch (ParseException e) {
				logger.warn(ConstantsController.WARN, e);
			}
		}else if(fechaInicio.length()>10){
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
			SimpleDateFormat sdf1 = new SimpleDateFormat(DD_MM_YYYY);
		    try {
				date = sdf.parse(fechaInicio);
				fechaInicio = sdf1.format(date);
			} catch (ParseException e) {
				logger.warn(ConstantsController.WARN, e);
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
			SimpleDateFormat sdf1 = new SimpleDateFormat(DD_MM_YYYY);
		    try {
				date = sdf.parse(fechaFin);
				fechaFin = sdf1.format(date);
			} catch (ParseException e) {
				logger.warn(ConstantsController.WARN, e);
			}
		}else if(fechaFin.length()>10){
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
			SimpleDateFormat sdf1 = new SimpleDateFormat(DD_MM_YYYY);
		    try {
				date = sdf.parse(fechaFin);
				fechaFin = sdf1.format(date);
			} catch (ParseException e) {
				logger.warn(ConstantsController.WARN, e);
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

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	
}
