/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web;

import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import mx.gob.segob.dgtic.web.config.SpringWebConfig;
import mx.gob.segob.dgtic.web.config.filter.EncodingFilter;
import mx.gob.segob.dgtic.web.config.security.listener.SeguridadSessionListener;

/**
 * Clase encargada de inicializar el aplicativo WEB dentro del servidor de aplicaciones.
 * 
 * - Levanta contexto de SPRING para su fabrica de beans y la inyecci&oacute;n de dependencias
 * - Inicializa el componente de SpringMVC y de SpringSecurity.
 * 
 * 
 * Esta clase es la encargada de generar las configuraciones que se realizaban mediante el archivo web.xml
 * 
 * @see org.springframework.web.context.WebApplicationContext
 */
public class WebMVCApplicationInit extends AbstractAnnotationConfigDispatcherServletInitializer   {


	
		
		/**
		 * Provee las clases de configuraci&oacute;n que inicializan el contexto principal (<b>root context</b>) de Spring para el aplicativo WEB.
		 * 
		 * @return Clases de configuraci&oacute;n del contexto WEB 
 		 * @see org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer#getRootConfigClasses()
 		 */
 		@Override
	     protected Class<?>[] getRootConfigClasses() {
	        return new Class[] { SpringWebConfig.class };
	     }
	  
	    /**
	     * Provee las clases de configuraci&oacute;n adicionales para el dispatcher del aplicativo WEB
	     * 
	     * @return Clases de configuraci&oacute;n para el dispatcher WEB 
    	 * @see org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer#getServletConfigClasses()
    	 */
    	@Override
	    protected Class<?>[] getServletConfigClasses() {
	        return new Class<?>[0];
	    }
	  
	    /**
	     * Especifica los contextos WEB a los cuales responder&aacute; el DispatcherServlet
	     * 
	     * @return Arreglo con los contextos WEB a los cuales responder&aacute; el DispatcherServlet
    	 * @see org.springframework.web.servlet.support.AbstractDispatcherServletInitializer#getServletMappings()
    	 */
    	@Override
	    protected String[] getServletMappings() {
	        return new String[] { "/" };
	    }
	    
	    
	    /**
	     * Configura los componentes (Servlet's, Listener's, Filtros, par&aacute;metros de contexto) necesarios para la correcta inicializaci&oacute;n del aplicativo WEB
	     * 
    	 * @see org.springframework.web.servlet.support.AbstractDispatcherServletInitializer#onStartup(javax.servlet.ServletContext)
    	 */
    	@Override
	    public void onStartup(ServletContext servletContext) throws ServletException {
	        WebApplicationContext context = getContext();
	        servletContext.addListener(new ContextLoaderListener(context));

	        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("SpringMVCDispatcherServlet", new DispatcherServlet(context));
	        dispatcher.setLoadOnStartup(1);
	        dispatcher.addMapping("/*");
	        dispatcher.setInitParameter("throwExceptionIfNoHandlerFound", "true");
	        
	        Dynamic filterRegis = servletContext.addFilter("EncodingFilter", EncodingFilter.class);
	        filterRegis.addMappingForUrlPatterns(null, false, "/*");
	        filterRegis.setInitParameter("encoding", "UTF-8");
	        filterRegis.setInitParameter("allow", "GET,PUT,POST,DELETE,OPTIONS");
	        filterRegis.setInitParameter("allow", "*");
	        filterRegis.setInitParameter("EncodingFilter", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
	        filterRegis.setInitParameter("allowCredentials", "true");
	        
	        //Se configura la seguridad de cookie
	        servletContext.getSessionCookieConfig().setHttpOnly(true);
	        
	        
	        habilitaContextoSeguridadWeb(servletContext);
	    }
	    

	    /**
    	 * Fuuncion que devuelve el contexto de aplicaci&oacute;n
    	 *
    	 * @return el contexto WEB del aplicativo
    	 */
    	private AnnotationConfigWebApplicationContext getContext() {
	        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
	        context.setConfigLocation("mx.gob.segob.dgtic.web.config");
	        return context;
	    }
	    
	    /**
		 * Se habilita el contexto de seguridad (SpringSecurity) para el aplicativo WEB.
		 * 
		 * @param servletContext el contexto de la aplicaci&oacute;n
		 */
    	private void habilitaContextoSeguridadWeb(ServletContext servletContext){
    	    servletContext.addListener(new SeguridadSessionListener());
	    	
	    	Dynamic filterSeguridad = servletContext.addFilter(
	    									AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME, 
	    									DelegatingFilterProxy.class);
	    	filterSeguridad.addMappingForUrlPatterns(null, false, "/*");
	    }
	
}
