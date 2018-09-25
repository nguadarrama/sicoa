/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.mvc.util.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Clase de apoyo utilizada para acceder a los diferentes atributos de un objeto {@link org.apache.http.HttpResponse} recibido por la utiler&iacute;a ClienteRestUtil 
 */
public class HttpResponseUtil {
	
	/**
	 * Instancia para realizar log 
	 */
	private static Logger logger = LoggerFactory.getLogger(HttpResponseUtil.class);	
	
	/** La constante DEFAULT_ENCONDING_CONTENT : constante que define el encoding del cuerpo la respuesta. */
	private static final String DEFAULT_ENCONDING_CONTENT = "UTF-8";
	
	/**
	 * Constructor de clase de utiler&iacute;a para prevenir se instancie
	 */
	private HttpResponseUtil(){
		throw new IllegalStateException("Utility class");
	}
	
	/**
	 * Obtiene el codigo de estatus del response.
	 *
	 * @param response El response de la petici&oacute;n
	 * @return El c&oacute;digo del estatus de la petici&oacute;n
	 */
	public static int getStatus(HttpResponse response){
		return response.getStatusLine().getStatusCode();
	}
	
	/**
	 * Obtiene el Content-Type que identifica el tipo de respuesta que se obtuvo
	 *
	 * @param response El response de la petici&oacute;n
	 * @return La cadena que identifica el Content-type de la respuesta
	 */
	public static String getContentType(HttpResponse response){
		String responseMediaType = "";
		if( response.getEntity() != null && 
			response.getEntity().getContentType() != null){
				responseMediaType = response.getEntity().getContentType().getValue();
		}
		return responseMediaType;
	}
	
	
	/**
	 * Verifica si la respuesta corresponde a un tipo de respuesta especifico mediante la evaluaci&oacute;n del Content-Type
	 *
	 * @param response La respuesta a evaluar
	 * @param contentType El Content-Type a verificar
	 * @return true, si el Content-Type de la respuesta corresponde
	 */
	public static boolean isContentType(HttpResponse response, ContentType contentType){
		boolean contieneMediaType = false;
		String contentTypeResponse = getContentType(response);
		if(contentTypeResponse.toUpperCase().contains(contentType.getMimeType().toUpperCase())){			
			contieneMediaType = true;
		}
		return contieneMediaType;
	}
	
	/**
	 * Obtiene el body de la respuesta en formato JSON
	 *
	 * @param response La respuesta a extraer el contenido
	 * @return El JSON del contenido de la respuesta
	 * @throws ParseException La excepci&oacute;n al convertir el cuerpo en formato JSON 
	 */
	public static JsonElement getJsonContent(HttpResponse response) 
		throws ParseException {
		JsonElement json = null;
		if(isContentType(response, ContentType.APPLICATION_JSON)){
			String encoding = getEncoding(response);
			
			try (
				InputStreamReader readerStream = new InputStreamReader(response.getEntity().getContent(), encoding);
			){
				JsonParser jsonParser = new JsonParser();
				json = jsonParser.parse(readerStream);
			} catch (IllegalStateException | IOException e) {
				throw new ParseException("Ocurrio un problema al convertir el content en JSON");
			}
		}
		return json;
	}
	
	/**
	 * Obtiene el cuerpo de la respuesta mediante un Inputstream
	 *
	 * @param response La respuesta
	 * @return El body en inputstream
	 */
	public static InputStream getInputStream(HttpResponse response){
		InputStream fileInput = null;
		if(response.getEntity().getContentLength() > 0){
			try {
				fileInput = response.getEntity().getContent();
			} catch (IllegalStateException | IOException e) {
				logger.error(e.getMessage(), e);
			}
		}		 
		return fileInput;
	}
	
	
	
	/**
	 * Obtiene un header de la respuesta 
	 *
	 * @param response La respuesta
	 * @param headerName El nombre con que si identifica el header a obtener
	 * @return El header buscado 
	 */
	public static Header getHeader(HttpResponse response, String headerName){
		return response.getFirstHeader(headerName);
	}
	
	/**
	 * Obtiene el encoding de la respuesta, de no estar especificado se interpreta con el encoding default.
	 *
	 * @param response La respuesta
	 * @return El encoding con el que se genero la respuesta.
	 */
	public static String getEncoding(HttpResponse response){
		HttpEntity entityResponse=response.getEntity();
		if(entityResponse == null){
			return DEFAULT_ENCONDING_CONTENT;
		}
		
		Header encondingHeader = entityResponse.getContentEncoding();
		if(encondingHeader != null && StringUtils.isNotBlank(encondingHeader.getValue())){
			return encondingHeader.getValue();
		} 
		
		Header responseMediaType = entityResponse.getContentType();
		String mediaType = responseMediaType.getValue().toUpperCase();
		String elementCharset = "charset".toUpperCase();
		
		String [] elementos = mediaType.split(";");
		for(String elemento : elementos){
			if(elemento.contains(elementCharset)){
				return elemento.split("=")[1].trim();
			}
		}
		return DEFAULT_ENCONDING_CONTENT;
	}
}
