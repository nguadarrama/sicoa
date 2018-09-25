/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.mvc.util.rest.exception;

/**
 * Clase de excepci&oacute;n para el cliente rest
 */
public class ClienteException  extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4954198801484848931L;

	/**
	 * Instancia una nueva excepci&oacute;n de cliente REST con parametros
	 *
	 * @param mensaje El mensaje a integrar para identificar la excepci&oacute;n
	 */
	public ClienteException(String mensaje) {
		super(mensaje);
	}	
	
	/**
	 * Instancia una nueva excepci&oacute;n de cliente REST con parametros
	 *
	 * @param mensaje El mensaje a integrar para identificar la excepci&oacute;n
	 * @param cause La excepci&oacute;n causa
	 */
	public ClienteException(String mensaje, Throwable cause) {
		super(mensaje, cause);
	}	
}
