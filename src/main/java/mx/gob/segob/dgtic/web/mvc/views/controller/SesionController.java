/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.mvc.views.controller;



import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import mx.gob.segob.dgtic.web.config.security.constants.SesionConstants;
import mx.gob.segob.dgtic.web.mvc.dto.UsuarioSesion;

/**
 * Controller para verificar atributos de sesi&oacute;n de usuario
 * 
 * Path de controller {contextoAplicacion}/sesion
 */
@RestController
@RequestMapping("sesion")
public class SesionController {
	
	/**
	 * Servicio que devuelve un arreglo de permisos del usuario en sesi&oacute;n, Metodo GET, Path de controller {contextoAplicacion}/sesion/features
	 *
	 * @param sesion La sesi&oacute;n
	 * @return arreglo de permisos en formato JSON.
	 */
	@GetMapping(value={"features"}, produces="application/json" )
	public UsuarioSesion permisos(HttpSession sesion){
		UsuarioSesion usuarioAutenticado;
		usuarioAutenticado = (UsuarioSesion)sesion.getAttribute(SesionConstants.USUARIO_SESION);
		return usuarioAutenticado;
	}
	
	/**
	 * Servicio que sirve para verificar que la sesi&oacute;n aun es vigente.  Metodo GET, Path de controller {contextoAplicacion}/sesion/ping
	 */
	@RequestMapping(value = "ping", method = RequestMethod.HEAD)
    public void getEmployeeInJSON() {
		// proceso exclusivamente para verficar que es accesible y que aun no a vencido la sesion
	}
}
