/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.mvc.util.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mx.gob.segob.dgtic.web.mvc.util.properties.AplicacionWebPropertiesUtil;
import mx.gob.segob.dgtic.web.mvc.util.properties.JndiResourceUtil;
import mx.gob.segob.dgtic.web.mvc.util.rest.exception.ClienteException;

/**
 * Clase de apoyo utilizada para acceder servicios REST, la clase se implementa mendiante el patron SINGLETON 
 */
public class ClienteRestUtil {
	
	/** 
	 * La constante JNDI_HOST_SERVICIOS_REST. 
	 * <p>
	 * Se define el host donde se consumen los servicios rest para el proceso de autenticaci&oacute;n mediante una variable de entorno. 
	 * La definici&oacute;n del nombre de la variable de entorno se encuentra en el archivo de propiedades del sistema
	 * 
	 */
	private static final String JNDI_HOST_SERVICIOS_REST = "jndi.host.rest.services.global";
	
	/** 
	 * La constante JNDI_UBICACION_CERTIFICADO_CLIENTE
	 * <p>
	 * Si el servidor de servicios requiere un certificado cliente se especifica la ubicaci&oacute;n mediante una 
	 * variable de entorno . La definici&oacute;n se encuentra en el archivo de propiedades del sistema  
	 */
	private static final String JNDI_UBICACION_CERTIFICADO_CLIENTE = "jndi.ubicacion.certificadocliente.global";
	
	/** 
	 * La constante JNDI_CONTRASENIA_CERTIFICADO_CLIENTE.
	 * <p>
	 * Si el servidor de servicios requiere un certificado cliente se especifica la constrase&ntilde;a mediante una variable de entorno 
	 * La definici&oacute;n se encuentra en el archivo de propiedades del sistema
	 */
	private static final String JNDI_CONTRASENIA_CERTIFICADO_CLIENTE = "jndi.contrasenia.certificadocliente.global";
	
	
	
	
	/** El cliente. */
	private static ClienteRestUtil cliente;	
	
	/**
	 * Metodo statico para el acceso a la instancia del cliente
	 *
	 * @return El cliente
	 */
	public static synchronized  ClienteRestUtil getCliente(){
		if(cliente == null){
			cliente = new ClienteRestUtil(); 
		}
		return cliente;
	}
	
	/**
	 * Serializa un mapa de objetos en una HttpEntity de tipo JSON.
	 * <p>
	 * Serializa el contenido a un formato JSON y se adjunta a de entidad JSON, identificando la petici&oacute;n de tipo "application/json"
	 *
	 * @param content El contenido a serializar en formato JSON
	 * @return La entidad a adjuntar en la petici&oacute;n de tipo JSON
	 * @throws ClienteException Error al convertir el contenido
	 */
	public HttpEntity convertContentToJSONEntity(Map<String, Object> content) throws ClienteException{		
		Gson gson = new GsonBuilder()
				.enableComplexMapKeySerialization()
				.serializeNulls()
				.create();
		
		StringEntity entityContent = null;
		try {
			String jsonContent = gson.toJson(content);
			jsonContent = jsonContent.replaceAll("Á", "AAA")
			.replaceAll("É", "EEE")
			.replaceAll("Í", "III")
			.replaceAll("Ó", "OOO")
			.replaceAll("Ú", "UUU")
			.replaceAll("á", "aaa")
			.replaceAll("é", "eee")
			.replaceAll("í", "iii")
			.replaceAll("ó", "ooo")
			.replaceAll("ú", "uuu")
			.replaceAll("ñ", "nnn")
			.replaceAll("Ñ", "NNN");
			entityContent = new StringEntity(jsonContent);
			entityContent.setContentEncoding("UTF-8");
			entityContent.setContentType(ContentType.APPLICATION_JSON.getMimeType());
		} catch (UnsupportedEncodingException e) {
			throw new ClienteException("No se puede convertir el contenido a JSON : "+e.getMessage(), e);
		}
		return entityContent;
	}
	
	
	/**
	 * Convierte un mapa de objetos en una entidad de tipo formulario, identificado como "application/x-www-form-urlencoded"
	 *
	 * @param content El contenido a convertir en un formulario
	 * @return La entidad a adjuntar en la petici&oacute;n de tipo FORM
	 * @throws ClienteException Error al convertir el contenido
	 */
	public HttpEntity convertContentToFormEntity(Map<String, Object> content) throws ClienteException{		
		
		List<NameValuePair> formParametros = new ArrayList<>(0);
		if(content != null && !content.isEmpty()){			
			for(Entry<String, Object> elemento : content.entrySet()){
				String nameForm = elemento.getKey();
				Object valueForm = elemento.getValue() == null?"":elemento.getValue();				
				
				formParametros.add(new BasicNameValuePair(nameForm, valueForm.toString()));
			}			
		}		
		return new UrlEncodedFormEntity(formParametros, Consts.UTF_8);
	}
	
	/**
	 * Genera un petici&oacute;n de POST a un recurso REST
	 * 
	 * @param pathRelativoEndpoint El path relativo del endpoint que define el recurso REST a consumir
	 * @param content Conjunto de parametros a enviar hacia la petici&oacute;n.
	 * @param contentType El tipo de contenido en que se debe serializar el cuerpo JSON o FORM
	 * @param headers Los encabezados a incluir en la petici&oacute;n
	 * @return La respuesta del recurso REST
	 * @throws ClienteException Error al consumir el recurso REST
	 */
	public HttpResponse post(String pathRelativoEndpoint, 
			Map<String, Object> content,
			ContentType contentType,
			Header...headers) throws ClienteException {		

		HttpEntity entity = null;
		if(contentType != null && contentType.equals(ContentType.APPLICATION_JSON)) {
			entity = convertContentToJSONEntity(content);
		} else if(contentType != null && contentType.equals(ContentType.APPLICATION_FORM_URLENCODED)) {
			entity = convertContentToFormEntity(content);
		} else {
			throw new ClienteException("Content type soportados application/json, application/x-www-form-urlencoded");
		}	

		return post(pathRelativoEndpoint, entity, headers);
	}


	/**
	 * Genera un petici&oacute;n de POST a un recurso REST
	 * 	 
	 * @param pathRelativoEndpoint El path relativo del endpoint que define el recurso REST a consumir
	 * @param contentEntity La entidad que serializara el cuerpo de la petici&oacute;n 
	 * @param headers Los encabezados a incluir en la petici&oacute;n
	 * @return La respuesta del recurso REST
	 * @throws ClienteException  Error al consumir el recurso REST
	 */
	public HttpResponse post(String pathRelativoEndpoint, 
			HttpEntity contentEntity, 
			Header...headers) throws ClienteException {
		String endpoint= obtenerServicio(pathRelativoEndpoint);
		HttpEntityEnclosingRequest request=  new HttpPost(endpoint);		
		request.setEntity(contentEntity);				
		return sendRequest((HttpRequestBase)request, headers);
	}
	
	
	/**
	 * Genera un petici&oacute;n de PUT a un recurso REST
	 * 
	 * @param pathRelativoEndpoint El path relativo del endpoint que define el recurso REST a consumir
	 * @param content Conjunto de parametros a enviar hacia la petici&oacute;n.
	 * @param contentType El tipo de contenido en que se debe serializar el cuerpo JSON o FORM
	 * @param headers Los encabezados a incluir en la petici&oacute;n
	 * @return La respuesta del recurso REST
	 * @throws ClienteException Error al consumir el recurso REST
	 */
	public HttpResponse put(String pathRelativoEndpoint, 
			Map<String, Object> content,
			ContentType contentType,
			Header...headers) throws ClienteException {		

		HttpEntity entity = null;
		if(contentType != null && contentType.equals(ContentType.APPLICATION_JSON)) {
			entity = convertContentToJSONEntity(content);
		} else if(contentType != null && contentType.equals(ContentType.APPLICATION_FORM_URLENCODED)) {
			entity = convertContentToFormEntity(content);
		} else {
			throw new ClienteException("Content type soportados application/json, application/x-www-form-urlencoded");
		}	

		return put(pathRelativoEndpoint, entity, headers);
	}


	/**
	 * Genera un petici&oacute;n de PUT a un recurso REST
	 *
	 * @param pathRelativoEndpoint El path relativo del endpoint que define el recurso REST a consumir
	 * @param contentEntity La entidad que serializara el cuerpo de la petici&oacute;n 
	 * @param headers Los encabezados a incluir en la petici&oacute;n
	 * @return La respuesta del recurso REST
	 * @throws ClienteException  Error al consumir el recurso REST
	 *
	 */
	public HttpResponse put(String pathRelativoEndpoint, 
			HttpEntity contentEntity, 
			Header...headers) throws ClienteException {
		String endpoint= obtenerServicio(pathRelativoEndpoint);
		HttpEntityEnclosingRequest request=  new HttpPut(endpoint);		
		request.setEntity(contentEntity);				
		return sendRequest((HttpRequestBase)request, headers);
	}
	
	
	/**
	 * Genera un petici&oacute;n de GET a un recurso REST
	 *
	 * @param pathRelativoEndpoint El path relativo del endpoint que define el recurso REST a consumir
	 * @param headers Los encabezados a incluir en la petici&oacute;n
	 * @return La respuesta del recurso REST
	 *
	 * @throws ClienteException  Error al consumir el recurso REST
	 */
	public HttpResponse get(String pathRelativoEndpoint, Header...headers) 
			 throws ClienteException {

		String endpoint= obtenerServicio(pathRelativoEndpoint);
		HttpRequestBase request=  new HttpGet(endpoint);		
		return sendRequest(request, headers);
	}
	
	
	/**
	 * Encargado de generar una petici&oacute;n a un recurso REST
	 *
	 * @param request El objeto request, que contiene la informaci&oacute;n de la petici&oacute;n
	 * @param headers Los encabezados a incluir en la petici&oacute;n
	 * @return La respuesta del recurso REST
	 *
	 * @throws ClienteException  Error al consumir el recurso REST
	 */
	private HttpResponse sendRequest(HttpRequestBase request, Header[] headers)
		throws ClienteException {
		HttpClient client = getClienteRest();		
		if(ArrayUtils.isNotEmpty(headers)){
			for(Header header : headers){
				request.addHeader(header);
			}
		}
		
		HttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (IOException exception) {
			throw new ClienteException("Ocurrio un problema al establecer comunicacion con el servicio : "+exception.getMessage(), exception);
		} 
		return response;
	}
	

	/**
	 * Obtiene la url completa del recurso REST a consumir {esquema}://{host}/{contextbase}/pathRelativo
	 * <p>
	 * La base de la url <code>{esquema}://{host}/{contextbase}</code> se obtiene mediante la b&uacute;squeda de una variable de entorno,
	 *  configurada en el servidor de aplicaciones y definida en el archivo de propiedades de configuraci&oacute;n del sistema.
	 *
	 * @param pathRelativoEndpoint el path relativo al endpoint
	 * @return La url completa del recurso rest
	 * @throws ClienteException Error al obtener la url
	 */
	private String obtenerServicio(String pathRelativoEndpoint) throws ClienteException {
		
		//Se obtiene el nombre de la variable de entorno de las propiedades de configuraci&oacute;n
		String variableEntornoHostService = AplicacionWebPropertiesUtil.getPropiedades().obtenerPropiedad(JNDI_HOST_SERVICIOS_REST);
		if(StringUtils.isBlank(variableEntornoHostService)) {
			throw new ClienteException("propiedad "+JNDI_HOST_SERVICIOS_REST+" no definida en archivo de propiedades");
		}			
		
		//Se inspecciona en las variables de entorno la configuraci&oacute;n del servidor
		String hostService = JndiResourceUtil.lookupResourceServer(variableEntornoHostService);
		if(StringUtils.isBlank(hostService)) {
			throw new ClienteException("Variable de entorno "+variableEntornoHostService+" no existe en el servidor");
		}
		
		
		//Se construye la url
		String service = hostService.endsWith("/") ? hostService.substring(0, hostService.length()-1) : hostService;
		service += pathRelativoEndpoint.startsWith("/") ? pathRelativoEndpoint : "/".concat(pathRelativoEndpoint);  		
		return service;
	}
	
	

	/**
	 * Obtiene el cliente REST
	 *
	 * @return El cliente REST
	 * @throws ClienteException the cliente exception
	 */
	private HttpClient getClienteRest() throws ClienteException{
		
		//Se inicia el proceso de configuracion del contexto SSL
		
		//Se crea el contexto builder para configurar las conectividad por medio de SSL
		SSLContextBuilder contextBuilder = SSLContexts.custom();		
		//Se estable el protocolo de comunicacion a TLS
		contextBuilder.useTLS();			
		
		//
		//Configuracion utilizada para forzar que toda peticion en el contexto HTTPS sea valida
		//devolviendo valido sin importar el estado del certificado HTTPS.
		//
		try {			
			contextBuilder.loadTrustMaterial(null, new TrustStrategy() {
				@Override
			    public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			        return true;
			    }
			});
		} catch (NoSuchAlgorithmException | KeyStoreException  e) {
			throw new ClienteException("Ocurrio un problema al cargar el mecanismo de verificacion de host", e);		
		}
		
		//
		//Se verifica si se configura y si existe instalado un certificado de seguridad a nivel cliente en un
		//repositorio especifico. De existir se adjunta al contexto SSL
		//
		if(esCertificadoClienteConfigurado()){
			cargarCertificadoClienteSeguridadPK12(contextBuilder);
		}
		
		//Se crea el contexto SSL
		SSLContext sslContext = null;
		try {
			sslContext = contextBuilder.build();			
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			throw new ClienteException("Ocurrio un problema al crear el contexto SSL");
		}
		
		//Se crea el builder para configurar los parametros a utilizar para un cliente HTTP.
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		//Se integra el contexto SSL
		clientBuilder.setSslcontext(sslContext);
		
		//Se especifica un fabrica de conexion seguras (SSL)
		SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, 
															(X509HostnameVerifier)SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		//Se registra una fabrica de conexiones, donde se carga la fabrica de conexiones seguras (HTTPS) previamente configurada, 
		//y se registra la posibilidad para generar conexciones normales (HTTP).
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
							.register("http", PlainConnectionSocketFactory.getSocketFactory())
							.register("https", sslSocketFactory)
							.build();
		
		//Se establece el mecanismo que despachara las conexciones al cliente
		PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		clientBuilder.setConnectionManager(connMgr);
	    	    
		//Se genera el cliente HTTP/HTTPS
		return clientBuilder.build();	
	}
	
	
	/**
	 * Se obtiene el archivo ".p12" de la ruta especificada. y se carga al contexto SSL mediante un KeyStore.
	 * 
	 * @param contextBuilder El contexto SSL
	 * @throws ClienteException Error al generar el keystore de certificado cliente.
	 */
	private void cargarCertificadoClienteSeguridadPK12(SSLContextBuilder contextBuilder) 
			throws ClienteException {		
		try {
			KeyStore clientStore = KeyStore.getInstance("PKCS12");
			File certificadoClienteP12 = getArchivoCertificadoJNDI();
			String contraseniaCertificado = getContraseniaCertificadoJNDI();
			try( FileInputStream certificadoFile = new FileInputStream(certificadoClienteP12);){			
				clientStore.load(certificadoFile, contraseniaCertificado.toCharArray());
				contextBuilder.loadKeyMaterial(clientStore, contraseniaCertificado.toCharArray());
			}
		} catch (KeyStoreException e) {
			throw new ClienteException("Ocurrio un problema al crear el keystore del certificado cliente", e);			
		} catch (FileNotFoundException e) {
			throw new ClienteException("Ocurrio un problema al obtener el archivo del certificado cliente", e);
		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
			throw new ClienteException("Ocurrio un problema al cargar el certificado al keystore", e);
		} catch (UnrecoverableKeyException e) {
			throw new ClienteException("Ocurrio un problema al cargar keystore al contexto SSL", e);
		} 
	}
	
	/**
	 * Obtiene la contrase&ntilde;a ubicada en la variable de entorno especificada en el archivo de propiedades del sistema (jndi.contrasenia.certificadocliente.global). 
	 *
	 * @return La contrase&ntilde;a del certificado
	 * @throws ClienteException Error al obtener el password
	 */
	private String getContraseniaCertificadoJNDI() throws ClienteException {		
		String variableEntornoContrasenia = AplicacionWebPropertiesUtil.getPropiedades().obtenerPropiedad(JNDI_CONTRASENIA_CERTIFICADO_CLIENTE);		
		String contrasenia = JndiResourceUtil.lookupResourceServer(variableEntornoContrasenia);
		if(StringUtils.isBlank(contrasenia)){
			throw new ClienteException("No se localizo la contrase\u00f1a del certificado cliente ");
		}		
		return contrasenia;
	}
	
	/**
	 * Obtiene archivo certificado ubicado donde lo indica la variable de entorno especificada en el archivo de propiedades del sistema (jndi.ubicacion.certificadocliente.global).
	 *
	 * @return the archivo certificado JNDI
	 */
	private File getArchivoCertificadoJNDI(){
		File certificado = null;
		String variableEntornoCertificado = AplicacionWebPropertiesUtil.getPropiedades().obtenerPropiedad(JNDI_UBICACION_CERTIFICADO_CLIENTE);
		if(StringUtils.isNotBlank(variableEntornoCertificado)){
			String rutaCertificado = JndiResourceUtil.lookupResourceServer(variableEntornoCertificado);
			if(StringUtils.isNotBlank(rutaCertificado)){
				certificado = new File(rutaCertificado);				
			}
		}		
		return certificado;
	}
	
	/**
	 * Verifica si el certificado es configurado y existe en la ruta que se especifica
	 *
	 * @return true, Si existe el certificado
	 */
	private boolean esCertificadoClienteConfigurado(){
		File certificado = getArchivoCertificadoJNDI();
		return certificado != null && certificado.exists();
	}
}
