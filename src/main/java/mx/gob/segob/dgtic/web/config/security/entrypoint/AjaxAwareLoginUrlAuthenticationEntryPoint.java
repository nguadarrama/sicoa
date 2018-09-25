/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.config.security.entrypoint;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;




/**
 * Clase encargada de verificar si un error de sesion se genera mediante una peticion asincrona o sincrona. 
 */
public class AjaxAwareLoginUrlAuthenticationEntryPoint extends
		LoginUrlAuthenticationEntryPoint {
	   
	/**
	 * Constante que representa el atributo AJAX_REQUEST, valor del header en el request que identifica si una petici&oacute;n es realizada de forma asincrona.
	 */
	private static final String AJAX_REQUEST = "XMLHttpRequest"; 
	
	/**
	 * Constante que representa el atributo HEADER_REQUEST, nombre del header en el request que identifica el tipo de petici&oacute;n que se realiza 
	 */
	private static final String HEADER_REQUEST = "X-Requested-With";
	
	/**
	 * Constante que representa el atributo ATTR_LOGOUT_NAME, parametro que se recibe cuando sea por un cierre de sesi&oacute;n 
	 */
	private static final String ATTR_LOGOUT_NAME="logout";
	
	/**
	 * Constante que representa el atributo ATTR_LOGOUT_NAME, parametro que se recibe cuando sea por un vencimiento de sesi&oacute;n
	 */
	private static final String ATTR_TIMEOUT_NAME="timeout";
	
	/**
	 * Instancia del objeto AjaxAwareLoginUrlAuthenticationEntryPoint indicando el path que resuelve el formulario de autenticaci&oacute;n
	 *
	 * @param loginFormUrl el path que resuelve el formulario de autenticaci&oacute;n.            
	 */
	public AjaxAwareLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
		
	}

		
	/** 
	 * Se realiza la evaluaci&oacute;n de la petici&oacute;n para determinar si es s&iacute;ncrona o as&iacute;ncrona, y generar la respuesta de error que corresponde.
	 * 
	 * @see org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint#commence(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
	 */
	@Override
	public void commence(	final HttpServletRequest request, 
							final HttpServletResponse response, 
							final AuthenticationException authException) 
				throws IOException, ServletException {
		
		Boolean ajaxRequest = ( request.getHeader(HEADER_REQUEST) != null && 
								AJAX_REQUEST.equals(request.getHeader(HEADER_REQUEST)));
        if (ajaxRequest) {
            response.sendError(401, "Sesion de usuario expiro");
        } else {	        	
        	String logout=(String) request.getParameter(ATTR_LOGOUT_NAME);
        	String timeout=(String) request.getParameter(ATTR_TIMEOUT_NAME);
        	if(StringUtils.isNotBlank(logout)){
        		setUseForward(true);
        		request.setAttribute(ATTR_LOGOUT_NAME, ATTR_LOGOUT_NAME);
        	}else if(StringUtils.isNotBlank(timeout)){
        		setUseForward(true);
        		request.setAttribute(ATTR_TIMEOUT_NAME, ATTR_TIMEOUT_NAME);
        	}
        	
        	super.commence(request, response, authException);
        }
    }
}
