package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import mx.gob.segob.dgtic.web.mvc.dto.PerfilUsuario;

public interface PerfilUsuarioService {
	
	public List<PerfilUsuario> recuperaPerfilesUsuario(String claveUsuario);
	public void guardaEliminaPerfilesUsuario(Integer[] clavePerfil, String claveUsuario);
}
