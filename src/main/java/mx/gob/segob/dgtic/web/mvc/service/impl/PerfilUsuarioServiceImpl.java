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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import mx.gob.segob.dgtic.web.config.security.constants.AutorizacionConstants;
import mx.gob.segob.dgtic.web.config.security.handler.LogoutCustomHandler;
import mx.gob.segob.dgtic.web.mvc.constants.CatalogoEndPointConstants;
import mx.gob.segob.dgtic.web.mvc.dto.PerfilUsuario;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;
import mx.gob.segob.dgtic.web.mvc.service.PerfilUsuarioService;
import mx.gob.segob.dgtic.web.mvc.util.rest.ClienteRestUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.HttpResponseUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.exception.ClienteException;

@Service
public class PerfilUsuarioServiceImpl implements PerfilUsuarioService{

	private static final Logger logger = LoggerFactory.getLogger(LogoutCustomHandler.class);
	
	@Override
	public List<PerfilUsuario> recuperaPerfilesUsuario(String claveUsuario) {
		
		List<PerfilUsuario> listaUsuarioPerfil = new ArrayList<>();
		HttpResponse response;
		try{
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_BUSCA_PERFILES_USUARIO+ "?id=" + claveUsuario);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaUsuarioPerfil = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<PerfilUsuario>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener los perfiles : "+response.getStatusLine().getReasonPhrase());
		}
		return listaUsuarioPerfil;
	}

	@Override
	public void guardaEliminaPerfilesUsuario(Integer[] clavePerfil, String claveUsuario) {
		Header header = new BasicHeader("Authorization", "Bearer %s");
		HttpEntity httpEntity = new BasicHttpEntity();
		//BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
		PerfilUsuario perfilUsuario = new PerfilUsuario();
		perfilUsuario.setIdsPerfil(clavePerfil);
		Usuario usuario = new Usuario();
		usuario.setClaveUsuario(claveUsuario);
		perfilUsuario.setClaveUsuario(usuario);
		
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("usuarioPerfil", perfilUsuario);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try { //se consume recurso rest
			ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_AGREGA_PERFILES_USUARIO, httpEntity, header);
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

}
