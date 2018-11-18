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
    public String reporteDireccion(Model model, String cveMusuario, String nombre, String paterno, String materno, String nivel,
    		Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa, String[] permisos, Authentication authentication) {
		
		AsistenciaBusquedaUtil asistenciaBusquedaUtil = new AsistenciaBusquedaUtil();
		
		asistenciaBusquedaUtil.setCveMusuario(cveMusuario);
		asistenciaBusquedaUtil.setNombre(nombre);
		asistenciaBusquedaUtil.setPaterno(paterno);
		asistenciaBusquedaUtil.setMaterno(materno);
		asistenciaBusquedaUtil.setNivel(nivel);
		asistenciaBusquedaUtil.setTipo(tipo);
		asistenciaBusquedaUtil.setEstado(estado);
		asistenciaBusquedaUtil.setFechaInicial(fechaInicial);
		asistenciaBusquedaUtil.setFechaFinal(fechaFinal);
		asistenciaBusquedaUtil.setUnidadAdministrativa(unidadAdministrativa);
		asistenciaBusquedaUtil.setP(permisos);
		
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
		model.addAttribute("fechaInicial", fechaInicial);
    	model.addAttribute("fechaFinal", fechaFinal);
    	model.addAttribute(ConstantsController.CVE_M_USUARIO, cveMusuario);
    	model.addAttribute("nombre", nombre);
    	model.addAttribute("paterno", paterno);
    	model.addAttribute("materno", materno);
    	model.addAttribute("nivel", nivel);
    	model.addAttribute("tipo", tipo);
    	model.addAttribute("estado", estado);
    	model.addAttribute("unidadAdministrativa", !unidadAdministrativa.isEmpty() ? Integer.parseInt(unidadAdministrativa) : 0);
    	
    	Boolean p = false;
    	
    	if (permisos != null) {
    		List<String> listaPermisos = new ArrayList<>(Arrays.asList(permisos));
    		
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
    	
    	if (tipo != null) {
    		model.addAttribute("hayTipo", true);
    	}
    	
    	return "/reportes/reportesDireccion";
    }
	
	@RequestMapping(value={"reporteDireccion"}, method = RequestMethod.GET, params="exporta")
    public ModelAndView exportaReporteDireccion(Model model, String cveMusuario, String nombre, String paterno, String materno, String nivel,
    		Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa, String[] permisos, 
    		HttpServletResponse response, Authentication authentication) {
		
		AsistenciaBusquedaUtil asistenciaBusquedaUtil = new AsistenciaBusquedaUtil();
		
		asistenciaBusquedaUtil.setCveMusuario(cveMusuario);
		asistenciaBusquedaUtil.setNombre(nombre);
		asistenciaBusquedaUtil.setPaterno(paterno);
		asistenciaBusquedaUtil.setMaterno(materno);
		asistenciaBusquedaUtil.setNivel(nivel);
		asistenciaBusquedaUtil.setTipo(tipo);
		asistenciaBusquedaUtil.setEstado(estado);
		asistenciaBusquedaUtil.setFechaInicial(fechaInicial);
		asistenciaBusquedaUtil.setFechaFinal(fechaFinal);
		asistenciaBusquedaUtil.setUnidadAdministrativa(unidadAdministrativa);
		asistenciaBusquedaUtil.setP(permisos);
		
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
			
			if (estatus != null) {
				if (a.getIdTipoDia().getIdTipoDia() != 5 && a.getIdTipoDia().getIdTipoDia() != 6 && a.getIdTipoDia().getIdTipoDia() != 7) {
					if (estatus.equals(VALIDADA) && a.getIncidencia().getDescuento()) {
						estatus = "Descuento Aprobado";
					} else if (estatus.equals(VALIDADA) && !a.getIncidencia().getDescuento()) {
						estatus = "Justificación Aprobada";
					} else if (estatus.equals(PENDIENTE) && a.getIncidencia().getDescuento()) {
						estatus = "Descuento Pendiente";
					} else if (estatus.equals(PENDIENTE) && !a.getIncidencia().getDescuento()) {
						estatus = "Justificación Pendiente";
					} else if (estatus.equals(RECHAZADA) && a.getIncidencia().getDescuento()) {
						estatus = "Descuento Rechazado";
					} else if (estatus.equals(RECHAZADA) && !a.getIncidencia().getDescuento()) {
						estatus = "Justificación Rechazada";
					}
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
    public String reporteCoordinador(Model model, String cveMUsuario, String nombre, String paterno, String materno, String nivel,
    		Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa, String[] permisos, Authentication authentication) {
		
		AsistenciaBusquedaUtil asistenciaBusquedaUtil = new AsistenciaBusquedaUtil();
		
		asistenciaBusquedaUtil.setCveMusuario(cveMUsuario);
		asistenciaBusquedaUtil.setNombre(nombre);
		asistenciaBusquedaUtil.setPaterno(paterno);
		asistenciaBusquedaUtil.setMaterno(materno);
		asistenciaBusquedaUtil.setNivel(nivel);
		asistenciaBusquedaUtil.setTipo(tipo);
		asistenciaBusquedaUtil.setEstado(estado);
		asistenciaBusquedaUtil.setFechaInicial(fechaInicial);
		asistenciaBusquedaUtil.setFechaFinal(fechaFinal);
		asistenciaBusquedaUtil.setUnidadAdministrativa(unidadAdministrativa);
		asistenciaBusquedaUtil.setP(permisos);
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
		model.addAttribute("fechaInicial", fechaInicial);
    	model.addAttribute("fechaFinal", fechaFinal);
    	model.addAttribute(ConstantsController.CVE_M_USUARIO, cveMUsuario);
    	model.addAttribute("nombre", nombre);
    	model.addAttribute("paterno", paterno);
    	model.addAttribute("materno", materno);
    	model.addAttribute("nivel", nivel);
    	model.addAttribute("tipo", tipo);
    	model.addAttribute("estado", estado);
    	
    	Boolean p = false;
    	
    	if (permisos != null) {
    		List<String> listaPermisos = new ArrayList<>(Arrays.asList(permisos));
    		
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
    	
    	if (tipo != null) {
    		model.addAttribute("hayTipo", true);
    	}
    	
    	return "/reportes/reportesCoordinador";
    }
	
	@RequestMapping(value={"reporteCoordinador"}, method = RequestMethod.GET, params="exporta")
    public ModelAndView exportaReporteCoordinador(Model model, String cveMUsuario, String nombre, String paterno, String materno, String nivel,
    		Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa, String[] permisos, 
    		HttpServletResponse response, Authentication authentication) {
		
		AsistenciaBusquedaUtil asistenciaBusquedaUtil = new AsistenciaBusquedaUtil();
		
		asistenciaBusquedaUtil.setCveMusuario(cveMUsuario);
		asistenciaBusquedaUtil.setNombre(nombre);
		asistenciaBusquedaUtil.setPaterno(paterno);
		asistenciaBusquedaUtil.setMaterno(materno);
		asistenciaBusquedaUtil.setNivel(nivel);
		asistenciaBusquedaUtil.setTipo(tipo);
		asistenciaBusquedaUtil.setEstado(estado);
		asistenciaBusquedaUtil.setFechaInicial(fechaInicial);
		asistenciaBusquedaUtil.setFechaFinal(fechaFinal);
		asistenciaBusquedaUtil.setUnidadAdministrativa(unidadAdministrativa);
		asistenciaBusquedaUtil.setP(permisos);
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
			
			if (estatus != null) {
				if (a.getIdTipoDia().getIdTipoDia() != 5 && a.getIdTipoDia().getIdTipoDia() != 6 && a.getIdTipoDia().getIdTipoDia() != 7) {
					if (estatus.equals(VALIDADA) && a.getIncidencia().getDescuento()) {
						estatus = "Descuento Aprobado";
					} else if (estatus.equals(VALIDADA) && !a.getIncidencia().getDescuento()) {
						estatus = "Justificación Aprobada";
					} else if (estatus.equals(PENDIENTE) && a.getIncidencia().getDescuento()) {
						estatus = "Descuento Pendiente";
					} else if (estatus.equals(PENDIENTE) && !a.getIncidencia().getDescuento()) {
						estatus = "Justificación Pendiente";
					} else if (estatus.equals(RECHAZADA) && a.getIncidencia().getDescuento()) {
						estatus = "Descuento Rechazado";
					} else if (estatus.equals(RECHAZADA) && !a.getIncidencia().getDescuento()) {
						estatus = "Justificación Rechazada";
					}
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