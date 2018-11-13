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
import mx.gob.segob.dgtic.web.mvc.dto.Justificacion;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;
import mx.gob.segob.dgtic.web.mvc.dto.reporte;
import mx.gob.segob.dgtic.web.mvc.service.ArchivoService;
import mx.gob.segob.dgtic.web.mvc.service.AsistenciaService;
import mx.gob.segob.dgtic.web.mvc.service.CatalogoService;
import mx.gob.segob.dgtic.web.mvc.service.EstatusService;
import mx.gob.segob.dgtic.web.mvc.service.UnidadAdministrativaService;
import mx.gob.segob.dgtic.web.mvc.service.UsuarioService;
import mx.gob.segob.dgtic.web.mvc.util.AsistenciaJustificacion;
import mx.gob.segob.dgtic.web.mvc.util.Excel;
import mx.gob.segob.dgtic.web.mvc.util.FormatoIncidencia;

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
	
	//JUSTIFICACION
	private String MENSAJE_REGISTRO_JUSTIFICACION = "La justificación ha quedado registrada";
	
	//DESCUENTO
	private String MENSAJE_REGISTRO_DESCUENTO     = "La incidencia ha sido marcada para enviar a descuento";
	private String MENSAJE_AUTORIZA_DESCUENTO     = "Esta incidencia será enviada a descuento";
	private String MENSAJE_RECHAZA_DESCUENTO      = "El descuento ha sido aceptado";
	
	private String MENSAJE_EXCEPCION = "No se registró la operación.";
	
	//EMPLEADO
	@RequestMapping(value={"empleado"}, method = RequestMethod.GET)
    public String buscaListaAsistenciaEmpleado(Model model) {
    	
    	model.addAttribute("listaAsistencia", new ArrayList<Asistencia>());
    	model.addAttribute("listaAsistenciaJustificar", new ArrayList<Asistencia>());
    	model.addAttribute("inicio", true);
    	
    	return "/asistencia/empleado";
    }
	
	@RequestMapping(value={"empleado/busca"}, method = RequestMethod.GET, params="busca")
    public String buscaListaAsistenciaEmpleadoRango(Model model, Authentication authentication, String fechaInicial, String fechaFinal) {

    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRango(authentication.getName(), fechaInicial, fechaFinal, authentication);
    	model.addAttribute("listaAsistencia", asistencia);
    	model.addAttribute("fechaInicial", fechaInicial);
    	model.addAttribute("fechaFinal", fechaFinal);
    	
    	return "/asistencia/empleado";
    }
	
	@RequestMapping(value="empleado/busca", method=RequestMethod.GET, params="exporta")
    public ModelAndView exportaExcelEmpleado(HttpServletRequest request, HttpServletResponse response, Authentication authentication, String fechaInicial, String fechaFinal) {
        
	    	List<Asistencia> listaAsistencia = asistenciaService.buscaAsistenciaEmpleadoRango(authentication.getName(), fechaInicial, fechaFinal, authentication);
	    	
	    	Map<String, Object> model = new HashMap<>();
	        
	    	//nombre de la hoja
	        model.put("nombreHoja", "asistencia");
	        
	        //nombres columnas
	        List<String> cabeceras = new ArrayList<>();
	        cabeceras.add("Id Asistencia");
	        cabeceras.add("Entrada");
	        cabeceras.add("Id Status");
	        cabeceras.add("Id Tipo");
	        cabeceras.add("Salida");
	        cabeceras.add("Usuario");
	        model.put("cabeceras", cabeceras);
	        
	        //Información registros (List<Object[]>)
	        List<List<String>> asistencias = new ArrayList<>();
	        
	        for (Asistencia a : listaAsistencia) {
	        	
	        	List<String> elementos = new ArrayList<>();
	        	elementos.add(a.getIdAsistencia() != null ? a.getIdAsistencia().toString() : new String(""));
	        	elementos.add(a.getEntrada().toString());
	        	elementos.add(a.getIdEstatus().getEstatus() != null ? a.getIdEstatus().getEstatus() : new String(""));
	        	elementos.add(a.getIdTipoDia().getNombre() != null ? a.getIdTipoDia().getNombre() : new String(""));
	        	elementos.add(a.getSalida() != null ? a.getSalida().toString() : new String(""));
	        	elementos.add(a.getUsuarioDto() != null ? a.getUsuarioDto().getClaveUsuario() : new String(""));
	        	asistencias.add(elementos);
	        }
	        
	        model.put("registros", asistencias);
	        
	        response.setContentType( "application/ms-excel" );
	        response.setHeader( "Content-disposition", "attachment; filename=SESNSP.xls" );         
	        
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
    	
    	model.addAttribute("listaTipo", catalogoService.obtieneTipoDias(authentication));
    	model.addAttribute("listaNivel", catalogoService.obtieneNiveles(authentication));
    	model.addAttribute("listaEstado", estatusService.obtieneListaCompletaEstatus(authentication));
    	model.addAttribute("listaAsistencia", new ArrayList<Asistencia>());
    	model.addAttribute("listaAsistenciaJustificar", new ArrayList<Asistencia>());
    	model.addAttribute("inicio", true);
    	
    	return "/asistencia/coordinador";
    }
    
    @RequestMapping(value={"coordinador/busca"}, method = RequestMethod.GET, params="busca")
    public String buscaListaAsistenciaCoordinadorRango(Model model, Authentication authentication, String cve_m_usuario, String nombre, String paterno, 
    		String materno, String nivel, Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa) {

	    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(cve_m_usuario, nombre, paterno, materno, nivel, 
	    			tipo, estado, fechaInicial, fechaFinal, "", authentication.getName(), authentication);
	    	
	    	model.addAttribute("listaTipo", catalogoService.obtieneTipoDias(authentication));
	    	model.addAttribute("listaNivel", catalogoService.obtieneNiveles(authentication));
	    	model.addAttribute("listaEstado", estatusService.obtieneListaCompletaEstatus(authentication));
	    	model.addAttribute("listaAsistencia", asistencia);
	    	model.addAttribute("listaAsistenciaJustificar", new ArrayList<Asistencia>());
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
	    	
	    	//se realizó búsqueda por usuario
	    	if (!cve_m_usuario.isEmpty()) {
	    		model.addAttribute("activaCheckbox", true);
	    	}
	    	
	    	return "/asistencia/coordinador";
    }
    
    @RequestMapping(value="coordinador/busca", method=RequestMethod.GET, params="exporta")
    public ModelAndView exportaExcelCoordinador(HttpServletRequest request, HttpServletResponse response, Authentication authentication, String cve_m_usuario, String nombre, String paterno, 
    		String materno, String nivel, Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa) {
        
    	List<Asistencia> listaAsistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(cve_m_usuario, nombre, paterno, materno, nivel, 
    			tipo, estado, fechaInicial, fechaFinal, "", authentication.getName(), authentication);
    	
    	Map<String, Object> model = new HashMap<>();
        
    	//nombre de la hoja
        model.put("nombreHoja", "asistencia");
        
        //nombres columnas
        List<String> cabeceras = new ArrayList<>();
        cabeceras.add("Id Asistencia");
        cabeceras.add("Entrada");
        cabeceras.add("Id Status");
        cabeceras.add("Id Tipo");
        cabeceras.add("Salida");
        cabeceras.add("Usuario");
        model.put("cabeceras", cabeceras);
        
        //Información registros (List<Object[]>)
        List<List<String>> asistencias = new ArrayList<>();
        
        for (Asistencia a : listaAsistencia) {
        	
        	List<String> elementos = new ArrayList<>();
        	elementos.add(a.getIdAsistencia() != null ? a.getIdAsistencia().toString() : new String(""));
        	elementos.add(a.getEntrada().toString());
        	elementos.add(a.getIdEstatus().getEstatus() != null ? a.getIdEstatus().getEstatus() : new String(""));
        	elementos.add(a.getIdTipoDia().getNombre() != null ? a.getIdTipoDia().getNombre() : new String(""));
        	elementos.add(a.getSalida() != null ? a.getSalida().toString() : new String(""));
        	elementos.add(a.getUsuarioDto() != null ? a.getUsuarioDto().getClaveUsuario() : new String(""));
        	asistencias.add(elementos);
        }
        
        model.put("registros", asistencias);
        
        response.setContentType( "application/ms-excel" );
        response.setHeader( "Content-disposition", "attachment; filename=ReporteSESNSP.xls" );         
        
        return new ModelAndView(new Excel(), model);
    	
    }
    
    @RequestMapping(value={"creaIncidencia"}, method = RequestMethod.POST, params="justifica")
    public String creaIncidencia(Model model, String cve_m_usuario_hidden, Integer idAsistenciaHidden, Integer idTipoDia, Integer idJustificacion, 
    		String nombreHidden, String paternoHidden, String maternoHidden, String nivelHidden, Integer tipoHidden, Integer estadoHidden, String fechaInicial, 
    		String fechaFinal, String unidadAdministrativaHidden, Authentication authentication, MultipartFile archivoSubido, String cve_m_usuario, String nombreAutorizador) {
    	
    	Integer resultadoProceso = 0;
    	Archivo archivo = null;
    	
    	try {
    		//guarda el archivo
    		if (archivoSubido.getSize() > 0) {
    			archivo = archivoService.guardaArchivo(archivoSubido, cve_m_usuario, "asistencia_justificacion", "justificacion-", authentication);
    		}
    		
    		if (archivo != null) {
	    		//crea la incidencia y asocia el archivo
	    		resultadoProceso = asistenciaService.creaIncidencia(idAsistenciaHidden, idTipoDia, idJustificacion, archivo.getIdArchivo(), nombreAutorizador, authentication);
    		} 

    	} catch(Exception e) {
    		new Exception("No se logró crear la incidencia " + e.getMessage());
    	}
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(cve_m_usuario_hidden, nombreHidden, paternoHidden, maternoHidden,
    			nivelHidden, tipoHidden, estadoHidden, fechaInicial, fechaFinal, unidadAdministrativaHidden, authentication.getName(), authentication );
    	
    	model.addAttribute("listaTipo", catalogoService.obtieneTipoDias(authentication));
    	model.addAttribute("listaNivel", catalogoService.obtieneNiveles(authentication));
    	model.addAttribute("listaEstado", estatusService.obtieneListaCompletaEstatus(authentication));
    	model.addAttribute("listaAsistencia", asistencia);
    	model.addAttribute("listaAsistenciaJustificar", new ArrayList<Asistencia>());
    	model.addAttribute("fechaInicial", fechaInicial);
    	model.addAttribute("fechaFinal", fechaFinal);
    	model.addAttribute("cve_m_usuario", cve_m_usuario_hidden);
    	model.addAttribute("nombre", nombreHidden);
    	model.addAttribute("paterno", paternoHidden);
    	model.addAttribute("materno", maternoHidden);
    	model.addAttribute("nivel", nivelHidden);
    	model.addAttribute("tipo", tipoHidden);
    	model.addAttribute("estado", estadoHidden);
    	model.addAttribute("unidadAdministrativa", unidadAdministrativaHidden);
    	
    	if (resultadoProceso == 1) {
    		model.addAttribute("MENSAJE", MENSAJE_REGISTRO_JUSTIFICACION);
    	} else if (resultadoProceso == 0) {
    		model.addAttribute("MENSAJE_EXCEPCION", MENSAJE_EXCEPCION);
    	}
    	
    	return "/asistencia/coordinador";
    }
    
    @RequestMapping(value={"creaIncidencia"}, method = RequestMethod.POST, params="descuenta")
    public String creaDescuento(Model model, String cve_m_usuario_hidden, Integer idAsistenciaHidden, Integer idTipoDia, Integer idJustificacion, 
    		String nombreHidden, String paternoHidden, String maternoHidden, String nivelHidden, Integer tipoHidden, Integer estadoHidden, String fechaInicial, 
    		String fechaFinal, String unidadAdministrativaHidden, Authentication authentication, MultipartFile archivoSubido, String cve_m_usuario, String nombreAutorizador) {
    	
    	Integer resultadoProceso = 0;
    	Archivo archivo = null;
    	
    	try {
    		//guarda el archivo
    		if (archivoSubido.getSize() > 0) {
    			archivo = archivoService.guardaArchivo(archivoSubido, cve_m_usuario, "descuento" , "descuento-", authentication);
    		}
    		
    		if (archivo != null) {
	    		//crea la petición de descuento y asocia el archivo
	    		resultadoProceso = asistenciaService.creaDescuento(idAsistenciaHidden, idTipoDia, idJustificacion, archivo.getIdArchivo(), nombreAutorizador, authentication);
    		} 

    	} catch(Exception e) {
    		new Exception("No se logró crear la incidencia " + e.getMessage());
    	}
    	
		List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(cve_m_usuario_hidden, nombreHidden, paternoHidden, maternoHidden,
    			nivelHidden, tipoHidden, estadoHidden, fechaInicial, fechaFinal, unidadAdministrativaHidden, authentication.getName(), authentication);
    	
		model.addAttribute("listaTipo", catalogoService.obtieneTipoDias(authentication));
		model.addAttribute("listaNivel", catalogoService.obtieneNiveles(authentication));
		model.addAttribute("listaEstado", estatusService.obtieneListaCompletaEstatus(authentication));
    	model.addAttribute("listaAsistencia", asistencia);
    	model.addAttribute("listaAsistenciaJustificar", new ArrayList<Asistencia>());
    	model.addAttribute("fechaInicial", fechaInicial);
    	model.addAttribute("fechaFinal", fechaFinal);
    	model.addAttribute("cve_m_usuario", cve_m_usuario_hidden);
    	model.addAttribute("nombre", nombreHidden);
    	model.addAttribute("paterno", paternoHidden);
    	model.addAttribute("materno", maternoHidden);
    	model.addAttribute("nivel", nivelHidden);
    	model.addAttribute("tipo", tipoHidden);
    	model.addAttribute("estado", estadoHidden);
    	model.addAttribute("unidadAdministrativa", unidadAdministrativaHidden);
    	
    	if (resultadoProceso == 1) {
    		model.addAttribute("MENSAJE", MENSAJE_REGISTRO_DESCUENTO);
    	} else if (resultadoProceso == 0) {
    		model.addAttribute("MENSAJE_EXCEPCION", MENSAJE_EXCEPCION);
    	}
    	
    	return "/asistencia/coordinador";
    }
    
    @RequestMapping(value={"creaIncidencia"}, method = RequestMethod.POST, params="formatoJustificacion")
    public String descargaFormatoJustificacion(Model model, String nombre, String unidad, String nombreAutorizador, String cve_m_usuario_hidden, Integer idAsistenciaHidden, Integer idTipoDia, Integer idJustificacion, 
    		String nombreHidden, String paternoHidden, String maternoHidden, String nivelHidden, Integer tipoHidden, Integer estadoHidden, String fechaInicial, 
    		String fechaFinal, String unidadAdministrativaHidden, Authentication authentication, String cve_m_usuario, Integer idTipoDiaModal, HttpServletResponse response) {
    	
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
    	SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
    	String fechaActual = f.format(fechaHoy);
    	
    	try {
			reporte archivo = asistenciaService.formatoJustificacion(new FormatoIncidencia(nombre, unidad, fechaActual, codigoincidencia, "", ""), authentication);
			InputStream targetStream = new ByteArrayInputStream(archivo.getNombre());
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
			e.printStackTrace();
		}
    	
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(cve_m_usuario_hidden, nombreHidden, paternoHidden, maternoHidden,
    			nivelHidden, tipoHidden, estadoHidden, fechaInicial, fechaFinal, unidadAdministrativaHidden, authentication.getName(), authentication);
    	
    	model.addAttribute("listaTipo", catalogoService.obtieneTipoDias(authentication));
    	model.addAttribute("listaNivel", catalogoService.obtieneNiveles(authentication));
    	model.addAttribute("listaEstado", estatusService.obtieneListaCompletaEstatus(authentication));
    	model.addAttribute("listaAsistencia", asistencia);
    	model.addAttribute("listaAsistenciaJustificar", new ArrayList<Asistencia>());
    	model.addAttribute("fechaInicial", fechaInicial);
    	model.addAttribute("fechaFinal", fechaFinal);
    	model.addAttribute("cve_m_usuario", cve_m_usuario_hidden);
    	model.addAttribute("nombre", nombreHidden);
    	model.addAttribute("paterno", paternoHidden);
    	model.addAttribute("materno", maternoHidden);
    	model.addAttribute("nivel", nivelHidden);
    	model.addAttribute("tipo", tipoHidden);
    	model.addAttribute("estado", estadoHidden);
    	model.addAttribute("unidadAdministrativa", unidadAdministrativaHidden);
    	
    	return "/asistencia/coordinador";
    }
    
    @RequestMapping(value={"creaIncidencia"}, method = RequestMethod.POST, params="formatoDescuento")
    public String descargaFormatoDescuento(Model model, String nombre, String unidad, String nombreAutorizador, String cve_m_usuario_hidden, Integer idAsistenciaHidden, Integer idTipoDia, Integer idJustificacion, 
    		String nombreHidden, String paternoHidden, String maternoHidden, String nivelHidden, Integer tipoHidden, Integer estadoHidden, String fechaInicial, 
    		String fechaFinal, String unidadAdministrativaHidden, Authentication authentication, String cve_m_usuario, HttpServletResponse response,
    		String fechaIncidenciaHidden) {
    	
    	//fecha actual para el reporte
    	Date fechaHoy = new Date();
    	SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
    	String fechaActual = f.format(fechaHoy);
    	
    	try {
			reporte archivo = asistenciaService.formatoDescuento(new FormatoIncidencia(nombre, "", fechaActual, "", cve_m_usuario, fechaIncidenciaHidden), authentication);
			InputStream targetStream = new ByteArrayInputStream(archivo.getNombre());
			String mimeType = URLConnection.guessContentTypeFromStream(targetStream);
			
			if(mimeType == null){
				mimeType = "application/pdf";
			}
			
			response.setContentType(mimeType);
			response.setHeader( "Content-Disposition", "attachment;filename= Formato Descuento.pdf" );
			IOUtils.copy(targetStream, response.getOutputStream());
	        ServletOutputStream stream = response.getOutputStream();
	        stream.flush();
	        response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(cve_m_usuario_hidden, nombreHidden, paternoHidden, maternoHidden,
    			nivelHidden, tipoHidden, estadoHidden, fechaInicial, fechaFinal, unidadAdministrativaHidden, authentication.getName(), authentication);
    	
    	model.addAttribute("listaTipo", catalogoService.obtieneTipoDias(authentication));
    	model.addAttribute("listaNivel", catalogoService.obtieneNiveles(authentication));
    	model.addAttribute("listaEstado", estatusService.obtieneListaCompletaEstatus(authentication));
    	model.addAttribute("listaAsistencia", asistencia);
    	model.addAttribute("listaAsistenciaJustificar", new ArrayList<Asistencia>());
    	model.addAttribute("fechaInicial", fechaInicial);
    	model.addAttribute("fechaFinal", fechaFinal);
    	model.addAttribute("cve_m_usuario", cve_m_usuario_hidden);
    	model.addAttribute("nombre", nombreHidden);
    	model.addAttribute("paterno", paternoHidden);
    	model.addAttribute("materno", maternoHidden);
    	model.addAttribute("nivel", nivelHidden);
    	model.addAttribute("tipo", tipoHidden);
    	model.addAttribute("estado", estadoHidden);
    	model.addAttribute("unidadAdministrativa", unidadAdministrativaHidden);
    	
    	return "/asistencia/coordinador";
    }
    
    @RequestMapping(value={"coordinador/buscaAsistenciasPorId"}, method = RequestMethod.POST)
    public String buscaAsistenciasPorId(Model model, Integer[] checkboxes, String[] arregloIdAsistencias, String cve_m_usuario_hidden_lista_multiple, String fechaInicial_hidden_lista_multiple, 
    		String fechaFinal_hidden_lista_multiple, Authentication authentication) {
    	
    	List<Asistencia> listaAsistenciaJustificar = new ArrayList<>();
    	String nombreJefe = "";
    	String motivo = "";
    	
    	List<Integer> idAsistencias = new ArrayList<>();
    	
    	for (String a : arregloIdAsistencias) {
    		idAsistencias.add(Integer.parseInt(a));
    	}
    	
    	if (idAsistencias != null) {
	    	for (Integer idAsistencia : idAsistencias) {
	    		Asistencia asistenciaJustificar = asistenciaService.buscaAsistenciaPorId(idAsistencia, authentication);
	    		listaAsistenciaJustificar.add(asistenciaJustificar);
	    		
	    		//se obtiene el jefe del usuario
	    		if (nombreJefe.isEmpty()) {
	    			nombreJefe = asistenciaJustificar.getUsuarioDto().getNombreJefe();
	    		}
	    		
	    		if (motivo.isEmpty()) {
	    			if (asistenciaJustificar.getIncidencia().getJustificacion().getJustificacion() != null) {
	    				motivo = asistenciaJustificar.getIncidencia().getJustificacion().getJustificacion();
	    			}
	    		}
	    	}
	    	
	    	List<Justificacion> listaJustificaciones = catalogoService.obtieneJustificaciones(authentication);
	    	List<Usuario> listaJefes = usuarioService.obtieneListaJefes(authentication);
	    	
	    	model.addAttribute("listaTipo", catalogoService.obtieneTipoDias(authentication));
	    	model.addAttribute("listaNivel", catalogoService.obtieneNiveles(authentication));
	    	model.addAttribute("listaEstado", estatusService.obtieneListaCompletaEstatus(authentication));
	    	model.addAttribute("listaAsistencia", new ArrayList<Asistencia>());
	    	model.addAttribute("listaAsistenciaJustificar", listaAsistenciaJustificar);
	    	model.addAttribute("listaJustificaciones", listaJustificaciones);
	    	model.addAttribute("listaAutorizadores", listaJefes);
	    	model.addAttribute("nombreJefe", nombreJefe);
	    	model.addAttribute("justificacion", motivo);
	    	model.addAttribute("fechaInicial", fechaInicial_hidden_lista_multiple);
	    	model.addAttribute("fechaFinal", fechaFinal_hidden_lista_multiple);
	    	model.addAttribute("cve_m_usuario", cve_m_usuario_hidden_lista_multiple);
    	} else {
    		List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(cve_m_usuario_hidden_lista_multiple, "", "", "",
        			"", 0, 0, fechaInicial_hidden_lista_multiple, fechaFinal_hidden_lista_multiple, "", authentication.getName(), authentication);
    		
    		model.addAttribute("listaTipo", catalogoService.obtieneTipoDias(authentication));
    		model.addAttribute("listaNivel", catalogoService.obtieneNiveles(authentication));
    		model.addAttribute("listaEstado", estatusService.obtieneListaCompletaEstatus(authentication));
    		model.addAttribute("listaAsistencia", asistencia);
	    	model.addAttribute("listaAsistenciaJustificar", new ArrayList<Asistencia>());
	    	model.addAttribute("fechaInicial", fechaInicial_hidden_lista_multiple);
	    	model.addAttribute("fechaFinal", fechaFinal_hidden_lista_multiple);
	    	model.addAttribute("cve_m_usuario", cve_m_usuario_hidden_lista_multiple);
	    	
	    	//se realizó búsqueda por usuario
	    	if (!cve_m_usuario_hidden_lista_multiple.isEmpty()) {
	    		model.addAttribute("activaCheckbox", true);
	    	}
    	}
    	
    	return "/asistencia/coordinador";
    }
    
    @RequestMapping(value={"coordinador/justificaMultiple"}, method = RequestMethod.POST, params = "justificacionMultipleGuarda")
    public String creaIncidencias(Model model, Integer[] listaIdAsistencias, MultipartFile archivoSubido, Integer selectJustificacion, String nombreAutorizador,
    		String cve_m_usuario_hidden_guarda_multiple, String fechaInicial_hidden_guarda_multiple, String fechaFinal_hidden_guarda_multiple, Authentication authentication) {

    	Integer resultadoProceso = 0;
    	Archivo archivo = null;
    	Integer idJustificacion = selectJustificacion;
    	
    	for (Integer idAsistencia : listaIdAsistencias) {
    		Asistencia asistencia = asistenciaService.buscaAsistenciaPorId(idAsistencia, authentication);
    		Integer idTipoDia = asistencia.getIdTipoDia().getIdTipoDia();
    		String cve_m_usuario = asistencia.getUsuarioDto().getClaveUsuario();
    		
    		try {
        		//guarda el archivo
        		if (archivoSubido.getSize() > 0) {
    				archivo = archivoService.guardaArchivo(archivoSubido, cve_m_usuario, "justificacion", "justificacion-", authentication);
        			
    				if (archivo != null) {
	        			//crea la incidencia y asocia el archivo
	            		resultadoProceso = asistenciaService.creaIncidencia(idAsistencia, idTipoDia, idJustificacion, archivo.getIdArchivo(), nombreAutorizador, authentication);
    				}
        		}
        	} catch(Exception e) {
        		new Exception("No se logró crear la incidencia " + e.getMessage());
        	}
    	}
    	
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(cve_m_usuario_hidden_guarda_multiple, "", "", "", "", 0, 0, 
    			fechaInicial_hidden_guarda_multiple, fechaFinal_hidden_guarda_multiple, "", authentication.getName(), authentication);
    	
    	model.addAttribute("listaTipo", catalogoService.obtieneTipoDias(authentication));
    	model.addAttribute("listaNivel", catalogoService.obtieneNiveles(authentication));
    	model.addAttribute("listaEstado", estatusService.obtieneListaCompletaEstatus(authentication));
    	model.addAttribute("listaAsistencia", asistencia);
    	model.addAttribute("listaAsistenciaJustificar", new ArrayList<>());
    	model.addAttribute("fechaInicial", fechaInicial_hidden_guarda_multiple);
    	model.addAttribute("fechaFinal", fechaFinal_hidden_guarda_multiple);
    	model.addAttribute("cve_m_usuario", cve_m_usuario_hidden_guarda_multiple);
    	
    	if (resultadoProceso == 1) {
    		model.addAttribute("MENSAJE", MENSAJE_REGISTRO_JUSTIFICACION);
    	} else if (resultadoProceso == 0) {
    		model.addAttribute("MENSAJE_EXCEPCION", MENSAJE_EXCEPCION);
    	}
    	
    	return "/asistencia/coordinador";
    }
    
    @RequestMapping(value={"coordinador/justificaMultiple"}, method = RequestMethod.POST, params = "formatoJustificacionMultipleBtn")
    public String descargaFormatoJustificacionMultiple(Model model, Integer[] listaIdAsistencias, MultipartFile archivo, Integer selectJustificacion, 
    		String nombreAutorizador, String cve_m_usuario_hidden_guarda_multiple, String fechaInicial_hidden_guarda_multiple, 
    		String fechaFinal_hidden_guarda_multiple, Authentication authentication, HttpServletResponse response) {
    	
    	Asistencia asistencia = new Asistencia();
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
			e.printStackTrace();
		}
    	
    	List<Asistencia> listaAsistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(cve_m_usuario_hidden_guarda_multiple, "", "", "", "", 0, 0, 
    			fechaInicial_hidden_guarda_multiple, fechaFinal_hidden_guarda_multiple, "", authentication.getName(), authentication);
   	
    	model.addAttribute("listaAsistencia", listaAsistencia);
    	model.addAttribute("listaAsistenciaJustificar", new ArrayList<>());
    	model.addAttribute("fechaInicial", fechaInicial_hidden_guarda_multiple);
    	model.addAttribute("fechaFinal", fechaFinal_hidden_guarda_multiple);
    	model.addAttribute("cve_m_usuario", cve_m_usuario_hidden_guarda_multiple);
   	
    	return "/asistencia/coordinador";
    }
    
    //TERMINA COORDINADOR
    
    //DIRECCIÓN
    @RequestMapping(value={"direccion"}, method = RequestMethod.GET)
    public String buscaListaAsistenciaDireccion(Model model, Authentication authentication) {
    	
    	model.addAttribute("listaTipo", catalogoService.obtieneTipoDias(authentication));
    	model.addAttribute("listaNivel", catalogoService.obtieneNiveles(authentication));
    	model.addAttribute("listaEstado", estatusService.obtieneListaCompletaEstatus(authentication));
    	model.addAttribute("listaUnidadAdministrativa", unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
    	model.addAttribute("listaAsistencia", new ArrayList<Asistencia>());
    	model.addAttribute("listaAsistenciaJustificar", new ArrayList<Asistencia>());
    	model.addAttribute("inicio", true);
    	
    	return "/asistencia/direccion";
    }
    
    @RequestMapping(value={"direccion/busca"}, method = RequestMethod.GET, params="busca")
    public String buscaListaAsistenciaDireccionRango(Model model, Authentication authentication, String cve_m_usuario, String nombre, String paterno, 
    		String materno, String nivel, Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa) {

	    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoDireccion(cve_m_usuario, nombre, paterno, materno, nivel, 
	    			tipo, estado, fechaInicial, fechaFinal, unidadAdministrativa, authentication);
	    	
	    	model.addAttribute("listaTipo", catalogoService.obtieneTipoDias(authentication));
	    	model.addAttribute("listaNivel", catalogoService.obtieneNiveles(authentication));
	    	model.addAttribute("listaEstado", estatusService.obtieneListaCompletaEstatus(authentication));
	    	model.addAttribute("listaUnidadAdministrativa", unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
	    	model.addAttribute("listaAsistencia", asistencia);
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
	    	
	    	return "/asistencia/direccion";
    }
    
    @RequestMapping(value="direccion/busca", method=RequestMethod.GET, params="exporta")
    public ModelAndView exportaExcelDireccion(HttpServletRequest request, HttpServletResponse response, Authentication authentication, 
    		String cve_m_usuario, String nombre, String paterno, String materno, String nivel, Integer tipo, Integer estado, String fechaInicial, String fechaFinal, String unidadAdministrativa) {
        
    	List<Asistencia> listaAsistencia = asistenciaService.buscaAsistenciaEmpleadoRangoDireccion(cve_m_usuario, nombre, paterno, materno, nivel, 
    			tipo, estado, fechaInicial, fechaFinal, unidadAdministrativa, authentication);
    	
    	Map<String, Object> model = new HashMap<>();
        
    	//nombre de la hoja
        model.put("nombreHoja", "asistencia");
        
        //nombres columnas
        List<String> cabeceras = new ArrayList<>();
        cabeceras.add("Id Asistencia");
        cabeceras.add("Entrada");
        cabeceras.add("Id Status");
        cabeceras.add("Id Tipo");
        cabeceras.add("Salida");
        cabeceras.add("Usuario");
        model.put("cabeceras", cabeceras);
        
        //Información registros (List<Object[]>)
        List<List<String>> asistencias = new ArrayList<>();
        
        for (Asistencia a : listaAsistencia) {
        	
        	List<String> elementos = new ArrayList<>();
        	elementos.add(a.getIdAsistencia() != null ? a.getIdAsistencia().toString() : new String(""));
        	elementos.add(a.getEntrada().toString());
        	elementos.add(a.getIdEstatus().getEstatus() != null ? a.getIdEstatus().getEstatus() : new String(""));
        	elementos.add(a.getIdTipoDia().getNombre() != null ? a.getIdTipoDia().getNombre() : new String(""));
        	elementos.add(a.getSalida() != null ? a.getSalida().toString() : new String(""));
        	elementos.add(a.getUsuarioDto() != null ? a.getUsuarioDto().getClaveUsuario() : new String(""));
        	asistencias.add(elementos);
        }
        
        model.put("registros", asistencias);
        
        response.setContentType( "application/ms-excel" );
        response.setHeader( "Content-disposition", "attachment; filename=SESNSP.xls" );         
        
        return new ModelAndView(new Excel(), model);
    	
    	
    }
    
    @RequestMapping(value={"direccion/dictamina_Incidencia_Descuento"}, method = RequestMethod.POST) //permite aprobar o rechazar justificaciones y descuentos
    public String dictaminaIncidenciaDescuento(Model model, String cve_m_usuario_hidden, Integer idAsistenciaHidden, String nombreHidden, String paternoHidden,
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

    		Archivo archivoAsociado= new Archivo();
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
				e.printStackTrace();
			}
            
    	}
    	
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoDireccion(cve_m_usuario_hidden, nombreHidden, paternoHidden, maternoHidden,
    			nivelHidden, tipoHidden, estadoHidden, fechaInicial, fechaFinal, unidadAdministrativaHidden, authentication);
    	
    	model.addAttribute("listaTipo", catalogoService.obtieneTipoDias(authentication));
    	model.addAttribute("listaNivel", catalogoService.obtieneNiveles(authentication));
    	model.addAttribute("listaEstado", estatusService.obtieneListaCompletaEstatus(authentication));
    	model.addAttribute("listaAsistencia", asistencia);
    	model.addAttribute("fechaInicial", fechaInicial);
    	model.addAttribute("fechaFinal", fechaFinal);
    	model.addAttribute("cve_m_usuario", cve_m_usuario_hidden);
    	model.addAttribute("nombre", nombreHidden);
    	model.addAttribute("paterno", paternoHidden);
    	model.addAttribute("materno", maternoHidden);
    	model.addAttribute("nivel", nivelHidden);
    	model.addAttribute("tipo", tipoHidden);
    	model.addAttribute("estado", estadoHidden);
    	model.addAttribute("unidadAdministrativa", unidadAdministrativaHidden);

    	if (resultadoProceso == 1) {
    		if (dictaminacion.equals("Autorizar")) {
    			model.addAttribute("MENSAJE", MENSAJE_AUTORIZA_DESCUENTO);
    		} else if (dictaminacion.equals("Rechazar")) {
    			model.addAttribute("MENSAJE", MENSAJE_RECHAZA_DESCUENTO);
    		}
    	} else if (resultadoProceso == 0) {
    		model.addAttribute("MENSAJE_EXCEPCION", MENSAJE_EXCEPCION);
    	}
    	
    	return "/asistencia/direccion";
    }
    
    @RequestMapping(value={"direccion/dictamina_Incidencia_Descuento"}, method = RequestMethod.POST, params="descargaArchivoJustificacionDescuento") //permite aprobar o rechazar justificaciones y descuentos
    public String descargaArchivoJustificacionDescuento(Model model, String cve_m_usuario_hidden, Integer idAsistenciaHidden, String nombreHidden, String paternoHidden,
    		String maternoHidden, String nivelHidden, Integer tipoHidden, Integer estadoHidden, String unidadAdministrativaHidden, Integer idTipoDia, 
    		Integer idJustificacion, String fechaInicial, String fechaFinal, String dictaminacion, Authentication authentication) {

    	asistenciaService.dictaminaIncidencia(idAsistenciaHidden, idTipoDia, idJustificacion, dictaminacion, authentication);
    	
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoDireccion(cve_m_usuario_hidden, nombreHidden, paternoHidden, maternoHidden,
    			nivelHidden, tipoHidden, estadoHidden, fechaInicial, fechaFinal, unidadAdministrativaHidden, authentication);
    	
    	model.addAttribute("listaTipo", catalogoService.obtieneTipoDias(authentication));
    	model.addAttribute("listaNivel", catalogoService.obtieneNiveles(authentication));
    	model.addAttribute("listaEstado", estatusService.obtieneListaCompletaEstatus(authentication));
    	model.addAttribute("listaAsistencia", asistencia);
    	model.addAttribute("fechaInicial", fechaInicial);
    	model.addAttribute("fechaFinal", fechaFinal);
    	model.addAttribute("cve_m_usuario", cve_m_usuario_hidden);
    	model.addAttribute("nombre", nombreHidden);
    	model.addAttribute("paterno", paternoHidden);
    	model.addAttribute("materno", maternoHidden);
    	model.addAttribute("nivel", nivelHidden);
    	model.addAttribute("tipo", tipoHidden);
    	model.addAttribute("estado", estadoHidden);
    	model.addAttribute("unidadAdministrativa", unidadAdministrativaHidden);
    	
    	return "/asistencia/direccion";
    }
    
    @RequestMapping(value={"direccion/dictaminaDescuento"}, method = RequestMethod.POST)
    public String dictaminaDescuento(Model model, String cve_m_usuario, Integer idAsistenciaHidden, String nombreHidden, String paternoHidden,
    		String maternoHidden, String nivelHidden, Integer tipoHidden, Integer estadoHidden, String unidadAdministrativaHidden, Integer idTipoDia, 
    		Integer idJustificacion, String fechaInicial, String fechaFinal, String dictaminacion, Authentication authentication ) {
    	
    	Integer resultadoProceso = 0;

    	resultadoProceso = asistenciaService.dictaminaIncidencia(idAsistenciaHidden, idTipoDia, idJustificacion, dictaminacion, authentication);
    	
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoDireccion(cve_m_usuario, nombreHidden, paternoHidden, maternoHidden,
    			nivelHidden, tipoHidden, estadoHidden, fechaInicial, fechaFinal, unidadAdministrativaHidden, authentication);
    	
    	model.addAttribute("listaTipo", catalogoService.obtieneTipoDias(authentication));
    	model.addAttribute("listaNivel", catalogoService.obtieneNiveles(authentication));
    	model.addAttribute("listaEstado", estatusService.obtieneListaCompletaEstatus(authentication));
    	model.addAttribute("listaAsistencia", asistencia);
    	model.addAttribute("fechaInicial", fechaInicial);
    	model.addAttribute("fechaFinal", fechaFinal);
    	model.addAttribute("cve_m_usuario", cve_m_usuario);
    	model.addAttribute("nombre", nombreHidden);
    	model.addAttribute("paterno", paternoHidden);
    	model.addAttribute("materno", maternoHidden);
    	model.addAttribute("nivel", nivelHidden);
    	model.addAttribute("tipo", tipoHidden);
    	model.addAttribute("estado", estadoHidden);
    	model.addAttribute("unidadAdministrativa", unidadAdministrativaHidden);

    	if (resultadoProceso == 1) {
    		if (dictaminacion.equals("Autorizar")) {
    			model.addAttribute("MENSAJE", MENSAJE_AUTORIZA_DESCUENTO);
    		} else if (dictaminacion.equals("Rechazar")) {
    			model.addAttribute("MENSAJE", MENSAJE_RECHAZA_DESCUENTO);
    		}
    	} else if (resultadoProceso == 0) {
    		model.addAttribute("MENSAJE_EXCEPCION", MENSAJE_EXCEPCION);
    	}
    	
    	return "/asistencia/direccion";
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
    	
    	InputStream inputStream;
		try {
			inputStream = new FileInputStream(new File(rutaArchivo));
			IOUtils.copy(inputStream, response.getOutputStream());
	        response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		} //load the file
        
        
    }
    
}