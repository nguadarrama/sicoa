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
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import mx.gob.segob.dgtic.web.config.security.constants.AutorizacionConstants;
import mx.gob.segob.dgtic.web.config.security.handler.LogoutCustomHandler;
import mx.gob.segob.dgtic.web.mvc.constants.CatalogoEndPointConstants;
import mx.gob.segob.dgtic.web.mvc.dto.PerfilUsuario;
import mx.gob.segob.dgtic.web.mvc.dto.UnidadAdministrativa;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;
import mx.gob.segob.dgtic.web.mvc.dto.UsuarioUnidadAdministrativa;
import mx.gob.segob.dgtic.web.mvc.service.UnidadAdministrativaService;
import mx.gob.segob.dgtic.web.mvc.util.rest.ClienteRestUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.HttpResponseUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.exception.ClienteException;
@Service
public class UnidadAdministrativaServiceImpl implements UnidadAdministrativaService {
	
	/**
     *  Instancia para realizar log.
     */
	private static final Logger logger = LoggerFactory.getLogger(LogoutCustomHandler.class);
	
	@Override
	public List<UsuarioUnidadAdministrativa> obtenerListaUnidadAdministrativa() {
		List<UsuarioUnidadAdministrativa> listaUnidadAdministrativa = new ArrayList<>();
		HttpResponse response;
		try{
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_INFO_UNIDAD_ADMINISTRATIVA);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaUnidadAdministrativa = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<UsuarioUnidadAdministrativa>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener unidades administrativas : "+response.getStatusLine().getReasonPhrase());
		}
		return listaUnidadAdministrativa;
	}
	
	private String obtenerMensajeError(HttpResponse response){
		JsonObject respuesta = (JsonObject) HttpResponseUtil.getJsonContent(response);
		JsonObject metadata = (JsonObject)respuesta.get(AutorizacionConstants.ATTR_META_DATA_JSON_NAME);			
		JsonArray jsonArray = metadata.get(AutorizacionConstants.ATTR_META_DATA_ERRORES_JSON_NAME).getAsJsonArray();
		
		return (jsonArray.size() != 0)?jsonArray.get(0).getAsString() : "Error desconocido";
	}

	@Override
	public void consultaRegistraUsuarioUnidadAdministrativa(Integer idUnidad, String claveUsuario) {
HttpResponse response;
		UnidadAdministrativa unidadAdministrativa= new UnidadAdministrativa();
		Usuario usuario = new Usuario();
		usuario.setClaveUsuario(claveUsuario);
		unidadAdministrativa.setIdUnidad(idUnidad);
		UsuarioUnidadAdministrativa usuarioUnidadAdministrativa= new UsuarioUnidadAdministrativa();
		usuarioUnidadAdministrativa.setClaveUsuario(usuario);
		usuarioUnidadAdministrativa.setIdUnidad(unidadAdministrativa);
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		
		Header header = new BasicHeader("Authorization", "Bearer %s");
		HttpEntity httpEntity = new BasicHttpEntity();
		//BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
		
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("UsuarioUnidadAdministrativa", usuarioUnidadAdministrativa);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_GUARDA_USUARIO_UNIDAD_ADMINISTRATIVA, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
	}

	@Override
	public List<UsuarioUnidadAdministrativa> consultaResponsable(String claveUsuario) {
		List<UsuarioUnidadAdministrativa> listaUsuarioUnidadAdministrativa = new ArrayList<>();
		HttpResponse response;
		try{
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_CONSULTA_RESPONSABLE+ "?id=" + claveUsuario);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaUsuarioUnidadAdministrativa = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<UsuarioUnidadAdministrativa>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener los perfiles : "+response.getStatusLine().getReasonPhrase());
		}
		return listaUsuarioUnidadAdministrativa;
	}

	@Override
	public List<UsuarioUnidadAdministrativa> obtenerUnidadesAdministrativas() {
		List<UsuarioUnidadAdministrativa> listaUnidadAdministrativa = new ArrayList<>();
		HttpResponse response;
		try{
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_INFO_UNIDADES_ADMINISTRATIVAS);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaUnidadAdministrativa = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<UsuarioUnidadAdministrativa>>(){}.getType());
			for(UsuarioUnidadAdministrativa usuarioUnidadAdministrativa: listaUnidadAdministrativa){
				System.out.println("nombre "+usuarioUnidadAdministrativa.getIdUnidad().getNombre());
				System.out.println("nombre "+usuarioUnidadAdministrativa.getIdUnidad().getIdUnidad());
			}
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener unidades administrativas : "+response.getStatusLine().getReasonPhrase());
		}
		return listaUnidadAdministrativa;
	}

}
