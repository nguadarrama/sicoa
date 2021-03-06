package mx.gob.segob.dgtic.web.mvc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import mx.gob.segob.dgtic.web.config.security.constants.AutorizacionConstants;
import mx.gob.segob.dgtic.web.config.security.handler.LogoutCustomHandler;
import mx.gob.segob.dgtic.web.config.security.service.AutenticacionService;
import mx.gob.segob.dgtic.web.mvc.constants.CatalogoEndPointConstants;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;
import mx.gob.segob.dgtic.web.mvc.service.UsuarioService;
import mx.gob.segob.dgtic.web.mvc.service.constants.Constantes;
import mx.gob.segob.dgtic.web.mvc.util.rest.ClienteRestUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.HttpResponseUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.exception.ClienteException;
@Service
public class UsuarioServiceImpl implements UsuarioService {

	/**
     *  Instancia para realizar log.
     */
	private static final Logger logger = LoggerFactory.getLogger(LogoutCustomHandler.class);
	
	@Autowired 
	AutenticacionService autenticacionSerivce;
	
	@SuppressWarnings("unchecked")
	public List<Usuario> obtieneUsuarios(Authentication authentication) { 
		List<Usuario> listaUsuario;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		try{
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_INFO_USUARIO, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaUsuario = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<Usuario>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener usuario.. : "+response.getStatusLine().getReasonPhrase());
		}
		return listaUsuario;
		
	}
	
	private String obtenerMensajeError(HttpResponse response){
		JsonObject respuesta = (JsonObject) HttpResponseUtil.getJsonContent(response);
		JsonObject metadata = (JsonObject)respuesta.get(AutorizacionConstants.ATTR_META_DATA_JSON_NAME);			
		JsonArray jsonArray = metadata.get(AutorizacionConstants.ATTR_META_DATA_ERRORES_JSON_NAME).getAsJsonArray();
		
		return (jsonArray.size() != 0)?jsonArray.get(0).getAsString() : "Error desconocido";
	}

	@SuppressWarnings("unchecked")
	@Override
	public Usuario buscaUsuario(String claveUsuario, Authentication authentication) {
		Usuario usuario;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_BUSCA_USUARIO + "?id=" + claveUsuario, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			usuario = gson.fromJson(dataJson, Usuario.class);		
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener usuario. : "+response.getStatusLine().getReasonPhrase());
		}
		usuario.setFechaIngreso(usuario.getFechaIngreso());
		return usuario;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void eliminaUsuario(String claveUsuario, Authentication authentication) {
		
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		try { //se consume recurso rest
			logger.info("clave para eliminar: {} ",claveUsuario);
			
			ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_ELIMINA_USUARIO + "?id=" + claveUsuario, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer modificaUsuario(Usuario usuario, Authentication authentication) {
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		Map<String, Object> content = new HashMap<>();
		content.put("usuario", usuario);
		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			logger.warn("Warn .- {} ",e1);
		}
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_MODIFICA_USUARIO, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			return 0;
		}
			return 1;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void reiniciaContrasenia(String claveUsuario, Authentication authentication) {

		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		try { //se consume recurso rest
			logger.debug("clave para reiniciar: {} ",claveUsuario);
			ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_REINICIA_CONTRASENIA + "?claveUsuario=" + claveUsuario, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Usuario buscaUsuarioPorId(String idUsuario, Authentication authentication) {
		Usuario usuario;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_BUSCA_USUARIO_POR_ID + "?id=" + idUsuario, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			usuario = gson.fromJson(dataJson, Usuario.class);		
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener usuario : "+response.getStatusLine().getReasonPhrase());
		}
		
		return usuario;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> obtieneListaJefes(Authentication authentication) { 
		List<Usuario> listaUsuarios;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		try{
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_INFO_JEFES, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaUsuarios = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<Usuario>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener usuario : "+response.getStatusLine().getReasonPhrase());
		}
		return listaUsuarios;
		
	}
	
}
