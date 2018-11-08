package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import mx.gob.segob.dgtic.web.mvc.dto.PerfilUsuario;

public interface PerfilUsuarioService {
	
	public List<PerfilUsuario> recuperaPerfilesUsuario(String claveUsuario, Authentication authentication);
	public void guardaEliminaPerfilesUsuario(Integer[] clavePerfil, String claveUsuario, Authentication authentication);
}
