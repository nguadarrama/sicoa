package mx.gob.segob.dgtic.web.mvc.views.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import mx.gob.segob.dgtic.web.mvc.dto.Archivo;
import mx.gob.segob.dgtic.web.mvc.dto.Estatus;
import mx.gob.segob.dgtic.web.mvc.dto.Periodo;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;
import mx.gob.segob.dgtic.web.mvc.dto.Vaca;
import mx.gob.segob.dgtic.web.mvc.dto.VacacionPeriodo;
import mx.gob.segob.dgtic.web.mvc.dto.Vacaciones;
import mx.gob.segob.dgtic.web.mvc.service.ArchivoService;
import mx.gob.segob.dgtic.web.mvc.service.CatalogoService;
import mx.gob.segob.dgtic.web.mvc.service.PeriodoService;
import mx.gob.segob.dgtic.web.mvc.service.UsuarioService;
import mx.gob.segob.dgtic.web.mvc.service.VacacionesService;

@Controller
@RequestMapping( value = "vacaciones")
public class VacacionesController {
	
	@Autowired
	private VacacionesService vacacionesService;
	
	@Autowired
	private PeriodoService periodoService;
	
	@Autowired 
	private ArchivoService archivoService;
	
//	@Autowired 
//	private UsuarioService usuarioService;
	
	
    @RequestMapping(value={"inicio"}, method = RequestMethod.GET)
    public String obtieneAsistencias(Model model, HttpSession session) {
    	System.out.print("Peticion de vacaciones");
	    
	    model.addAttribute("listaVacaciones", vacacionesService.obtieneVacaciones());
	    String string=""+ session.getAttribute("usuario");
    	String[] parts = string.split(": ");
    	String claveUsuario = parts[1];
    	Periodo periodo= new Periodo();
    	periodo=periodoService.buscaPeriodoPorClaveUsuario(claveUsuario);
	    model.addAttribute("periodo",periodo);
	    System.out.println("idPeriodo "+periodo.getIdPeriodo());
	    model.addAttribute("vacacion",vacacionesService.buscaVacacionPeriodoPorClaveUsuarioYPeriodo(claveUsuario,periodo.getIdPeriodo()));
	    
    	//Periodo periodo=periodoService.buscaPeriodoPorClaveUsuario(claveUsuario);
    	//System.out.println("datos del periodo "+periodo.getFechaInicio());
    	return "/vacaciones/inicio";
    }
    
    
    @RequestMapping(value={"busca"}, method = RequestMethod.GET)
    public String buscaAsistencia(Model model) {
	    
	    model.addAttribute("listaVacaciones", vacacionesService.obtieneVacaciones());
	    
    	return "/vacaciones/busca";
    }
    
    @PostMapping("/vacacion/modifica")
    public String registraVacacioness(@RequestParam MultipartFile archivo, @RequestParam String fechaInicio, @RequestParam String fechaFin, @RequestParam Integer diasPorPedir, @RequestParam Integer idPeriodo, @RequestParam String idVacacion, HttpSession session ) {
    	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    	Date fechaInicial = new Date();
    	Date fechaFinal = new Date();
    	InputStream nuevoArchivo = null;
    	try {
			nuevoArchivo=archivo.getInputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	System.out.println("fechaInicio "+fechaInicio+" fechaFin "+fechaFin+" diasPorPedir "+diasPorPedir+" idPeriodo "+idPeriodo);
    	System.out.println("Archivo "+archivo+" idVacacion "+idVacacion);
    	
    	try {
    		fechaInicial = df.parse(fechaInicio);
    		fechaFinal=df.parse(fechaFin);
			System.out.println("fechaInicio "+fechaInicial);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 String string=""+ session.getAttribute("usuario");
     	String[] parts = string.split(": ");
     	String claveUsuario = parts[1];
     	VacacionPeriodo vacacion=new  VacacionPeriodo();
     	Archivo archivoDto = new Archivo();
     	vacacion.setIdVacacion(Integer.parseInt(idVacacion));
//     	usuarioService.buscaUsuario(claveUsuario);
//     	Integer idArchivo=archivoService.guardaArchivo(archivo, claveUsuario, new String("vacaciones"));
     	//archivoDto.setIdArchivo(idArchivo);
     	Estatus estatus=new Estatus();
     	estatus.setIdEstatus(1);
     	vacacionesService.agregaVacaciones(new Vacaciones(null,vacacion,archivoDto, null, estatus, fechaInicial, fechaFinal, diasPorPedir), claveUsuario);
    	System.out.println("fechaInicio "+fechaInicial+" fechaFin "+fechaFinal+" diasPorPedir "+diasPorPedir+" idPeriodo "+idPeriodo+" claveUsuario "+claveUsuario);
    	System.out.println("Archivo "+archivo);
    	return "redirect:/vacaciones/inicio";
    }
    
    @GetMapping("vacacion/acepta")
    public String aceptaVacaciones(Integer id, String fechaInicio, String fechaFin,Integer idUsuario) {
    	System.out.println("idVacacion "+id+" fechaInicio "+fechaInicio+" fechaFinal "+fechaFin+" idUsuario "+idUsuario);
    	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    	Date fechaInicial = new Date();
    	Date fechaFinal = new Date();
    	Usuario usuario=new Usuario();
    	usuario.setIdUsuario(idUsuario);
    	Estatus estatus= new Estatus();
        estatus.setIdEstatus(2);
        try {
    		fechaInicial = df.parse(fechaInicio);
    		fechaFinal=df.parse(fechaFin);
			System.out.println("fechaInicio "+fechaInicial);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	vacacionesService.aceptaORechazaVacaciones(new Vacaciones(usuario,null,null,null,estatus,fechaInicial,fechaFinal,null), id);
    	//catalogoService.eliminaHorario(id);
    	
    	return "redirect:/vacaciones/inicio";
    }
    
    @GetMapping("vacacion/rechaza")
    public String rechazaVacaciones(Integer id,String idUsuario,Integer idVacacion,Integer dias) {
    	System.out.println("idVacacion "+id+" idUsuario "+idUsuario+" idVacacion "+idVacacion+" dias"+dias);
    	VacacionPeriodo vacacion=new  VacacionPeriodo();
    	vacacion.setIdVacacion(idVacacion);
    	Usuario usuario=new Usuario();
    	usuario.setIdUsuario(Integer.parseInt(idUsuario));
    	Estatus estatus= new Estatus();
        estatus.setIdEstatus(3);
    	vacacionesService.aceptaORechazaVacaciones(new Vacaciones(usuario,vacacion,null,null,estatus,null,null,dias), id);
    	return "redirect:/vacaciones/inicio";
    }
    
    @RequestMapping(value = "/archivo", method = RequestMethod.GET)
    public void getFile(
        @PathVariable("C:/Users/Anzen Digital/Pictures/tigre.jpg") String fileName, 
        HttpServletResponse response) {
        try {
          // get your file as InputStream
        	FileInputStream archi= new FileInputStream("C:/Users/Anzen Digital/Desktop");
          InputStream is = archi;
          // copy it to response's OutputStream
          org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
          response.flushBuffer();
        } catch (IOException ex) {
          System.out.println("Error writing file to output stream. Filename was '{}'"+ fileName+" "+ex);
          throw new RuntimeException("IOError writing file to output stream");
        }

    }
}
