package mx.gob.segob.dgtic.web.mvc.dto;

public class Estatus {
	
	private Integer idEstatus;
	private String descripcion;
	private String estatus;
	
	public Estatus() {
		
	}
	
	public Estatus(Integer idEstatus, String descripcion, String estatus) {
		super();
		this.idEstatus = idEstatus;
		this.descripcion = descripcion;
		this.estatus = estatus;
	}

	public Integer getIdEstatus() {
		return idEstatus;
	}

	public void setIdEstatus(Integer idEstatus) {
		this.idEstatus = idEstatus;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	
}
