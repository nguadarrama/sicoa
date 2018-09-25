/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.config.filter;

import javax.servlet.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.gob.segob.dgtic.web.WebMVCApplicationInit;

import java.io.IOException;


/**
 * Filtro de encoding
 */
public class EncodingFilter implements Filter {

    /**
     *  Instancia para realizar log.
     */
	private static Logger logger = LoggerFactory.getLogger(WebMVCApplicationInit.class);
	
    /**
     * Constante que representa el atributo DEFAULT_ENCODING, representa el encoding por default si no se define al configurar el filtro "UTF-8". 
     */
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * El encoding con que interpretaran todas la peticiones.
     */
    private String encoding;

    /**
     * Inicializa el par&aacute;metro del encoding a integrar en la petici&oacute;n,  de no existir una definici&oacute;n como par&aacute;metro inicial se toma el valor por default "DEFAULT_ENCODING"
     * 
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("encoding");
        encoding = (encoding == null || encoding.isEmpty())? DEFAULT_ENCODING : encoding;

        logger.info("Inicializando filtro "+EncodingFilter.class.getName()+":"+encoding );
    }

    /**
     * Se verifica si no se especifica un encoding en la petici&oacute;n.
     * <p>
     * Si no existe un encoding en la petici&oacute;n realizada se agrega el encoding configurado.
     * <p>
     * Se agrega el encoding configurado a toda respuesta realizada.
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (null == servletRequest.getCharacterEncoding()) {
            servletRequest.setCharacterEncoding(encoding);
        }

        servletResponse.setCharacterEncoding(encoding);

        filterChain.doFilter(servletRequest, servletResponse);        
        
    }

	/**
	 * Invocado por el contenedor web para indicar a un filtro que ha sido detenido
	 */
    @Override
    public void destroy() {
    	logger.info("Destruyendo filtro "+EncodingFilter.class.getName());        
    }
}
