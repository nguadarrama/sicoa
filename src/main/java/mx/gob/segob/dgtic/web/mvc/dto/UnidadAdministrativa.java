package mx.gob.segob.dgtic.web.mvc.dto;

public class UnidadAdministrativa {
	
	private Integer idUnidad;
	private String nombre;
	
	public UnidadAdministrativa(){
		/**
		 * 
		 */
		
	}

	public Integer getIdUnidad() {
		return idUnidad;
	}

	public void setIdUnidad(Integer idUnidad) {
		this.idUnidad = idUnidad;
	}

	public String getNombre() {
		String aux = nombre.replaceAll("Ã“", "Ó");
		aux = aux.replaceAll("Ã‰", "É");
		aux = aux.replaceAll("Ãš", "Ú");
		aux = aux.replaceAll("Ã", "Í");
		aux = aux.replaceAll("Ã", "Í");
		nombre = aux;
		return nombre;
	}

	public void setNombre(String nombre) {
		String aux = nombre.replaceAll("Ã“", "Ó");
		aux = aux.replaceAll("Ã‰", "É");
		aux = aux.replaceAll("Ãš", "Ú");
		aux = aux.replaceAll("Ã", "Í");
		aux = aux.replaceAll("Ã", "Í");
		this.nombre = aux;
	}
	
	
}
