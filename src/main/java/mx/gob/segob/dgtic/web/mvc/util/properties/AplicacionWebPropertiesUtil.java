/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.mvc.util.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Clase de apoyo para poder leer el archivo de propiedades de configuracio&oacuten del proyecto (/config/properties/aplicacion.properties).
 * <p>
 * El acceso a las utiler&iacute;as de esta clase es bajo el patr&oacute;n SINGLETON
 * <p>
 * Ejemplo
 * <pre class="code">
 * 
 *   String valorConfigurado = AplicacionPropertiesUtil.propiedades.obtenerPropiedad("propiedad-configuracion);
 * 
 * </pre>
 * 
 */
public class AplicacionWebPropertiesUtil {
	
	/**
	 * Instancia para realizar log
	 */
	private static final Logger logger = LoggerFactory.getLogger(AplicacionWebPropertiesUtil.class);
	
	/**
	 * Constante que representa el atributo CONFIGURACION_PROPERTIES. Archivo que contiene los par&aacute;metros de configuraci&oacute;n del aplicativo. 
	 */
	private static final  String CONFIGURACION_PROPERTIES = "/config/properties/aplicacion_web.properties";
	
	/**
	 * La instancia de acceso est&aacute;tico. Accesada bajo el patr&oacute;n SINGLETON
	 */
	private static AplicacionWebPropertiesUtil propiedades = null;
	
	/**
	 * Contenedor de los atributos que se encuentran en el archivo de configuracion
	 */
	private Properties aplicacionProperties;
	
	
	/**
	 * Instancia una clase de apoyo para accseo a las configuraciones del sistema.
	 */
	private AplicacionWebPropertiesUtil() {

		ClassLoader loader = AplicacionWebPropertiesUtil.class.getClassLoader();
		aplicacionProperties = new Properties();
		//Se obtiene el archivo de configuracion de la estructura del proyecto
		try (InputStream inputStream = loader.getResourceAsStream(CONFIGURACION_PROPERTIES);) {
			//De existir se cargan al contenedor de atributos.
			if(inputStream != null){
				aplicacionProperties.load(inputStream);	
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);			
		}	
	}
	
	/**
	 * Obtiene la instancia SINGLETON para acceso a las propiedades del sistema.
	 * 
	 * Ejemplo
	 * <pre class="code">
	 * 
	 *   String valorConfigurado = AplicacionPropertiesUtil.propiedades.obtenerPropiedad("propiedad-configuracion);
	 * 
	 * </pre>
	 *
	 * @return Instancia singleton para acceso a las propiedades del sistema.
	 * 
	 */
	public static AplicacionWebPropertiesUtil getPropiedades() {
	     if(propiedades == null) {
	    	 propiedades = new AplicacionWebPropertiesUtil();
	     }
	     return propiedades;
	  }



	/**
	 * Obtener propiedad.
	 *
	 * @param nombrePropiedad El nombre de la propiedad a buscar
	 * @return El valor asociado a esa propiedad
	 */
	public String obtenerPropiedad(String nombrePropiedad) {
		return aplicacionProperties.getProperty(nombrePropiedad);
	}
	
	/**
	 * Agregar propiedad.
	 *
	 * @param nombrePropiedad Nombre de la propiedad con que sera identificada
	 * @param valor El valor a asociar a esa propiedad
	 */
	public void agregarPropiedad(String nombrePropiedad, String  valor) {
		aplicacionProperties.put(nombrePropiedad, valor);
	}
}
