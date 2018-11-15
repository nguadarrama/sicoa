package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import mx.gob.segob.dgtic.web.mvc.dto.BusquedaDto;
import mx.gob.segob.dgtic.web.mvc.dto.GeneraReporteArchivo;
import mx.gob.segob.dgtic.web.mvc.dto.VacacionPeriodo;
import mx.gob.segob.dgtic.web.mvc.dto.Vacaciones;
import mx.gob.segob.dgtic.web.mvc.dto.VacacionesAux;
import mx.gob.segob.dgtic.web.mvc.dto.reporte;

public interface VacacionesService {
	public List<Vacaciones> obtieneVacaciones(Authentication authentication);
	public VacacionesAux obtieneVacacion(String idVacacion, Authentication authentication);
	public Vacaciones eliminaVacaciones(Integer idVacaciones, Authentication authentication);
	public Vacaciones agregaVacaciones(VacacionesAux vacaciones, String claveUsuario, Authentication authentication);
	public Vacaciones modificaVacaciones(Vacaciones vacaciones, Authentication authentication);
	public VacacionPeriodo buscaVacacionPeriodoPorClaveUsuarioYPeriodo(String claveUsuario, Integer idPeriodo, Authentication authentication);
	public Vacaciones aceptaORechazaVacaciones(Vacaciones vacaciones, Integer idDetalle, Authentication authentication);
	public List<Vacaciones> obtenerVacacionesPorFiltros(BusquedaDto busquedaDto, Authentication authentication);
	public List<Vacaciones> consultaVacacionesPropiasPorFiltros(BusquedaDto busquedaDto, Authentication authentication );
	public reporte generaReporte(GeneraReporteArchivo generaReporteArchivo, Authentication authentication);
	public String recuperaDiasVacacioness(String claveUsuario, Authentication authentication);
	public Vacaciones cancelaVacaciones(Integer idDetalle, Authentication authentication);
	
	
}
