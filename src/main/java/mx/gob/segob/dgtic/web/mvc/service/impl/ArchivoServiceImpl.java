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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import mx.gob.segob.dgtic.web.config.security.handler.LogoutCustomHandler;
import mx.gob.segob.dgtic.web.mvc.constants.CatalogoEndPointConstants;
import mx.gob.segob.dgtic.web.mvc.dto.Archivo;
import mx.gob.segob.dgtic.web.mvc.dto.Horario;
import mx.gob.segob.dgtic.web.mvc.service.ArchivoService;
import mx.gob.segob.dgtic.web.mvc.util.rest.ClienteRestUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.HttpResponseUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.exception.ClienteException;
@Service
public class ArchivoServiceImpl implements ArchivoService{

	private static final Logger logger = LoggerFactory.getLogger(LogoutCustomHandler.class);
	
	@Override
	public Integer guardaArchivo(MultipartFile archivo, String claveUsuario, String accion) {
		HttpResponse response;
		Archivo archivoResponse = null;
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		Archivo archivoDto = new Archivo();
		Header header = new BasicHeader("Authorization", "Bearer %s");
		HttpEntity httpEntity = new BasicHttpEntity();
		//BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
		String ruta="C:/Sicoa/"+claveUsuario+"/"+accion+"/";
		try {
			System.out.println("Antes del error");
			archivoDto.setArchivo(IOUtils.toByteArray(archivo.getInputStream()));
			
			
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		archivoDto.setUrl(ruta);
		archivoDto.setSize((int) (long) archivo.getSize());
		archivoDto.setActivo(true);
		System.out.println("nombre compelto "+archivo.getOriginalFilename()+" nombre"+archivo.getName());
		archivoDto.setNombre(archivo.getOriginalFilename());
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("archivo", archivoDto);
		System.out.println("Despues del error1");

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
			System.out.println("Despues del error2");
		} catch (ClienteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
			archivoResponse = gson.fromJson(dataJson, Archivo.class);		
			
		} 
		
		return archivoResponse.getIdArchivo();
	}
}
