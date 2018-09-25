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
import mx.gob.segob.dgtic.web.mvc.dto.Horario;
import mx.gob.segob.dgtic.web.mvc.dto.TipoDia;
import mx.gob.segob.dgtic.web.mvc.dto.Perfil;
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
	
	@Autowired
	AutenticacionService autenticacionSerivce; 

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
	
	@SuppressWarnings("unused")
	@Override
	public void modificaHorario(Horario horario) {
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
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_MODIFICA_HORARIO, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
				
	}
	
	@Override
	public void agregaHorario(Horario horario) {
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
			ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_AGREGA_HORARIO, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
	}
	
	@Override
	public void borraHorario(Horario horario) {
		@SuppressWarnings("unused")
		HttpResponse response;
	}

	@Override
	public void guardaHorario(Horario horario) {
		// TODO Auto-generated method stub
		
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
			throw new AuthenticationServiceException("Error al obtener usuario : "+response.getStatusLine().getReasonPhrase());
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
	@SuppressWarnings("unused")
	public void modificaTipoDia(TipoDia horario) {
		try{
		
			Header header = new BasicHeader("Authorization", "Bearer %s");
			HttpEntity httpEntity = new BasicHttpEntity();
			//BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
		
			Map<String, Object> content = new HashMap<String, Object>();
			content.put("horario", horario);
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
			//se consume recurso rest
			HttpResponse response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_MODIFICA_HORARIO, httpEntity, header);
			
		}catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}catch (Exception i){
			logger.error(i.getMessage(), i);
		}
				
	}

	@Override
	public void agregaTipoDia(TipoDia horario) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardaTipoDia(TipoDia horario) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void borraTipoDia(TipoDia horario) {
		// TODO Auto-generated method stub
		
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

	@SuppressWarnings("unused")
	@Override
	public void eliminaTipoDia(Integer id) {
		
		try { //se consume recurso rest
			HttpResponse response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_ELIMINA_TIPODIA + "?id=" + id);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
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

}
