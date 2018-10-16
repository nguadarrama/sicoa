package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import mx.gob.segob.dgtic.web.mvc.dto.Asistencia;

public interface AsistenciaService {
	public List<Asistencia> buscaAsistenciaEmpleadoMes(String claveEmpleado);
	public List<Asistencia> buscaAsistenciaEmpleadoRango (String claveEmpleado, String fechaInicio, String fechaFin);
	
	public List<Asistencia> buscaAsistenciaEmpleadoRangoCoordinador (String cve_m_usuario, String nombre, String paterno, 
    		String materno, String nivel, String tipo, String estado, String fechaInicial, String fechaFinal, String unidadAdministrativa, String cveCoordinador);
	
	public List<Asistencia> buscaAsistenciaEmpleadoRangoDireccion (String cve_m_usuario, String nombre, String paterno, 
    		String materno, String nivel, String tipo, String estado, String fechaInicial, String fechaFinal, String unidadAdministrativa);
	
	public Asistencia buscaAsistenciaPorId(Integer id);
	public void creaIncidencia(Integer idAsistencia, Integer idTipoDia, Integer idJustificacion, Integer idArchivo);
	public void dictaminaIncidencia(Integer idAsistencia, Integer idTipoDia, Integer idJustificacion, String dictaminacion);
}
