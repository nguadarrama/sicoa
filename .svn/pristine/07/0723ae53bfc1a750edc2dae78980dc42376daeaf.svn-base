/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.config.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import mx.gob.segob.dgtic.web.config.security.constants.SesionConstants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;


/**
 * Clase encargada de complementar la informaci&oacute;n de sesi&oacute;n cuando el proceso de autenticaci&oacute;n fue correcto y valido.
 */
public class AutenticacionCorrectaHandler extends
		SavedRequestAwareAuthenticationSuccessHandler {
	
	/**
	 * Constante que representa el atributo PAGINA_REDIRECT_DEFAULT, path al cual se redirige por default una vez que el proceso de autenticacion sea correcto
	 */
	private static final String PAGINA_REDIRECT_DEFAULT = "/";
	
	/**
	 * Se establecen los atributos adicionales a considerar para la sesi&oacute;n de usuario correctamente autenticado.
	 * 
	 * @param request  La petici&oacute;n que realizo la autenticaci&oacute;n
	 * @param response La respuesta que se genero una vez concluido el proceso de autenticaci&oacute;n. 
	 * @param authentication  Objeto que contiene los atributos que se generan para el contexto de seguridad.
	 * 
	 * @see org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {
		
		String paginaRedirect = PAGINA_REDIRECT_DEFAULT;
		subirDetallesSession(request, authentication);
		getRedirectStrategy().sendRedirect(request, response, paginaRedirect);
		
	}
	
	
	/**
	 * Proceso donde se agregan los atributos que estar&aacute;n disponibles por medio de la sesi&oacute;n de usuario
	 *
	 * @param request  La petici&oacute;n de autenticaci&oacute;n.
	 * @param authentication  Objeto que contiene la informaci&oacute;n que se genera para el contexto de seguridad
	 */
	@SuppressWarnings("unchecked")
	private void subirDetallesSession(HttpServletRequest request, Authentication authentication)
	{	
	
		Map<String, Object> detalle = (Map<String, Object>)authentication.getDetails();		
		HttpSession session = request.getSession();
		session.setAttribute(SesionConstants.USUARIO_SESION, detalle.get(SesionConstants.USUARIO_SESION));
		session.setAttribute(SesionConstants.TOKEN_SESION, detalle.get(SesionConstants.TOKEN_SESION));
	}
}
