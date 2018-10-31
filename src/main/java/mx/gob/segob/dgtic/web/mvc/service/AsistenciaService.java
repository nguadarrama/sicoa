package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import mx.gob.segob.dgtic.web.mvc.dto.Asistencia;
import mx.gob.segob.dgtic.web.mvc.dto.GeneraReporteArchivo;
import mx.gob.segob.dgtic.web.mvc.dto.reporte;
import mx.gob.segob.dgtic.web.mvc.util.FormatoIncidencia;

public interface AsistenciaService {
	public List<Asistencia> buscaAsistenciaEmpleadoMes(String claveEmpleado);
	public List<Asistencia> buscaAsistenciaEmpleadoRango (String claveEmpleado, String fechaInicio, String fechaFin);
	
	public List<Asistencia> buscaAsistenciaEmpleadoRangoCoordinador (String cve_m_usuario, String nombre, String paterno, 
    		String materno, String nivel, Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa, String cveCoordinador);
	
	public List<Asistencia> buscaAsistenciaEmpleadoRangoDireccion (String cve_m_usuario, String nombre, String paterno, 
    		String materno, String nivel, Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa);
	
	public Asistencia buscaAsistenciaPorId(Integer id);
	public Integer creaIncidencia(Integer idAsistencia, Integer idTipoDia, Integer idJustificacion, Integer idArchivo, String nombreAutorizador);
	public Integer creaDescuento(Integer idAsistencia, Integer idTipoDia, Integer idJustificacion, Integer idArchivo, String nombreAutorizador);
	public Integer aplicaDescuento(Integer idAsistencia);
	public Integer dictaminaIncidencia(Integer idAsistencia, Integer idTipoDia, Integer idJustificacion, String dictaminacion);
	public reporte formatoJustificacion(FormatoIncidencia formatoIncidencia);
	public reporte formatoDescuento(FormatoIncidencia formatoIncidencia);
	
	public List<Asistencia> buscaAsistenciaDireccionReporte (String cve_m_usuario, String nombre, String paterno, 
    		String materno, String nivel, Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa, String[] permisos);
	
	public List<Asistencia> buscaAsistenciaCoordinadorReporte (String cve_m_usuario, String nombre, String paterno, 
    		String materno, String nivel, Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa, String cveCoordinador, String[] permisos);
}
