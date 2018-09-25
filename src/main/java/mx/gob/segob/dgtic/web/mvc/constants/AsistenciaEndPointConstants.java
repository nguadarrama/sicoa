package mx.gob.segob.dgtic.web.mvc.constants;

public final class AsistenciaEndPointConstants {
	
	/**
	 * Restringe la construccion de la clase para evitar una creacion mediante el constructor por default.
	 */
	private AsistenciaEndPointConstants(){
		throw new IllegalStateException("Constants class");
	}
	
	/**
	 * Constante que representa el atributo WEB_SERVICE_INFO_HORARIO, path del WebService para obtener la informaci&oacute;n del catálogo horario. 
	 */	
	public static final String WEB_SERVICE_INFO_ASISTENCIA =  "/asistencia/obtieneAsistencias";
}
