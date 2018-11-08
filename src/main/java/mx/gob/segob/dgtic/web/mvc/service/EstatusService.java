package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import mx.gob.segob.dgtic.web.mvc.dto.Estatus;

public interface EstatusService {
	public List<Estatus> obtieneListaEstatus(Authentication authentication);
	public List<Estatus> obtieneListaCompletaEstatus(Authentication authentication);
}
