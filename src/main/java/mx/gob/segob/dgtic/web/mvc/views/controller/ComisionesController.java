package mx.gob.segob.dgtic.web.mvc.views.controller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import mx.gob.segob.dgtic.web.mvc.service.CatalogoService;
import mx.gob.segob.dgtic.web.mvc.service.ComisionService;
import mx.gob.segob.dgtic.web.mvc.service.EstatusService;
import mx.gob.segob.dgtic.web.mvc.service.PerfilUsuarioService;
import mx.gob.segob.dgtic.web.mvc.service.UnidadAdministrativaService;
import mx.gob.segob.dgtic.web.mvc.service.UsuarioService;

@Controller
@RequestMapping(value = "comisiones", method = RequestMethod.GET)
public class ComisionesController {

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
  public String obtieneComisiones(String idUsuario, String fechaInicioBusca1, String fechaFinBusca1,
      String idEstatus, Model model, HttpSession session, Authentication authentication) {

    String string = "" + session.getAttribute("usuario");
    String[] parts = string.split(": ");
    String claveUsuario = parts[1];
    Usuario usuario = usuarioService.buscaUsuario(claveUsuario, authentication);
    idUsuario = String.valueOf(usuario.getIdUsuario());

    System.out.println("-----Peticion comisiones: ------------ " + idUsuario + " "
        + fechaInicioBusca1 + " " + fechaFinBusca1 + " " + idEstatus);

    List<Comision> lista = new ArrayList<>();
    lista = comisionService.obtenerListaComisionesPorFiltros(idUsuario, fechaInicioBusca1,
        fechaFinBusca1, idEstatus, authentication);
    System.out.println("-----Consulta comisiones: ------------ " + lista.size());
    model.addAttribute("comisiones", lista);
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
    System.out.println("Datos para comisionesEmpleados claveUsuario " + claveUsuario + " nombre "
        + nombre + " apellidoPaterno " + apellidoPaterno + " apellidoMaterno " + apellidoMaterno
        + " idUnidad " + idUnidad + " idEstatus " + idEstatus);
    if (idEstatus == null) {
      idEstatus = "";
      System.out.println("idestatus en controller " + idEstatus);
    }
    String string = "" + session.getAttribute("usuario");
    String[] parts = string.split(": ");
    String claveUsuarioLider = parts[1];
    List<PerfilUsuario> listaPerfilUsuario = new ArrayList<>();
    listaPerfilUsuario = perfilUsuarioService.recuperaPerfilesUsuario(claveUsuarioLider, authentication);
    Boolean usuarioBoolean = false;
    
    for (PerfilUsuario perfilUsuario : listaPerfilUsuario) {
      System.out.println("perfil usuario:" + perfilUsuario.getClavePerfil().getClavePerfil());
      if (perfilUsuario.getClavePerfil().getClavePerfil().equals('2')) {
        usuarioBoolean = true;
      }
    }
    System.out.println("Bandera para determinar si es Coordinador o no " + usuarioBoolean);
    Usuario usuarioAux = new Usuario();
    if (usuarioBoolean) {
      usuarioAux = usuarioService.buscaUsuario(claveUsuarioLider, authentication);
      idUnidad = idUnidad != null ? "" + usuarioAux.getIdUnidad() : "";
    } else {
      idUnidad = "";
    }
    
    System.out.println("idUnidad: " + idUnidad);
    model.addAttribute("listaUnidades",
        unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
    List<Comision> comisiones = comisionService.obtenerListaComisionesPorFiltrosEmpleados(
        claveUsuario, nombre, apellidoPaterno, apellidoMaterno, idUnidad, idEstatus, authentication);
    System.out.println("Tamaño " + comisiones.size());
    if (comisiones.size() > 0) {
      model.addAttribute("comisionesEmpleados", comisiones);
    } else {
      model.addAttribute("comisionesEmpleados", null);
    }

    model.addAttribute("listaEstatus", estatusService.obtieneListaEstatus(authentication));
    if (!this.mensaje.equals("")) {
      if (this.mensaje.contains("correctamente"))
        model.addAttribute("MENSAJE", this.mensaje);
      else
        model.addAttribute("MENSAJE_EXCEPCION", this.mensaje);
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
  public HashMap<String, Object> buscaUsuario(String idComision, Authentication authentication ) {
    HashMap<String, Object> hmap = new HashMap<String, Object>();
    System.out.println("Id comision: " + idComision);
    Comision comisiones = comisionService.obtieneComision(idComision, authentication);
    comisiones.getIdUsuario().setFechaIngreso(comisiones.getIdUsuario().getFechaIngreso());
    comisiones.setFechaInicio(comisiones.getFechaInicio());
    comisiones.setFechaFin(comisiones.getFechaFin());
    comisiones.setFechaRegistro(comisiones.getFechaRegistro());
    System.out.println("Objeto comision: " + ReflectionToStringBuilder.toString(comisiones));
    System.out.println(
        "Objeto Usuario: " + ReflectionToStringBuilder.toString(comisiones.getIdUsuario()));
    System.out.println(
        "Objeto Estatus: " + ReflectionToStringBuilder.toString(comisiones.getIdEstatus()));
    System.out.println(
        "Objeto Horario: " + ReflectionToStringBuilder.toString(comisiones.getIdHorario()));
    System.out.println(
        "Objeto Archivo: " + ReflectionToStringBuilder.toString(comisiones.getIdArchivo()));
    hmap.put("comision", comisiones);
    

    if (comisiones.getIdResponsable() != null
        && !comisiones.getIdResponsable().toString().isEmpty()) {
      hmap.put("responsable",
          usuarioService.buscaUsuarioPorId(Integer.toString(comisiones.getIdResponsable()), authentication));
    } else {
      hmap.put("responsable", null);
    }

    if (comisiones.getIdHorario() != null && comisiones.getIdHorario().getIdHorario() != null
        && !comisiones.getIdHorario().getIdHorario().toString().isEmpty()) {
      Horario horario = catalogoService.buscaHorario(comisiones.getIdHorario().getIdHorario(), authentication);
      System.out.println(
          "Horario en string: " + horario.getHoraEntrada().toString() + " - " + horario.getHoraSalida().toString());
      hmap.put("horario",
          horario.getHoraEntrada().toString() + " - " + horario.getHoraSalida().toString());
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
  public String solicitudComisiones(Model model, HttpSession session, Authentication authentication) {

    String string = "" + session.getAttribute("usuario");
    String[] parts = string.split(": ");
    String claveUsuario = parts[1];
    model.addAttribute("fechaRegistro", obtenerFechaActual("dd-MM-yyyy"));
    model.addAttribute("usuario", usuarioService.buscaUsuario(claveUsuario, authentication));
    model.addAttribute("listaResponsable",
        unidadAdministrativaService.consultaResponsable(claveUsuario, authentication));
    model.addAttribute("listaHorarios", catalogoService.obtieneHorarios(authentication));
    String cadena = catalogoService.obtieneDiaFestivoParaBloquear(authentication);
    System.out.println("Dias festivos para bloquear " + cadena);
    model.addAttribute("listaDiasFestivos", cadena);

    if (!this.getMensaje().equals("")) {
      if (this.mensaje.contains("correctamente"))
        model.addAttribute("MENSAJE", this.mensaje);
      else
        model.addAttribute("MENSAJE_EXCEPCION", this.mensaje);
    }
    this.mensaje = "";

    System.out.println("Registrar comision usuario: " + claveUsuario);
    return "/comisiones/solicitudComision";

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
      String fechaFin, Integer dias, String comision, String idHorario, Model model, HttpSession session, Authentication authentication) {

    System.out.println(
        "Datos claveUsuario " + claveUsuario + " responsable " + idResponsable + " fechaInicio "
            + fechaInicio + " fechaFin " + fechaFin + " dias " + dias + " comision " + comision);

    Integer idResponsableAux = null;
    if (idResponsable != null && !idResponsable.trim().isEmpty()) {
      idResponsableAux = Integer.parseInt(idResponsable);
    }
   
    System.out.println(
        "Datos claveUsuario " + claveUsuario + " responsable " + idResponsable + " fechaInicio "
            + fechaInicio + " fechaFin " + fechaFin + " dias " + dias + " comision " + comision);
    Comision comisionRespuesta = comisionService.agregarComision(new ComisionAux(null, null, idResponsableAux, null, 1,
        fechaInicio, fechaFin, dias, comision, obtenerFechaActual("dd/MM/yyyy"), Integer.valueOf(idHorario)), claveUsuario, authentication);
    System.out.println("Mensaje obtenido "+comisionRespuesta.getMensaje());
    this.mensaje=comisionRespuesta.getMensaje();
    
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
  public String SolitudComisionesEmpleados(String claveUsuario, String nombre,
      String apellidoPaterno, String apellidoMaterno, Model model, HttpSession session, Authentication authentication) {
    System.out.println("claveUsuario " + claveUsuario + " nombre " + nombre + " apellidoPaterno "
        + apellidoPaterno + " apellidoMaterno " + apellidoMaterno);
    String string = "" + session.getAttribute("usuario");
    String[] parts = string.split(": ");
    String claveUsuarioAux = parts[1];
    System.out.println("Datos para comisiones empleados");
    Usuario usuario = new Usuario();
    usuario = usuarioService.buscaUsuario(claveUsuarioAux, authentication);
    String idUnidad = "" + usuario.getIdUnidad();
    List<Comision> comisionesEmpleados = comisionService.obtenerComisionesPorUnidad(idUnidad,
        claveUsuario, nombre, apellidoPaterno, apellidoMaterno, authentication);

    if (comisionesEmpleados.size() > 0) {
      model.addAttribute("comisionesEmpleados", comisionesEmpleados);
    } else {
      model.addAttribute("comisionesEmpleados", null);
    }
    if (!this.getMensaje().equals("")) {
      if (this.mensaje.contains("correctamente"))
        model.addAttribute("MENSAJE", this.mensaje);
      else
        model.addAttribute("MENSAJE_EXCEPCION", this.mensaje);
    }
    model.addAttribute("listaHorarios",
        catalogoService.obtieneHorarios(authentication));
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
    System.out.println("Usuarioooooooooooooo " + claveUsuario);

    model.addAttribute("fechaRegistro", obtenerFechaActual("dd-MM-yyyy"));
    model.addAttribute("usuario", usuarioService.buscaUsuario(claveUsuario, authentication));
    model.addAttribute("listaResponsable",
        unidadAdministrativaService.consultaResponsable(claveUsuario, authentication));
    model.addAttribute("listaHorarios",
        catalogoService.obtieneHorarios(authentication));
    String cadena=catalogoService.obtieneDiaFestivoParaBloquear(authentication);
    System.out.println("Dias festivos para bloquear "+cadena);
    model.addAttribute("listaDiasFestivos", cadena);

    System.out.println("Registrar comision usuario: " + claveUsuario);
     return "/comisiones/registraComisionesEmpleados";

  }

  @RequestMapping(value = {"modificarComision"}, method = RequestMethod.GET)
  public String modificarComisiones(String idComision, String claveUsuario, Model model,
      HttpSession session, Authentication authentication) {
    System.out.println("Comision con id: " + idComision);
    System.out.println("ClaveUsuario con id: " + claveUsuario);

    model.addAttribute("comision", comisionService.obtieneComision(idComision, authentication));
    model.addAttribute("fechaRegistro", obtenerFechaActual("dd-MM-yyyy"));
    model.addAttribute("usuario", usuarioService.buscaUsuario(claveUsuario, authentication));
    model.addAttribute("listaResponsable",
        unidadAdministrativaService.consultaResponsable(claveUsuario, authentication));
    model.addAttribute("listaHorarios",
        catalogoService.obtieneHorarios(authentication));
    String cadena=catalogoService.obtieneDiaFestivoParaBloquear(authentication);
    System.out.println("Dias festivos para bloquear "+cadena);
    model.addAttribute("listaDiasFestivos", cadena);

    System.out.println("Registrar comision usuario: " + claveUsuario);
    return "/comisiones/modificarComisionEmpleados";

  }

  @RequestMapping(value = {"modificarComisionGuardado"}, method = RequestMethod.GET)
  public String modificarComisionesGuardado(String idComision, String fechaInicio, String fechaFin,
      String dias, String comision, String claveUsuario, String idHorario, Model model, HttpSession session, Authentication authentication) {
    System.out.println("Comision a ser modificada con id: " + idComision);
    System.out.println("fechaInicio " + fechaInicio + " fechaFin " + fechaFin + " diasPorPedir "
        + dias);
    ComisionAux comisioPeticion = new ComisionAux();
    comisioPeticion.setIdComision(Integer.valueOf(idComision));
    comisioPeticion.setFechaInicio(fechaInicio);
    comisioPeticion.setFechaFin(fechaFin);
    comisioPeticion.setDias(Integer.valueOf(dias));
    comisioPeticion.setComision(comision);
    comisioPeticion.setIdHorario(Integer.valueOf(idHorario));
    
    System.out.println("ComisionAux front: " + ReflectionToStringBuilder.toString(comisioPeticion));
    Comision comisionRespuesta = comisionService.modificaComisiones(comisioPeticion, claveUsuario, authentication);
    this.mensaje=comisionRespuesta.getMensaje();
    System.out.println("mensaje recuperado "+comisionRespuesta.getMensaje());
    return "redirect:/comisiones/comisionesEmpleados";

  }
  
  @RequestMapping(value = "comision/generarArchivo", method = RequestMethod.GET)
  public void generarReporteComisiones(String idComision, String idUnidadAdministrativa,
      String nombre, String apellidoPaterno, String apellidoMaterno, String fechaInicio1,
      String fechaFin1, String idHorario, String comision, HttpServletRequest request,
      HttpServletResponse response, Authentication authentication) {

    System.out.println("Datos para el archivo " + idComision + " unidadAdministrativa "
        + idUnidadAdministrativa + " nombre " + nombre + " apellidoPaterno " + apellidoPaterno
        + " apellidoMaterno " + apellidoMaterno + " fechaInicio1 " + fechaInicio1 + " fechaFin1 "
        + fechaFin1 + " idHOrario " + idHorario + " comision " + comision);
    String nombreCompleto = nombre + " " + apellidoPaterno + " " + apellidoMaterno;

    try {
      System.out.println("Datos " + nombre);
      reporte archivo =
          comisionService.generarReporte(new GenerarReporteArchivoComision(nombreCompleto,
              idUnidadAdministrativa, comision, fechaInicio1, fechaFin1, idHorario), authentication);

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
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  @RequestMapping(value = "/descargaArchivo", method = RequestMethod.GET)
  public void getFile(Integer idArchivo, HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
      System.out.println("id del archivo "+idArchivo);
      Archivo archivo= new Archivo();
      archivo=archivoService.consultaArchivo(idArchivo, authentication);
      System.out.println("archivo retornado "+archivo.getUrl());
      String nombrecompleto=archivo.getUrl()+archivo.getNombre();
      String nombreArchivo=nombrecompleto;
      System.out.println("nombre de archivo "+nombreArchivo);
      File file = new File(nombreArchivo);
      InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
      String mimeType= URLConnection.guessContentTypeFromStream(inputStream);
      if(mimeType==null){
          mimeType="application/pdf";
      }
      
      response.setContentType(mimeType);
      response.setContentLength((int)file.length());
      response.setHeader("Content-Disposition", String.format("attachment; filename\"%s\"",file.getName()));
      FileCopyUtils.copy(inputStream, response.getOutputStream());
  }
  
  @PostMapping("comision/actualizaArchivo")
  public String actualizaArchivoComision(@RequestParam MultipartFile archivo, Integer idArchivo,
      String claveUsuario, Integer idComisionArchivo, Authentication authentication) {

    System.out.println("Datos archivo " + "" + " idArchivo " + idArchivo + " claveUsuario "
        + claveUsuario + " idComision " + idComisionArchivo);
    Comision comision = new Comision();
    Archivo idArchivoAux = null;
    Archivo archivoDto = new Archivo();
    if (archivo != null && !archivo.isEmpty()) {
      if (idArchivo != null && !idArchivo.toString().isEmpty()) {
        archivoService.actualizaArchivo(archivo, claveUsuario, "comisiones", idArchivo,
            "comision-", authentication);
         comision= comisionService.modificaComisionEstatusArchivo(new ComisionAux(idComisionArchivo, null, null, idArchivo, 1, null, null, null, null, null, null), claveUsuario, authentication);
      } else {
        // idArchivoAux=archivoService.guardaArchivo(archivo, claveUsuario, "vacaciones");
        // archivoDto.setIdArchivo(idArchivoAux);
        idArchivoAux =
            archivoService.guardaArchivo(archivo, claveUsuario, "comisiones", "comision-", authentication);
        System.out.println("IDArchivo recuperado " + idArchivoAux.getIdArchivo());
        comision= comisionService.modificaComisionEstatusArchivo(new ComisionAux(idComisionArchivo, null, null, idArchivoAux.getIdArchivo(), 1, null, null, null, null, null, null), claveUsuario, authentication);
      }
    }
    System.out.println("mensaje recuperado " + comision.getMensaje());
    this.mensaje = comision.getMensaje();
    return "redirect:/comisiones/comisionesEmpleados";
  }
  
  @PostMapping("acepta")
  public String aceptarComisiones(Integer idComision,String claveUsuario, Integer idArchivo, Authentication authentication) {
      System.out.println("idLicencia "+idComision+" claveUsuario "+claveUsuario+" idArchivo "+idArchivo);
      Comision comision= comisionService.modificaComisionEstatusArchivo(new ComisionAux(idComision, null, null, idArchivo, 2, null, null, null, null, null, null), claveUsuario, authentication);
      this.mensaje=comision.getMensaje();
      System.out.println("mensaje recuperado "+comision.getMensaje());
      return "redirect:/comisiones/comisionesEmpleados";
  }
  
  @PostMapping("rechaza")
  public String rechazarComisiones(Integer idComision,String claveUsuario, Integer idArchivo, Authentication authentication) {
      System.out.println("idLicencia "+idComision+" claveUsuario "+claveUsuario+" idArchivo "+idArchivo);
      Comision comision= comisionService.modificaComisionEstatusArchivo(new ComisionAux(idComision, null, null, idArchivo, 3, null, null, null, null, null, null), claveUsuario, authentication);
      this.mensaje=comision.getMensaje();
      System.out.println("mensaje recuperado "+comision.getMensaje());
      return "redirect:/comisiones/comisionesEmpleados";
  }
  
  @PostMapping("cancelarComision")
  public String cancelarComision(Integer idComisionCancelar,String claveUsuarioCancelar, Integer idArchivoCancelar,Authentication authentication) {
      System.out.println("CancelarComision");
      System.out.println("idComision "+idComisionCancelar+" claveUsuario "+claveUsuarioCancelar);
      Comision comision= comisionService.modificaComisionEstatusArchivo(new ComisionAux(idComisionCancelar, null, null, idArchivoCancelar, 6, null, null, null, null, null, null), claveUsuarioCancelar, authentication);
      this.mensaje=comision.getMensaje();
      System.out.println("mensaje recuperado "+comision.getMensaje());
      return "redirect:/comisiones/comisionesEmpleados";
  }

  public String getMensaje() {
    return mensaje == null ? "" : this.mensaje;
  }

  public void setMensaje(String mensaje) {
    this.mensaje = mensaje;
  }

  /**
   * Obtiene fecha actual en formato dd-MM-yyyy
   * 
   * @return
   */
  private String obtenerFechaActual(String format) {
    Calendar now = Calendar.getInstance();
    DateFormat df = new SimpleDateFormat(format);
    Date d= now.getTime();
    return df.format(d);
  }

}
