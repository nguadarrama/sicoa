package mx.gob.segob.dgtic.web.mvc.service.impl;

import java.sql.Timestamp;
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

import groovyjarjarcommonscli.ParseException;
import mx.gob.segob.dgtic.web.config.security.constants.AutorizacionConstants;
import mx.gob.segob.dgtic.web.config.security.handler.LogoutCustomHandler;
import mx.gob.segob.dgtic.web.mvc.constants.CatalogoEndPointConstants;
import mx.gob.segob.dgtic.web.mvc.dto.Archivo;
import mx.gob.segob.dgtic.web.mvc.dto.DiaFestivo;
import mx.gob.segob.dgtic.web.mvc.dto.Estatus;
import mx.gob.segob.dgtic.web.mvc.dto.GeneraReporteArchivo;
import mx.gob.segob.dgtic.web.mvc.dto.Horario;
import mx.gob.segob.dgtic.web.mvc.dto.PerfilUsuario;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;
import mx.gob.segob.dgtic.web.mvc.dto.VacacionPeriodo;
import mx.gob.segob.dgtic.web.mvc.dto.Vacaciones;
import mx.gob.segob.dgtic.web.mvc.dto.VacacionesAux;
import mx.gob.segob.dgtic.web.mvc.dto.reporte;
import mx.gob.segob.dgtic.web.mvc.service.CatalogoService;
import mx.gob.segob.dgtic.web.mvc.service.UsuarioService;
import mx.gob.segob.dgtic.web.mvc.service.VacacionesService;
import mx.gob.segob.dgtic.web.mvc.util.rest.ClienteRestUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.HttpResponseUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.exception.ClienteException;

@Service
public class VacacionesServiceImpl implements VacacionesService{
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	private CatalogoService catalogoService;
	
	private static final Logger logger = LoggerFactory.getLogger(LogoutCustomHandler.class);
	
	@Override
	public List<Vacaciones> obtieneVacaciones(Authentication authentication) {
		List<Vacaciones> listaVacaciones = new ArrayList<Vacaciones>();
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		try{
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_INFO_VACACONES, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonArray dataJson = json.getAsJsonArray("data");
			if(dataJson!=null)
			listaVacaciones = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<Vacaciones>>(){}.getType());
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener vacaciones : "+response.getStatusLine().getReasonPhrase());
		}
		return listaVacaciones;
	}

	@Override
	public VacacionesAux obtieneVacacion(String idVacacion, Authentication authentication) {
		VacacionesAux vacaciones = new VacacionesAux();
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		
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
		//System.out.println("Fecha recuperada fin "+vacaciones.getFechaFin());
		vacaciones.setFechaInicio(vacaciones.getFechaInicio());
		vacaciones.setFechaFin(vacaciones.getFechaFin());
		vacaciones.setFechaRegistro(vacaciones.getFechaRegistro());
		Usuario usua= null;
		usua=vacaciones.getIdUsuario();
		usua.setFechaIngreso(usua.getFechaIngreso());
		vacaciones.setIdUsuario(usua);
		
		return vacaciones;
	}
	
	@Override
	public Vacaciones eliminaVacaciones(Integer idVacaciones, Authentication authentication) {
		HttpResponse response;
		Vacaciones vacacio= new Vacaciones();
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		
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
			throw new AuthenticationServiceException("Error al obtener el día Inhábil : "+response.getStatusLine().getReasonPhrase());
		}
		return vacacio;
	}
	
	@Override
	public Vacaciones agregaVacaciones(VacacionesAux vacaciones, String claveUsuario, Authentication authentication) {
		Vacaciones vacacio= new Vacaciones();
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		HttpEntity httpEntity = new BasicHttpEntity();
		HttpResponse response;
		//BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
		Usuario usuario= new Usuario();
		usuario=usuarioService.buscaUsuario(claveUsuario, authentication);
		usuario.setFechaIngreso(null);
		vacaciones.setIdUsuario(usuario);
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("detalleVacacion", vacaciones);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
			throw new AuthenticationServiceException("Error al obtener el día Inhábil : "+response.getStatusLine().getReasonPhrase());
		}
				
		return vacacio;
	}
	
	@Override
	public Vacaciones modificaVacaciones(Vacaciones vacaciones, Authentication authentication) {
		Vacaciones vacacio= new Vacaciones();
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		//BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
		System.out.println("Datos en front "+ vacaciones.getIdDetalle()+ " "+ vacaciones.getIdArchivo().getIdArchivo());
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("detalleVacacion", vacaciones);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
			throw new AuthenticationServiceException("Error al obtener el día Inhábil : "+response.getStatusLine().getReasonPhrase());
		}
		return vacacio;	
	}
	
	private String obtenerMensajeError(HttpResponse response){
		JsonObject respuesta = (JsonObject) HttpResponseUtil.getJsonContent(response);
		JsonObject metadata = (JsonObject)respuesta.get(AutorizacionConstants.ATTR_META_DATA_JSON_NAME);			
		JsonArray jsonArray = metadata.get(AutorizacionConstants.ATTR_META_DATA_ERRORES_JSON_NAME).getAsJsonArray();
		
		return (jsonArray.size() != 0)?jsonArray.get(0).getAsString() : "Error desconocido";
	}

	@Override
	public VacacionPeriodo buscaVacacionPeriodoPorClaveUsuarioYPeriodo(String claveUsuario, Integer idPeriodo, Authentication authentication) {
		VacacionPeriodo vacaciones = new VacacionPeriodo();
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		HttpResponse response;
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("claveUsuario", claveUsuario);
		content.put("idPeriodo", idPeriodo);

		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
				e.printStackTrace();
				//vacaciones.setDias(0);
			}		
			
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener vacaciones : "+response.getStatusLine().getReasonPhrase());
		}
		
		return vacaciones;
	}
	
	@Override
	public Vacaciones aceptaORechazaVacaciones(Vacaciones vacaciones,Integer idDetalle, Authentication authentication) {
		Vacaciones vacacio= new Vacaciones();
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		vacaciones.setIdDetalle(idDetalle);
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		//BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
		
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("detalleVacacion", vacaciones);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_ACEPTAORECHAZA_VACACIONES, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
//			
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
	public List<Vacaciones> obtenerVacacionesPorFiltros(String claveUsuario, String nombre, String apellidoPaterno,
			String apellidoMaterno, String idUnidad, String idEstatus, Authentication authentication) {
		List<Vacaciones> listaVacaciones = new ArrayList<>();
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		try{
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_OBTIENE_VACACIONES_POR_FILTROS+ "?claveUsuario="+removerEspacios(claveUsuario)+"&nombre="+removerEspacios(nombre)+"&apellidoPaterno="+removerEspacios(apellidoPaterno)+"&apellidoMaterno="+removerEspacios(apellidoMaterno)+"&idUnidad="+idUnidad+"&idEstatus="+idEstatus, header);
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
		return listaVacaciones;
	}

	@Override
	public List<Vacaciones> consultaVacacionesPropiasPorFiltros(String claveUsuario, String idPeriodo,
			String idEstatus, String pfechaInicio, String pfechaFin, Authentication authentication) {
		List<Vacaciones> listaVacaciones = new ArrayList<>();
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		try{
			response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_OBTIENE_VACACIONES_PROPIAS+ "?claveUsuario="+removerEspacios(claveUsuario)+"&idEstatus="+idEstatus+"&idPeriodo="+idPeriodo+"&fechaInicio="+pfechaInicio+"&fechaFin="+pfechaFin, header);
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
				System.out.println("fechaInicio "+fechaInicial);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return listaVacaciones;
	}
	
	@Override
	public reporte generaReporte(GeneraReporteArchivo generaReporteArchivo, Authentication authentication) {
		HttpResponse response;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		reporte respuesta = new reporte();
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		HttpEntity httpEntity = new BasicHttpEntity();
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("generaReporteArchivo", generaReporteArchivo);
		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
			response = ClienteRestUtil.getCliente().put(CatalogoEndPointConstants.WEB_SERVICE_GENERA_REPORTE, httpEntity, header);
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
	public String recuperaDiasVacacioness(String claveUsuario, Authentication authentication) {
			List<Vacaciones> listaVacaciones = new ArrayList<>();
			HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
			Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
			String idEstatus="";
			String idUnidad="";
			String idPeriodo="";
			String pfechaInicio="";
			String pfechaFin="";
			HttpResponse response;
			try{
				response = ClienteRestUtil.getCliente().get(CatalogoEndPointConstants.WEB_SERVICE_OBTIENE_VACACIONES_PROPIAS+ "?claveUsuario="+claveUsuario+"&idEstatus="+idEstatus+"&idPeriodo="+idPeriodo+"&fechaInicio="+pfechaInicio+"&fechaFin="+pfechaFin, header);
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
				    List<Date> listaFechasAux = new ArrayList<Date>();
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
				if(!listaFechas.isEmpty() && listaFechas!=null){
				listaFechas=listaFechas.substring(0, (listaFechas.length()- 1));
				}
				//System.out.println("Fecha de fechas desde el array "+listaFechas);
			
			return listaFechas;
	}

	@Override
	public Vacaciones cancelaVacaciones(Integer idDetalle, Authentication authentication) {
		HttpResponse response;
		Vacaciones vacacio= new Vacaciones();
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		
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

	private String removerEspacios(String string) {
	    if(string != null && !string.isEmpty()) {
	      return string.replace(" ", "_");
	    }
	    return string;
	  }
	


}
