/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;



/**
 * Configura y establece la configuraci&oacute;n dentro del contexto Web de Spring y de SpringMVC con la integraci&oacute;n de Thymeleaf para la resoluci&oacute;n y 
 * renderizaci&oacute;n de las vistas (HTML)  
 * 
 * <p>La clase de inicializaci&oacute;n {@link mx.gob.segob.dgtic.web.WebMVCApplicationInit} detectara este componente 
 * como una clase de configuraci&oacute;n mediante la anotaci&oacute;n {@link org.springframework.context.annotation.Configuration @Configuration} 
 * y por  medio de la anotaci&oacute;n {@link org.springframework.context.annotation.ComponentScan @ComponentScan} se establece la 
 * ubicaci&oacute;n del paquete  que contendr&aacute; las clases a detectar como componentes o beans para la inyecci&oacute;n de dependencia.
 *
 * <p>Por medio de la anotaci&oacute;n {@link org.springframework.web.servlet.config.annotation.EnableWebMvc} se habilita el componente de SpringMVC
 *
 * @see org.springframework.web.servlet.config.annotation.EnableWebMvc
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.context.annotation.ComponentScan
 */
@EnableWebMvc
@Configuration
@ComponentScan({"mx.gob.segob.dgtic.web.mvc"})
public class SpringWebConfig extends WebMvcConfigurerAdapter {

    /** Constante que representa PROPERTIES_FILES_BASEMESSAGE, Ubicaci&oacute;n del archivo de Etiquetas. */
    private static final String [] PROPERTIES_FILES_BASEMESSAGE = {"classpath:/bundles/messages"};
	
	/** 
	 * Constante que representa DIRECTORIO_BASE_VIEWTEMPLATE, Directorio base donde se ubican los html de la vista. 
	 * <p>
	 * Se establece como el prefijo de la vista a resolver <code>{DIRECTORIO_BASE}/pathVista<code>
	 * 
	 * @see org.thymeleaf.templateresolver.ServletContextTemplateResolver#setPrefix(java.lang.String)
	 */
	private static final String DIRECTORIO_BASE_VIEWTEMPLATE = "/WEB-INF/html/";	
	
	/** 
	 * Constante que representa EXTENSION_BASE_VIEWTEMPLATE, Extension que se considera durante la resolucion de las vistas.
	 * <p>
	 * Se establece como el sufijo de la vista a resolver <code>{DIRECTORIO_BASE}/pathVista.{EXTENSION_BASE_VIEWTEMPLATE}</code> 	
	 * 
	 * @see org.thymeleaf.templateresolver.ServletContextTemplateResolver#setSuffix(java.lang.String)
	 */
	private static final String EXTENSION_BASE_VIEWTEMPLATE = ".html";
	
	/**
	 *  Constante que representa TEMPLATE_MODE_HTML, Establece el patron a interpretar al resolver y renderizar la vista.
	 * 
	 * @see org.thymeleaf.templateresolver.ServletContextTemplateResolver#setTemplateMode(java.lang.String)
	 */
	private static final String TEMPLATE_MODE_HTML = "HTML5";
	
	/**
	 *  Constante que representa ENCODING, Establece el encoding con el que se resolvera la vista
	 * 
	 * @see org.thymeleaf.templateresolver.ServletContextTemplateResolver#setCharacterEncoding(java.lang.String)
	 */
	private static final String ENCODING = "UTF-8";

    
	/**
	 * Establece los path's a resolver dentro del contexto WEB de la aplicaci&oacute;n para los recursos estaticos del lado cliente del proyecto
	 * <p>
	 * Los recursos estaticos imagenes, scripts, hojas de estilo, etc.
	 * 
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry)
	 */
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/recursos/**").addResourceLocations("/recursos/");
    }

    /**
     * Establece y configura el objeto encargado de resolver las vistas dentro de Thymeleaf.
     *
     * @return El objeto encargado de resolver las vistas.
     */
    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setCharacterEncoding(ENCODING);
        viewResolver.setOrder(1);
        viewResolver.setTemplateEngine(templateEngine());

        return viewResolver;
    }

    /**
     * Establece y configura el mecanismo que se encargara de renderizar las vistas a resolver por Thymeleaf    
     * <p>
     * Se configura los mecanismos y dialectos a considerar para la renderizaci&oacute;n de las vistas.
     *
     * @return El mecanismo de renderizado
     */
    @Bean
    public SpringTemplateEngine templateEngine(){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        
        Set<IDialect> additionalDialects = new HashSet<>(0);
        additionalDialects.add(new nz.net.ultraq.thymeleaf.LayoutDialect());
        additionalDialects.add(new SpringSecurityDialect());
        
        templateEngine.setAdditionalDialects(additionalDialects);
        return templateEngine;
    }

    /**
     * Establece y configura el objeto que resolver&aacute; las plantillas dentro de la estructura del proyecto.
     *
     * @return El objeto que resuelve las plantillas dentro de la estructura del proyecto.
     */
    @Bean
    public TemplateResolver templateResolver(){
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setCacheable(false);
        templateResolver.setPrefix(DIRECTORIO_BASE_VIEWTEMPLATE);
        templateResolver.setSuffix(EXTENSION_BASE_VIEWTEMPLATE);
        templateResolver.setTemplateMode(TEMPLATE_MODE_HTML);

        return templateResolver;
    }

    /**
     * Establece el archivo de etiquetas utilizado durante el renderizado de las vistas
     *
     * @return el archivo de etiquetas
     */
    @Bean
    public MessageSource messageSource()
    {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(PROPERTIES_FILES_BASEMESSAGE);
        messageSource.setDefaultEncoding(ENCODING);
        return messageSource;
    }
}

