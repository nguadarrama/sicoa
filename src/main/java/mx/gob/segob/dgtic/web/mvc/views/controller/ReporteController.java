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
import mx.gob.segob.dgtic.web.mvc.dto.Estatus;
import mx.gob.segob.dgtic.web.mvc.service.AsistenciaService;
import mx.gob.segob.dgtic.web.mvc.service.CatalogoService;
import mx.gob.segob.dgtic.web.mvc.service.EstatusService;
import mx.gob.segob.dgtic.web.mvc.service.UnidadAdministrativaService;
import mx.gob.segob.dgtic.web.mvc.util.AsistenciaBusquedaUtil;
import mx.gob.segob.dgtic.web.mvc.util.Excel;
import mx.gob.segob.dgtic.web.mvc.views.controller.constants.ConstantsController;

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
	private static final String VALIDADA = "Validada";
	private static final String PENDIENTE = "Pendiente";
	private static final String RECHAZADA = "Rechazada";
	
	@RequestMapping(value={"inicioDireccion"}, method = RequestMethod.GET)
    public String inicioReporteDireccion(Model model, Authentication authentication) {
		
		List<Estatus> estatus =  estatusService.obtieneListaCompletaEstatus(authentication);
    	List<Estatus> listaEstado = new ArrayList<>();
    	
    	for (Estatus e : estatus) {
    		if (e.getIdEstatus() != 4 && e.getIdEstatus() != 5) {
    			listaEstado.add(e);
    		}
    	}

		model.addAttribute(ConstantsController.LISTA_TIPO, catalogoService.obtieneTipoDias(authentication));
		model.addAttribute(ConstantsController.LISTA_NIVEL, catalogoService.obtieneNiveles(authentication));
		model.addAttribute(ConstantsController.LISTA_ESTADO, listaEstado);
		model.addAttribute(ConstantsController.LISTA_UNIDAD_ADMINISTRATIVA, unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
		model.addAttribute(ConstantsController.LISTA_ASISTENCIA, new ArrayList<Asistencia>());
		model.addAttribute("inicio", true);
    	
    	return "/reportes/reportesDireccion";
    }
	
	@RequestMapping(value={"inicioCoordinador"}, method = RequestMethod.GET)
    public String inicioReporteCoordinador(Model model, Authentication authentication) {
		
		List<Estatus> estatus =  estatusService.obtieneListaCompletaEstatus(authentication);
    	List<Estatus> listaEstado = new ArrayList<>();
    	
    	for (Estatus e : estatus) {
    		if (e.getIdEstatus() != 4 && e.getIdEstatus() != 5) {
    			listaEstado.add(e);
    		}
    	}

		model.addAttribute(ConstantsController.LISTA_TIPO, catalogoService.obtieneTipoDias(authentication));
		model.addAttribute(ConstantsController.LISTA_NIVEL, catalogoService.obtieneNiveles(authentication));
		model.addAttribute(ConstantsController.LISTA_ESTADO, listaEstado);
		model.addAttribute(ConstantsController.LISTA_UNIDAD_ADMINISTRATIVA, unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
		model.addAttribute(ConstantsController.LISTA_ASISTENCIA, new ArrayList<Asistencia>());
		model.addAttribute("inicio", true);
    	
    	return "/reportes/reportesCoordinador";
    }
	
	//DIRECCIÓN
	@RequestMapping(value={"reporteDireccion"}, method = RequestMethod.GET, params="busca")
    public String reporteDireccion(Model model, AsistenciaBusquedaUtil asistenciaBusquedaUtil, Authentication authentication) {
		
		List<Asistencia> listaAsistencias = asistenciaService.buscaAsistenciaDireccionReporte(asistenciaBusquedaUtil, authentication);
		
		List<Estatus> estatus =  estatusService.obtieneListaCompletaEstatus(authentication);
    	List<Estatus> listaEstado = new ArrayList<>();
    	
    	for (Estatus e : estatus) {
    		if (e.getIdEstatus() != 4 && e.getIdEstatus() != 5) {
    			listaEstado.add(e);
    		}
    	}

		model.addAttribute(ConstantsController.LISTA_TIPO, catalogoService.obtieneTipoDias(authentication));
		model.addAttribute(ConstantsController.LISTA_NIVEL, catalogoService.obtieneNiveles(authentication));
		model.addAttribute(ConstantsController.LISTA_ESTADO, listaEstado);
		model.addAttribute(ConstantsController.LISTA_UNIDAD_ADMINISTRATIVA, unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
		model.addAttribute(ConstantsController.LISTA_ASISTENCIA, listaAsistencias);
		model.addAttribute("fechaInicial", asistenciaBusquedaUtil.getFechaInicial());
    	model.addAttribute("fechaFinal", asistenciaBusquedaUtil.getFechaFinal());
    	model.addAttribute(ConstantsController.CVE_M_USUARIO, asistenciaBusquedaUtil.getCveMusuario());
    	model.addAttribute("nombre", asistenciaBusquedaUtil.getNombre());
    	model.addAttribute("paterno", asistenciaBusquedaUtil.getPaterno());
    	model.addAttribute("materno", asistenciaBusquedaUtil.getMaterno());
    	model.addAttribute("nivel", asistenciaBusquedaUtil.getNivel());
    	model.addAttribute("tipo", asistenciaBusquedaUtil.getTipo());
    	model.addAttribute("estado", asistenciaBusquedaUtil.getEstado());
    	model.addAttribute("unidadAdministrativa", !asistenciaBusquedaUtil.getUnidadAdministrativa().isEmpty() ? Integer.parseInt(asistenciaBusquedaUtil.getUnidadAdministrativa()) : 0);
    	
    	Boolean p = false;
    	
    	if (asistenciaBusquedaUtil.getPermisos() != null) {
    		List<String> listaPermisos = new ArrayList<>(Arrays.asList(asistenciaBusquedaUtil.getPermisos()));
    		
    		if (listaPermisos.contains(ConstantsController.VACACION)) {
    			model.addAttribute(ConstantsController.VACACION, true);
    			p = true;
    		}
    		
    		if (listaPermisos.contains(ConstantsController.COMISION)) {
    			model.addAttribute(ConstantsController.COMISION, true);
    			p = true;
    		}
    		
    		if (listaPermisos.contains(ConstantsController.LICENCIA)) {
    			model.addAttribute(ConstantsController.LICENCIA, true);
    			p = true;
    		}
    		
    		if (listaPermisos.contains(ConstantsController.DESCUENTO)) {
    			model.addAttribute(ConstantsController.DESCUENTO, true);
    			p = true;
    		}
    	}
    	
    	model.addAttribute("hayPermiso", p);
    	
    	if (asistenciaBusquedaUtil.getTipo() != null) {
    		model.addAttribute("hayTipo", true);
    	}
    	
    	return "/reportes/reportesDireccion";
    }
	
	@RequestMapping(value={"reporteDireccion"}, method = RequestMethod.GET, params="exporta")
    public ModelAndView exportaReporteDireccion(Model model, AsistenciaBusquedaUtil asistenciaBusquedaUtil, 
    		HttpServletResponse response, Authentication authentication) {
			
		List<Asistencia> listaAsistencias = asistenciaService.buscaAsistenciaDireccionReporte(asistenciaBusquedaUtil, authentication);
		
    	Map<String, Object> map = new HashMap<>();
        
    	//nombre de la hoja
    	map.put("nombreHoja", "Reporte");
        
        //nombres columnas
        List<String> cabeceras = new ArrayList<>();
        cabeceras.add("Empleado");
        cabeceras.add("Nombre");
        cabeceras.add("Nivel");
        cabeceras.add("Unidad Administrativa");
        cabeceras.add("Tipo");
        cabeceras.add("Estado");
        
        map.put("cabeceras", cabeceras);
        
        //Información registros (List<Object[]>)
        List<List<String>> asistencias = new ArrayList<>();
        
        for (Asistencia a : listaAsistencias) {
        	String nombreCompleto = a.getUsuarioDto().getNombre() + " " + 
        							a.getUsuarioDto().getApellidoPaterno() + " " + 
        							a.getUsuarioDto().getApellidoMaterno();
        	
        	List<String> elementos = new ArrayList<>();
        	elementos.add(a.getUsuarioDto().getClaveUsuario());
        	elementos.add(nombreCompleto);
        	elementos.add(a.getUsuarioDto().getNivel());
        	elementos.add(a.getUsuarioDto().getNombreUnidad());
        	elementos.add(a.getIdTipoDia().getNombre());
			String estatus = a.getIdEstatus().getEstatus();
			
			//define status personalizados para los que no son permisos
			if (estatus != null && (a.getIdTipoDia().getIdTipoDia() != 5 && a.getIdTipoDia().getIdTipoDia() != 6 && a.getIdTipoDia().getIdTipoDia() != 7)) {
				estatus = defineEstatusPersonalizado(estatus, a);
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
    public String reporteCoordinador(Model model, AsistenciaBusquedaUtil asistenciaBusquedaUtil, Authentication authentication) {
		
		asistenciaBusquedaUtil.setCveUsuarioLogeado(authentication.getName());
		
		List<Asistencia> listaAsistencias = asistenciaService.buscaAsistenciaCoordinadorReporte(asistenciaBusquedaUtil, authentication);

		List<Estatus> estatus =  estatusService.obtieneListaCompletaEstatus(authentication);
    	List<Estatus> listaEstado = new ArrayList<>();
    	
    	for (Estatus e : estatus) {
    		if (e.getIdEstatus() != 4 && e.getIdEstatus() != 5) {
    			listaEstado.add(e);
    		}
    	}
		
		model.addAttribute(ConstantsController.LISTA_TIPO, catalogoService.obtieneTipoDias(authentication));
		model.addAttribute(ConstantsController.LISTA_NIVEL, catalogoService.obtieneNiveles(authentication));
		model.addAttribute(ConstantsController.LISTA_ESTADO, listaEstado);
		model.addAttribute(ConstantsController.LISTA_UNIDAD_ADMINISTRATIVA, unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
		model.addAttribute(ConstantsController.LISTA_ASISTENCIA, listaAsistencias);
		model.addAttribute("fechaInicial", asistenciaBusquedaUtil.getFechaInicial());
    	model.addAttribute("fechaFinal", asistenciaBusquedaUtil.getFechaFinal());
    	model.addAttribute(ConstantsController.CVE_M_USUARIO, asistenciaBusquedaUtil.getCveMusuario());
    	model.addAttribute("nombre", asistenciaBusquedaUtil.getNombre());
    	model.addAttribute("paterno", asistenciaBusquedaUtil.getPaterno());
    	model.addAttribute("materno", asistenciaBusquedaUtil.getMaterno());
    	model.addAttribute("nivel", asistenciaBusquedaUtil.getNivel());
    	model.addAttribute("tipo", asistenciaBusquedaUtil.getTipo());
    	model.addAttribute("estado", asistenciaBusquedaUtil.getEstado());
    	
    	Boolean p = false;
    	
    	if (asistenciaBusquedaUtil.getPermisos() != null) {
    		List<String> listaPermisos = new ArrayList<>(Arrays.asList(asistenciaBusquedaUtil.getPermisos()));
    		
    		if (listaPermisos.contains(ConstantsController.VACACION)) {
    			model.addAttribute(ConstantsController.VACACION, true);
    			p = true;
    		}
    		
    		if (listaPermisos.contains(ConstantsController.COMISION)) {
    			model.addAttribute(ConstantsController.COMISION, true);
    			p = true;
    		}
    		
    		if (listaPermisos.contains(ConstantsController.LICENCIA)) {
    			model.addAttribute(ConstantsController.LICENCIA, true);
    			p = true;
    		}
    		
    		if (listaPermisos.contains(ConstantsController.DESCUENTO)) {
    			model.addAttribute(ConstantsController.DESCUENTO, true);
    			p = true;
    		}
    	}
    	
    	model.addAttribute("hayPermiso", p);
    	
    	if (asistenciaBusquedaUtil.getTipo() != null) {
    		model.addAttribute("hayTipo", true);
    	}
    	
    	return "/reportes/reportesCoordinador";
    }
	
	@RequestMapping(value={"reporteCoordinador"}, method = RequestMethod.GET, params="exporta")
    public ModelAndView exportaReporteCoordinador(Model model, AsistenciaBusquedaUtil asistenciaBusquedaUtil, HttpServletResponse response, Authentication authentication) {
		
		asistenciaBusquedaUtil.setCveUsuarioLogeado(authentication.getName());
		
		List<Asistencia> listaAsistencias = asistenciaService.buscaAsistenciaCoordinadorReporte(asistenciaBusquedaUtil, authentication);
    	
    	Map<String, Object> map = new HashMap<>();
        
    	//nombre de la hoja
    	map.put("nombreHoja", "Reporte");
        
        //nombres columnas
        List<String> cabeceras = new ArrayList<>();
        cabeceras.add("Empleado");
        cabeceras.add("Nombre");
        cabeceras.add("Nivel");
        cabeceras.add("Unidad Administrativa");
        cabeceras.add("Tipo");
        cabeceras.add("Estado");
        
        map.put("cabeceras", cabeceras);
        
        //Información registros (List<Object[]>)
        List<List<String>> asistencias = new ArrayList<>();
        
        for (Asistencia a : listaAsistencias) {
        	String nombreCompleto = a.getUsuarioDto().getNombre() + " " + 
					a.getUsuarioDto().getApellidoPaterno() + " " + 
					a.getUsuarioDto().getApellidoMaterno();
        	
        	List<String> elementos = new ArrayList<>();
        	elementos.add(a.getUsuarioDto().getClaveUsuario());
        	elementos.add(nombreCompleto);
        	elementos.add(a.getUsuarioDto().getNivel());
        	elementos.add(a.getUsuarioDto().getNombreUnidad());
        	elementos.add(a.getIdTipoDia().getNombre());
			String estatus = a.getIdEstatus().getEstatus();

			//define status personalizados para los que no son permisos
			if (estatus != null && (a.getIdTipoDia().getIdTipoDia() != 5 && a.getIdTipoDia().getIdTipoDia() != 6 && a.getIdTipoDia().getIdTipoDia() != 7)) {
				estatus = defineEstatusPersonalizado(estatus, a);
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
	
	private String defineEstatusPersonalizado(String estatus, Asistencia a) {
		String nuevoEstatus = "";
		
		if (estatus.equals(VALIDADA) && a.getIncidencia().getDescuento()) {
			nuevoEstatus = "Descuento Aprobado";
		} else if (estatus.equals(VALIDADA) && !a.getIncidencia().getDescuento()) {
			nuevoEstatus = "Justificación Aprobada";
		} else if (estatus.equals(PENDIENTE) && a.getIncidencia().getDescuento()) {
			nuevoEstatus = "Descuento Pendiente";
		} else if (estatus.equals(PENDIENTE) && !a.getIncidencia().getDescuento()) {
			nuevoEstatus = "Justificación Pendiente";
		} else if (estatus.equals(RECHAZADA) && a.getIncidencia().getDescuento()) {
			nuevoEstatus = "Descuento Rechazado";
		} else if (estatus.equals(RECHAZADA) && !a.getIncidencia().getDescuento()) {
			nuevoEstatus = "Justificación Rechazada";
		}
		
		return nuevoEstatus;
	}
	
}