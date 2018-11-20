/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.config.security.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import mx.gob.segob.dgtic.web.config.security.constants.AuthenticationEndPointConstants;
import mx.gob.segob.dgtic.web.config.security.constants.AutorizacionConstants;
import mx.gob.segob.dgtic.web.config.security.handler.LogoutCustomHandler;
import mx.gob.segob.dgtic.web.config.security.service.AutenticacionService;
import mx.gob.segob.dgtic.web.mvc.dto.UsuarioSesion;
import mx.gob.segob.dgtic.web.mvc.service.constants.Constantes;
import mx.gob.segob.dgtic.web.mvc.util.rest.ClienteRestUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.HttpResponseUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.exception.ClienteException;
import mx.gob.segob.dgtic.web.mvc.views.controller.constants.ConstantsController;

/**
 * Implementaci&oacute;n de los m&eacute;todos para la l&oacute;gica de negocio de autenticaci&oacute;n
 */
@Service
public class AutenticacionServiceImpl implements AutenticacionService {
	
	 /**
     *  Instancia para realizar log.
     */
	private static final Logger logger = LoggerFactory.getLogger(LogoutCustomHandler.class);


	
	/**
	 * Obtener token de autorizaci&oacute;n de login.
	 *
	 * @param solicitante El solicitante de sistema
	 * @return Token de autorizacio&oacute;n
	 * @throws AuthenticationServiceException Error al solicitar el token de autorizaci&oacute;n
	 * 
	 * @see mx.gob.segob.dgtic.web.config.security.service.AutenticacionService#obtenerTokenAutorizacionLogin(java.lang.String)
	 */
	@Override
	public String obtenerTokenAutorizacionLogin(String solicitante) {
			
		String pathServiceTokenAuth = String.format(AuthenticationEndPointConstants.WEB_SERVICE_TOKEN_AUTH, solicitante);
		String tokenAutorizacion = null;
		HttpResponse response;
		try {
			response = ClienteRestUtil.getCliente().get(pathServiceTokenAuth);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()){
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			tokenAutorizacion = json.get(AutorizacionConstants.ATTR_DATA_JSON_NAME).getAsString();
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)){
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);
		} else {
			throw new AuthenticationServiceException("Error al obtener autorizacion : "+response.getStatusLine().getReasonPhrase());
		}
		
		return tokenAutorizacion;
	}
	
	/**
	 * Obtener token acceso.
	 *
	 * @param tokenAutorizacionLogin El token de autorizaci&oacute;n de login
	 * @param usuario El usuario a autenticar
	 * @param contrasenia La palabra clave que identifica al usuario a autenticar
	 * @return El token de acceso a recursos restringidos
	 * @throws AuthenticationServiceException Error al solicitar el token de acceso
	 * 
	 * @see mx.gob.segob.dgtic.web.config.security.service.AutenticacionService#obtenerTokenAccesoLogin(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String obtenerTokenAccesoLogin(String tokenAutorizacionLogin, String usuario, String contrasenia) {
		Map<String, Object> parametros = new HashMap<>(0);
		parametros.put("usuario", usuario);
		parametros.put("contrasenia", contrasenia);
		
		Header tokenAuthenticacionHeader = new BasicHeader( AutorizacionConstants.AUTHORIZATION_HEADER_NAME, 
				String.format(AutorizacionConstants.AUTHENTICATION_TOKEN_WITH_SCHEME, tokenAutorizacionLogin));
				
		HttpResponse response;
		try {
			response = ClienteRestUtil.getCliente().post(AuthenticationEndPointConstants.WEB_SERVICE_LOGIN, 
															parametros , 
															ContentType.APPLICATION_FORM_URLENCODED, 
															tokenAuthenticacionHeader);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		String tokenAcceso = null;
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()){
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			tokenAcceso = json.get(AutorizacionConstants.ATTR_DATA_JSON_NAME).getAsString();
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)){
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error de proceso de login : "+response.getStatusLine().getReasonPhrase());			
		}
		
		return tokenAcceso;
	}
	
	/**
	 * Obtener informacion usuario.
	 *
	 * @param tokenAcceso El token de acceso
	 * @return La informaci&oacute;n del usuario asociado al token
	 * @throws AuthenticationServiceException Error al solicitar el token de acceso
	 * 
	 * @see mx.gob.segob.dgtic.web.config.security.service.AutenticacionService#obtenerInformacionUsuario(java.lang.String)
	 */
	@Override
	public 	UsuarioSesion obtenerInformacionUsuario(String tokenAcceso) {
		
		Gson gson = new GsonBuilder()
				.enableComplexMapKeySerialization()
				.serializeNulls()				
				.create();
		
		UsuarioSesion usuarioSesion = null;
		Header tokenAccesoHeader = new BasicHeader(AutorizacionConstants.AUTHORIZATION_HEADER_NAME, 
				String.format(AutorizacionConstants.AUTHENTICATION_TOKEN_WITH_SCHEME, tokenAcceso));
				
		HttpResponse response;
		try {
			response = ClienteRestUtil.getCliente().get(AuthenticationEndPointConstants.WEB_SERVICE_INFO_USUARIO,tokenAccesoHeader);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()){
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement usuarioJson = json.get(AutorizacionConstants.ATTR_DATA_JSON_NAME).getAsJsonObject();	
			usuarioSesion = gson.fromJson(usuarioJson, UsuarioSesion.class);
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)){
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener usuario : "+response.getStatusLine().getReasonPhrase());
		}
		return usuarioSesion;
	}
	
	
	
	/**
	 * Cierre de sesion
	 *
	 * @param tokenAcceso El token de acceso asociado al sistema
	 * @throws AuthenticationServiceException Error al solicitar el token de acceso
	 * 
	 * @see mx.gob.segob.dgtic.web.config.security.service.AutenticacionService#logout(java.lang.String)
	 */
	@Override
	public void logout(String tokenAcceso) {
		try {
			Header tokenAccesoHeader = new BasicHeader( AutorizacionConstants.AUTHORIZATION_HEADER_NAME, 
					String.format(AutorizacionConstants.AUTHENTICATION_TOKEN_WITH_SCHEME, tokenAcceso));
			
			ClienteRestUtil.getCliente()
				.put(AuthenticationEndPointConstants.WEB_SERVICE_LOGOUT, null, tokenAccesoHeader);
			
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
	}
	
	
	/**
	 * Obtener mensaje error.
	 *
	 * @param response El response recibido del webservice
	 * @return El mensaje de error enviado por el webservice
	 */
	private String obtenerMensajeError(HttpResponse response){
		JsonObject respuesta = (JsonObject) HttpResponseUtil.getJsonContent(response);
		JsonObject metadata = (JsonObject)respuesta.get(AutorizacionConstants.ATTR_META_DATA_JSON_NAME);			
		JsonArray jsonArray = metadata.get(AutorizacionConstants.ATTR_META_DATA_ERRORES_JSON_NAME).getAsJsonArray();
		
		return (jsonArray.size() != 0)?jsonArray.get(0).getAsString() : "Error desconocido";
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public Boolean cambiaContrasenia(String usuario, String contrasenia, Authentication authentication) {
		
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
		HttpResponse response;
		Boolean respuesta=false;
		
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		
		Map<String, Object> content = new HashMap<>();
		content.put("usuario", usuario);
		content.put("contrasenia", contrasenia);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			logger.info(ConstantsController.WARN,e1);
		}
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(AuthenticationEndPointConstants.WEB_SERVICE_CONTRASENIA, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()){
			respuesta=true;	
			this.logout(detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)){
			respuesta=false;	
					 
		} else {
			respuesta=false;
		}
		return respuesta;
		
	}
		
}
