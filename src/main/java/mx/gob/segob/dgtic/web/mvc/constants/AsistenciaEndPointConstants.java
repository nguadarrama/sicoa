package mx.gob.segob.dgtic.web.mvc.constants;

public final class AsistenciaEndPointConstants {
	
	/**
	 * Restringe la construccion de la clase para evitar una creacion mediante el constructor por default.
	 */
	private AsistenciaEndPointConstants(){
		throw new IllegalStateException("Constants class");
	}
	
	public static final String WEB_SERVICE_INFO_ASISTENCIA_EMPLEADO =  "/asistencia/obtieneAsistenciasEmpleadoMes";
	public static final String WEB_SERVICE_INFO_ASISTENCIA_EMPLEADO_RANGO =  "/asistencia/obtieneAsistenciasEmpleadoRango";
	public static final String WEB_SERVICE_INFO_ASISTENCIA_EMPLEADO_RANGO_COORDINADOR =  "/asistencia/obtieneAsistenciasEmpleadoRangoCoordinador";
	public static final String WEB_SERVICE_INFO_ASISTENCIA_EMPLEADO_RANGO_DIRECCION =  "/asistencia/obtieneAsistenciasEmpleadoRangoDireccion";
	public static final String WEB_SERVICE_INFO_ASISTENCIA_EMPLEADO_ID =  "/asistencia/obtieneAsistenciaPorId";
	public static final String WEB_SERVICE_INFO_ASISTENCIA_JUSTIFICA =  "/asistencia/creaIncidencia";
	public static final String WEB_SERVICE_INFO_ASISTENCIA_DICTAMINA =  "/asistencia/dictaminaIncidencia";
	public static final String WEB_SERVICE_INFO_ASISTENCIA_DESCUENTA =  "/asistencia/descuentaIncidencia";
}
