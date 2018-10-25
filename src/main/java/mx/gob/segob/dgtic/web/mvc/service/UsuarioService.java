package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import mx.gob.segob.dgtic.web.mvc.dto.Usuario;

public interface UsuarioService {

	//usuarios
	public List<Usuario> obtieneUsuarios();
	public Usuario buscaUsuario(String claveUsuario);
	public void eliminaUsuario(String claveUsuario);
	public void modificaUsuario(Usuario usuario);
	public void reiniciaContrasenia(String claveUsuario);
	public Usuario buscaUsuarioPorId(String idUsuario);
	public List<Usuario> obtieneListaJefes();
}
