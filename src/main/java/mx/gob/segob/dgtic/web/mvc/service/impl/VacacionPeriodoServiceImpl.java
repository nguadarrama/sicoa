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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import mx.gob.segob.dgtic.web.config.security.constants.AutorizacionConstants;
import mx.gob.segob.dgtic.web.config.security.handler.LogoutCustomHandler;
import mx.gob.segob.dgtic.web.mvc.constants.CatalogoEndPointConstants;
import mx.gob.segob.dgtic.web.mvc.dto.VacacionPeriodo;
import mx.gob.segob.dgtic.web.mvc.service.VacacionPeriodoService;
import mx.gob.segob.dgtic.web.mvc.util.rest.ClienteRestUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.HttpResponseUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.exception.ClienteException;

@Service
public class VacacionPeriodoServiceImpl implements VacacionPeriodoService{

	private static final Logger logger = LoggerFactory.getLogger(LogoutCustomHandler.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public List<VacacionPeriodo> obtenerUsuariosVacacionesPorFiltros(String claveUsuario, String nombre,
			String apellidoPaterno, String apellidoMaterno,String idUnidad, Authentication authentication) {
		List<VacacionPeriodo> listaUsuariosVacaciones;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		
		try{
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_OBTIENE_USUARIOS_VACACIONES+ "?claveUsuario="+removerEspacios(claveUsuario)+"&nombre="+removerEspacios(nombre)+"&apellidoPaterno="+removerEspacios(apellidoPaterno)+"&apellidoMaterno="+removerEspacios(apellidoMaterno)+"&idUnidad="+idUnidad, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaUsuariosVacaciones = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<VacacionPeriodo>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener vacaciones por filtros: "+response.getStatusLine().getReasonPhrase());
		}
		return listaUsuariosVacaciones;
	}

	private String obtenerMensajeError(HttpResponse response){
		JsonObject respuesta = (JsonObject) HttpResponseUtil.getJsonContent(response);
		JsonObject metadata = (JsonObject)respuesta.get(AutorizacionConstants.ATTR_META_DATA_JSON_NAME);			
		JsonArray jsonArray = metadata.get(AutorizacionConstants.ATTR_META_DATA_ERRORES_JSON_NAME).getAsJsonArray();
		
		return (jsonArray.size() != 0)?jsonArray.get(0).getAsString() : "Error desconocido";
	}
	
	private String removerEspacios(String string) {
	    if(string != null && !string.isEmpty()) {
	      return string.replace(" ", "_");
	    }
	    return string;
	  }
}
