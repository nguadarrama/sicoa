package mx.gob.segob.dgtic.web.mvc.dto;

public class Incidencia {
	 
	private Integer idIncidencia;
	private Asistencia idAsistencia;
	private TipoDia tipoDia;
	private Estatus estatus;
//	private ArchivoDto idArchivo;
	private Integer idResponsable;
	private Boolean descuento;
	private Justificacion justificacion;
	
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
	public TipoDia getTipoDia() {
		return tipoDia;
	}
	public void setTipoDia(TipoDia tipoDia) {
		this.tipoDia = tipoDia;
	}
	public Estatus getEstatus() {
		return estatus;
	}
	public void setEstatus(Estatus estatus) {
		this.estatus = estatus;
	}
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

	public Justificacion getJustificacion() {
		return justificacion;
	}
	public void setJustificacion(Justificacion justificacion) {
		this.justificacion = justificacion;
	}
	
}
