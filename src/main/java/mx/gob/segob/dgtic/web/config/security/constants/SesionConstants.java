/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.config.security.constants;

/**
 * Constantes que identifican los atributos de sesion
 */
public final class SesionConstants {
	
	/**
	 *  Restringe la construccion de la clase para evitar una creacion mediante el constructor por default.
	 */
	private SesionConstants(){throw new IllegalStateException("Constants class");}
	
	/**
	 * Constante que representa el atributo USUARIO_SESION, nombre del atributo de sesion del objeto que contiene la informaci&oacute;n de usuario. 
	 */
	public static final String USUARIO_SESION = "usuario";
	
	/**
	 * Constante que representa el atributo TOKEN_SESION, nombre del atributo de sesion del token requerido para el consumo de los webservices restringidos. 
	 */
	public static final String TOKEN_SESION = "_token";
	///////////////////////
	public static final String CAMBIA_PASSWORD = "password";
	
}
