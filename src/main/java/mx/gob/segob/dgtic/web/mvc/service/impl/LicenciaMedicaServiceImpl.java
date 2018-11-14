package mx.gob.segob.dgtic.web.mvc.service.impl;

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
import mx.gob.segob.dgtic.web.mvc.constants.LicenciaMedicaEndPointConstants;
import mx.gob.segob.dgtic.web.mvc.dto.Estatus;
import mx.gob.segob.dgtic.web.mvc.dto.LicenciaMedica;
import mx.gob.segob.dgtic.web.mvc.dto.LicenciaMedicaAux;
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
			String fechaFin, String idEstatus, Authentication authentication) {
		
		List<LicenciaMedica> listaLicencias = new ArrayList<>();
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		
		try{
			response = ClienteRestUtil.getCliente().get(LicenciaMedicaEndPointConstants.WEB_SERVICE_CONSULTA_LICENCIA_POR_FILTROS+ "?claveUsuario="+claveUsuario+"&idEstatus="+idEstatus+"&fechaInicio="+fechaInicio+"&fechaFin="+fechaFin, header);
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
			String apellidoPaterno, String apellidoMaterno, String idEstatus, String idUnidad, Authentication authentication) {
		List<LicenciaMedica> listaLicencias = new ArrayList<>();
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		
		try{
			response = ClienteRestUtil.getCliente().get(LicenciaMedicaEndPointConstants.WEB_SERVICE_CONSULTA_LICENCIA_EMPLEADOS+ "?claveUsuario="+claveUsuario+"&idEstatus="+idEstatus+"&nombre="+nombre
					+"&apellidoPaterno="+apellidoPaterno+"&apellidoMaterno="+apellidoMaterno+"&idUnidad="+idUnidad, header);
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
	public LicenciaMedica AgregaLicenciaMedica(LicenciaMedicaAux licenciaMedica, String claveUsuario, Authentication authentication) {
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		HttpEntity httpEntity = new BasicHttpEntity();
		HttpResponse response;
		LicenciaMedica licenciaMedicaRespuesta= new LicenciaMedica();
		//BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
		Usuario usuario= new Usuario();
		usuario=usuarioService.buscaUsuario(claveUsuario, authentication);
		System.out.println("id del usuario "+usuario.getIdUsuario());
		licenciaMedica.setIdUsuario(usuario.getIdUsuario());
		licenciaMedica.setIdEstatus(1);
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
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			licenciaMedicaRespuesta = gson.fromJson(dataJson, LicenciaMedica.class);		
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener el día Inhábil : "+response.getStatusLine().getReasonPhrase());
		}
		return licenciaMedicaRespuesta;
		
	}

	@Override
	public LicenciaMedica buscaLicenciaMedica(Integer idLicencia, Authentication authentication) {
		LicenciaMedica licencia = new LicenciaMedica();
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(LicenciaMedicaEndPointConstants.WEB_SERVICE_BUSCA_LICENCIA + "?id=" + idLicencia, header);
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
		licencia.setFechaInicio(licencia.getFechaInicio());
		licencia.setFechaFin(licencia.getFechaFin());
		licencia.setFechaRegistro(licencia.getFechaRegistro());
		Usuario usua= null;
		usua=licencia.getIdUsuario();
		usua.setFechaIngreso(usua.getFechaIngreso());
		licencia.setIdUsuario(usua);
		return licencia;
	}

	@Override
	public List<LicenciaMedica> obtenerLicenciasPorUnidad(String idUnidad,String claveUsuario, String nombre,
			String apellidoPaterno, String apellidoMaterno, Authentication authentication) {
		List<LicenciaMedica> listaLicencias = new ArrayList<>();
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		
		try{
			response = ClienteRestUtil.getCliente().get(LicenciaMedicaEndPointConstants.WEB_SERVICE_CONSULTA_LICENCIA_POR_UNIDAD+ "?idUnidad="+idUnidad+"&claveUsuario="+claveUsuario+"&nombre="+nombre
					+"&apellidoPaterno="+apellidoPaterno+"&apellidoMaterno="+apellidoMaterno, header);
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
		System.out.println("Recuperados "+listaLicencias.size());
		for(LicenciaMedica licencia: listaLicencias){
			if(licencia.getTotalLicencias()==null || licencia.getTotalLicencias().toString().isEmpty()){
				licencia.setTotalLicencias("0");
			}
			if(licencia.getDiasTotales()==null || licencia.getDiasTotales().toString().isEmpty()){
				licencia.setDiasTotales("0");
			}
		}
		return listaLicencias;
	}

	@Override
	public LicenciaMedica modificaLicenciaMedica(LicenciaMedicaAux licenciaMedica, String claveUsuario, Authentication authentication) {
		LicenciaMedica licenciaMedicaRespuesta= new LicenciaMedica();
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		HttpEntity httpEntity = new BasicHttpEntity();
		HttpResponse response;
		//BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
		Usuario usuario= new Usuario();
		usuario=usuarioService.buscaUsuario(claveUsuario, authentication);
		System.out.println("IdUsuario recuperado "+usuario.getIdUsuario());
		licenciaMedica.setIdUsuario(usuario.getIdUsuario());
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("licenciaMedica", licenciaMedica);

		try {
			httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
		} catch (ClienteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try { //se consume recurso rest
			response=ClienteRestUtil.getCliente().put(LicenciaMedicaEndPointConstants.WEB_SERVICE_MODIFICA_LICENCIA, httpEntity, header);
		} catch (ClienteException e) {
			logger.error(e.getMessage(), e);
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
			
			JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
			JsonElement dataJson = json.get("data").getAsJsonObject();
			licenciaMedicaRespuesta = gson.fromJson(dataJson, LicenciaMedica.class);		
		} else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
			String mensaje = obtenerMensajeError(response);					 
			throw new AuthenticationServiceException(mensaje);			
		} else {
			throw new AuthenticationServiceException("Error al obtener el día Inhábil : "+response.getStatusLine().getReasonPhrase());
		}
		return licenciaMedicaRespuesta;
	}

	@Override
	public LicenciaMedica buscaDiasLicenciaMedica(String claveUsuario, Authentication authentication) {
		LicenciaMedica licencia = new LicenciaMedica();
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
		
		try { //se consume recurso rest
			response = ClienteRestUtil.getCliente().get(LicenciaMedicaEndPointConstants.WEB_SERVICE_CONSULTA_DIAS_LICENCIA + "?claveUsuario=" + claveUsuario, header);
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

	@Override
	public String consultaDiasPorBloquear(String claveUsuario, Authentication authentication) {
		List<LicenciaMedica> listaLicencias = new ArrayList<>();
		HttpResponse response;
		HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();
		//Se agrega el JWT a la cabecera para acceso al recurso rest
		Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
		String apellidoPaterno="";
		String apellidoMaterno="";
		String idUnidad="";
		String nombre="";
		String idEstatus="";
		
		try{
			response = ClienteRestUtil.getCliente().get(LicenciaMedicaEndPointConstants.WEB_SERVICE_CONSULTA_LICENCIA_EMPLEADOS+ "?claveUsuario="+claveUsuario+"&idEstatus="+idEstatus+"&nombre="+nombre
					+"&apellidoPaterno="+apellidoPaterno+"&apellidoMaterno="+apellidoMaterno+"&idUnidad="+idUnidad, header);
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
		
		String listaFechas="";
		SimpleDateFormat sdfnuevo = new SimpleDateFormat("dd-MM-yyyy");
		for(LicenciaMedica licenciaMedica: listaLicencias){
			if(licenciaMedica.getIdEstatus().getIdEstatus()!=3){
				System.out.println("fechaInicio "+licenciaMedica.getFechaInicio()+" fechaFin "+licenciaMedica.getFechaFin());
				Date fechaInicio=null;
				Date fechaFin=null;
				try{
					fechaInicio=sdfnuevo.parse(licenciaMedica.getFechaInicio());
				 fechaFin=sdfnuevo.parse(licenciaMedica.getFechaFin());
				}catch(Exception e){
					e.printStackTrace();
				}
				
				Calendar c1 = Calendar.getInstance();
				//System.out.println("Fechas fecha inicial "+detalleVacacionDto.getFechaInicio()+" fecha final "+detalleVacacionDto.getFechaFin());
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
			System.out.println("Fecha de fechas desde el array "+listaFechas);
		
		//return listaFechas;
		return listaFechas;
	}

}
