package mx.gob.segob.dgtic.web.mvc.constants;


public final class CatalogoEndPointConstants {
	
	/**
	 * Restringe la construccion de la clase para evitar una creacion mediante el constructor por default.
	 */
	private CatalogoEndPointConstants(){
		throw new IllegalStateException("Constants class");
	}
	
	/**
	 * Constante que representa el atributo WEB_SERVICE_INFO_HORARIO, path del WebService para obtener la informaci&oacute;n del catálogo horario. 
	 */	
	public static final String WEB_SERVICE_INFO_HORARIO =  "/catalogo/obtieneHorarios";
	public static final String WEB_SERVICE_BUSCA_HORARIO = "/catalogo/buscaHorario";
	public static final String WEB_SERVICE_MODIFICA_HORARIO = "/catalogo/modificaHorario";
	public static final String WEB_SERVICE_AGREGA_HORARIO = "/catalogo/agregaHorario";
	public static final String WEB_SERVICE_ELIMINA_HORARIO = "/catalogo/eliminaHorario";
	
	public static final String WEB_SERVICE_INFO_TIPODIA =  "/catalogo/obtieneTipoDias";
	public static final String WEB_SERVICE_BUSCA_TIPODIA = "/catalogo/buscaTipoDia";
	public static final String WEB_SERVICE_MODIFICA_TIPODIA = "/catalogo/modificaTipoDia";
	public static final String WEB_SERVICE_AGREGA_TIPODIA = "/catalogo/agregaTipoDia";
	public static final String WEB_SERVICE_ELIMINA_TIPODIA = "/catalogo/eliminaTipoDia";

	//Usuario
	public static final String WEB_SERVICE_INFO_USUARIO =  "/catalogo/obtieneUsuarios";
	public static final String WEB_SERVICE_BUSCA_USUARIO = "/catalogo/buscaUsuario";
	public static final String WEB_SERVICE_MODIFICA_USUARIO = "/catalogo/modificaUsuario";
	public static final String WEB_SERVICE_AGREGA_USUARIO = "/catalogo/agregaUsuario";
	public static final String WEB_SERVICE_ELIMINA_USUARIO = "/catalogo/eliminaUsuario";
	
	//
	public static final String WEB_SERVICE_INFO_PERFIL =  "/catalogo/obtienePerfiles";
}

