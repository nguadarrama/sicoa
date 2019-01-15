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
import mx.gob.segob.dgtic.web.mvc.constants.ComisionEndPointConstants;
import mx.gob.segob.dgtic.web.mvc.dto.Comision;
import mx.gob.segob.dgtic.web.mvc.dto.ComisionAux;
import mx.gob.segob.dgtic.web.mvc.dto.GenerarReporteArchivoComision;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;
import mx.gob.segob.dgtic.web.mvc.dto.reporte;
import mx.gob.segob.dgtic.web.mvc.service.ComisionService;
import mx.gob.segob.dgtic.web.mvc.service.UsuarioService;
import mx.gob.segob.dgtic.web.mvc.service.constants.Constantes;
import mx.gob.segob.dgtic.web.mvc.util.rest.ClienteRestUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.HttpResponseUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.exception.ClienteException;

@Service
@SuppressWarnings("unchecked")
public class ComisionServiceImpl implements ComisionService {

  private static final Logger logger = LoggerFactory.getLogger(LogoutCustomHandler.class);

  private static final String ETIQUETA_EXCEPCION = "Excepcion: {}";
  private static final String ETIQUETA_COMISION = "comision";
  private static final String ERROR_DIA_INHABIL = "Error al obtener el día Inhábil : ";

  @Autowired
  private UsuarioService usuarioService;

  @Override
  public List<Comision> obtenerListaComisionesPorFiltros(String claveUsuario, String fechaInicio,
      String fechaFin, String idEstatus, Authentication authentication) {
    List<Comision> listaComisiones = null;
    HttpResponse response;
    HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

    /** Se agrega el JWT a la cabecera para acceso al recurso rest **/
    Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION,
        Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());

    try {
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
    Comision comisiones;
    HttpResponse response;
    HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

    Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION,
        Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());

    Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
    logger.info("Entro a servicio FRONT");
    try {
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
    List<Comision> listaComisiones = null;
    HttpResponse response;
    HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

    /** Se agrega el JWT a la cabecera para acceso al recurso rest **/
    Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION,
        Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());

    try {
      response = ClienteRestUtil.getCliente()
          .get(ComisionEndPointConstants.WEB_SERVICE_CONSULTA_COMISION_EMPLEADOS_POR_FILTROS
              + "?claveUsuario=" + removerEspacios(claveUsuario) + "&nombre="
              + removerEspacios(nombre) + "&apellidoPaterno=" + removerEspacios(apellidoPaterno)
              + "&apellidoMaterno=" + removerEspacios(apellidoMaterno) + "&idUnidad=" + idUnidad
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
  public Comision agregarComision(ComisionAux comisionAux, String claveUsuario,
      Authentication authentication) {
    Comision comision;
    HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

    Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION,
        Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
    Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
    HttpEntity httpEntity = new BasicHttpEntity();
    HttpResponse response;
    Usuario usuario = usuarioService.buscaUsuario(claveUsuario, authentication);
    comisionAux.setIdUsuario(usuario.getIdUsuario());
    Map<String, Object> content = new HashMap<>();
    content.put(ETIQUETA_COMISION, comisionAux);

    try {
      httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
    } catch (ClienteException e) {
      logger.error(ETIQUETA_EXCEPCION, e.getMessage());
    }

    try {
      response = ClienteRestUtil.getCliente()
          .put(ComisionEndPointConstants.WEB_SERVICE_AGREGA_COMISION, httpEntity, header);
    } catch (ClienteException e) {
      logger.error(ETIQUETA_EXCEPCION, e.getMessage());
      throw new AuthenticationServiceException(e.getMessage(), e);
    }
    if (HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {

      JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
      JsonElement dataJson = json.get("data").getAsJsonObject();
      comision = gson.fromJson(dataJson, Comision.class);
    } else if (HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
      String mensaje = obtenerMensajeError(response);
      throw new AuthenticationServiceException(mensaje);
    } else {
      throw new AuthenticationServiceException(
          ERROR_DIA_INHABIL + response.getStatusLine().getReasonPhrase());
    }

    return comision;
  }

  @Override
  public Comision modificaComisiones(ComisionAux comisionAux, String claveUsuario,
      Authentication authentication) {
    Comision comisionRespuesta;
    HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

    Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION,
        Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());
    Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
    HttpEntity httpEntity = new BasicHttpEntity();
    HttpResponse response;
    Usuario usuario = usuarioService.buscaUsuario(claveUsuario, authentication);
    logger.info("Id usuario recuperado: {}", usuario.getIdUsuario());
    comisionAux.setIdUsuario(usuario.getIdUsuario());
    Map<String, Object> content = new HashMap<>();
    content.put(ETIQUETA_COMISION, comisionAux);

    try {
      httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
    } catch (ClienteException e) {
      logger.error(ETIQUETA_EXCEPCION, e.getMessage());
    }

    try {
      response = ClienteRestUtil.getCliente()
          .put(ComisionEndPointConstants.WEB_SERVICE_MODIFICA_COMISION, httpEntity, header);
    } catch (ClienteException e) {
      logger.error(ETIQUETA_EXCEPCION, e.getMessage());
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
          ERROR_DIA_INHABIL + response.getStatusLine().getReasonPhrase());
    }
    return comisionRespuesta;
  }


  @Override
  public void eliminaComisiones(String idComision, Authentication authentication) {

    HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

    Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION,
        Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());

    try {
      ClienteRestUtil.getCliente().get(
          ComisionEndPointConstants.WEB_SERVICE_ELIMINA_COMISION + "?id=" + idComision, header);
    } catch (ClienteException e) {
      logger.error(e.getMessage(), e);
      throw new AuthenticationServiceException(e.getMessage(), e);
    }

  }

 
  @Override
  public List<Comision> obtenerComisionesPorUnidad(String idUnidad, String claveUsuario,
      String nombre, String apellidoPaterno, String apellidoMaterno,
      Authentication authentication) {
    List<Comision> listaComisiones;
    HttpResponse response;
    HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

    Header header = new BasicHeader(Constantes.ETIQUETA_AUTHORIZATION,
        Constantes.ETIQUETA_BEARER + detalles.get(Constantes.ETIQUETA_TOKEN).toString());

    try {
      response = ClienteRestUtil.getCliente()
          .get(ComisionEndPointConstants.WEB_SERVICE_CONSULTA_COMISION_POR_UNIDAD + "?idUnidad="
              + idUnidad + "&claveUsuario=" + removerEspacios(claveUsuario) + "&nombre="
              + removerEspacios(nombre) + "&apellidoPaterno=" + removerEspacios(apellidoPaterno)
              + "&apellidoMaterno=" + removerEspacios(apellidoMaterno), header);
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
          "Error al obtener vacaciones por filtros: " + response.getStatusLine().getReasonPhrase());
    }
    logger.info("Numero de comisiones recuperadas: {}", listaComisiones.size());
    return listaComisiones;
  }

  @Override
  public reporte generarReporte(GenerarReporteArchivoComision generaReporteArchivo,
      Authentication authentication) {
    HttpResponse response;
    Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
    reporte respuesta = new reporte();
    HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

    Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
    HttpEntity httpEntity = new BasicHttpEntity();
    Map<String, Object> content = new HashMap<>();
    content.put("generaReporteArchivo", generaReporteArchivo);
    try {
      httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
      response = ClienteRestUtil.getCliente()
          .put(ComisionEndPointConstants.WEB_SERVICE_GENERA_REPORTE, httpEntity, header);
      if (HttpResponseUtil.getStatus(response) == Status.OK.getStatusCode()) {
        JsonObject json = (JsonObject) HttpResponseUtil.getJsonContent(response);
        JsonElement dataJson = json.get("data").getAsJsonObject();
        respuesta = gson.fromJson(dataJson, reporte.class);
      } else if (HttpResponseUtil.isContentType(response, ContentType.APPLICATION_JSON)) {
        String mensaje = obtenerMensajeError(response);
        throw new AuthenticationServiceException(mensaje);
      } else {
        throw new AuthenticationServiceException(
            "Error al obtener el archivo : " + response.getStatusLine().getReasonPhrase());
      }
    } catch (ClienteException e) {
      logger.error(ETIQUETA_EXCEPCION, e.getMessage());
      throw new AuthenticationServiceException(e.getMessage(), e);
    } catch (Exception e) {
      logger.error(ETIQUETA_EXCEPCION, e.getMessage());
    }
    return respuesta;
  }

  @Override
  public Comision modificaComisionEstatusArchivo(ComisionAux comisionAux, String claveUsuario,
      Authentication authentication) {
    Comision comisionRespuesta;
    HashMap<String, Object> detalles = (HashMap<String, Object>) authentication.getDetails();

    Header header = new BasicHeader("Authorization", "Bearer " + detalles.get("_token").toString());
    Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
    HttpEntity httpEntity = new BasicHttpEntity();
    HttpResponse response;
    Usuario usuario = usuarioService.buscaUsuario(claveUsuario, authentication);
    comisionAux.setIdUsuario(usuario.getIdUsuario());
    Map<String, Object> content = new HashMap<>();
    content.put(ETIQUETA_COMISION, comisionAux);

    try {
      httpEntity = ClienteRestUtil.getCliente().convertContentToJSONEntity(content);
    } catch (ClienteException e) {
      logger.error(ETIQUETA_EXCEPCION, e.getMessage());
    }

    try {
      response = ClienteRestUtil.getCliente().put(
          ComisionEndPointConstants.WEB_SERVICE_MODIFICA_COMISION_ESTATUS_ARCHIVO, httpEntity,
          header);
    } catch (ClienteException e) {
      logger.error(ETIQUETA_EXCEPCION, e.getMessage());
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
          ERROR_DIA_INHABIL + response.getStatusLine().getReasonPhrase());
    }
    return comisionRespuesta;
  }

  private String removerEspacios(String string) {
    if (string != null && !string.isEmpty()) {
      return string.replace(" ", "_");
    }
    return string;
  }

}
