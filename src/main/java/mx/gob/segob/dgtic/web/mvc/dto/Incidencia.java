package mx.gob.segob.dgtic.web.mvc.dto;

public class Incidencia {
	 
	private Integer idIncidencia;
	private Asistencia idAsistencia;
	private Integer idTipoIncidencia;
//	private EstatusDto idEstatus;
//	private ArchivoDto idArchivo;
	private Integer idResponsable;
	private Boolean descuento;
	private String observaciones;
	
	public Incidencia () {
		
	}
	
	public Integer getIdIncidencia() {
		return idIncidencia;
	}
	public void setIdIncidencia(Integer idIncidencia) {
		this.idIncidencia = idIncidencia;
	}
	public Asistencia getIdAsistencia() {
		return idAsistencia;
	}
	public void setIdAsistencia(Asistencia idAsistencia) {
		this.idAsistencia = idAsistencia;
	}
	public Integer getIdTipoIncidencia() {
		return idTipoIncidencia;
	}
	public void setIdTipoIncidencia(Integer idTipoIncidencia) {
		this.idTipoIncidencia = idTipoIncidencia;
	}
//	public EstatusDto getIdEstatus() {
//		return idEstatus;
//	}
//	public void setIdEstatus(EstatusDto idEstatus) {
//		this.idEstatus = idEstatus;
//	}
//	public ArchivoDto getIdArchivo() {
//		return idArchivo;
//	}
//	public void setIdArchivo(ArchivoDto idArchivo) {
//		this.idArchivo = idArchivo;
//	}
	public Integer getIdResponsable() {
		return idResponsable;
	}
	public void setIdResponsable(Integer idResponsable) {
		this.idResponsable = idResponsable;
	}
	public Boolean getDescuento() {
		return descuento;
	}
	public void setDescuento(Boolean descuento) {
		this.descuento = descuento;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	
}
