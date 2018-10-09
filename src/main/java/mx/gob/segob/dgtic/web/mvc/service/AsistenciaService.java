package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import mx.gob.segob.dgtic.web.mvc.dto.Asistencia;

public interface AsistenciaService {
	public List<Asistencia> buscaAsistenciaEmpleadoMes(String claveEmpleado);
	public List<Asistencia> buscaAsistenciaEmpleadoRango (String claveEmpleado, String fechaInicio, String fechaFin);
	public Asistencia buscaAsistenciaPorId(Integer id);
	public void creaIncidencia(Integer idAsistencia, Integer idTipoDia, Integer idJustificacion);
}
