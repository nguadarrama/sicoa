/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.config.security.service;

import org.apache.http.HttpResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;

import mx.gob.segob.dgtic.web.mvc.dto.UsuarioSesion;

/**
 * Definici&oacute;n de los m&eacute;todos para la l&oacute;gica de negocio de autenticaci&oacute;n
 */
public interface AutenticacionService {


	/**
	 * Obtener token de autorizaci&oacute;n de login.
	 *
	 * @param solicitanteSistema El solicitante de sistema
	 * @return Token de autorizacio&oacute;n
	 * @throws AuthenticationServiceException Error al solicitar el token de autorizaci&oacute;n
	 */
	String obtenerTokenAutorizacionLogin(String solicitanteSistema) throws AuthenticationServiceException;

	/**
	 * Obtener token acceso.
	 *
	 * @param tokenAutorizacionLogin El token de autorizaci&oacute;n de login
	 * @param usuario El usuario a autenticar
	 * @param contrasenia La palabra clave que identifica al usuario a autenticar
	 * @return El token de acceso a recursos restringidos
	 * @throws AuthenticationServiceException Error al solicitar el token de acceso
	 */
	String obtenerTokenAccesoLogin(String tokenAutorizacionLogin, String usuario, String contrasenia)
			throws AuthenticationServiceException;

	/**
	 * Obtener informacion usuario.
	 *
	 * @param tokenAcceso El token de acceso
	 * @return La informaci&oacute;n del usuario asociado al token
	 * @throws AuthenticationServiceException Error al solicitar el token de acceso
	 */
	UsuarioSesion obtenerInformacionUsuario(String tokenAcceso) throws AuthenticationServiceException;

	/**
	 * Cierre de sesion
	 *
	 * @param tokenAcceso El token de acceso asociado al sistema
	 * @throws AuthenticationServiceException Error al solicitar el token de acceso
	 */
	void logout(String tokenAcceso) throws AuthenticationServiceException;

	
	Boolean cambiaContrasenia(String usuario, String contrasenia, Authentication authentication)
			throws AuthenticationServiceException;
	

}
