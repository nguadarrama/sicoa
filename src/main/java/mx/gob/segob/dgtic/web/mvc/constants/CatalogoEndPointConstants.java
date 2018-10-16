package mx.gob.segob.dgtic.web.mvc.constants;


public final class CatalogoEndPointConstants {
	
	/**
	 * Restringe la construccion de la clase para evitar una creacion mediante el constructor por default.
	 */
	private CatalogoEndPointConstants(){
		throw new IllegalStateException("Constants class");
	}
	
	/**
	 * Constante que representa el atributo WEB_SERVICE_INFO_HORARIO, path del WebService para obtener la información del catálogo horario. 
	 */	
	public static final String WEB_SERVICE_INFO_HORARIO =  "/catalogo/obtieneHorarios";
	public static final String WEB_SERVICE_BUSCA_HORARIO = "/catalogo/buscaHorario";
	public static final String WEB_SERVICE_MODIFICA_HORARIO = "/catalogo/modificaHorario";
	public static final String WEB_SERVICE_AGREGA_HORARIO = "/catalogo/agregaHorario";
	public static final String WEB_SERVICE_ELIMINA_HORARIO = "/catalogo/eliminaHorario";
	/**
	 * Constante que representa el atributo WEB_SERVICE_INFO_TIPO_DIA, path del WebService para obtener la información del catálogo tipo Día. 
	 */	
	public static final String WEB_SERVICE_INFO_TIPODIA =  "/catalogo/obtieneTipoDias";
	public static final String WEB_SERVICE_BUSCA_TIPODIA = "/catalogo/buscaTipoDia";
	public static final String WEB_SERVICE_MODIFICA_TIPODIA = "/catalogo/modificaTipoDia";
	public static final String WEB_SERVICE_AGREGA_TIPODIA = "/catalogo/agregaTipoDia";
	public static final String WEB_SERVICE_ELIMINA_TIPODIA = "/catalogo/eliminaTipoDia";
	/**
	 * Constante que representa el atributo WEB_SERVICE_INFO_JUSTIFICACION, path del WebService para obtener la información del catálogo justificacion. 
	 */	
	public static final String WEB_SERVICE_INFO_JUSTIFICACION =  "/catalogo/obtieneJustificaciones";
	public static final String WEB_SERVICE_BUSCA_JUSTIFICACION = "/catalogo/buscaJustificacion";
	public static final String WEB_SERVICE_MODIFICA_JUSTIFICACION = "/catalogo/modificaJustificacion";
	public static final String WEB_SERVICE_AGREGA_JUSTIFICACION = "/catalogo/agregaJustificacion";
	public static final String WEB_SERVICE_ELIMINA_JUSTIFICACION = "/catalogo/eliminaJustificacion";
	//Usuario
	public static final String WEB_SERVICE_INFO_USUARIO =  "/catalogo/obtieneUsuarios";
	public static final String WEB_SERVICE_BUSCA_USUARIO = "/catalogo/buscaUsuario";
	public static final String WEB_SERVICE_MODIFICA_USUARIO = "/catalogo/modificaUsuario";
	public static final String WEB_SERVICE_AGREGA_USUARIO = "/catalogo/agregaUsuario";
	public static final String WEB_SERVICE_ELIMINA_USUARIO = "/catalogo/eliminaUsuario";
	public static final String WEB_SERVICE_REINICIA_CONTRASENIA = "/catalogo/reiniciaContrasenia";
	public static final String WEB_SERVICE_INFO_RESPONSABLES =  "/catalogo/obtieneResponsables";
	public static final String WEB_SERVICE_ACEPTAORECHAZA_VACACIONES = "/catalogo/aceptaORechazaDetalleVacacion";

	//Archivo
			public static final String WEB_SERVICE_GUARDA_ARCHIVO="/catalogo/agregaArchivo";
	//
	public static final String WEB_SERVICE_INFO_PERFIL =  "/catalogo/obtienePerfiles";
	
	//Vacaciones
		public static final String WEB_SERVICE_INFO_VACACONES = "/catalogo/obtieneDetalleVacaciones";
		public static final String WEB_SERVICE_BUSCA_VACACIONES = "/catalogo/buscaDetalleVacacion";
		public static final String WEB_SERVICE_MODIFICA_VACACIONES = "/catalogo/modificaDetalleVacacion";
		public static final String WEB_SERVICE_AGREGA_VACACIONES = "/catalogo/agregaDetalleVacacion";
		public static final String WEB_SERVICE_ELIMINA_VACACIONES = "/catalogo/eliminaDetalleVacacion";
		public static final String WEB_SERVICE_CONSULTA_VACACION="/catalogo/buscaVacacionPeriodoPorClaveUsuarioYPeriodo";
		
		//Periodo
		public static final String WEB_SERVICE_BUSCA_PERFIL_POR_CLAVE_USUARIO="/catalogo/buscaPeriodoPorClaveUsuario";
		public static final String WEB_SERVICE_OBTIENE_PERIODOS = "/catalogo/obtienePeriodos";
		public static final String WEB_SERVICE_AGREGA_PERIODO = "/catalogo/agregaPeriodoVacacional";
		public static final String WEB_SERVICE_MODIFICA_PERIODO = "/catalogo/modificaPeriodoVacacional";
		
		//Unidad Administrativa
		public static final String WEB_SERVICE_INFO_UNIDAD_ADMINISTRATIVA =  "/catalogo/obtieneUnidadesAdministrativas";
		public static final String WEB_SERVICE_GUARDA_USUARIO_UNIDAD_ADMINISTRATIVA =  "/catalogo/consultaRegistraUsuarioUnidadAdministrativa";
		
		//Perfiles
		public static final String WEB_SERVICE_BUSCA_PERFILES_USUARIO = "/catalogo/consultaPerfilesUsuario";
		public static final String WEB_SERVICE_AGREGA_PERFILES_USUARIO = "/catalogo/agregaEliminaPerfilesUsuario";
}

