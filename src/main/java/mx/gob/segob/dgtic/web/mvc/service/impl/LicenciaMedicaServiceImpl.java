package mx.gob.segob.dgtic.web.mvc.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import mx.gob.segob.dgtic.web.config.security.constants.AutorizacionConstants;
import mx.gob.segob.dgtic.web.config.security.handler.LogoutCustomHandler;
import mx.gob.segob.dgtic.web.mvc.constants.LicenciaMedicaEndPointConstants;
import mx.gob.segob.dgtic.web.mvc.dto.LicenciaMedica;
import mx.gob.segob.dgtic.web.mvc.service.LicenciaMedicaService;
import mx.gob.segob.dgtic.web.mvc.util.rest.ClienteRestUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.HttpResponseUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.exception.ClienteException;

@Service
public class LicenciaMedicaServiceImpl implements LicenciaMedicaService{

	private static final Logger logger = LoggerFactory.getLogger(LogoutCustomHandler.class);
	
	@Override
	public List<LicenciaMedica> obtenerListaLicenciaMedicaPorFiltros(String claveUsuario, String fechaInicio,
			String fechaFin, String idEstatus) {
		
		List<LicenciaMedica> listaLicencias = new ArrayList<>();
		HttpResponse response;
		try{
			response = ClienteRestUtil.getCliente().get(LicenciaMedicaEndPointConstants.WEB_SERVICE_CONSULTA_LICENCIA_POR_FILTROS+ "?claveUsuario="+claveUsuario+"&idEstatus="+idEstatus+"&fechaInicio="+fechaInicio+"&fechaFin="+fechaFin);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaLicencias = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<LicenciaMedica>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener vacaciones por filtros: "+response.getStatusLine().getReasonPhrase());
		}
		return listaLicencias;
	}
	
	private String obtenerMensajeError(HttpResponse response){
		JsonObject respuesta = (JsonObject) HttpResponseUtil.getJsonContent(response);
		JsonObject metadata = (JsonObject)respuesta.get(AutorizacionConstants.ATTR_META_DATA_JSON_NAME);			
		JsonArray jsonArray = metadata.get(AutorizacionConstants.ATTR_META_DATA_ERRORES_JSON_NAME).getAsJsonArray();
		
		return (jsonArray.size() != 0)?jsonArray.get(0).getAsString() : "Error desconocido";
	}

}