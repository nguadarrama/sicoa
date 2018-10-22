package mx.gob.segob.dgtic.web.mvc.views.controller;


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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import mx.gob.segob.dgtic.web.mvc.dto.Asistencia;
import mx.gob.segob.dgtic.web.mvc.dto.GeneraReporteArchivo;
import mx.gob.segob.dgtic.web.mvc.dto.Justificacion;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;
import mx.gob.segob.dgtic.web.mvc.dto.reporte;
import mx.gob.segob.dgtic.web.mvc.service.ArchivoService;
import mx.gob.segob.dgtic.web.mvc.service.AsistenciaService;
import mx.gob.segob.dgtic.web.mvc.service.CatalogoService;
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
	
	//EMPLEADO
	@RequestMapping(value={"empleado"}, method = RequestMethod.GET)
    public String buscaListaAsistenciaEmpleado(Model model) {
    	
    	model.addAttribute("listaAsistencia", new ArrayList<Asistencia>());
    	model.addAttribute("listaAsistenciaJustificar", new ArrayList<Asistencia>());
    	
    	return "/asistencia/empleado";
    }
	
	@RequestMapping(value={"empleado/busca"}, method = RequestMethod.GET, params="busca")
    public String buscaListaAsistenciaEmpleadoRango(Model model, Authentication authentication, String fechaInicial, String fechaFinal) {

    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRango(authentication.getName(), fechaInicial, fechaFinal);
    	model.addAttribute("listaAsistencia", asistencia);
    	model.addAttribute("fechaInicial", fechaInicial);
    	model.addAttribute("fechaFinal", fechaFinal);
    	
    	return "/asistencia/empleado";
    }
	
	@RequestMapping(value="empleado/busca", method=RequestMethod.GET, params="exporta")
    public ModelAndView exportaExcelEmpleado(HttpServletRequest request, HttpServletResponse response, Authentication authentication, String fechaInicial, String fechaFinal) {
        
	    	List<Asistencia> listaAsistencia = asistenciaService.buscaAsistenciaEmpleadoRango(authentication.getName(), fechaInicial, fechaFinal);
	    	
	    	Map<String, Object> model = new HashMap<String, Object>();
	        
	    	//nombre de la hoja
	        model.put("nombreHoja", "asistencia");
	        
	        //nombres columnas
	        List<String> cabeceras = new ArrayList<String>();
	        cabeceras.add("Id Asistencia");
	        cabeceras.add("Entrada");
	        cabeceras.add("Id Status");
	        cabeceras.add("Id Tipo");
	        cabeceras.add("Salida");
	        cabeceras.add("Usuario");
	        model.put("cabeceras", cabeceras);
	        
	        //Información registros (List<Object[]>)
	        List<List<String>> asistencias = new ArrayList<List<String>>();
	        
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
    	
    	model.addAttribute("listaAsistencia", new ArrayList<Asistencia>());
    	model.addAttribute("listaAsistenciaJustificar", new ArrayList<Asistencia>());
    	
    	return "/asistencia/coordinador";
    }
    
    @RequestMapping(value={"coordinador/busca"}, method = RequestMethod.GET, params="busca")
    public String buscaListaAsistenciaCoordinadorRango(Model model, Authentication authentication, String cve_m_usuario, String nombre, String paterno, 
    		String materno, String nivel, String tipo, String estado, String fechaInicial, String fechaFinal, String unidadAdministrativa) {

	    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(cve_m_usuario, nombre, paterno, materno, nivel, 
	    			tipo, estado, fechaInicial, fechaFinal, unidadAdministrativa, authentication.getName());
	    	
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
    		String materno, String nivel, String tipo, String estado, String fechaInicial, String fechaFinal, String unidadAdministrativa) {
        
    	List<Asistencia> listaAsistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(cve_m_usuario, nombre, paterno, materno, nivel, 
    			tipo, estado, fechaInicial, fechaFinal, unidadAdministrativa, authentication.getName());
    	
    	Map<String, Object> model = new HashMap<String, Object>();
        
    	//nombre de la hoja
        model.put("nombreHoja", "asistencia");
        
        //nombres columnas
        List<String> cabeceras = new ArrayList<String>();
        cabeceras.add("Id Asistencia");
        cabeceras.add("Entrada");
        cabeceras.add("Id Status");
        cabeceras.add("Id Tipo");
        cabeceras.add("Salida");
        cabeceras.add("Usuario");
        model.put("cabeceras", cabeceras);
        
        //Información registros (List<Object[]>)
        List<List<String>> asistencias = new ArrayList<List<String>>();
        
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
    
    @RequestMapping(value={"creaIncidencia"}, method = RequestMethod.POST, params="justifica")
    public String creaIncidencia(Model model, String cve_m_usuario_hidden, Integer idAsistenciaHidden, Integer idTipoDia, Integer idJustificacion, 
    		String nombreHidden, String paternoHidden, String maternoHidden, String nivelHidden, String tipoHidden, String estadoHidden, String fechaInicial, 
    		String fechaFinal, String unidadAdministrativaHidden, Authentication authentication, MultipartFile archivo, String nombreAutorizador) {
    	
    	Integer idArchivo = null;
    	
    	try {
    		//guarda el archivo
    		if (archivo.getSize() > 0) {
    			idArchivo = archivoService.guardaArchivo(archivo, cve_m_usuario_hidden, new String("asistencia_justificacion"));
    		}
    		
    		//crea la incidencia y asocia el archivo
    		asistenciaService.creaIncidencia(idAsistenciaHidden, idTipoDia, idJustificacion, idArchivo, nombreAutorizador);

    	} catch(Exception e) {
    		new Exception("No se logró crear la incidencia " + e.getMessage());
    	}
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(cve_m_usuario_hidden, nombreHidden, paternoHidden, maternoHidden,
    			nivelHidden, tipoHidden, estadoHidden, fechaInicial, fechaFinal, unidadAdministrativaHidden, authentication.getName());
    	
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
    
    @RequestMapping(value={"creaIncidencia"}, method = RequestMethod.POST, params="descuenta")
    public String creaDescuento(Model model, String cve_m_usuario_hidden, Integer idAsistenciaHidden, Integer idTipoDia, Integer idJustificacion, 
    		String nombreHidden, String paternoHidden, String maternoHidden, String nivelHidden, String tipoHidden, String estadoHidden, String fechaInicial, 
    		String fechaFinal, String unidadAdministrativaHidden, Authentication authentication, MultipartFile archivo, String cve_m_usuario, String nombreAutorizador) {
    	
    	Integer idArchivo = null;
    	
    	try {
    		//guarda el archivo
    		if (archivo.getSize() > 0) {
    			idArchivo = archivoService.guardaArchivo(archivo, cve_m_usuario_hidden, new String("asistencia_descuento"));
    		}
    		
    		//crea la petición de descuento y asocia el archivo
    		asistenciaService.creaDescuento(idAsistenciaHidden, idTipoDia, idJustificacion, idArchivo, nombreAutorizador);

    	} catch(Exception e) {
    		new Exception("No se logró crear la incidencia " + e.getMessage());
    	}
    	
		

		List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(cve_m_usuario_hidden, nombreHidden, paternoHidden, maternoHidden,
    			nivelHidden, tipoHidden, estadoHidden, fechaInicial, fechaFinal, unidadAdministrativaHidden, authentication.getName());
    	
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
    
    @RequestMapping(value={"creaIncidencia"}, method = RequestMethod.POST, params="formatoJustificacion")
    public String descargaFormatoJustificacion(Model model, String nombre, String unidad, String nombreAutorizador, String cve_m_usuario_hidden, Integer idAsistenciaHidden, Integer idTipoDia, Integer idJustificacion, 
    		String nombreHidden, String paternoHidden, String maternoHidden, String nivelHidden, String tipoHidden, String estadoHidden, String fechaInicial, 
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
			reporte archivo = asistenciaService.formatoJustificacion(new FormatoIncidencia(nombre, unidad, fechaActual, codigoincidencia, ""));
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
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
    	
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(cve_m_usuario_hidden, nombreHidden, paternoHidden, maternoHidden,
    			nivelHidden, tipoHidden, estadoHidden, fechaInicial, fechaFinal, unidadAdministrativaHidden, authentication.getName());
    	
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
    		String nombreHidden, String paternoHidden, String maternoHidden, String nivelHidden, String tipoHidden, String estadoHidden, String fechaInicial, 
    		String fechaFinal, String unidadAdministrativaHidden, Authentication authentication, String cve_m_usuario, HttpServletResponse response) {
    	
    	//fecha actual para el reporte
    	Date fechaHoy = new Date();
    	SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
    	String fechaActual = f.format(fechaHoy);
    	
    	try {
			reporte archivo = asistenciaService.formatoDescuento(new FormatoIncidencia(nombre, "", fechaActual, "", cve_m_usuario));
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
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
    	
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(cve_m_usuario_hidden, nombreHidden, paternoHidden, maternoHidden,
    			nivelHidden, tipoHidden, estadoHidden, fechaInicial, fechaFinal, unidadAdministrativaHidden, authentication.getName());
    	
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
    public String buscaAsistenciasPorId(Model model, Integer[] checkboxes, String cve_m_usuario_hidden_lista_multiple, String fechaInicial_hidden_lista_multiple, 
    		String fechaFinal_hidden_lista_multiple) {
    	
    	List<Asistencia> listaAsistenciaJustificar = new ArrayList<>();
    	String nombreJefe = "";
    	String motivo = "";
    	
    	for (Integer idAsistencia : checkboxes) {
    		Asistencia asistenciaJustificar = asistenciaService.buscaAsistenciaPorId(idAsistencia);
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
    	
    	List<Justificacion> listaJustificaciones = catalogoService.obtieneJustificaciones();
    	List<Usuario> listaJefes = usuarioService.obtieneListaJefes();
    	
    	model.addAttribute("listaAsistencia", new ArrayList<Asistencia>());
    	model.addAttribute("listaAsistenciaJustificar", listaAsistenciaJustificar);
    	model.addAttribute("listaJustificaciones", listaJustificaciones);
    	model.addAttribute("listaAutorizadores", listaJefes);
    	model.addAttribute("nombreJefe", nombreJefe);
    	model.addAttribute("justificacion", motivo);
    	model.addAttribute("fechaInicial", fechaInicial_hidden_lista_multiple);
    	model.addAttribute("fechaFinal", fechaFinal_hidden_lista_multiple);
    	model.addAttribute("cve_m_usuario", cve_m_usuario_hidden_lista_multiple);
    	
    	return "/asistencia/coordinador";
    }
    
    @RequestMapping(value={"coordinador/justificaMultiple"}, method = RequestMethod.POST)
    public String creaIncidencias(Model model, Integer[] listaIdAsistencias, MultipartFile archivo, Integer selectJustificacion, String nombreAutorizador,
    		String cve_m_usuario_hidden_guarda_multiple, String fechaInicial_hidden_guarda_multiple, String fechaFinal_hidden_guarda_multiple, Authentication authentication) {
    	
    	Integer idArchivo = null;
    	Integer idJustificacion = selectJustificacion;
    	
    	for (Integer idAsistencia : listaIdAsistencias) {
    		Asistencia asistencia = asistenciaService.buscaAsistenciaPorId(idAsistencia);
    		Integer idTipoDia = asistencia.getIdTipoDia().getIdTipoDia();
    		String cve_m_usuario = asistencia.getUsuarioDto().getClaveUsuario();
    		
    		try {
        		//guarda el archivo
        		if (archivo.getSize() > 0) {
        			if (idArchivo == null) {
        				idArchivo = archivoService.guardaArchivo(archivo, cve_m_usuario, new String("asistencia_justificacion"));
        			}
        			//crea la incidencia y asocia el archivo
            		asistenciaService.creaIncidencia(idAsistencia, idTipoDia, idJustificacion, idArchivo, nombreAutorizador);
        		}
        	} catch(Exception e) {
        		new Exception("No se logró crear la incidencia " + e.getMessage());
        	}
    	}
    	
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoCoordinador(cve_m_usuario_hidden_guarda_multiple, "", "", "", "", "", "", 
    			fechaInicial_hidden_guarda_multiple, fechaFinal_hidden_guarda_multiple, "", authentication.getName());
    	
    	model.addAttribute("listaAsistencia", asistencia);
    	model.addAttribute("listaAsistenciaJustificar", new ArrayList<>());
    	model.addAttribute("fechaInicial", fechaInicial_hidden_guarda_multiple);
    	model.addAttribute("fechaFinal", fechaFinal_hidden_guarda_multiple);
    	model.addAttribute("cve_m_usuario", cve_m_usuario_hidden_guarda_multiple);
    	
    	
    	return "/asistencia/coordinador";
    }
    //TERMINA COORDINADOR
    
    //DIRECCIÓN
    @RequestMapping(value={"direccion"}, method = RequestMethod.GET)
    public String buscaListaAsistenciaDireccion(Model model) {
    	
    	model.addAttribute("listaAsistencia", new ArrayList<Asistencia>());
    	model.addAttribute("listaAsistenciaJustificar", new ArrayList<Asistencia>());
    	
    	return "/asistencia/direccion";
    }
    
    @RequestMapping(value={"direccion/busca"}, method = RequestMethod.GET, params="busca")
    public String buscaListaAsistenciaDireccionRango(Model model, Authentication authentication, String cve_m_usuario, String nombre, String paterno, 
    		String materno, String nivel, String tipo, String estado, String fechaInicial, String fechaFinal, String unidadAdministrativa) {

	    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoDireccion(cve_m_usuario, nombre, paterno, materno, nivel, 
	    			tipo, estado, fechaInicial, fechaFinal, unidadAdministrativa);
	    	
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
	    	model.addAttribute("unidadAdministrativa", unidadAdministrativa);
	    	
	    	return "/asistencia/direccion";
    }
    
    @RequestMapping(value="direccion/busca", method=RequestMethod.GET, params="exporta")
    public ModelAndView exportaExcelDireccion(HttpServletRequest request, HttpServletResponse response, Authentication authentication, 
    		String cve_m_usuario, String nombre, String paterno, String materno, String nivel, String tipo, String estado, String fechaInicial, String fechaFinal, String unidadAdministrativa) {
        
    	if (!cve_m_usuario.isEmpty() && !fechaInicial.isEmpty() && !fechaFinal.isEmpty()) {
	    	List<Asistencia> listaAsistencia = asistenciaService.buscaAsistenciaEmpleadoRangoDireccion(cve_m_usuario, nombre, paterno, materno, nivel, 
	    			tipo, estado, fechaInicial, fechaFinal, unidadAdministrativa);
	    	
	    	Map<String, Object> model = new HashMap<String, Object>();
	        
	    	//nombre de la hoja
	        model.put("nombreHoja", "asistencia");
	        
	        //nombres columnas
	        List<String> cabeceras = new ArrayList<String>();
	        cabeceras.add("Id Asistencia");
	        cabeceras.add("Entrada");
	        cabeceras.add("Id Status");
	        cabeceras.add("Id Tipo");
	        cabeceras.add("Salida");
	        cabeceras.add("Usuario");
	        model.put("cabeceras", cabeceras);
	        
	        //Información registros (List<Object[]>)
	        List<List<String>> asistencias = new ArrayList<List<String>>();
	        
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
    	
    	return new ModelAndView();
    	
    }
    
    @RequestMapping(value={"direccion/dictamina_Incidencia_Descuento"}, method = RequestMethod.POST) //permite aprobar o rechazar justificaciones y descuentos
    public String dictaminaIncidenciaDescuento(Model model, String cve_m_usuario_hidden, Integer idAsistenciaHidden, String nombreHidden, String paternoHidden,
    		String maternoHidden, String nivelHidden, String tipoHidden, String estadoHidden, String unidadAdministrativaHidden, Integer idTipoDia, 
    		Integer idJustificacion, String fechaInicial, String fechaFinal, String dictaminacion) {

    	asistenciaService.dictaminaIncidencia(idAsistenciaHidden, idTipoDia, idJustificacion, dictaminacion);
    	
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoDireccion(cve_m_usuario_hidden, nombreHidden, paternoHidden, maternoHidden,
    			nivelHidden, tipoHidden, estadoHidden, fechaInicial, fechaFinal, unidadAdministrativaHidden);
    	
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
    		String maternoHidden, String nivelHidden, String tipoHidden, String estadoHidden, String unidadAdministrativaHidden, Integer idTipoDia, 
    		Integer idJustificacion, String fechaInicial, String fechaFinal, String dictaminacion) {

    	asistenciaService.dictaminaIncidencia(idAsistenciaHidden, idTipoDia, idJustificacion, dictaminacion);
    	List<Asistencia> asistencia = asistenciaService.buscaAsistenciaEmpleadoRangoDireccion(cve_m_usuario, nombreHidden, paternoHidden, maternoHidden,
    			nivelHidden, tipoHidden, estadoHidden, fechaInicial, fechaFinal, unidadAdministrativaHidden);
    	
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
    	
    	return "/asistencia/direccion";
    }
    //TERMINA DIRECCION
    
    @GetMapping("buscaId")
    @ResponseBody
    public AsistenciaJustificacion buscaAsistenciaPorId(Integer id) {
    	
    	AsistenciaJustificacion asistenciaJustificacion = new AsistenciaJustificacion(); 
    	asistenciaJustificacion.setAsistencia(asistenciaService.buscaAsistenciaPorId(id));
    	asistenciaJustificacion.setListaJustificacion(catalogoService.obtieneJustificaciones());
    	asistenciaJustificacion.setListaAutorizador(usuarioService.obtieneListaJefes());
    	
    	return asistenciaJustificacion;
    }
    
    @RequestMapping(value={"archivo"}, method = RequestMethod.GET)
    public void download(HttpServletResponse response, Integer id) {
    	
    	AsistenciaJustificacion asistenciaJustificacion = new AsistenciaJustificacion(); 
    	asistenciaJustificacion.setAsistencia(asistenciaService.buscaAsistenciaPorId(id));
    	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //load the file
        
        
    }
    
}