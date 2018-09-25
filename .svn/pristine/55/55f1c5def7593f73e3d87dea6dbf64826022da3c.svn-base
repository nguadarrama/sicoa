/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.mvc.util.crypto;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 *  Clase de apoyo para realizar una conversi&oacute;n de HASH.
 */
public class HashUtils {

	 /**
     *  Instancia para realizar log.
     */
	private static final Logger logger = LoggerFactory.getLogger(HashUtils.class);
	

	/**
	 * Constructor de utiler&iacute;a para acceso completamente est&aacute;tico.
	 */
	private HashUtils() {
		throw new IllegalStateException("Utility class");
	}
	
	/**
	 * Funci&oacute;n que ejecuta el mismo algoritmo utilizado por la funcion MySql.password()
	 * <p>
	 * Se utiliza un algoritmo de doble hash SHA1
	 *
	 * @param palabra El valor al cual se aplicara el hash indicado 
	 * @return El valor con la conversi&oacute;n password
	 */
	public static String mysqlPassword(String palabra){
		try {
	        MessageDigest diggestSha1 = MessageDigest.getInstance("SHA-1");
	        diggestSha1.reset();
	        diggestSha1.update(palabra.getBytes());
	        byte[] encodedSha1 = diggestSha1.digest();
	
	        diggestSha1.reset();
	        diggestSha1.update(encodedSha1);
	        byte[] encodedDobleSha1 = diggestSha1.digest();

	        StringBuilder hashPassword = new StringBuilder();
	        hashPassword.append("*");
	        hashPassword.append(Hex.encodeHex(encodedDobleSha1, false));
	        return hashPassword.toString();
		}  catch (NoSuchAlgorithmException exception) {
			logger.error(exception.getMessage(), exception);
			return palabra;
		}
	}
	
	
	/**
	 * Funci&oacute;n que ejecuta el algoritmo MD5.
	 *
	 * @param palabra El valor al cual se aplicara el hash indicado
	 * @return  El valor con la conversi&oacute;n MD5
	 */
	public static String md5(String palabra) {
		try {
			MessageDigest diggestMD5 = MessageDigest.getInstance("MD5");
			diggestMD5.update(palabra.getBytes());
			byte [] encodedMD5 = diggestMD5.digest();
			StringBuilder hashMD5 = new StringBuilder();
			for (byte b : encodedMD5) {
				hashMD5.append(String.format("%02x", b));
			}
			return hashMD5.toString();
		} catch (NoSuchAlgorithmException exception) {
			logger.error(exception.getMessage(), exception);
			return palabra;
		}
	}
}
