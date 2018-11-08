package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import mx.gob.segob.dgtic.web.mvc.dto.Periodo;

public interface PeriodoService {

	public Periodo buscaPeriodoPorClaveUsuario(String claveUsuario, Authentication authentication);
	
	public List<Periodo> periodos(Authentication authentication);
}
