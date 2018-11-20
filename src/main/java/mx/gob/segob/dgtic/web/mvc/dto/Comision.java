package mx.gob.segob.dgtic.web.mvc.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import mx.gob.segob.dgtic.web.mvc.service.BaseService;

public class Comision extends BaseService {

  private Integer idComision;
  private Usuario idUsuario;
  private Integer idResponsable;
  private Archivo idArchivo;
  private Estatus idEstatus;
  private String fechaInicio;
  private String fechaFin;
  private Integer dias;
  private String comisionDesc;
  private String fechaRegistro;
  private Horario idHorario;
  private String mensaje;
  
  private static final String FECHA_FORMATO_LARGO = "MMM dd, yyyy HH:mm:ss";
  private static final String FECHA_FORMATO_MMM_DD_YYYY = "MMM dd, yyyy";
  private static final String FECHA_FORMATO_DD_MM_YYYY = "dd-MM-yyyy";
  private static final String ES = "es_ES";
  private static final String EXCEPCION = "Excepcion: {}";
  
  
  
  public String getMensaje() {
    return mensaje;
  }

  public void setMensaje(String mensaje) {
    this.mensaje = mensaje;
  }

  /**
   * @return the idComision
   */
  public Integer getIdComision() {
    return idComision;
  }
  /**
   * @param idComision the idComision to set
   */
  public void setIdComision(Integer idComision) {
    this.idComision = idComision;
  }
  /**
   * @return the idUsuario
   */
  public Usuario getIdUsuario() {
    return idUsuario;
  }
  /**
   * @param idUsuario the idUsuario to set
   */
  public void setIdUsuario(Usuario idUsuario) {
    this.idUsuario = idUsuario;
  }
  /**
   * @return the idResponsable
   */
  public Integer getIdResponsable() {
    return idResponsable;
  }
  /**
   * @param idResponsable the idResponsable to set
   */
  public void setIdResponsable(Integer idResponsable) {
    this.idResponsable = idResponsable;
  }
  /**
   * @return the idAarchivo
   */
  public Archivo getIdArchivo() {
    return idArchivo;
  }
  /**
   * @param idAarchivo the idAarchivo to set
   */
  public void setIdAarchivo(Archivo idAarchivo) {
    this.idArchivo = idAarchivo;
  }
  /**
   * @return the idEstatus
   */
  public Estatus getIdEstatus() {
    return idEstatus;
  }
  /**
   * @param idEstatus the idEstatus to set
   */
  public void setIdEstatus(Estatus idEstatus) {
    this.idEstatus = idEstatus;
  }
  /**
   * @return the fechaInicio
   */
  public String getFechaInicio() {
    if(fechaInicio.length()>13){
      Date date = null;
      SimpleDateFormat sdf = new SimpleDateFormat(FECHA_FORMATO_LARGO, new Locale(ES));
      SimpleDateFormat sdf1 = new SimpleDateFormat(FECHA_FORMATO_DD_MM_YYYY);
      try {
          date = sdf.parse(fechaInicio);
          fechaInicio = sdf1.format(date);
      } catch (ParseException e) {
        logger.error(EXCEPCION, e.getMessage());
      }
  }else if(fechaInicio.length()>10){
      Date date = null;
      SimpleDateFormat sdf = new SimpleDateFormat(FECHA_FORMATO_MMM_DD_YYYY);
      SimpleDateFormat sdf1 = new SimpleDateFormat(FECHA_FORMATO_DD_MM_YYYY);
      try {
          date = sdf.parse(fechaInicio);
          fechaInicio = sdf1.format(date);
      } catch (ParseException e) {
        logger.error(EXCEPCION, e.getMessage());
      }
  }
  return fechaInicio;
  }
  /**
   * @param fechaInicio the fechaInicio to set
   */
  public void setFechaInicio(String fechaInicio) {
    this.fechaInicio = fechaInicio;
  }
  /**
   * @return the fechaFin
   */
  public String getFechaFin() {
    if(fechaFin.length()>13){
      Date date = null;
      SimpleDateFormat sdf = new SimpleDateFormat(FECHA_FORMATO_LARGO, new Locale(ES));
      SimpleDateFormat sdf1 = new SimpleDateFormat(FECHA_FORMATO_DD_MM_YYYY);
      try {
          date = sdf.parse(fechaFin);
          fechaFin = sdf1.format(date);
      } catch (ParseException e) {
        logger.error(EXCEPCION, e.getMessage());
      }
  }else if(fechaFin.length()>10){
      Date date = null;
      SimpleDateFormat sdf = new SimpleDateFormat(FECHA_FORMATO_MMM_DD_YYYY);
      SimpleDateFormat sdf1 = new SimpleDateFormat(FECHA_FORMATO_DD_MM_YYYY);
      try {
          date = sdf.parse(fechaFin);
          fechaFin = sdf1.format(date);
      } catch (ParseException e) {
        logger.error(EXCEPCION, e.getMessage());
      }
  }
  return fechaFin;
  }
  /**
   * @param fechaFin the fechaFin to set
   */
  public void setFechaFin(String fechaFin) {
    this.fechaFin = fechaFin;
  }
  /**
   * @return the dias
   */
  public Integer getDias() {
    return dias;
  }
  /**
   * @param dias the dias to set
   */
  public void setDias(Integer dias) {
    this.dias = dias;
  }
  /**
   * @return the comision
   */
  public String getComision() {
    return comisionDesc;
  }
  /**
   * @param comisionDesc the comision to set
   */
  public void setComision(String comisionDesc) {
    this.comisionDesc = comisionDesc;
  }
  /**
   * @return the fechaRegistro
   */
  public String getFechaRegistro() {
    if(fechaRegistro.length()>13){
      Date date = null;
      SimpleDateFormat sdf = new SimpleDateFormat(FECHA_FORMATO_LARGO, new Locale(ES));
      SimpleDateFormat sdf1 = new SimpleDateFormat(FECHA_FORMATO_DD_MM_YYYY);
      try {
          date = sdf.parse(fechaRegistro);
          fechaRegistro = sdf1.format(date);
      } catch (ParseException e) {
          logger.error(EXCEPCION, e.getMessage());
      }
  }else if(fechaRegistro.length()>10){
      Date date = null;
      SimpleDateFormat sdf = new SimpleDateFormat(FECHA_FORMATO_MMM_DD_YYYY);
      SimpleDateFormat sdf1 = new SimpleDateFormat(FECHA_FORMATO_DD_MM_YYYY);
      try {
          date = sdf.parse(fechaRegistro);
          fechaRegistro = sdf1.format(date);
      } catch (ParseException e) {
        logger.error(EXCEPCION, e.getMessage());
      }
  }
  return fechaRegistro;
  }
  /**
   * @param fechaRegistro the fechaRegistro to set
   */
  public void setFechaRegistro(String fechaRegistro) {
    this.fechaRegistro = fechaRegistro;
  }

  public Horario getIdHorario() {
    return idHorario;
  }

  public void setIdHorario(Horario idHorario) {
    this.idHorario = idHorario;
  }

}
