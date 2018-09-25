package mx.gob.segob.dgtic.web.mvc.dto;

import java.io.Serializable;

public class TipoDia implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -2768358354708161354L;

	private Integer idTipoDia;
    private String nombre;
    private String observacion;
    private boolean incidencia;
	
	public TipoDia () {
		
	}
	

	public TipoDia(Integer idTipoDia, String nombre, String observacion, boolean incidencia) {
		super();
		this.idTipoDia = idTipoDia;
		this.nombre = nombre;
		this.observacion = observacion;
		this.incidencia = incidencia;
	}


	/**
	 * @return the idTipoDia
	 */
	public Integer getIdTipoDia() {
		return idTipoDia;
	}


	/**
	 * @param idTipoDia the idTipoDia to set
	 */
	public void setIdTipoDia(Integer idTipoDia) {
		this.idTipoDia = idTipoDia;
	}


	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}


	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	/**
	 * @return the observacion
	 */
	public String getObservacion() {
		return observacion;
	}


	/**
	 * @param observacion the observacion to set
	 */
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}


	/**
	 * @return the incidencia
	 */
	public boolean isIncidencia() {
		return incidencia;
	}


	/**
	 * @param incidencia the incidencia to set
	 */
	public void setIncidencia(boolean incidencia) {
		this.incidencia = incidencia;
	}
	
}
