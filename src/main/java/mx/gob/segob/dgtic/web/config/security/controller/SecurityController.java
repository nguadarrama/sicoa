/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.config.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller que resulve las vistas asociadas al contexto de seguridad
 */
@Controller
public class SecurityController {
	
	 	/**
	 	 * Encargado de resolver el path <em>/login</em> y redirige al formulario para la captura de credenciales de autenticaci&oacute;n.
	 	 *
	 	 * @param model  El modelo a integrar en el request
	 	 * @return La vista a resolver
	 	 */
	 	@RequestMapping(value="/login", method={RequestMethod.GET,RequestMethod.POST})
		public String showLoginForm(Model model)
		{
			return "login";
		}    

}
