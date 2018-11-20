package mx.gob.segob.dgtic.web.mvc.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class Vacaciones {
	private Integer idDetalle;
	private Usuario idUsuario;
	private VacacionPeriodo idVacacion;
	private Integer idResponsable;
	private Archivo idArchivo;
	private Estatus idEstatus;
	private Date fechaInicio;
	private Date fechaFin;
	private Integer dias;
	private Date fechaRegistro;
    private String name;
    private String mensaje;
    public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	private String description;
 
    private List<MultipartFile> archivo;
	
	public Vacaciones(){
		
	}
	public Vacaciones(Usuario idUsuario, VacacionPeriodo idVacacion,  Archivo idArchivo, Integer idResponsable, Estatus idEstatus, Date fechaInicio, Date fechaFin, Integer dias){
		super();
		this.idUsuario=idUsuario;
		this.idVacacion=idVacacion;
		this.idArchivo=idArchivo;
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
	public Date getFechaInicio() {

		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		/**
		 * 
		 */
		 
	}
	public Date getFechaFin() {

		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public Integer getDias() {
		return dias;
	}
	public void setDias(Integer dias) {
		this.dias = dias;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<MultipartFile> getArchivo() {
		return archivo;
	}
	public void setArchivo(List<MultipartFile> archivo) {
		this.archivo = archivo;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
}
