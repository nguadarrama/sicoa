package mx.gob.segob.dgtic.web.mvc.views.controller;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import mx.gob.segob.dgtic.web.mvc.dto.Archivo;
import mx.gob.segob.dgtic.web.mvc.dto.Asistencia;
import mx.gob.segob.dgtic.web.mvc.dto.EstatusDto;
import mx.gob.segob.dgtic.web.mvc.dto.JustificacionDto;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;
import mx.gob.segob.dgtic.web.mvc.dto.reporte;
import mx.gob.segob.dgtic.web.mvc.service.ArchivoService;
import mx.gob.segob.dgtic.web.mvc.service.AsistenciaService;
import mx.gob.segob.dgtic.web.mvc.service.CatalogoService;
import mx.gob.segob.dgtic.web.mvc.service.EstatusService;
import mx.gob.segob.dgtic.web.mvc.service.UnidadAdministrativaService;
import mx.gob.segob.dgtic.web.mvc.service.UsuarioService;
import mx.gob.segob.dgtic.web.mvc.util.AsistenciaBusquedaUtil;
import mx.gob.segob.dgtic.web.mvc.util.AsistenciaJustificacion;
import mx.gob.segob.dgtic.web.mvc.util.Excel;
import mx.gob.segob.dgtic.web.mvc.util.FormatoIncidencia;
import mx.gob.segob.dgtic.web.mvc.views.controller.constants.ConstantsController;

/**
 * Controller donde se registran las vistas de asistencia
 * 
 * Path : {contextoAplicacion}/
 */
@Controller
@RequestMapping( value = ConstantsController.ASISTENCIA)
public class AsistenciaController  {	
	
	@Autowired
	private AsistenciaService asistenciaService;
	
	@Autowired
	private CatalogoService catalogoService;
	
	@Autowired
	private ArchivoService archivoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private EstatusService estatusService;
	
	@Autowired
	private UnidadAdministrativaService unidadAdministrativaService;
	private static final Logger logger = LoggerFactory.getLogger(CatalogoController.class);
	//JUSTIFICACION
	private static final String MENSAJE_REGISTRO_JUSTIFICACION = "La justificación ha quedado registrada";
	private static final String MENSAJE_AUTORIZA_JUSTIFICACION = "La justificación ha sido aceptada";
	private static final String MENSAJE_RECHAZA_JUSTIFICACION = "La justificación ha sido marcada para enviar a descuento";
	
	//DESCUENTO
	private static final String MENSAJE_REGISTRO_DESCUENTO     = "La incidencia ha sido marcada para enviar a descuento";
	private static final String MENSAJE_AUTORIZA_DESCUENTO     = "Esta incidencia será enviada a descuento";
	private static final String MENSAJE_RECHAZA_DESCUENTO      = "El descuento ha sido aceptado";
	
	private static final String MENSAJE_EXCEPCION = "No se registró la operación.";
	
	private static final String ID_ASISTENCIA = "Id Asistencia";
	private static final String ENTRADA = "Entrada";
	private static final String ID_STATUS = "Id Status";
	private static final String ID_TIPO = "Id Tipo";
	private static final String SALIDA = "Salida";
	private static final String USUARIO = "Usuario";
	private static final String CABECERAS = "cabeceras";
	private static final String ASISTENCIA_DIRECCION = "/asistencia/direccion";
	private static final String APPLICATION_MS_EXCEL = "application/ms-excel";
	private static final String CONTENT_DISPOSITION = "Content-disposition";
	private static final String ASISTENCIA_COORDINADOR = "/asistencia/coordinador";
	
	//EMPLEADO
	@RequestMapping(value={"empleado"}, method = RequestMethod.GET)
    public String buscaListaAsistenciaEmpleado(Model model) {
    	
    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA, new ArrayList<Asistencia>());
    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA_JUSTIFICAR, new ArrayList<Asistencia>());
    	model.addAttribute(ConstantsController.INICIO, true);
    	
    	return "/asistencia/empleado";
    }
	
	@RequestMapping(value={"empleado/busca"}, method = RequestMethod.GET, params="busca")
    public String buscaListaAsistenciaEmpleadoRango(Model model, Authentication authentication, String fechaInicial, String fechaFinal) {

    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRango(authentication.getName(), fechaInicial, fechaFinal, authentication);
    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA, asistencia);
    	model.addAttribute(ConstantsController.FECHA_INICIAL, fechaInicial);
    	model.addAttribute(ConstantsController.FECHA_FINAL, fechaFinal);
    	
    	return "/asistencia/empleado";
    }
	
	@RequestMapping(value="empleado/busca", method=RequestMethod.GET, params="exporta")
    public ModelAndView exportaExcelEmpleado(HttpServletRequest request, HttpServletResponse response, Authentication authentication, String fechaInicial, String fechaFinal) {
        
	    	List<Asistencia> listaAsistencia = asistenciaService.buscaAsistenciaEmpleadoRango(authentication.getName(), fechaInicial, fechaFinal, authentication);
	    	
	    	Map<String, Object> model = new HashMap<>();
	        
	    	//nombre de la hoja
	        model.put(ConstantsController.NOMBRE_HOJA, ConstantsController.ASISTENCIA);
	        
	        //nombres columnas
	        List<String> cabeceras = new ArrayList<>();
	        cabeceras.add(ID_ASISTENCIA);
	        cabeceras.add(ENTRADA);
	        cabeceras.add(ID_STATUS);
	        cabeceras.add(ID_TIPO);
	        cabeceras.add(SALIDA);
	        cabeceras.add(USUARIO);
	        model.put(CABECERAS, cabeceras);
	        
	        //Información registros (List<Object[]>)
	        List<List<String>> asistencias = new ArrayList<>();
	        
	        for (Asistencia a : listaAsistencia) {
	        	
	        	List<String> elementos = new ArrayList<>();
	        	elementos.add(a.getIdAsistencia() != null ? a.getIdAsistencia().toString() : "");
	        	elementos.add(a.getEntrada().toString());
	        	elementos.add(a.getIdEstatus().getEstatus() != null ? a.getIdEstatus().getEstatus() : "");
	        	elementos.add(a.getIdTipoDia().getNombre() != null ? a.getIdTipoDia().getNombre() : "");
	        	elementos.add(a.getSalida() != null ? a.getSalida().toString() : "");
	        	elementos.add(a.getUsuarioDto() != null ? a.getUsuarioDto().getClaveUsuario() :"");
	        	asistencias.add(elementos);
	        }
	        
	        model.put(ConstantsController.REGISTROS, asistencias);
	        
	        response.setContentType( APPLICATION_MS_EXCEL );
	        response.setHeader( CONTENT_DISPOSITION, "attachment; filename=SESNSP.xls" );         
	        
	        return new ModelAndView(new Excel(), model);
    }
    
    @RequestMapping(value="empleado/busca", method=RequestMethod.GET, params="imprime")
    public ModelAndView imprimeEmpleado(HttpServletRequest request, HttpServletResponse response, Authentication authentication, String cve_m_usuario, String fechaInicial, String fechaFinal) {
        
    	return new ModelAndView();
    	
    }
	//TERMINA EMPLEADO
    
	//COORDINADOR
    @RequestMapping(value={"coordinador"}, method = RequestMethod.GET)
    public String buscaListaAsistenciaCoordinador(Model model, Authentication authentication) {
    	
    	List<EstatusDto> estatus =  estatusService.obtieneListaCompletaEstatus(authentication);
    	List<EstatusDto> listaEstado = new ArrayList<>();
    	
    	for (EstatusDto e : estatus) {
    		if (e.getIdEstatus() != 4 && e.getIdEstatus() != 5) {
    			listaEstado.add(e);
    		}
    	}
    	
    	model.addAttribute(ConstantsController.LISTA_TIPO, catalogoService.obtieneTipoDias(authentication));
    	model.addAttribute(ConstantsController.LISTA_NIVEL, catalogoService.obtieneNiveles(authentication));
    	model.addAttribute(ConstantsController.LISTA_ESTADO, listaEstado);
    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA, new ArrayList<Asistencia>());
    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA_JUSTIFICAR, new ArrayList<Asistencia>());
    	model.addAttribute(ConstantsController.INICIO, true);
    	
    	return ASISTENCIA_COORDINADOR;
    }
    
    @RequestMapping(value={"coordinador/busca"}, method = RequestMethod.GET, params="busca")
    public String buscaListaAsistenciaCoordinadorRango(Model model, Authentication authentication, String cveMusuario, String nombre, String paterno, 
    		String materno, String nivel, Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa) {

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
		asistenciaBusquedaUtil.setUnidadAdministrativa("");
		asistenciaBusquedaUtil.setCveUsuarioLogeado(authentication.getName());
    	
	    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(asistenciaBusquedaUtil, authentication);
	    	
	    	List<EstatusDto> estatus =  estatusService.obtieneListaCompletaEstatus(authentication);
	    	List<EstatusDto> listaEstado = new ArrayList<>();
	    	
	    	for (EstatusDto e : estatus) {
	    		if (e.getIdEstatus() != 4 && e.getIdEstatus() != 5) {
	    			listaEstado.add(e);
	    		}
	    	}
	    	
	    	model.addAttribute(ConstantsController.LISTA_TIPO, catalogoService.obtieneTipoDias(authentication));
	    	model.addAttribute(ConstantsController.LISTA_NIVEL, catalogoService.obtieneNiveles(authentication));
	    	model.addAttribute(ConstantsController.LISTA_ESTADO, listaEstado);
	    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA, asistencia);
	    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA_JUSTIFICAR, new ArrayList<Asistencia>());
	    	model.addAttribute(ConstantsController.FECHA_INICIAL, fechaInicial);
	    	model.addAttribute(ConstantsController.FECHA_FINAL, fechaFinal);
	    	model.addAttribute(ConstantsController.CVE_M_USUARIO, cveMusuario);
	    	model.addAttribute(ConstantsController.NOMBRE, nombre);
	    	model.addAttribute(ConstantsController.PATERNO, paterno);
	    	model.addAttribute(ConstantsController.MATERNO, materno);
	    	model.addAttribute(ConstantsController.NIVEL, nivel);
	    	model.addAttribute(ConstantsController.TIPO, tipo);
	    	model.addAttribute(ConstantsController.ESTADO, estado);
	    	model.addAttribute(ConstantsController.UNIDAD_ADMINISTRATIVA, unidadAdministrativa);
	    	
	    	//se realizó búsqueda por usuario
	    	if (!cveMusuario.isEmpty()) {
	    		model.addAttribute("activaCheckbox", true);
	    	}
	    	
	    	return ASISTENCIA_COORDINADOR;
    }
    
    @RequestMapping(value="coordinador/busca", method=RequestMethod.GET, params="exporta")
    public ModelAndView exportaExcelCoordinador(HttpServletRequest request, HttpServletResponse response, Authentication authentication, String cveMusuario, String nombre, String paterno, 
    		String materno, String nivel, Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa) {
    	
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
		asistenciaBusquedaUtil.setUnidadAdministrativa("");
		asistenciaBusquedaUtil.setCveUsuarioLogeado(authentication.getName());
        
    	List<Asistencia> listaAsistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(asistenciaBusquedaUtil, authentication);
    	
    	Map<String, Object> model = new HashMap<>();
        
    	//nombre de la hoja
        model.put(ConstantsController.NOMBRE_HOJA, ConstantsController.ASISTENCIA);
        
        //nombres columnas
        List<String> cabeceras = new ArrayList<>();
        cabeceras.add(ID_ASISTENCIA);
        cabeceras.add(ENTRADA);
        cabeceras.add(ID_STATUS);
        cabeceras.add(ID_TIPO);
        cabeceras.add(SALIDA);
        cabeceras.add(USUARIO);
        model.put(CABECERAS, cabeceras);
        
        //Información registros (List<Object[]>)
        List<List<String>> asistencias = new ArrayList<>();
        
        for (Asistencia a : listaAsistencia) {
        	
        	List<String> elementos = new ArrayList<>();
        	elementos.add(a.getIdAsistencia() != null ? a.getIdAsistencia().toString() : "");
        	elementos.add(a.getEntrada().toString());
        	elementos.add(a.getIdEstatus().getEstatus() != null ? a.getIdEstatus().getEstatus() : "");
        	elementos.add(a.getIdTipoDia().getNombre() != null ? a.getIdTipoDia().getNombre() : "");
        	elementos.add(a.getSalida() != null ? a.getSalida().toString() : "");
        	elementos.add(a.getUsuarioDto() != null ? a.getUsuarioDto().getClaveUsuario() : "");
        	asistencias.add(elementos);
        }
        
        model.put(ConstantsController.REGISTROS, asistencias);
        
        response.setContentType( APPLICATION_MS_EXCEL );
        response.setHeader( CONTENT_DISPOSITION, "attachment; filename=ReporteSESNSP.xls" );         
        
        return new ModelAndView(new Excel(), model);
    	
    }
    
    @RequestMapping(value={"creaIncidencia"}, method = RequestMethod.POST, params="justifica")
    public String creaIncidencia(Model model, String cveMUsuarioHidden, Integer idAsistenciaHidden, Integer idTipoDia, Integer idJustificacion, 
    		String nombreHidden, String paternoHidden, String maternoHidden, String nivelHidden, Integer tipoHidden, Integer estadoHidden, String fechaInicial, 
    		String fechaFinal, String unidadAdministrativaHidden, Authentication authentication, MultipartFile archivoSubido, String cveMusuario, String nombreAutorizador) {
    	
    	Integer resultadoProceso = 0;
    	Archivo archivo = null;
    	
    	try {
    		//guarda el archivo
    		if (archivoSubido.getSize() > 0) {
    			archivo = archivoService.guardaArchivo(archivoSubido, cveMusuario, "asistencia_justificacion", "justificacion-", authentication);
    		}
    		
    		if (archivo != null) {
	    		//crea la incidencia y asocia el archivo
	    		resultadoProceso = asistenciaService.creaIncidencia(idAsistenciaHidden, idTipoDia, idJustificacion, archivo.getIdArchivo(), nombreAutorizador, authentication);
    		} 

    	} catch(Exception e) {
    		logger.warn(ConstantsController.ERROR,e);
    	}
    	
    	AsistenciaBusquedaUtil asistenciaBusquedaUtil = new AsistenciaBusquedaUtil();
		
		asistenciaBusquedaUtil.setCveMusuario(cveMUsuarioHidden);
		asistenciaBusquedaUtil.setNombre(nombreHidden);
		asistenciaBusquedaUtil.setPaterno(paternoHidden);
		asistenciaBusquedaUtil.setMaterno(maternoHidden);
		asistenciaBusquedaUtil.setNivel(nivelHidden);
		asistenciaBusquedaUtil.setTipo(tipoHidden);
		asistenciaBusquedaUtil.setEstado(estadoHidden);
		asistenciaBusquedaUtil.setFechaInicial(fechaInicial);
		asistenciaBusquedaUtil.setFechaFinal(fechaFinal);
		asistenciaBusquedaUtil.setUnidadAdministrativa(unidadAdministrativaHidden);
		asistenciaBusquedaUtil.setCveUsuarioLogeado(authentication.getName());
    	
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(asistenciaBusquedaUtil, authentication );
    	
    	List<EstatusDto> estatus =  estatusService.obtieneListaCompletaEstatus(authentication);
    	List<EstatusDto> listaEstado = new ArrayList<>();
    	
    	for (EstatusDto e : estatus) {
    		if (e.getIdEstatus() != 4 && e.getIdEstatus() != 5) {
    			listaEstado.add(e);
    		}
    	}
    	
    	model.addAttribute(ConstantsController.LISTA_TIPO, catalogoService.obtieneTipoDias(authentication));
    	model.addAttribute(ConstantsController.LISTA_NIVEL, catalogoService.obtieneNiveles(authentication));
    	model.addAttribute(ConstantsController.LISTA_ESTADO, listaEstado);
    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA, asistencia);
    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA_JUSTIFICAR, new ArrayList<Asistencia>());
    	model.addAttribute(ConstantsController.FECHA_INICIAL, fechaInicial);
    	model.addAttribute(ConstantsController.FECHA_FINAL, fechaFinal);
    	model.addAttribute(ConstantsController.CVE_M_USUARIO, cveMUsuarioHidden);
    	model.addAttribute(ConstantsController.NOMBRE, nombreHidden);
    	model.addAttribute(ConstantsController.PATERNO, paternoHidden);
    	model.addAttribute(ConstantsController.MATERNO, maternoHidden);
    	model.addAttribute(ConstantsController.NIVEL, nivelHidden);
    	model.addAttribute(ConstantsController.TIPO, tipoHidden);
    	model.addAttribute(ConstantsController.ESTADO, estadoHidden);
    	model.addAttribute(ConstantsController.UNIDAD_ADMINISTRATIVA, unidadAdministrativaHidden);
    	
    	if (resultadoProceso == 1) {
    		model.addAttribute(ConstantsController.MENSAJE, MENSAJE_REGISTRO_JUSTIFICACION);
    	} else if (resultadoProceso == 0) {
    		model.addAttribute(ConstantsController.MENSAJE_EXCEPTION, MENSAJE_EXCEPCION);
    	}
    	
    	return ASISTENCIA_COORDINADOR;
    }
    
    @RequestMapping(value={"creaIncidencia"}, method = RequestMethod.POST, params="descuenta")
    public String creaDescuento(Model model, String cveMUsuarioHidden, Integer idAsistenciaHidden, Integer idTipoDia, Integer idJustificacion, 
    		String nombreHidden, String paternoHidden, String maternoHidden, String nivelHidden, Integer tipoHidden, Integer estadoHidden, String fechaInicial, 
    		String fechaFinal, String unidadAdministrativaHidden, Authentication authentication, MultipartFile archivoSubido, String cveMusuario, String nombreAutorizador) {
    	
    	Integer resultadoProceso = 0;
    	Archivo archivo = null;
    	
    	try {
    		//guarda el archivo
    		if (archivoSubido.getSize() > 0) {
    			archivo = archivoService.guardaArchivo(archivoSubido, cveMusuario, "descuento" , "descuento-", authentication);
    		}
    		
    		if (archivo != null) {
	    		//crea la petición de descuento y asocia el archivo
	    		resultadoProceso = asistenciaService.creaDescuento(idAsistenciaHidden, idTipoDia, idJustificacion, archivo.getIdArchivo(), nombreAutorizador, authentication);
    		} 

    	} catch(Exception e) {
    		logger.warn(ConstantsController.ERROR,e);
    	}
    	
    	AsistenciaBusquedaUtil asistenciaBusquedaUtil = new AsistenciaBusquedaUtil();
		
		asistenciaBusquedaUtil.setCveMusuario(cveMUsuarioHidden);
		asistenciaBusquedaUtil.setNombre(nombreHidden);
		asistenciaBusquedaUtil.setPaterno(paternoHidden);
		asistenciaBusquedaUtil.setMaterno(maternoHidden);
		asistenciaBusquedaUtil.setNivel(nivelHidden);
		asistenciaBusquedaUtil.setTipo(tipoHidden);
		asistenciaBusquedaUtil.setEstado(estadoHidden);
		asistenciaBusquedaUtil.setFechaInicial(fechaInicial);
		asistenciaBusquedaUtil.setFechaFinal(fechaFinal);
		asistenciaBusquedaUtil.setUnidadAdministrativa(unidadAdministrativaHidden);
		asistenciaBusquedaUtil.setCveUsuarioLogeado(authentication.getName());
    	
		List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(asistenciaBusquedaUtil, authentication);
    	
		List<EstatusDto> estatus =  estatusService.obtieneListaCompletaEstatus(authentication);
    	List<EstatusDto> listaEstado = new ArrayList<>();
    	
    	for (EstatusDto e : estatus) {
    		if (e.getIdEstatus() != 4 && e.getIdEstatus() != 5) {
    			listaEstado.add(e);
    		}
    	}
		
		model.addAttribute(ConstantsController.LISTA_TIPO, catalogoService.obtieneTipoDias(authentication));
		model.addAttribute(ConstantsController.LISTA_NIVEL, catalogoService.obtieneNiveles(authentication));
		model.addAttribute(ConstantsController.LISTA_ESTADO, listaEstado);
    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA, asistencia);
    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA_JUSTIFICAR, new ArrayList<Asistencia>());
    	model.addAttribute(ConstantsController.FECHA_INICIAL, fechaInicial);
    	model.addAttribute(ConstantsController.FECHA_FINAL, fechaFinal);
    	model.addAttribute(ConstantsController.CVE_M_USUARIO, cveMUsuarioHidden);
    	model.addAttribute(ConstantsController.NOMBRE, nombreHidden);
    	model.addAttribute(ConstantsController.PATERNO, paternoHidden);
    	model.addAttribute(ConstantsController.MATERNO, maternoHidden);
    	model.addAttribute(ConstantsController.NIVEL, nivelHidden);
    	model.addAttribute(ConstantsController.TIPO, tipoHidden);
    	model.addAttribute(ConstantsController.ESTADO, estadoHidden);
    	model.addAttribute(ConstantsController.UNIDAD_ADMINISTRATIVA, unidadAdministrativaHidden);
    	
    	if (resultadoProceso == 1) {
    		model.addAttribute(ConstantsController.MENSAJE, MENSAJE_REGISTRO_DESCUENTO);
    	} else if (resultadoProceso == 0) {
    		model.addAttribute(ConstantsController.MENSAJE_EXCEPTION, MENSAJE_EXCEPCION);
    	}
    	
    	return ASISTENCIA_COORDINADOR;
    }
    
    @RequestMapping(value={"creaIncidencia"}, method = RequestMethod.POST, params="formatoJustificacion")
    public String descargaFormatoJustificacion(Model model, String nombre, String unidad, String nombreAutorizador, String cveMusuarioHidden, Integer idAsistenciaHidden, Integer idTipoDia, Integer idJustificacion, 
    		String nombreHidden, String paternoHidden, String maternoHidden, String nivelHidden, Integer tipoHidden, Integer estadoHidden, String fechaInicial, 
    		String fechaFinal, String unidadAdministrativaHidden, Authentication authentication, String cveMusuario, Integer idTipoDiaModal, HttpServletResponse response,
    		String fechaIncidenciaHidden) {
    	
    	//se traduce la incidencia
    	String codigoincidencia = "";
    	
    	if (idTipoDiaModal == 2) {
    		codigoincidencia = "O";
    	} else if (idTipoDiaModal == 3) {
    		codigoincidencia = "1/2";
    	} else if (idTipoDiaModal == 4) {
    		codigoincidencia = "P";
    	} else if (idTipoDiaModal == 6) {
    		codigoincidencia = "Inasistencia";
    	} else if (idTipoDiaModal == 7) {
    		codigoincidencia = "Comisión";
    	}else if (idTipoDiaModal == 8) {
    		codigoincidencia = "Licencia Médica";
    	}
    	
    	//fecha actual para el reporte
    	Date fechaHoy = new Date();
    	SimpleDateFormat f = new SimpleDateFormat(ConstantsController.DD_MM_YYYY);
    	String fechaActual = f.format(fechaHoy);
    	
    	try {
			reporte archivo = asistenciaService.formatoJustificacion(new FormatoIncidencia(nombre, unidad, fechaActual, codigoincidencia, "", fechaIncidenciaHidden), authentication);
			InputStream targetStream = new ByteArrayInputStream(archivo.getNombre());
			String mimeType = URLConnection.guessContentTypeFromStream(targetStream);
			
			if(mimeType == null){
				mimeType = ConstantsController.APLICATION_PDF;
			}
			
			response.setContentType(mimeType);
			response.setHeader( ConstantsController.CONTENT_DISPOSITION, "attachment;filename= Formato Justificacion.pdf" );
			IOUtils.copy(targetStream, response.getOutputStream());
	        ServletOutputStream stream = response.getOutputStream();
	        stream.flush();
	        response.flushBuffer();
		} catch (Exception e) {
			logger.warn(ConstantsController.ERROR,e);
		}
    	
    	AsistenciaBusquedaUtil asistenciaBusquedaUtil = new AsistenciaBusquedaUtil();
		
		asistenciaBusquedaUtil.setCveMusuario(cveMusuarioHidden);
		asistenciaBusquedaUtil.setNombre(nombreHidden);
		asistenciaBusquedaUtil.setPaterno(paternoHidden);
		asistenciaBusquedaUtil.setMaterno(maternoHidden);
		asistenciaBusquedaUtil.setNivel(nivelHidden);
		asistenciaBusquedaUtil.setTipo(tipoHidden);
		asistenciaBusquedaUtil.setEstado(estadoHidden);
		asistenciaBusquedaUtil.setFechaInicial(fechaInicial);
		asistenciaBusquedaUtil.setFechaFinal(fechaFinal);
		asistenciaBusquedaUtil.setUnidadAdministrativa(unidadAdministrativaHidden);
		asistenciaBusquedaUtil.setCveUsuarioLogeado(authentication.getName());
    	
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(asistenciaBusquedaUtil, authentication);
    	
    	List<EstatusDto> estatus =  estatusService.obtieneListaCompletaEstatus(authentication);
    	List<EstatusDto> listaEstado = new ArrayList<>();
    	
    	for (EstatusDto e : estatus) {
    		if (e.getIdEstatus() != 4 && e.getIdEstatus() != 5) {
    			listaEstado.add(e);
    		}
    	}
    	
    	model.addAttribute(ConstantsController.LISTA_TIPO, catalogoService.obtieneTipoDias(authentication));
    	model.addAttribute(ConstantsController.LISTA_NIVEL, catalogoService.obtieneNiveles(authentication));
    	model.addAttribute(ConstantsController.LISTA_ESTADO, listaEstado);
    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA, asistencia);
    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA_JUSTIFICAR, new ArrayList<Asistencia>());
    	model.addAttribute(ConstantsController.FECHA_INICIAL, fechaInicial);
    	model.addAttribute(ConstantsController.FECHA_FINAL, fechaFinal);
    	model.addAttribute(ConstantsController.CVE_M_USUARIO, cveMusuarioHidden);
    	model.addAttribute(ConstantsController.NOMBRE, nombreHidden);
    	model.addAttribute(ConstantsController.PATERNO, paternoHidden);
    	model.addAttribute(ConstantsController.MATERNO, maternoHidden);
    	model.addAttribute(ConstantsController.NIVEL, nivelHidden);
    	model.addAttribute(ConstantsController.TIPO, tipoHidden);
    	model.addAttribute(ConstantsController.ESTADO, estadoHidden);
    	model.addAttribute(ConstantsController.UNIDAD_ADMINISTRATIVA, unidadAdministrativaHidden);
    	
    	return ASISTENCIA_COORDINADOR;
    }
    
    @RequestMapping(value={"creaIncidencia"}, method = RequestMethod.POST, params="formatoDescuento")
    public String descargaFormatoDescuento(Model model, String nombre, String unidad, String nombreAutorizador, String cveMusuarioHidden, Integer idAsistenciaHidden, Integer idTipoDia, Integer idJustificacion, 
    		String nombreHidden, String paternoHidden, String maternoHidden, String nivelHidden, Integer tipoHidden, Integer estadoHidden, String fechaInicial, 
    		String fechaFinal, String unidadAdministrativaHidden, Authentication authentication, String cveMusuario, HttpServletResponse response,
    		String fechaIncidenciaHidden) {
    	
    	//fecha actual para el reporte
    	Date fechaHoy = new Date();
    	SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
    	String fechaActual = f.format(fechaHoy);
    	
    	try {
			reporte archivo = asistenciaService.formatoDescuento(new FormatoIncidencia(nombre, "", fechaActual, "", cveMusuario, fechaIncidenciaHidden), authentication);
			InputStream targetStream = new ByteArrayInputStream(archivo.getNombre());
			String mimeType = URLConnection.guessContentTypeFromStream(targetStream);
			
			if(mimeType == null){
				mimeType = ConstantsController.APLICATION_PDF;
			}
			
			response.setContentType(mimeType);
			response.setHeader( CONTENT_DISPOSITION, "attachment;filename= Formato Descuento.pdf" );
			IOUtils.copy(targetStream, response.getOutputStream());
	        ServletOutputStream stream = response.getOutputStream();
	        stream.flush();
	        response.flushBuffer();
		} catch (Exception e) {
			logger.warn(ConstantsController.ERROR,e);
		}
    	
    	AsistenciaBusquedaUtil asistenciaBusquedaUtil = new AsistenciaBusquedaUtil();
		
		asistenciaBusquedaUtil.setCveMusuario(cveMusuarioHidden);
		asistenciaBusquedaUtil.setNombre(nombreHidden);
		asistenciaBusquedaUtil.setPaterno(paternoHidden);
		asistenciaBusquedaUtil.setMaterno(maternoHidden);
		asistenciaBusquedaUtil.setNivel(nivelHidden);
		asistenciaBusquedaUtil.setTipo(tipoHidden);
		asistenciaBusquedaUtil.setEstado(estadoHidden);
		asistenciaBusquedaUtil.setFechaInicial(fechaInicial);
		asistenciaBusquedaUtil.setFechaFinal(fechaFinal);
		asistenciaBusquedaUtil.setUnidadAdministrativa(unidadAdministrativaHidden);
		asistenciaBusquedaUtil.setCveUsuarioLogeado(authentication.getName());
    	
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(asistenciaBusquedaUtil, authentication);
    	
    	List<EstatusDto> estatus =  estatusService.obtieneListaCompletaEstatus(authentication);
    	List<EstatusDto> listaEstado = new ArrayList<>();
    	
    	for (EstatusDto e : estatus) {
    		if (e.getIdEstatus() != 4 && e.getIdEstatus() != 5) {
    			listaEstado.add(e);
    		}
    	}
    	
    	model.addAttribute(ConstantsController.LISTA_TIPO, catalogoService.obtieneTipoDias(authentication));
    	model.addAttribute(ConstantsController.LISTA_NIVEL, catalogoService.obtieneNiveles(authentication));
    	model.addAttribute(ConstantsController.LISTA_ESTADO, listaEstado);
    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA, asistencia);
    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA_JUSTIFICAR, new ArrayList<Asistencia>());
    	model.addAttribute(ConstantsController.FECHA_INICIAL, fechaInicial);
    	model.addAttribute(ConstantsController.FECHA_FINAL, fechaFinal);
    	model.addAttribute(ConstantsController.CVE_M_USUARIO, cveMusuarioHidden);
    	model.addAttribute(ConstantsController.NOMBRE, nombreHidden);
    	model.addAttribute(ConstantsController.PATERNO, paternoHidden);
    	model.addAttribute(ConstantsController.MATERNO, maternoHidden);
    	model.addAttribute(ConstantsController.NIVEL, nivelHidden);
    	model.addAttribute(ConstantsController.TIPO, tipoHidden);
    	model.addAttribute(ConstantsController.ESTADO, estadoHidden);
    	model.addAttribute(ConstantsController.UNIDAD_ADMINISTRATIVA, unidadAdministrativaHidden);
    	
    	return ASISTENCIA_COORDINADOR;
    }
    
    @SuppressWarnings("unused")
	@RequestMapping(value={"coordinador/buscaAsistenciasPorId"}, method = RequestMethod.POST)
    public String buscaAsistenciasPorId(Model model, Integer[] checkboxes, String[] arregloIdAsistencias, String cveMusuarioHiddenListaMultiple, String fechaInicialHiddenListaMultiple, 
    		String fechaFinalHiddenListaMultiple, Authentication authentication) {
    	
    	List<Asistencia> listaAsistenciaJustificar = new ArrayList<>();
    	String nombreJefe = "";
    	String motivo = "";
    	
    	List<Integer> idAsistencias = new ArrayList<>();
    	
    	for (String a : arregloIdAsistencias) {
    		idAsistencias.add(Integer.parseInt(a));
    	}
    	
    	if (!idAsistencias.isEmpty()) {
    		
    		//obtiene las asistencias que se van a justificar
    		obtieneListaAsistenciasAjustificar(idAsistencias, nombreJefe, motivo, listaAsistenciaJustificar, authentication);
    		
	    	List<JustificacionDto> listaJustificaciones = catalogoService.obtieneJustificaciones(authentication);
	    	List<Usuario> listaJefes = usuarioService.obtieneListaJefes(authentication);
	    	
	    	List<EstatusDto> estatus =  estatusService.obtieneListaCompletaEstatus(authentication);
	    	List<EstatusDto> listaEstado = new ArrayList<>();
	    	
	    	for (EstatusDto e : estatus) {
	    		if (e.getIdEstatus() != 4 && e.getIdEstatus() != 5) {
	    			listaEstado.add(e);
	    		}
	    	}
	    	
	    	model.addAttribute(ConstantsController.LISTA_TIPO, catalogoService.obtieneTipoDias(authentication));
	    	model.addAttribute(ConstantsController.LISTA_NIVEL, catalogoService.obtieneNiveles(authentication));
	    	model.addAttribute(ConstantsController.LISTA_ESTADO, listaEstado);
	    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA, new ArrayList<Asistencia>());
	    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA_JUSTIFICAR, listaAsistenciaJustificar);
	    	model.addAttribute(ConstantsController.LISTA_JUSTIFICACIONES, listaJustificaciones);
	    	model.addAttribute(ConstantsController.LISTA_AUTORIZADORES, listaJefes);
	    	model.addAttribute(ConstantsController.NOMBRE_JEFE, nombreJefe);
	    	model.addAttribute(ConstantsController.JUSTIFICACION, motivo);
	    	model.addAttribute(ConstantsController.FECHA_INICIAL, fechaInicialHiddenListaMultiple);
	    	model.addAttribute(ConstantsController.FECHA_FINAL, fechaFinalHiddenListaMultiple);
	    	model.addAttribute(ConstantsController.CVE_M_USUARIO, cveMusuarioHiddenListaMultiple);
    	} else {
    		AsistenciaBusquedaUtil asistenciaBusquedaUtil = new AsistenciaBusquedaUtil();
    		
    		asistenciaBusquedaUtil.setCveMusuario(cveMusuarioHiddenListaMultiple);
    		asistenciaBusquedaUtil.setNombre("");
    		asistenciaBusquedaUtil.setPaterno("");
    		asistenciaBusquedaUtil.setMaterno("");
    		asistenciaBusquedaUtil.setNivel("");
    		asistenciaBusquedaUtil.setTipo(null);
    		asistenciaBusquedaUtil.setEstado(null);
    		asistenciaBusquedaUtil.setFechaInicial(fechaInicialHiddenListaMultiple);
    		asistenciaBusquedaUtil.setFechaFinal(fechaFinalHiddenListaMultiple);
    		asistenciaBusquedaUtil.setUnidadAdministrativa("");
    		asistenciaBusquedaUtil.setCveUsuarioLogeado(authentication.getName());
    		
    		List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(asistenciaBusquedaUtil, authentication);
    		
    		model.addAttribute(ConstantsController.LISTA_TIPO, catalogoService.obtieneTipoDias(authentication));
    		model.addAttribute(ConstantsController.LISTA_NIVEL, catalogoService.obtieneNiveles(authentication));
    		model.addAttribute(ConstantsController.LISTA_ESTADO, estatusService.obtieneListaCompletaEstatus(authentication));
    		model.addAttribute(ConstantsController.LISTA_ASISTENCIA, asistencia);
	    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA_JUSTIFICAR, new ArrayList<Asistencia>());
	    	model.addAttribute(ConstantsController.FECHA_INICIAL, fechaInicialHiddenListaMultiple);
	    	model.addAttribute(ConstantsController.FECHA_FINAL, fechaFinalHiddenListaMultiple);
	    	model.addAttribute(ConstantsController.CVE_M_USUARIO, cveMusuarioHiddenListaMultiple);
	    	
	    	//se realizó búsqueda por usuario
	    	if (!cveMusuarioHiddenListaMultiple.isEmpty()) {
	    		model.addAttribute("activaCheckbox", true);
	    	}
    	}
    	
    	return ASISTENCIA_COORDINADOR;
    }
    
    @RequestMapping(value={"coordinador/justificaMultiple"}, method = RequestMethod.POST, params = "justificacionMultipleGuarda")
    public String creaIncidencias(Model model, Integer[] listaIdAsistencias, MultipartFile archivoSubido, Integer selectJustificacion, String nombreAutorizador,
    		String cvemUsuarioHiddenGuardaMultiple, String fechaInicialHiddenGuardaMultiple, String fechaFinalHiddenGuardaMultiple, Authentication authentication) {
    			   
    	Integer resultadoProceso = 0;
    	Archivo archivo = null;
    	Integer idJustificacion = selectJustificacion;
    	
    	for (Integer idAsistencia : listaIdAsistencias) {
    		Asistencia asistencia = asistenciaService.buscaAsistenciaPorId(idAsistencia, authentication);
    		Integer idTipoDia = asistencia.getIdTipoDia().getIdTipoDia();
    		String cveMusuario = asistencia.getUsuarioDto().getClaveUsuario();
    		
    		try {
        		//guarda el archivo
        		if (archivoSubido.getSize() > 0) {
    				archivo = archivoService.guardaArchivo(archivoSubido, cveMusuario, "justificacion", "justificacion-", authentication);
        			
    				if (archivo != null) {
	        			//crea la incidencia y asocia el archivo
	            		resultadoProceso = asistenciaService.creaIncidencia(idAsistencia, idTipoDia, idJustificacion, archivo.getIdArchivo(), nombreAutorizador, authentication);
    				}
        		}
        	} catch(Exception e) {
        		logger.error(ConstantsController.ERROR, e);
        	}
    	}
    	
    	AsistenciaBusquedaUtil asistenciaBusquedaUtil = new AsistenciaBusquedaUtil();
		
		asistenciaBusquedaUtil.setCveMusuario(cvemUsuarioHiddenGuardaMultiple);
		asistenciaBusquedaUtil.setNombre("");
		asistenciaBusquedaUtil.setPaterno("");
		asistenciaBusquedaUtil.setMaterno("");
		asistenciaBusquedaUtil.setNivel("");
		asistenciaBusquedaUtil.setTipo(null);
		asistenciaBusquedaUtil.setEstado(null);
		asistenciaBusquedaUtil.setFechaInicial(fechaInicialHiddenGuardaMultiple);
		asistenciaBusquedaUtil.setFechaFinal(fechaFinalHiddenGuardaMultiple);
		asistenciaBusquedaUtil.setUnidadAdministrativa("");
		asistenciaBusquedaUtil.setCveUsuarioLogeado(authentication.getName());
    	
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(asistenciaBusquedaUtil, authentication);
    	
    	List<EstatusDto> estatus =  estatusService.obtieneListaCompletaEstatus(authentication);
    	List<EstatusDto> listaEstado = new ArrayList<>();
    	
    	for (EstatusDto e : estatus) {
    		if (e.getIdEstatus() != 4 && e.getIdEstatus() != 5) {
    			listaEstado.add(e);
    		}
    	}
    	
    	model.addAttribute(ConstantsController.LISTA_TIPO, catalogoService.obtieneTipoDias(authentication));
    	model.addAttribute(ConstantsController.LISTA_NIVEL, catalogoService.obtieneNiveles(authentication));
    	model.addAttribute(ConstantsController.LISTA_ESTADO, listaEstado);
    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA, asistencia);
    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA_JUSTIFICAR, new ArrayList<>());
    	model.addAttribute(ConstantsController.FECHA_INICIAL, fechaInicialHiddenGuardaMultiple);
    	model.addAttribute(ConstantsController.FECHA_FINAL, fechaFinalHiddenGuardaMultiple);
    	model.addAttribute(ConstantsController.CVE_M_USUARIO, cvemUsuarioHiddenGuardaMultiple);
    	
    	if (resultadoProceso == 1) {
    		model.addAttribute(ConstantsController.MENSAJE, MENSAJE_REGISTRO_JUSTIFICACION);
    	} else if (resultadoProceso == 0) {
    		model.addAttribute(ConstantsController.MENSAJE_EXCEPTION, MENSAJE_EXCEPCION);
    	}
    	
    	return ASISTENCIA_COORDINADOR;
    }
    
    @RequestMapping(value={"coordinador/justificaMultiple"}, method = RequestMethod.POST, params = "formatoJustificacionMultipleBtn")
    public String descargaFormatoJustificacionMultiple(Model model, Integer[] listaIdAsistencias, MultipartFile archivo, Integer selectJustificacion, 
    		String nombreAutorizador, String cveMUsuarioHiddenGuardaMultiple, String fechaInicialHiddenGuardaMultiple, 
    		String fechaFinalHiddenGuardaMultiple, Authentication authentication, HttpServletResponse response) {
    	
    	Asistencia asistencia;
    	Integer idTipoDia = 0;
    	String nombre = "";
    	String unidadAdministrativa = "";
    	
    	//obtiene la asistencia del primer checkbox seleccionado
    	if (listaIdAsistencias != null) {
    		asistencia = asistenciaService.buscaAsistenciaPorId(listaIdAsistencias[0], authentication);
    		idTipoDia = asistencia.getIdTipoDia().getIdTipoDia();
    		nombre = asistencia.getUsuarioDto().getNombre() + " " + asistencia.getUsuarioDto().getApellidoPaterno() + " " + asistencia.getUsuarioDto().getApellidoPaterno();
    		unidadAdministrativa = asistencia.getUsuarioDto().getNombreUnidad();
    	}
    	
    	//se traduce la incidencia
    	String codigoincidencia = "";
    	
    	if (idTipoDia == 2) {
    		codigoincidencia = "O";
    	} else if (idTipoDia == 3) {
    		codigoincidencia = "1/2";
    	} else if (idTipoDia == 4) {
    		codigoincidencia = "P";
    	} else if (idTipoDia == 6) {
    		codigoincidencia = "Inasistencia";
    	} else if (idTipoDia == 7) {
    		codigoincidencia = "Comisión";
    	} else if (idTipoDia == 8) {
    		codigoincidencia = "Licencia Médica";
    	} else {
    		codigoincidencia = "Código no registrado";
    	}
    	
    	//fecha actual para el reporte
    	Date fechaHoy = new Date();
    	SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
    	String fechaActual = f.format(fechaHoy);
    	
    	try {
			reporte formatoJustificacion = asistenciaService.formatoJustificacion(new FormatoIncidencia(nombre, unidadAdministrativa, fechaActual, codigoincidencia, "", ""), authentication);
			InputStream targetStream = new ByteArrayInputStream(formatoJustificacion.getNombre());
			String mimeType = URLConnection.guessContentTypeFromStream(targetStream);
			
			if(mimeType == null){
				mimeType = "application/pdf";
			}
			
			response.setContentType(mimeType);
			response.setHeader( "Content-Disposition", "attachment;filename= Formato Justificacion.pdf" );
			IOUtils.copy(targetStream, response.getOutputStream());
	        ServletOutputStream stream = response.getOutputStream();
	        stream.flush();
	        response.flushBuffer();
		} catch (Exception e) {
			logger.warn(ConstantsController.ERROR,e);
		}
    	
    	AsistenciaBusquedaUtil asistenciaBusquedaUtil = new AsistenciaBusquedaUtil();
		
		asistenciaBusquedaUtil.setCveMusuario(cveMUsuarioHiddenGuardaMultiple);
		asistenciaBusquedaUtil.setNombre("");
		asistenciaBusquedaUtil.setPaterno("");
		asistenciaBusquedaUtil.setMaterno("");
		asistenciaBusquedaUtil.setNivel("");
		asistenciaBusquedaUtil.setTipo(null);
		asistenciaBusquedaUtil.setEstado(null);
		asistenciaBusquedaUtil.setFechaInicial(fechaInicialHiddenGuardaMultiple);
		asistenciaBusquedaUtil.setFechaFinal(fechaFinalHiddenGuardaMultiple);
		asistenciaBusquedaUtil.setUnidadAdministrativa("");
		asistenciaBusquedaUtil.setCveUsuarioLogeado(authentication.getName());
    	
    	List<Asistencia> listaAsistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(asistenciaBusquedaUtil, authentication);
   	
    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA, listaAsistencia);
    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA_JUSTIFICAR, new ArrayList<>());
    	model.addAttribute(ConstantsController.FECHA_INICIAL, fechaInicialHiddenGuardaMultiple);
    	model.addAttribute(ConstantsController.FECHA_FINAL, fechaFinalHiddenGuardaMultiple);
    	model.addAttribute(ConstantsController.CVE_M_USUARIO, cveMUsuarioHiddenGuardaMultiple);
   	
    	return ASISTENCIA_COORDINADOR;
    }
    
    //TERMINA COORDINADOR
    
    //DIRECCIÓN
    @RequestMapping(value={"direccion"}, method = RequestMethod.GET)
    public String buscaListaAsistenciaDireccion(Model model, Authentication authentication) {
    	
    	List<EstatusDto> estatus =  estatusService.obtieneListaCompletaEstatus(authentication);
    	List<EstatusDto> listaEstado = new ArrayList<>();
    	
    	for (EstatusDto e : estatus) {
    		if (e.getIdEstatus() != 4 && e.getIdEstatus() != 5) {
    			listaEstado.add(e);
    		}
    	}
    	
    	model.addAttribute(ConstantsController.LISTA_TIPO, catalogoService.obtieneTipoDias(authentication));
    	model.addAttribute(ConstantsController.LISTA_NIVEL, catalogoService.obtieneNiveles(authentication));
    	model.addAttribute(ConstantsController.LISTA_ESTADO, listaEstado);
    	model.addAttribute(ConstantsController.LISTA_UNIDAD_ADMINISTRATIVA, unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA, new ArrayList<Asistencia>());
    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA_JUSTIFICAR, new ArrayList<Asistencia>());
    	model.addAttribute(ConstantsController.INICIO, true);
    	
    	return ASISTENCIA_DIRECCION;
    }
    
    @RequestMapping(value={"direccion/busca"}, method = RequestMethod.GET, params="busca")
    public String buscaListaAsistenciaDireccionRango(Model model, Authentication authentication, String cveMusuario, String nombre, String paterno, 
    		String materno, String nivel, Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa) {

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
    	
	    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoDireccion(asistenciaBusquedaUtil, authentication);
	    	
	    	List<EstatusDto> estatus =  estatusService.obtieneListaCompletaEstatus(authentication);
	    	List<EstatusDto> listaEstado = new ArrayList<>();
	    	
	    	for (EstatusDto e : estatus) {
	    		if (e.getIdEstatus() != 4 && e.getIdEstatus() != 5) {
	    			listaEstado.add(e);
	    		}
	    	}
	    	
	    	model.addAttribute(ConstantsController.LISTA_TIPO, catalogoService.obtieneTipoDias(authentication));
	    	model.addAttribute(ConstantsController.LISTA_NIVEL, catalogoService.obtieneNiveles(authentication));
	    	model.addAttribute(ConstantsController.LISTA_ESTADO, listaEstado);
	    	model.addAttribute(ConstantsController.LISTA_UNIDAD_ADMINISTRATIVA, unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
	    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA, asistencia);
	    	model.addAttribute(ConstantsController.FECHA_INICIAL, fechaInicial);
	    	model.addAttribute(ConstantsController.FECHA_FINAL, fechaFinal);
	    	model.addAttribute(ConstantsController.CVE_M_USUARIO, cveMusuario);
	    	model.addAttribute(ConstantsController.NOMBRE, nombre);
	    	model.addAttribute(ConstantsController.PATERNO, paterno);
	    	model.addAttribute(ConstantsController.MATERNO, materno);
	    	model.addAttribute(ConstantsController.NIVEL, nivel);
	    	model.addAttribute(ConstantsController.TIPO, tipo);
	    	model.addAttribute(ConstantsController.ESTADO, estado);
	    	model.addAttribute(ConstantsController.UNIDAD_ADMINISTRATIVA, !unidadAdministrativa.isEmpty() ? Integer.parseInt(unidadAdministrativa) : 0);
	    	
	    	return ASISTENCIA_DIRECCION;
    }
    
    @RequestMapping(value="direccion/busca", method=RequestMethod.GET, params="exporta")
    public ModelAndView exportaExcelDireccion(HttpServletRequest request, HttpServletResponse response, Authentication authentication, 
    		String cveMusuario, String nombre, String paterno, String materno, String nivel, Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa) {
        
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
    	
    	List<Asistencia> listaAsistencia = asistenciaService.buscaAsistenciaEmpleadoRangoDireccion(asistenciaBusquedaUtil, authentication);
    	
    	Map<String, Object> model = new HashMap<>();
        
    	//nombre de la hoja
        model.put(ConstantsController.NOMBRE_HOJA, ConstantsController.ASISTENCIA);
        
        //nombres columnas
        List<String> cabeceras = new ArrayList<>();
        cabeceras.add(ID_ASISTENCIA);
        cabeceras.add(ENTRADA);
        cabeceras.add(ID_STATUS);
        cabeceras.add(ID_TIPO);
        cabeceras.add(SALIDA);
        cabeceras.add(USUARIO);
        model.put(CABECERAS, cabeceras);
        
        //Información registros (List<Object[]>)
        List<List<String>> asistencias = new ArrayList<>();
        
        for (Asistencia a : listaAsistencia) {
        	
        	List<String> elementos = new ArrayList<>();
        	elementos.add(a.getIdAsistencia() != null ? a.getIdAsistencia().toString() : "");
        	elementos.add(a.getEntrada().toString());
        	elementos.add(a.getIdEstatus().getEstatus() != null ? a.getIdEstatus().getEstatus() : "");
        	elementos.add(a.getIdTipoDia().getNombre() != null ? a.getIdTipoDia().getNombre() : "");
        	elementos.add(a.getSalida() != null ? a.getSalida().toString() : "");
        	elementos.add(a.getUsuarioDto() != null ? a.getUsuarioDto().getClaveUsuario() : "");
        	asistencias.add(elementos);
        }
        
        model.put(ConstantsController.REGISTROS, asistencias);
        
        response.setContentType( APPLICATION_MS_EXCEL );
        response.setHeader( CONTENT_DISPOSITION, "attachment; filename=SESNSP.xls" );         
        
        return new ModelAndView(new Excel(), model);
    	
    	
    }
    
    @RequestMapping(value={"direccion/dictamina_Incidencia_Descuento"}, method = RequestMethod.POST) //permite aprobar o rechazar justificaciones y descuentos
    public String dictaminaIncidenciaDescuento(Model model, String cveMUsuarioHidden, Integer idAsistenciaHidden, String nombreHidden, String paternoHidden,
    		String maternoHidden, String nivelHidden, Integer tipoHidden, Integer estadoHidden, String unidadAdministrativaHidden, Integer idTipoDia, 
    		Integer idJustificacion, String fechaInicial, String fechaFinal, String dictaminacion, String nombre, String unidad, HttpServletResponse response,
    		Authentication authentication) {
    	
    	Integer resultadoProceso = 0;

    	//se lleva a cabo la dictaminación
    	if (!dictaminacion.equals("Ver Archivo")) {
	    	resultadoProceso = asistenciaService.dictaminaIncidencia(idAsistenciaHidden, idTipoDia, idJustificacion, dictaminacion, authentication);
    	} else { //se muestra el archivo asociado de la petición
    		Asistencia asistencia = asistenciaService.buscaAsistenciaPorId(idAsistenciaHidden, authentication);
    		Integer idArchivo = asistencia.getIncidencia().getIdArchivo().getIdArchivo();    		

    		Archivo archivoAsociado;
        	archivoAsociado = archivoService.consultaArchivo(idArchivo, authentication);
        	String nombrecompleto = archivoAsociado.getUrl() + archivoAsociado.getNombre();

        	File file = new File(nombrecompleto);
            InputStream inputStream;
            
			try {
				inputStream = new BufferedInputStream(new FileInputStream(file));
				String mimeType= URLConnection.guessContentTypeFromStream(inputStream);
				
				if(mimeType==null){
	            	mimeType="application/pdf";
	            }
	            
	            response.setContentType(mimeType);
	            response.setContentLength((int)file.length());
	            response.setHeader("Content-Disposition", String.format("attachment; filename\"%s\"",file.getName()));
	            FileCopyUtils.copy(inputStream, response.getOutputStream());
			} catch (IOException e) {
				logger.info(ConstantsController.ERROR,e);
			}
            
    	}
    	
    	AsistenciaBusquedaUtil asistenciaBusquedaUtil = new AsistenciaBusquedaUtil();
		
		asistenciaBusquedaUtil.setCveMusuario(cveMUsuarioHidden);
		asistenciaBusquedaUtil.setNombre(nombreHidden);
		asistenciaBusquedaUtil.setPaterno(paternoHidden);
		asistenciaBusquedaUtil.setMaterno(maternoHidden);
		asistenciaBusquedaUtil.setNivel(nivelHidden);
		asistenciaBusquedaUtil.setTipo(tipoHidden);
		asistenciaBusquedaUtil.setEstado(estadoHidden);
		asistenciaBusquedaUtil.setFechaInicial(fechaInicial);
		asistenciaBusquedaUtil.setFechaFinal(fechaFinal);
		asistenciaBusquedaUtil.setUnidadAdministrativa(unidadAdministrativaHidden);
    	
    	List<Asistencia> listaAsistencia = asistenciaService.buscaAsistenciaEmpleadoRangoDireccion(asistenciaBusquedaUtil, authentication);
    	List<EstatusDto> estatus =  estatusService.obtieneListaCompletaEstatus(authentication);
    	List<EstatusDto> listaEstado = new ArrayList<>();
    	
    	for (EstatusDto e : estatus) {
    		if (e.getIdEstatus() != 4 && e.getIdEstatus() != 5) {
    			listaEstado.add(e);
    		}
    	}
    	
    	model.addAttribute(ConstantsController.LISTA_TIPO, catalogoService.obtieneTipoDias(authentication));
    	model.addAttribute(ConstantsController.LISTA_NIVEL, catalogoService.obtieneNiveles(authentication));
    	model.addAttribute(ConstantsController.LISTA_ESTADO, listaEstado);
    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA, listaAsistencia);
    	model.addAttribute(ConstantsController.FECHA_INICIAL, fechaInicial);
    	model.addAttribute(ConstantsController.FECHA_FINAL, fechaFinal);
    	model.addAttribute(ConstantsController.CVE_M_USUARIO, cveMUsuarioHidden);
    	model.addAttribute(ConstantsController.NOMBRE, nombreHidden);
    	model.addAttribute(ConstantsController.PATERNO, paternoHidden);
    	model.addAttribute(ConstantsController.MATERNO, maternoHidden);
    	model.addAttribute(ConstantsController.NIVEL, nivelHidden);
    	model.addAttribute(ConstantsController.TIPO, tipoHidden);
    	model.addAttribute(ConstantsController.ESTADO, estadoHidden);
    	model.addAttribute(ConstantsController.UNIDAD_ADMINISTRATIVA, unidadAdministrativaHidden);

    	//define el mensaje al usuario que se va a mostrar dependiendo de la tarea realizada
    	defineMensajeUsuario(resultadoProceso, dictaminacion, model);
    	
    	return ASISTENCIA_DIRECCION;
    }
    
    @RequestMapping(value={"direccion/dictamina_Incidencia_Descuento"}, method = RequestMethod.POST, params="descargaArchivoJustificacionDescuento") //permite aprobar o rechazar justificaciones y descuentos
    public String descargaArchivoJustificacionDescuento(Model model, String cveMUsuarioHidden, Integer idAsistenciaHidden, String nombreHidden, String paternoHidden,
    		String maternoHidden, String nivelHidden, Integer tipoHidden, Integer estadoHidden, String unidadAdministrativaHidden, Integer idTipoDia, 
    		Integer idJustificacion, String fechaInicial, String fechaFinal, String dictaminacion, Authentication authentication) {

    	asistenciaService.dictaminaIncidencia(idAsistenciaHidden, idTipoDia, idJustificacion, dictaminacion, authentication);
    	
    	AsistenciaBusquedaUtil asistenciaBusquedaUtil = new AsistenciaBusquedaUtil();
		
		asistenciaBusquedaUtil.setCveMusuario(cveMUsuarioHidden);
		asistenciaBusquedaUtil.setNombre(nombreHidden);
		asistenciaBusquedaUtil.setPaterno(paternoHidden);
		asistenciaBusquedaUtil.setMaterno(maternoHidden);
		asistenciaBusquedaUtil.setNivel(nivelHidden);
		asistenciaBusquedaUtil.setTipo(tipoHidden);
		asistenciaBusquedaUtil.setEstado(estadoHidden);
		asistenciaBusquedaUtil.setFechaInicial(fechaInicial);
		asistenciaBusquedaUtil.setFechaFinal(fechaFinal);
		asistenciaBusquedaUtil.setUnidadAdministrativa(unidadAdministrativaHidden);
    	
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoDireccion(asistenciaBusquedaUtil, authentication);
    	
    	List<EstatusDto> estatus =  estatusService.obtieneListaCompletaEstatus(authentication);
    	List<EstatusDto> listaEstado = new ArrayList<>();
    	
    	for (EstatusDto e : estatus) {
    		if (e.getIdEstatus() != 4 && e.getIdEstatus() != 5) {
    			listaEstado.add(e);
    		}
    	}
    	
    	model.addAttribute(ConstantsController.LISTA_TIPO, catalogoService.obtieneTipoDias(authentication));
    	model.addAttribute(ConstantsController.LISTA_NIVEL, catalogoService.obtieneNiveles(authentication));
    	model.addAttribute(ConstantsController.LISTA_ESTADO, listaEstado);
    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA, asistencia);
    	model.addAttribute(ConstantsController.FECHA_INICIAL, fechaInicial);
    	model.addAttribute(ConstantsController.FECHA_FINAL, fechaFinal);
    	model.addAttribute(ConstantsController.CVE_M_USUARIO, cveMUsuarioHidden);
    	model.addAttribute(ConstantsController.NOMBRE, nombreHidden);
    	model.addAttribute(ConstantsController.PATERNO, paternoHidden);
    	model.addAttribute(ConstantsController.MATERNO, maternoHidden);
    	model.addAttribute(ConstantsController.NIVEL, nivelHidden);
    	model.addAttribute(ConstantsController.TIPO, tipoHidden);
    	model.addAttribute(ConstantsController.ESTADO, estadoHidden);
    	model.addAttribute(ConstantsController.UNIDAD_ADMINISTRATIVA, unidadAdministrativaHidden);
    	
    	return ASISTENCIA_DIRECCION;
    }
    
    @RequestMapping(value={"direccion/dictaminaDescuento"}, method = RequestMethod.POST)
    public String dictaminaDescuento(Model model, String cveMusuario, Integer idAsistenciaHidden, String nombreHidden, String paternoHidden,
    		String maternoHidden, String nivelHidden, Integer tipoHidden, Integer estadoHidden, String unidadAdministrativaHidden, Integer idTipoDia, 
    		Integer idJustificacion, String fechaInicial, String fechaFinal, String dictaminacion, Authentication authentication ) {
    	
    	Integer resultadoProceso = 0;

    	resultadoProceso = asistenciaService.dictaminaIncidencia(idAsistenciaHidden, idTipoDia, idJustificacion, dictaminacion, authentication);
    	
    	AsistenciaBusquedaUtil asistenciaBusquedaUtil = new AsistenciaBusquedaUtil();
		
		asistenciaBusquedaUtil.setCveMusuario(cveMusuario);
		asistenciaBusquedaUtil.setNombre(nombreHidden);
		asistenciaBusquedaUtil.setPaterno(paternoHidden);
		asistenciaBusquedaUtil.setMaterno(maternoHidden);
		asistenciaBusquedaUtil.setNivel(nivelHidden);
		asistenciaBusquedaUtil.setTipo(tipoHidden);
		asistenciaBusquedaUtil.setEstado(estadoHidden);
		asistenciaBusquedaUtil.setFechaInicial(fechaInicial);
		asistenciaBusquedaUtil.setFechaFinal(fechaFinal);
		asistenciaBusquedaUtil.setUnidadAdministrativa(unidadAdministrativaHidden);
    	
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoDireccion(asistenciaBusquedaUtil, authentication);
    	
    	List<EstatusDto> estatus =  estatusService.obtieneListaCompletaEstatus(authentication);
    	List<EstatusDto> listaEstado = new ArrayList<>();
    	
    	for (EstatusDto e : estatus) {
    		if (e.getIdEstatus() != 4 && e.getIdEstatus() != 5) {
    			listaEstado.add(e);
    		}
    	}
    	
    	model.addAttribute(ConstantsController.LISTA_TIPO, catalogoService.obtieneTipoDias(authentication));
    	model.addAttribute(ConstantsController.LISTA_NIVEL, catalogoService.obtieneNiveles(authentication));
    	model.addAttribute(ConstantsController.LISTA_ESTADO, listaEstado);
    	model.addAttribute(ConstantsController.LISTA_ASISTENCIA, asistencia);
    	model.addAttribute(ConstantsController.FECHA_INICIAL, fechaInicial);
    	model.addAttribute(ConstantsController.FECHA_FINAL, fechaFinal);
    	model.addAttribute(ConstantsController.CVE_M_USUARIO, cveMusuario);
    	model.addAttribute(ConstantsController.NOMBRE, nombreHidden);
    	model.addAttribute(ConstantsController.PATERNO, paternoHidden);
    	model.addAttribute(ConstantsController.MATERNO, maternoHidden);
    	model.addAttribute(ConstantsController.NIVEL, nivelHidden);
    	model.addAttribute(ConstantsController.TIPO, tipoHidden);
    	model.addAttribute(ConstantsController.ESTADO, estadoHidden);
    	model.addAttribute(ConstantsController.UNIDAD_ADMINISTRATIVA, unidadAdministrativaHidden);

    	if (resultadoProceso == 1) {
    		if (dictaminacion.equals("Autorizar")) {
    			model.addAttribute(ConstantsController.MENSAJE, MENSAJE_AUTORIZA_DESCUENTO);
    		} else if (dictaminacion.equals("Rechazar")) {
    			model.addAttribute(ConstantsController.MENSAJE_EXCEPTION, MENSAJE_RECHAZA_DESCUENTO);
    		}
    	} else if (resultadoProceso == 0) {
    		model.addAttribute("MENSAJE_EXCEPCION", MENSAJE_EXCEPCION);
    	}
    	
    	return ASISTENCIA_DIRECCION;
    }
    //TERMINA DIRECCION
    
    @GetMapping("buscaId")
    @ResponseBody
    public AsistenciaJustificacion buscaAsistenciaPorId(Integer id, Authentication authentication) {
    	
    	Asistencia asistencia = asistenciaService.buscaAsistenciaPorId(id, authentication);
    	asistencia.getUsuarioDto().setFechaIngreso(asistencia.getUsuarioDto().getFechaIngreso());
    	
    	AsistenciaJustificacion asistenciaJustificacion = new AsistenciaJustificacion(); 
    	asistenciaJustificacion.setAsistencia(asistencia);
    	asistenciaJustificacion.setListaJustificacion(catalogoService.obtieneJustificaciones(authentication));
    	asistenciaJustificacion.setListaAutorizador(usuarioService.obtieneListaJefes(authentication));
    	
    	return asistenciaJustificacion;
    }
    
    @RequestMapping(value={"archivo"}, method = RequestMethod.GET)
    public void download(HttpServletResponse response, Integer id, Authentication authentication) {
    	
    	AsistenciaJustificacion asistenciaJustificacion = new AsistenciaJustificacion(); 
    	asistenciaJustificacion.setAsistencia(asistenciaService.buscaAsistenciaPorId(id, authentication));
    	
    	String rutaArchivo = 
    			asistenciaJustificacion.getAsistencia().getIncidencia().getIdArchivo().getUrl() 
    			+ "/" + 
    			asistenciaJustificacion.getAsistencia().getIncidencia().getIdArchivo().getNombre();
    	
		try (InputStream inputStream = new FileInputStream(new File(rutaArchivo));)	{
			IOUtils.copy(inputStream, response.getOutputStream());
	        response.flushBuffer();
		} catch (IOException e) {
			logger.warn(ConstantsController.ERROR,e);
		}
        
    }
    
    private void obtieneListaAsistenciasAjustificar(List<Integer> idAsistencias, String nombreJefe, String motivo, List<Asistencia> listaAsistenciaJustificar, Authentication authentication) {
    	for (Integer idAsistencia : idAsistencias) {
    		Asistencia asistenciaJustificar = asistenciaService.buscaAsistenciaPorId(idAsistencia, authentication);
    		listaAsistenciaJustificar.add(asistenciaJustificar);
    		
    		//se obtiene el jefe del usuario
    		if (nombreJefe.isEmpty()) {
    			nombreJefe = asistenciaJustificar.getUsuarioDto().getNombreJefe();
    		}
    		
    		if (motivo.isEmpty() && asistenciaJustificar.getIncidencia().getJustificacion().getJustificacion() != null) {
				motivo = asistenciaJustificar.getIncidencia().getJustificacion().getJustificacion();
    		}
    	}
    }
    
    private void defineMensajeUsuario(int resultadoProceso, String dictaminacion, Model model) {
    	if (resultadoProceso != 0) {
    		if (dictaminacion.equals("Autorizar")) {
    			if (resultadoProceso == 1) {
    				model.addAttribute(ConstantsController.MENSAJE, MENSAJE_AUTORIZA_JUSTIFICACION);
    			} else if (resultadoProceso == 2) {
    				model.addAttribute(ConstantsController.MENSAJE, MENSAJE_RECHAZA_DESCUENTO);
    			}
    		} else if (dictaminacion.equals("Rechazar")) {
    			if (resultadoProceso == 3) {
    				model.addAttribute(ConstantsController.MENSAJE, MENSAJE_RECHAZA_JUSTIFICACION);
    			} else if (resultadoProceso == 2) {
    				model.addAttribute(ConstantsController.MENSAJE, MENSAJE_RECHAZA_DESCUENTO);
    			}
    		}
    	} else {
    		model.addAttribute(ConstantsController.MENSAJE_EXCEPTION, MENSAJE_EXCEPCION);
    	}
    }
    
}