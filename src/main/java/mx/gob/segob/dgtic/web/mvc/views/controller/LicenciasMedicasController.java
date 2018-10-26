package mx.gob.segob.dgtic.web.mvc.views.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.gob.segob.dgtic.web.mvc.dto.LicenciaMedica;
import mx.gob.segob.dgtic.web.mvc.dto.Periodo;
import mx.gob.segob.dgtic.web.mvc.service.EstatusService;
import mx.gob.segob.dgtic.web.mvc.service.LicenciaMedicaService;
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
	
	@RequestMapping(value={"licenciasPropias"}, method = RequestMethod.GET)
    public String obtieneLicencias(String fechaInicio, String fechaFin, String idEstatus, Model model, HttpSession session) {
		String string=""+ session.getAttribute("usuario");
    	String[] parts = string.split(": ");
    	String claveUsuario = parts[1];
    	Periodo periodo= new Periodo();
		System.out.println("Datos claveUsuario "+ claveUsuario +" fechaInicio "+fechaInicio+" fechaFin "+fechaFin+" idEstatus "+idEstatus);
		
		if(fechaInicio==null || fechaInicio.trim().isEmpty()){
			fechaInicio="";
		}
		if(fechaFin==null || fechaFin.trim().isEmpty()){
			fechaFin="";
		}
		List<LicenciaMedica> lista= new ArrayList<>();
		lista=licenciaMedicaService.obtenerListaLicenciaMedicaPorFiltros(claveUsuario, fechaInicio, fechaFin, idEstatus);
		System.out.println("Haciendo la consulta "+lista.size());
		model.addAttribute("licenciasMedicas",lista);
    	return "/licenciasMedicas/licenciasPropias"; 
    }
	
	@RequestMapping(value={"agregaSolicitudLicencia"}, method = RequestMethod.POST)
    public String agregaLicencias(String claveUsuario, String idResponsable, String fechaInicio, String fechaFin,
    		Integer dias, String padecimiento, Model model, HttpSession session) {
		System.out.println("Datos claveUsuario "+claveUsuario+" responsable "+idResponsable +" fechaInicio "+fechaInicio
				+" fechaFin "+fechaFin+" dias "+dias+" padecimiento "+padecimiento);
		Integer idResponsableAux=null;
		if(idResponsable!=null && !idResponsable.trim().isEmpty()){
			idResponsableAux=Integer.parseInt(idResponsable);
		}
		//licenciaMedicaService.AgregaLicenciaMedica(new LicenciaMedica (null,null,idResponsableAux,null,null,fechaInicio,fechaFin,dias,padecimiento), claveUsuario);
		List<LicenciaMedica> lista= new ArrayList<>();
		//System.out.println("Haciendo la consulta ");
    	return "/licenciasMedicas/solicitudLicencia";
    	 
    }
	
	@RequestMapping(value={"solicitudLicencia"}, method = RequestMethod.GET)
    public String solicitudLicencia(Model model, HttpSession session) {
		
		
		//licenciaMedicaService.AgregaLicenciaMedica(new LicenciaMedica (null,null,idResponsableAux,null,null,fechaInicio,fechaFin,dias,padecimiento), claveUsuario);
		List<LicenciaMedica> lista= new ArrayList<>();
		//System.out.println("Haciendo la consulta ");
		String string=""+ session.getAttribute("usuario");
    	String[] parts = string.split(": ");
    	String claveUsuario = parts[1];
		model.addAttribute("usuario",usuarioService.buscaUsuario(claveUsuario));
		System.out.println("recuperando datos "+claveUsuario);
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
		if(idUnidad==null || idUnidad.trim().isEmpty()){
			idUnidad="";
		}
		
		List<LicenciaMedica> lista= new ArrayList<>();
		lista=licenciaMedicaService.obtenerListaLicenciaMedicaEmpleados(claveUsuario, nombre, apellidoPaterno, apellidoMaterno, idEstatus, idUnidad);
		System.out.println("Haciendo la consulta "+lista.size());
		model.addAttribute("licenciasMedicas",lista);
		model.addAttribute("listaUnidades",unidadAdministrativaService.obtenerUnidadesAdministrativas());
		model.addAttribute("listaEstatus",estatusService.obtieneListaEstatus());
    	return "/licenciasMedicas/licenciasEmpleados"; 
    }
	
	//@RequestMapping(value={"busca"}, method = RequestMethod.POST)
	@GetMapping("/busca")
	@ResponseBody
    public HashMap<String, Object>  obtieneLicenciasDeEmpleado(Integer idLicencia) {
		HashMap<String, Object> hmap = new HashMap<String, Object>();
		//List<LicenciaMedica> lista= new ArrayList<>();
		//lista=licenciaMedicaService.obtenerListaLicenciaMedicaEmpleados(claveUsuario, nombre, apellidoPaterno, apellidoMaterno, idEstatus, idUnidad);
		//System.out.println("Haciendo la consulta "+lista.size());
		//model.addAttribute("licenciasMedicas",lista);
		//model.addAttribute("listaUnidades",unidadAdministrativaService.obtenerUnidadesAdministrativas());
		//model.addAttribute("listaEstatus",estatusService.obtieneListaEstatus());
		hmap.put("licencia",licenciaMedicaService.buscaLicenciaMedica(idLicencia));
    	return hmap;
    }

}
