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
import mx.gob.segob.dgtic.web.mvc.constants.AsistenciaEndPointConstants;
import mx.gob.segob.dgtic.web.mvc.constants.CatalogoEndPointConstants;
import mx.gob.segob.dgtic.web.mvc.dto.Asistencia;
import mx.gob.segob.dgtic.web.mvc.dto.Estatus;
import mx.gob.segob.dgtic.web.mvc.dto.Horario;
import mx.gob.segob.dgtic.web.mvc.dto.Incidencia;
import mx.gob.segob.dgtic.web.mvc.dto.Justificacion;
import mx.gob.segob.dgtic.web.mvc.dto.TipoDia;
import mx.gob.segob.dgtic.web.mvc.service.AsistenciaService;
import mx.gob.segob.dgtic.web.mvc.util.rest.ClienteRestUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.HttpResponseUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.exception.ClienteException;

@Service
public class AsistenciaServiceImpl implements AsistenciaService {
	
	private static final Logger logger = LoggerFactory.getLogger(LogoutCustomHandler.class);
	
	@Override
	public List<Asistencia> buscaAsistenciaEmpleadoMes(String claveEmpleado) {
		List<Asistencia> listaAsistencia = new ArrayList<>();
		HttpResponse response;
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(AsistenciaEndPointConstants.WEB_SERVICE_INFO_ASISTENCIA_EMPLEADO + "?claveEmpleado=" + claveEmpleado);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			if(dataJson != null){
				listaAsistencia = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<Asistencia>>(){}.getType());
			}
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener usuario : "+response.getStatusLine().getReasonPhrase());
		}
		
		return listaAsistencia;
	}
	
	@Override
	public List<Asistencia> buscaAsistenciaEmpleadoRango(String claveEmpleado, String inicio, String fin) {
		List<Asistencia> listaAsistencia = new ArrayList<>();
		HttpResponse response;
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(
					AsistenciaEndPointConstants.WEB_SERVICE_INFO_ASISTENCIA_EMPLEADO_RANGO + "?claveEmpleado=" + claveEmpleado + "&inicio=" + inicio + "&fin=" + fin);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			if(dataJson != null){
				listaAsistencia = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<Asistencia>>(){}.getType());
			}
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener usuario : "+response.getStatusLine().getReasonPhrase());
		}
		
		return listaAsistencia;
	}
	
	@Override
	public Asistencia buscaAsistenciaPorId(Integer id) {
		Asistencia asistencia = new Asistencia();
		HttpResponse response = null;
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(AsistenciaEndPointConstants.WEB_SERVICE_INFO_ASISTENCIA_EMPLEADO_ID + "?id=" + id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			asistencia = gson.fromJson(dataJson, Asistencia.class);		
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener usuario : "+response.getStatusLine().getReasonPhrase());
		}
		
		return asistencia;
	}
	
	@Override
	public void creaIncidencia(Integer idAsistencia, Integer idTipoDia, Integer idJustificacion) {
		
		//creación de la justificación para una incidencia
		
		//motivo de justificación
		Justificacion justificacion = new Justificacion();
		justificacion.setIdJustificacion(idJustificacion);
		
		//motivo de incidencia
		TipoDia tipoDia = new TipoDia();
		tipoDia.setIdTipoDia(idTipoDia);
		
		//la asistencia con incidencia que se quiere justificar
		Asistencia asistencia = new Asistencia();
		asistencia.setIdAsistencia(idAsistencia);
		
		//la justificación se crea con estatus "pendiente"
		Estatus estatus = new Estatus();
		estatus.setIdEstatus(1);
				
		//se crea la incidencia con la información 
		Incidencia incidencia = new Incidencia();
		incidencia.setJustificacion(justificacion);
		incidencia.setTipoDia((tipoDia));
		incidencia.setIdAsistencia(asistencia);
		incidencia.setEstatus(estatus);
		
		Header header = new BasicHeader("Authorization", "Bearer %s");
		HttpEntity httpEntity = new BasicHttpEntity();
		//BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
		
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("incidencia", incidencia);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try { //se consume recurso rest
			ClienteRestUtil.getCliente().put(AsistenciaEndPointConstants.WEB_SERVICE_INFO_ASISTENCIA_JUSTIFICA, httpEntity, header);
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
