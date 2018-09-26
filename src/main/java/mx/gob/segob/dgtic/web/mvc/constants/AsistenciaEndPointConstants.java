package mx.gob.segob.dgtic.web.mvc.constants;

public final class AsistenciaEndPointConstants {
	
	/**
	 * Restringe la construccion de la clase para evitar una creacion mediante el constructor por default.
	 */
	private AsistenciaEndPointConstants(){
		throw new IllegalStateException("Constants class");
	}
	
	public static final String WEB_SERVICE_INFO_ASISTENCIA_EMPLEADO =  "/asistencia/obtieneAsistenciasEmpleado";
}
