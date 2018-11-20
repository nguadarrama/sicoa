package mx.gob.segob.dgtic.web.mvc.views.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import mx.gob.segob.dgtic.web.config.security.handler.LogoutCustomHandler;
import mx.gob.segob.dgtic.web.mvc.dto.Archivo;
import mx.gob.segob.dgtic.web.mvc.dto.BusquedaDto;
import mx.gob.segob.dgtic.web.mvc.dto.Estatus;
import mx.gob.segob.dgtic.web.mvc.dto.GeneraReporteArchivo;
import mx.gob.segob.dgtic.web.mvc.dto.PerfilUsuario;
import mx.gob.segob.dgtic.web.mvc.dto.Periodo;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;
import mx.gob.segob.dgtic.web.mvc.dto.VacacionPeriodo;
import mx.gob.segob.dgtic.web.mvc.dto.Vacaciones;
import mx.gob.segob.dgtic.web.mvc.dto.VacacionesAux;
import mx.gob.segob.dgtic.web.mvc.dto.reporte;
import mx.gob.segob.dgtic.web.mvc.service.ArchivoService;
import mx.gob.segob.dgtic.web.mvc.service.CatalogoService;
import mx.gob.segob.dgtic.web.mvc.service.EstatusService;
import mx.gob.segob.dgtic.web.mvc.service.PerfilUsuarioService;
import mx.gob.segob.dgtic.web.mvc.service.PeriodoService;
import mx.gob.segob.dgtic.web.mvc.service.UnidadAdministrativaService;
import mx.gob.segob.dgtic.web.mvc.service.UsuarioService;
import mx.gob.segob.dgtic.web.mvc.service.VacacionPeriodoService;
import mx.gob.segob.dgtic.web.mvc.service.VacacionesService;
import mx.gob.segob.dgtic.web.mvc.views.controller.constants.ConstantsController;


@Controller
@RequestMapping( value = ConstantsController.VACACIONES)
public class VacacionesController {
	
	private static final Logger logger = LoggerFactory.getLogger(LogoutCustomHandler.class);
	private static final String WARN = "Warn : {} ";
	private static final String VACACION = "vacacion-";
	private static final String REDIRECT_VAC_EMPLEADOS = "redirect:/vacaciones/vacacionesEmpleados";
	private static final String REDIRECT_VAC_PROPIAS = "redirect:/vacaciones/vacacionesPropias";
	
	@Autowired
	private VacacionesService vacacionesService;
	
	@Autowired
	private PeriodoService periodoService;
	
	@Autowired 
	private ArchivoService archivoService;
	
	@Autowired 
	private UnidadAdministrativaService unidadAdministrativaService;
	
	@Autowired
	private EstatusService estatusService;
	
	@Autowired 
	private UsuarioService usuarioService;
	
	@Autowired 
	private PerfilUsuarioService perfilUsuarioService;
	
	@Autowired
	private VacacionPeriodoService vacacionPeriodoService;
	
	@Autowired 
	private CatalogoService catalogoService;
	
	private String mensaje = "";
	
	private String global="";
	
    @RequestMapping(value={"solicitudVacaciones"}, method = RequestMethod.GET)
    public String obtieneAsistencias(Model model, HttpSession session, Authentication authentication) {
	    String string = ""+ session.getAttribute(ConstantsController.USUARIO);
    	String[] parts = string.split(": ");
    	String claveUsuario = parts[1];
    	Periodo periodo;
    	String cadena = catalogoService.obtieneDiaFestivoParaBloquear(authentication);
	    model.addAttribute("listaDiasFestivos", cadena);
    	periodo = periodoService.buscaPeriodoPorClaveUsuario(claveUsuario, authentication);
    	String fechas=vacacionesService.recuperaDiasVacacioness(claveUsuario, authentication);
    	if(!fechas.isEmpty()){
    		if(!cadena.isEmpty()){
    			cadena+=","+fechas;
    		}else{
    			cadena=fechas;
    		}
    	}
    	periodo.setMensaje(cadena);
	   
	    if(periodo.getIdPeriodo()!=null && !periodo.getIdPeriodo().toString().isEmpty()){
	    	 model.addAttribute(ConstantsController.PERIODO,periodo);
	    	 VacacionPeriodo vaca; 
	    	 vaca=vacacionesService.buscaVacacionPeriodoPorClaveUsuarioYPeriodo(claveUsuario,periodo.getIdPeriodo(), authentication);
	    	 try{
		    	 if(vaca.getDias()>0 && vaca.getDias()!=null){
		    	 model.addAttribute(ConstantsController.VACACION,vaca);
		    	 }else{
		    		 model.addAttribute(ConstantsController.VACACION,null);
		    	 }
	    	 }
	    	 catch(Exception e){
	    		 logger.warn(WARN,e);
	    	 }
	    }else{
	    	model.addAttribute(ConstantsController.PERIODO,null);
	    	 model.addAttribute(ConstantsController.VACACION,null);
	    }
	    logger.info("idPeriodo {} ",periodo.getIdPeriodo());
	    
	    model.addAttribute(ConstantsController.LISTA_RESPONSABLE,unidadAdministrativaService.consultaResponsable(claveUsuario, authentication));
	    model.addAttribute(ConstantsController.LISTA_UNIDADES,unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
	    model.addAttribute(ConstantsController.LISTA_ESTATUS,estatusService.obtieneListaEstatus(authentication));
	    model.addAttribute(ConstantsController.USUARIO,usuarioService.buscaUsuario(claveUsuario,authentication));
	    
	    if(!this.getMensaje().equals("")){
			if(this.mensaje.contains(ConstantsController.CORRECTAMENTE)){
				model.addAttribute(ConstantsController.MENSAJE, this.mensaje);
			}else if(this.mensaje.contains("10")){
				model.addAttribute(ConstantsController.MENSAJE_ALERTA, this.mensaje);
			}else{
				model.addAttribute(ConstantsController.MENSAJE_EXCEPTION, this.mensaje);
			}
			
		}
		this.mensaje = "";
    	return "/vacaciones/solicitudVacaciones";
    }
    @GetMapping("vacacion/busca")
	@ResponseBody
	public Map<String, Object> buscaUsuario(String idVacacion, Authentication authentication) {
		HashMap<String, Object> hmap = new HashMap<>();
		logger.info("vamos bien: {} ",idVacacion);
		VacacionesAux vacaciones;
		vacaciones = vacacionesService.obtieneVacacion(idVacacion, authentication);
		hmap.put(ConstantsController.VACACION,vacaciones);
		if(vacaciones.getIdResponsable()!=null && !vacaciones.getIdResponsable().toString().isEmpty()){
			hmap.put(ConstantsController.RESPONSABLE, usuarioService.buscaUsuarioPorId(Integer.toString(vacaciones.getIdResponsable()),authentication));
		}else{
			hmap.put(ConstantsController.RESPONSABLE, null);
		}
		return hmap;
	}
    
    @RequestMapping(value= "vacacion/generaArchivo",method = RequestMethod.GET)
	public void  generaReporteVacaciones(String idSolicitud,String idEstatus,String idPuesto,String idUnidadAdministrativa,String numeroEmpleado,
			String fechaIngreso,String rfc,String nombre,String apellidoPaterno,String apellidoMaterno, String fechaInicio1, String fechaFin1, 
			String dias, String responsable, String idVacacion, String responsableAux,String fechaSolicitud ,HttpServletRequest request, HttpServletResponse response, Authentication authentication){
    	
    	try {
    		logger.info("Datos: {} ",nombre);
			Map<String,Object> parametros = new HashMap<>();
			parametros.put("nombre", nombre);
			parametros.put("apellidoPaterno", apellidoPaterno);
			parametros.put("apellidoMaterno", apellidoMaterno);
			parametros.put("rfc", rfc);
			parametros.put("idSolicitud", idSolicitud);
			parametros.put("idEstatus", idEstatus);
			parametros.put("idPuesto", idPuesto);
			parametros.put("unidadAdministrativa", idUnidadAdministrativa);
			
			parametros.put(ConstantsController.NUMERO_EMPLEADO, numeroEmpleado);
			parametros.put("fechaIngreso", fechaIngreso);
			parametros.put("fechaInicio", fechaInicio1);
			parametros.put("fechaFin", fechaFin1);
			parametros.put(ConstantsController.RESPONSABLE, responsable);
			parametros.put(ConstantsController.NUMERO_EMPLEADO, numeroEmpleado);
			parametros.put(ConstantsController.RESPONSABLE, responsable);
			parametros.put(ConstantsController.NUMERO_EMPLEADO, numeroEmpleado);
			parametros.put("diasVacaciones", dias);
			if(responsable==null || responsable.trim().isEmpty()){
				responsable=responsableAux;
			}
			
			reporte archivo = vacacionesService.generaReporte(new GeneraReporteArchivo (idSolicitud, idEstatus, idPuesto, idUnidadAdministrativa, numeroEmpleado, fechaIngreso, rfc, nombre, apellidoPaterno, apellidoMaterno, fechaInicio1, fechaFin1, dias, responsable, idVacacion, fechaSolicitud), authentication);
			InputStream targetStream = new ByteArrayInputStream(archivo.getNombre());
			String mimeType = URLConnection.guessContentTypeFromStream(targetStream);
			if(mimeType == null){
				mimeType = "application/pdf";
			}
			response.setContentType(mimeType);
			response.setHeader( "Content-Disposition", "attachment;filename= formatoVacaciones.pdf " );
			IOUtils.copy(targetStream, response.getOutputStream());
	        ServletOutputStream stream = response.getOutputStream();
	        stream.flush();
	        response.flushBuffer();
		} catch (IOException e) {
			logger.warn(WARN, e);
		}catch (Exception e) {
			logger.warn("wARN.. {}", e);
		}
	}
    
    @RequestMapping(value={"vacacionesPropias"}, method = RequestMethod.GET)
    public String obtieneAsistenciasPropias(String idPeriodo, String idEstatus, String fechaInicioBusca1, String fechaFinBusca1 ,Model model, HttpSession session, Authentication authentication) {
    	
    	if(idEstatus==null || idEstatus.isEmpty()){
    		idEstatus="";
    	}
    	if(idPeriodo==null || idPeriodo.isEmpty()){
    		idPeriodo="";
    	}
    	if(fechaInicioBusca1==null || fechaInicioBusca1.isEmpty()){
    		fechaInicioBusca1="";
    	}
    	if(fechaFinBusca1==null || fechaFinBusca1.isEmpty()){
    		fechaFinBusca1="";
    	}
    	String string=""+ session.getAttribute(ConstantsController.USUARIO);
    	String[] parts = string.split(": ");
    	String claveUsuario = parts[1];
    	logger.info("Datos para vacacionesPropias");
	    model.addAttribute(ConstantsController.LISTA_UNIDADES,unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
	    model.addAttribute("listaPeriodos",periodoService.periodos(authentication));
    	List<Vacaciones> vacaciones;
    	BusquedaDto busquedaDto = new BusquedaDto();
    	busquedaDto.setClaveUsuario(claveUsuario);
    	busquedaDto.setIdEstatus(idEstatus);
    	busquedaDto.setIdPeriodo(idPeriodo);
    	busquedaDto.setFechaInicio(fechaInicioBusca1);
    	busquedaDto.setFechaFin(fechaFinBusca1);
    	vacaciones = vacacionesService.consultaVacacionesPropiasPorFiltros(busquedaDto, authentication);
    	if(!vacaciones.isEmpty()){
    		model.addAttribute("vacacionesPropias",vacaciones);	
    	}else{
    	model.addAttribute("vacacionesPropias",null);
    	}
    	model.addAttribute(ConstantsController.LISTA_ESTATUS,estatusService.obtieneListaEstatus(authentication));
    	if(!this.getMensaje().equals("")){
			if(this.mensaje.contains(ConstantsController.CORRECTAMENTE)){
				model.addAttribute(ConstantsController.MENSAJE, this.mensaje);
			}else if(this.mensaje.contains("10")){
				model.addAttribute(ConstantsController.MENSAJE_ALERTA, this.mensaje);
			}else{
				model.addAttribute(ConstantsController.MENSAJE_EXCEPTION, this.mensaje);
			}
			
		}
		this.mensaje = "";
    	return "/vacaciones/vacacionesPropias"; 
    	
    }
    @RequestMapping(value={"solicitudVacacionesEmpleados"}, method = RequestMethod.GET)
    public String SolitudVacacionesEmpleados(String claveUsuario, String nombre, String apellidoPaterno, String apellidoMaterno, Model model,HttpSession session, Authentication authentication) {
    	
    	String string=""+ session.getAttribute(ConstantsController.USUARIO);
    	String[] parts = string.split(": ");
    	String claveUsuarioAux = parts[1];
    	logger.info("Datos para vacacionesPropias");
    	Usuario usuario;
    	usuario=usuarioService.buscaUsuario(claveUsuarioAux, authentication);
    	
    	String idUnidad=""+usuario.getIdUnidad();
    	logger.info("id de la unidad administrativa {} ",idUnidad);
    	List<VacacionPeriodo> conVacaciones;
    	conVacaciones=vacacionPeriodoService.obtenerUsuariosVacacionesPorFiltros(claveUsuario, nombre, apellidoPaterno, apellidoMaterno, idUnidad, authentication);
    	if(!conVacaciones.isEmpty()){
    	model.addAttribute("usuariosConVacaciones",conVacaciones);
    	}else{
    		model.addAttribute("usuariosConVacaciones",null);
    	}
    	if(!this.getMensaje().equals("")){
			if(this.mensaje.contains(ConstantsController.CORRECTAMENTE)){
				model.addAttribute(ConstantsController.MENSAJE, this.mensaje);
			}else if(this.mensaje.contains("10")){
				model.addAttribute(ConstantsController.MENSAJE_ALERTA, this.mensaje);
			}else{
				model.addAttribute(ConstantsController.MENSAJE_EXCEPTION, this.mensaje);
			}
			
		}
		this.mensaje = "";
    	return "/vacaciones/solicitudVacacionesEmpleados"; 
    	
    }
    @RequestMapping(value={"vacacionesEmpleados"}, method = RequestMethod.GET)
    public String obtieneVacacionesEmpleados(String claveUsuario, String nombre, String apellidoPaterno, String apellidoMaterno, String idUnidad, String idEstatus, Model model,HttpSession session, Authentication authentication) {
    	logger.info("Datos para vacacionesEmpleados claveUsuario.- {} ",claveUsuario);
    	logger.info("nombre.- {} ",nombre);
    	logger.info("apellidoPaterno.- {} ",apellidoPaterno);
    	logger.info("apellidoMaterno-. {} ",apellidoMaterno);
    	logger.info("idUnidad {}",idUnidad);
    	logger.info("idEstatus: {} ",idEstatus );
    	if(nombre==null || nombre.trim().isEmpty()){
			nombre="";
		}
		if(claveUsuario==null || claveUsuario.trim().isEmpty()){
			claveUsuario="";
		}
		if(apellidoPaterno==null || apellidoPaterno.trim().isEmpty()){
			apellidoPaterno="";
		}
		if(apellidoMaterno==null || apellidoMaterno.trim().isEmpty()){
			apellidoMaterno="";
		}
		if(idEstatus==null || idEstatus.trim().isEmpty()){
			idEstatus="";
		}
		if(idUnidad==null || idUnidad.trim().isEmpty()){
			idUnidad="";
		}
    	String string=""+ session.getAttribute(ConstantsController.USUARIO);
    	String[] parts = string.split(": ");
    	String claveUsuarioLider = parts[1];
    	List<PerfilUsuario> listaPerfilUsuario;
    	listaPerfilUsuario=perfilUsuarioService.recuperaPerfilesUsuario(claveUsuarioLider, authentication);
    	Boolean usuario= false;
    	for(PerfilUsuario perfilUsuario: listaPerfilUsuario){
    		if(idUnidad==null || idUnidad.isEmpty()){
    			logger.info("Entrando al if {} ",perfilUsuario.getClavePerfil().getClavePerfil());
	    		if(perfilUsuario.getClavePerfil().getClavePerfil().equals("2")){
	    			logger.info("Entrando al if {} ",perfilUsuario.getClavePerfil().getClavePerfil());
	    			usuario=true;
	    			Usuario usuarioAux;
	    	    	if(usuario){
	    	    		
	    	    		usuarioAux=usuarioService.buscaUsuario(claveUsuarioLider, authentication);
	    	    		idUnidad=""+usuarioAux.getIdUnidad();
	    	    	}
	    		}
	    		if(perfilUsuario.getClavePerfil().getClavePerfil().equals("1")){
	    			idUnidad="";
	    		}
    		}
    		logger.info("valor para idUnidad {} ",idUnidad);
    	}
    	logger.info("Bandera para determinar si es empleado o no: {} ", usuario);
    	logger.info("claveUsuario: {} ",claveUsuario);
    	
	    model.addAttribute(ConstantsController.LISTA_UNIDADES,unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
	    List<Vacaciones> vacaciones;
	    BusquedaDto busquedaDto = new BusquedaDto();
    	busquedaDto.setClaveUsuario(claveUsuario);
    	busquedaDto.setNombre(nombre);
    	busquedaDto.setApellidoPaterno(apellidoPaterno);
    	busquedaDto.setApellidoMaterno(apellidoMaterno);
    	busquedaDto.setIdUnidad(idUnidad);
    	busquedaDto.setIdEstatus(idEstatus);
	    vacaciones=vacacionesService.obtenerVacacionesPorFiltros(busquedaDto, authentication);
	    logger.info("Tamaño {} ",vacaciones.size());
	    if(!vacaciones.isEmpty()){
	    	model.addAttribute("vacacionesEmpleados",vacaciones);
	    }else{
	    	model.addAttribute("vacacionesEmpleados",null);
	    }
	    
    	model.addAttribute(ConstantsController.LISTA_ESTATUS,estatusService.obtieneListaEstatus(authentication));
    	if(!this.getMensaje().equals("")){
			if(this.mensaje.contains(ConstantsController.CORRECTAMENTE)){
				model.addAttribute(ConstantsController.MENSAJE, this.mensaje);
			}else if(this.mensaje.contains("10")){
				model.addAttribute(ConstantsController.MENSAJE_ALERTA, this.mensaje);
			}else{
				model.addAttribute(ConstantsController.MENSAJE_EXCEPTION, this.mensaje);
			}
			
		}
		this.mensaje = "";
    	return "/vacaciones/vacacionesEmpleados"; 
    }
    
    @RequestMapping(value={"busca"}, method = RequestMethod.GET)
    public String buscaAsistencia(Model model, Authentication authentication) {
	    
	    model.addAttribute("listaVacaciones", vacacionesService.obtieneVacaciones(authentication));
	    
    	return "/vacaciones/busca";
    }
    
    @RequestMapping(value={"vacacion/buscaUsuario"}, method = RequestMethod.GET)
    public String buscaUsuario(String claveUsuario, Model model, Authentication authentication) {
    	logger.info("Usuarioooooooooooooo para buscar {} ",claveUsuario);
	    global=claveUsuario;
    	Periodo periodo;
    	logger.info("periodo.getIdPeriodo() {} ",claveUsuario);
    	periodo = periodoService.buscaPeriodoPorClaveUsuario(claveUsuario, authentication);
    	String cadena=catalogoService.obtieneDiaFestivoParaBloquear(authentication);
    	logger.info("Dias festivos para bloquear {} ",cadena);
	    model.addAttribute("listaDiasFestivos", cadena);
    	periodo=periodoService.buscaPeriodoPorClaveUsuario(claveUsuario, authentication);
    	String fechas=vacacionesService.recuperaDiasVacacioness(claveUsuario, authentication);
    	if(!fechas.isEmpty()){
    		if(!cadena.isEmpty()){
    			cadena+=","+fechas;
    		}else{
    			cadena=fechas;
    		}
    	}
    	periodo.setMensaje(cadena);
    	model.addAttribute(ConstantsController.USUARIO,usuarioService.buscaUsuario(claveUsuario, authentication));
    	logger.info("periodo.getIdPeriodo() {} ",periodo.getIdPeriodo());
	    if(periodo.getIdPeriodo()!=null && !periodo.getIdPeriodo().toString().isEmpty()){
	    	 model.addAttribute(ConstantsController.PERIODO,periodo);
	    	 logger.info("Datos claveUsuario {}", claveUsuario);
	    	 logger.info("periodo.getIdPeriodo(){} ",periodo.getIdPeriodo());
	    	 VacacionPeriodo vaca; 
	    	 vaca=vacacionesService.buscaVacacionPeriodoPorClaveUsuarioYPeriodo(claveUsuario,periodo.getIdPeriodo(), authentication);
	    	 try{
		    	 if(vaca.getDias()>0 && vaca.getDias()!=null){
		    	 model.addAttribute(ConstantsController.VACACION,vaca);
		    	 }else{
		    		 model.addAttribute(ConstantsController.VACACION,null);
		    	 }
	    	 }
	    	 catch(Exception e){
	    		 logger.warn("Warn: {} ", e);
	    	 }
	    }else{
	    	model.addAttribute(ConstantsController.PERIODO,null);
	    	 model.addAttribute(ConstantsController.VACACION,null);
	    }
	    logger.info("idPeriodo {} ",periodo.getIdPeriodo());
	    model.addAttribute(ConstantsController.LISTA_UNIDADES,unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
	    model.addAttribute(ConstantsController.LISTA_ESTATUS,estatusService.obtieneListaEstatus(authentication));
    	return "/vacaciones/registraVacacionesEmpleados";
    	
    }
    
    @PostMapping("/vacacion/modifica")
    public String registraVacacioness(@RequestParam String fechaInicio, @RequestParam String fechaFin, @RequestParam Integer diasPorPedir, @RequestParam Integer idPeriodo, @RequestParam String idVacacion ,Integer responsable, HttpSession session, Authentication authentication ) {
    	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    	logger.info("fechaInicio.- {} ",fechaInicio);
     	logger.info("fechaFin-. {} ",fechaFin);
     	logger.info("diasPorPedir-. {} ",diasPorPedir);
     	logger.info("idPeriodo-. {} ",idPeriodo);
     	logger.info("idVacacion. {} ",idVacacion);
    	logger.info("responsable.: {} ",responsable);
    	Date fechaInicial = new Date();
    	
    	try {
    		fechaInicial = df.parse(fechaInicio);
    		
    		logger.info("fechaInicio: {} ",fechaInicial);
		} catch (ParseException e) {
			logger.warn(WARN,e);
		}
    	 String string=""+ session.getAttribute(ConstantsController.USUARIO);
     	String[] parts = string.split(": ");
     	String claveUsuario = parts[1];
     	VacacionPeriodo vacacion=new  VacacionPeriodo();
     	vacacion.setIdVacacion(Integer.parseInt(idVacacion));
     	Estatus estatus=new Estatus();
     	estatus.setIdEstatus(1);
     	VacacionPeriodo vacacion1= new VacacionPeriodo();
     	vacacion1.setIdVacacion(Integer.parseInt(idVacacion));
   
     	Vacaciones respuesta=vacacionesService.agregaVacaciones(new VacacionesAux(null,vacacion1, responsable, estatus, fechaInicio, fechaFin, diasPorPedir), claveUsuario,authentication);
     	logger.info("fechaInicio..- {} ",fechaInicio);
     	logger.info("fechaFin-.. {} ",fechaFin);
     	logger.info("diasPorPedir..- {} ",diasPorPedir);
     	logger.info("idPeriodo-.. {} ",idPeriodo);
     	logger.info("claveUsuario... {} ",claveUsuario);
     	logger.info("respuesta con mensaje {} ",respuesta.getMensaje());
    	
    	this.mensaje=respuesta.getMensaje();
    	
    	return REDIRECT_VAC_PROPIAS;
    }
    @PostMapping("/vacacion/guardaVacacion")
    public String registraVacacionesEmpleado( String fechaInicio,  String fechaFin,  Integer diasPorPedir,  Integer idPeriodo,  String idVacacion,Integer responsable,  String claveUsuario, HttpSession session, Authentication authentication) {
    	DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
    	claveUsuario = global;
    	logger.info("fechaInicio- {} ",fechaInicio);
     	logger.info("fechaFin- {} ",fechaFin);
     	logger.info("diasPorPedir- {} ",diasPorPedir);
     	logger.info("idPeriodo- {} ",idPeriodo);
     	logger.info("claveUsuario. {} ",claveUsuario);
    	logger.info("responsable.{} ",responsable);
    	Date fechaInicial = new Date();
    	Date fechaFinal = new Date();
    	try {
    		fechaInicial = df.parse(fechaInicio);
    		fechaFinal=df.parse(fechaFin);
    		logger.info("fechaInicio: {} ",fechaInicial);
		} catch (ParseException e) {
			logger.warn(WARN, e);
		}

     	VacacionPeriodo vacacion=new  VacacionPeriodo();
     	vacacion.setIdVacacion(Integer.parseInt(idVacacion));
     	Estatus estatus=new Estatus();
     	estatus.setIdEstatus(1);
     	Vacaciones vacaciones= vacacionesService.agregaVacaciones(new VacacionesAux(null,vacacion, responsable, estatus, fechaInicio, fechaFin, diasPorPedir), claveUsuario, authentication);
     	logger.info("Mensaje obtenido {}",vacaciones.getMensaje());
     	this.mensaje=vacaciones.getMensaje();
     	logger.info("fechaInicio- {} ",fechaInicial);
     	logger.info("fechaFin- {} ",fechaFinal);
     	logger.info("diasPorPedir- {} ",diasPorPedir);
     	logger.info("idPeriodo- {} ",idPeriodo);
     	logger.info("claveUsuario. {} ",claveUsuario);
     	global="";
     	return "redirect:/vacaciones/solicitudVacacionesEmpleados";
    }
    @GetMapping("vacacion/acepta")
    public String aceptaVacaciones(Integer idSolicitud, String fechaInicio, String fechaFin,Integer id, Authentication authentication) {
    	logger.info("idVacacion. {} ",idSolicitud);
    	logger.info("fechaInicio.- {} ",fechaInicio);
    	logger.info("fechaFinal.-  {} ",fechaFin);
    	logger.info("idUsuario.-. {} ",id);
    	ParsePosition pos = new ParsePosition(4);
    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy, hh:mm:ss a");
    	Date fechaInicial =null;
    	Date fechaFinal =null;
    	Usuario usuario=new Usuario();
    	usuario.setIdUsuario(id);
    	Estatus estatus= new Estatus();
        estatus.setIdEstatus(2);
        fechaInicial = formatter.parse(fechaInicio,pos);
		fechaFinal=formatter.parse(fechaFin,pos);
		logger.info("fechaInicio {} ",fechaInicial);
		Vacaciones vacaciones = vacacionesService.aceptaORechazaVacaciones(new Vacaciones(usuario,null,null,null,estatus,fechaInicial,fechaFinal,null), idSolicitud, authentication);
		this.mensaje=vacaciones.getMensaje();
    	
    	return REDIRECT_VAC_EMPLEADOS;
    }
    
    @GetMapping("vacacion/actualizaPropiasVacaciones")
    public String actualizaPropiasVacaciones(Integer id,String idUsuario,Integer idVacacion,Integer dias, Authentication authentication) {
    	logger.info("idVacacion : {}",id);
    	logger.info("idUsuario --{} ",idUsuario);
    	logger.info(".idVacacion {} ",idVacacion);
    	logger.info(". dias {}",dias);
    	VacacionPeriodo vacacion=new  VacacionPeriodo();
    	vacacion.setIdVacacion(idVacacion);
    	Usuario usuario=new Usuario();
    	usuario.setIdUsuario(Integer.parseInt(idUsuario));
    	Estatus estatus= new Estatus();
        estatus.setIdEstatus(3);
    	Vacaciones vacaciones=vacacionesService.aceptaORechazaVacaciones(new Vacaciones(usuario,vacacion,null,null,estatus,null,null,dias), id, authentication);
    	this.mensaje=vacaciones.getMensaje();
    	return REDIRECT_VAC_PROPIAS;
    
    }
    @GetMapping("vacacion/actualizaVacacionesEmpleados")
    public String actualizaVacacionesEmpleados(Integer id,String idUsuario,Integer idVacacion,Integer dias, Authentication authentication) {
    	logger.info("idVacacion : {}",id);
    	logger.info("idUsuario {} ",idUsuario);
    	logger.info(".idVacacion {} ",idVacacion);
    	logger.info(". dias {}",dias);
    	VacacionPeriodo vacacion = new  VacacionPeriodo();
    	vacacion.setIdVacacion(idVacacion);
    	Usuario usuario = new Usuario();
    	usuario.setIdUsuario(Integer.parseInt(idUsuario));
    	Estatus estatus = new Estatus();
        estatus.setIdEstatus(3);
    	Vacaciones vacaciones = vacacionesService.aceptaORechazaVacaciones(new Vacaciones(usuario,vacacion,null,null,estatus,null,null,dias), id, authentication);
    	this.mensaje = vacaciones.getMensaje();
    	return REDIRECT_VAC_EMPLEADOS;
    
    }
    
    @PostMapping("/vacacion/actualizaArchivo")
    public String registraVacaciones(@RequestParam MultipartFile archivo, Integer idArchivo,String claveUsuario,Integer idDetalle, Authentication authentication ){
    	logger.info("Datos archivo: {} ",archivo);
    	logger.info("idArchivo- {} ",idArchivo);
    	logger.info("claveUsuario.- {} ",claveUsuario);
    	Archivo idArchivoAux;
    	Vacaciones vacaciones= new Vacaciones();
    	Archivo archivoDto=new Archivo();
    	vacaciones.setIdDetalle(idDetalle);
    	if(idArchivo!=null && !idArchivo.toString().isEmpty()){
    		idArchivoAux=archivoService.actualizaArchivo(archivo, claveUsuario, ConstantsController.VACACIONES,idArchivo,VACACION, authentication);
    		archivoDto.setIdArchivo(idArchivo);
    		vacaciones.setIdArchivo(archivoDto);
    		vacacionesService.modificaVacaciones(vacaciones, authentication);
    	}else{
    		idArchivoAux=archivoService.guardaArchivo(archivo, claveUsuario, ConstantsController.VACACIONES,VACACION, authentication);
    		archivoDto.setIdArchivo(idArchivoAux.getIdArchivo());
    		vacaciones.setIdArchivo(archivoDto);
    		vacacionesService.modificaVacaciones(vacaciones, authentication);
    	}
    	this.mensaje=idArchivoAux.getMensaje();
    	return REDIRECT_VAC_PROPIAS;
    	}
    @PostMapping("/vacacion/actualizaArchivoEmpleado")
    public String registraVacacionesEmpleado(@RequestParam MultipartFile archivo, Integer idArchivo,String claveUsuario,Integer idDetalle, Authentication authentication ){
    	logger.info("Datos archivo: {} ",archivo);
    	logger.info("idArchivo- {} ",idArchivo);
    	logger.info("claveUsuario- {} ",claveUsuario);
    	logger.info("idDetalle- {} ",idDetalle);
    	Archivo idArchivoAux;
    	Vacaciones vacaciones = new Vacaciones();
    	Archivo archivoDto = new Archivo();
    	vacaciones.setIdDetalle(idDetalle);
    	if(idArchivo!=null && !idArchivo.toString().isEmpty()){
    		logger.info("id de archivo actualizar: {} ",idArchivo);
    		idArchivoAux=archivoService.actualizaArchivo(archivo, claveUsuario, ConstantsController.VACACIONES,idArchivo,VACACION, authentication);
    		archivoDto.setIdArchivo(idArchivo);
    		vacaciones.setIdArchivo(archivoDto);
    		vacacionesService.modificaVacaciones(vacaciones, authentication);
    	}else{
    		idArchivoAux = archivoService.guardaArchivo(archivo, claveUsuario, ConstantsController.VACACIONES,VACACION, authentication);
    		logger.info("id de archivo guardar {} ",idArchivoAux.getIdArchivo());
    		archivoDto.setIdArchivo(idArchivoAux.getIdArchivo());
    		vacaciones.setIdArchivo(archivoDto);
    		vacacionesService.modificaVacaciones(vacaciones, authentication);
    	}
    	this.mensaje=idArchivoAux.getMensaje();
    	return REDIRECT_VAC_EMPLEADOS;
    	}
    
    @GetMapping("vacacion/rechaza")
    public String rechazaVacaciones(Integer idSolicitud,String idUsuario,Integer idVacacion,Integer dias, Authentication authentication) {
    	logger.info("idVacacion {} ",idSolicitud);
    	logger.info("idUsuario {} ",idUsuario);
    	logger.info("idVacacion: {} ",idVacacion);
    	logger.info("dias{} ",dias);
    	VacacionPeriodo vacacion=new  VacacionPeriodo();
    	vacacion.setIdVacacion(idVacacion);
    	Usuario usuario = new Usuario();
    	usuario.setIdUsuario(Integer.parseInt(idUsuario));
    	Estatus estatus = new Estatus();
        estatus.setIdEstatus(3);
        Vacaciones vacaciones;
        vacaciones = vacacionesService.aceptaORechazaVacaciones(new Vacaciones(usuario,vacacion,null,null,estatus,null,null,dias), idSolicitud, authentication);
    	this.mensaje = vacaciones.getMensaje();
    	return REDIRECT_VAC_EMPLEADOS;
    }
    
    @RequestMapping(value = "/descargaArchivo", method = RequestMethod.GET)
    public void getFile(Integer idArchivo, HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
    	logger.info("id del archivo .{} ",idArchivo);
    	Archivo archivo;
    	archivo=archivoService.consultaArchivo(idArchivo, authentication);
    	logger.info("archivo retornado : {}",archivo.getUrl());
    	String nombrecompleto=archivo.getUrl()+archivo.getNombre();
    	String nombreArchivo = nombrecompleto;
    	logger.info("nombre de archivo: {} ",nombreArchivo);
    	File file = new File(nombreArchivo);
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        String mimeType= URLConnection.guessContentTypeFromStream(inputStream);
        if(mimeType == null){
        	mimeType = "application/pdf";
        }
        
        response.setContentType(mimeType);
        response.setContentLength((int)file.length());
        response.setHeader("Content-Disposition", String.format("attachment; filename\"%s\"",file.getName()));
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }
    
    @RequestMapping(value={"eliminaVacacion"}, method = RequestMethod.POST)
    public String eliminaVacacion(Integer idVacacionEliminar, Authentication authentication) {
    	logger.info("id para eliminar: {} ",idVacacionEliminar);
	    Vacaciones vacaciones;
	    vacaciones = vacacionesService.eliminaVacaciones(idVacacionEliminar, authentication);
	    this.mensaje = vacaciones.getMensaje();
	    return REDIRECT_VAC_PROPIAS;
	    
    }
    
    @RequestMapping(value={"cancelaVacacion"}, method = RequestMethod.POST)
    public String cancelaVacacion(Integer idVacacionCancelar, Authentication authentication) {
	    logger.info("id para cancelar: {} ",idVacacionCancelar);
	    Vacaciones vacaciones;
	    vacaciones=vacacionesService.cancelaVacaciones(idVacacionCancelar, authentication);
	    this.mensaje=vacaciones.getMensaje();
	    return REDIRECT_VAC_EMPLEADOS;
	    
    }
    
	public String getMensaje() {
		return mensaje == null ? "" : this.mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
    
    
}
