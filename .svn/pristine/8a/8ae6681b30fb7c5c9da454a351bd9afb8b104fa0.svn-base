/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.config.security.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * Clase encargada de manejar el error de acceso denegado.
 */
public class AccesoDenegadoHandler implements AccessDeniedHandler{	

    /**
     * Evaluaci&oacute;n sobre el tipo de excepci&oacute;n que origina el error. 
     * Si es un error de CSRF redirige a la pagina de login, de lo contrario genera un JSON de respuesta de error.
     * 
     * @see org.springframework.security.web.access.AccessDeniedHandler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.access.AccessDeniedException)
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
    	
    	if(accessDeniedException instanceof org.springframework.security.web.csrf.MissingCsrfTokenException){
    		response.sendRedirect(request.getServletContext().getContextPath()+"/login?restartServer");
    	} else {           
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            StringBuilder respuesta = new StringBuilder();
            respuesta.append("{");
            respuesta.append("\"meta\": {\"errors\": [\""+accessDeniedException.getMessage()+"\"]"+", \"response\":\"ERROR\"},");
            respuesta.append("\"data\": \"\"");
            respuesta.append("}");
            PrintWriter out = response.getWriter();            
            out.print(respuesta.toString());
            out.flush();
            out.close();
    	}
    }

}


