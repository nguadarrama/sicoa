/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.config.security;

import mx.gob.segob.dgtic.web.config.security.entrypoint.AjaxAwareLoginUrlAuthenticationEntryPoint;
import mx.gob.segob.dgtic.web.config.security.handler.AccesoDenegadoHandler;
import mx.gob.segob.dgtic.web.config.security.handler.AutenticacionCorrectaHandler;
import mx.gob.segob.dgtic.web.config.security.handler.AutenticacionFallidaHandler;
import mx.gob.segob.dgtic.web.config.security.handler.LogoutCustomHandler;
import mx.gob.segob.dgtic.web.config.security.provider.AuthenticationRestProvider;
import mx.gob.segob.dgtic.web.config.security.service.AutenticacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;




/**
 * Clase de configuraci&oacute;n del contexto de seguridad dentro del proyecto WEB.
 * <p>
 * La anotaci&oacute;n {@link org.springframework.security.config.annotation.web.configuration.EnableWebSecurity} se encarga de activar la configuraci&oacute;n de seguridad para el aplicativo
 * La anotaci&oacute;n {@link org.springframework.context.annotation.ComponentScan} especifica la ruta donde se encuentran los componentes de configuraci&oacute;n de spring security
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@ComponentScan({"mx.gob.segob.dgtic.web.config.security"})
public class SecurityWebConfig extends WebSecurityConfigurerAdapter {
	
	/** Constante  CAMPO_NOMBRE_USUARIO : Nombre con que se identificara el parametro que contiene el nombre del usuario dentro del request. */
	private static final String CAMPO_NOMBRE_USUARIO = "accesoUsuario";
	
	
	/** Constante  CAMPO_CONTRASENIA : Nombre con que se identificara el parametro que contiene la palabra secreta del usuario dentro del request. */
	private static final String CAMPO_CONTRASENIA = "accesoClave";
	
	/** 
	 * Servicio que contiene las tareas de autenticaci&oacute;n 
	 */
	@Autowired
	private AutenticacionService autenticacionService;
	
	
	/**
	 * Realiza la configuracion global del contexto de seguridad.
	 * 
	 * @param auth el constructor de manejador de autenticaciones.
	 * @throws Exception Error al configurar parametros globales
	 */
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
		//Se registra el proveedor de autenticaci&oacute;n.
		auth.authenticationProvider(authenticationRestProvider());
    }
	
	/**
	 * Se configura el contexto de seguridad.
	 * <p>
	 * Se configuran los parametros requeridos dentro del contexto de seguridad
	 * 	- Parametros a inspeccionar en el request con las credenciales de usuarios.
	 *  - La pagina que tiene el formulario de parametros para autenticar a un usuario.
	 *  - El path de respuesta para una solicitud exitosa
	 *  - Se habilitan los requersos a considerar fuera del contexto de seguridad
	 *  - Se establecen los manejadores de login y de logout
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        		.antMatchers("/recursos/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage(getLoginView()).loginProcessingUrl(getLoginCheck()).failureHandler(autenticacionFallidaHandler())
                .passwordParameter(CAMPO_CONTRASENIA)
                .usernameParameter(CAMPO_NOMBRE_USUARIO)
                .defaultSuccessUrl(getHomeView(), true).permitAll().successHandler(autenticacionCorrectaHandler())
                .and()
                .logout().addLogoutHandler(logoutCustomHandler())
                .and()
                .csrf()
                .disable();
        
        		http.exceptionHandling().authenticationEntryPoint(ajaxAwareLoginUrlAuthenticationEntryPoint()).accessDeniedHandler(accesoDenegadoHandler());
              
        
    }
	
	
	
	/**
	 * Ajax aware login url authentication entry point.
	 *
	 * @return the ajax aware login url authentication entry point
	 */
	
	public AjaxAwareLoginUrlAuthenticationEntryPoint ajaxAwareLoginUrlAuthenticationEntryPoint() {
	    return new AjaxAwareLoginUrlAuthenticationEntryPoint(getLoginView());
	}	
	
	/**
	 * Obtiene el manejador para el acceso denegado
	 *
	 * @return the acceso denegado handler
	 */
	public AccesoDenegadoHandler accesoDenegadoHandler() {
	    return new AccesoDenegadoHandler();
	}
 
	/**
	 * Obtiene el manejador para una autenticaci&oacute;n correcta
	 *
	 * @return El manejador para una autenticacion correcta
	 */
	public AutenticacionCorrectaHandler autenticacionCorrectaHandler() {
	    return new AutenticacionCorrectaHandler();
	}
	
	/**
	 * Obtiene el manejador para una autenticaci&oacute;n fallida
	 *
	 * @return El manejador para una autenticacion fallida
	 */
	public AutenticacionFallidaHandler autenticacionFallidaHandler() {
	    return new AutenticacionFallidaHandler(getLoginView());
	}
	
	/**
	 * Obtiene el provedor del proceso de autenticaci&oacute;n
	 *
	 * @return El provedor del proceso de autenticaci&oacute;n
	 */
	public AuthenticationRestProvider authenticationRestProvider() {
	    return new AuthenticationRestProvider(autenticacionService);
	}
	
	/**
	 * El manejador de cierre de sesi&oacute;n
	 *
	 * @return El manejador de cierre de sesi&oacute;n
	 */
	public LogoutCustomHandler logoutCustomHandler() {
		return new LogoutCustomHandler(autenticacionService);
	}
	
	/**
	 * Obtiene el path asociado al formulario de autenticaci&oacute;n.
	 *
	 * @return La vista de login
	 */
	public String getLoginView(){
		return "/login";
	}
	
	/**
	 * Obtiene el path asociado a la vista de default
	 *
	 * @return La vista de default
	 */
	public String getHomeView(){
		return "/home";
	}
	
	/**
	 * Obtiene el path por el cual se realizara el proceso de autenticaci&oacute;n 
	 *
	 * @return El path de proceso
	 */
	public String getLoginCheck(){
		return "/logincheck";
	}
}
