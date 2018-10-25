package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;


import mx.gob.segob.dgtic.web.mvc.dto.Periodo;

public interface PeriodoService {

	public Periodo buscaPeriodoPorClaveUsuario(String claveUsuario);
	
	public List<Periodo> periodos();
}
