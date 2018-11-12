package mx.gob.segob.dgtic.web.mvc.views.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import mx.gob.segob.dgtic.web.mvc.dto.Archivo;
import mx.gob.segob.dgtic.web.mvc.dto.Estatus;
import mx.gob.segob.dgtic.web.mvc.dto.LicenciaMedica;
import mx.gob.segob.dgtic.web.mvc.dto.LicenciaMedicaAux;
import mx.gob.segob.dgtic.web.mvc.dto.PerfilUsuario;
import mx.gob.segob.dgtic.web.mvc.dto.Periodo;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;
import mx.gob.segob.dgtic.web.mvc.dto.VacacionPeriodo;
import mx.gob.segob.dgtic.web.mvc.dto.Vacaciones;
import mx.gob.segob.dgtic.web.mvc.service.ArchivoService;
import mx.gob.segob.dgtic.web.mvc.service.CatalogoService;
import mx.gob.segob.dgtic.web.mvc.service.EstatusService;
import mx.gob.segob.dgtic.web.mvc.service.LicenciaMedicaService;
import mx.gob.segob.dgtic.web.mvc.service.PerfilUsuarioService;
import mx.gob.segob.dgtic.web.mvc.service.UnidadAdministrativaService;
import mx.gob.segob.dgtic.web.mvc.service.UsuarioService;

@Controller
@RequestMapping( value = "licenciasMedicas", method = RequestMethod.GET)
public class LicenciasMedicasController {
	
	@Autowired 
	private LicenciaMedicaService licenciaMedicaService;
	
	@Autowired 
	private UnidadAdministrativaService unidadAdministrativaService;
	
	@Autowired 
	private EstatusService estatusService;
	
	@Autowired 
	private UsuarioService usuarioService;
	
	@Autowired 
	private ArchivoService archivoService;
	
	@Autowired 
	private CatalogoService catalogoService;
	
	@Autowired
	private PerfilUsuarioService perfilUsuarioService;
	
	private String mensaje = "";
	
	@RequestMapping(value={"licenciasPropias"}, method = RequestMethod.GET)
    public String obtieneLicencias(String fechaInicioBusca1, String fechaFinBusca1, String idEstatus, Model model, HttpSession session, Authentication authentication) {
		String string=""+ session.getAttribute("usuario");
    	String[] parts = string.split(": ");
    	String claveUsuario = parts[1];
    	Periodo periodo= new Periodo();
		System.out.println("Datos claveUsuario "+ claveUsuario +" fechaInicio "+fechaInicioBusca1+" fechaFin "+fechaFinBusca1+" idEstatus "+idEstatus);
		
		if(fechaInicioBusca1==null || fechaInicioBusca1.trim().isEmpty()){
			fechaInicioBusca1="";
		}
		if(fechaFinBusca1==null || fechaFinBusca1.trim().isEmpty()){
			fechaFinBusca1="";
		}
		if(idEstatus==null || idEstatus.trim().isEmpty()){
			idEstatus="";
		}
		List<LicenciaMedica> lista= new ArrayList<>();
		lista=licenciaMedicaService.obtenerListaLicenciaMedicaPorFiltros(claveUsuario, fechaInicioBusca1, fechaFinBusca1, idEstatus, authentication);
		System.out.println("Haciendo la consulta "+lista.size());
		model.addAttribute("licenciasMedicas",lista);
		model.addAttribute("listaEstatus",estatusService.obtieneListaEstatus(authentication));
		if(!this.getMensaje().equals("")){
			if(this.mensaje.contains("correctamente"))
				model.addAttribute("MENSAJE", this.mensaje);
			else
				model.addAttribute("MENSAJE_EXCEPCION", this.mensaje);
		}
		this.mensaje = "";
    	return "/licenciasMedicas/licenciasPropias"; 
    }
	
//	@RequestMapping(value={"agregaSolicitudLicencia"}, method = RequestMethod.POST)
//    public String agregaLicencias(String claveUsuario, String idResponsable, String fechaInicio, String fechaFin,
//    		Integer dias, String padecimiento, Model model, HttpSession session) {
//		System.out.println("Datos claveUsuario "+claveUsuario+" responsable "+idResponsable +" fechaInicio "+fechaInicio
//				+" fechaFin "+fechaFin+" dias "+dias+" padecimiento "+padecimiento);
//		Integer idResponsableAux=null;
//		if(idResponsable!=null && !idResponsable.trim().isEmpty()){
//			idResponsableAux=Integer.parseInt(idResponsable);
//		}
//		//licenciaMedicaService.AgregaLicenciaMedica(new LicenciaMedica (null,null,idResponsableAux,null,null,fechaInicio,fechaFin,dias,padecimiento), claveUsuario);
//		List<LicenciaMedica> lista= new ArrayList<>();
//		//System.out.println("Haciendo la consulta ");
//    	return "/licenciasMedicas/solicitudLicencia";
//    	 
//    }
	
	@RequestMapping(value={"solicitudLicencia"}, method = RequestMethod.GET)
    public String solicitudLicencia(Model model, HttpSession session, Authentication authentication) {
		
		
		//licenciaMedicaService.AgregaLicenciaMedica(new LicenciaMedica (null,null,idResponsableAux,null,null,fechaInicio,fechaFin,dias,padecimiento), claveUsuario);
		List<LicenciaMedica> lista= new ArrayList<>();
		//System.out.println("Haciendo la consulta ");
		String string=""+ session.getAttribute("usuario");
    	String[] parts = string.split(": ");
    	String claveUsuario = parts[1];
		model.addAttribute("usuario",usuarioService.buscaUsuario(claveUsuario, authentication));
		System.out.println("recuperando datos "+claveUsuario);
		if(!this.getMensaje().equals("")){
			if(this.mensaje.contains("correctamente"))
				model.addAttribute("MENSAJE", this.mensaje);
			else
				model.addAttribute("MENSAJE_EXCEPCION", this.mensaje);
		}
		this.mensaje = "";
    	return "/licenciasMedicas/solicitudLicencia";
    	 
    }
	
	@RequestMapping(value={"solicitudLicenciasEmpleados"}, method = RequestMethod.GET)
    public String agregaLicenciaEmpleados(String claveUsuario, String nombre, String apellidoPaterno, String apellidoMaterno ,Model model, HttpSession session, Authentication authentication) {
		
		List<LicenciaMedica> lista= new ArrayList<>();
		System.out.println("Haciendo la consulta de empleados claveUsuario "+claveUsuario+" nombre "+nombre+" apellidoPaterno "+apellidoPaterno
		+" apellidoMaterno "+apellidoMaterno);
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
		String string=""+ session.getAttribute("usuario");
    	String[] parts = string.split(": ");
    	String claveUsuarioAux = parts[1];
    	Usuario usuario= new Usuario();
    	usuario=usuarioService.buscaUsuario(claveUsuarioAux, authentication);
    	String idUnidad=""+usuario.getIdUnidad();
    	System.out.println("idUnidad para buscar "+idUnidad);
		//String idUnidad="13";
		model.addAttribute("licencias",licenciaMedicaService.obtenerLicenciasPorUnidad(idUnidad, claveUsuario, nombre, apellidoPaterno, apellidoMaterno, authentication));
		if(!this.getMensaje().equals("")){
			if(this.mensaje.contains("correctamente"))
				model.addAttribute("MENSAJE", this.mensaje);
			else
				model.addAttribute("MENSAJE_EXCEPCION", this.mensaje);
		}
		this.mensaje = "";
		return "/licenciasMedicas/solicitudLicenciaEmpleados";
    	 
    }
	
	@RequestMapping(value={"licenciasEmpleados"}, method = RequestMethod.GET)
    public String obtieneLicenciasPropias(String claveUsuario, String nombre, String apellidoPaterno, String apellidoMaterno, 
    		String idEstatus, String idUnidad, Model model, HttpSession session, Authentication authentication) {
		
		System.out.println("Datos claveUsuario "+ "nombre "+nombre+" apellidoPaterno "+apellidoPaterno+" idEstatus "
    		+idEstatus+" apellidoMaterno "+apellidoMaterno+" idUnidad "+idUnidad);
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
		String string=""+ session.getAttribute("usuario");
    	String[] parts = string.split(": ");
    	String claveUsuarioLider = parts[1];
    	List<PerfilUsuario> listaPerfilUsuario= new ArrayList<>();
    	listaPerfilUsuario=perfilUsuarioService.recuperaPerfilesUsuario(claveUsuarioLider, authentication);
    	Boolean usuario= false;
    	for(PerfilUsuario perfilUsuario: listaPerfilUsuario){
    		if(idUnidad==null || idUnidad.toString().isEmpty()){
    			System.out.println("Entrando al if "+perfilUsuario.getClavePerfil().getClavePerfil());
	    		if(perfilUsuario.getClavePerfil().getClavePerfil().toString().equals("2")){
	    			System.out.println("Entrando al if "+perfilUsuario.getClavePerfil().getClavePerfil());
	    			usuario=true;
	    			Usuario usuarioAux= new Usuario();
	    	    	if(usuario==true){
	    	    		
	    	    		usuarioAux=usuarioService.buscaUsuario(claveUsuarioLider, authentication);
	    	    		idUnidad=""+usuarioAux.getIdUnidad();
	    	    	}else if(idUnidad==null){
	    	    			idUnidad="";
	    	    	}
	    		}
	    		if(perfilUsuario.getClavePerfil().getClavePerfil().toString().equals("1")){
	    			idUnidad="";
	    		}
    		}
    		
    	}
    	System.out.println("Bandera para determinar si es empleado o no "+usuario+" claveUsuario "+claveUsuario);
		
		System.out.println("idUnidad "+idUnidad);
		List<LicenciaMedica> lista= new ArrayList<>();
		lista=licenciaMedicaService.obtenerListaLicenciaMedicaEmpleados(claveUsuario, nombre, apellidoPaterno, apellidoMaterno, idEstatus, idUnidad, authentication);
		System.out.println("Haciendo la consulta "+lista.size());
		model.addAttribute("licenciasMedicas",lista);
		model.addAttribute("listaUnidades",unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
		model.addAttribute("listaEstatus",estatusService.obtieneListaEstatus(authentication));
		if(!this.getMensaje().equals("")){
			if(this.mensaje.contains("correctamente"))
				model.addAttribute("MENSAJE", this.mensaje);
			else
				model.addAttribute("MENSAJE_EXCEPCION", this.mensaje);
		}
		this.mensaje = "";
    	return "/licenciasMedicas/licenciasEmpleados"; 
    }
	
	//@RequestMapping(value={"busca"}, method = RequestMethod.POST)
	@GetMapping("/busca")
	@ResponseBody
    public HashMap<String, Object>  obtieneLicenciasDeEmpleado(Integer idLicencia, Authentication authentication) {
		HashMap<String, Object> hmap = new HashMap<String, Object>();
		//List<LicenciaMedica> lista= new ArrayList<>();
		//lista=licenciaMedicaService.obtenerListaLicenciaMedicaEmpleados(claveUsuario, nombre, apellidoPaterno, apellidoMaterno, idEstatus, idUnidad);
		//System.out.println("Haciendo la consulta "+lista.size());
		//model.addAttribute("licenciasMedicas",lista);
		//model.addAttribute("listaUnidades",unidadAdministrativaService.obtenerUnidadesAdministrativas());
		//model.addAttribute("listaEstatus",estatusService.obtieneListaEstatus());
		hmap.put("licencia",licenciaMedicaService.buscaLicenciaMedica(idLicencia, authentication));
    	return hmap;
    }
	
	@GetMapping("/buscaDatos")
	@ResponseBody
    public HashMap<String, Object>  obtieneDatos( HttpSession session, Authentication authentication) {
		HashMap<String, Object> hmap = new HashMap<String, Object>();
		
		//List<LicenciaMedica> lista= new ArrayList<>();
		//lista=licenciaMedicaService.obtenerListaLicenciaMedicaEmpleados(claveUsuario, nombre, apellidoPaterno, apellidoMaterno, idEstatus, idUnidad);
		//System.out.println("Haciendo la consulta "+lista.size());
		//model.addAttribute("licenciasMedicas",lista);
		//model.addAttribute("listaUnidades",unidadAdministrativaService.obtenerUnidadesAdministrativas());
		//model.addAttribute("listaEstatus",estatusService.obtieneListaEstatus());
		String string=""+ session.getAttribute("usuario");
    	String[] parts = string.split(": ");
    	String claveUsuario = parts[1];
    	System.out.println("recuperando datos "+claveUsuario);
		hmap.put("usuario",usuarioService.buscaUsuario(claveUsuario, authentication));
		System.out.println("recuperando datos "+claveUsuario);
		hmap.put("usuario",usuarioService.buscaUsuario(claveUsuario, authentication));
		String cadena=catalogoService.obtieneDiaFestivoParaBloquear(authentication);
		String fechas=licenciaMedicaService.consultaDiasPorBloquear(claveUsuario, authentication);
		if(!fechas.isEmpty() && fechas!=null){
    		if(!cadena.isEmpty() && cadena!=null){
    			cadena+=","+fechas;
    		}else{
    			cadena=fechas;
    		}
    	}
		Periodo periodo= new Periodo();
		periodo.setMensaje(cadena);
		hmap.put("periodo",periodo);
		
		hmap.put("valores", licenciaMedicaService.buscaDiasLicenciaMedica(claveUsuario, authentication));
		
    	//return "/licenciasMedicas/solicitudLicencia";
		//hmap.put("licencia",licenciaMedicaService.buscaLicenciaMedica(idLicencia));
    	return hmap;
    }
	
	@GetMapping("/buscaDatosEmpleado")
	@ResponseBody
    public HashMap<String, Object>  obtieneDatosEmpleado( String claveUsuario, HttpSession session, Authentication authentication) {
		HashMap<String, Object> hmap = new HashMap<String, Object>();
		
		//List<LicenciaMedica> lista= new ArrayList<>();
		//lista=licenciaMedicaService.obtenerListaLicenciaMedicaEmpleados(claveUsuario, nombre, apellidoPaterno, apellidoMaterno, idEstatus, idUnidad);
		//System.out.println("Haciendo la consulta "+lista.size());
		//model.addAttribute("licenciasMedicas",lista);
		//model.addAttribute("listaUnidades",unidadAdministrativaService.obtenerUnidadesAdministrativas());
		//model.addAttribute("listaEstatus",estatusService.obtieneListaEstatus());
//		String string=""+ session.getAttribute("usuario");
//    	String[] parts = string.split(": ");
//    	String claveUsuario = parts[1];
    	System.out.println("recuperando datos "+claveUsuario);
		hmap.put("usuario",usuarioService.buscaUsuario(claveUsuario, authentication));
		String cadena=catalogoService.obtieneDiaFestivoParaBloquear(authentication);
		String fechas=licenciaMedicaService.consultaDiasPorBloquear(claveUsuario, authentication);
		if(!fechas.isEmpty() && fechas!=null){
    		if(!cadena.isEmpty() && cadena!=null){
    			cadena+=","+fechas;
    		}else{
    			cadena=fechas;
    		}
    	}
		Periodo periodo= new Periodo();
		periodo.setMensaje(cadena);
		hmap.put("periodo",periodo);
		System.out.println("claveUsuario para la consulta "+claveUsuario);
		hmap.put("valores", licenciaMedicaService.buscaDiasLicenciaMedica(claveUsuario, authentication));
    	//return "/licenciasMedicas/solicitudLicencia";
		//hmap.put("licencia",licenciaMedicaService.buscaLicenciaMedica(idLicencia));
		licenciaMedicaService.consultaDiasPorBloquear(claveUsuario, authentication);
    	return hmap;
    }
	
	@PostMapping("/agregaSolicitudLicencia")
    public String registraLicencia(String fechaInicio, String fechaFin, Integer dias, String claveUsuario1, String padecimiento, Authentication authentication ) {
	System.out.println("Datos para la insercion claveUsuario "+claveUsuario1+" fechaInicio "+fechaInicio+" fechaFin "+fechaFin
			+" dias "+dias+" padecimiento "+padecimiento);
	LicenciaMedica licencia= new LicenciaMedica();
	licencia=licenciaMedicaService.AgregaLicenciaMedica(new LicenciaMedicaAux(null,null,null, null, null, fechaInicio, fechaFin, dias, padecimiento), claveUsuario1, authentication);
	System.out.println("mensaje recuperado "+licencia.getMensaje());
	this.mensaje=licencia.getMensaje();
	return "redirect:/licenciasMedicas/solicitudLicenciasEmpleados";
	}
	
	@PostMapping("/actualizaArchivo")
    public String registraVacaciones(@RequestParam MultipartFile archivo, Integer idArchivo,String claveUsuario,Integer idLicencia, Authentication authentication){
    	System.out.println("Datos archivo "+archivo+" idArchivo "+idArchivo+" claveUsuario "+claveUsuario+" idLicencia "+idLicencia);
    	Archivo idArchivoAux=new Archivo();
    	LicenciaMedica licencia= new LicenciaMedica();
    	//Vacaciones vacaciones= new Vacaciones();
    	Archivo archivoDto=new Archivo();
    	//vacaciones.setIdDetalle(idDetalle);
    	if(archivo!=null && !archivo.isEmpty()){
	    	if(idArchivo!=null && !idArchivo.toString().isEmpty()){
	    		archivoService.actualizaArchivo(archivo, claveUsuario, "licenciasMedicas",idArchivo,"licenciaMedica-", authentication);
	    		licencia=licenciaMedicaService.modificaLicenciaMedica(new LicenciaMedicaAux(idLicencia,null,null,idArchivo,1,null,null,null,null), claveUsuario, authentication);
	    		//archivoService.guardaArchivo(archivo, claveUsuario, "licenciasMedicas");
	    		//archivoDto.setIdArchivo(idArchivo);
	    		//vacaciones.setIdArchivo(archivoDto);
	    		//vacacionesService.modificaVacaciones(vacaciones);
	    	}else{
	    		//idArchivoAux=archivoService.guardaArchivo(archivo, claveUsuario, "vacaciones");
	    		//archivoDto.setIdArchivo(idArchivoAux);
	    		idArchivoAux=archivoService.guardaArchivo(archivo, claveUsuario, "licenciasMedicas","licenciaMedica-", authentication);
	    		System.out.println("IDArchivo recuperado "+idArchivoAux.getIdArchivo());
	    		licencia=licenciaMedicaService.modificaLicenciaMedica(new LicenciaMedicaAux(idLicencia,null,null,idArchivoAux.getIdArchivo(),1,null,null,null,null), claveUsuario, authentication);
	    		//vacaciones.setIdArchivo(archivoDto);
	    		//vacacionesService.modificaVacaciones(vacaciones);
	    	}
    	}
    	System.out.println("mensaje recuperado "+licencia.getMensaje());
    	this.mensaje=licencia.getMensaje();
    	//this.mensaje=archivoDto.getMensaje();
    	return"redirect:/licenciasMedicas/licenciasEmpleados";
    	}
	
	@RequestMapping(value = "/descargaArchivo", method = RequestMethod.GET)
    public void getFile(Integer idArchivo, HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
    	System.out.println("id del archivo "+idArchivo);
    	Archivo archivo= new Archivo();
    	archivo=archivoService.consultaArchivo(idArchivo, authentication);
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
	
	@PostMapping("rechaza")
    public String rechazaVacaciones(Integer idLicencia,String claveUsuario, Integer idArchivo, Authentication authentication) {
    	System.out.println("idLicencia "+idLicencia+" claveUsuario "+claveUsuario+" idArchivo "+idArchivo);
    	LicenciaMedica licencia= new LicenciaMedica();
    	licencia=licenciaMedicaService.modificaLicenciaMedica(new LicenciaMedicaAux(idLicencia, null,null, idArchivo, 3,null,null,null,null), claveUsuario, authentication);
   // 	vacacionesService.aceptaORechazaVacaciones(new Vacaciones(usuario,vacacion,null,null,estatus,null,null,dias), idSolicitud);
    	this.mensaje=licencia.getMensaje();
    	System.out.println("mensaje recuperado "+licencia.getMensaje());
    	return "redirect:/licenciasMedicas/licenciasEmpleados";
    }
	
	@PostMapping("acepta")
    public String aceptaVacaciones(Integer idLicencia,String claveUsuario, Integer idArchivo, Authentication authentication) {
    	System.out.println("idLicencia "+idLicencia+" claveUsuario "+claveUsuario+" idArchivo "+idArchivo);
    	LicenciaMedica licencia= new LicenciaMedica();
    	licencia=licenciaMedicaService.modificaLicenciaMedica(new LicenciaMedicaAux(idLicencia, null,null, idArchivo, 2,null,null,null,null), claveUsuario, authentication);
    	//fechaInicio=fechaInicio.substring(0,21);
    	//ParsePosition pos = new ParsePosition(4);
    	this.mensaje=licencia.getMensaje();
    	System.out.println("mensaje recuperado "+licencia.getMensaje());
    	return "redirect:/licenciasMedicas/licenciasEmpleados";
    }
	
	public String getMensaje() {
		return mensaje == null ? "" : this.mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}
