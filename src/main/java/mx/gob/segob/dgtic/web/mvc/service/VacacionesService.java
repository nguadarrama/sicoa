package mx.gob.segob.dgtic.web.mvc.service;

import java.util.Date;
import java.util.List;

import mx.gob.segob.dgtic.web.mvc.dto.VacacionPeriodo;
import mx.gob.segob.dgtic.web.mvc.dto.Vacaciones;

public interface VacacionesService {
	public List<Vacaciones> obtieneVacaciones();
	public Vacaciones obtieneVacacion(String idVacacion);
	public void eliminaVacaciones(String idVacaciones);
	public void agregaVacaciones(Vacaciones vacaciones, String claveUsuario);
	public void modificaVacaciones(Vacaciones vacaciones);
	public VacacionPeriodo buscaVacacionPeriodoPorClaveUsuarioYPeriodo(String claveUsuario, Integer idPeriodo);
	public void aceptaORechazaVacaciones(Vacaciones vacaciones, Integer idDetalle);
	public List<Vacaciones> obtenerVacacionesPorFiltros(String claveUsuario, String nombre, String apellidoPaterno, String apellidoMaterno, String idUnidad, String idEstatus);
	public List<Vacaciones> consultaVacacionesPropiasPorFiltros(String claveUsuario, String idPeriodo, String idEstatus, String pfechaInicio, String pfechaFin );
}
