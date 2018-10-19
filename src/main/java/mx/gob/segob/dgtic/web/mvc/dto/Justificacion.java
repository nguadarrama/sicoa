package mx.gob.segob.dgtic.web.mvc.dto;


public class Justificacion {

	private Integer idJustificacion;
	private String clave;
	private String justificacion;
	private Boolean activo;
	private String mensaje;
	
	public Justificacion () {
		
	}

	/**
	 * @param idJustificacion
	 * @param clave
	 * @param justificacion
	 * @param activo
	 * @param mensaje
	 */
	public Justificacion(Integer idJustificacion, String clave, String justificacion, Boolean activo, String mensaje) {
		super();
		this.idJustificacion = idJustificacion;
		this.clave = clave;
		this.justificacion = justificacion;
		this.activo = activo;
		this.mensaje = mensaje;
	}

	/**
	 * @return the idJustificacion
	 */
	public Integer getIdJustificacion() {
		return idJustificacion;
	}

	/**
	 * @param idJustificacion the idJustificacion to set
	 */
	public void setIdJustificacion(Integer idJustificacion) {
		this.idJustificacion = idJustificacion;
	}

	/**
	 * @return the clave
	 */
	public String getClave() {
		return clave;
	}

	/**
	 * @param clave the clave to set
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}

	/**
	 * @return the justificacion
	 */
	public String getJustificacion() {
		return justificacion;
	}

	/**
	 * @param justificacion the justificacion to set
	 */
	public void setJustificacion(String justificacion) {
		this.justificacion = justificacion;
	}

	/**
	 * @return the activo
	 */
	public Boolean getActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
}
