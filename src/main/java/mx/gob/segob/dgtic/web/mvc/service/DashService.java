package mx.gob.segob.dgtic.web.mvc.service;

import org.springframework.security.core.Authentication;

import mx.gob.segob.dgtic.web.mvc.dto.DashBoardDto;


public interface DashService {
	
	public DashBoardDto getDash(Integer id, Authentication authentication);
	
}
