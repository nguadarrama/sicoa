package mx.gob.segob.dgtic.web.mvc.views.controller;

import java.io.File;
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
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import mx.gob.segob.dgtic.web.mvc.dto.Archivo;
import mx.gob.segob.dgtic.web.mvc.dto.Periodo;
import mx.gob.segob.dgtic.web.mvc.dto.Vaca;
import mx.gob.segob.dgtic.web.mvc.dto.Vacaciones;
import mx.gob.segob.dgtic.web.mvc.service.ArchivoService;
import mx.gob.segob.dgtic.web.mvc.service.PeriodoService;
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
	
	
    @RequestMapping(value={"inicio"}, method = RequestMethod.GET)
    public String obtieneAsistencias(Model model, HttpSession session) {
	    
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
    public String registraVacacioness(@RequestParam MultipartFile archivo, @RequestParam String fechaInicio, @RequestParam String fechaFin, @RequestParam Integer diasPorPedir, @RequestParam Integer idPeriodo, HttpSession session ) {
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
    	System.out.println("Archivo "+archivo);
    	
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
     	archivoService.guardaArchivo(archivo, claveUsuario, new String("vacaciones"));
     	vacacionesService.agregaVacaciones(new Vacaciones(null, null, null, null, fechaInicial, fechaFinal, diasPorPedir), claveUsuario);
    	System.out.println("fechaInicio "+fechaInicial+" fechaFin "+fechaFinal+" diasPorPedir "+diasPorPedir+" idPeriodo "+idPeriodo+" claveUsuario "+claveUsuario);
    	System.out.println("Archivo "+archivo);
    	return "redirect:/vacaciones/inicio";
    }
    
   /* @RequestMapping("/vacacion/modifica")
    public String uploadResources( HttpServletRequest servletRequest,
                                 @ModelAttribute Vaca vacaciones,
                                 Model model)
    {
    	System.out.println("fech Ini "+vacaciones.getFechaInicio()+ " fech Fin "+vacaciones.getFechaFin());
        //Get the uploaded files and store them
        List<MultipartFile> files = vacaciones.getArchivo();
        List<String> fileNames = new ArrayList<String>();
        if (null != files && files.size() > 0)
        {
        	System.out.println("archivo "+vacaciones.getArchivo());
            for (MultipartFile multipartFile : files) {
 
                String fileName = multipartFile.getOriginalFilename();
                fileNames.add(fileName);
 
                File imageFile = new File(servletRequest.getServletContext().getRealPath("/image"), fileName);
                try
                {
                    multipartFile.transferTo(imageFile);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
 
        // Here, you can save the product details in database
         
        model.addAttribute("product", vacaciones);
        System.out.println("arvchivo "+vacaciones.getArchivo());
        return "redirect:/vacaciones/inicio";
    }*/
    
    @GetMapping("vacacion/acepta")
    public String aceptaVacaciones(Integer id, String fechaInicio, String fechaFinal) {
    	System.out.println("idVacacion "+id+" fechaInicio "+fechaInicio+" fechaFinal "+fechaFinal);
    	//catalogoService.eliminaHorario(id);
    	
    	return "redirect:/vacaciones/inicio";
    }
    
    @GetMapping("vacacion/rechaza")
    public String rechazaVacaciones(Integer id) {
    	System.out.println("idVacacion "+id);
    	//catalogoService.eliminaHorario(id);
    	return "redirect:/vacaciones/inicio";
    }
}
