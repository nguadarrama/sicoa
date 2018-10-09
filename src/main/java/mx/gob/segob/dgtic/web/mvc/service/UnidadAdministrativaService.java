package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import mx.gob.segob.dgtic.web.mvc.dto.UsuarioUnidadAdministrativa;

public interface UnidadAdministrativaService {

	public List<UsuarioUnidadAdministrativa> obtenerListaUnidadAdministrativa();
	public void consultaRegistraUsuarioUnidadAdministrativa(Integer idUnidad, String claveUsuario);
}
