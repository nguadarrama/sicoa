package mx.gob.segob.dgtic.web.mvc.views.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller donde se registran las vistas de reportes
 * 
 * Path : {contextoAplicacion}/
 */
@Controller
@RequestMapping( value = "reportes")
public class ReportesController  {	
	
	@RequestMapping(value={"coordinador"}, method = RequestMethod.GET)
    public String buscaListaAsistenciaEmpleado(Model model) {
    	
    	return "/reportes/reportes";
    }
	
}