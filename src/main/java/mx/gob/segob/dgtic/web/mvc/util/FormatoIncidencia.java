package mx.gob.segob.dgtic.web.mvc.util;

public class FormatoIncidencia {
	private String nombre;
	private String unidadAdministrativa;
	private String fechaActual;
	private String codigoIncidencia;
	private String cve_m_usuario;
	private String fechaIncidencia;

	public FormatoIncidencia(String nombre, String unidadAdministrativa, String fechaActual, String codigoIncidencia,
			String cve_m_usuario, String fechaIncidencia) {
		super();
		this.nombre = nombre;
		this.unidadAdministrativa = unidadAdministrativa;
		this.fechaActual = fechaActual;
		this.codigoIncidencia = codigoIncidencia;
		this.cve_m_usuario = cve_m_usuario;
		this.fechaIncidencia = fechaIncidencia;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getUnidadAdministrativa() {
		return unidadAdministrativa;
	}
	public void setUnidadAdministrativa(String unidadAdministrativa) {
		this.unidadAdministrativa = unidadAdministrativa;
	}
	public String getFechaActual() {
		return fechaActual;
	}
	public void setFechaActual(String fechaActual) {
		this.fechaActual = fechaActual;
	}
	public String getCodigoIncidencia() {
		return codigoIncidencia;
	}
	public void setCodigoIncidencia(String codigoIncidencia) {
		this.codigoIncidencia = codigoIncidencia;
	}
	public String getCve_m_usuario() {
		return cve_m_usuario;
	}

	public void setCve_m_usuario(String cve_m_usuario) {
		this.cve_m_usuario = cve_m_usuario;
	}

	public String getFechaIncidencia() {
		return fechaIncidencia;
	}

	public void setFechaIncidencia(String fechaIncidencia) {
		this.fechaIncidencia = fechaIncidencia;
	}
	
}
