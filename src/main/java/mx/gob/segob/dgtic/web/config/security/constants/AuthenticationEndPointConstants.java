/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.config.security.constants;


/**
 * Constantes de los enpoints requeridos para la autenticaci&oacute;n 
 */
public final class AuthenticationEndPointConstants {
	
	/**
	 * Restringe la construccion de la clase para evitar una creacion mediante el constructor por default.
	 */
	private AuthenticationEndPointConstants(){
		throw new IllegalStateException("Constants class");
	}
	
	
	/**
	 * Constante que representa el atributo WEB_SERVICE_TOKEN_AUTH, path del WebService para obtener el token de autenticaci&oacute;n. 
	 */
	public static final String WEB_SERVICE_TOKEN_AUTH = "/sec/autorizacionAutenticacion?solicitante=%s";
	
	/**
	 * Constante que representa el atributo WEB_SERVICE_LOGIN, path del WebService para obtener el token de acceso, mediante las credenciales del usuario. 
	 */
	public static final String WEB_SERVICE_LOGIN = "/sec/login";
	
	/**
	 * Constante que representa el atributo WEB_SERVICE_INFO_USUARIO, path del WebService para obtener la informaci&oacute;n del usuario. 
	 */	
	public static final String WEB_SERVICE_INFO_USUARIO = "/sec/informacion/usuario";
	
	/**
	 * Constante que representa el atributo WEB_SERVICE_LOGOUT, path del WebService para cerrar la sesi&oacute;n de usuario. 
	 */
	public static final String WEB_SERVICE_LOGOUT = "/sec/logout";
	
	public static final String WEB_SERVICE_CONTRASENIA = "/sec/cambiaContrasenia";
}
