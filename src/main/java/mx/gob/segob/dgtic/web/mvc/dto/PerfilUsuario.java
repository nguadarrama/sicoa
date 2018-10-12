package mx.gob.segob.dgtic.web.mvc.dto;

public class PerfilUsuario {
	private String idUsuarioPerfil;
	private Usuario claveUsuario;
	private Perfil clavePerfil;
	private Integer[] idsPerfil;
	
	public PerfilUsuario(){
		
	}

	public String getIdUsuarioPerfil() {
		return idUsuarioPerfil;
	}

	public void setIdUsuarioPerfil(String idUsuarioPerfil) {
		this.idUsuarioPerfil = idUsuarioPerfil;
	}

	public Usuario getClaveUsuario() {
		return claveUsuario;
	}

	public void setClaveUsuario(Usuario claveUsuario) {
		this.claveUsuario = claveUsuario;
	}

	public Integer[] getIdsPerfil() {
		return idsPerfil;
	}

	public void setIdsPerfil(Integer[] idsPerfil) {
		this.idsPerfil = idsPerfil;
	}

	public Perfil getClavePerfil() {
		return clavePerfil;
	}

	public void setClavePerfil(Perfil clavePerfil) {
		this.clavePerfil = clavePerfil;
	}
}
