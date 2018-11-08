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
import mx.gob.segob.dgtic.web.mvc.constants.ComisionEndPointConstants;
import mx.gob.segob.dgtic.web.mvc.constants.LicenciaMedicaEndPointConstants;
import mx.gob.segob.dgtic.web.mvc.dto.Comision;
import mx.gob.segob.dgtic.web.mvc.dto.ComisionAux;
import mx.gob.segob.dgtic.web.mvc.dto.GenerarReporteArchivoComision;
import mx.gob.segob.dgtic.web.mvc.dto.LicenciaMedica;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;
import mx.gob.segob.dgtic.web.mvc.dto.Vacaciones;
import mx.gob.segob.dgtic.web.mvc.dto.reporte;
import mx.gob.segob.dgtic.web.mvc.service.ComisionService;
import mx.gob.segob.dgtic.web.mvc.service.UsuarioService;
import mx.gob.segob.dgtic.web.mvc.util.rest.ClienteRestUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.HttpResponseUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.exception.ClienteException;

@Service
public class ComisionServiceImpl implements ComisionService {

  private static final Logger logger = LoggerFactory.getLogger(LogoutCustomHandler.class);

  @Autowired
  private UsuarioService usuarioService;

  @Override
  public List<Comision> obtenerListaComisionesPorFiltros(String claveUsuario, String fechaInicio,
      String fechaFin, String idEstatus, Authentication authentication) {
    List<Comision> listaComisiones = new ArrayList<>();
    HttpResponse response;
    HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

	//Se agrega el JWT a la cabecera para acceso al recurso rest
	Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
    
    try {
      logger.info("----USUARIO-------- {}", claveUsuario);
      response = ClienteRestUtil.getCliente()
          .get(ComisionEndPointConstants.WEB_SERVICE_CONSULTA_COMISION_POR_FILTROS
              + "?claveUsuario=" + claveUsuario + "&idEstatus=" + idEstatus + "&fechaInicio="
              + fechaInicio + "&fechaFin=" + fechaFin, header);
    } catch (ClienteException e) {
      logger.error(e.getMessage(), e);
      throw new AuthenticationServiceException(e.getMessage(), e);
    }

    if (HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {

      JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
      JsonArray dataJson = json.getAsJsonArray("data");
      listaComisiones = new Gson().fromJson(dataJson.toString(),
          new TypeToken<ArrayList<Comision>>() {}.getType());

    } else if (HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {

      String mensaje = obtenerMensajeError(response);
      throw new AuthenticationServiceException(mensaje);
    } else {
      throw new AuthenticationServiceException(
          "Error al obtener comisiones por filtros: " + response.getStatusLine().getReasonPhrase());
    }
    return listaComisiones;
  }

  private String obtenerMensajeError(HttpResponse response) {
    JsonObject respuesta = (JsonObject) HttpResponseUtil.getJsonContent(response);
    JsonObject metadata =
        (JsonObject) respuesta.get(AutorizacionConstants.ATTR_META_DATA_JSON_NAME);
    JsonArray jsonArray =
        metadata.get(AutorizacionConstants.ATTR_META_DATA_ERRORES_JSON_NAME).getAsJsonArray();

    return (jsonArray.size() != 0) ? jsonArray.get(0).getAsString() : "Error desconocido";
  }

  @Override
  public Comision obtieneComision(String idComision, Authentication authentication) {
    Comision comisiones = new Comision();
    HttpResponse response;
    HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

	//Se agrega el JWT a la cabecera para acceso al recurso rest
	Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());

    Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();

    try { // se consume recurso rest
      response = ClienteRestUtil.getCliente()
          .get(ComisionEndPointConstants.WEB_SERVICE_BUSCA_COMISION + "?id=" + idComision, header);
    } catch (ClienteException e) {
      logger.error(e.getMessage(), e);
      throw new AuthenticationServiceException(e.getMessage(), e);
    }

    if (HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {

      JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
      JsonElement dataJson = json.get("data").getAsJsonObject();
      comisiones = gson.fromJson(dataJson, Comision.class);

    } else if (HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {

      String mensaje = obtenerMensajeError(response);
      throw new AuthenticationServiceException(mensaje);
    } else {
      throw new AuthenticationServiceException(
          "Error al obtener vacaciones : " + response.getStatusLine().getReasonPhrase());
    }

    return comisiones;
  }

  @Override
  public List<Comision> obtenerListaComisionesPorFiltrosEmpleados(String claveUsuario,
      String nombre, String apellidoPaterno, String apellidoMaterno, String idUnidad,
      String idEstatus, Authentication authentication) {
    List<Comision> listaComisiones = new ArrayList<>();
    HttpResponse response;
    HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

	//Se agrega el JWT a la cabecera para acceso al recurso rest
	Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
	
    try {
      response = ClienteRestUtil.getCliente()
          .get(ComisionEndPointConstants.WEB_SERVICE_CONSULTA_COMISION_EMPLEADOS_POR_FILTROS
              + "?claveUsuario=" + claveUsuario + "&nombre=" + nombre + "&apellidoPaterno="
              + apellidoPaterno + "&apellidoMaterno=" + apellidoMaterno + "&idUnidad=" + idUnidad
              + "&idEstatus=" + idEstatus, header);
    } catch (ClienteException e) {
      logger.error(e.getMessage(), e);
      throw new AuthenticationServiceException(e.getMessage(), e);
    }

    if (HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {

      JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
      JsonArray dataJson = json.getAsJsonArray("data");
      listaComisiones = new Gson().fromJson(dataJson.toString(),
          new TypeToken<ArrayList<Comision>>() {}.getType());

    } else if (HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {

      String mensaje = obtenerMensajeError(response);
      throw new AuthenticationServiceException(mensaje);
    } else {
      throw new AuthenticationServiceException("Error al obtener comisiones por filtros empleados: "
          + response.getStatusLine().getReasonPhrase());
    }
    return listaComisiones;
  }

  @Override
  public Comision agregarComision(ComisionAux comisionAux, String claveUsuario, Authentication authentication) {
    Comision comision= new Comision();
    HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

	//Se agrega el JWT a la cabecera para acceso al recurso rest
	Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
    Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
    HttpEntity httpEntity = new BasicHttpEntity();
    HttpResponse response;
    //BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
    Usuario usuario= new Usuario();
    usuario=usuarioService.buscaUsuario(claveUsuario, authentication);
    comisionAux.setIdUsuario(usuario.getIdUsuario());
    Map<String, Object> content = new HashMap<String, Object>();
    content.put("comision", comisionAux);

    try {
        httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
    } catch (ClienteException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }
    
    try { //se consume recurso rest
        response=ClienteRestUtil.getCliente().put(ComisionEndPointConstants.WEB_SERVICE_AGREGA_COMISION, httpEntity, header);
    } catch (ClienteException e) {
        logger.error(e.getMessage(), e);
        throw new AuthenticationServiceException(e.getMessage(), e);
    }
    if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
        
        JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
        JsonElement dataJson = json.get("data").getAsJsonObject();
        comision = gson.fromJson(dataJson, Comision.class);        
    } else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
        String mensaje = obtenerMensajeError(response);                  
        throw new AuthenticationServiceException(mensaje);          
    } else {
        throw new AuthenticationServiceException("Error al obtener el día Inhábil : "+response.getStatusLine().getReasonPhrase());
    }
            
    return comision;
  }

  @Override
  public Comision modificaComisiones(ComisionAux comisionAux, String claveUsuario, Authentication authentication) {
    Comision comisionRespuesta = new Comision();
    HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

	//Se agrega el JWT a la cabecera para acceso al recurso rest
	Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
    Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
    HttpEntity httpEntity = new BasicHttpEntity();
    HttpResponse response;
    // BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
    Usuario usuario = new Usuario();
    usuario = usuarioService.buscaUsuario(claveUsuario, authentication);
    System.out.println("IdUsuario recuperado " + usuario.getIdUsuario());
    comisionAux.setIdUsuario(usuario.getIdUsuario());
    Map<String, Object> content = new HashMap<String, Object>();
    content.put("comision", comisionAux);

    try {
      httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
    } catch (ClienteException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    try { // se consume recurso rest
      response = ClienteRestUtil.getCliente()
          .put(ComisionEndPointConstants.WEB_SERVICE_MODIFICA_COMISION, httpEntity, header);
    } catch (ClienteException e) {
      logger.error(e.getMessage(), e);
      throw new AuthenticationServiceException(e.getMessage(), e);
    }
    if (HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {

      JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
      JsonElement dataJson = json.get("data").getAsJsonObject();
      comisionRespuesta = gson.fromJson(dataJson, Comision.class);
    } else if (HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
      String mensaje = obtenerMensajeError(response);
      throw new AuthenticationServiceException(mensaje);
    } else {
      throw new AuthenticationServiceException(
          "Error al obtener el día Inhábil : " + response.getStatusLine().getReasonPhrase());
    }
    return comisionRespuesta;
  }
  

  @Override
  public void eliminaComisiones(String idComision, Authentication authentication) {
    HttpResponse response;

    Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
    HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

	//Se agrega el JWT a la cabecera para acceso al recurso rest
	Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());

    try { // se consume recurso rest
      response = ClienteRestUtil.getCliente()
          .get(ComisionEndPointConstants.WEB_SERVICE_ELIMINA_COMISION + "?id=" + idComision, header);
    } catch (ClienteException e) {
      logger.error(e.getMessage(), e);
      throw new AuthenticationServiceException(e.getMessage(), e);
    }

  }

  @Override
  public List<Comision> obtenerComisionesPorUnidad(String idUnidad, String claveUsuario,
      String nombre, String apellidoPaterno, String apellidoMaterno, Authentication authentication) {
    List<Comision> listaComisiones = new ArrayList<>();
    HttpResponse response;
    HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

	//Se agrega el JWT a la cabecera para acceso al recurso rest
	Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
	
    try{
        response = ClienteRestUtil.getCliente().get(ComisionEndPointConstants.WEB_SERVICE_CONSULTA_COMISION_POR_UNIDAD+ "?idUnidad="+idUnidad+"&claveUsuario="+claveUsuario+"&nombre="+nombre
                +"&apellidoPaterno="+apellidoPaterno+"&apellidoMaterno="+apellidoMaterno, header);
    } catch (ClienteException e) {
        logger.error(e.getMessage(), e);
        throw new AuthenticationServiceException(e.getMessage(), e);
    }
    
    if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
        
        JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
        JsonArray dataJson = json.getAsJsonArray("data");
        listaComisiones = new Gson().fromJson(dataJson.toString(), new TypeToken<ArrayList<Comision>>(){}.getType());
        
    } else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
        
        String mensaje = obtenerMensajeError(response);                  
        throw new AuthenticationServiceException(mensaje);          
    } else {
        throw new AuthenticationServiceException("Error al obtener vacaciones por filtros: "+response.getStatusLine().getReasonPhrase());
    }
    System.out.println("Recuperados "+listaComisiones.size());
    return listaComisiones;
  }

  @Override
  public reporte generarReporte(GenerarReporteArchivoComision generaReporteArchivo, Authentication authentication) {
    HttpResponse response;
    Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
    reporte respuesta = new reporte();
    HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

	//Se agrega el JWT a la cabecera para acceso al recurso rest
	Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
    HttpEntity httpEntity = new BasicHttpEntity();
    Map<String, Object> content = new HashMap<String, Object>();
    content.put("generaReporteArchivo", generaReporteArchivo);
    try {
        httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
        response = ClienteRestUtil.getCliente().put(ComisionEndPointConstants.WEB_SERVICE_GENERA_REPORTE, httpEntity, header);
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
  public Comision modificaComisionEstatusArchivo(ComisionAux comisionAux, String claveUsuario, Authentication authentication) {
    Comision comisionRespuesta= new Comision();
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
    comisionAux.setIdUsuario(usuario.getIdUsuario());
    Map<String, Object> content = new HashMap<String, Object>();
    content.put("comision", comisionAux);

    try {
        httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
    } catch (ClienteException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }
    
    try { //se consume recurso rest
        response=ClienteRestUtil.getCliente().put(ComisionEndPointConstants.WEB_SERVICE_MODIFICA_COMISION_ESTATUS_ARCHIVO, httpEntity, header);
    } catch (ClienteException e) {
        logger.error(e.getMessage(), e);
        throw new AuthenticationServiceException(e.getMessage(), e);
    }
    if(HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
        
        JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
        JsonElement dataJson = json.get("data").getAsJsonObject();
        comisionRespuesta = gson.fromJson(dataJson, Comision.class);        
    } else if(HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
        String mensaje = obtenerMensajeError(response);                  
        throw new AuthenticationServiceException(mensaje);          
    } else {
        throw new AuthenticationServiceException("Error al obtener el día Inhábil : "+response.getStatusLine().getReasonPhrase());
    }
    return comisionRespuesta;
  }

}
