package mx.gob.segob.dgtic.web.mvc.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import mx.gob.segob.dgtic.web.mvc.constants.CatalogoEndPointConstants;
import mx.gob.segob.dgtic.web.mvc.dto.BusquedaDto;
import mx.gob.segob.dgtic.web.mvc.dto.Estatus;
import mx.gob.segob.dgtic.web.mvc.dto.GeneraReporteArchivo;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;
import mx.gob.segob.dgtic.web.mvc.dto.VacacionPeriodo;
import mx.gob.segob.dgtic.web.mvc.dto.Vacaciones;
import mx.gob.segob.dgtic.web.mvc.dto.VacacionesAux;
import mx.gob.segob.dgtic.web.mvc.dto.reporte;
import mx.gob.segob.dgtic.web.mvc.service.UsuarioService;
import mx.gob.segob.dgtic.web.mvc.service.VacacionesService;
import mx.gob.segob.dgtic.web.mvc.service.constants.Constantes;
import mx.gob.segob.dgtic.web.mvc.util.rest.ClienteRestUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.HttpResponseUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.exception.ClienteException;

@Service
public class VacacionesServiceImpl implements VacacionesService{
	
	@Autowired
	UsuarioService usuarioService;
	
	private static final Logger logger = LoggerFactory.getLogger(LogoutCustomHandler.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Vacaciones> obtieneVacaciones(Authentication authentication) {
		List<Vacaciones> listaVacaciones = new ArrayList<>();
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		try{
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_INFO_VACACONES, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			if(dataJson!=null) {
			listaVacaciones = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<Vacaciones>>(){}.getType());
			}
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener vacaciones.. : "+response.getStatusLine().getReasonPhrase());
		}
		return listaVacaciones;
	}

	@SuppressWarnings("unchecked")
	@Override
	public VacacionesAux obtieneVacacion(String idVacacion, Authentication authentication) {
		VacacionesAux vacaciones ;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_BUSCA_VACACIONES + "?id=" + idVacacion, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			vacaciones = gson.fromJson(dataJson, VacacionesAux.class);		
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener vacaciones : "+response.getStatusLine().getReasonPhrase());
		}
		vacaciones.setFechaInicio(vacaciones.getFechaInicio());
		vacaciones.setFechaFin(vacaciones.getFechaFin());
		vacaciones.setFechaRegistro(vacaciones.getFechaRegistro());
		Usuario usua= null;
		usua=vacaciones.getIdUsuario();
		usua.setFechaIngreso(usua.getFechaIngreso());
		vacaciones.setIdUsuario(usua);
		
		return vacaciones;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Vacaciones eliminaVacaciones(Integer idVacaciones, Authentication authentication) {
		HttpResponse response;
		Vacaciones vacacio;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_ELIMINA_VACACIONES + "?id=" + idVacaciones, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			vacacio = gson.fromJson(dataJson, Vacaciones.class);		
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener el día Inhábil.- : "+response.getStatusLine().getReasonPhrase());
		}
		return vacacio;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Vacaciones agregaVacaciones(VacacionesAux vacaciones, String claveUsuario, Authentication authentication) {
		Vacaciones vacacio;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		HttpEntity httpEntity = new BasicHttpEntity();
		HttpResponse response;
		Usuario usuario;
		usuario=usuarioService.buscaUsuario(claveUsuario, authentication);
		//usuario.setFechaIngreso(null);
		vacaciones.setIdUsuario(usuario);
		Map<String, Object> content = new HashMap<>();
		content.put(Constantes.DETALLEVACACION, vacaciones);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			logger.warn(".Warn : {} ",e1);
		}
		
		try { //se consume recurso rest
			response=ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_AGREGA_VACACIONES, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			vacacio = gson.fromJson(dataJson, Vacaciones.class);		
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener el día Inhábil... : "+response.getStatusLine().getReasonPhrase());
		}
				
		return vacacio;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Vacaciones modificaVacaciones(Vacaciones vacaciones, Authentication authentication) {
		Vacaciones vacacio;
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		logger.info("Datos en front: {} ", vacaciones.getIdDetalle()+ " "+ vacaciones.getIdArchivo().getIdArchivo());
		Map<String, Object> content = new HashMap<>();
		content.put(Constantes.DETALLEVACACION, vacaciones);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			logger.warn("Warn-. {} ",e1);
		}
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_MODIFICA_VACACIONES, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			vacacio = gson.fromJson(dataJson, Vacaciones.class);		
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener el día Inhábil -: "+response.getStatusLine().getReasonPhrase());
		}
		return vacacio;	
	}
	
	private String obtenerMensajeError(HttpResponse response){
		JsonObject respuesta = (JsonObject) HttpResponseUtil.getJsonContent(response);
		JsonObject metadata = (JsonObject)respuesta.get(AutorizacionConstants.ATTR_META_DATA_JSON_NAME);			
		JsonArray jsonArray = metadata.get(AutorizacionConstants.ATTR_META_DATA_ERRORES_JSON_NAME).getAsJsonArray();
		
		return (jsonArray.size() != 0)?jsonArray.get(0).getAsString() : "Error desconocido";
	}

	@SuppressWarnings("unchecked")
	@Override
	public VacacionPeriodo buscaVacacionPeriodoPorClaveUsuarioYPeriodo(String claveUsuario, Integer idPeriodo, Authentication authentication) {
		VacacionPeriodo vacaciones = new VacacionPeriodo();
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		HttpResponse response;
		Map<String, Object> content = new HashMap<>();
		content.put("claveUsuario", claveUsuario);
		content.put("idPeriodo", idPeriodo);

		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			logger.warn("Warn-.- {} ",e1);
		}
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_CONSULTA_VACACION, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			try{
			JsonElement dataJson = json.get("data").getAsJsonObject();
			vacaciones = gson.fromJson(dataJson, VacacionPeriodo.class);	
			}catch(Exception e){
				logger.warn("Warn-.-. {} ",e);
			}		
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener vacaciones : "+response.getStatusLine().getReasonPhrase());
		}
		
		return vacaciones;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Vacaciones aceptaORechazaVacaciones(Vacaciones vacaciones,Integer idDetalle, Authentication authentication) {
		Vacaciones vacacio;
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		vacaciones.setIdDetalle(idDetalle);
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		HttpEntity httpEntity = new BasicHttpEntity();

		Map<String, Object> content = new HashMap<>();
		content.put(Constantes.DETALLEVACACION, vacaciones);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			logger.warn("...Warn {} ",e1);
		}
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_ACEPTAORECHAZA_VACACIONES, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			vacacio = gson.fromJson(dataJson, Vacaciones.class);		
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener el día Inhábil : "+response.getStatusLine().getReasonPhrase());
		}
		return vacacio;
	}

	@Override
	public List<Vacaciones> obtenerVacacionesPorFiltros(BusquedaDto busquedaDto, Authentication authentication) {
		List<Vacaciones> listaVacaciones;
		HttpResponse response;
		@SuppressWarnings("unchecked")
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		
		Map<String, Object> content = new HashMap<>();
		content.put(Constantes.BUSQUEDA, busquedaDto);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			logger.warn("Warn-.- :{} ",e1);
		}
		try{
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_OBTIENE_VACACIONES_POR_FILTROS,httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaVacaciones = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<Vacaciones>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener vacaciones por filtros....: "+response.getStatusLine().getReasonPhrase());
		}
		return listaVacaciones;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Vacaciones> consultaVacacionesPropiasPorFiltros(BusquedaDto busquedaDto, Authentication authentication) {
		List<Vacaciones> listaVacaciones;
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		
		Map<String, Object> content = new HashMap<>();
		content.put(Constantes.BUSQUEDA, busquedaDto);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			logger.warn("Warn-.- {} ",e1);
		}
		try{
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_OBTIENE_VACACIONES_PROPIAS,httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			listaVacaciones = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<Vacaciones>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener vacaciones por filtros: "+response.getStatusLine().getReasonPhrase());
		}
		for(Vacaciones vacaciones: listaVacaciones){
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	    	String fechaInicial =null;
	    	String fechaFinal = null;
	    	Estatus estatus= new Estatus();
	    	Date nuevaFecha = new Date();
	    	Date nuevaFecha1 = new Date();
	        estatus.setIdEstatus(2);
	        fechaInicial = df.format(vacaciones.getFechaInicio());
    		fechaFinal=df.format(vacaciones.getFechaFin());
	        try {
	    		
	    		nuevaFecha=df.parse(fechaInicial);
	    		nuevaFecha1=df.parse(fechaFinal);
	    		vacaciones.setFechaInicio(nuevaFecha);
	    		vacaciones.setFechaFin(nuevaFecha1);
				logger.info("fechaInicio : {} ",fechaInicial);
			} catch (java.text.ParseException e) {
				logger.warn("Warn.-.- {} ",e);
			}
		}
		return listaVacaciones;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public reporte generaReporte(GeneraReporteArchivo generaReporteArchivo, Authentication authentication) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		reporte respuesta = new reporte();
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		Map<String, Object> content = new HashMap<>();
		content.put("generaReporteArchivo", generaReporteArchivo);
		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_GENERA_REPORTE, httpEntity, header);
			if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
				JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
				
					JsonElement dataJson = json.get("data").getAsJsonObject();
					respuesta = gson.fromJson(dataJson, reporte.class);	
					
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

	@SuppressWarnings("unchecked")
	@Override
	public String recuperaDiasVacacioness(String claveUsuario, Authentication authentication) {
			List<Vacaciones> listaVacaciones;
			HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
			Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
			BusquedaDto busquedaDto = new BusquedaDto();
			busquedaDto.setIdEstatus("");
			busquedaDto.setIdUnidad("");
			busquedaDto.setIdPeriodo("");
			busquedaDto.setFechaInicio("");
			busquedaDto.setFechaFin("");
			busquedaDto.setClaveUsuario(claveUsuario);
			HttpResponse response;
			
			HttpEntity httpEntity = new BasicHttpEntity();
			
			Map<String, Object> content = new HashMap<>();
			content.put(Constantes.BUSQUEDA, busquedaDto);

			try {
				httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
			} catch (ClienteException e1) {
				logger.warn("...Warn-.- {} ",1);
			}
			try{
				response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_OBTIENE_VACACIONES_PROPIAS, httpEntity, header);
			} catch (ClienteException e) {
				logger.error(e.getMessage(), e);
				throw new AuthenticationServiceException(e.getMessage(), e);
			}
			
			if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
				
				JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
				JsonArray dataJson = json.getAsJsonArray("data");
				listaVacaciones = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<Vacaciones>>(){}.getType());
				
			} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
				
				String mensaje = obtenerMensajeError(response);					 
				throw new AuthenticationServiceException(mensaje);			
			} else {
				throw new AuthenticationServiceException("Error al obtener vacaciones por filtros: "+response.getStatusLine().getReasonPhrase());
			}
			String listaFechas="";
			for(Vacaciones vacaciones: listaVacaciones){
				if(vacaciones.getIdEstatus().getIdEstatus()!=3 && vacaciones.getIdEstatus().getIdEstatus()!=6){
					Date fechaInicio=vacaciones.getFechaInicio();
					Date fechaFin=vacaciones.getFechaFin();
					
					Calendar c1 = Calendar.getInstance();
				    c1.setTime(fechaInicio);
				    Calendar c2 = Calendar.getInstance();
				    c2.setTime(fechaFin);
				    List<Date> listaFechasAux = new ArrayList<>();
				    while (!c1.after(c2)) {
				        listaFechasAux.add(c1.getTime());
				        c1.add(Calendar.DAY_OF_MONTH, 1);
				    }
				    SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
				    String fecha=null;
				    for (Iterator<Date> it = listaFechasAux.iterator(); it.hasNext();) {
				        Date date = it.next();
				        fecha = sdf1.format(date);
				        listaFechas+=""+fecha+",";
				    }
			}
				    
			}
				if(!listaFechas.isEmpty()){
				listaFechas=listaFechas.substring(0, (listaFechas.length()- 1));
				}

			return listaFechas;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vacaciones cancelaVacaciones(Integer idDetalle, Authentication authentication) {
		HttpResponse response;
		Vacaciones vacacio;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
		Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION, Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_CANCELA_VACACIONES + "?id=" + idDetalle, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			vacacio = gson.fromJson(dataJson, Vacaciones.class);		
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener el día Inhábil : "+response.getStatusLine().getReasonPhrase());
		}
		return vacacio;
	}

}
