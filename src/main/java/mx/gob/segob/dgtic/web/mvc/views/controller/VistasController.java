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
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;

import mx.gob.segob.dgtic.web.config.security.service.AutenticacionService;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;
import mx.gob.segob.dgtic.web.mvc.service.DashService;
import mx.gob.segob.dgtic.web.mvc.service.UsuarioService;
import mx.gob.segob.dgtic.web.mvc.util.rest.exception.ClienteException;
import mx.gob.segob.dgtic.web.mvc.views.controller.constants.ConstantsController;


/**
 * Controller donde se registran las vistas del aplicativo
 * 
 * Path : {contextoAplicacion}/
 */
@Controller
@RequestMapping( value = ConstantsController.DIAGONAL)
public class VistasController {	
	

    @Autowired 
    AutenticacionService autenticacionService;
    
    @Autowired 
    UsuarioService usuarioService;
    
    @Autowired 
    CatalogoController catalogoController;
    
    @Autowired
	private DashService dashService;
	
	 /**
     * Vista principal : Path : {contextoAplicacion}/home
     * @param session the session
     * @return La vista de home
     */
    @RequestMapping(value={ConstantsController.VACIO,ConstantsController.HOME}, method = RequestMethod.GET)
    public String index(HttpSession session, Model model, Authentication authentication){ 
    	String string= ConstantsController.VACIO+ session.getAttribute(ConstantsController.USUARIO);
    	String[] parts = string.split(ConstantsController.SPLIT);
    	String claveUsuario = parts[1];
    	Usuario usuario=null;
    	usuario=usuarioService.buscaUsuario(claveUsuario, authentication);
    	if(usuario.getPrimeraVez().equals(ConstantsController.YES) || usuario.getPrimeraVez().equals(ConstantsController.SI)){
    		return ConstantsController.CAMBIA_CONTRA;
    	}else{
    		model.addAttribute(ConstantsController.TOP,dashService.getDash(usuario.getIdUsuario(), authentication));
    		return ConstantsController.HOME; 
	   	}
    }   


    @PostMapping(ConstantsController.CAMBIA_CONTRA)
    public String cambiaContrasenia(HttpSession session,String accesoClave, String aC1, String accesoClave2, Model model, Authentication authentication) throws AuthenticationServiceException, ClienteException{
    	String string= ConstantsController.VACIO+ session.getAttribute(ConstantsController.USUARIO);
    	String[] parts = string.split(ConstantsController.SPLIT);
    	String claveUsuario = parts[1];
    	Boolean response=autenticacionService.cambiaContrasenia(claveUsuario, aC1, authentication);
    	return response == true ? ConstantsController.RE_LOGIN : ConstantsController.RE_CAMBIA_CONTRA;
    }
    @GetMapping(ConstantsController.CAMBIA_CONTRA)
    public String cambiaNuevaContrasenia() {
    	return ConstantsController.CAMBIA_CONTRAS;
    }
    @GetMapping(ConstantsController.CAMBIA_CONTRA1)
    public String cambiaNuevaContrasenia1() {
    	return ConstantsController.CAMBIA_CONTRA1S;
    }
    @GetMapping(ConstantsController.HOME)
    public String home(HttpSession session, Model model, Authentication authentication){ 
    	String string= ConstantsController.VACIO+ session.getAttribute(ConstantsController.USUARIO);
    	String[] parts = string.split(ConstantsController.SPLIT);
    	String claveUsuario = parts[1];
    	Usuario usuario=null;
    	usuario=usuarioService.buscaUsuario(claveUsuario, authentication);
    	model.addAttribute("top",dashService.getDash(usuario.getIdUsuario(), authentication));
    	return "home";
    }
    
    @GetMapping(ConstantsController.MSJ_CONFIRMACION)
    public String mensajeConfirmacion() {
    	return ConstantsController.MSJ_CONFIRMACIONS;
    }
    
    @PostMapping(ConstantsController.CAMBIA_CONTRA1)
    public String cambiaContrasenia1(HttpSession session, String aC1, String accesoClave2, Model model, Authentication authentication) throws AuthenticationServiceException, ClienteException{
    	String string= ConstantsController.VACIO+ session.getAttribute(ConstantsController.USUARIO);
    	String[] parts = string.split(ConstantsController.SPLIT);
    	String claveUsuario = parts[1];
    	Boolean response=autenticacionService.cambiaContrasenia(claveUsuario, aC1, authentication);
    	return response == true ? ConstantsController.RE_LOGIN : ConstantsController.RE_CAMBIA_CONTRA;
    }
    
}
