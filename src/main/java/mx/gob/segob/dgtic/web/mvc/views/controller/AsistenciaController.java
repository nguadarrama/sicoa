package mx.gob.segob.dgtic.web.mvc.views.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import mx.gob.segob.dgtic.web.mvc.dto.Asistencia;
import mx.gob.segob.dgtic.web.mvc.service.ArchivoService;
import mx.gob.segob.dgtic.web.mvc.service.AsistenciaService;
import mx.gob.segob.dgtic.web.mvc.service.CatalogoService;
import mx.gob.segob.dgtic.web.mvc.util.AsistenciaJustificacion;
import mx.gob.segob.dgtic.web.mvc.util.Excel;

/**
 * Controller donde se registran las vistas de asistencia
 * 
 * Path : {contextoAplicacion}/
 */
@Controller
@RequestMapping( value = "asistencia")
public class AsistenciaController  {	
	
	@Autowired
	private AsistenciaService asistenciaService;
	
	@Autowired
	private CatalogoService catalogoService;
	
	@Autowired
	private ArchivoService archivoService;
	
	//EMPLEADO
	@RequestMapping(value={"empleado"}, method = RequestMethod.GET)
    public String buscaListaAsistenciaEmpleado(Model model) {
    	
    	model.addAttribute("listaAsistencia", new ArrayList<Asistencia>());
    	model.addAttribute("inicio", true); //permite controlar en el front que una etiqueta se va a esconder cuando es el "inicio"
    	
    	return "/asistencia/empleado";
    }
	
	@RequestMapping(value={"empleado/busca"}, method = RequestMethod.GET, params="busca")
    public String buscaListaAsistenciaEmpleadoRango(Model model, Authentication authentication, String fechaInicial, String fechaFinal) {

    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRango(authentication.getName(), fechaInicial, fechaFinal);
    	model.addAttribute("listaAsistencia", asistencia);
    	model.addAttribute("fechaInicial", fechaInicial);
    	model.addAttribute("fechaFinal", fechaFinal);
    	
    	return "/asistencia/empleado";
    }
	
	@RequestMapping(value="empleado/busca", method=RequestMethod.GET, params="exporta")
    public ModelAndView exportaExcelEmpleado(HttpServletRequest request, HttpServletResponse response, Authentication authentication, String fechaInicial, String fechaFinal) {
        
	    	List<Asistencia> listaAsistencia = asistenciaService.buscaAsistenciaEmpleadoRango(authentication.getName(), fechaInicial, fechaFinal);
	    	
	    	Map<String, Object> model = new HashMap<String, Object>();
	        
	    	//nombre de la hoja
	        model.put("nombreHoja", "asistencia");
	        
	        //nombres columnas
	        List<String> cabeceras = new ArrayList<String>();
	        cabeceras.add("Id Asistencia");
	        cabeceras.add("Entrada");
	        cabeceras.add("Id Status");
	        cabeceras.add("Id Tipo");
	        cabeceras.add("Salida");
	        cabeceras.add("Usuario");
	        model.put("cabeceras", cabeceras);
	        
	        //Información registros (List<Object[]>)
	        List<List<String>> asistencias = new ArrayList<List<String>>();
	        
	        for (Asistencia a : listaAsistencia) {
	        	
	        	List<String> elementos = new ArrayList<>();
	        	elementos.add(a.getIdAsistencia() != null ? a.getIdAsistencia().toString() : new String(""));
	        	elementos.add(a.getEntrada().toString());
	        	elementos.add(a.getIdEstatus().getEstatus() != null ? a.getIdEstatus().getEstatus() : new String(""));
	        	elementos.add(a.getIdTipoDia().getNombre() != null ? a.getIdTipoDia().getNombre() : new String(""));
	        	elementos.add(a.getSalida() != null ? a.getSalida().toString() : new String(""));
	        	elementos.add(a.getUsuarioDto() != null ? a.getUsuarioDto().getClaveUsuario() : new String(""));
	        	asistencias.add(elementos);
	        }
	        
	        model.put("registros", asistencias);
	        
	        response.setContentType( "application/ms-excel" );
	        response.setHeader( "Content-disposition", "attachment; filename=SESNSP.xls" );         
	        
	        return new ModelAndView(new Excel(), model);
    }
    
    @RequestMapping(value="empleado/busca", method=RequestMethod.GET, params="imprime")
    public ModelAndView imprimeEmpleado(HttpServletRequest request, HttpServletResponse response, Authentication authentication, String cve_m_usuario, String fechaInicial, String fechaFinal) {
        
    	return new ModelAndView();
    	
    }
	//TERMINA EMPLEADO
    
	//COORDINADOR
    @RequestMapping(value={"coordinador"}, method = RequestMethod.GET)
    public String buscaListaAsistenciaCoordinador(Model model, Authentication authentication) {
    	
    	model.addAttribute("listaAsistencia", new ArrayList<Asistencia>());
    	model.addAttribute("inicio", true); //permite controlar en el front que una etiqueta se va a esconder cuando es el "inicio"
    	
    	return "/asistencia/coordinador";
    }
    
    @RequestMapping(value={"coordinador/busca"}, method = RequestMethod.GET, params="busca")
    public String buscaListaAsistenciaCoordinadorRango(Model model, Authentication authentication, String cve_m_usuario, String nombre, String paterno, 
    		String materno, String nivel, String tipo, String estado, String fechaInicial, String fechaFinal, String unidadAdministrativa) {

	    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(cve_m_usuario, nombre, paterno, materno, nivel, 
	    			tipo, estado, fechaInicial, fechaFinal, unidadAdministrativa, authentication.getName());
	    	
	    	model.addAttribute("listaAsistencia", asistencia);
	    	model.addAttribute("fechaInicial", fechaInicial);
	    	model.addAttribute("fechaFinal", fechaFinal);
	    	model.addAttribute("cve_m_usuario", cve_m_usuario);
	    	model.addAttribute("nombre", nombre);
	    	model.addAttribute("paterno", paterno);
	    	model.addAttribute("materno", materno);
	    	model.addAttribute("nivel", nivel);
	    	model.addAttribute("tipo", tipo);
	    	model.addAttribute("estado", estado);
	    	model.addAttribute("unidadAdministrativa", unidadAdministrativa);
	    	
	    	return "/asistencia/coordinador";
    }
    
    @RequestMapping(value="coordinador/busca", method=RequestMethod.GET, params="exporta")
    public ModelAndView exportaExcelCoordinador(HttpServletRequest request, HttpServletResponse response, Authentication authentication, String cve_m_usuario, String nombre, String paterno, 
    		String materno, String nivel, String tipo, String estado, String fechaInicial, String fechaFinal, String unidadAdministrativa) {
        
    	List<Asistencia> listaAsistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(cve_m_usuario, nombre, paterno, materno, nivel, 
    			tipo, estado, fechaInicial, fechaFinal, unidadAdministrativa, authentication.getName());
    	
    	Map<String, Object> model = new HashMap<String, Object>();
        
    	//nombre de la hoja
        model.put("nombreHoja", "asistencia");
        
        //nombres columnas
        List<String> cabeceras = new ArrayList<String>();
        cabeceras.add("Id Asistencia");
        cabeceras.add("Entrada");
        cabeceras.add("Id Status");
        cabeceras.add("Id Tipo");
        cabeceras.add("Salida");
        cabeceras.add("Usuario");
        model.put("cabeceras", cabeceras);
        
        //Información registros (List<Object[]>)
        List<List<String>> asistencias = new ArrayList<List<String>>();
        
        for (Asistencia a : listaAsistencia) {
        	
        	List<String> elementos = new ArrayList<>();
        	elementos.add(a.getIdAsistencia() != null ? a.getIdAsistencia().toString() : new String(""));
        	elementos.add(a.getEntrada().toString());
        	elementos.add(a.getIdEstatus().getEstatus() != null ? a.getIdEstatus().getEstatus() : new String(""));
        	elementos.add(a.getIdTipoDia().getNombre() != null ? a.getIdTipoDia().getNombre() : new String(""));
        	elementos.add(a.getSalida() != null ? a.getSalida().toString() : new String(""));
        	elementos.add(a.getUsuarioDto() != null ? a.getUsuarioDto().getClaveUsuario() : new String(""));
        	asistencias.add(elementos);
        }
        
        model.put("registros", asistencias);
        
        response.setContentType( "application/ms-excel" );
        response.setHeader( "Content-disposition", "attachment; filename=SESNSP.xls" );         
        
        return new ModelAndView(new Excel(), model);
    	
    }
    
    @RequestMapping(value="coordinador/busca", method=RequestMethod.GET, params="imprime")
    public ModelAndView imprimeCoordinador(HttpServletRequest request, HttpServletResponse response, Authentication authentication, String cve_m_usuario, String fechaInicial, String fechaFinal) {
        
    	return new ModelAndView();
    	
    }
    //TERMINA COORDINADOR
    
  //DIRECCIÓN
    @RequestMapping(value={"direccion"}, method = RequestMethod.GET)
    public String buscaListaAsistenciaDireccion(Model model) {
    	
    	model.addAttribute("listaAsistencia", new ArrayList<Asistencia>());
    	model.addAttribute("inicio", true); //permite controlar en el front que una etiqueta se va a esconder cuando es el "inicio"
    	
    	return "/asistencia/direccion";
    }
    
    @RequestMapping(value={"direccion/busca"}, method = RequestMethod.GET, params="busca")
    public String buscaListaAsistenciaDireccionRango(Model model, Authentication authentication, String cve_m_usuario, String nombre, String paterno, 
    		String materno, String nivel, String tipo, String estado, String fechaInicial, String fechaFinal, String unidadAdministrativa) {

	    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoDireccion(cve_m_usuario, nombre, paterno, materno, nivel, 
	    			tipo, estado, fechaInicial, fechaFinal, unidadAdministrativa);
	    	
	    	model.addAttribute("listaAsistencia", asistencia);
	    	model.addAttribute("fechaInicial", fechaInicial);
	    	model.addAttribute("fechaFinal", fechaFinal);
	    	model.addAttribute("cve_m_usuario", cve_m_usuario);
	    	model.addAttribute("nombre", nombre);
	    	model.addAttribute("paterno", paterno);
	    	model.addAttribute("materno", materno);
	    	model.addAttribute("nivel", nivel);
	    	model.addAttribute("tipo", tipo);
	    	model.addAttribute("estado", estado);
	    	model.addAttribute("unidadAdministrativa", unidadAdministrativa);
	    	
	    	return "/asistencia/direccion";
    }
    
    @RequestMapping(value="direccion/busca", method=RequestMethod.GET, params="exporta")
    public ModelAndView exportaExcelDireccion(HttpServletRequest request, HttpServletResponse response, Authentication authentication, 
    		String cve_m_usuario, String nombre, String paterno, String materno, String nivel, String tipo, String estado, String fechaInicial, String fechaFinal, String unidadAdministrativa) {
        
    	if (!cve_m_usuario.isEmpty() && !fechaInicial.isEmpty() && !fechaFinal.isEmpty()) {
	    	List<Asistencia> listaAsistencia = asistenciaService.buscaAsistenciaEmpleadoRangoDireccion(cve_m_usuario, nombre, paterno, materno, nivel, 
	    			tipo, estado, fechaInicial, fechaFinal, unidadAdministrativa);
	    	
	    	Map<String, Object> model = new HashMap<String, Object>();
	        
	    	//nombre de la hoja
	        model.put("nombreHoja", "asistencia");
	        
	        //nombres columnas
	        List<String> cabeceras = new ArrayList<String>();
	        cabeceras.add("Id Asistencia");
	        cabeceras.add("Entrada");
	        cabeceras.add("Id Status");
	        cabeceras.add("Id Tipo");
	        cabeceras.add("Salida");
	        cabeceras.add("Usuario");
	        model.put("cabeceras", cabeceras);
	        
	        //Información registros (List<Object[]>)
	        List<List<String>> asistencias = new ArrayList<List<String>>();
	        
	        for (Asistencia a : listaAsistencia) {
	        	
	        	List<String> elementos = new ArrayList<>();
	        	elementos.add(a.getIdAsistencia() != null ? a.getIdAsistencia().toString() : new String(""));
	        	elementos.add(a.getEntrada().toString());
	        	elementos.add(a.getIdEstatus().getEstatus() != null ? a.getIdEstatus().getEstatus() : new String(""));
	        	elementos.add(a.getIdTipoDia().getNombre() != null ? a.getIdTipoDia().getNombre() : new String(""));
	        	elementos.add(a.getSalida() != null ? a.getSalida().toString() : new String(""));
	        	elementos.add(a.getUsuarioDto() != null ? a.getUsuarioDto().getClaveUsuario() : new String(""));
	        	asistencias.add(elementos);
	        }
	        
	        model.put("registros", asistencias);
	        
	        response.setContentType( "application/ms-excel" );
	        response.setHeader( "Content-disposition", "attachment; filename=SESNSP.xls" );         
	        
	        return new ModelAndView(new Excel(), model);
    	}
    	
    	return new ModelAndView();
    	
    }
    
    @RequestMapping(value="direccion/busca", method=RequestMethod.GET, params="imprime")
    public ModelAndView imprimeDireccion(HttpServletRequest request, HttpServletResponse response, Authentication authentication, String cve_m_usuario, String fechaInicial, String fechaFinal) {
        
    	return new ModelAndView();
    	
    }
    
    @RequestMapping(value={"direccion/dictaminaIncidencia"}, method = RequestMethod.POST)
    public String dictaminaIncidencia(Model model, String cve_m_usuario_hidden, Integer idAsistenciaHidden, Integer idTipoDia, Integer idJustificacion, String fechaInicial, String fechaFinal, String dictaminacion) {

    	asistenciaService.dictaminaIncidencia(idAsistenciaHidden, idTipoDia, idJustificacion, dictaminacion);
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRango(cve_m_usuario_hidden, fechaInicial, fechaFinal);
    	
    	model.addAttribute("listaAsistencia", asistencia);
    	model.addAttribute("inicio", true); //permite controlar en el front que una etiqueta se va a esconder cuando es el "inicio"
    	model.addAttribute("fechaInicial", fechaInicial);
    	model.addAttribute("fechaFinal", fechaFinal);
    	model.addAttribute("cve_m_usuario", cve_m_usuario_hidden);
    	
    	return "/asistencia/direccion";
    }
    
    @GetMapping("buscaId")
    @ResponseBody
    public AsistenciaJustificacion buscaAsistenciaPorId(Integer id) {
    	
    	AsistenciaJustificacion asistenciaJustificacion = new AsistenciaJustificacion(); 
    	asistenciaJustificacion.setAsistencia(asistenciaService.buscaAsistenciaPorId(id));
    	asistenciaJustificacion.setListaJustificacion(catalogoService.obtieneJustificaciones());
    	
    	return asistenciaJustificacion;
    }
    
    @RequestMapping(value={"creaIncidencia"}, method = RequestMethod.POST)
    public String creaIncidencia(Model model, String cve_m_usuario_hidden, Integer idAsistenciaHidden, Integer idTipoDia, Integer idJustificacion, String fechaInicial, String fechaFinal, MultipartFile archivo) {
    	Integer idArchivo = null;
    	
    	try {
    		//guarda el archivo
    		if (archivo.getSize() > 0) {
    			idArchivo = archivoService.guardaArchivo(archivo, cve_m_usuario_hidden, new String("asistencia"));
    		}
    		
    		//crea la incidencia y asocia el archivo
    		asistenciaService.creaIncidencia(idAsistenciaHidden, idTipoDia, idJustificacion, idArchivo);

    	} catch(Exception e) {
    		new Exception("No se logró crear la incidencia " + e.getMessage());
    	}
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRango(cve_m_usuario_hidden, fechaInicial, fechaFinal);
    	
    	model.addAttribute("listaAsistencia", asistencia);
    	model.addAttribute("inicio", true); //permite controlar en el front que una etiqueta se va a esconder cuando es el "inicio"
    	model.addAttribute("fechaInicial", fechaInicial);
    	model.addAttribute("fechaFinal", fechaFinal);
    	model.addAttribute("cve_m_usuario", cve_m_usuario_hidden);
    	
    	return "/asistencia/coordinador";
    }
    
}