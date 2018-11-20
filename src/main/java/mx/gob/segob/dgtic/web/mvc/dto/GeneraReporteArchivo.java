
package mx.gob.segob.dgtic.web.mvc.dto;

import java.io.Serializable;

public class GeneraReporteArchivo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3939434980461556712L;
	private String idsolicitud;
	private String idEstatus;
	private String idPuesto;
	private String unidadAdministrativa;
	private String numeroEmpleado;
	private String fechaIngreso;
	private String rfc;
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String fechaInicio; 
	private String fechaFin; 
	private String dias; 
	private String responsable;
	private String idVacacion;
	private String fechaPeticion;
	

	/**
	 * 
	 */
	public GeneraReporteArchivo() {
		super();

	}
	public GeneraReporteArchivo(String idsolicitud,String idEstatus,String idPuesto,String unidadAdministrativa,String numeroEmpleado,
			String fechaIngreso,String rfc,String nombre,String apellidoPaterno,String apellidoMaterno, String fechaInicio, String fechaFin, 
			String dias, String responsable, String idVacacion, String fechaPeticion){
		super();
		this.idsolicitud = idsolicitud;
		this.idEstatus = idEstatus;
		this.idPuesto = idPuesto;
		this.unidadAdministrativa = unidadAdministrativa;
		this.numeroEmpleado=numeroEmpleado;
		this.fechaIngreso=fechaIngreso;
		this.fechaFin=fechaFin;
		this.rfc=rfc;
		this.nombre=nombre;
		this.apellidoMaterno=apellidoMaterno;
		this.apellidoPaterno=apellidoPaterno;
		this.dias=dias;
		this.responsable=responsable;
		this.fechaInicio=fechaInicio;
		this.idVacacion=idVacacion;
		this.fechaPeticion=fechaPeticion;
	}
	public String getFechaPeticion() {
		return fechaPeticion;
	}
	public void setFechaPeticion(String fechaPeticion) {
		this.fechaPeticion = fechaPeticion;
	}
	public String getIdsolicitud() {
		return idsolicitud;
	}
	public void setIdsolicitud(String idsolicitud) {
		this.idsolicitud = idsolicitud;
	}
	public String getIdEstatus() {
		return idEstatus;
	}
	public void setIdEstatus(String idEstatus) {
		this.idEstatus = idEstatus;
	}
	public String getIdPuesto() {
		return idPuesto;
	}
	public void setIdPuesto(String idPuesto) {
		this.idPuesto = idPuesto;
	}
	public String getUnidadAdministrativa() {
		return unidadAdministrativa;
	}
	public void setUnidadAdministrativa(String unidadAdministrativa) {
		this.unidadAdministrativa = unidadAdministrativa;
	}
	public String getNumeroEmpleado() {
		return numeroEmpleado;
	}
	public void setNumeroEmpleado(String numeroEmpleado) {
		this.numeroEmpleado = numeroEmpleado;
	}
	public String getFechaIngreso() {
		return fechaIngreso;
	}
	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
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
	public String getDias() {
		return dias;
	}
	public void setDias(String dias) {
		this.dias = dias;
	}
	public String getResponsable() {
		return responsable;
	}
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	public String getIdVacacion() {
		return idVacacion;
	}
	public void setIdVacacion(String idVacacion) {
		this.idVacacion = idVacacion;
	}
}
