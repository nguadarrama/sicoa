package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import mx.gob.segob.dgtic.web.mvc.dto.Usuario;

public interface UsuarioService {

	//usuarios
	public List<Usuario> obtieneUsuarios(Authentication authentication);
	public Usuario buscaUsuario(String claveUsuario, Authentication authentication);
	public void eliminaUsuario(String claveUsuario, Authentication authentication);
	public void modificaUsuario(Usuario usuario, Authentication authentication);
	public void reiniciaContrasenia(String claveUsuario, Authentication authentication);
	public Usuario buscaUsuarioPorId(String idUsuario, Authentication authentication);
	public List<Usuario> obtieneListaJefes(Authentication authentication);
}
