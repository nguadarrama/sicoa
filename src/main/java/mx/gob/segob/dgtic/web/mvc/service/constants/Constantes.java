package mx.gob.segob.dgtic.web.mvc.service.constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import mx.gob.segob.dgtic.web.config.security.handler.LogoutCustomHandler;

public class Constantes {

  private static final Logger logger = LoggerFactory.getLogger(LogoutCustomHandler.class);

  private Constantes() {

  }

  public static final String ETIQUETA_AUTHORIZATION = "Authorization";
  public static final String ETIQUETA_BEARER = "Bearer ";
  public static final String ETIQUETA_TOKEN = "_token";
  public static final String PERIODO = "periodo";
  public static final String BUSQUEDA = "busqueda";
  public static final String DETALLEVACACION = "detalleVacacion";

  public static final String FECHA_FORMATO_LARGO = "MMM dd, yyyy HH:mm:ss";
  public static final String FECHA_FORMATO_MMM_DD_YYYY = "MMM dd, yyyy";
  public static final String FECHA_FORMATO_DD_MM_YYYY = "dd-MM-yyyy";
  public static final String ES = "es_ES";
  public static final String EXCEPCION = "Excepcion: {}";

  public static String formatearFecha(String fecha) {
    if (fecha.length() > 13) {
      Date date = null;
      SimpleDateFormat sdf = new SimpleDateFormat(FECHA_FORMATO_LARGO, new Locale(ES));
      SimpleDateFormat sdf1 = new SimpleDateFormat(FECHA_FORMATO_DD_MM_YYYY);
      try {
        date = sdf.parse(fecha);
        fecha = sdf1.format(date);
      } catch (ParseException e) {
        logger.error(EXCEPCION, e.getMessage());
      }
    } else if (fecha.length() > 10) {
      Date date = null;
      SimpleDateFormat sdf = new SimpleDateFormat(FECHA_FORMATO_MMM_DD_YYYY);
      SimpleDateFormat sdf1 = new SimpleDateFormat(FECHA_FORMATO_DD_MM_YYYY);
      try {
        date = sdf.parse(fecha);
        fecha = sdf1.format(date);
      } catch (ParseException e) {
        logger.error(EXCEPCION, e.getMessage());
      }
    }
    return fecha;
  }

}
