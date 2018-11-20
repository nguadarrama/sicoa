package mx.gob.segob.dgtic.web.mvc.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import mx.gob.segob.dgtic.web.mvc.views.controller.constants.ConstantsController;

public class DiaFestivo {

	private static final Logger logger = Logger.getLogger(DiaFestivo.class);
	private Integer idDiaFestivo;
	private String nombre;
	private String fecha;
	private Boolean activo;
	private String mensaje;
	/**
	 * 
	 */
	public DiaFestivo() {
		super();
	}
	/**
	 * @param idDiaFestivo
	 * @param nombre
	 * @param fecha
	 * @param activo
	 */
	public DiaFestivo(Integer idDiaFestivo, String nombre, String fecha, Boolean activo, String mensaje) {
		super();
		this.idDiaFestivo = idDiaFestivo;
		this.nombre = nombre;
		this.fecha = fecha;
		this.activo = activo;
		this.mensaje = mensaje;
	}
	/**
	 * @return the idDiaFestivo
	 */
	public Integer getIdDiaFestivo() {
		return idDiaFestivo;
	}
	/**
	 * @param idDiaFestivo the idDiaFestivo to set
	 */
	public void setIdDiaFestivo(Integer idDiaFestivo) {
		this.idDiaFestivo = idDiaFestivo;
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
	 * @return the fecha
	 */
	public String getFecha() {
		if(fecha.length()>13){
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss z");
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		    try {
				date = sdf.parse(fecha);
				fecha = sdf1.format(date);
			} catch (ParseException e) {
				logger.warn(ConstantsController.WARN, e);
			}
		}else if(fecha.length()>10){
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		    try {
				date = sdf.parse(fecha);
				fecha = sdf1.format(date);
			} catch (ParseException e) {
				 logger.warn(ConstantsController.WARN, e);
			}
		}
		return fecha;
	}
	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
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
