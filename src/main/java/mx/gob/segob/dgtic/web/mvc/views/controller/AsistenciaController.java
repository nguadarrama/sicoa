package mx.gob.segob.dgtic.web.mvc.views.controller;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import mx.gob.segob.dgtic.web.mvc.dto.Asistencia;
import mx.gob.segob.dgtic.web.mvc.dto.Horario;
import mx.gob.segob.dgtic.web.mvc.service.ArchivoService;
import mx.gob.segob.dgtic.web.mvc.service.AsistenciaService;
import mx.gob.segob.dgtic.web.mvc.service.CatalogoService;
import mx.gob.segob.dgtic.web.mvc.util.AsistenciaJustificacion;

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
    public String buscaListaAsistenciaEmpleado(Model model, Authentication authentication) {
    	
    	model.addAttribute("listaAsistencia", new ArrayList<Asistencia>());
    	model.addAttribute("inicio", true); //permite controlar en el front que una etiqueta se va a esconder cuando es el "inicio"
    	
    	return "/asistencia/empleado";
    }
	
	@RequestMapping(value={"empleado/busca"}, method = RequestMethod.GET)
    public String buscaListaAsistenciaEmpleadoRango(Model model, Authentication authentication, String fechaInicial, String fechaFinal) {

    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRango(authentication.getName(), fechaInicial, fechaFinal);
    	model.addAttribute("listaAsistencia", asistencia);
    	model.addAttribute("fechaInicial", fechaInicial);
    	model.addAttribute("fechaFinal", fechaFinal);
    	
    	return "/asistencia/empleado";
    }
	//TERMINA EMPLEADO
    
	//COORDINADOR
    @RequestMapping(value={"coordinador"}, method = RequestMethod.GET)
    public String buscaListaAsistenciaCoordinador(Model model, Authentication authentication) {
    	
    	model.addAttribute("listaAsistencia", new ArrayList<Asistencia>());
    	model.addAttribute("inicio", true); //permite controlar en el front que una etiqueta se va a esconder cuando es el "inicio"
    	
    	return "/asistencia/coordinador";
    }
    
    @RequestMapping(value={"coordinador/busca"}, method = RequestMethod.GET)
    public String buscaListaAsistenciaCoordinadorRango(Model model, String cve_m_usuario, String fechaInicial, String fechaFinal) {

    	if (!cve_m_usuario.isEmpty() && !fechaInicial.isEmpty() && !fechaFinal.isEmpty()) {
	    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRango(cve_m_usuario, fechaInicial, fechaFinal);
	    	model.addAttribute("listaAsistencia", asistencia);
	    	model.addAttribute("fechaInicial", fechaInicial);
	    	model.addAttribute("fechaFinal", fechaFinal);
	    	model.addAttribute("cve_m_usuario", cve_m_usuario);
	    	
	    	return "/asistencia/coordinador";
    	} else {
    		model.addAttribute("listaAsistencia", new ArrayList<Asistencia>());
	    	model.addAttribute("fechaInicial", fechaInicial);
	    	model.addAttribute("fechaFinal", fechaFinal);
    		
    		return "/asistencia/coordinador";
    	}
    }
    
  //DIRECCIÓN
    @RequestMapping(value={"direccion"}, method = RequestMethod.GET)
    public String buscaListaAsistenciaDireccion(Model model, Authentication authentication) {
    	
    	model.addAttribute("listaAsistencia", new ArrayList<Asistencia>());
    	model.addAttribute("inicio", true); //permite controlar en el front que una etiqueta se va a esconder cuando es el "inicio"
    	
    	return "/asistencia/direccion";
    }
    
    @RequestMapping(value={"direccion/busca"}, method = RequestMethod.GET)
    public String buscaListaAsistenciaDireccionRango(Model model, String cve_m_usuario, String fechaInicial, String fechaFinal) {

    	if (!cve_m_usuario.isEmpty() && !fechaInicial.isEmpty() && !fechaFinal.isEmpty()) {
	    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRango(cve_m_usuario, fechaInicial, fechaFinal);
	    	model.addAttribute("listaAsistencia", asistencia);
	    	model.addAttribute("fechaInicial", fechaInicial);
	    	model.addAttribute("fechaFinal", fechaFinal);
	    	model.addAttribute("cve_m_usuario", cve_m_usuario);
	    	
	    	return "/asistencia/direccion";
    	} else {
    		model.addAttribute("listaAsistencia", new ArrayList<Asistencia>());
	    	model.addAttribute("fechaInicial", fechaInicial);
	    	model.addAttribute("fechaFinal", fechaFinal);
    		
    		return "/asistencia/direccion";
    	}
    }
    
    @RequestMapping(value={"direccion/dictaminaIncidencia"}, method = RequestMethod.POST)
    public String dictaminaIncidencia(Model model, String cve_m_usuario_hidden, Integer id, Integer idTipoDia, Integer idJustificacion, String fechaInicial, String fechaFinal, String dictaminacion) {

    	asistenciaService.dictaminaIncidencia(id, idTipoDia, idJustificacion, dictaminacion);
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