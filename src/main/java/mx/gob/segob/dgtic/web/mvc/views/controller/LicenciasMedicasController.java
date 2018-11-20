package mx.gob.segob.dgtic.web.mvc.views.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import mx.gob.segob.dgtic.web.mvc.dto.Archivo;
import mx.gob.segob.dgtic.web.mvc.dto.BusquedaDto;
import mx.gob.segob.dgtic.web.mvc.dto.LicenciaMedica;
import mx.gob.segob.dgtic.web.mvc.dto.LicenciaMedicaAux;
import mx.gob.segob.dgtic.web.mvc.dto.PerfilUsuario;
import mx.gob.segob.dgtic.web.mvc.dto.Periodo;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;
import mx.gob.segob.dgtic.web.mvc.service.ArchivoService;
import mx.gob.segob.dgtic.web.mvc.service.CatalogoService;
import mx.gob.segob.dgtic.web.mvc.service.EstatusService;
import mx.gob.segob.dgtic.web.mvc.service.LicenciaMedicaService;
import mx.gob.segob.dgtic.web.mvc.service.PerfilUsuarioService;
import mx.gob.segob.dgtic.web.mvc.service.UnidadAdministrativaService;
import mx.gob.segob.dgtic.web.mvc.service.UsuarioService;
import mx.gob.segob.dgtic.web.mvc.views.controller.constants.ConstantsController;

@Controller
@RequestMapping( value = ConstantsController.LICENCIAS_MEDICAS, method = RequestMethod.GET)
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
	private static final String REDIRECT_LICENCIAS_MEDICAS_EMPLEADOS = "redirect:/licenciasMedicas/licenciasEmpleados";
	
	private static final Logger logger = LoggerFactory.getLogger(CatalogoController.class);
	private String mensaje = "";
	
	@RequestMapping(value={"licenciasPropias"}, method = RequestMethod.GET)
    public String obtieneLicencias(String fechaInicioBusca1, String fechaFinBusca1, String idEstatus, Model model, HttpSession session, Authentication authentication) {
		String string=""+ session.getAttribute(ConstantsController.USUARIO);
    	String[] parts = string.split(": ");
    	String claveUsuario = parts[1];
    	
		if(fechaInicioBusca1==null || fechaInicioBusca1.trim().isEmpty()){
			fechaInicioBusca1="";
		}
		if(fechaFinBusca1==null || fechaFinBusca1.trim().isEmpty()){
			fechaFinBusca1="";
		}
		if(idEstatus==null || idEstatus.trim().isEmpty()){
			idEstatus="";
		}
		List<LicenciaMedica> lista;
		BusquedaDto busquedaDto = new BusquedaDto();
		busquedaDto.setFechaFin(fechaFinBusca1);
		busquedaDto.setFechaInicio(fechaInicioBusca1);
		busquedaDto.setClaveUsuario(claveUsuario);
		busquedaDto.setIdEstatus(idEstatus);
		lista=licenciaMedicaService.obtenerListaLicenciaMedicaPorFiltros(busquedaDto, authentication);
		logger.info("Haciendo la consulta. {} ",lista.size());
		model.addAttribute(ConstantsController.LICENCIAS_MEDICAS,lista);
		model.addAttribute("listaEstatus",estatusService.obtieneListaEstatus(authentication));
		if(!this.getMensaje().equals("")){
			if(this.mensaje.contains(ConstantsController.CORRECTAMENTE))
				model.addAttribute(ConstantsController.MENSAJE, this.mensaje);
			else
				model.addAttribute(ConstantsController.MENSAJE_EXCEPTION, this.mensaje);
		}
		this.mensaje = "";
    	return "/licenciasMedicas/licenciasPropias"; 
    }

	
	@RequestMapping(value={"solicitudLicencia"}, method = RequestMethod.GET)
    public String solicitudLicencia(Model model, HttpSession session, Authentication authentication) {
	
		String string=""+ session.getAttribute(ConstantsController.USUARIO);
    	String[] parts = string.split(": ");
    	String claveUsuario = parts[1];
		model.addAttribute(ConstantsController.USUARIO,usuarioService.buscaUsuario(claveUsuario, authentication));
		logger.info("recuperando datos: {} ",claveUsuario);
		if(!this.getMensaje().equals("")){
			if(this.mensaje.contains(ConstantsController.CORRECTAMENTE))
				model.addAttribute(ConstantsController.MENSAJE, this.mensaje);
			else
				model.addAttribute(ConstantsController.MENSAJE_EXCEPTION, this.mensaje);
		}
		this.mensaje = "";
    	return "/licenciasMedicas/solicitudLicencia";
    	 
    }
	
	@RequestMapping(value={"solicitudLicenciasEmpleados"}, method = RequestMethod.GET)
    public String agregaLicenciaEmpleados(String claveUsuario, String nombre, String apellidoPaterno, String apellidoMaterno ,Model model, HttpSession session, Authentication authentication) {
		
		String string=""+ session.getAttribute(ConstantsController.USUARIO);
    	String[] parts = string.split(": ");
    	String claveUsuarioAux = parts[1];
    	Usuario usuario;
    	usuario=usuarioService.buscaUsuario(claveUsuarioAux, authentication);
    	String idUnidad=""+usuario.getIdUnidad();
    	logger.info("idUnidad para buscar : {}",idUnidad);
    	BusquedaDto busquedaDto = new BusquedaDto();
    	busquedaDto.setApellidoMaterno(apellidoMaterno);
    	busquedaDto.setApellidoPaterno(apellidoPaterno);
    	busquedaDto.setClaveUsuario(claveUsuario);
    	busquedaDto.setIdUnidad(idUnidad);
    	busquedaDto.setNombre(nombre);
    	
		model.addAttribute("licencias",licenciaMedicaService.obtenerLicenciasPorUnidad(busquedaDto, authentication));
		if(!this.getMensaje().equals("")){
			if(this.mensaje.contains(ConstantsController.CORRECTAMENTE))
				model.addAttribute(ConstantsController.MENSAJE, this.mensaje);
			else
				model.addAttribute(ConstantsController.MENSAJE_EXCEPTION, this.mensaje);
		}
		this.mensaje = "";
		return "/licenciasMedicas/solicitudLicenciaEmpleados";
    	 
    }
	
	@RequestMapping(value={"licenciasEmpleados"}, method = RequestMethod.GET)
    public String obtieneLicenciasPropias(String claveUsuario, String nombre, String apellidoPaterno, String apellidoMaterno, 
    		String idEstatus, String idUnidad, Model model, HttpSession session, Authentication authentication) {
		String string=""+ session.getAttribute(ConstantsController.USUARIO);
    	String[] parts = string.split(": ");
    	String claveUsuarioLider = parts[1];
    	List<PerfilUsuario> listaPerfilUsuario;
    	listaPerfilUsuario=perfilUsuarioService.recuperaPerfilesUsuario(claveUsuarioLider, authentication);
    	Boolean usuario= false;
    	for(PerfilUsuario perfilUsuario: listaPerfilUsuario){
    		if(idUnidad==null || idUnidad.isEmpty()){
    			logger.info("Entrando al if. {} ",perfilUsuario.getClavePerfil().getClavePerfil());
	    		if(perfilUsuario.getClavePerfil().getClavePerfil().equals("2")){
	    			logger.info("Entrando al if: {} ",perfilUsuario.getClavePerfil().getClavePerfil());
	    			usuario=true;
	    			Usuario usuarioAux;
	    	    	if(usuario==true){
	    	    		
	    	    		usuarioAux=usuarioService.buscaUsuario(claveUsuarioLider, authentication);
	    	    		idUnidad=""+usuarioAux.getIdUnidad();
	    	    	}
	    		}
	    		if(perfilUsuario.getClavePerfil().getClavePerfil().equals("1")){
	    			idUnidad="";
	    		}
    		}
    		
    	}
		
    	logger.info("idUnidad : {}",idUnidad);
		List<LicenciaMedica> lista;
		BusquedaDto busquedaDto = new BusquedaDto();
		busquedaDto.setClaveUsuario(claveUsuario);
		busquedaDto.setNombre(nombre);
		busquedaDto.setApellidoPaterno(apellidoPaterno);
		busquedaDto.setApellidoMaterno(apellidoMaterno);
		busquedaDto.setIdEstatus(idEstatus);
		busquedaDto.setIdUnidad(idUnidad);
		lista=licenciaMedicaService.obtenerListaLicenciaMedicaEmpleados(busquedaDto, authentication);
		logger.info("Haciendo la consulta. {} ",lista.size());
		model.addAttribute(ConstantsController.LICENCIAS_MEDICAS,lista);
		model.addAttribute("listaUnidades",unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
		model.addAttribute("listaEstatus",estatusService.obtieneListaEstatus(authentication));
		if(!this.getMensaje().equals("")){
			if(this.mensaje.contains(ConstantsController.CORRECTAMENTE))
				model.addAttribute(ConstantsController.MENSAJE, this.mensaje);
			else
				model.addAttribute(ConstantsController.MENSAJE_EXCEPTION, this.mensaje);
		}
		this.mensaje = "";
    	return "/licenciasMedicas/licenciasEmpleados"; 
    }
	
	
	@GetMapping("/busca")
	@ResponseBody
    public Map<String, Object>  obtieneLicenciasDeEmpleado(Integer idLicencia, Authentication authentication) {
		HashMap<String, Object> hmap = new HashMap<>();

		hmap.put("licencia",licenciaMedicaService.buscaLicenciaMedica(idLicencia, authentication));
    	return hmap;
    }
	
	@GetMapping("/buscaDatos")
	@ResponseBody
    public Map<String, Object>  obtieneDatos( HttpSession session, Authentication authentication) {
		HashMap<String, Object> hmap = new HashMap<>();

		String string=""+ session.getAttribute(ConstantsController.USUARIO);
    	String[] parts = string.split(": ");
    	String claveUsuario = parts[1];
    	logger.info("recuperando datos.. {} ",claveUsuario);
		hmap.put(ConstantsController.USUARIO,usuarioService.buscaUsuario(claveUsuario, authentication));
		logger.info("recuperando datos.. {} ",claveUsuario);
		hmap.put(ConstantsController.USUARIO,usuarioService.buscaUsuario(claveUsuario, authentication));
		String cadena=catalogoService.obtieneDiaFestivoParaBloquear(authentication);
		String fechas=licenciaMedicaService.consultaDiasPorBloquear(claveUsuario, authentication);
		if(!fechas.isEmpty()){
    		if(!cadena.isEmpty()){
    			cadena+=","+fechas;
    		}else{
    			cadena=fechas;
    		}
    	}
		Periodo periodo= new Periodo();
		periodo.setMensaje(cadena);
		hmap.put("periodo",periodo);
		
		hmap.put("valores", licenciaMedicaService.buscaDiasLicenciaMedica(claveUsuario, authentication));

    	return hmap;
    }
	
	@GetMapping("/buscaDatosEmpleado")
	@ResponseBody
    public Map<String, Object>  obtieneDatosEmpleado( String claveUsuario, HttpSession session, Authentication authentication) {
		HashMap<String, Object> hmap = new HashMap<>();
		
		logger.info("recuperando datos: {} ",claveUsuario);
		hmap.put(ConstantsController.USUARIO,usuarioService.buscaUsuario(claveUsuario, authentication));
		String cadena=catalogoService.obtieneDiaFestivoParaBloquear(authentication);
		String fechas=licenciaMedicaService.consultaDiasPorBloquear(claveUsuario, authentication);
		if(!fechas.isEmpty()){
    		if(!cadena.isEmpty()){
    			cadena+=","+fechas;
    		}else{
    			cadena=fechas;
    		}
    	}
		Periodo periodo= new Periodo();
		periodo.setMensaje(cadena);
		hmap.put("periodo",periodo);
		logger.info("claveUsuario para la consulta- {} ",claveUsuario);
		hmap.put("valores", licenciaMedicaService.buscaDiasLicenciaMedica(claveUsuario, authentication));
		licenciaMedicaService.consultaDiasPorBloquear(claveUsuario, authentication);
    	return hmap;
    }
	
	@PostMapping("/agregaSolicitudLicencia")
    public String registraLicencia(String fechaInicio, String fechaFin, Integer dias, String claveUsuario1, String padecimiento, Authentication authentication ) {

	LicenciaMedica licencia;
	licencia=licenciaMedicaService.AgregaLicenciaMedica(new LicenciaMedicaAux(null,null,null, null, null, fechaInicio, fechaFin, dias, padecimiento), claveUsuario1, authentication);
	logger.info("mensaje recuperado -.{}",licencia.getMensaje());
	this.mensaje=licencia.getMensaje();
	return "redirect:/licenciasMedicas/solicitudLicenciasEmpleados";
	}
	
	@PostMapping("/actualizaArchivo")
    public String registraVacaciones(@RequestParam MultipartFile archivo, Integer idArchivo,String claveUsuario,Integer idLicencia, Authentication authentication){
    	
    	Archivo idArchivoAux;
    	LicenciaMedica licencia= new LicenciaMedica();
    	
    	if(archivo!=null && !archivo.isEmpty()){
	    	if(idArchivo!=null && !idArchivo.toString().isEmpty()){
	    		archivoService.actualizaArchivo(archivo, claveUsuario, ConstantsController.LICENCIAS_MEDICAS,idArchivo,"licenciaMedica-", authentication);
	    		licencia=licenciaMedicaService.modificaLicenciaMedica(new LicenciaMedicaAux(idLicencia,null,null,idArchivo,1,null,null,null,null), claveUsuario, authentication);
	    	}else{

	    		idArchivoAux=archivoService.guardaArchivo(archivo, claveUsuario, ConstantsController.LICENCIAS_MEDICAS,"licenciaMedica-", authentication);
	    		logger.info("IDArchivo recuperado.- {} ",idArchivoAux.getIdArchivo());
	    		licencia=licenciaMedicaService.modificaLicenciaMedica(new LicenciaMedicaAux(idLicencia,null,null,idArchivoAux.getIdArchivo(),1,null,null,null,null), claveUsuario, authentication);

	    	}
    	}
    	logger.info("mensaje recuperado {} ",licencia.getMensaje());
    	this.mensaje=licencia.getMensaje();
    	return REDIRECT_LICENCIAS_MEDICAS_EMPLEADOS;
    	}
	
	@RequestMapping(value = "/descargaArchivo", method = RequestMethod.GET)
    public void getFile(Integer idArchivo, HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
    	
    	Archivo archivo;
    	archivo=archivoService.consultaArchivo(idArchivo, authentication);
    	logger.info("archivo retornado : {}",archivo.getUrl());
    	String nombrecompleto=archivo.getUrl()+archivo.getNombre();
    	String nombreArchivo=nombrecompleto;
    	logger.info("nombre de archivo. {}  ",nombreArchivo);
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
    	
    	LicenciaMedica licencia;
    	licencia=licenciaMedicaService.modificaLicenciaMedica(new LicenciaMedicaAux(idLicencia, null,null, idArchivo, 3,null,null,null,null), claveUsuario, authentication);
    	this.mensaje=licencia.getMensaje();
    	logger.info("mensaje recuperado: {} ",licencia.getMensaje());
    	return REDIRECT_LICENCIAS_MEDICAS_EMPLEADOS;
    }
	
	@PostMapping("acepta")
    public String aceptaVacaciones(Integer idLicencia,String claveUsuario, Integer idArchivo, Authentication authentication) {
    	
    	LicenciaMedica licencia;
    	licencia=licenciaMedicaService.modificaLicenciaMedica(new LicenciaMedicaAux(idLicencia, null,null, idArchivo, 2,null,null,null,null), claveUsuario, authentication);
    	this.mensaje=licencia.getMensaje();
    	logger.info("mensaje recuperado: {} ",licencia.getMensaje());
    	return REDIRECT_LICENCIAS_MEDICAS_EMPLEADOS;
    }
	
	public String getMensaje() {
		return mensaje == null ? "" : this.mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}
