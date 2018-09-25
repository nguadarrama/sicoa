package mx.gob.segob.dgtic.web.mvc.views.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.segob.dgtic.web.mvc.service.AsistenciaService;

/**
 * Controller donde se registran las vistas de asistencia
 * 
 * Path : {contextoAplicacion}/
 */
@Controller
@RequestMapping( value = "asistencia")
public class AsistenciaController {	
	
	@Autowired
	private AsistenciaService asistenciaService;
	
	/**
     * Vista donde se ubica la vista de asistencias. Path : {contextoAplicacion}/asistencia/inicio
     * @return La vista de asistencia
     */
    @RequestMapping(value={"inicio"}, method = RequestMethod.GET)
    public String obtieneAsistencias(Model model) {
	    
	    model.addAttribute("listaAsistencia", asistenciaService.obtieneAsistencia());
	    
    	return "/asistencia/inicio";
    }
    
    /**
     * Vista donde se ubica la vista de búsqueda de asistencia. Path : {contextoAplicacion}/asistencia/busca
     * @return La vista de asistencia
     */
    @RequestMapping(value={"busca"}, method = RequestMethod.GET)
    public String buscaAsistencia(Model model) {
	    
	    model.addAttribute("listaAsistencia", asistenciaService.obtieneAsistencia());
	    
    	return "/asistencia/busca";
    }
    
}