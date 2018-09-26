package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import mx.gob.segob.dgtic.web.mvc.dto.Asistencia;

public interface AsistenciaService {
	public List<Asistencia> buscaAsistenciaEmpleado(String claveEmpleado);
}
