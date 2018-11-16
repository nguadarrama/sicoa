package mx.gob.segob.dgtic.web.mvc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
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
import mx.gob.segob.dgtic.web.mvc.dto.Periodo;
import mx.gob.segob.dgtic.web.mvc.service.PeriodoService;
import mx.gob.segob.dgtic.web.mvc.service.constants.Constantes;
import mx.gob.segob.dgtic.web.mvc.util.rest.ClienteRestUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.HttpResponseUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.exception.ClienteException;
@Service
public class PeriodoServiceImpl implements PeriodoService{

	private static final Logger logger = LoggerFactory.getLogger(LogoutCustomHandler.class);
	
	@Override
	public Periodo buscaPeriodoPorClaveUsuario(String claveUsuario, Authentication authentication) {
		Periodo periodo;
		HttpResponse response;
		@SuppressWarnings("unchecked")
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		logger.info("usuario: {} ",claveUsuario);
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_BUSCA_PERFIL_POR_CLAVE_USUARIO + "?claveUsuario=" + claveUsuario, header);
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
			throw new AuthenticationServiceException("Error al obtener perfil : "+response.getStatusLine().getReasonPhrase());
		}
		
		return periodo;
	}
	
	private String obtenerMensajeError(HttpResponse response){
		JsonObject respuesta = (JsonObject) HttpResponseUtil.getJsonContent(response);
		JsonObject metadata = (JsonObject)respuesta.get(AutorizacionConstants.ATTR_META_DATA_JSON_NAME);			
		JsonArray jsonArray = metadata.get(AutorizacionConstants.ATTR_META_DATA_ERRORES_JSON_NAME).getAsJsonArray();
		
		return (jsonArray.size() != 0)?jsonArray.get(0).getAsString() : "Error desconocido";
	}

	@Override
	public List<Periodo> periodos(Authentication authentication) {
		List<Periodo> listaPeriodo;
		HttpResponse response;
		@SuppressWarnings("unchecked")
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_OBTIENE_PERIODOS, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaPeriodo = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<Periodo>>(){}.getType());
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener catalogo de periodos vacacionales: "+response.getStatusLine().getReasonPhrase());
		}
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String gsonT = gson.toJson(listaPeriodo);
				logger.info("ListaService: {} ", gsonT);
		return listaPeriodo;
	}

}
