package mx.gob.segob.dgtic.web.mvc.dto;

import java.util.Date;

public class Usuario {
	
	private Integer idUsuario;
	private Integer idArea;
	private String clavePerfil;
	private Integer idHorario;
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
	
	public Usuario (){
		
	}
	public Usuario (String clavePerfil, Integer idHorario, String claveUsuario, String nombre,
			String apellidoPaterno, String apellidoMaterno, String bloqueado){
		super();
		this.clavePerfil=clavePerfil;
		this.idHorario=idHorario;
		this.claveUsuario=claveUsuario;
		this.nombre=nombre;
		this.apellidoPaterno=apellidoPaterno;
		this.apellidoMaterno=apellidoMaterno;
		this.bloqueado=bloqueado;
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
	
	public String getClavePerfil() {
		return clavePerfil;
	}
	
	public void setClavePerfil(String clavePerfil) {
		this.clavePerfil = clavePerfil;
	}
	
	public Integer getIdHorario() {
		return idHorario;
	}
	
	public void setIdHorario(Integer idHorario) {
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
}
