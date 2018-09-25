/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.config.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import mx.gob.segob.dgtic.web.config.security.constants.AutorizacionConstants;

/**
 *  Clase encargada de manejar los eventos a realizar cuando suceda una inconsistencia ante una inconsistencia en el proceso autenticaci&oacute;n
 */
public class AutenticacionFallidaHandler extends SimpleUrlAuthenticationFailureHandler  {
	
	
	
	/** Path al que se redirecciona en caso de error. */
	private String redirectLoginPath;
	
	
	/**
	 * Constructor del manejador de error 
	 *
	 * @param redirectLoginPath the redirect login path
	 */
	public AutenticacionFallidaHandler(String redirectLoginPath){
		this.redirectLoginPath = redirectLoginPath;
	}
	
	
	/**
	 * Se establece la informaci&oacute;n de respuesta durante el proceso de respuesta de error 
	 * 
	 * @param request La petici&oacute;n que realizo la autenticaci&oacute;n
	 * @param response La respuesta que se genero una vez concluido el proceso de autenticaci&oacute;n. 
	 * @param exception El error que se genero durante la autenticaci&oacute;n
	 * 
	 * @see org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler#onAuthenticationFailure(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
	 */
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		saveException(request, exception);
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		
		request.setAttribute(AutorizacionConstants.ATTRIBUTE_ERROR_LOGIN, exception.getMessage());
		request.getRequestDispatcher(redirectLoginPath).forward(request, response);
	}
}
