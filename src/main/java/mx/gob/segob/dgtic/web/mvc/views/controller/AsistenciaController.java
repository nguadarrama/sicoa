package mx.gob.segob.dgtic.web.mvc.views.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.gob.segob.dgtic.web.mvc.dto.Asistencia;
import mx.gob.segob.dgtic.web.mvc.dto.Horario;
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
    
    @RequestMapping(value={"inicio"}, method = RequestMethod.GET)
    public String buscaListaAsistenciaEmpleado(Model model, Authentication authentication) {
    	
    	model.addAttribute("listaAsistencia", new ArrayList<Asistencia>());
    	model.addAttribute("inicio", true); //permite controlar en el front que una etiqueta se va a esconder cuando es el "inicio"

    	return "/asistencia/inicio";
    }
    
    @RequestMapping(value={"busca"}, method = RequestMethod.GET)
    public String buscaListaAsistenciaEmpleadoRango(Model model, Authentication authentication, String fechaInicial, String fechaFinal) {

    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRango(authentication.getName(), fechaInicial, fechaFinal);
    	model.addAttribute("listaAsistencia", asistencia);
    	model.addAttribute("fechaInicial", fechaInicial);
    	model.addAttribute("fechaFinal", fechaFinal);
    	
    	return "/asistencia/inicio";
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
    public String creaIncidencia(Model model, Authentication authentication, Integer id, Integer idTipoDia, Integer idJustificacion, String fechaInicial, String fechaFinal) {

    	asistenciaService.creaIncidencia(id, idTipoDia, idJustificacion );
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRango(authentication.getName(), fechaInicial, fechaFinal);
    	
    	model.addAttribute("listaAsistencia", asistencia);
    	model.addAttribute("inicio", true); //permite controlar en el front que una etiqueta se va a esconder cuando es el "inicio"
    	model.addAttribute("fechaInicial", fechaInicial);
    	model.addAttribute("fechaFinal", fechaFinal);
    	
    	return "/asistencia/inicio";
    }
    
}