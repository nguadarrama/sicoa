package mx.gob.segob.dgtic.web.mvc.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.web.multipart.MultipartFile;

public class VacacionesAux {

		
	private Integer idDetalle;
	private Usuario idUsuario;
	private  VacacionPeriodo idVacacion;
	private Integer idResponsable;
	private Archivo idArchivo;
	private Estatus idEstatus;
	private String fechaInicio;
	private String fechaFin;
	private Integer dias;
	private String fechaRegistro;
    private String name;
    private String mensaje;
    
    public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public VacacionesAux(){
    	
    }
    public VacacionesAux(Usuario idUsuario, VacacionPeriodo idVacacion, Integer idResponsable, Estatus idEstatus, String fechaInicio, String fechaFin, 
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
	public Usuario getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}
	public VacacionPeriodo getIdVacacion() {
		return idVacacion;
	}
	public void setIdVacacion(VacacionPeriodo idVacacion) {
		this.idVacacion = idVacacion;
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
	public Estatus getIdEstatus() {
		return idEstatus;
	}
	public void setIdEstatus(Estatus idEstatus) {
		this.idEstatus = idEstatus;
	}
	public String getFechaInicio() {
//		System.out.println("fechaInicio "+fechaInicio);
//		fechaInicio=fechaInicio.substring(0, 21);
//		System.out.println("fechaInicio "+fechaInicio);
		if(fechaInicio.length()>13){
			Date date = null;
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", new Locale("es_ES"));
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
		//fechaFin=fechaFin.substring(0, 21);
		if(fechaFin.length()>13){
			Date date = null;
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", new Locale("es_ES"));
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
	public Integer getDias() {
		return dias;
	}
	public void setDias(Integer dias) {
		this.dias = dias;
	}
	public String getFechaRegistro() {
		System.out.println("Fecha registro "+fechaRegistro);
		fechaRegistro=fechaRegistro.substring(0, 11);
		System.out.println("Fecha registro "+fechaRegistro);
		if(fechaRegistro.length()>13){
			Date date = null;
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", new Locale("es_ES"));
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		    try {
				date = sdf.parse(fechaRegistro);
				fechaRegistro = sdf1.format(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(fechaRegistro.length()>10){
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy" , new Locale("es_ES"));
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		    try {
				date = sdf.parse(fechaRegistro);
				fechaRegistro = sdf1.format(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
