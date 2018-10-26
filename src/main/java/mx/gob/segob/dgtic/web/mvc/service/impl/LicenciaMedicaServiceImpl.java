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
import mx.gob.segob.dgtic.web.mvc.constants.CatalogoEndPointConstants;
import mx.gob.segob.dgtic.web.mvc.constants.LicenciaMedicaEndPointConstants;
import mx.gob.segob.dgtic.web.mvc.dto.Estatus;
import mx.gob.segob.dgtic.web.mvc.dto.LicenciaMedica;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;
import mx.gob.segob.dgtic.web.mvc.dto.Vacaciones;
import mx.gob.segob.dgtic.web.mvc.service.LicenciaMedicaService;
import mx.gob.segob.dgtic.web.mvc.service.UsuarioService;
import mx.gob.segob.dgtic.web.mvc.util.rest.ClienteRestUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.HttpResponseUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.exception.ClienteException;

@Service
public class LicenciaMedicaServiceImpl implements LicenciaMedicaService{
	

	private static final Logger logger = LoggerFactory.getLogger(LogoutCustomHandler.class);
	
	@Autowired
	private UsuarioService usuarioService;
	
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

	@Override
	public List<LicenciaMedica> obtenerListaLicenciaMedicaEmpleados(String claveUsuario, String nombre,
			String apellidoPaterno, String apellidoMaterno, String idEstatus, String idUnidad) {
		List<LicenciaMedica> listaLicencias = new ArrayList<>();
		HttpResponse response;
		try{
			response = ClienteRestUtil.getCliente().get(LicenciaMedicaEndPointConstants.WEB_SERVICE_CONSULTA_LICENCIA_EMPLEADOS+ "?claveUsuario="+claveUsuario+"&idEstatus="+idEstatus+"&nombre="+nombre
					+"&apellidoPaterno="+apellidoPaterno+"&apellidoMaterno="+apellidoMaterno+"&idUnidad="+idUnidad);
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

	@Override
	public void AgregaLicenciaMedica(LicenciaMedica licenciaMedica, String claveUsuario) {
		Header header = new BasicHeader("Authorization", "Bearer %s");
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		HttpEntity httpEntity = new BasicHttpEntity();
		HttpResponse response;
		//BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
		Usuario usuario= new Usuario();
		usuario=usuarioService.buscaUsuario(claveUsuario);
		licenciaMedica.setIdUsuario(usuario);
		Estatus idEstatus= new Estatus();
		idEstatus.setIdEstatus(1);
		licenciaMedica.setIdEstatus(idEstatus);
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("licenciaMedica", licenciaMedica);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try { //se consume recurso rest
			response=ClienteRestUtil.getCliente().put(LicenciaMedicaEndPointConstants.WEB_SERVICE_AGREGA_LICENCIA, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
//		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
//			
//			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
//			JsonElement dataJson = json.get("data").getAsJsonObject();
//			vacacio = gson.fromJson(dataJson, Vacaciones.class);		
//		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
//			String mensaje = obtenerMensajeError(response);					 
//			throw new AuthenticationServiceException(mensaje);			
//		} else {
//			throw new AuthenticationServiceException("Error al obtener el día Inhábil : "+response.getStatusLine().getReasonPhrase());
//		}
//				
//		return vacacio;
		
	}

	@Override
	public LicenciaMedica buscaLicenciaMedica(Integer idLicencia) {
		LicenciaMedica licencia = new LicenciaMedica();
		HttpResponse response;
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(LicenciaMedicaEndPointConstants.WEB_SERVICE_BUSCA_LICENCIA + "?id=" + idLicencia);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			licencia = gson.fromJson(dataJson, LicenciaMedica.class);		
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener vacaciones : "+response.getStatusLine().getReasonPhrase());
		}
		
		return licencia;
	}

}
