package mx.gob.segob.dgtic.web.mvc.dto;


public class Justificacion {

	private Integer idJustificacion;
	private String justificacion;
	private Boolean activo;
	
	public Justificacion () {
		
	}

	/**
	 * @param idJustificacion
	 * @param justificacion
	 * @param activo
	 */
	public Justificacion(Integer idJustificacion, String justificacion, Boolean activo) {
		super();
		this.idJustificacion = idJustificacion;
		this.justificacion = justificacion;
		this.activo = activo;
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

	
}
