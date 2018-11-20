package mx.gob.segob.dgtic.web.mvc.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Usuario {
	
	private Integer idUsuario;
	private Integer idArea;
	private Perfil clavePerfil;
	private Horario idHorario;
	private String idPuesto;
	private String claveUsuario;
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String fechaIngreso;
	private String password;
	private Boolean activo;
	private Boolean nuevo;
	private String enSesion;
	private Date ultimoAcceso;
	private Integer numeroIntentos;
	private String bloqueado;
	private Date fechaBloqueo;
	private String primeraVez;
	private String estatus;
	private String rfc;
	private String nivel;
	private String nombreUnidad;
	private Integer idUnidad;
	private String nombreJefe;
	
	public Usuario (){
		
	}
	public Usuario (Perfil clavePerfil, Horario idHorario, String claveUsuario, String nombre,
			String apellidoPaterno, String apellidoMaterno, String estatus){
		super();
		this.clavePerfil=clavePerfil;
		this.idHorario=idHorario;
		this.claveUsuario=claveUsuario;
		this.nombre=nombre;
		this.apellidoPaterno=apellidoPaterno;
		this.apellidoMaterno=apellidoMaterno;
		this.estatus=estatus;
		}
	public Integer getIdUsuario() {
		return idUsuario;
	}
	
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public Integer getIdArea() {
		return idArea;
	}
	
	public void setIdArea(Integer idArea) {
		this.idArea = idArea;
	}
	
	public Perfil getClavePerfil() {
		return clavePerfil;
	}
	
	public void setClavePerfil(Perfil clavePerfil) {
		this.clavePerfil = clavePerfil;
	}
	
	public Horario getIdHorario() {
		return idHorario;
	}
	
	public void setIdHorario(Horario idHorario) {
		this.idHorario = idHorario;
	}
	
	public String getIdPuesto() {
		return idPuesto;
	}
	
	public void setIdPuesto(String idPuesto) {
		this.idPuesto = idPuesto;
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
	
	public String getFechaIngreso() {
	  if(fechaIngreso.length()>13){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss z");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = sdf.parse(fechaIngreso);
            fechaIngreso = sdf1.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }else if(fechaIngreso.length()>=10){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = sdf.parse(fechaIngreso);
            fechaIngreso = sdf1.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    return fechaIngreso;
	}
	
	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Boolean getActivo() {
		return activo;
	}
	
	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
	
	public Boolean getNuevo() {
		return nuevo;
	}
	
	public void setNuevo(Boolean nuevo) {
		this.nuevo = nuevo;
	}
	
	public String getEnSesion() {
		return enSesion;
	}
	
	
	public void setEnSesion(String enSesion) {
		this.enSesion = enSesion;
	}
	
	public Date getUltimoAcceso() {
		return ultimoAcceso;
	}
	
	public void setUltimoAcceso(Date ultimoAcceso) {
		this.ultimoAcceso = ultimoAcceso;
	}
	
	public Integer getNumeroIntentos() {
		return numeroIntentos;
	}
	
	public void setNumeroIntentos(Integer numeroIntentos) {
		this.numeroIntentos = numeroIntentos;
	}
	
	public String getBloqueado() {
		return bloqueado;
	}
	
	public void setBloqueado(String bloqueado) {
		this.bloqueado = bloqueado;
	}
	
	public Date getFechaBloqueo() {
		return fechaBloqueo;
	}
	
	public void setFechaBloqueo(Date fecha_bloqueo) {
		this.fechaBloqueo = fecha_bloqueo;
	}
	
	public String getPrimeraVez() {
		return primeraVez;
	}
	
	public void setPrimeraVez(String primeraVez) {
		this.primeraVez = primeraVez;
	}
	public String getEstatus() {
		return estatus;
	}
	
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	public String getNombreUnidad() {
		return nombreUnidad;
	}
	public void setNombre_unidad(String nombreUnidad) {
		this.nombreUnidad = nombreUnidad;
	}
	public Integer getIdUnidad() {
		return idUnidad;
	}
	public void setIdUnidad(Integer idUnidad) {
		this.idUnidad = idUnidad;
	}
	public String getNombreJefe() {
		return nombreJefe;
	}
	public void setNombreJefe(String nombreJefe) {
		this.nombreJefe = nombreJefe;
	}
}
