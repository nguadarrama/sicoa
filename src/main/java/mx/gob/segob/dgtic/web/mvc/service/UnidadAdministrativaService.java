package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import mx.gob.segob.dgtic.web.mvc.dto.UsuarioUnidadAdministrativa;

public interface UnidadAdministrativaService {

	public List<UsuarioUnidadAdministrativa> obtenerListaUnidadAdministrativa(Authentication authentication);
	public void consultaRegistraUsuarioUnidadAdministrativa(Integer idUnidad, String claveUsuario, Authentication authentication);
	public List<UsuarioUnidadAdministrativa> consultaResponsable(String claveUsuario, Authentication authentication);
	public List<UsuarioUnidadAdministrativa> obtenerUnidadesAdministrativas(Authentication authentication);
}
