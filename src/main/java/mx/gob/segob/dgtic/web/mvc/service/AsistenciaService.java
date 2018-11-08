package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

import mx.gob.segob.dgtic.web.mvc.dto.Asistencia;
import mx.gob.segob.dgtic.web.mvc.dto.GeneraReporteArchivo;
import mx.gob.segob.dgtic.web.mvc.dto.reporte;
import mx.gob.segob.dgtic.web.mvc.util.FormatoIncidencia;

public interface AsistenciaService {
	public List<Asistencia> buscaAsistenciaEmpleadoMes(String claveEmpleado, Authentication authentication);
	public List<Asistencia> buscaAsistenciaEmpleadoRango (String claveEmpleado, String fechaInicio, String fechaFin, Authentication authentication);
	
	public List<Asistencia> buscaAsistenciaEmpleadoRangoCoordinador (String cve_m_usuario, String nombre, String paterno, 
    		String materno, String nivel, Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa, String cveCoordinador, Authentication authentication);
	
	public List<Asistencia> buscaAsistenciaEmpleadoRangoDireccion (String cve_m_usuario, String nombre, String paterno, 
    		String materno, String nivel, Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa, Authentication authentication);
	
	public Asistencia buscaAsistenciaPorId(Integer id, Authentication authentication);
	public Integer creaIncidencia(Integer idAsistencia, Integer idTipoDia, Integer idJustificacion, Integer idArchivo, String nombreAutorizador, Authentication authentication);
	public Integer creaDescuento(Integer idAsistencia, Integer idTipoDia, Integer idJustificacion, Integer idArchivo, String nombreAutorizador, Authentication authentication);
	public Integer aplicaDescuento(Integer idAsistencia, Authentication authentication);
	public Integer dictaminaIncidencia(Integer idAsistencia, Integer idTipoDia, Integer idJustificacion, String dictaminacion, Authentication authentication);
	public reporte formatoJustificacion(FormatoIncidencia formatoIncidencia, Authentication authentication);
	public reporte formatoDescuento(FormatoIncidencia formatoIncidencia, Authentication authentication);
	
	public List<Asistencia> buscaAsistenciaDireccionReporte (String cve_m_usuario, String nombre, String paterno, 
    		String materno, String nivel, Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa, String[] permisos, Authentication authentication);
	
	public List<Asistencia> buscaAsistenciaCoordinadorReporte (String cve_m_usuario, String nombre, String paterno, 
    		String materno, String nivel, Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa, String cveCoordinador, String[] permisos, Authentication authentication);
}
