package mx.gob.segob.dgtic.web.mvc.dto;

public class Incidencia {
	 
	private Integer idIncidencia;
	private Asistencia idAsistencia;
	private TipoDia tipoDia;
	private EstatusDto estatus;
	private Archivo idArchivo;
	private Integer idResponsable;
	private Boolean descuento;
	private JustificacionDto justificacion;
	private String nombreAutorizador;
	
	public Incidencia () {
		/**
		 * 
		 */
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
	public EstatusDto getEstatus() {
		return estatus;
	}
	public void setEstatus(EstatusDto estatus) {
		this.estatus = estatus;
	}
	public Archivo getIdArchivo() {
		return idArchivo;
	}
	public void setIdArchivo(Archivo idArchivo) {
		this.idArchivo = idArchivo;
	}
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

	public JustificacionDto getJustificacion() {
		return justificacion;
	}
	public void setJustificacion(JustificacionDto justificacion) {
		this.justificacion = justificacion;
	}

	public String getNombreAutorizador() {
		return nombreAutorizador;
	}

	public void setNombreAutorizador(String nombreAutorizador) {
		this.nombreAutorizador = nombreAutorizador;
	}
	
}
