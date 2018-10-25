package mx.gob.segob.dgtic.web.mvc.constants;


public class LicenciaMedicaEndPointConstants {

	/**
	 * Restringe la construccion de la clase para evitar una creacion mediante el constructor por default.
	 */
	private LicenciaMedicaEndPointConstants(){
		throw new IllegalStateException("Constants class");
	}
	public static final String WEB_SERVICE_INFO_LICENCIA =  "/licencia/obtieneLicenciasMedicas";
	public static final String WEB_SERVICE_BUSCA_LICENCIA = "/licencia/buscaLicenciaMedica";
	public static final String WEB_SERVICE_MODIFICA_LICENCIA = "/licencia/modificaLicenciaMedica";
	public static final String WEB_SERVICE_AGREGA_LICENCIA = "/licencia/agregaLicenciaMedica";
	public static final String WEB_SERVICE_ELIMINA_LICENCIA = "/licencia/eliminaLicenciaMedica";
	public static final String WEB_SERVICE_CONSULTA_LICENCIA_POR_FILTROS = "/licencia/obtieneListaLicenciaMedicaPorFiltros";
}
