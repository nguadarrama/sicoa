package mx.gob.segob.dgtic.web.mvc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.multipart.MultipartFile;

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
import mx.gob.segob.dgtic.web.mvc.util.FormatoIncidencia;
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
	public List<Asistencia> buscaAsistenciaEmpleadoRangoCoordinador(String cve_m_usuario, String nombre, String paterno,
			String materno, String nivel, String tipo, String estado, String fechaInicial, String fechaFinal,
			String unidadAdministrativa, String cveCoordinador) {

		List<Asistencia> listaAsistencia = new ArrayList<>();
		HttpResponse response;
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(
					AsistenciaEndPointConstants.WEB_SERVICE_INFO_ASISTENCIA_EMPLEADO_RANGO_COORDINADOR 
					+ "?claveEmpleado=" + cve_m_usuario + "&nombre=" + nombre + "&paterno=" + paterno + "&materno=" + materno + "&nivel=" + nivel
					+ "&tipo=" + tipo + "&estado=" + estado + "&inicio=" + fechaInicial + "&fin=" + fechaFinal + "&unidad=" + unidadAdministrativa
					+ "&cveCoordinador=" + cveCoordinador);
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
	public List<Asistencia> buscaAsistenciaEmpleadoRangoDireccion(String cve_m_usuario, String nombre, String paterno,
			String materno, String nivel, String tipo, String estado, String fechaInicial, String fechaFinal,
			String unidadAdministrativa) {

		List<Asistencia> listaAsistencia = new ArrayList<>();
		HttpResponse response;
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(
					AsistenciaEndPointConstants.WEB_SERVICE_INFO_ASISTENCIA_EMPLEADO_RANGO_DIRECCION 
					+ "?claveEmpleado=" + cve_m_usuario + "&nombre=" + nombre + "&paterno=" + paterno + "&materno=" + materno + "&nivel=" + nivel
					+ "&tipo=" + tipo + "&estado=" + estado + "&inicio=" + fechaInicial + "&fin=" + fechaFinal + "&unidad=" + unidadAdministrativa);
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
	public void creaIncidencia(Integer idAsistencia, Integer idTipoDia, Integer idJustificacion, Integer idArchivo, String nombreAutorizador) {
		
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
		incidencia.setDescuento(false);
		
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
	
	@Override
	public void creaDescuento(Integer idAsistencia, Integer idTipoDia, Integer idJustificacion, Integer idArchivo, String nombreAutorizador) {
		
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
			ClienteRestUtil.getCliente().put(AsistenciaEndPointConstants.WEB_SERVICE_INFO_ASISTENCIA_CREA_DESCUENTO, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
	}
	
	@Override
	public void aplicaDescuento(Integer idAsistencia) {
		
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
			ClienteRestUtil.getCliente().put(AsistenciaEndPointConstants.WEB_SERVICE_INFO_ASISTENCIA_APLICA_DESCUENTO, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
	}
	
	@Override
	public void dictaminaIncidencia(Integer idAsistencia, Integer idTipoDia, Integer idJustificacion, String dictaminacion) {
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
		
		if (dictaminacion.equals("Autorizar")) {
			estatus.setIdEstatus(2); //validada
		} else if (dictaminacion.equals("Rechazar")) {
			estatus.setIdEstatus(3); //rechazada
		}
				
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
			ClienteRestUtil.getCliente().put(AsistenciaEndPointConstants.WEB_SERVICE_INFO_ASISTENCIA_DICTAMINA, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
	}
	
	@Override
	public reporte formatoJustificacion(FormatoIncidencia generaReporteArchivo) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		reporte respuesta = new reporte();
		Header header = new BasicHeader("Authorization", "Bearer %s");
		HttpEntity httpEntity = new BasicHttpEntity();
		Map<String, Object> content = new HashMap<String, Object>();
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
	public reporte formatoDescuento(FormatoIncidencia generaReporteArchivo) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		reporte respuesta = new reporte();
		Header header = new BasicHeader("Authorization", "Bearer %s");
		HttpEntity httpEntity = new BasicHttpEntity();
		Map<String, Object> content = new HashMap<String, Object>();
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

}
