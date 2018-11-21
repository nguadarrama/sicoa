package mx.gob.segob.dgtic.web.mvc.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import mx.gob.segob.dgtic.web.mvc.views.controller.constants.ConstantsController;

public class LicenciaMedica {

	private static final Logger logger = Logger.getLogger(LicenciaMedica.class);
	private Integer idLicencia;
	private Usuario idUsuario;
	private Integer idResponsable;
	private Archivo idArchivo;
	private EstatusDto idEstatus;
	private String fechaInicio;
	private String fechaFin;
	private Integer dias;
	private String padecimiento;
	private String fechaRegistro;
	private String fechaInicioAux;
	private String fechaFinAux;
	private String totalLicencias;
	private String diasTotales;
	private String mensaje;
	private String conversionCorta="MMM dd, yyyy";
	private String conversion="MMM dd, yyyy HH:mm:ss z";
	private String conversionDeseada="dd-MM-yyyy";
	
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getDiasTotales() {
		return diasTotales;
	}
	public void setDiasTotales(String diasTotales) {
		this.diasTotales = diasTotales;
	}
	public String getTotalLicencias() {
		return totalLicencias;
	}
	public void setTotalLicencias(String totalLicencias) {
		this.totalLicencias = totalLicencias;
	}
	public LicenciaMedica(){
		/**
		 * 
		 */
	}
	
	public String getFechaInicioAux() {
		return fechaInicioAux;
	}
	public void setFechaInicioAux(String fechaInicioAux) {
		this.fechaInicioAux = fechaInicioAux;
	}
	public String getFechaFinAux() {
		return fechaFinAux;
	}
	public void setFechaFinAux(String fechaFinAux) {
		this.fechaFinAux = fechaFinAux;
	}
	public Integer getIdLicencia() {
		return idLicencia;
	}
	public void setIdLicencia(Integer idLicencia) {
		this.idLicencia = idLicencia;
	}
	public Usuario getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}
	public Integer getIdResponsable() {
		return idResponsable;
	}
	public void setIdResponsable(Integer idResponsable) {
		this.idResponsable = idResponsable;
	}
	public Archivo getIdArchivo() {
		return idArchivo;
	}
	public void setIdArchivo(Archivo idArchivo) {
		this.idArchivo = idArchivo;
	}
	public EstatusDto getIdEstatus() {
		return idEstatus;
	}
	public void setIdEstatus(EstatusDto idEstatus) {
		this.idEstatus = idEstatus;
	}
	public String getFechaInicio() {
		if(fechaInicio.length()>13){
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(conversion);
			SimpleDateFormat sdf1 = new SimpleDateFormat(conversionDeseada);
		    try {
				date = sdf.parse(fechaInicio);
				fechaInicio = sdf1.format(date);
			} catch (ParseException e) {
				logger.warn(ConstantsController.WARN, e);
			}
		}else if(fechaInicio.length()>10){
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(conversionCorta);
			SimpleDateFormat sdf1 = new SimpleDateFormat(conversionDeseada);
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
			SimpleDateFormat sdf = new SimpleDateFormat(conversion);
			SimpleDateFormat sdf1 = new SimpleDateFormat(conversionDeseada);
		    try {
				date = sdf.parse(fechaFin);
				fechaFin = sdf1.format(date);
			} catch (ParseException e) {
				logger.warn(ConstantsController.WARN, e);
			}
		}else if(fechaFin.length()>10){
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(conversionCorta);
			SimpleDateFormat sdf1 = new SimpleDateFormat(conversionDeseada);
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
	public Integer getDias() {
		return dias;
	}
	public void setDias(Integer dias) {
		this.dias = dias;
	}
	public String getPadecimiento() {
		return padecimiento;
	}
	public void setPadecimiento(String padecimiento) {
		this.padecimiento = padecimiento;
	}
	public String getFechaRegistro() {
		if(fechaRegistro.length()>13){
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(conversion);
			SimpleDateFormat sdf1 = new SimpleDateFormat(conversionDeseada);
		    try {
				date = sdf.parse(fechaRegistro);
				fechaRegistro = sdf1.format(date);
			} catch (ParseException e) {
				logger.warn(ConstantsController.WARN, e);
			}
		}else if(fechaRegistro.length()>10){
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(conversionCorta);
			SimpleDateFormat sdf1 = new SimpleDateFormat(conversionDeseada);
		    try {
				date = sdf.parse(fechaRegistro);
				fechaRegistro = sdf1.format(date);
			} catch (ParseException e) {
				logger.warn(ConstantsController.WARN, e);
			}
		}
		return fechaRegistro;
	}
	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
}
