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
import org.springframework.beans.factory.annotation.Autowired;
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

import mx.gob.segob.dgtic.web.mvc.dto.Archivo;
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
	
    @RequestMapping(value={"solicitudVacaciones"}, method = RequestMethod.GET)
    public String obtieneAsistencias(Model model, HttpSession session) {
    	System.out.print("Peticion de vacaciones");
	    
	    //model.addAttribute("listaVacaciones", vacacionesService.obtieneVacaciones());
	    String string=""+ session.getAttribute("usuario");
    	String[] parts = string.split(": ");
    	String claveUsuario = parts[1];
    	
    	Periodo periodo= new Periodo();
    	System.out.println("periodo.getIdPeriodo() "+claveUsuario);
    	String cadena=catalogoService.obtieneDiaFestivoParaBloquear();
	    ;
	    model.addAttribute("listaDiasFestivos", cadena);
    	periodo=periodoService.buscaPeriodoPorClaveUsuario(claveUsuario);
    	String fechas=vacacionesService.recuperaDiasVacacioness(claveUsuario);
    	if(!fechas.isEmpty() && fechas!=null){
    		if(!cadena.isEmpty() && cadena!=null){
    			cadena+=","+fechas;
    		}else{
    			cadena=fechas;
    		}
    	}
    	System.out.println("Dias festivos para bloquear "+cadena);
    	periodo.setMensaje(cadena);
    	System.out.println("periodo.getIdPeriodo() "+periodo.getIdPeriodo());
	   
	    if(periodo.getIdPeriodo()!=null && !periodo.getIdPeriodo().toString().isEmpty()){
	    	 model.addAttribute("periodo",periodo);
	    	 model.addAttribute("vacacion",vacacionesService.buscaVacacionPeriodoPorClaveUsuarioYPeriodo(claveUsuario,periodo.getIdPeriodo()));
	    }else{
	    	model.addAttribute("periodo",null);
	    	 model.addAttribute("vacacion",null);
	    }
	    System.out.println("idPeriodo "+periodo.getIdPeriodo());
	    
	    model.addAttribute("listaResponsable",unidadAdministrativaService.consultaResponsable(claveUsuario));
	    model.addAttribute("listaUnidades",unidadAdministrativaService.obtenerUnidadesAdministrativas());
	    model.addAttribute("listaEstatus",estatusService.obtieneListaEstatus());
	    
	    if(!this.mensaje.equals("")){
			if(this.mensaje.contains("correctamente"))
				model.addAttribute("MENSAJE", this.mensaje);
			else
				model.addAttribute("MENSAJE_EXCEPCION", this.mensaje);
		}
		this.mensaje = "";
    	//Periodo periodo=periodoService.buscaPeriodoPorClaveUsuario(claveUsuario);
    	//System.out.println("datos del periodo "+periodo.getFechaInicio());
    	return "/vacaciones/solicitudVacaciones";
    }
    @GetMapping("vacacion/busca")
	@ResponseBody
	public HashMap<String, Object> buscaUsuario(String idVacacion) {
		//cargaUsuarioPerfil(id, model);
		HashMap<String, Object> hmap = new HashMap<String, Object>();
		System.out.println("vamos bien "+idVacacion);
		Vacaciones vacaciones = new Vacaciones();
		vacaciones=vacacionesService.obtieneVacacion(idVacacion);
		hmap.put("vacacion",vacaciones);
		//String claveResponsable=Integer.toString(vacaciones.getIdResponsable());
		//System.out.println("claveResponsable "+claveResponsable);
		if(vacaciones.getIdResponsable()!=null && !vacaciones.getIdResponsable().toString().isEmpty()){
			hmap.put("responsable", usuarioService.buscaUsuarioPorId(Integer.toString(vacaciones.getIdResponsable())));
		}else{
			hmap.put("responsable", null);
		}
		//hmap.put("listaUsuarioPerfiles", perfilUsuarioService.recuperaPerfilesUsuario(id));
		//hmap.put("listaPerfiles", catalogoService.obtienePerfiles());
		return hmap;
	}
    
    @RequestMapping(value= "vacacion/generaArchivo",method = RequestMethod.GET)
	public void  generaReporteVacaciones(String idSolicitud,String idEstatus,String idPuesto,String idUnidadAdministrativa,String numeroEmpleado,
			String fechaIngreso,String rfc,String nombre,String apellidoPaterno,String apellidoMaterno, String fechaInicio1, String fechaFin1, 
			String dias, String responsable, String idVacacion, String responsableAux,HttpServletRequest request, HttpServletResponse response){
    		System.out.println("Datos para el archivo "+idSolicitud+" unidadAdministrativa "+idUnidadAdministrativa+" idPuesto "+idPuesto +" idUnidad "+idVacacion
    				+" fechaInicio "+fechaInicio1+" fechaFin "+fechaFin1+" responsableAux "+responsableAux +" responsable "+responsable);
    	try {
			System.out.println("Datos "+nombre);
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
			
			reporte archivo = vacacionesService.generaReporte(new GeneraReporteArchivo (idSolicitud, idEstatus, idPuesto, idUnidadAdministrativa, numeroEmpleado, fechaIngreso, rfc, nombre, apellidoPaterno, apellidoMaterno, fechaInicio1, fechaFin1, dias, responsable, idVacacion));
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
    public String obtieneAsistenciasPropias(String idPeriodo, String idEstatus, String fechaInicioBusca1, String fechaFinBusca1 ,Model model, HttpSession session) {
    	System.out.println("idEstatus "+idEstatus+" idPeriodo "+idPeriodo+" fechaInicio "+fechaInicioBusca1+" fechaFin "+fechaFinBusca1);
    	if(idEstatus==null){
    		idEstatus="";
    	}
    	if(idPeriodo==null){
    		idPeriodo="";
    	}
    	String string=""+ session.getAttribute("usuario");
    	String[] parts = string.split(": ");
    	String claveUsuario = parts[1];
    	System.out.println("Datos para vacacionesPropias");
//    	Periodo periodo= new Periodo();
//    	periodo=periodoService.buscaPeriodoPorClaveUsuario(claveUsuario);
//	    model.addAttribute("periodo",periodo);
//	    System.out.println("idPeriodo "+periodo.getIdPeriodo());
	    model.addAttribute("listaUnidades",unidadAdministrativaService.obtenerUnidadesAdministrativas());
	    model.addAttribute("listaPeriodos",periodoService.periodos());
	   // model.addAttribute("vacacion",vacacionesService.buscaVacacionPeriodoPorClaveUsuarioYPeriodo(claveUsuario,periodo.getIdPeriodo()));
    	List<Vacaciones> vacaciones = new ArrayList<>();
    	vacaciones=vacacionesService.consultaVacacionesPropiasPorFiltros(claveUsuario, idPeriodo, idEstatus, fechaInicioBusca1, fechaFinBusca1);
    	if(vacaciones.size()>0){
    		model.addAttribute("vacacionesPropias",vacaciones);	
    	}else{
    	model.addAttribute("vacacionesPropias",null);
    	}
    	model.addAttribute("listaEstatus",estatusService.obtieneListaEstatus());
    	if(!this.getMensaje().equals("")){
			if(this.mensaje.contains("correctamente"))
				model.addAttribute("MENSAJE", this.mensaje);
			else
				model.addAttribute("MENSAJE_EXCEPCION", this.mensaje);
		}
		this.mensaje = "";
    	return "/vacaciones/vacacionesPropias"; 
    	
    }
    @RequestMapping(value={"solicitudVacacionesEmpleados"}, method = RequestMethod.GET)
    public String SolitudVacacionesEmpleados(String claveUsuario, String nombre, String apellidoPaterno, String apellidoMaterno, Model model,HttpSession session) {
    	System.out.println("claveUsuario "+claveUsuario+" nombre "+nombre+" apellidoPaterno "+apellidoPaterno+" apellidoMaterno "+apellidoMaterno);
    	String string=""+ session.getAttribute("usuario");
    	String[] parts = string.split(": ");
    	String claveUsuarioAux = parts[1];
    	System.out.println("Datos para vacacionesPropias");
    	Usuario usuario= new Usuario();
    	usuario=usuarioService.buscaUsuario(claveUsuarioAux);
    	String idUnidad=""+usuario.getIdUnidad();
    	List<VacacionPeriodo> conVacaciones= new ArrayList<>();
    	conVacaciones=vacacionPeriodoService.obtenerUsuariosVacacionesPorFiltros(claveUsuario, nombre, apellidoPaterno, apellidoMaterno, idUnidad);
    	if(conVacaciones.size()>0){
    	model.addAttribute("usuariosConVacaciones",conVacaciones);
    	}else{
    		model.addAttribute("usuariosConVacaciones",null);
    	}
    	if(!this.getMensaje().equals("")){
			if(this.mensaje.contains("correctamente"))
				model.addAttribute("MENSAJE", this.mensaje);
			else
				model.addAttribute("MENSAJE_EXCEPCION", this.mensaje);
		}
		this.mensaje = "";
    	//model.addAttribute("listaEstatus",estatusService.obtieneListaEstatus());
    	return "/vacaciones/solicitudVacacionesEmpleados"; 
    	
    }
    @RequestMapping(value={"vacacionesEmpleados"}, method = RequestMethod.GET)
    public String obtieneVacacionesEmpleados(String claveUsuario, String nombre, String apellidoPaterno, String apellidoMaterno, String idUnidad, String idEstatus, Model model,HttpSession session) {
    	System.out.println("Datos para vacacionesEmpleados claveUsuario "+claveUsuario+" nombre "+nombre+" apellidoPaterno "+apellidoPaterno+" apellidoMaterno "+apellidoMaterno+" idUnidad "+idUnidad+" idEstatus "+idEstatus );
    	if(idEstatus==null){
    		idEstatus="";
    		System.out.println("idestatus en controller "+idEstatus);
    	}
    	String string=""+ session.getAttribute("usuario");
    	String[] parts = string.split(": ");
    	String claveUsuarioLider = parts[1];
    	List<PerfilUsuario> listaPerfilUsuario= new ArrayList<>();
    	listaPerfilUsuario=perfilUsuarioService.recuperaPerfilesUsuario(claveUsuarioLider);
    	Boolean usuario= false;
    	for(PerfilUsuario perfilUsuario: listaPerfilUsuario){
    		if(perfilUsuario.getClavePerfil().equals('2')){
    			usuario=true;
    		}
    	}
    	System.out.println("Bandera para determinar si es empleado o no "+usuario+" claveUsuario "+claveUsuario);
    	Usuario usuarioAux= new Usuario();
    	if(usuario==false){
    		usuarioAux=usuarioService.buscaUsuario(claveUsuarioLider);
    		idUnidad=""+usuarioAux.getIdUnidad();
    	}else if(idUnidad==null){
    			idUnidad="";
    	}
	    model.addAttribute("listaUnidades",unidadAdministrativaService.obtenerUnidadesAdministrativas());
	    List<Vacaciones> vacaciones = new ArrayList<>();
	    vacaciones=vacacionesService.obtenerVacacionesPorFiltros(claveUsuario, nombre, apellidoPaterno, apellidoMaterno, idUnidad, idEstatus);
	    System.out.println("Tamaño "+vacaciones.size());
	    if(vacaciones.size()>0){
	    	model.addAttribute("vacacionesEmpleados",vacaciones);
	    }else{
	    	model.addAttribute("vacacionesEmpleados",null);
	    }
	    
    	model.addAttribute("listaEstatus",estatusService.obtieneListaEstatus());
    	if(!this.getMensaje().equals("")){
			if(this.mensaje.contains("correctamente"))
				model.addAttribute("MENSAJE", this.mensaje);
			else
				model.addAttribute("MENSAJE_EXCEPCION", this.mensaje);
		}
		this.mensaje = "";
    	return "/vacaciones/vacacionesEmpleados"; 
    }
    
    @RequestMapping(value={"busca"}, method = RequestMethod.GET)
    public String buscaAsistencia(Model model) {
	    
	    model.addAttribute("listaVacaciones", vacacionesService.obtieneVacaciones());
	    
    	return "/vacaciones/busca";
    }
    
    @RequestMapping(value={"vacacion/buscaUsuario"}, method = RequestMethod.GET)
    public String buscaUsuario(String claveUsuario, Model model) {
	    System.out.println("Usuarioooooooooooooo "+claveUsuario);
    	Periodo periodo= new Periodo();
    	System.out.println("periodo.getIdPeriodo() "+claveUsuario);
    	periodo=periodoService.buscaPeriodoPorClaveUsuario(claveUsuario);
    	String cadena=catalogoService.obtieneDiaFestivoParaBloquear();
	    System.out.println("Dias festivos para bloquear "+cadena);
	    model.addAttribute("listaDiasFestivos", cadena);
    	periodo=periodoService.buscaPeriodoPorClaveUsuario(claveUsuario);
    	String fechas=vacacionesService.recuperaDiasVacacioness(claveUsuario);
    	if(!fechas.isEmpty() && fechas!=null){
    		if(!cadena.isEmpty() && cadena!=null){
    			cadena+=","+fechas;
    		}else{
    			cadena=fechas;
    		}
    	}
    	periodo.setMensaje(cadena);
    	System.out.println("periodo.getIdPeriodo() "+periodo.getIdPeriodo());
    	VacacionPeriodo vacacionPeriodo=new VacacionPeriodo();
	    if(periodo.getIdPeriodo()!=null && !periodo.getIdPeriodo().toString().isEmpty()){
	    	 model.addAttribute("periodo",periodo);
	    	 System.out.println("Datos claveUsuario "+claveUsuario+"periodo.getIdPeriodo() "+periodo.getIdPeriodo());
	    	 vacacionPeriodo=vacacionesService.buscaVacacionPeriodoPorClaveUsuarioYPeriodo(claveUsuario,periodo.getIdPeriodo());
//	    	 System.out.println("Datos para la consulta "+claveUsuario +" idPeriodo "+periodo.getIdPeriodo()+" "
//		    	 		+ "vacacionPeriodo.getIdUsuario().getClaveUsuario() "+vacacionPeriodo.getIdUsuario().getClaveUsuario());
	    	 
	    	 model.addAttribute("vacacion",vacacionPeriodo);
	    }else{
	    	model.addAttribute("periodo",null);
	    	 model.addAttribute("vacacion",null);
	    }
	    System.out.println("idPeriodo "+periodo.getIdPeriodo());
	    String claveUsuarioAux= null;
	    try{
	    
	    claveUsuarioAux=vacacionPeriodo.getIdUsuario().getClaveUsuario();
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    //claveUsuarioAux=vacacionPeriodo.getIdUsuario().getClaveUsuario();
	    if(claveUsuarioAux!=null && !claveUsuarioAux.toString().isEmpty()){
	    	model.addAttribute("listaResponsable",unidadAdministrativaService.consultaResponsable(claveUsuarioAux));
	    }else{
	    	model.addAttribute("listaResponsable",null);
	    }
//	    String cadena=catalogoService.obtieneDiaFestivoParaBloquear();
//	    System.out.println("Dias festivos para bloquear "+cadena);
//	    model.addAttribute("listaDiasFestivos", cadena);
	    model.addAttribute("listaUnidades",unidadAdministrativaService.obtenerUnidadesAdministrativas());
	    model.addAttribute("listaEstatus",estatusService.obtieneListaEstatus());
    	//Periodo periodo=periodoService.buscaPeriodoPorClaveUsuario(claveUsuario);
    	//System.out.println("datos del periodo "+periodo.getFechaInicio());
    	return "/vacaciones/registraVacacionesEmpleados";
    	
    }
    
    @PostMapping("/vacacion/modifica")
    public String registraVacacioness(@RequestParam String fechaInicio, @RequestParam String fechaFin, @RequestParam Integer diasPorPedir, @RequestParam Integer idPeriodo, @RequestParam String idVacacion,Integer responsable, HttpSession session ) {
    	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    	System.out.println("fechaInicio "+fechaInicio+" fechaFin "+fechaFin+" diasPorPedir "+diasPorPedir+" idPeriodo "+idPeriodo+" responsable "+responsable);
    	//System.out.println("Archivo "+archivo+" idVacacion "+idVacacion);
    	System.out.println("responsable "+responsable);
    	Date fechaInicial = new Date();
    	Date fechaFinal = new Date();
    	
    	
    	try {
    		fechaInicial = df.parse(fechaInicio);
    		fechaFinal=df.parse(fechaFin);
			System.out.println("fechaInicio "+fechaInicial);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	InputStream nuevoArchivo = null;
//    	try {
//			nuevoArchivo=archivo.getInputStream();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
    	 String string=""+ session.getAttribute("usuario");
     	String[] parts = string.split(": ");
     	String claveUsuario = parts[1];
     	VacacionPeriodo vacacion=new  VacacionPeriodo();
     	Archivo archivoDto = new Archivo();
     	vacacion.setIdVacacion(Integer.parseInt(idVacacion));
     	//usuarioService.buscaUsuario(claveUsuario);
     	//Integer idArchivo=archivoService.guardaArchivo(archivo, claveUsuario, new String("vacaciones"));
     	//archivoDto.setIdArchivo(idArchivo);
     	Estatus estatus=new Estatus();
     	estatus.setIdEstatus(1);
     	Vacaciones respuesta=vacacionesService.agregaVacaciones(new VacacionesAux(null,Integer.parseInt(idVacacion), responsable, 1, fechaInicio, fechaFin, diasPorPedir), claveUsuario);
    	System.out.println("fechaInicio "+fechaInicial+" fechaFin "+fechaFinal+" diasPorPedir "+diasPorPedir+" idPeriodo "+idPeriodo+" claveUsuario "+claveUsuario);
    	System.out.println("respuesta con mensaje "+respuesta.getMensaje());
    	
    	this.mensaje=respuesta.getMensaje();
    	
    	return "redirect:/vacaciones/solicitudVacaciones";
    }
    @PostMapping("/vacacion/guardaVacacion")
    public String registraVacacionesEmpleado(@RequestParam String fechaInicio, @RequestParam String fechaFin, @RequestParam Integer diasPorPedir, @RequestParam Integer idPeriodo, @RequestParam String idVacacion,Integer responsable, HttpSession session ) {
    	DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
    	System.out.println("fechaInicio "+fechaInicio+" fechaFin "+fechaFin+" diasPorPedir "+diasPorPedir+" idPeriodo "+idPeriodo);
    	System.out.println("responsable "+responsable);
    	Date fechaInicial = new Date();
    	Date fechaFinal = new Date();
    	try {
    		fechaInicial = df.parse(fechaInicio);
    		fechaFinal=df.parse(fechaFin);
			System.out.println("fechaInicio "+fechaInicial);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 String string=""+ session.getAttribute("usuario");
     	String[] parts = string.split(": ");
     	String claveUsuario = parts[1];
     	VacacionPeriodo vacacion=new  VacacionPeriodo();
     	Archivo archivoDto = new Archivo();
     	vacacion.setIdVacacion(Integer.parseInt(idVacacion));
     	Estatus estatus=new Estatus();
     	estatus.setIdEstatus(1);
     	Vacaciones vacaciones= vacacionesService.agregaVacaciones(new VacacionesAux(null,Integer.parseInt(idVacacion), responsable, 1, fechaInicio, fechaFin, diasPorPedir), claveUsuario);
    	System.out.println("Mensaje obtenido "+vacaciones.getMensaje());
     	this.mensaje=vacaciones.getMensaje();
     	System.out.println("fechaInicio "+fechaInicial+" fechaFin "+fechaFinal+" diasPorPedir "+diasPorPedir+" idPeriodo "+idPeriodo+" claveUsuario "+claveUsuario);
    	return "redirect:/vacaciones/solicitudVacacionesEmpleados";
    }
    @GetMapping("vacacion/acepta")
    public String aceptaVacaciones(Integer idSolicitud, String fechaInicio, String fechaFin,Integer id) {
    	System.out.println("idVacacion "+idSolicitud+" fechaInicio "+fechaInicio+" fechaFinal "+fechaFin+" idUsuario "+id);
    	//fechaInicio=fechaInicio.substring(0,21);
    	ParsePosition pos = new ParsePosition(4);
    	//fechaFin=fechaFin.substring(0,12);
//    	VacacionPeriodo vacacion=  new  VacacionPeriodo();
//    	vacacion.setIdVacacion(id);
    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy, hh:mm:ss a");
    	Date fechaInicial = new Date();
    	Date fechaFinal = new Date();
    	Usuario usuario=new Usuario();
    	usuario.setIdUsuario(id);
    	Estatus estatus= new Estatus();
        estatus.setIdEstatus(2);
        fechaInicial = formatter.parse(fechaInicio,pos);
		fechaFinal=formatter.parse(fechaFin,pos);
		System.out.println("fechaInicio "+fechaInicial);
		Vacaciones vacaciones=vacacionesService.aceptaORechazaVacaciones(new Vacaciones(usuario,null,null,null,estatus,fechaInicial,fechaFinal,null), idSolicitud);
    	//catalogoService.eliminaHorario(id);
		this.mensaje=vacaciones.getMensaje();
    	
    	return "redirect:/vacaciones/vacacionesEmpleados";
    }
    
    @GetMapping("vacacion/actualizaPropiasVacaciones")
    public String actualizaPropiasVacaciones(Integer id,String idUsuario,Integer idVacacion,Integer dias) {
    	System.out.println("idVacacion "+id+" idUsuario "+idUsuario+" idVacacion "+idVacacion+" dias"+dias);
    	VacacionPeriodo vacacion=new  VacacionPeriodo();
    	vacacion.setIdVacacion(idVacacion);
    	Usuario usuario=new Usuario();
    	usuario.setIdUsuario(Integer.parseInt(idUsuario));
    	Estatus estatus= new Estatus();
        estatus.setIdEstatus(3);
    	Vacaciones vacaciones=vacacionesService.aceptaORechazaVacaciones(new Vacaciones(usuario,vacacion,null,null,estatus,null,null,dias), id);
    	this.mensaje=vacaciones.getMensaje();
    	return "redirect:/vacaciones/vacacionesPropias";
    
    }
    @GetMapping("vacacion/actualizaVacacionesEmpleados")
    public String actualizaVacacionesEmpleados(Integer id,String idUsuario,Integer idVacacion,Integer dias) {
    	System.out.println("idVacacion "+id+" idUsuario "+idUsuario+" idVacacion "+idVacacion+" dias"+dias);
    	VacacionPeriodo vacacion=new  VacacionPeriodo();
    	vacacion.setIdVacacion(idVacacion);
    	Usuario usuario=new Usuario();
    	usuario.setIdUsuario(Integer.parseInt(idUsuario));
    	Estatus estatus= new Estatus();
        estatus.setIdEstatus(3);
    	Vacaciones vacaciones=vacacionesService.aceptaORechazaVacaciones(new Vacaciones(usuario,vacacion,null,null,estatus,null,null,dias), id);
    	this.mensaje=vacaciones.getMensaje();
    	return "redirect:/vacaciones/vacacionesEmpleados";
    
    }
    
    @PostMapping("/vacacion/actualizaArchivo")
    public String registraVacaciones(@RequestParam MultipartFile archivo, Integer idArchivo,String claveUsuario,Integer idDetalle ){
    	System.out.println("Datos archivo "+archivo+" idArchivo "+idArchivo+" claveUsuario "+claveUsuario);
    	Integer idArchivoAux=null;
    	Vacaciones vacaciones= new Vacaciones();
    	Archivo archivoDto=new Archivo();
    	vacaciones.setIdDetalle(idDetalle);
    	if(idArchivo!=null && !idArchivo.toString().isEmpty()){
    		archivoService.actualizaArchivo(archivo, claveUsuario, new String("vacaciones"),idArchivo,"vacacion-");
    		archivoDto.setIdArchivo(idArchivo);
    		vacaciones.setIdArchivo(archivoDto);
    		vacacionesService.modificaVacaciones(vacaciones);
    	}else{
    		idArchivoAux=archivoService.guardaArchivo(archivo, claveUsuario, "vacaciones","vacacion-");
    		archivoDto.setIdArchivo(idArchivoAux);
    		vacaciones.setIdArchivo(archivoDto);
    		vacacionesService.modificaVacaciones(vacaciones);
    	}
    	this.mensaje=archivoDto.getMensaje();
    	return"redirect:/vacaciones/vacacionesPropias";
    	}
    @PostMapping("/vacacion/actualizaArchivoEmpleado")
    public String registraVacacionesEmpleado(@RequestParam MultipartFile archivo, Integer idArchivo,String claveUsuario,Integer idDetalle ){
    	System.out.println("Datos archivo "+archivo+" idArchivo "+idArchivo+" claveUsuario "+claveUsuario+" idDetalle "+idDetalle);
    	Integer idArchivoAux=null;
    	Vacaciones vacaciones= new Vacaciones();
    	Archivo archivoDto=new Archivo();
    	vacaciones.setIdDetalle(idDetalle);
    	if(idArchivo!=null && !idArchivo.toString().isEmpty()){
    		archivoService.actualizaArchivo(archivo, claveUsuario, new String("vacaciones"),idArchivo,"vacacion-");
    		archivoDto.setIdArchivo(idArchivo);
    		vacaciones.setIdArchivo(archivoDto);
    		vacacionesService.modificaVacaciones(vacaciones);
    	}else{
    		idArchivoAux=archivoService.guardaArchivo(archivo, claveUsuario, "vacaciones","vacacion-");
    		archivoDto.setIdArchivo(idArchivoAux);
    		vacaciones.setIdArchivo(archivoDto);
    		vacacionesService.modificaVacaciones(vacaciones);
    	}
    	return"redirect:/vacaciones/vacacionesEmpleados";
    	}
    
    @GetMapping("vacacion/rechaza")
    public String rechazaVacaciones(Integer idSolicitud,String idUsuario,Integer idVacacion,Integer dias) {
    	System.out.println("idVacacion "+idSolicitud+" idUsuario "+idUsuario+" idVacacion "+idVacacion+" dias"+dias);
    	VacacionPeriodo vacacion=new  VacacionPeriodo();
    	vacacion.setIdVacacion(idVacacion);
    	Usuario usuario=new Usuario();
    	usuario.setIdUsuario(Integer.parseInt(idUsuario));
    	Estatus estatus= new Estatus();
        estatus.setIdEstatus(3);
    	vacacionesService.aceptaORechazaVacaciones(new Vacaciones(usuario,vacacion,null,null,estatus,null,null,dias), idSolicitud);
    	return "redirect:/vacaciones/vacacionesEmpleados";
    }
    
    @RequestMapping(value = "/descargaArchivo", method = RequestMethod.GET)
    public void getFile(Integer idArchivo, HttpServletRequest request, HttpServletResponse response) throws IOException{
    	System.out.println("id del archivo "+idArchivo);
    	Archivo archivo= new Archivo();
    	archivo=archivoService.consultaArchivo(idArchivo);
    	System.out.println("archivo retornado "+archivo.getUrl());
    	String nombrecompleto=archivo.getUrl()+archivo.getNombre();
    	//String nombreArchivo=nombrecompleto.replace('/','\\');
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
	public String getMensaje() {
		return mensaje == null ? "" : this.mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
    
    
}
