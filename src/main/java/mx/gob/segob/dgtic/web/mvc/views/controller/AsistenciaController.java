package mx.gob.segob.dgtic.web.mvc.views.controller;


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
    
    @RequestMapping(value={"inicio"}, method = RequestMethod.GET)
    public String buscaListaAsistenciaEmpleado(Model model, Authentication authentication) {
    	
//    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoMes(authentication.getName());
//    	model.addAttribute("listaAsistencia", asistencia);

    	return "/asistencia/inicio";
    }
    
    @RequestMapping(value={"busca"}, method = RequestMethod.GET)
    public String buscaAsistenciaEmpleado(Model model, Authentication authentication, String fechaInicial, String fechaFinal) {

    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRango(authentication.getName(), fechaInicial, fechaFinal);
    	model.addAttribute("listaAsistencia", asistencia);
    	
    	return "/asistencia/inicio";
    }
    
    @GetMapping("buscaId")
    @ResponseBody
    public Asistencia buscaAsistenciaPorId(Integer id) {
    	
    	Asistencia asistencia = asistenciaService.buscaAsistenciaPorId(id);
    	
    	return asistencia;
    }
    
    @RequestMapping(value={"justifica"}, method = RequestMethod.POST)
    public String justificaAsistencia(Model model, Authentication authentication, String id, String observacion) {

    	asistenciaService.creaJustificacion(observacion);
    	
    	return "/asistencia/inicio";
    }
    
}