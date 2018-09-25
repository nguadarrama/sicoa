/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.config.security.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import mx.gob.segob.dgtic.web.config.security.constants.AutorizacionConstants;
import mx.gob.segob.dgtic.web.config.security.constants.SesionConstants;
import mx.gob.segob.dgtic.web.config.security.service.AutenticacionService;
import mx.gob.segob.dgtic.web.config.security.service.impl.AutenticacionServiceImpl;
import mx.gob.segob.dgtic.web.mvc.util.properties.AplicacionWebPropertiesUtil;

/**
 * Listener que verifica los eventos de una sesion de usuario.
 *
 * @see javax.servlet.http.HttpSessionListener	
 */
public class SeguridadSessionListener implements HttpSessionListener {

	
	/** 
	 * Servicio que contiene las tareas de autenticaci&oacute;n 
	 * */
	private AutenticacionService autenticacionService;
	
	/**
	 * Constructor del listener de seguridad
	 */
	public SeguridadSessionListener(){
		autenticacionService = new AutenticacionServiceImpl();
	}
	
	/**
	 * Evento que se ejecuta cada que se crea una sesion de usuario, establecen los parametros y las configuraciones de la sesi&oacute;n de usuario.
	 * 
	 * @param event Evento de cierre de sesi&oacute;n
	 * 
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		int segundosInactivoSession = -1;
		String tiempoSesionMinutos = AplicacionWebPropertiesUtil
											.getPropiedades()
											.obtenerPropiedad(AutorizacionConstants.TIEMPO_SESION_MINUTOS);
		if(StringUtils.isNotBlank(tiempoSesionMinutos) && NumberUtils.isCreatable(tiempoSesionMinutos)){
			segundosInactivoSession = Integer.parseInt(tiempoSesionMinutos)*60;
		}
		
		event.getSession().setMaxInactiveInterval(segundosInactivoSession);		
	}

	/**
	 * Evento que se ejecuta al momento de destruir la sesi&oacute;n de usuario, registrando su cierre de sesi&oacute;n.
	 * 
	 * @param event Evento de cierre de sesi&oacute;n
	 * 
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
	
		String tokenAcceso = (String)event.getSession().getAttribute(SesionConstants.TOKEN_SESION);			
		if(StringUtils.isNotBlank(tokenAcceso)){
			autenticacionService.logout(tokenAcceso);				
		}		
	}	

}
