package mx.gob.segob.dgtic.web.mvc.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import mx.gob.segob.dgtic.web.mvc.constants.CatalogoEndPointConstants;
import mx.gob.segob.dgtic.web.mvc.dto.DiaFestivo;
import mx.gob.segob.dgtic.web.mvc.dto.Horario;
import mx.gob.segob.dgtic.web.mvc.dto.JustificacionDto;
import mx.gob.segob.dgtic.web.mvc.dto.NivelOrganizacional;
import mx.gob.segob.dgtic.web.mvc.dto.TipoDia;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;
import mx.gob.segob.dgtic.web.mvc.dto.Perfil;
import mx.gob.segob.dgtic.web.mvc.dto.Periodo;
import mx.gob.segob.dgtic.web.mvc.service.CatalogoService;
import mx.gob.segob.dgtic.web.mvc.service.constants.Constantes;
import mx.gob.segob.dgtic.web.mvc.util.rest.ClienteRestUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.HttpResponseUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.exception.ClienteException;

@Service
public class CatalogoServiceImpl implements CatalogoService {
	
	/**
     *  Instancia para realizar log.
     */
	private static final Logger logger = LoggerFactory.getLogger(LogoutCustomHandler.class);
	

	@Override
	public List<Horario> obtieneHorarios(Authentication authentication) {
		List<Horario> listaHorario;
		@SuppressWarnings("unchecked")
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		HttpResponse response;
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_INFO_HORARIO, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaHorario = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<Horario>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener usuario.. : "+response.getStatusLine().getReasonPhrase());
		}	
		return listaHorario;
	}
	
	@Override
	public List<Horario> obtieneHorariosCat(Authentication authentication) {
		List<Horario> listaHorario;
		HttpResponse response;
		@SuppressWarnings("unchecked")
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION,Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_INFO_HORARIO_CAT, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaHorario = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<Horario>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener usuario- : "+response.getStatusLine().getReasonPhrase());
		}	
		return listaHorario;
	}
	
	@Override
	public Horario modificaHorario(Horario horario, Authentication authentication) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		@SuppressWarnings("unchecked")
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		HttpEntity httpEntity = new BasicHttpEntity();
		Map<String, Object> content = new HashMap<>();
		content.put("horario", horario);
		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			logger.warn("Warn {} ",e1);
		}
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_MODIFICA_HORARIO, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			horario = gson.fromJson(dataJson, Horario.class);		
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener usuario... : "+response.getStatusLine().getReasonPhrase());
		}
		return horario;		
	}
	
	@Override
	public Horario agregaHorario(Horario horario, Authentication authentication) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		@SuppressWarnings("unchecked")
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		HttpEntity httpEntity = new BasicHttpEntity();
		Map<String, Object> content = new HashMap<>();
		content.put("horario", horario);
		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			logger.info("Warn -- {} ",e1);
		}
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_AGREGA_HORARIO, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			horario = gson.fromJson(dataJson, Horario.class);		
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener usuario.: "+response.getStatusLine().getReasonPhrase());
		}
		
		return horario;
	}
	
	@Override
	public void borraHorario(Horario horario, Authentication authentication) {
		/**
		 * 
		 */
	}

	@Override
	public Horario guardaHorario(Horario horario, Authentication authentication) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Horario buscaHorario(Integer id, Authentication authentication) {
		Horario horario;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_BUSCA_HORARIO + "?id=" + id, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			horario = gson.fromJson(dataJson, Horario.class);		
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener el horario : "+response.getStatusLine().getReasonPhrase());
		}
		
		return horario;
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void eliminaHorario(Integer id, Authentication authentication) {
	
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		try { //se consume recurso rest
		ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_ELIMINA_HORARIO + "?id=" + id, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
	}
	
	private String obtenerMensajeError(HttpResponse response){
		JsonObject respuesta = (JsonObject) HttpResponseUtil.getJsonContent(response);
		JsonObject metadata = (JsonObject)respuesta.get(AutorizacionConstants.ATTR_META_DATA_JSON_NAME);			
		JsonArray jsonArray = metadata.get(AutorizacionConstants.ATTR_META_DATA_ERRORES_JSON_NAME).getAsJsonArray();
		return (jsonArray.size() != 0)?jsonArray.get(0).getAsString() : "Error desconocido";
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoDia> obtieneTipoDias(Authentication authentication) {
		List<TipoDia> listaTipoDia;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_INFO_TIPODIA, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaTipoDia = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<TipoDia>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener usuario.- : "+response.getStatusLine().getReasonPhrase());
		}
				
		return listaTipoDia;
	}


	@SuppressWarnings("unchecked")
	@Override
	public TipoDia buscaTipoDia(Integer id, Authentication authentication) {
		TipoDia tipo;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_BUSCA_TIPODIA + "?id=" + id, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			tipo = gson.fromJson(dataJson, TipoDia.class);		
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener el Tipo Dia : "+response.getStatusLine().getReasonPhrase());
		}
		
		return tipo;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Perfil> obtienePerfiles(Authentication authentication) {
		List<Perfil> listaPerfil;
		HttpResponse response;	
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_INFO_PERFIL, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaPerfil = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<Perfil>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener perfiles : "+response.getStatusLine().getReasonPhrase());
		}
				
		return listaPerfil;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JustificacionDto> obtieneListaJ(Authentication authentication) {
		List<JustificacionDto> listaJustificacion;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_CAT_JUSTIFICACION, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaJustificacion = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<JustificacionDto>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener catalogo de justificaciones: "+response.getStatusLine().getReasonPhrase());
		}
				
		return listaJustificacion;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JustificacionDto> obtieneJustificaciones(Authentication authentication) {
		List<JustificacionDto> listaJustificacion;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_INFO_JUSTIFICACION, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaJustificacion = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<JustificacionDto>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener catalogo de justificaciones: "+response.getStatusLine().getReasonPhrase());
		}
				
		return listaJustificacion;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public JustificacionDto modificaJustificacion(JustificacionDto justificacion, Authentication authentication) {
			HttpResponse response;
			Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
			HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
			
			//Se agrega el JWT a la cabecera para acceso al recurso rest
			Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
					+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
			HttpEntity httpEntity = new BasicHttpEntity();
			Map<String, Object> content = new HashMap<>();
			content.put("justificacion", justificacion);
			try {
				httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
			} catch (ClienteException e1) {
				logger.warn("Warn: -- {} ",e1);
			}
			try { //se consume recurso rest
				response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_MODIFICA_JUSTIFICACION, httpEntity, header);
			} catch (ClienteException e) {
				logger.error(e.getMessage(), e);
				throw new AuthenticationServiceException(e.getMessage(), e);
			}
			if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
				
				JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
				JsonElement dataJson = json.get("data").getAsJsonObject();
				justificacion = gson.fromJson(dataJson, JustificacionDto.class);		
				
			} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
				
				String mensaje = obtenerMensajeError(response);					 
				throw new AuthenticationServiceException(mensaje);			
			} else {
				throw new AuthenticationServiceException("Error al obtener justificacion : "+response.getStatusLine().getReasonPhrase());
			}
			return justificacion;		
		}
		
	@SuppressWarnings("unchecked")
	@Override
	public JustificacionDto agregaJustificacion(JustificacionDto justificacion, Authentication authentication) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		Map<String, Object> content = new HashMap<>();
		content.put("justificacion", justificacion);
		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			logger.warn("Warn.-. {} ",e1);
		}
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_AGREGA_JUSTIFICACION, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			justificacion = gson.fromJson(dataJson, JustificacionDto.class);		
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener justificacion : "+response.getStatusLine().getReasonPhrase());
		}
		return justificacion;
	}


	@SuppressWarnings("unchecked")
	@Override
	public JustificacionDto buscaJustificacion(Integer id, Authentication authentication) {
		JustificacionDto justificacion;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_BUSCA_JUSTIFICACION + "?id=" + id, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			justificacion = gson.fromJson(dataJson, JustificacionDto.class);		
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener la justificaicon : "+response.getStatusLine().getReasonPhrase());
		}
		return justificacion;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void eliminaJustificacion(Integer id, Authentication authentication) {
		
		
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		try { //se consume recurso rest
			ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_ELIMINA_JUSTIFICACION + "?id=" + id, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Periodo agregaPeriodoVacacional(Periodo periodo, Authentication authentication) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		
		Map<String, Object> content = new HashMap<>();
		content.put(Constantes.PERIODO, periodo);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			logger.warn(" Warn-. {} ",e1);
		}
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_AGREGA_PERIODO, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			periodo = gson.fromJson(dataJson, Periodo.class);		
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener usuario : "+response.getStatusLine().getReasonPhrase());
		}
		String gsonT = gson.toJson(periodo);
		logger.info("CAtalogoServiceImpl--method--agregaPeriodoVacacional-- {} ",gsonT);
		return periodo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void modificaPeriodoVacacional(Periodo periodo, Authentication authentication) {
		
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		Map<String, Object> content = new HashMap<>();
		content.put(Constantes.PERIODO, periodo);
		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			logger.warn("Warn--. {} ",e1);
		}
		try { //se consume recurso rest
			ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_MODIFICA_PERIODO, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DiaFestivo> obtieneDiaFestivo(Authentication authentication) {
		List<DiaFestivo> lista;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_INFO_DIA_FESTIVO, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			lista = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<DiaFestivo>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener los días festivos.-. : "+response.getStatusLine().getReasonPhrase());
		}
				
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DiaFestivo> obtieneDiaFestivoCat(Authentication authentication) {
		List<DiaFestivo> lista;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_INFO_DIA_FESTIVO_CAT, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			lista = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<DiaFestivo>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener los días festivos : "+response.getStatusLine().getReasonPhrase());
		}
				
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DiaFestivo modificaDiaFestivo(DiaFestivo dia, Authentication authentication) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		Map<String, Object> content = new HashMap<>();
		content.put("diaFestivo", dia);
		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			logger.warn(".Warn {} ",e1);
		}
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_MODIFICA_DIA_FESTIVO, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			dia = gson.fromJson(dataJson, DiaFestivo.class);		
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener el día Inhábil : "+response.getStatusLine().getReasonPhrase());
		}
				
		return dia;
				
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DiaFestivo agregaDiaFestivo(DiaFestivo dia, Authentication authentication) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		Map<String, Object> content = new HashMap<>();
		content.put("diaFestivo", dia);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			logger.warn("-Warn: {} ",e1);
		}
		
		try { //se consume recurso rest
			response =  ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_AGREGA_DIA_FESTIVO, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			dia = gson.fromJson(dataJson, DiaFestivo.class);		
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener el día Inhábil : "+response.getStatusLine().getReasonPhrase());
		}
				
		return dia;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DiaFestivo buscaDiaFestivo(Integer id, Authentication authentication) {
		DiaFestivo dia;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_BUSCA_DIA_FESTIVO + "?id=" + id, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			dia = gson.fromJson(dataJson, DiaFestivo.class);		
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener el día Festivp : "+response.getStatusLine().getReasonPhrase());
		}
		
		return dia;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Periodo> obtienePeriodos(Authentication authentication) {
		List<Periodo> listaPeriodos;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_OBTIENE_PERIODOS, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaPeriodos = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<Periodo>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener los periodos : "+response.getStatusLine().getReasonPhrase());
		}
				
		return listaPeriodos;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Periodo> obtienePeriodosCat(Authentication authentication) {
		List<Periodo> listaPeriodos;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_OBTIENE_PERIODOS_CAT, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaPeriodos = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<Periodo>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener los periodos : "+response.getStatusLine().getReasonPhrase());
		}
				
		return listaPeriodos;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Periodo modificaEstatusPeriodo(Periodo periodo, Authentication authentication) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		Map<String, Object> content = new HashMap<>();
		content.put(Constantes.PERIODO, periodo);
		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			logger.warn(",Warn : {} ",e1);
		}
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_MODIFICA_PERIODO, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			periodo = gson.fromJson(dataJson, Periodo.class);		
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener el periodo : "+response.getStatusLine().getReasonPhrase());
		}
		String gsonT = gson.toJson(periodo);
		logger.info("CatalogoServiceimpl--method--modificaEstatusPeriodo: {} ",gsonT);	
		return periodo;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Periodo buscaPeriodo(Integer id, Authentication authentication) {
		Periodo periodo;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_BUSCA_PERIODO + "?id=" + id, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			periodo = gson.fromJson(dataJson, Periodo.class);		
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener el periodo : "+response.getStatusLine().getReasonPhrase());
		}
		
		return periodo;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> nivelesEmpleado(Authentication authentication) {
		List<Usuario> listaNiveles;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_OBTIENE_NIVELES_EMPLEADOS, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaNiveles = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<Usuario>>(){}.getType());
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener la lista de Unidades : "+response.getStatusLine().getReasonPhrase());
		}
		return listaNiveles;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NivelOrganizacional> obtieneNiveles(Authentication authentication) {
		List<NivelOrganizacional> listaNiveles;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_OBTIENE_NIVELES, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaNiveles = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<NivelOrganizacional>>(){}.getType());
			String gsonT = gson.toJson(listaNiveles);
			logger.info("ResponseObtieneUnidades: {} ",gsonT);
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener la lista de Niveles Organizacionales : "+response.getStatusLine().getReasonPhrase());
		}
		return listaNiveles;
	}

	@SuppressWarnings("unchecked")
	@Override
	public NivelOrganizacional nivelAgrega(NivelOrganizacional nivel, Authentication authentication) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		
		Map<String, Object> content = new HashMap<>();
		content.put("nivel", nivel);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			logger.warn("_Warn: {} ", e1);
		}
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_AGREGA_NIVEL_ORGANIZACIONAL, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			nivel = gson.fromJson(dataJson, NivelOrganizacional.class);		
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener usuario : "+response.getStatusLine().getReasonPhrase());
		}
		return nivel;
	}

	@SuppressWarnings("unchecked")
	@Override
	public NivelOrganizacional nivelBusca(Integer idNivel, Authentication authentication) {
		logger.info("entro a catalogoServiceImpl--method--nivelBusca idNivel: {} ",idNivel);
		NivelOrganizacional nivel;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_BUSCA_NIVEL+"?idNivel="+idNivel, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			nivel = gson.fromJson(dataJson, NivelOrganizacional.class);		
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener el nivel : "+response.getStatusLine().getReasonPhrase());
		}
		
		return nivel;
	}

	@SuppressWarnings("unchecked")
	@Override
	public NivelOrganizacional modificaNivel(NivelOrganizacional nivel, Authentication authentication) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		
		Map<String, Object> content = new HashMap<>();
		content.put("nivel", nivel);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			logger.warn("..Warn: {} ", e1);
		}
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_MODIFICA_NIVEL, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			nivel = gson.fromJson(dataJson, NivelOrganizacional.class);		
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener nivel : "+response.getStatusLine().getReasonPhrase());
		}
		String gsonT = gson.toJson(nivel);
		logger.info("CAtalogoServiceImpl--method--modificaNivel-- {} ",gsonT);	
		return nivel;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String obtieneDiaFestivoParaBloquear(Authentication authentication) {
		
		List<DiaFestivo> lista;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER
				+ detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_INFO_DIA_FESTIVO_ACTIVO, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			lista = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<DiaFestivo>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener los días festivos : "+response.getStatusLine().getReasonPhrase());
		}

		logger.info("tamaño: {} ",lista.size());
		String listaDias= "";
		for(DiaFestivo diaFestivo: lista){
			Date date = new Date();
			String fecha=null;
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
			logger.info("Fechas obtenidas: {} ",diaFestivo.getFecha());
		    try {
				date = sdf.parse(diaFestivo.getFecha());
				fecha = sdf1.format(date);
			} catch (ParseException e) {
				logger.warn("Warn-- {} ",e);
			}
		    listaDias=""+fecha+",";
		}
		if(!listaDias.isEmpty()){
		listaDias=listaDias.substring(0, (listaDias.length()- 1));
		}
		return listaDias;
	}
}
