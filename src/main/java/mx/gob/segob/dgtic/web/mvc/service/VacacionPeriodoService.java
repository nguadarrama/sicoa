package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import mx.gob.segob.dgtic.web.mvc.dto.VacacionPeriodo;

public interface VacacionPeriodoService {

	public List<VacacionPeriodo> obtenerUsuariosVacacionesPorFiltros(String claveUsuario, String nombre, String apellidoPaterno, String apellidoMaterno, String idUnidad);
}
