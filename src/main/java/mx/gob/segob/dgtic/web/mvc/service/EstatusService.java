package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import mx.gob.segob.dgtic.web.mvc.dto.EstatusDto;

public interface EstatusService {
	public List<EstatusDto> obtieneListaEstatus(Authentication authentication);
	public List<EstatusDto> obtieneListaCompletaEstatus(Authentication authentication);
}
