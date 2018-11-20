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
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PathVariable;
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


@Controller
@RequestMapping( value = "vacaciones")
public class VacacionesController {
	
	private static final Logger logger = LoggerFactory.getLogger(LogoutCustomHandler.class);
	
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
	    String string=""+ session.getAttribute("usuario");
    	String[] parts = string.split(": ");
    	String claveUsuario = parts[1];
    	Periodo periodo= new Periodo();
    	String cadena=catalogoService.obtieneDiaFestivoParaBloquear(authentication);
	    model.addAttribute("listaDiasFestivos", cadena);
    	periodo=periodoService.buscaPeriodoPorClaveUsuario(claveUsuario, authentication);
    	String fechas=vacacionesService.recuperaDiasVacacioness(claveUsuario, authentication);
    	if(!fechas.isEmpty() && fechas!=null){
    		if(!cadena.isEmpty() && cadena!=null){
    			cadena+=","+fechas;
    		}else{
    			cadena=fechas;
    		}
    	}
    	periodo.setMensaje(cadena);
	   
	    if(periodo.getIdPeriodo()!=null && !periodo.getIdPeriodo().toString().isEmpty()){
	    	 model.addAttribute("periodo",periodo);
	    	 VacacionPeriodo vaca= new VacacionPeriodo(); 
	    	 vaca=vacacionesService.buscaVacacionPeriodoPorClaveUsuarioYPeriodo(claveUsuario,periodo.getIdPeriodo(), authentication);
	    	 try{
		    	 if(vaca.getDias()>0 && vaca.getDias()!=null){
		    	 model.addAttribute("vacacion",vaca);
		    	 }else{
		    		 model.addAttribute("vacacion",null);
		    	 }
	    	 }
	    	 catch(Exception e){
	    		 
	    	 }
	    }else{
	    	model.addAttribute("periodo",null);
	    	 model.addAttribute("vacacion",null);
	    }
	    logger.info("idPeriodo "+periodo.getIdPeriodo());
	    
	    model.addAttribute("listaResponsable",unidadAdministrativaService.consultaResponsable(claveUsuario, authentication));
	    model.addAttribute("listaUnidades",unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
	    model.addAttribute("listaEstatus",estatusService.obtieneListaEstatus(authentication));
	    model.addAttribute("usuario",usuarioService.buscaUsuario(claveUsuario,authentication));
	    
	    if(!this.getMensaje().equals("")){
			if(this.mensaje.contains("correctamente")){
				model.addAttribute("MENSAJE", this.mensaje);
			}else if(this.mensaje.contains("10")){
				model.addAttribute("MENSAJE_ALERTA", this.mensaje);
			}else{
				model.addAttribute("MENSAJE_EXCEPCION", this.mensaje);
			}
			
		}
		this.mensaje = "";
    	return "/vacaciones/solicitudVacaciones";
    }
    @GetMapping("vacacion/busca")
	@ResponseBody
	public HashMap<String, Object> buscaUsuario(String idVacacion, Authentication authentication) {
		HashMap<String, Object> hmap = new HashMap<String, Object>();
		logger.info("vamos bien "+idVacacion);
		VacacionesAux vacaciones = new VacacionesAux();
		vacaciones=vacacionesService.obtieneVacacion(idVacacion, authentication);
		hmap.put("vacacion",vacaciones);
		if(vacaciones.getIdResponsable()!=null && !vacaciones.getIdResponsable().toString().isEmpty()){
			hmap.put("responsable", usuarioService.buscaUsuarioPorId(Integer.toString(vacaciones.getIdResponsable()),authentication));
		}else{
			hmap.put("responsable", null);
		}
		return hmap;
	}
    
    @RequestMapping(value= "vacacion/generaArchivo",method = RequestMethod.GET)
	public void  generaReporteVacaciones(String idSolicitud,String idEstatus,String idPuesto,String idUnidadAdministrativa,String numeroEmpleado,
			String fechaIngreso,String rfc,String nombre,String apellidoPaterno,String apellidoMaterno, String fechaInicio1, String fechaFin1, 
			String dias, String responsable, String idVacacion, String responsableAux,String fechaSolicitud ,HttpServletRequest request, HttpServletResponse response, Authentication authentication){
    	logger.info("Datos para el archivo "+idSolicitud+" unidadAdministrativa "+idUnidadAdministrativa+" idPuesto "+idPuesto +" idUnidad "+idVacacion
    				+" fechaInicio "+fechaInicio1+" fechaFin "+fechaFin1+" responsableAux "+responsableAux +" responsable "+responsable);
    	try {
    		logger.info("Datos "+nombre);
			Map<String,Object> parametros = new HashMap<String, Object>();
			parametros.put("nombre", nombre);
			parametros.put("apellidoPaterno", apellidoPaterno);
			parametros.put("apellidoMaterno", apellidoMaterno);
			parametros.put("rfc", rfc);
			parametros.put("idSolicitud", idSolicitud);
			parametros.put("idEstatus", idEstatus);
			parametros.put("idPuesto", idPuesto);
			parametros.put("unidadAdministrativa", idUnidadAdministrativa);
			
			parametros.put("numeroEmpleado", numeroEmpleado);
			parametros.put("fechaIngreso", fechaIngreso);
			parametros.put("fechaInicio", fechaInicio1);
			parametros.put("fechaFin", fechaFin1);
			parametros.put("responsable", responsable);
			parametros.put("numeroEmpleado", numeroEmpleado);
			parametros.put("responsable", responsable);
			parametros.put("numeroEmpleado", numeroEmpleado);
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
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    @RequestMapping(value={"vacacionesPropias"}, method = RequestMethod.GET)
    public String obtieneAsistenciasPropias(String idPeriodo, String idEstatus, String fechaInicioBusca1, String fechaFinBusca1 ,Model model, HttpSession session, Authentication authentication) {
    	logger.info("idEstatus "+idEstatus+" idPeriodo "+idPeriodo+" fechaInicio "+fechaInicioBusca1+" fechaFin "+fechaFinBusca1 );
    	if(idEstatus==null || idEstatus.toString().isEmpty()){
    		idEstatus="";
    	}
    	if(idPeriodo==null || idPeriodo.toString().isEmpty()){
    		idPeriodo="";
    	}
    	if(fechaInicioBusca1==null || fechaInicioBusca1.toString().isEmpty()){
    		fechaInicioBusca1="";
    	}
    	if(fechaFinBusca1==null || fechaFinBusca1.toString().isEmpty()){
    		fechaFinBusca1="";
    	}
    	String string=""+ session.getAttribute("usuario");
    	String[] parts = string.split(": ");
    	String claveUsuario = parts[1];
    	logger.info("Datos para vacacionesPropias");
	    model.addAttribute("listaUnidades",unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
	    model.addAttribute("listaPeriodos",periodoService.periodos(authentication));
    	List<Vacaciones> vacaciones = new ArrayList<>();
    	BusquedaDto busquedaDto = new BusquedaDto();
    	busquedaDto.setClaveUsuario(claveUsuario);
    	busquedaDto.setIdEstatus(idEstatus);
    	busquedaDto.setIdPeriodo(idPeriodo);
    	busquedaDto.setFechaInicio(fechaInicioBusca1);
    	busquedaDto.setFechaFin(fechaFinBusca1);
    	vacaciones=vacacionesService.consultaVacacionesPropiasPorFiltros(busquedaDto, authentication);
    	if(vacaciones.size()>0){
    		model.addAttribute("vacacionesPropias",vacaciones);	
    	}else{
    	model.addAttribute("vacacionesPropias",null);
    	}
    	model.addAttribute("listaEstatus",estatusService.obtieneListaEstatus(authentication));
    	if(!this.getMensaje().equals("")){
			if(this.mensaje.contains("correctamente")){
				model.addAttribute("MENSAJE", this.mensaje);
			}else if(this.mensaje.contains("10")){
				model.addAttribute("MENSAJE_ALERTA", this.mensaje);
			}else{
				model.addAttribute("MENSAJE_EXCEPCION", this.mensaje);
			}
			
		}
		this.mensaje = "";
    	return "/vacaciones/vacacionesPropias"; 
    	
    }
    @RequestMapping(value={"solicitudVacacionesEmpleados"}, method = RequestMethod.GET)
    public String SolitudVacacionesEmpleados(String claveUsuario, String nombre, String apellidoPaterno, String apellidoMaterno, Model model,HttpSession session, Authentication authentication) {
    	logger.info("claveUsuario "+claveUsuario+" nombre "+nombre+" apellidoPaterno "+apellidoPaterno+" apellidoMaterno "+apellidoMaterno);
    	String string=""+ session.getAttribute("usuario");
    	String[] parts = string.split(": ");
    	String claveUsuarioAux = parts[1];
    	logger.info("Datos para vacacionesPropias");
    	Usuario usuario= new Usuario();
    	usuario=usuarioService.buscaUsuario(claveUsuarioAux, authentication);
    	
    	String idUnidad=""+usuario.getIdUnidad();
    	logger.info("id de la unidad administrativa "+idUnidad);
    	List<VacacionPeriodo> conVacaciones= new ArrayList<>();
    	conVacaciones=vacacionPeriodoService.obtenerUsuariosVacacionesPorFiltros(claveUsuario, nombre, apellidoPaterno, apellidoMaterno, idUnidad, authentication);
    	if(conVacaciones.size()>0){
    	model.addAttribute("usuariosConVacaciones",conVacaciones);
    	}else{
    		model.addAttribute("usuariosConVacaciones",null);
    	}
    	if(!this.getMensaje().equals("")){
			if(this.mensaje.contains("correctamente")){
				model.addAttribute("MENSAJE", this.mensaje);
			}else if(this.mensaje.contains("10")){
				model.addAttribute("MENSAJE_ALERTA", this.mensaje);
			}else{
				model.addAttribute("MENSAJE_EXCEPCION", this.mensaje);
			}
			
		}
		this.mensaje = "";
    	return "/vacaciones/solicitudVacacionesEmpleados"; 
    	
    }
    @RequestMapping(value={"vacacionesEmpleados"}, method = RequestMethod.GET)
    public String obtieneVacacionesEmpleados(String claveUsuario, String nombre, String apellidoPaterno, String apellidoMaterno, String idUnidad, String idEstatus, Model model,HttpSession session, Authentication authentication) {
    	logger.info("Datos para vacacionesEmpleados claveUsuario "+claveUsuario+" nombre "+nombre+" apellidoPaterno "+apellidoPaterno+" apellidoMaterno "+apellidoMaterno+" idUnidad "+idUnidad+" idEstatus "+idEstatus );
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
    	String string=""+ session.getAttribute("usuario");
    	String[] parts = string.split(": ");
    	String claveUsuarioLider = parts[1];
    	List<PerfilUsuario> listaPerfilUsuario= new ArrayList<>();
    	listaPerfilUsuario=perfilUsuarioService.recuperaPerfilesUsuario(claveUsuarioLider, authentication);
    	Boolean usuario= false;
    	for(PerfilUsuario perfilUsuario: listaPerfilUsuario){
    		if(idUnidad==null || idUnidad.toString().isEmpty()){
    			logger.info("Entrando al if "+perfilUsuario.getClavePerfil().getClavePerfil());
	    		if(perfilUsuario.getClavePerfil().getClavePerfil().toString().equals("2")){
	    			logger.info("Entrando al if "+perfilUsuario.getClavePerfil().getClavePerfil());
	    			usuario=true;
	    			Usuario usuarioAux= new Usuario();
	    	    	if(usuario==true){
	    	    		
	    	    		usuarioAux=usuarioService.buscaUsuario(claveUsuarioLider, authentication);
	    	    		idUnidad=""+usuarioAux.getIdUnidad();
	    	    	}
	    		}
	    		if(perfilUsuario.getClavePerfil().getClavePerfil().toString().equals("1")){
	    			idUnidad="";
	    		}
    		}
    		logger.info("valor para idUnidad "+idUnidad);
    	}
    	logger.info("Bandera para determinar si es empleado o no "+usuario+" claveUsuario "+claveUsuario);
    	
	    model.addAttribute("listaUnidades",unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
	    List<Vacaciones> vacaciones = new ArrayList<>();
	    BusquedaDto busquedaDto = new BusquedaDto();
    	busquedaDto.setClaveUsuario(claveUsuario);
    	busquedaDto.setNombre(nombre);
    	busquedaDto.setApellidoPaterno(apellidoPaterno);
    	busquedaDto.setApellidoMaterno(apellidoMaterno);
    	busquedaDto.setIdUnidad(idUnidad);
    	busquedaDto.setIdEstatus(idEstatus);
	    vacaciones=vacacionesService.obtenerVacacionesPorFiltros(busquedaDto, authentication);
	    logger.info("Tamaño "+vacaciones.size());
	    if(vacaciones.size()>0){
	    	model.addAttribute("vacacionesEmpleados",vacaciones);
	    }else{
	    	model.addAttribute("vacacionesEmpleados",null);
	    }
	    
    	model.addAttribute("listaEstatus",estatusService.obtieneListaEstatus(authentication));
    	if(!this.getMensaje().equals("")){
			if(this.mensaje.contains("correctamente")){
				model.addAttribute("MENSAJE", this.mensaje);
			}else if(this.mensaje.contains("10")){
				model.addAttribute("MENSAJE_ALERTA", this.mensaje);
			}else{
				model.addAttribute("MENSAJE_EXCEPCION", this.mensaje);
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
    	logger.info("Usuarioooooooooooooo para buscar "+claveUsuario);
	    global=claveUsuario;
    	Periodo periodo= new Periodo();
    	logger.info("periodo.getIdPeriodo() "+claveUsuario);
    	periodo=periodoService.buscaPeriodoPorClaveUsuario(claveUsuario, authentication);
    	String cadena=catalogoService.obtieneDiaFestivoParaBloquear(authentication);
    	logger.info("Dias festivos para bloquear "+cadena);
	    model.addAttribute("listaDiasFestivos", cadena);
    	periodo=periodoService.buscaPeriodoPorClaveUsuario(claveUsuario, authentication);
    	String fechas=vacacionesService.recuperaDiasVacacioness(claveUsuario, authentication);
    	if(!fechas.isEmpty() && fechas!=null){
    		if(!cadena.isEmpty() && cadena!=null){
    			cadena+=","+fechas;
    		}else{
    			cadena=fechas;
    		}
    	}
    	periodo.setMensaje(cadena);
    	model.addAttribute("usuario",usuarioService.buscaUsuario(claveUsuario, authentication));
    	logger.info("periodo.getIdPeriodo() "+periodo.getIdPeriodo());
	    if(periodo.getIdPeriodo()!=null && !periodo.getIdPeriodo().toString().isEmpty()){
	    	 model.addAttribute("periodo",periodo);
	    	 logger.info("Datos claveUsuario "+claveUsuario+"periodo.getIdPeriodo() "+periodo.getIdPeriodo());
	    	 VacacionPeriodo vaca= new VacacionPeriodo(); 
	    	 vaca=vacacionesService.buscaVacacionPeriodoPorClaveUsuarioYPeriodo(claveUsuario,periodo.getIdPeriodo(), authentication);
	    	 try{
		    	 if(vaca.getDias()>0 && vaca.getDias()!=null){
		    	 model.addAttribute("vacacion",vaca);
		    	 }else{
		    		 model.addAttribute("vacacion",null);
		    	 }
	    	 }
	    	 catch(Exception e){
	    		 
	    	 }
	    }else{
	    	model.addAttribute("periodo",null);
	    	 model.addAttribute("vacacion",null);
	    }
	    logger.info("idPeriodo "+periodo.getIdPeriodo());
	    model.addAttribute("listaUnidades",unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
	    model.addAttribute("listaEstatus",estatusService.obtieneListaEstatus(authentication));
    	return "/vacaciones/registraVacacionesEmpleados";
    	
    }
    
    @PostMapping("/vacacion/modifica")
    public String registraVacacioness(@RequestParam String fechaInicio, @RequestParam String fechaFin, @RequestParam Integer diasPorPedir, @RequestParam Integer idPeriodo, @RequestParam String idVacacion ,Integer responsable, HttpSession session, Authentication authentication ) {
    	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    	logger.info("fechaInicio "+fechaInicio+" fechaFin "+fechaFin+" diasPorPedir "+diasPorPedir+" idPeriodo "+idPeriodo+" responsable "+responsable+" idVacacion "+idVacacion);
    	logger.info("responsable "+responsable);
    	Date fechaInicial = new Date();
    	Date fechaFinal = new Date();
    	
    	
    	try {
    		fechaInicial = df.parse(fechaInicio);
    		fechaFinal=df.parse(fechaFin);
    		logger.info("fechaInicio "+fechaInicial);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	 String string=""+ session.getAttribute("usuario");
     	String[] parts = string.split(": ");
     	String claveUsuario = parts[1];
     	VacacionPeriodo vacacion=new  VacacionPeriodo();
     	vacacion.setIdVacacion(Integer.parseInt(idVacacion));
     	Estatus estatus=new Estatus();
     	estatus.setIdEstatus(1);
     	VacacionPeriodo vacacion1= new VacacionPeriodo();
     	vacacion1.setIdVacacion(Integer.parseInt(idVacacion));
   
     	Vacaciones respuesta=vacacionesService.agregaVacaciones(new VacacionesAux(null,vacacion1, responsable, estatus, fechaInicio, fechaFin, diasPorPedir), claveUsuario,authentication);
     	logger.info("fechaInicio "+fechaInicial+" fechaFin "+fechaFinal+" diasPorPedir "+diasPorPedir+" idPeriodo "+idPeriodo+" claveUsuario "+claveUsuario);
     	logger.info("respuesta con mensaje "+respuesta.getMensaje());
    	
    	this.mensaje=respuesta.getMensaje();
    	
    	return "redirect:/vacaciones/vacacionesPropias";
    }
    @PostMapping("/vacacion/guardaVacacion")
    public String registraVacacionesEmpleado( String fechaInicio,  String fechaFin,  Integer diasPorPedir,  Integer idPeriodo,  String idVacacion,Integer responsable,  String claveUsuario, HttpSession session, Authentication authentication) {
    	DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
    	claveUsuario=global;
    	logger.info("fechaInicio "+fechaInicio+" fechaFin "+fechaFin+" diasPorPedir "+diasPorPedir+" idPeriodo "+idPeriodo+" claveUsuario "+claveUsuario);
    	logger.info("responsable "+responsable);
    	Date fechaInicial = new Date();
    	Date fechaFinal = new Date();
    	try {
    		fechaInicial = df.parse(fechaInicio);
    		fechaFinal=df.parse(fechaFin);
    		logger.info("fechaInicio "+fechaInicial);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//    	 String string=""+ session.getAttribute("usuario");
//     	String[] parts = string.split(": ");
//     	String claveUsuario = parts[1];
     	VacacionPeriodo vacacion=new  VacacionPeriodo();
     	Archivo archivoDto = new Archivo();
     	vacacion.setIdVacacion(Integer.parseInt(idVacacion));
     	Estatus estatus=new Estatus();
     	estatus.setIdEstatus(1);
     	Vacaciones vacaciones= vacacionesService.agregaVacaciones(new VacacionesAux(null,vacacion, responsable, estatus, fechaInicio, fechaFin, diasPorPedir), claveUsuario, authentication);
     	logger.info("Mensaje obtenido "+vacaciones.getMensaje());
     	this.mensaje=vacaciones.getMensaje();
     	logger.info("fechaInicio "+fechaInicial+" fechaFin "+fechaFinal+" diasPorPedir "+diasPorPedir+" idPeriodo "+idPeriodo+" claveUsuario "+claveUsuario);
     	global="";
     	return "redirect:/vacaciones/solicitudVacacionesEmpleados";
    }
    @GetMapping("vacacion/acepta")
    public String aceptaVacaciones(Integer idSolicitud, String fechaInicio, String fechaFin,Integer id, Authentication authentication) {
    	logger.info("idVacacion "+idSolicitud+" fechaInicio "+fechaInicio+" fechaFinal "+fechaFin+" idUsuario "+id);
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
		logger.info("fechaInicio "+fechaInicial);
		Vacaciones vacaciones=vacacionesService.aceptaORechazaVacaciones(new Vacaciones(usuario,null,null,null,estatus,fechaInicial,fechaFinal,null), idSolicitud, authentication);
    	//catalogoService.eliminaHorario(id);
		this.mensaje=vacaciones.getMensaje();
    	
    	return "redirect:/vacaciones/vacacionesEmpleados";
    }
    
    @GetMapping("vacacion/actualizaPropiasVacaciones")
    public String actualizaPropiasVacaciones(Integer id,String idUsuario,Integer idVacacion,Integer dias, Authentication authentication) {
    	logger.info("idVacacion "+id+" idUsuario "+idUsuario+" idVacacion "+idVacacion+" dias"+dias);
    	VacacionPeriodo vacacion=new  VacacionPeriodo();
    	vacacion.setIdVacacion(idVacacion);
    	Usuario usuario=new Usuario();
    	usuario.setIdUsuario(Integer.parseInt(idUsuario));
    	Estatus estatus= new Estatus();
        estatus.setIdEstatus(3);
    	Vacaciones vacaciones=vacacionesService.aceptaORechazaVacaciones(new Vacaciones(usuario,vacacion,null,null,estatus,null,null,dias), id, authentication);
    	this.mensaje=vacaciones.getMensaje();
    	return "redirect:/vacaciones/vacacionesPropias";
    
    }
    @GetMapping("vacacion/actualizaVacacionesEmpleados")
    public String actualizaVacacionesEmpleados(Integer id,String idUsuario,Integer idVacacion,Integer dias, Authentication authentication) {
    	logger.info("idVacacion "+id+" idUsuario "+idUsuario+" idVacacion "+idVacacion+" dias"+dias);
    	VacacionPeriodo vacacion=new  VacacionPeriodo();
    	vacacion.setIdVacacion(idVacacion);
    	Usuario usuario=new Usuario();
    	usuario.setIdUsuario(Integer.parseInt(idUsuario));
    	Estatus estatus= new Estatus();
        estatus.setIdEstatus(3);
    	Vacaciones vacaciones=vacacionesService.aceptaORechazaVacaciones(new Vacaciones(usuario,vacacion,null,null,estatus,null,null,dias), id, authentication);
    	this.mensaje=vacaciones.getMensaje();
    	return "redirect:/vacaciones/vacacionesEmpleados";
    
    }
    
    @PostMapping("/vacacion/actualizaArchivo")
    public String registraVacaciones(@RequestParam MultipartFile archivo, Integer idArchivo,String claveUsuario,Integer idDetalle, Authentication authentication ){
    	logger.info("Datos archivo "+archivo+" idArchivo "+idArchivo+" claveUsuario "+claveUsuario);
    	Archivo idArchivoAux=new Archivo();
    	Vacaciones vacaciones= new Vacaciones();
    	Archivo archivoDto=new Archivo();
    	vacaciones.setIdDetalle(idDetalle);
    	if(idArchivo!=null && !idArchivo.toString().isEmpty()){
    		idArchivoAux=archivoService.actualizaArchivo(archivo, claveUsuario, new String("vacaciones"),idArchivo,"vacacion-", authentication);
    		archivoDto.setIdArchivo(idArchivo);
    		vacaciones.setIdArchivo(archivoDto);
    		vacacionesService.modificaVacaciones(vacaciones, authentication);
    	}else{
    		idArchivoAux=archivoService.guardaArchivo(archivo, claveUsuario, "vacaciones","vacacion-", authentication);
    		archivoDto.setIdArchivo(idArchivoAux.getIdArchivo());
    		vacaciones.setIdArchivo(archivoDto);
    		vacacionesService.modificaVacaciones(vacaciones, authentication);
    	}
    	this.mensaje=idArchivoAux.getMensaje();
    	return"redirect:/vacaciones/vacacionesPropias";
    	}
    @PostMapping("/vacacion/actualizaArchivoEmpleado")
    public String registraVacacionesEmpleado(@RequestParam MultipartFile archivo, Integer idArchivo,String claveUsuario,Integer idDetalle, Authentication authentication ){
    	logger.info("Datos archivo "+archivo+" idArchivo "+idArchivo+" claveUsuario "+claveUsuario+" idDetalle "+idDetalle);
    	Archivo idArchivoAux=new Archivo();
    	Vacaciones vacaciones= new Vacaciones();
    	Archivo archivoDto=new Archivo();
    	vacaciones.setIdDetalle(idDetalle);
    	if(idArchivo!=null && !idArchivo.toString().isEmpty()){
    		logger.info("id de archivo actualizar "+idArchivo);
    		idArchivoAux=archivoService.actualizaArchivo(archivo, claveUsuario, new String("vacaciones"),idArchivo,"vacacion-", authentication);
    		archivoDto.setIdArchivo(idArchivo);
    		vacaciones.setIdArchivo(archivoDto);
    		vacacionesService.modificaVacaciones(vacaciones, authentication);
    	}else{
    		idArchivoAux=archivoService.guardaArchivo(archivo, claveUsuario, "vacaciones","vacacion-", authentication);
    		logger.info("id de archivo guardar "+idArchivoAux.getIdArchivo());
    		archivoDto.setIdArchivo(idArchivoAux.getIdArchivo());
    		vacaciones.setIdArchivo(archivoDto);
    		vacacionesService.modificaVacaciones(vacaciones, authentication);
    	}
    	this.mensaje=idArchivoAux.getMensaje();
    	return"redirect:/vacaciones/vacacionesEmpleados";
    	}
    
    @GetMapping("vacacion/rechaza")
    public String rechazaVacaciones(Integer idSolicitud,String idUsuario,Integer idVacacion,Integer dias, Authentication authentication) {
    	logger.info("idVacacion "+idSolicitud+" idUsuario "+idUsuario+" idVacacion "+idVacacion+" dias"+dias);
    	VacacionPeriodo vacacion=new  VacacionPeriodo();
    	vacacion.setIdVacacion(idVacacion);
    	Usuario usuario=new Usuario();
    	usuario.setIdUsuario(Integer.parseInt(idUsuario));
    	Estatus estatus= new Estatus();
        estatus.setIdEstatus(3);
        Vacaciones vacaciones= new Vacaciones();
        vacaciones=vacacionesService.aceptaORechazaVacaciones(new Vacaciones(usuario,vacacion,null,null,estatus,null,null,dias), idSolicitud, authentication);
    	this.mensaje=vacaciones.getMensaje();
    	return "redirect:/vacaciones/vacacionesEmpleados";
    }
    
    @RequestMapping(value = "/descargaArchivo", method = RequestMethod.GET)
    public void getFile(Integer idArchivo, HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
    	logger.info("id del archivo "+idArchivo);
    	Archivo archivo= new Archivo();
    	archivo=archivoService.consultaArchivo(idArchivo, authentication);
    	logger.info("archivo retornado "+archivo.getUrl());
    	String nombrecompleto=archivo.getUrl()+archivo.getNombre();
    	//String nombreArchivo=nombrecompleto.replace('/','\\');
    	String nombreArchivo=nombrecompleto;
    	logger.info("nombre de archivo "+nombreArchivo);
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
    
    @RequestMapping(value={"eliminaVacacion"}, method = RequestMethod.POST)
    public String eliminaVacacion(Integer idVacacionEliminar, Authentication authentication) {
    	logger.info("id para eliminar "+idVacacionEliminar);
	    Vacaciones vacaciones= new Vacaciones();
	    vacaciones=vacacionesService.eliminaVacaciones(idVacacionEliminar, authentication);
	    this.mensaje=vacaciones.getMensaje();
	    return "redirect:/vacaciones/vacacionesPropias";
	    
    }
    
    @RequestMapping(value={"cancelaVacacion"}, method = RequestMethod.POST)
    public String cancelaVacacion(Integer idVacacionCancelar, Authentication authentication) {
	    logger.info("id para cancelar "+idVacacionCancelar);
	    Vacaciones vacaciones= new Vacaciones();
	    vacaciones=vacacionesService.cancelaVacaciones(idVacacionCancelar, authentication);
	    this.mensaje=vacaciones.getMensaje();
	    return "redirect:/vacaciones/vacacionesEmpleados";
	    
    }
    
	public String getMensaje() {
		return mensaje == null ? "" : this.mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
    
    
}
