/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.config.security.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import mx.gob.segob.dgtic.web.config.security.constants.SesionConstants;
import mx.gob.segob.dgtic.web.config.security.service.AutenticacionService;
import mx.gob.segob.dgtic.web.mvc.dto.UsuarioSesion;
import mx.gob.segob.dgtic.web.mvc.util.properties.AplicacionWebPropertiesUtil;

/**
 * Proveedor encargado de verificar la situaci&oacute;n de un usuario mediante sus credenciales.
 * 
 * 
 */
public class AuthenticationRestProvider implements AuthenticationProvider {
	
	 /**
     *  Instancia para realizar log.
     */
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationRestProvider.class);

	/** 
	 * Servicio que contiene las tareas de autenticaci&oacute;n 
	 */
	private AutenticacionService autenticacionService;

	/**
	 * Constructor del provedor de autenticaci&oacute;n
	 *
	 * @param autenticacionService el service de autenticaci&oacute;n
	 */
	public AuthenticationRestProvider(AutenticacionService autenticacionService){
		this.autenticacionService = autenticacionService;
	}	
		
	/**
	 * Proceso de autenticaci&oacute;n.
	 * <p>
	 * Este proceso verifica la situaci&oacute;n del usuario para evaluar si tiene permitido el acceso y obtiene los datos relevantes (permisos) para 
	 * futura inspecci&oacute;n mediante las anotaciones de spring security. 
	 * 
	 * @return El objeto necesario para generar el contexto de seguridad.
	 * 
	 * @see org.springframework.security.authentication.AuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(Authentication autentication) throws AuthenticationException {
		logger.trace("Iniciando proceso de autenticacion");
		
		
		//Se obtiene el identificador del solicitante del sistema.
		String solicitante = obtenerSolicitanteSistema();	
		
		//Se obtienen las credenciales del usuario que intenta autenticarse
		String nombreUsuario = autentication.getName();
        String contrasenio = autentication.getCredentials().toString();


		//Se solicita un token de autorizacion para realizar el proceso de login
        String tokenAutorizacionLogin =  autenticacionService.obtenerTokenAutorizacionLogin(solicitante);
        
		//Se solicita un token de acceso con las credenciales de usuario y el token de auto
        String tokenAcceso = autenticacionService.obtenerTokenAccesoLogin(tokenAutorizacionLogin, nombreUsuario, contrasenio);

		//De ser correcto se solicita el estatus del usuario.
        UsuarioSesion usuario = autenticacionService.obtenerInformacionUsuario(tokenAcceso);
		
        //Se registran los permisos asignados al usuario al contexto de springSecurity 
		List<GrantedAuthority> grantedList = new ArrayList<>(0);
		Set<String> permisos = usuario.getPermisos();
		for(String permiso : permisos){
			grantedList.add(new SimpleGrantedAuthority(permiso));
		}
		
		//Se genera el objeto de autorizacion requerida para el contexto de seguridad
		Map<String, Object> details = new HashMap<>();
		details.put(SesionConstants.USUARIO_SESION, usuario);
		details.put(SesionConstants.TOKEN_SESION, tokenAcceso);
		UsernamePasswordAuthenticationToken userAuthorization = new UsernamePasswordAuthenticationToken(usuario.getClaveUsuario(), null, grantedList);
		userAuthorization.setDetails(details);		
				
		return userAuthorization;
	}
	

	/**
	 * Se evalua si el objeto de autenticacion corresponde al proveedor
	 * 
	 * @see org.springframework.security.authentication.AuthenticationProvider#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);		
	}
	
	
	/**
	 * Obtener solicitante sistema.
	 * <p>
	 * Se obtiene el identificador del solicitante configurado en el archivo de propiedades del sistema.
	 * <code>aplicacion_web.properties:token.solicitante</code>
	 *
	 * @return the string
	 */
	public String obtenerSolicitanteSistema(){
		return AplicacionWebPropertiesUtil.getPropiedades().obtenerPropiedad("token.solicitante");
	}
	

	
}
