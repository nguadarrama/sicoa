/*
* ****************************************************
* * Aplicacion Base
* * Version 1.0.0
* * Secretaria de Gobernacion - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.mvc.views.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.segob.dgtic.web.config.security.service.AutenticacionService;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;


/**
 * Controller donde se registran las vistas del aplicativo
 * 
 * Path : {contextoAplicacion}/
 */
@Controller
@RequestMapping( value = "/")
public class VistasController {	
	

    @Autowired 
    AutenticacionService autenticacionService;
    
    @Autowired 
    CatalogoController catalogoController;
	
	 /**
     * Vista principal : Path : {contextoAplicacion}/home
     * @param session the session
     * @return La vista de home
     */
    @RequestMapping(value={"","home"}, method = RequestMethod.GET)
    public String index(HttpSession session){ 

    	String string=""+ session.getAttribute("usuario");
    	String[] parts = string.split(": ");
    	String claveUsuario = parts[1];
   	
    	Usuario usuario=null;
    	usuario=catalogoController.buscaUsuario(claveUsuario);
    	if(usuario.getPrimeraVez().equals("Y") || usuario.getPrimeraVez().equals("S")){
    		return "/cambiaContrasenia";
    	}else{
    		return "home"; 
	   	}
    }   


    @PostMapping("/cambiaContrasenia")
    public String cambiaContrasenia(HttpSession session, String accesoClave1, String accesoClave2){
    	String string=""+ session.getAttribute("usuario");
    	String[] parts = string.split(": ");
    	String claveUsuario = parts[1];
    	Boolean response=autenticacionService.cambiaContrasenia(claveUsuario, accesoClave1);
    	System.out.println("Si entra "+response);
    	
    	if(response==true){
        	return "home";
        	}else{
        	return "cambiaContrasenia";
        	}
    }
    @GetMapping("/cambiaContrasenia")
    public String cambiaNuevaContrasenia() {
  
    	return "cambiaContrasenia";
    }
    @GetMapping("/cambiaContrasenia1")
    public String cambiaNuevaContrasenia1() {
    	
    	
    	return "cambiaContrasenia1";
    }
    @GetMapping("/home")
    public String home() {
    	return "home";
    }
    
    @PostMapping("/cambiaContrasenia1")
    public String cambiaContrasenia1(HttpSession session, String accesoClave1, String accesoClave2){
    	String string=""+ session.getAttribute("usuario");
    	String[] parts = string.split(": ");
    	String claveUsuario = parts[1];
    	
    	Boolean response=autenticacionService.cambiaContrasenia(claveUsuario, accesoClave1);
    	System.out.println("cambia contraseña 1 "+response );
    	if(response==true){
    	return "home";
    	}else{
    	return "cambiaContrasenia1";
    	}
    }
    
    ///
    //Vista de demo
    //
    ///
    /**
     * Vista donde se ubica el demo de formularios. Path : {contextoAplicacion}/demo/formularios
     * @return La vista de demo de formularios
     */
    @RequestMapping(value={"demo/formularios"}, method = RequestMethod.GET)
    public String demoFormulario(){ return "/demo/demoFormularios"; }
    
    /**
     * Vista donde se ubica el demo de la tabla. Path : {contextoAplicacion}/demo/tabla
     * @return La vista de demo de la tabla
     */
    @RequestMapping(value={"demo/tabla"}, method = RequestMethod.GET)
    public String demoTabla(){ return "/demo/demoTabla"; }
    
    /**
     * Vista donde se ubica el demo de validaciones. Path : {contextoAplicacion}/demo/jqueryValidator
     * @return La vista de demo de validaciones
     */
    @RequestMapping(value={"demo/jqueryValidator"}, method = RequestMethod.GET)
    public String demoValidaciones(){ return "/demo/demoJqueryValidator"; }
    
    /**
     * Vista donde se ubica el demo de ajax. Path : {contextoAplicacion}/demo/ajax
     * @return La vista de demo de ajax
     */
    @RequestMapping(value={"demo/ajax"}, method = RequestMethod.GET)
    public String demoAjax(){ return "/demo/demoAjax"; }
}
