package mx.gob.segob.dgtic.web.mvc.views.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import mx.gob.segob.dgtic.web.mvc.dto.Asistencia;
import mx.gob.segob.dgtic.web.mvc.service.AsistenciaService;
import mx.gob.segob.dgtic.web.mvc.service.CatalogoService;
import mx.gob.segob.dgtic.web.mvc.service.EstatusService;
import mx.gob.segob.dgtic.web.mvc.util.Excel;

/**
 * Controller donde se registran las vistas de reportes
 * 
 * Path : {contextoAplicacion}/
 */
@Controller
@RequestMapping( value = "reportes")
public class ReporteController {
	
	@Autowired
	private AsistenciaService asistenciaService;
	
	@Autowired
	private CatalogoService catalogoService;
	
	@Autowired
	private EstatusService estatusService;
	
	@RequestMapping(value={"inicioDireccion"}, method = RequestMethod.GET)
    public String inicioReporteDireccion(Model model) {
		
		model.addAttribute("listaTipo", catalogoService.obtieneTipoDias());
		model.addAttribute("listaNivel", catalogoService.obtieneNiveles());
		model.addAttribute("listaEstado", estatusService.obtieneListaCompletaEstatus());
		model.addAttribute("listaAsistencia", new ArrayList<Asistencia>());
		model.addAttribute("inicio", true);
    	
    	return "/reportes/reportesDireccion";
    }
	
	@RequestMapping(value={"inicioCoordinador"}, method = RequestMethod.GET)
    public String inicioReporteCoordinador(Model model) {
		
		model.addAttribute("listaTipo", catalogoService.obtieneTipoDias());
		model.addAttribute("listaNivel", catalogoService.obtieneNiveles());
		model.addAttribute("listaEstado", estatusService.obtieneListaCompletaEstatus());
		model.addAttribute("listaAsistencia", new ArrayList<Asistencia>());
		model.addAttribute("inicio", true);
    	
    	return "/reportes/reportesCoordinador";
    }
	
	//DIRECCIÓN
	@RequestMapping(value={"reporteDireccion"}, method = RequestMethod.GET, params="busca")
    public String reporteDireccion(Model model, String cve_m_usuario, String nombre, String paterno, String materno, String nivel,
    		Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa, String[] permisos) {
		
		List<Asistencia> listaAsistencias = asistenciaService.buscaAsistenciaDireccionReporte(cve_m_usuario, nombre, paterno, materno, 
				nivel, tipo, estado, fechaInicial, fechaFinal, unidadAdministrativa, permisos);
		
		model.addAttribute("listaTipo", catalogoService.obtieneTipoDias());
		model.addAttribute("listaNivel", catalogoService.obtieneNiveles());
		model.addAttribute("listaEstado", estatusService.obtieneListaCompletaEstatus());
		model.addAttribute("listaAsistencia", listaAsistencias);
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
    	
    	if (permisos != null) {
    		List<String> listaPermisos = new ArrayList<String>(Arrays.asList(permisos));
    		
    		if (listaPermisos.contains("vacacion")) {
    			model.addAttribute("vacacion", true);
    		}
    		
    		if (listaPermisos.contains("comision")) {
    			model.addAttribute("comision", true);
    		}
    		
    		if (listaPermisos.contains("licencia")) {
    			model.addAttribute("licencia", true);
    		}
    		
    		if (listaPermisos.contains("descuento")) {
    			model.addAttribute("descuento", true);
    		}
    	}
    	
    	return "/reportes/reportesDireccion";
    }
	
	@RequestMapping(value={"reporteDireccion"}, method = RequestMethod.GET, params="exporta")
    public ModelAndView exportaReporteDireccion(Model model, String cve_m_usuario, String nombre, String paterno, String materno, String nivel,
    		Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa, String[] permisos, 
    		HttpServletResponse response) {
		
		List<Asistencia> listaAsistencias = asistenciaService.buscaAsistenciaDireccionReporte(cve_m_usuario, nombre, paterno, materno, 
				nivel, tipo, estado, fechaInicial, fechaFinal, unidadAdministrativa, permisos);
    	
    	Map<String, Object> map = new HashMap<String, Object>();
        
    	//nombre de la hoja
    	map.put("nombreHoja", "Reporte");
        
        //nombres columnas
        List<String> cabeceras = new ArrayList<String>();
        cabeceras.add("No. Empleado");
        cabeceras.add("Nombre");
        cabeceras.add("Unidad Administrativa");
        cabeceras.add("Nivel/Puesto");
        cabeceras.add("Estado");
        map.put("cabeceras", cabeceras);
        
        //Información registros (List<Object[]>)
        List<List<String>> asistencias = new ArrayList<List<String>>();
        
        for (Asistencia a : listaAsistencias) {
        	
        	List<String> elementos = new ArrayList<>();
        	elementos.add(a.getUsuarioDto().getClaveUsuario());
        	elementos.add(a.getUsuarioDto().getNombre() + " " + a.getUsuarioDto().getApellidoPaterno() + " " + a.getUsuarioDto().getApellidoMaterno());
        	elementos.add(a.getUsuarioDto().getNombreUnidad());
        	elementos.add(a.getUsuarioDto().getNivel() + "/" + a.getUsuarioDto().getClavePerfil().getDescripcion());
			
			String estatus = a.getIdEstatus().getEstatus();
			
			if (estatus != null) {
				if (estatus.equals("Validada") && a.getIncidencia().getDescuento()) {
					estatus = "Descuento Aprobado";
				} else if (estatus.equals("Validada") && !a.getIncidencia().getDescuento()) {
					estatus = "Justificación Aprobada";
				} else if (estatus.equals("Pendiente") && a.getIncidencia().getDescuento()) {
					estatus = "Descuento Pendiente";
				} else if (estatus.equals("Pendiente") && !a.getIncidencia().getDescuento()) {
					estatus = "Justificación Pendiente";
				} else if (estatus.equals("Rechazada") && a.getIncidencia().getDescuento()) {
					estatus = "Descuento Rechazado";
				} else if (estatus.equals("Rechazada") && !a.getIncidencia().getDescuento()) {
					estatus = "Justificación Rechazada";
				} 
			}
        	
        	elementos.add(estatus);
        	asistencias.add(elementos);
        }
        
        map.put("registros", asistencias);
        
        response.setContentType( "application/ms-excel" );
        response.setHeader( "Content-disposition", "attachment; filename=ReporteSESNSP.xls" );         
        
        return new ModelAndView(new Excel(), map);
    }
	//TERMINA DIRECCION
	
	//COORDINADOR
	@RequestMapping(value={"reporteCoordinador"}, method = RequestMethod.GET, params="busca")
    public String reporteCoordinador(Model model, String cve_m_usuario, String nombre, String paterno, String materno, String nivel,
    		Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa, String[] permisos, Authentication authentication) {
		
		List<Asistencia> listaAsistencias = asistenciaService.buscaAsistenciaCoordinadorReporte(cve_m_usuario, nombre, paterno, materno, 
				nivel, tipo, estado, fechaInicial, fechaFinal, unidadAdministrativa, authentication.getName(), permisos);
		
		model.addAttribute("listaTipo", catalogoService.obtieneTipoDias());
		model.addAttribute("listaNivel", catalogoService.obtieneNiveles());
		model.addAttribute("listaEstado", estatusService.obtieneListaCompletaEstatus());
		model.addAttribute("listaAsistencia", listaAsistencias);
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
    	
    	if (permisos != null) {
    		List<String> listaPermisos = new ArrayList<String>(Arrays.asList(permisos));
    		
    		if (listaPermisos.contains("vacacion")) {
    			model.addAttribute("vacacion", true);
    		}
    		
    		if (listaPermisos.contains("comision")) {
    			model.addAttribute("comision", true);
    		}
    		
    		if (listaPermisos.contains("licencia")) {
    			model.addAttribute("licencia", true);
    		}
    		
    		if (listaPermisos.contains("descuento")) {
    			model.addAttribute("descuento", true);
    		}
    	}
    	
    	return "/reportes/reportesCoordinador";
    }
	
	@RequestMapping(value={"reporteCoordinador"}, method = RequestMethod.GET, params="exporta")
    public ModelAndView exportaReporteCoordinador(Model model, String cve_m_usuario, String nombre, String paterno, String materno, String nivel,
    		Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa, String[] permisos, 
    		HttpServletResponse response, Authentication authentication) {
		
		List<Asistencia> listaAsistencias = asistenciaService.buscaAsistenciaCoordinadorReporte(cve_m_usuario, nombre, paterno, materno, 
				nivel, tipo, estado, fechaInicial, fechaFinal, unidadAdministrativa, authentication.getName(), permisos);
    	
    	Map<String, Object> map = new HashMap<String, Object>();
        
    	//nombre de la hoja
    	map.put("nombreHoja", "Reporte");
        
        //nombres columnas
        List<String> cabeceras = new ArrayList<String>();
        cabeceras.add("No. Empleado");
        cabeceras.add("Nombre");
        cabeceras.add("Unidad Administrativa");
        cabeceras.add("Nivel/Puesto");
        cabeceras.add("Estado");
        map.put("cabeceras", cabeceras);
        
        //Información registros (List<Object[]>)
        List<List<String>> asistencias = new ArrayList<List<String>>();
        
        for (Asistencia a : listaAsistencias) {
        	
        	List<String> elementos = new ArrayList<>();
        	elementos.add(a.getUsuarioDto().getClaveUsuario());
        	elementos.add(a.getUsuarioDto().getNombre() + " " + a.getUsuarioDto().getApellidoPaterno() + " " + a.getUsuarioDto().getApellidoMaterno());
        	elementos.add(a.getUsuarioDto().getNombreUnidad());
        	elementos.add(a.getUsuarioDto().getNivel() + "/" + a.getUsuarioDto().getClavePerfil().getDescripcion());
			
			String estatus = a.getIdEstatus().getEstatus();
			
			if (estatus != null) {
				if (estatus.equals("Validada") && a.getIncidencia().getDescuento()) {
					estatus = "Descuento Aprobado";
				} else if (estatus.equals("Validada") && !a.getIncidencia().getDescuento()) {
					estatus = "Justificación Aprobada";
				} else if (estatus.equals("Pendiente") && a.getIncidencia().getDescuento()) {
					estatus = "Descuento Pendiente";
				} else if (estatus.equals("Pendiente") && !a.getIncidencia().getDescuento()) {
					estatus = "Justificación Pendiente";
				} else if (estatus.equals("Rechazada") && a.getIncidencia().getDescuento()) {
					estatus = "Descuento Rechazado";
				} else if (estatus.equals("Rechazada") && !a.getIncidencia().getDescuento()) {
					estatus = "Justificación Rechazada";
				} 
			}
        	
        	elementos.add(estatus);
        	asistencias.add(elementos);
        }
        
        map.put("registros", asistencias);
        
        response.setContentType( "application/ms-excel" );
        response.setHeader( "Content-disposition", "attachment; filename=ReporteSESNSP.xls" );         
        
        return new ModelAndView(new Excel(), map);
    }
	//TERMINA COORDINADOR
	
}