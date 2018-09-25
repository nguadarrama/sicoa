/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.config.security.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import mx.gob.segob.dgtic.web.config.security.constants.SesionConstants;
import mx.gob.segob.dgtic.web.config.security.service.AutenticacionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Map;

/**
 * Clase que maneja el cierre de sesi&oacute;n de usuario
 */
public class LogoutCustomHandler  implements LogoutHandler {

	 /**
     *  Instancia para realizar log.
     */
	private static final Logger logger = LoggerFactory.getLogger(LogoutCustomHandler.class);
	
	/** 
	 * Servicio que contiene las tareas de autenticaci&oacute;n 
	 * */
	private AutenticacionService autenticacionService;
	
	/**
	 * Constructor del manejador de cierre de sesi&oacute;n
	 *
	 * @param autenticacionService El servicio de autenticacion
	 */
	public LogoutCustomHandler(AutenticacionService autenticacionService) {
		this.autenticacionService = autenticacionService;
	}

	/**
	 * Se realizan las tareas adicionales a durante el cierre de sesi&oacute;n.
	 * 
	 * @see org.springframework.security.web.authentication.logout.LogoutHandler#logout(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void logout(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) {
		
		logger.trace("Cerrando sesion ");
		if(authentication != null){
			Map<String, Object> detalles = (Map<String, Object>)authentication.getDetails();			
			String tokenAcceso = (String)detalles.get(SesionConstants.TOKEN_SESION);
			autenticacionService.logout(tokenAcceso);
			
			HttpSession session = request.getSession();
			session.invalidate();
		}
	}

}
