package mx.gob.segob.dgtic.web.mvc.constants;

public class ComisionEndPointConstants {
  
  /**
   * Restringe la construccion de la clase para evitar una creacion mediante el constructor por default.
   */
  private ComisionEndPointConstants(){
      throw new IllegalStateException("Constants class");
  }
  
  public static final String WEB_SERVICE_CONSULTA_COMISION_EMPLEADOS_POR_FILTROS = "/comisiones/obtieneListaComisionPorFiltrosEmpleados";
  public static final String WEB_SERVICE_CONSULTA_COMISION_POR_FILTROS = "/comisiones/obtieneListaComisionPorFiltros";
  public static final String WEB_SERVICE_CONSULTA_COMISION_POR_UNIDAD = "/comisiones/obtieneComisionesPorUnidad";
  public static final String WEB_SERVICE_AGREGA_COMISION = "/comisiones/agregaComision";
  
  public static final String WEB_SERVICE_INFO_COMISION =  "/comisiones/obtieneComisiones";
  public static final String WEB_SERVICE_BUSCA_COMISION = "/comisiones/buscaComision";
  public static final String WEB_SERVICE_MODIFICA_COMISION = "/comisiones/modificaComision";
  public static final String WEB_SERVICE_ELIMINA_COMISION = "/comisiones/eliminaComision";
  public static final String WEB_SERVICE_MODIFICA_COMISION_ESTATUS_ARCHIVO = "/comisiones/modificaComisionEstatusArchivo";
  
  public static final String WEB_SERVICE_GENERA_REPORTE = "/comisiones/generarReporte";

}
