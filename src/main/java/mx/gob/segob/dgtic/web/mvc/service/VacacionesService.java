package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import mx.gob.segob.dgtic.web.mvc.dto.GeneraReporteArchivo;
import mx.gob.segob.dgtic.web.mvc.dto.VacacionPeriodo;
import mx.gob.segob.dgtic.web.mvc.dto.Vacaciones;
import mx.gob.segob.dgtic.web.mvc.dto.VacacionesAux;
import mx.gob.segob.dgtic.web.mvc.dto.reporte;

public interface VacacionesService {
	public List<Vacaciones> obtieneVacaciones();
	public Vacaciones obtieneVacacion(String idVacacion);
	public void eliminaVacaciones(String idVacaciones);
	public Vacaciones agregaVacaciones(VacacionesAux vacaciones, String claveUsuario);
	public Vacaciones modificaVacaciones(Vacaciones vacaciones);
	public VacacionPeriodo buscaVacacionPeriodoPorClaveUsuarioYPeriodo(String claveUsuario, Integer idPeriodo);
	public Vacaciones aceptaORechazaVacaciones(Vacaciones vacaciones, Integer idDetalle);
	public List<Vacaciones> obtenerVacacionesPorFiltros(String claveUsuario, String nombre, String apellidoPaterno, String apellidoMaterno, String idUnidad, String idEstatus);
	public List<Vacaciones> consultaVacacionesPropiasPorFiltros(String claveUsuario, String idPeriodo, String idEstatus, String pfechaInicio, String pfechaFin );
	public reporte generaReporte(GeneraReporteArchivo generaReporteArchivo);
}
