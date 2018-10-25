package mx.gob.segob.dgtic.web.mvc.views.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.segob.dgtic.web.mvc.dto.LicenciaMedica;
import mx.gob.segob.dgtic.web.mvc.service.EstatusService;
import mx.gob.segob.dgtic.web.mvc.service.LicenciaMedicaService;
import mx.gob.segob.dgtic.web.mvc.service.UnidadAdministrativaService;

@Controller
@RequestMapping( value = "licenciasMedicas", method = RequestMethod.GET)
public class LicenciasMedicasController {
	
	@Autowired 
	private LicenciaMedicaService licenciaMedicaService;
	
	@Autowired 
	private UnidadAdministrativaService unidadAdministrativaService;
	
	@Autowired EstatusService estatusService;
	
	@RequestMapping(value={"licenciasPropias"}, method = RequestMethod.GET)
    public String obtieneLicencias(String claveUsuario, String fechaInicio, String fechaFin, String idEstatus, Model model, HttpSession session) {
		
		List<LicenciaMedica> lista= new ArrayList<>();
		lista=licenciaMedicaService.obtenerListaLicenciaMedicaPorFiltros(claveUsuario, fechaInicio, fechaFin, idEstatus);
		System.out.println("Haciendo la consulta "+lista.size());
		model.addAttribute("licenciasMedicas",lista);
    	return "/licenciasMedicas/licenciasPropias"; 
    }
	
	@RequestMapping(value={"solicitudLicencia"}, method = RequestMethod.GET)
    public String agregaLicencias(Model model, HttpSession session) {
		
		List<LicenciaMedica> lista= new ArrayList<>();
		System.out.println("Haciendo la consulta ");
    	return "/licenciasMedicas/solicitudLicencia";
    	 
    }
	
	@RequestMapping(value={"solicitudLicenciaEmpleados"}, method = RequestMethod.GET)
    public String agregaLicenciaEmpleados(Model model, HttpSession session) {
		
		List<LicenciaMedica> lista= new ArrayList<>();
		System.out.println("Haciendo la consulta ");
    	return "/licenciasMedicas/solicitudLicenciaEmpleados";
    	 
    }
	
	@RequestMapping(value={"licenciasEmpleados"}, method = RequestMethod.GET)
    public String obtieneLicenciasPropias(String claveUsuario, String nombre, String apellidoPaterno, String apellidoMaterno, 
    		String idEstatus, String idUnidad, Model model, HttpSession session) {
		
		List<LicenciaMedica> lista= new ArrayList<>();
		lista=licenciaMedicaService.obtenerListaLicenciaMedicaEmpleados(claveUsuario, nombre, apellidoPaterno, apellidoMaterno, idEstatus, idUnidad);
		System.out.println("Haciendo la consulta "+lista.size());
		model.addAttribute("licenciasMedicas",lista);
		model.addAttribute("listaUnidades",unidadAdministrativaService.obtenerUnidadesAdministrativas());
		model.addAttribute("listaEstatus",estatusService.obtieneListaEstatus());
    	return "/licenciasMedicas/licenciasEmpleados"; 
    }

}
