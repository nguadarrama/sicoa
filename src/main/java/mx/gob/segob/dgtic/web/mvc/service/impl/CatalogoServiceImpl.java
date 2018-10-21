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
import org.springframework.security.authentication.AuthenticationServiceException;
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
import mx.gob.segob.dgtic.web.mvc.dto.Justificacion;
import mx.gob.segob.dgtic.web.mvc.dto.TipoDia;
import mx.gob.segob.dgtic.web.mvc.dto.Perfil;
import mx.gob.segob.dgtic.web.mvc.dto.Periodo;
import mx.gob.segob.dgtic.web.mvc.service.CatalogoService;
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
	public List<Horario> obtieneHorarios() {
		List<Horario> listaHorario = new ArrayList<>();
		HttpResponse response;
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_INFO_HORARIO);
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
			throw new AuthenticationServiceException("Error al obtener usuario : "+response.getStatusLine().getReasonPhrase());
		}
				
		return listaHorario;
	}
	
	@Override
	public Horario modificaHorario(Horario horario) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		Header header = new BasicHeader("Authorization", "Bearer %s");
		HttpEntity httpEntity = new BasicHttpEntity();
		
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("horario", horario);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
			throw new AuthenticationServiceException("Error al obtener usuario : "+response.getStatusLine().getReasonPhrase());
		}
		
		return horario;
				
	}
	
	@Override
	public Horario agregaHorario(Horario horario) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		Header header = new BasicHeader("Authorization", "Bearer %s");
		HttpEntity httpEntity = new BasicHttpEntity();
		//BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("horario", horario);
		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
			throw new AuthenticationServiceException("Error al obtener usuario : "+response.getStatusLine().getReasonPhrase());
		}
		
		return horario;
	}
	
	@Override
	public void borraHorario(Horario horario) {
		@SuppressWarnings("unused")
		HttpResponse response;
	}

	@Override
	public Horario guardaHorario(Horario horario) {
		return null;
	}

	@Override
	public Horario buscaHorario(Integer id) {
		Horario horario = new Horario();
		HttpResponse response;
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_BUSCA_HORARIO + "?id=" + id);
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
	
	
	@Override
	@SuppressWarnings("unused")
	public void eliminaHorario(Integer id) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_ELIMINA_HORARIO + "?id=" + id);
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

	@Override
	public List<TipoDia> obtieneTipoDias() {
		List<TipoDia> listaTipoDia = new ArrayList<>();
		HttpResponse response;
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_INFO_TIPODIA);
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
			throw new AuthenticationServiceException("Error al obtener usuario : "+response.getStatusLine().getReasonPhrase());
		}
				
		return listaTipoDia;
	}


	@Override
	public TipoDia buscaTipoDia(Integer id) {
		TipoDia tipo = new TipoDia();
		HttpResponse response;
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_BUSCA_TIPODIA + "?id=" + id);
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


	@Override
	public List<Perfil> obtienePerfiles() {
		List<Perfil> listaPerfil = new ArrayList<>();
		HttpResponse response;	
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_INFO_PERFIL);
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

	@Override
	public List<Justificacion> obtieneListaJ() {
		List<Justificacion> listaJustificacion = new ArrayList<>();
		HttpResponse response;
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_CAT_JUSTIFICACION);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaJustificacion = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<Justificacion>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener catalogo de justificaciones: "+response.getStatusLine().getReasonPhrase());
		}
				
		return listaJustificacion;
	}

	@Override
	public List<Justificacion> obtieneJustificaciones() {
		List<Justificacion> listaJustificacion = new ArrayList<>();
		HttpResponse response;
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_INFO_JUSTIFICACION);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaJustificacion = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<Justificacion>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener catalogo de justificaciones: "+response.getStatusLine().getReasonPhrase());
		}
				
		return listaJustificacion;
	}

	
	@Override
	public Justificacion modificaJustificacion(Justificacion justificacion) {
		HttpResponse response = null;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		try{
			Header header = new BasicHeader("Authorization", "Bearer %s");
			HttpEntity httpEntity = new BasicHttpEntity();
			//BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
			Map<String, Object> content = new HashMap<String, Object>();
			content.put("justificacion", justificacion);
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
			//se consume recurso rest
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_MODIFICA_JUSTIFICACION, httpEntity, header);
			
		}catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}catch (Exception i){
			logger.error(i.getMessage(), i);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			justificacion = gson.fromJson(dataJson, Justificacion.class);		
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener la justificaicon : "+response.getStatusLine().getReasonPhrase());
		}
		
		return justificacion;
	}

	@Override
	public Justificacion agregaJustificacion(Justificacion justificacion) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		Header header = new BasicHeader("Authorization", "Bearer %s");
		HttpEntity httpEntity = new BasicHttpEntity();
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("justificacion", justificacion);
		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_AGREGA_JUSTIFICACION, httpEntity, header);
			if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
				JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
				JsonElement dataJson = json.get("data").getAsJsonObject();
				justificacion = gson.fromJson(dataJson, Justificacion.class);		
			} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
				String mensaje = obtenerMensajeError(response);					 
				throw new AuthenticationServiceException(mensaje);			
			} else {
				throw new AuthenticationServiceException("Error al guardar la justificaicon : "+response.getStatusLine().getReasonPhrase());
			}
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}		
		return justificacion;
	}


	@Override
	public Justificacion buscaJustificacion(Integer id) {
		Justificacion justificacion = new Justificacion();
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_BUSCA_JUSTIFICACION + "?id=" + id);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			justificacion = gson.fromJson(dataJson, Justificacion.class);		
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener la justificaicon : "+response.getStatusLine().getReasonPhrase());
		}
		return justificacion;
	}

	@Override
	@SuppressWarnings("unused")
	public void eliminaJustificacion(Integer id) {
		
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_ELIMINA_JUSTIFICACION + "?id=" + id);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
	}

	@Override
	public Periodo agregaPeriodoVacacional(Periodo periodo) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		Header header = new BasicHeader("Authorization", "Bearer %s");
		HttpEntity httpEntity = new BasicHttpEntity();
		//BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("periodo", periodo);
		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
			throw new AuthenticationServiceException("Error al obtener periodo : "+response.getStatusLine().getReasonPhrase());
		}
		
		return periodo;
			
		
	}

	@Override
	public Periodo modificaPeriodoVacacional(Periodo periodo) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		Header header = new BasicHeader("Authorization", "Bearer %s");
		HttpEntity httpEntity = new BasicHttpEntity();
		//BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
		
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("periodo", periodo);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
		
		return periodo;
	}
	
	@Override
	public List<DiaFestivo> obtieneDiaFestivo() {
		List<DiaFestivo> lista = new ArrayList<>();
		HttpResponse response;
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_INFO_DIA_FESTIVO);
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
	
	@Override
	public DiaFestivo modificaDiaFestivo(DiaFestivo dia) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		Header header = new BasicHeader("Authorization", "Bearer %s");
		HttpEntity httpEntity = new BasicHttpEntity();
		//BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("diaFestivo", dia);
		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
	
	@Override
	public DiaFestivo agregaDiaFestivo(DiaFestivo dia) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		Header header = new BasicHeader("Authorization", "Bearer %s");
		HttpEntity httpEntity = new BasicHttpEntity();
		//BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
		
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("diaFestivo", dia);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
	
	@Override
	public DiaFestivo buscaDiaFestivo(Integer id) {
		DiaFestivo dia = new DiaFestivo();
		HttpResponse response;
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_BUSCA_DIA_FESTIVO + "?id=" + id);
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


	@Override
	public List<Periodo> obtienePeriodos() {
		List<Periodo> listaPeriodos = new ArrayList<>();
		HttpResponse response;
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_OBTIENE_PERIODOS);
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
	
	@SuppressWarnings("unused")
	@Override
	public void modificaEstatusPeriodo(Integer idPeriodo, boolean activo) {
		try {
			HttpResponse response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_MODIFICA_PERIODO+"?idPeriodo="+idPeriodo+"&activo="+activo);
		} catch (ClienteException e) {
			logger.error(e.getMessage(),e);
			throw new AuthenticationServiceException(e.getMessage(),e);
		}
	}
	
	@Override
	public Periodo buscaPeriodo(Integer id) {
		Periodo periodo = new Periodo();
		HttpResponse response;
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_BUSCA_PERIODO + "?id=" + id);
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
}
