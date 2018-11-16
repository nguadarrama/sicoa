package mx.gob.segob.dgtic.web.mvc.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
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
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import mx.gob.segob.dgtic.web.config.security.constants.AutorizacionConstants;
import mx.gob.segob.dgtic.web.config.security.handler.LogoutCustomHandler;
import mx.gob.segob.dgtic.web.mvc.constants.CatalogoEndPointConstants;
import mx.gob.segob.dgtic.web.mvc.dto.Archivo;
import mx.gob.segob.dgtic.web.mvc.service.ArchivoService;
import mx.gob.segob.dgtic.web.mvc.service.constants.Constantes;
import mx.gob.segob.dgtic.web.mvc.util.rest.ClienteRestUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.HttpResponseUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.exception.ClienteException;
@Service
public class ArchivoServiceImpl implements ArchivoService{

	private static final Logger logger = LoggerFactory.getLogger(LogoutCustomHandler.class);
	
	@Override
	public Archivo guardaArchivo(MultipartFile archivo, String claveUsuario, String accion, String nombreArchivo, Authentication authentication) {
		HttpResponse response;
		@SuppressWarnings("unchecked")
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		Archivo archivoDto = new Archivo();
		Archivo archivoRespuesta;
		HttpEntity httpEntity = new BasicHttpEntity();
		String ruta="/documentos/sicoa/"+claveUsuario+"/"+accion+"/";
		try {
			logger.info("Antes del error");
			archivoDto.setArchivo(IOUtils.toByteArray(archivo.getInputStream()));
			
			
		} catch (IOException e2) {
			logger.warn(".Warn. {} ",e2);
		}
		archivoDto.setUrl(ruta);
		archivoDto.setSize((int) (long) archivo.getSize());
		archivoDto.setActivo(true);
		logger.info("nombre compelto : {}",archivo.getOriginalFilename()+" nombre"+archivo.getName());
		archivoDto.setNombre(nombreArchivo);
		Map<String, Object> content = new HashMap<>();
		content.put("archivo", archivoDto);
		logger.info("Despues del error1");

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			logger.warn("Warn- {} ",e1);
		}
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_GUARDA_ARCHIVO, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			archivoRespuesta = gson.fromJson(dataJson, Archivo.class);		
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al actualizar archivo : "+response.getStatusLine().getReasonPhrase());
		}
		return archivoRespuesta;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Archivo actualizaArchivo(MultipartFile archivo, String claveUsuario, String accion, Integer idArchivo,String nombreArchivo, Authentication authentication) {
		HttpResponse response;
		Archivo archivoRespuesta;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		Archivo archivoDto = new Archivo();
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		String ruta="/documentos/sicoa/"+claveUsuario+"/"+accion+"/";
		try {
			logger.info("..Antes del error");
			archivoDto.setArchivo(IOUtils.toByteArray(archivo.getInputStream()));
			
			
		} catch (IOException e2) {
			logger.warn("Warn.- {} ",e2);
		}
		String nombreaux=archivo.getOriginalFilename();
		
		logger.info("nombreOriginal: {} ",nombreaux);
		archivoDto.setIdArchivo(idArchivo);
		archivoDto.setUrl(ruta);
		archivoDto.setSize((int) (long) archivo.getSize());
		archivoDto.setActivo(true);
		archivoDto.setNombre(nombreArchivo);
		Map<String, Object> content = new HashMap<>();
		content.put("archivo", archivoDto);
		logger.info("Despues del error1");

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			logger.warn("Warns {} ",e1);
		}
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_ACTUALIZA_ARCHIVO, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}

		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			archivoRespuesta = gson.fromJson(dataJson, Archivo.class);		
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al actualizar archivo : "+response.getStatusLine().getReasonPhrase());
		}
		return archivoRespuesta;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Archivo consultaArchivo(Integer idArchivo, Authentication authentication) {
		Archivo archivo;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_BUSCA_ARCHIVO + "?id=" + idArchivo, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			archivo = gson.fromJson(dataJson, Archivo.class);		
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener archivo : "+response.getStatusLine().getReasonPhrase());
		}
		logger.info("datos del archivo que se fue a buscar: {} ",archivo.getUrl());
		return archivo;
	}
	
	private String obtenerMensajeError(HttpResponse response){
		JsonObject respuesta = (JsonObject) HttpResponseUtil.getJsonContent(response);
		JsonObject metadata = (JsonObject)respuesta.get(AutorizacionConstants.ATTR_META_DATA_JSON_NAME);			
		JsonArray jsonArray = metadata.get(AutorizacionConstants.ATTR_META_DATA_ERRORES_JSON_NAME).getAsJsonArray();
		
		return (jsonArray.size() != 0)?jsonArray.get(0).getAsString() : "Error desconocido";
	}
}
