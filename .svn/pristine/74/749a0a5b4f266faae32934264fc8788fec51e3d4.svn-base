/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.mvc.util;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/**
 *  Clase de apoyo para accesar al archivo de etiquetas.
 */
@Component
public class MessagesUtil {

    /** Bean con el archivo de etiquetas cargado. */
    @Autowired
    private MessageSource messageSource;

    /** Interfaz de acceso a la fuente de mensajes */
    private MessageSourceAccessor accessor;

    /**
     * Inicializa las configuraciones para activar la interfaz de acceso a etiquetas
     */
    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource, Locale.ROOT);
    }

    /**
     * Obtiene el mensaje identificado por un codigo
     *
     * @param code El codigo del mensaje de etiquetas
     * @return La etiqueta que corresponde al codigo
     */
    public String get(String code) {
        return accessor.getMessage(code);
    }
    
    /**
     * Obtiene el mensaje identificado por un codigo y reemplaza los parametros configurados dentro del mensaje
     *
     * @param code El codigo del mensaje
     * @param args Los parametros a reemplazar dentro del mensaje
     * @return La etiqueta que corresponde al codigo
     */
    public String get(String code, String...args) {
    	return accessor.getMessage(code, args);
    }
}

