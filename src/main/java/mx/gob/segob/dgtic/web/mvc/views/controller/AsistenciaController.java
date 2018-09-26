package mx.gob.segob.dgtic.web.mvc.views.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.segob.dgtic.web.mvc.dto.Asistencia;
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
    
    @RequestMapping(value={"inicio"}, method = RequestMethod.GET)
    public String buscaAsistenciaEmpleado(Model model, Authentication authentication) {
    	
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleado(authentication.getName());
    	model.addAttribute("listaAsistencia", asistencia);

    	return "/asistencia/inicio";
    }
    
}