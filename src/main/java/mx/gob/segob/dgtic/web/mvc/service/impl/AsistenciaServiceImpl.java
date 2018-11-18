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
import mx.gob.segob.dgtic.web.mvc.constants.AsistenciaEndPointConstants;
import mx.gob.segob.dgtic.web.mvc.constants.CatalogoEndPointConstants;
import mx.gob.segob.dgtic.web.mvc.dto.Archivo;
import mx.gob.segob.dgtic.web.mvc.dto.Asistencia;
import mx.gob.segob.dgtic.web.mvc.dto.Estatus;
import mx.gob.segob.dgtic.web.mvc.dto.GeneraReporteArchivo;
import mx.gob.segob.dgtic.web.mvc.dto.Horario;
import mx.gob.segob.dgtic.web.mvc.dto.Incidencia;
import mx.gob.segob.dgtic.web.mvc.dto.Justificacion;
import mx.gob.segob.dgtic.web.mvc.dto.TipoDia;
import mx.gob.segob.dgtic.web.mvc.dto.reporte;
import mx.gob.segob.dgtic.web.mvc.service.AsistenciaService;
import mx.gob.segob.dgtic.web.mvc.util.AsistenciaBusquedaUtil;
import mx.gob.segob.dgtic.web.mvc.util.FormatoIncidencia;
import mx.gob.segob.dgtic.web.mvc.util.rest.ClienteRestUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.HttpResponseUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.exception.ClienteException;

@Service
public class AsistenciaServiceImpl implements AsistenciaService {
	
	private static final Logger logger = LoggerFactory.getLogger(LogoutCustomHandler.class);
	private static final String INCIDENCIA = "incidencia";
	private static final String TOKEN = "_token";
	private static final String ERROR = "Error al obtener usuario : ";
	private static final String BEARER = "Bearer ";
	private static final String AUTHORIZATION = "Authorization";
	
	@Override
	public List<Asistencia> buscaAsistenciaEmpleadoMes(String claveEmpleado, Authentication authentication) {
		List<Asistencia> listaAsistencia = new ArrayList<>();
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader(AUTHORIZATION, BEARER + detalles.get(TOKEN).toString());
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(AsistenciaEndPointConstants.WEB_SERVICE_INFO_ASISTENCIA_EMPLEADO + "?claveEmpleado=" + claveEmpleado, header);
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
			throw new AuthenticationServiceException(ERROR + response.getStatusLine().getReasonPhrase());
		}
		
		return listaAsistencia;
	}
	
	@Override
	public List<Asistencia> buscaAsistenciaEmpleadoRango(String claveEmpleado, String inicio, String fin, Authentication authentication) {
		List<Asistencia> listaAsistencia = new ArrayList<>();
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(
					AsistenciaEndPointConstants.WEB_SERVICE_INFO_ASISTENCIA_EMPLEADO_RANGO + "?claveEmpleado=" + claveEmpleado + "&inicio=" + inicio + "&fin=" + fin, header);
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
	public List<Asistencia> buscaAsistenciaEmpleadoRangoCoordinador(AsistenciaBusquedaUtil asistenciaBusquedaUtil, Authentication authentication) {

		List<Asistencia> listaAsistencia = new ArrayList<>();
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		
		HttpEntity httpEntity = new BasicHttpEntity();
		Map<String, Object> content = new HashMap<>();
		content.put("asistenciaBusqueda", asistenciaBusquedaUtil);
		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			e1.printStackTrace();
		}
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(AsistenciaEndPointConstants.WEB_SERVICE_INFO_ASISTENCIA_EMPLEADO_RANGO_COORDINADOR, httpEntity, header);
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
	public List<Asistencia> buscaAsistenciaEmpleadoRangoDireccion(AsistenciaBusquedaUtil asistenciaBusquedaUtil, Authentication authentication) {

		List<Asistencia> listaAsistencia = new ArrayList<>();
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		
		HttpEntity httpEntity = new BasicHttpEntity();
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("asistenciaBusqueda", asistenciaBusquedaUtil);
		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			e1.printStackTrace();
		}
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(AsistenciaEndPointConstants.WEB_SERVICE_INFO_ASISTENCIA_EMPLEADO_RANGO_DIRECCION, httpEntity, header);
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
	public Asistencia buscaAsistenciaPorId(Integer id, Authentication authentication) {
		Asistencia asistencia = new Asistencia();
		HttpResponse response = null;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(AsistenciaEndPointConstants.WEB_SERVICE_INFO_ASISTENCIA_EMPLEADO_ID + "?id=" + id, header);
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
	public Integer creaIncidencia(Integer idAsistencia, Integer idTipoDia, Integer idJustificacion, Integer idArchivo, String nombreAutorizador, Authentication authentication) {
		
		//creación de la justificación para una incidencia
		
		//motivo de justificación
		Justificacion justificacion = new Justificacion();
		justificacion.setIdJustificacion(idJustificacion);
		
		//motivo de incidencia
		TipoDia tipoDia = new TipoDia();
		tipoDia.setIdTipoDia(null);
		
		//la asistencia con incidencia que se quiere justificar
		Asistencia asistencia = new Asistencia();
		asistencia.setIdAsistencia(idAsistencia);
		
		//la justificación se crea con estatus "pendiente"
		Estatus estatus = new Estatus();
		estatus.setIdEstatus(1);
		
		Archivo archivo = new Archivo();
		archivo.setIdArchivo(idArchivo);
				
		//se crea la incidencia con la información 
		Incidencia incidencia = new Incidencia();
		incidencia.setJustificacion(justificacion);
		incidencia.setTipoDia((tipoDia));
		incidencia.setIdAsistencia(asistencia);
		incidencia.setEstatus(estatus);
		incidencia.setIdArchivo(archivo);
		incidencia.setNombreAutorizador(nombreAutorizador);
		incidencia.setDescuento(false);
		
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		
		Map<String, Object> content = new HashMap<String, Object>();
		content.put(INCIDENCIA, incidencia);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			e1.printStackTrace();
		}
		
		HttpResponse response = null;
		Integer respuesta;
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(AsistenciaEndPointConstants.WEB_SERVICE_INFO_ASISTENCIA_JUSTIFICA, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			respuesta = (Integer) json.get("data").getAsInt();
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al crear la incidencia: "+response.getStatusLine().getReasonPhrase());
		}
		
		return respuesta;
		
	}
	
	@Override
	public Integer creaDescuento(Integer idAsistencia, Integer idTipoDia, Integer idJustificacion, Integer idArchivo, String nombreAutorizador, Authentication authentication) {
		
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
		
		Archivo archivo = new Archivo();
		archivo.setIdArchivo(idArchivo);
		
		//se crea la incidencia con la información 
		Incidencia incidencia = new Incidencia();
		incidencia.setJustificacion(justificacion);
		incidencia.setTipoDia((tipoDia));
		incidencia.setIdAsistencia(asistencia);
		incidencia.setEstatus(estatus);
		incidencia.setIdArchivo(archivo);
		incidencia.setNombreAutorizador(nombreAutorizador);
		incidencia.setDescuento(true); //bandera de descuento
		
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		
		Map<String, Object> content = new HashMap<String, Object>();
		content.put(INCIDENCIA, incidencia);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			e1.printStackTrace();
		}
		
		HttpResponse response = null;
		Integer respuesta;
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(AsistenciaEndPointConstants.WEB_SERVICE_INFO_ASISTENCIA_CREA_DESCUENTO, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			respuesta = (Integer) json.get("data").getAsInt();
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException(ERROR + response.getStatusLine().getReasonPhrase());
		}
		
		return respuesta;
	}
	
	@Override
	public Integer aplicaDescuento(Integer idAsistencia, Authentication authentication) {
		
		//la asistencia con incidencia que se quiere descontar
		Asistencia asistencia = new Asistencia();
		asistencia.setIdAsistencia(idAsistencia);
		
		//el descuento se actualiza con estatus "validada"
		Estatus estatus = new Estatus();
		estatus.setIdEstatus(2);
		
		//se crea la incidencia con la información 
		Incidencia incidencia = new Incidencia();
		incidencia.setIdAsistencia(asistencia);
		incidencia.setEstatus(estatus);
		incidencia.setDescuento(true);
		
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("incidencia", incidencia);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			e1.printStackTrace();
		}
		
		HttpResponse response = null;
		Integer respuesta;
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(AsistenciaEndPointConstants.WEB_SERVICE_INFO_ASISTENCIA_APLICA_DESCUENTO, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			respuesta = (Integer) json.get("data").getAsInt();
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al crear el descuento: "+response.getStatusLine().getReasonPhrase());
		}
		
		return respuesta;
	}
	
	@Override
	public Integer dictaminaIncidencia(Integer idAsistencia, Integer idTipoDia, Integer idJustificacion, String dictaminacion, Authentication authentication) {
		//acepta justificación

		//motivo de justificación
		Justificacion justificacion = new Justificacion();
		justificacion.setIdJustificacion(idJustificacion);
		
		//motivo de incidencia
		TipoDia tipoDia = new TipoDia();
		tipoDia.setIdTipoDia(idTipoDia);
		
		//la asistencia con incidencia que se quiere justificar
		Asistencia asistencia = new Asistencia();
		asistencia.setIdAsistencia(idAsistencia);
		
		//se dictamina
		Estatus estatus = new Estatus();
		
		//se crea la incidencia con la información 
		Incidencia incidencia = new Incidencia();
		incidencia.setJustificacion(justificacion);
		incidencia.setTipoDia((tipoDia));
		incidencia.setIdAsistencia(asistencia);
		
		if (dictaminacion.equals("Autorizar")) {
			if (esDescuento(asistencia.getIdAsistencia(), authentication)) {
				estatus.setIdEstatus(2);
				incidencia.setDescuento(true);
			} else { //si la incidencia es justificación, entonces la convierte en descuento y queda pendiente
				estatus.setIdEstatus(2); //validada
				incidencia.setDescuento(false);
			}
		} else if (dictaminacion.equals("Rechazar")) {
			//si la incidencia es descuento, entonces la valida
			if (esDescuento(asistencia.getIdAsistencia(), authentication)) {
				estatus.setIdEstatus(3);
				incidencia.setDescuento(true);
			} else { //si la incidencia es justificación, entonces la convierte en descuento y queda pendiente
				estatus.setIdEstatus(1);
				incidencia.setDescuento(true);
			}
		}
		
		incidencia.setEstatus(estatus);
		
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("incidencia", incidencia);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			e1.printStackTrace();
		}
		
		HttpResponse response = null;
		Integer respuesta;
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(AsistenciaEndPointConstants.WEB_SERVICE_INFO_ASISTENCIA_DICTAMINA, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			respuesta = (Integer) json.get("data").getAsInt();
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al crear el descuento: "+response.getStatusLine().getReasonPhrase());
		}
		
		return respuesta;
		
	}
	
	@Override
	public reporte formatoJustificacion(FormatoIncidencia generaReporteArchivo, Authentication authentication) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		reporte respuesta = new reporte();
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		Map<String, Object> content = new HashMap<>();
		content.put("generaReporteArchivo", generaReporteArchivo);
		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
			response = ClienteRestUtil.getCliente().put(AsistenciaEndPointConstants.WEB_SERVICE_INFO_ASISTENCIA_FORMATO_JUSTIFICACION, httpEntity, header);
			if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
				JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
				try{
					JsonElement dataJson = json.get("data").getAsJsonObject();
					respuesta = gson.fromJson(dataJson, reporte.class);	
				}catch(Exception e){
					e.printStackTrace();
				}		
			} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
				String mensaje = obtenerMensajeError(response);					 
				throw new AuthenticationServiceException(mensaje);			
			} else {
				throw new AuthenticationServiceException("Error al obtener el archivo : "+response.getStatusLine().getReasonPhrase());
			}
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		return respuesta;
	}
	
	@Override
	public reporte formatoDescuento(FormatoIncidencia generaReporteArchivo, Authentication authentication) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		reporte respuesta = new reporte();
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		Map<String, Object> content = new HashMap<>();
		content.put("generaReporteArchivo", generaReporteArchivo);
		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
			response = ClienteRestUtil.getCliente().put(AsistenciaEndPointConstants.WEB_SERVICE_INFO_ASISTENCIA_FORMATO_DESCUENTO, httpEntity, header);
			if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
				JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
				try{
					JsonElement dataJson = json.get("data").getAsJsonObject();
					respuesta = gson.fromJson(dataJson, reporte.class);	
				}catch(Exception e){
					e.printStackTrace();
				}		
			} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
				String mensaje = obtenerMensajeError(response);					 
				throw new AuthenticationServiceException(mensaje);			
			} else {
				throw new AuthenticationServiceException("Error al obtener el archivo : "+response.getStatusLine().getReasonPhrase());
			}
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		return respuesta;
	}
	
	private String obtenerMensajeError(HttpResponse response){
		JsonObject respuesta = (JsonObject) HttpResponseUtil.getJsonContent(response);
		JsonObject metadata = (JsonObject)respuesta.get(AutorizacionConstants.ATTR_META_DATA_JSON_NAME);			
		JsonArray jsonArray = metadata.get(AutorizacionConstants.ATTR_META_DATA_ERRORES_JSON_NAME).getAsJsonArray();
		
		return (jsonArray.size() != 0)?jsonArray.get(0).getAsString() : "Error desconocido";
	}

	@Override
	public List<Asistencia> buscaAsistenciaDireccionReporte(AsistenciaBusquedaUtil asistenciaBusquedaUtil, Authentication authentication) {

		List<Asistencia> listaAsistencia = new ArrayList<>();
		HttpResponse response;
		StringBuilder permisos = new StringBuilder();
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		
		if (asistenciaBusquedaUtil.getP() != null) {
			for (int i = 0; i < asistenciaBusquedaUtil.getP().length; i++ ) {
				permisos.append(asistenciaBusquedaUtil.getP()[i]);
				
				if (i < asistenciaBusquedaUtil.getP().length - 1) {
					permisos.append(",");
				}
			}
		}
		
		asistenciaBusquedaUtil.setPermisos(permisos.toString());
		
		HttpEntity httpEntity = new BasicHttpEntity();
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("asistenciaBusqueda", asistenciaBusquedaUtil);
		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			e1.printStackTrace();
		}
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(AsistenciaEndPointConstants.WEB_SERVICE_INFO_ASISTENCIA_REPORTE_DIRECCION, httpEntity, header);
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
	public List<Asistencia> buscaAsistenciaCoordinadorReporte(AsistenciaBusquedaUtil asistenciaBusquedaUtil, Authentication authentication) {

		
		List<Asistencia> listaAsistencia = new ArrayList<>();
		HttpResponse response;
		StringBuilder permisos = new StringBuilder();
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		
		if (asistenciaBusquedaUtil.getP() != null) {
			for (int i = 0; i < asistenciaBusquedaUtil.getP().length; i++ ) {
				permisos.append(asistenciaBusquedaUtil.getP()[i]);
				
				if (i < asistenciaBusquedaUtil.getP().length - 1) {
					permisos.append(",");
				}
			}
		}
		
		asistenciaBusquedaUtil.setPermisos(permisos.toString());
		
		HttpEntity httpEntity = new BasicHttpEntity();
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("asistenciaBusqueda", asistenciaBusquedaUtil);
		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			e1.printStackTrace();
		}
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(AsistenciaEndPointConstants.WEB_SERVICE_INFO_ASISTENCIA_REPORTE_COORDINADOR, httpEntity, header);
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
			throw new AuthenticationServiceException("Error al información para el reporte de coordinador : "+response.getStatusLine().getReasonPhrase());
		}
		
		return listaAsistencia;
	}
	
	private boolean esDescuento(Integer idIncidencia, Authentication authentication) {
		Incidencia incidencia = new Incidencia();
		HttpResponse response = null;
		boolean esDescuento = false;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get("/catalogo/buscaIncidenciaPorIdAsistencia?id=" + idIncidencia, header);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			incidencia = gson.fromJson(dataJson, Incidencia.class);	
			
			if (incidencia.getDescuento() != null && incidencia.getDescuento()) { //es descuento
				esDescuento = true;
			}
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener la incidencia : "+response.getStatusLine().getReasonPhrase());
		}
		
		return esDescuento;
	}

}
