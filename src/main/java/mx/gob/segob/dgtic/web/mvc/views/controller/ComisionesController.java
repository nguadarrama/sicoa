package mx.gob.segob.dgtic.web.mvc.views.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mx.gob.segob.dgtic.web.mvc.dto.Archivo;
import mx.gob.segob.dgtic.web.mvc.dto.Comision;
import mx.gob.segob.dgtic.web.mvc.dto.ComisionAux;
import mx.gob.segob.dgtic.web.mvc.dto.GenerarReporteArchivoComision;
import mx.gob.segob.dgtic.web.mvc.dto.Horario;
import mx.gob.segob.dgtic.web.mvc.dto.PerfilUsuario;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;
import mx.gob.segob.dgtic.web.mvc.dto.reporte;
import mx.gob.segob.dgtic.web.mvc.service.ArchivoService;
import mx.gob.segob.dgtic.web.mvc.service.BaseService;
import mx.gob.segob.dgtic.web.mvc.service.CatalogoService;
import mx.gob.segob.dgtic.web.mvc.service.ComisionService;
import mx.gob.segob.dgtic.web.mvc.service.EstatusService;
import mx.gob.segob.dgtic.web.mvc.service.PerfilUsuarioService;
import mx.gob.segob.dgtic.web.mvc.service.UnidadAdministrativaService;
import mx.gob.segob.dgtic.web.mvc.service.UsuarioService;

@Controller
@RequestMapping(value = "comisiones", method = RequestMethod.GET)
public class ComisionesController extends BaseService {

  @Autowired
  private ComisionService comisionService;

  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  private EstatusService estatusService;

  @Autowired
  private PerfilUsuarioService perfilUsuarioService;

  @Autowired
  private UnidadAdministrativaService unidadAdministrativaService;

  @Autowired
  private CatalogoService catalogoService;
  
  @Autowired 
  private ArchivoService archivoService;

  private String mensaje = "";
  
  private static final String ETIQUETA_USUARIO = "usuario";
  private static final String ETIQUETA_COMISIONES = "comisiones";
  private static final String COMISIONES_EMPLEADOS = "comisionesEmpleados";
  private static final String ETIQUETA_CORRECTAMENTE = "correctamente";
  private static final String ETIQUETA_MENSAJE = "MENSAJE";
  private static final String ETIQUETA_MENSAJE_EXCEPCION = "MENSAJE_EXCEPCION";
  private static final String ETIQUETA_LISTA_HORARIOS = "listaHorarios";
  private static final String ETIQUETA_LISTA_DIAS_FESTIVOS = "listaDiasFestivos";
  private static final String ETIQUETA_LISTA_RESPONSABLE = "listaResponsable";
  private static final String ETIQUETA_FECHA_REGISTRO = "fechaRegistro";
  private static final String FORMATO_DD_MM_YYYY = "dd-MM-yyyy";
  private static final String REDIRECT_COMISIONES_EMPLEADOS =
      "redirect:/comisiones/comisionesEmpleados";

  /**
   * Comisiones propias
   * 
   * @param idUsuario
   * @param fechaInicioBusca1
   * @param fechaFinBusca1
   * @param idEstatus
   * @param model
   * @param session
   * @return
   */
  @RequestMapping(value = {"comisionesPropias"}, method = RequestMethod.GET)
  public String obtieneComisiones(String fechaInicioBusca1, String fechaFinBusca1, String idEstatus,
      Model model, HttpSession session, Authentication authentication) {

    String string = "" + session.getAttribute(ETIQUETA_USUARIO);
    String[] parts = string.split(": ");
    String claveUsuario = parts[1];
    Usuario usuario = usuarioService.buscaUsuario(claveUsuario, authentication);

    logger.info(
        "Peticion de comisiones: idUsuario: {} fechaInicioBusca1: {} fechaFinBusca1: {} idEstatus: {}",
        new Object[] {usuario.getIdUsuario(), fechaInicioBusca1, fechaFinBusca1, idEstatus});

    List<Comision> listaComisiones =
        comisionService.obtenerListaComisionesPorFiltros(String.valueOf(usuario.getIdUsuario()),
            fechaInicioBusca1, fechaFinBusca1, idEstatus, authentication);
    logger.info("Numero de comisiones obtenidas: {}", listaComisiones.size());
    model.addAttribute(ETIQUETA_COMISIONES, listaComisiones);
    model.addAttribute("listaEstatus", estatusService.obtieneListaEstatus(authentication));
    return "/comisiones/comisionesPropias";

  }

  /**
   * Comisiones de empleados
   * 
   * @param claveUsuario
   * @param nombre
   * @param apellidoPaterno
   * @param apellidoMaterno
   * @param idUnidad
   * @param idEstatus
   * @param model
   * @param session
   * @return
   */
  @RequestMapping(value = {"comisionesEmpleados"}, method = RequestMethod.GET)
  public String obtieneComisionesEmpleados(String claveUsuario, String nombre,
      String apellidoPaterno, String apellidoMaterno, String idUnidad, String idEstatus,
      Model model, HttpSession session, Authentication authentication) {
    logger.info(
        "Peticion de comisiones de empleados: idUsuario: {} Nombre: {} apellidoPaterno: {} apellidoMaterno: {} idUnidad: {} idEstatus: {}",
        new Object[] {claveUsuario, nombre, apellidoPaterno, apellidoMaterno, idUnidad, idEstatus});

    if (idEstatus == null) {
      idEstatus = "";
    }
    String string = "" + session.getAttribute(ETIQUETA_USUARIO);
    String[] parts = string.split(": ");
    String claveUsuarioLider = parts[1];
    List<PerfilUsuario> listaPerfilUsuario =
        perfilUsuarioService.recuperaPerfilesUsuario(claveUsuarioLider, authentication);
    Boolean usuarioBoolean = false;

    for (PerfilUsuario perfilUsuario : listaPerfilUsuario) {
      logger.info("Perfiles de usuario: {}", perfilUsuario.getClavePerfil().getClavePerfil());
      if (perfilUsuario.getClavePerfil().getClavePerfil().equals("2")) {
        usuarioBoolean = true;
      }
    }
    logger.info("Bandera es coordinador: {}", usuarioBoolean);
    if (usuarioBoolean) {
      Usuario usuarioAux = usuarioService.buscaUsuario(claveUsuarioLider, authentication);
      idUnidad = idUnidad != null ? "" + usuarioAux.getIdUnidad() : "";
    } else {
      idUnidad = "";
    }

    logger.info("Id de Unidad: {} ", idUnidad);
    model.addAttribute("listaUnidades",
        unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
    List<Comision> comisiones =
        comisionService.obtenerListaComisionesPorFiltrosEmpleados(claveUsuario, nombre,
            apellidoPaterno, apellidoMaterno, idUnidad, idEstatus, authentication);
    logger.info("Numero de comisiones obtenidas: {}", comisiones.size());
    if (!comisiones.isEmpty()) {
      model.addAttribute(COMISIONES_EMPLEADOS, comisiones);
    } else {
      model.addAttribute(COMISIONES_EMPLEADOS, null);
    }

    model.addAttribute("listaEstatus", estatusService.obtieneListaEstatus(authentication));
    if (!this.getMensaje().equals("")) {
      if (this.mensaje.contains(ETIQUETA_CORRECTAMENTE)) {
        model.addAttribute(ETIQUETA_MENSAJE, this.mensaje);
      } else {
        model.addAttribute(ETIQUETA_MENSAJE_EXCEPCION, this.mensaje);
      }
    }
    this.mensaje = "";
    return "/comisiones/comisionesEmpleados";
  }

  /**
   * Busca comision cuando presionan ver detalle
   * 
   * @param idComision
   * @return
   */
  @GetMapping("comision/busca")
  @ResponseBody
  public Map<String, Object> buscaUsuario(String idComision, Authentication authentication) {
    Map<String, Object> hmap = new HashMap<>();
    logger.info("Id de la comision: {}", idComision);
    Comision comisiones = comisionService.obtieneComision(idComision, authentication);
    comisiones.getIdUsuario().setFechaIngreso(comisiones.getIdUsuario().getFechaIngreso());
    comisiones.setFechaInicio(comisiones.getFechaInicio());
    comisiones.setFechaFin(comisiones.getFechaFin());
    comisiones.setFechaRegistro(comisiones.getFechaRegistro());

    String comisionJson = gson.toJson(comisiones);
    logger.info("Comision objeto: {}", comisionJson);
    hmap.put("comision", comisiones);

    if (comisiones.getIdResponsable() != null
        && !comisiones.getIdResponsable().toString().isEmpty()) {
      hmap.put("responsable", usuarioService
          .buscaUsuarioPorId(Integer.toString(comisiones.getIdResponsable()), authentication));
    } else {
      hmap.put("responsable", null);
    }

    if (comisiones.getIdHorario() != null && comisiones.getIdHorario().getIdHorario() != null
        && !comisiones.getIdHorario().getIdHorario().toString().isEmpty()) {
      Horario horario =
          catalogoService.buscaHorario(comisiones.getIdHorario().getIdHorario(), authentication);

      String horarioString =
          horario.getHoraEntrada().toString() + " - " + horario.getHoraSalida().toString();
      logger.info("Horario: {}", horarioString);
      hmap.put("horario", horarioString);
    } else {
      hmap.put("horario", null);
    }
    return hmap;
  }

  /**
   * Solicitud de comisiones
   * 
   * @param model
   * @param session
   * @return
   */
  @RequestMapping(value = {"solicitudComision"}, method = RequestMethod.GET)
  public String solicitudComisiones(Model model, HttpSession session,
      Authentication authentication) {

    String string = "" + session.getAttribute(ETIQUETA_USUARIO);
    String[] parts = string.split(": ");
    String claveUsuario = parts[1];
    model.addAttribute(ETIQUETA_FECHA_REGISTRO, obtenerFechaActual(FORMATO_DD_MM_YYYY));
    model.addAttribute(ETIQUETA_USUARIO, usuarioService.buscaUsuario(claveUsuario, authentication));
    model.addAttribute(ETIQUETA_LISTA_RESPONSABLE,
        unidadAdministrativaService.consultaResponsable(claveUsuario, authentication));
    model.addAttribute(ETIQUETA_LISTA_HORARIOS, catalogoService.obtieneHorarios(authentication));
    String cadena = catalogoService.obtieneDiaFestivoParaBloquear(authentication);
    logger.info("Dias festivos a bloquear: {}", cadena);
    model.addAttribute(ETIQUETA_LISTA_DIAS_FESTIVOS, cadena);

    if (!this.getMensaje().equals("")) {
      if (this.mensaje.contains(ETIQUETA_CORRECTAMENTE))
        model.addAttribute(ETIQUETA_MENSAJE, this.mensaje);
      else
        model.addAttribute(ETIQUETA_MENSAJE_EXCEPCION, this.mensaje);
    }
    this.mensaje = "";

    logger.info("Registrar comision a usuario: {}", claveUsuario);
    return "/comisiones/solicitudComission";

  }

  /**
   * Agrega comision
   * 
   * @param claveUsuario
   * @param idResponsable
   * @param fechaInicio
   * @param fechaFin
   * @param dias
   * @param comision
   * @param model
   * @param session
   * @return
   */
  @RequestMapping(value = {"agregarComision"}, method = RequestMethod.POST)
  public String agregarComision(String claveUsuario, String idResponsable, String fechaInicio,
      String fechaFin, Integer dias, String comision, String idHorario, Model model,
      HttpSession session, Authentication authentication) {

    logger.info(
        "Peticion de agregar comision: claveUsuario: {} idResponsable: {} fechaInicio: {} fechaFin: {} dias: {} comision: {} idHorario: {}",
        new Object[] {claveUsuario, idResponsable, fechaInicio, fechaFin, dias, comision,
            idHorario});

    Integer idResponsableAux = null;
    if (idResponsable != null && !idResponsable.trim().isEmpty()) {
      idResponsableAux = Integer.parseInt(idResponsable);
    }

    ComisionAux comisionAux = new ComisionAux();
    comisionAux.setIdResponsable(idResponsableAux);
    comisionAux.setFechaInicio(fechaInicio);
    comisionAux.setFechaFin(fechaFin);
    comisionAux.setDias(dias);
    comisionAux.setComision(comision);
    comisionAux.setFechaRegistro(obtenerFechaActual("dd/MM/yyyy"));
    comisionAux.setIdHorario(Integer.valueOf(idHorario));
    comisionAux.setIdEstatus(1);

    Comision comisionRespuesta =
        comisionService.agregarComision(comisionAux, claveUsuario, authentication);
    this.mensaje = comisionRespuesta.getMensaje();

    return "redirect:/comisiones/solicitudComisionEmpleados";

  }

  /**
   * Solicitud de comisiones (Registra comisiones de asignados)
   * 
   * @param claveUsuario
   * @param nombre
   * @param apellidoPaterno
   * @param apellidoMaterno
   * @param model
   * @param session
   * @return
   */
  @RequestMapping(value = {"solicitudComisionEmpleados"}, method = RequestMethod.GET)
  public String solitudComisionesEmpleados(String claveUsuario, String nombre,
      String apellidoPaterno, String apellidoMaterno, Model model, HttpSession session,
      Authentication authentication) {

    logger.info(
        "Peticion de registro de comisiones a empleados: idUsuario: {} Nombre: {} apellidoPaterno: {} apellidoMaterno: {}",
        new Object[] {claveUsuario, nombre, apellidoPaterno, apellidoMaterno});

    String string = "" + session.getAttribute(ETIQUETA_USUARIO);
    String[] parts = string.split(": ");
    String claveUsuarioAux = parts[1];
    Usuario usuario = usuarioService.buscaUsuario(claveUsuarioAux, authentication);
    String idUnidad = "" + usuario.getIdUnidad();
    List<Comision> comisionesEmpleados = comisionService.obtenerComisionesPorUnidad(idUnidad,
        claveUsuario, nombre, apellidoPaterno, apellidoMaterno, authentication);

    if (!comisionesEmpleados.isEmpty()) {
      model.addAttribute(COMISIONES_EMPLEADOS, comisionesEmpleados);
    } else {
      model.addAttribute(COMISIONES_EMPLEADOS, null);
    }
    if (!this.getMensaje().equals("")) {
      if (this.mensaje.contains(ETIQUETA_CORRECTAMENTE))
        model.addAttribute(ETIQUETA_MENSAJE, this.mensaje);
      else
        model.addAttribute(ETIQUETA_MENSAJE_EXCEPCION, this.mensaje);
    }
    model.addAttribute(ETIQUETA_LISTA_HORARIOS, catalogoService.obtieneHorarios(authentication));
    this.mensaje = "";
    return "/comisiones/solicitudComisionesEmpleados";

  }

  /**
   * Busca usuario
   * 
   * @param claveUsuario
   * @param model
   * @return
   */
  @RequestMapping(value = {"comision/buscaUsuario"}, method = RequestMethod.GET)
  public String buscaUsuario(String claveUsuario, Model model, Authentication authentication) {
    logger.info("Id de usuario: {}", claveUsuario);

    model.addAttribute(ETIQUETA_FECHA_REGISTRO, obtenerFechaActual(FORMATO_DD_MM_YYYY));
    model.addAttribute(ETIQUETA_USUARIO, usuarioService.buscaUsuario(claveUsuario, authentication));
    model.addAttribute(ETIQUETA_LISTA_RESPONSABLE,
        unidadAdministrativaService.consultaResponsable(claveUsuario, authentication));
    model.addAttribute(ETIQUETA_LISTA_HORARIOS, catalogoService.obtieneHorarios(authentication));
    String cadena = catalogoService.obtieneDiaFestivoParaBloquear(authentication);
    logger.info("Dias festivos para bloquear: {}", cadena);
    model.addAttribute(ETIQUETA_LISTA_DIAS_FESTIVOS, cadena);

    return "/comisiones/registraComisionesEmpleados";

  }

  /**
   * Muestra la pantalla para modificar una comision.
   * 
   * @param idComision
   * @param claveUsuario
   * @param model
   * @param session
   * @param authentication
   * @return
   */
  @RequestMapping(value = {"modificarComision"}, method = RequestMethod.GET)
  public String modificarComisiones(String idComision, String claveUsuario, Model model,
      HttpSession session, Authentication authentication) {
    logger.info("Id de comision: {} claveUsuario: {}", idComision, claveUsuario);

    model.addAttribute("comision", comisionService.obtieneComision(idComision, authentication));
    model.addAttribute(ETIQUETA_FECHA_REGISTRO, obtenerFechaActual(FORMATO_DD_MM_YYYY));
    model.addAttribute(ETIQUETA_USUARIO, usuarioService.buscaUsuario(claveUsuario, authentication));
    model.addAttribute(ETIQUETA_LISTA_RESPONSABLE,
        unidadAdministrativaService.consultaResponsable(claveUsuario, authentication));
    model.addAttribute(ETIQUETA_LISTA_HORARIOS, catalogoService.obtieneHorarios(authentication));
    String cadena = catalogoService.obtieneDiaFestivoParaBloquear(authentication);
    model.addAttribute(ETIQUETA_LISTA_DIAS_FESTIVOS, cadena);

    return "/comisiones/modificarComisionEmpleados";

  }

  /**
   * Envia los datos modificados de una comision al back.
   * 
   * @param idComision
   * @param fechaInicio
   * @param fechaFin
   * @param dias
   * @param comision
   * @param claveUsuario
   * @param idHorario
   * @param model
   * @param session
   * @param authentication
   * @return
   */
  @RequestMapping(value = {"modificarComisionGuardado"}, method = RequestMethod.GET)
  public String modificarComisionesGuardado(String idComision, String fechaInicio, String fechaFin,
      String dias, String comision, String claveUsuario, String idHorario, Model model,
      HttpSession session, Authentication authentication) {
    logger.info("Clave de usuario: {}", claveUsuario);
    ComisionAux comisioPeticion = new ComisionAux();
    comisioPeticion.setIdComision(Integer.valueOf(idComision));
    comisioPeticion.setFechaInicio(fechaInicio);
    comisioPeticion.setFechaFin(fechaFin);
    comisioPeticion.setDias(Integer.valueOf(dias));
    comisioPeticion.setComision(comision);
    comisioPeticion.setIdHorario(Integer.valueOf(idHorario));

    String comisionPeticionJson = gson.toJson(comisioPeticion);
    logger.info("Comision peticion: {}", comisionPeticionJson);

    Comision comisionRespuesta =
        comisionService.modificaComisiones(comisioPeticion, claveUsuario, authentication);
    this.mensaje = comisionRespuesta.getMensaje();
    return REDIRECT_COMISIONES_EMPLEADOS;

  }

  /**
   * Genera el formato de comisiones.
   * 
   * @param idComision
   * @param idUnidadAdministrativa
   * @param nombre
   * @param apellidoPaterno
   * @param apellidoMaterno
   * @param fechaInicio1
   * @param fechaFin1
   * @param idHorario
   * @param comision
   * @param request
   * @param response
   * @param authentication
   */
  @RequestMapping(value = "comision/generarArchivo", method = RequestMethod.GET)
  public void generarReporteComisiones(String idComision, String idUnidadAdministrativa,
      String nombre, String apellidoPaterno, String apellidoMaterno, String fechaInicio1,
      String fechaFin1, String idHorario, String comisionDesc, HttpServletRequest request,
      HttpServletResponse response, Authentication authentication) {

    logger.info(
        "Peticion para generar formato. Parametros: idComision: {} Unidad Administrativa: {} Nombre: {} apellidoPaterno: {} apellidoMaterno: {} fechaInicio: {} fechaFin: {} idHorario: {} comision: {}",
        new Object[] {idComision, idUnidadAdministrativa, nombre, apellidoPaterno, apellidoMaterno,
            fechaInicio1, fechaFin1, idHorario, comisionDesc});

    String nombreCompleto = nombre + " " + apellidoPaterno + " " + apellidoMaterno;

    try {
      logger.info("Datos: {}", nombreCompleto);
      reporte archivo = comisionService
          .generarReporte(new GenerarReporteArchivoComision(nombreCompleto, idUnidadAdministrativa,
              comisionDesc, fechaInicio1, fechaFin1, idHorario), authentication);

      InputStream targetStream = new ByteArrayInputStream(archivo.getNombre());
      String mimeType = URLConnection.guessContentTypeFromStream(targetStream);
      if (mimeType == null) {
        mimeType = "application/pdf";
      }
      response.setContentType(mimeType);
      response.setHeader("Content-Disposition", "attachment;filename= formatoComisiones.pdf ");
      IOUtils.copy(targetStream, response.getOutputStream());
      ServletOutputStream stream = response.getOutputStream();
      stream.flush();
      response.flushBuffer();
    } catch (IOException e) {
      logger.error("Exception {}", e.getMessage());
    }
  }

  /**
   * Descarga el archivo que se le ha cargado a la comision.
   * 
   * @param idArchivo
   * @param request
   * @param response
   * @param authentication
   * @throws IOException
   */
  @RequestMapping(value = "/descargaArchivo", method = RequestMethod.GET)
  public void getFile(Integer idArchivo, HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    logger.info("Id del archivo: {}", idArchivo);

    Archivo archivo = archivoService.consultaArchivo(idArchivo, authentication);
    logger.info("URL del archivo recuperado: {}", archivo.getUrl());
    String nombrecompleto = archivo.getUrl() + archivo.getNombre();
    String nombreArchivo = nombrecompleto;
    logger.info("Nombre del archivo: {}", nombreArchivo);

    File file = new File(nombreArchivo);
    InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
    String mimeType = URLConnection.guessContentTypeFromStream(inputStream);

    if (mimeType == null) {
      mimeType = "application/pdf";
    }

    response.setContentType(mimeType);
    response.setContentLength((int) file.length());
    response.setHeader("Content-Disposition",
        String.format("attachment; filename\"%s\"", file.getName()));
    FileCopyUtils.copy(inputStream, response.getOutputStream());
  }

  /**
   * Actualiza o carga el archivo a la comision.
   * 
   * @param archivo
   * @param idArchivo
   * @param claveUsuario
   * @param idComisionArchivo
   * @param authentication
   * @return
   */
  @PostMapping("comision/actualizaArchivo")
  public String actualizaArchivoComision(@RequestParam MultipartFile archivo, Integer idArchivo,
      String claveUsuario, Integer idComisionArchivo, Authentication authentication) {
    logger.info(
        "Actualiza o carga archivo de comision. Parametros: idComision {} claveUsuario: {} idArchivo: {}",
        new Object[] {idComisionArchivo, claveUsuario, idArchivo});

    Comision comision = new Comision();
    Archivo idArchivoAux = null;
    if (archivo != null && !archivo.isEmpty()) {
      if (idArchivo != null && !idArchivo.toString().isEmpty()) {
        archivoService.actualizaArchivo(archivo, claveUsuario, ETIQUETA_COMISIONES, idArchivo,
            "comision-", authentication);
        ComisionAux comisionAux = new ComisionAux();
        comisionAux.setIdComision(idComisionArchivo);
        comisionAux.setIdArchivo(idArchivo);
        comisionAux.setIdEstatus(1);

        comision = comisionService.modificaComisionEstatusArchivo(comisionAux, claveUsuario,
            authentication);
      } else {
        idArchivoAux = archivoService.guardaArchivo(archivo, claveUsuario, ETIQUETA_COMISIONES,
            "comision-", authentication);
        logger.info("Id de archivo recuperado: {}", idArchivoAux.getIdArchivo());

        ComisionAux comisionAux = new ComisionAux();
        comisionAux.setIdComision(idComisionArchivo);
        comisionAux.setIdArchivo(idArchivoAux.getIdArchivo());
        comisionAux.setIdEstatus(1);

        comision = comisionService.modificaComisionEstatusArchivo(comisionAux, claveUsuario,
            authentication);
      }
    }

    this.mensaje = comision.getMensaje();
    return REDIRECT_COMISIONES_EMPLEADOS;
  }

  /**
   * Acepta una comision.
   * 
   * @param idComision
   * @param claveUsuario
   * @param idArchivo
   * @param authentication
   * @return
   */
  @PostMapping("acepta")
  public String aceptarComisiones(Integer idComision, String claveUsuario, Integer idArchivo,
      Authentication authentication) {
    logger.info("Aceptar comision. Parametros: idComision {} claveUsuario: {} idArchivo: {}",
        new Object[] {idComision, claveUsuario, idArchivo});

    ComisionAux comisionAux = new ComisionAux();
    comisionAux.setIdComision(idComision);
    comisionAux.setIdEstatus(2);
    comisionAux.setIdArchivo(idArchivo);

    Comision comision =
        comisionService.modificaComisionEstatusArchivo(comisionAux, claveUsuario, authentication);

    this.mensaje = comision.getMensaje();
    return REDIRECT_COMISIONES_EMPLEADOS;
  }

  /**
   * Rechaza una comision.
   * 
   * @param idComision
   * @param claveUsuario
   * @param idArchivo
   * @param authentication
   * @return
   */
  @PostMapping("rechaza")
  public String rechazarComisiones(Integer idComision, String claveUsuario, Integer idArchivo,
      Authentication authentication) {
    logger.info("Rechazar comision. Parametros: idComision {} claveUsuario: {} idArchivo: {}",
        new Object[] {idComision, claveUsuario, idArchivo});
    ComisionAux comisionAux = new ComisionAux();
    comisionAux.setIdComision(idComision);
    comisionAux.setIdArchivo(idArchivo);
    comisionAux.setIdEstatus(3);

    Comision comision =
        comisionService.modificaComisionEstatusArchivo(comisionAux, claveUsuario, authentication);

    this.mensaje = comision.getMensaje();
    return REDIRECT_COMISIONES_EMPLEADOS;
  }

  /**
   * Cancela una comision.
   * 
   * @param idComisionCancelar
   * @param claveUsuarioCancelar
   * @param idArchivoCancelar
   * @param authentication
   * @return
   */
  @PostMapping("cancelarComision")
  public String cancelarComision(Integer idComisionCancelar, String claveUsuarioCancelar,
      Integer idArchivoCancelar, Authentication authentication) {
    logger.info("Cancelar comision. Parametros: idComision: {} claveUsuario: {}",
        idComisionCancelar, claveUsuarioCancelar);
    ComisionAux comisionAux = new ComisionAux();
    comisionAux.setIdComision(idComisionCancelar);
    comisionAux.setIdArchivo(idArchivoCancelar);
    comisionAux.setIdEstatus(6);

    Comision comision = comisionService.modificaComisionEstatusArchivo(comisionAux,
        claveUsuarioCancelar, authentication);

    this.mensaje = comision.getMensaje();
    return REDIRECT_COMISIONES_EMPLEADOS;
  }

  public String getMensaje() {
    return mensaje == null ? "" : this.mensaje;
  }

  public void setMensaje(String mensaje) {
    this.mensaje = mensaje;
  }

  /**
   * Obtiene fecha actual en un formato dado.
   * 
   * @param format
   * @return
   */
  private String obtenerFechaActual(String format) {
    Calendar now = Calendar.getInstance();
    DateFormat df = new SimpleDateFormat(format);
    Date d = now.getTime();
    return df.format(d);
  }

}
