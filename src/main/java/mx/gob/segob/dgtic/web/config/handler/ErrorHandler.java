/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.config.handler;

import javax.servlet.http.HttpServletRequest;

import mx.gob.segob.dgtic.web.mvc.util.MessagesUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;


/**
 * El manejador de errores, para generar respuestas personalizadas
 */
@ControllerAdvice
public class ErrorHandler  {	 
	
    /**
     *  Instancia para realizar log.
     */
	private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);
	
	/** Utileria que resuelve etiquetas dentro del archivo de etiquetas. */
	@Autowired
	private MessagesUtil messagesUtil;
	
	/**
	 * Encargado de resolver las excepciones gen&eacute;ricas de tipo {@link java.lang.Throwable}.
	 *
	 * @param throwable La excepcion que se genero
	 * @param model modelo que integra los atributos del request
	 * @return La vista que se resolvera
	 */
	@ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(final Throwable throwable, final Model model) {
		logger.error(throwable.getMessage(), throwable);
        String errorMessage = (StringUtils.isNotBlank(throwable.getMessage()) ? throwable.getMessage() : "ERROR_INTERNO");
        return redirectError(messagesUtil.get("SEGURIDAD.RECURSO_ERROR_TITULO"),
								errorMessage,
								messagesUtil.get("SEGURIDAD.RECURSO_ERROR"),
								StringUtils.EMPTY,
								model
			);
    }
	
	/**
	 * Encargado de resolver las excepciones de acceso denegado de tipo {@link org.springframework.security.access.AccessDeniedException} 
	 * 
	 * @param throwable La excepcion que se genero
	 * @param model modelo que integra los atributos del request
	 * @return La vista que se resolvera
	 */
	@ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String accesoDenegado(final Throwable throwable, final Model model) {
		logger.error(throwable.getMessage(), throwable);
        String errorMessage = (StringUtils.isNotBlank(throwable.getMessage()) ? throwable.getMessage() : "FORBIDDEN");        		
        return redirectError(messagesUtil.get("SEGURIDAD.ACCESO_DENEGADO_TITULO"),
								errorMessage,
								messagesUtil.get("SEGURIDAD.ACCESO_DENEGADO"),
								StringUtils.EMPTY,
								model
			);
    }
	

	/**
	 * Encargado de resolver las excepciones de recurso no encontrado de tipo {@link org.springframework.web.servlet.NoHandlerFoundException}
	 * 
	 * @param throwable the throwable
	 * @param model the model
	 * @param req the req
	 * @return the string
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String recursoNoEncontrado(final Throwable throwable, final Model model,HttpServletRequest req) {
		logger.error(throwable.getMessage(), throwable);
        String errorMessage = (StringUtils.isNotBlank(throwable.getMessage()) ? throwable.getMessage() : "NOT_FOUND");        
        return redirectError(messagesUtil.get("SEGURIDAD.RECURSO_NO_ENCONTRADO_TITULO"),
        						errorMessage,
        						messagesUtil.get("SEGURIDAD.RECURSO_NO_ENCONTRADO"),
        						req.getRequestURL().toString(),
        						model
        					);
    }	

	
	/**
	 * Establece los parametros que se enviaran en el modelo a la vista que se renderizara.
	 *
	 * @param titulo Titulo que identifica el tipo de error
	 * @param errorMessage El mensaje de error que corresponde a la excepcion
	 * @param descripcionError La descripcion clara del error
	 * @param url La url que genera el error
	 * @param model El modelo que contiene los atributos a integrar en la respuesta
	 * @return La vista a resolver
	 */
	public String redirectError(String titulo, String errorMessage, String descripcionError, String url, final Model model){
    
		model.addAttribute("titulo", titulo);
		model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("mensaje", descripcionError);        
        model.addAttribute("url", url);
		
		return "/error";
	}
}
