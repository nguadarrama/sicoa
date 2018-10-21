package mx.gob.segob.dgtic.web.mvc.dto;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class VacacionesAux {

		
	private Integer idDetalle;
	private Integer idUsuario;
	private Integer idVacacion;
	private Integer idResponsable;
	private Integer idArchivo;
	private Integer idEstatus;
	private String fechaInicio;
	private String fechaFin;
	private Integer dias;
	private String fechaRegistro;
    private String name;
    
    public VacacionesAux(){
    	
    }
    public VacacionesAux(Integer idUsuario, Integer idVacacion, Integer idResponsable, Integer idEstatus, String fechaInicio, String fechaFin, 
    	Integer dias	){
    	this.idUsuario = idUsuario;
    	this.idVacacion=idVacacion;
    	this.idResponsable=idResponsable;
    	this.idEstatus=idEstatus;
    	this.fechaInicio=fechaInicio;
    	this.fechaFin=fechaFin;
    	this.dias=dias;
    	
    }
	public Integer getIdDetalle() {
		return idDetalle;
	}
	public void setIdDetalle(Integer idDetalle) {
		this.idDetalle = idDetalle;
	}
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	public Integer getIdVacacion() {
		return idVacacion;
	}
	public void setIdVacacion(Integer idVacacion) {
		this.idVacacion = idVacacion;
	}
	public Integer getIdResponsable() {
		return idResponsable;
	}
	public void setIdResponsable(Integer idResponsable) {
		this.idResponsable = idResponsable;
	}
	public Integer getIdArchivo() {
		return idArchivo;
	}
	public void setIdArchivo(Integer idArchivo) {
		this.idArchivo = idArchivo;
	}
	public Integer getIdEstatus() {
		return idEstatus;
	}
	public void setIdEstatus(Integer idEstatus) {
		this.idEstatus = idEstatus;
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
	public Integer getDias() {
		return dias;
	}
	public void setDias(Integer dias) {
		this.dias = dias;
	}
	public String getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
