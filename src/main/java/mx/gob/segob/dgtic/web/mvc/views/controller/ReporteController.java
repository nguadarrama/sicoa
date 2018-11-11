package mx.gob.segob.dgtic.web.mvc.views.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import mx.gob.segob.dgtic.web.mvc.dto.UsuarioUnidadAdministrativa;
import mx.gob.segob.dgtic.web.mvc.service.AsistenciaService;
import mx.gob.segob.dgtic.web.mvc.service.CatalogoService;
import mx.gob.segob.dgtic.web.mvc.service.EstatusService;
import mx.gob.segob.dgtic.web.mvc.service.UnidadAdministrativaService;
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
	
	@Autowired
	private UnidadAdministrativaService unidadAdministrativaService;
	
	@RequestMapping(value={"inicioDireccion"}, method = RequestMethod.GET)
    public String inicioReporteDireccion(Model model, Authentication authentication) {
		
		model.addAttribute("listaTipo", catalogoService.obtieneTipoDias(authentication));
		model.addAttribute("listaNivel", catalogoService.obtieneNiveles(authentication));
		model.addAttribute("listaEstado", estatusService.obtieneListaCompletaEstatus(authentication));
		model.addAttribute("listaUnidadAdministrativa", unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
		model.addAttribute("listaAsistencia", new ArrayList<Asistencia>());
		model.addAttribute("inicio", true);
    	
    	return "/reportes/reportesDireccion";
    }
	
	@RequestMapping(value={"inicioCoordinador"}, method = RequestMethod.GET)
    public String inicioReporteCoordinador(Model model, Authentication authentication) {
		
		model.addAttribute("listaTipo", catalogoService.obtieneTipoDias(authentication));
		model.addAttribute("listaNivel", catalogoService.obtieneNiveles(authentication));
		model.addAttribute("listaEstado", estatusService.obtieneListaCompletaEstatus(authentication));
		model.addAttribute("listaUnidadAdministrativa", unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
		model.addAttribute("listaAsistencia", new ArrayList<Asistencia>());
		model.addAttribute("inicio", true);
    	
    	return "/reportes/reportesCoordinador";
    }
	
	//DIRECCIÓN
	@RequestMapping(value={"reporteDireccion"}, method = RequestMethod.GET, params="busca")
    public String reporteDireccion(Model model, String cve_m_usuario, String nombre, String paterno, String materno, String nivel,
    		Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa, String[] permisos, Authentication authentication) {
		
		List<Asistencia> listaAsistencias = asistenciaService.buscaAsistenciaDireccionReporte(cve_m_usuario, nombre, paterno, materno, 
				nivel, tipo, estado, fechaInicial, fechaFinal, unidadAdministrativa, permisos, authentication);
		
		model.addAttribute("listaTipo", catalogoService.obtieneTipoDias(authentication));
		model.addAttribute("listaNivel", catalogoService.obtieneNiveles(authentication));
		model.addAttribute("listaEstado", estatusService.obtieneListaCompletaEstatus(authentication));
		model.addAttribute("listaUnidadAdministrativa", unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
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
    	model.addAttribute("unidadAdministrativa", !unidadAdministrativa.isEmpty() ? Integer.parseInt(unidadAdministrativa) : 0);
    	
    	Boolean p = false;
    	
    	if (permisos != null) {
    		List<String> listaPermisos = new ArrayList<String>(Arrays.asList(permisos));
    		
    		if (listaPermisos.contains("vacacion")) {
    			model.addAttribute("vacacion", true);
    			p = true;
    		}
    		
    		if (listaPermisos.contains("comision")) {
    			model.addAttribute("comision", true);
    			p = true;
    		}
    		
    		if (listaPermisos.contains("licencia")) {
    			model.addAttribute("licencia", true);
    			p = true;
    		}
    		
    		if (listaPermisos.contains("descuento")) {
    			model.addAttribute("descuento", true);
    			p = true;
    		}
    	}
    	
    	model.addAttribute("hayPermiso", p);
    	
    	if (tipo != null) {
    		model.addAttribute("hayTipo", true);
    	}
    	
    	return "/reportes/reportesDireccion";
    }
	
	@RequestMapping(value={"reporteDireccion"}, method = RequestMethod.GET, params="exporta")
    public ModelAndView exportaReporteDireccion(Model model, String cve_m_usuario, String nombre, String paterno, String materno, String nivel,
    		Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa, String[] permisos, 
    		HttpServletResponse response, Authentication authentication) {
		
		List<Asistencia> listaAsistencias = asistenciaService.buscaAsistenciaDireccionReporte(cve_m_usuario, nombre, paterno, materno, 
				nivel, tipo, estado, fechaInicial, fechaFinal, unidadAdministrativa, permisos, authentication);
    	
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        
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
				nivel, tipo, estado, fechaInicial, fechaFinal, unidadAdministrativa, authentication.getName(), permisos, authentication);
		
		model.addAttribute("listaTipo", catalogoService.obtieneTipoDias(authentication));
		model.addAttribute("listaNivel", catalogoService.obtieneNiveles(authentication));
		model.addAttribute("listaEstado", estatusService.obtieneListaCompletaEstatus(authentication));
		model.addAttribute("listaUnidadAdministrativa", unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
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
    	
    	Boolean p = false;
    	
    	if (permisos != null) {
    		List<String> listaPermisos = new ArrayList<String>(Arrays.asList(permisos));
    		
    		if (listaPermisos.contains("vacacion")) {
    			model.addAttribute("vacacion", true);
    			p = true;
    		}
    		
    		if (listaPermisos.contains("comision")) {
    			model.addAttribute("comision", true);
    			p = true;
    		}
    		
    		if (listaPermisos.contains("licencia")) {
    			model.addAttribute("licencia", true);
    			p = true;
    		}
    		
    		if (listaPermisos.contains("descuento")) {
    			model.addAttribute("descuento", true);
    			p = true;
    		}
    	}
    	
    	model.addAttribute("hayPermiso", p);
    	
    	if (tipo != null) {
    		model.addAttribute("hayTipo", true);
    	}
    	
    	return "/reportes/reportesCoordinador";
    }
	
	@RequestMapping(value={"reporteCoordinador"}, method = RequestMethod.GET, params="exporta")
    public ModelAndView exportaReporteCoordinador(Model model, String cve_m_usuario, String nombre, String paterno, String materno, String nivel,
    		Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa, String[] permisos, 
    		HttpServletResponse response, Authentication authentication) {
		
		List<Asistencia> listaAsistencias = asistenciaService.buscaAsistenciaCoordinadorReporte(cve_m_usuario, nombre, paterno, materno, 
				nivel, tipo, estado, fechaInicial, fechaFinal, unidadAdministrativa, authentication.getName(), permisos, authentication);
    	
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
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