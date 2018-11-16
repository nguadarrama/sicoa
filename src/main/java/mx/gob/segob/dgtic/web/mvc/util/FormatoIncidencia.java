package mx.gob.segob.dgtic.web.mvc.util;

public class FormatoIncidencia {
	private String nombre;
	private String unidadAdministrativa;
	private String fechaActual;
	private String codigoIncidencia;
	private String cveMusuario;
	private String fechaIncidencia;

	public FormatoIncidencia(String nombre, String unidadAdministrativa, String fechaActual, String codigoIncidencia,
			String cveMusuario, String fechaIncidencia) {
		super();
		this.nombre = nombre;
		this.unidadAdministrativa = unidadAdministrativa;
		this.fechaActual = fechaActual;
		this.codigoIncidencia = codigoIncidencia;
		this.cveMusuario = cveMusuario;
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
	public String getCveMUsuario() {
		return cveMusuario;
	}

	public void setCveMUsuario(String cveMusuario) {
		this.cveMusuario = cveMusuario;
	}

	public String getFechaIncidencia() {
		return fechaIncidencia;
	}

	public void setFechaIncidencia(String fechaIncidencia) {
		this.fechaIncidencia = fechaIncidencia;
	}
	
}
