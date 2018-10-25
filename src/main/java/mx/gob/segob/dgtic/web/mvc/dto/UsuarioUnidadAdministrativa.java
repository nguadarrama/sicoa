package mx.gob.segob.dgtic.web.mvc.dto;

public class UsuarioUnidadAdministrativa {
	
	private Usuario claveUsuario;
	private UnidadAdministrativa idUnidad;
	
	public UsuarioUnidadAdministrativa(){
		
	}

	public Usuario getClaveUsuario() {
		return claveUsuario;
	}

	public void setClaveUsuario(Usuario claveUsuario) {
		this.claveUsuario = claveUsuario;
	}

	public UnidadAdministrativa getIdUnidad() {
		return idUnidad;
	}

	public void setIdUnidad(UnidadAdministrativa idUnidad) {
		this.idUnidad = idUnidad;
	}

}
