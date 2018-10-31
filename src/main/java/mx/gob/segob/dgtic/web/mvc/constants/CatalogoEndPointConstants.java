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
	public static final String WEB_SERVICE_CAT_JUSTIFICACION =  "/catalogo/obtieneLista";
	public static final String WEB_SERVICE_BUSCA_JUSTIFICACION = "/catalogo/buscaJustificacion";
	public static final String WEB_SERVICE_MODIFICA_JUSTIFICACION = "/catalogo/modificaJust";
	public static final String WEB_SERVICE_AGREGA_JUSTIFICACION = "/catalogo/agregaJust";
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
	public static final String WEB_SERVICE_BUSCA_USUARIO_POR_ID = "/catalogo/buscaUsuarioPorId";
	public static final String WEB_SERVICE_INFO_JEFES =  "/catalogo/obtieneJefes";

	//Archivo
			public static final String WEB_SERVICE_GUARDA_ARCHIVO="/catalogo/agregaArchivo";
			public static final String WEB_SERVICE_ACTUALIZA_ARCHIVO="/catalogo/modificaArchivo";
			public static final String WEB_SERVICE_BUSCA_ARCHIVO="/catalogo/buscaArchivo";
	//
	public static final String WEB_SERVICE_INFO_PERFIL =  "/catalogo/obtienePerfiles";
	
	//Vacaciones
		public static final String WEB_SERVICE_INFO_VACACONES = "/catalogo/obtieneDetalleVacaciones";
		public static final String WEB_SERVICE_BUSCA_VACACIONES = "/catalogo/buscaDetalleVacacion";
		public static final String WEB_SERVICE_MODIFICA_VACACIONES = "/catalogo/modificaDetalleVacacion";
		public static final String WEB_SERVICE_AGREGA_VACACIONES = "/catalogo/agregaDetalleVacacion";
		public static final String WEB_SERVICE_ELIMINA_VACACIONES = "/catalogo/eliminaDetalleVacacion";
		public static final String WEB_SERVICE_CONSULTA_VACACION="/catalogo/buscaVacacionPeriodoPorClaveUsuarioYPeriodo";
		public static final String WEB_SERVICE_OBTIENE_VACACIONES_PROPIAS = "/catalogo/consultaVacacionesPropiasPorFiltros";
		public static final String WEB_SERVICE_OBTIENE_VACACIONES_POR_FILTROS = "/catalogo/obtenerVacacionesPorFiltros";
		public static final String WEB_SERVICE_GENERA_REPORTE = "/catalogo/generaReporte";
		
		
		//Periodo
		public static final String WEB_SERVICE_BUSCA_PERFIL_POR_CLAVE_USUARIO="/catalogo/buscaPeriodoPorClaveUsuario";
		public static final String WEB_SERVICE_OBTIENE_PERIODOS = "/catalogo/obtienePeriodos";
		public static final String WEB_SERVICE_AGREGA_PERIODO = "/catalogo/agregaPeriodo";
		public static final String WEB_SERVICE_MODIFICA_PERIODO ="/catalogo/modificaEstatusPeriodo";
		public static final String WEB_SERVICE_BUSCA_PERIODO ="/catalogo/buscaPeriodo";
		
		//Unidad Administrativa
		public static final String WEB_SERVICE_INFO_UNIDAD_ADMINISTRATIVA =  "/catalogo/obtieneUnidadesAdministrativas";
		public static final String WEB_SERVICE_GUARDA_USUARIO_UNIDAD_ADMINISTRATIVA =  "/catalogo/consultaRegistraUsuarioUnidadAdministrativa";
		public static final String WEB_SERVICE_CONSULTA_RESPONSABLE =  "/catalogo/consultaResponsable";
		public static final String WEB_SERVICE_INFO_UNIDADES_ADMINISTRATIVAS =  "/catalogo/obtieneUnidadesAdministrativasCompletas";
		public static final String WEB_SERVICE_OBTIENE_NIVELES_EMPLEADOS = "/catalogo/obtieneNivelesEmpleado";
		//Perfiles
		public static final String WEB_SERVICE_BUSCA_PERFILES_USUARIO = "/catalogo/consultaPerfilesUsuario";
		public static final String WEB_SERVICE_AGREGA_PERFILES_USUARIO = "/catalogo/agregaEliminaPerfilesUsuario";
				
		//Estatus
		public static final String WEB_SERVICE_INFO_ESTATUS = "/catalogo/obtieneEstatus";
		public static final String WEB_SERVICE_INFO_LISTA_ESTATUS = "/catalogo/obtieneListaCompletaEstatus";
		
		/**
		 * Constante que representa el atributo WEB_SERVICE_INFO_DIA_FESTIVO, path del WebService para obtener la información del catálogo día festivo. 
		 */	
		public static final String WEB_SERVICE_INFO_DIA_FESTIVO =  "/diaFestivo/obtiene";
		public static final String WEB_SERVICE_BUSCA_DIA_FESTIVO = "/diaFestivo/busca";
		public static final String WEB_SERVICE_MODIFICA_DIA_FESTIVO = "/diaFestivo/modifica";
		public static final String WEB_SERVICE_AGREGA_DIA_FESTIVO = "/diaFestivo/agrega";
		public static final String WEB_SERVICE_ELIMINA_DIA_FESTIVO = "/diaFestivo/elimina";
		
		//PeriodoVacacion
		public static final String WEB_SERVICE_OBTIENE_USUARIOS_VACACIONES = "/catalogo/obtenerUsuariosVacacionesPorFiltros";
		
		//Niveles Organizacionales
		public static final String 	WEB_SERVICE_OBTIENE_NIVELES = "/catalogo/obtieneNiveles";
		public static final String WEB_SERVICE_AGREGA_NIVEL_ORGANIZACIONAL = "/catalogo/agregaNivel";
		public static final String WEB_SERVICE_BUSCA_NIVEL = "/catalogo/buscaNivel";
		public static final String WEB_SERVICE_MODIFICA_NIVEL = "/catalogo/modificaNivel";
		
}

