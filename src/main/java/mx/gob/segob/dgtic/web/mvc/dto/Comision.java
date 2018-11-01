package mx.gob.segob.dgtic.web.mvc.dto;

import java.util.Date;

public class Comision {

  private Integer idComision;
  private Usuario idUsuario;
  private Integer idResponsable;
  private Archivo idArchivo;
  private Estatus idEstatus;
  private String fechaInicio;
  private String fechaFin;
  private Integer dias;
  private String comision;
  private String fechaRegistro;
  private Horario idHorario;
  private String mensaje;
  /**
   * @param idComision
   * @param idUsuario
   * @param idResponsable
   * @param idAarchivo
   * @param idEstatus
   * @param fechaInicio
   * @param fechaFin
   * @param dias
   * @param comision
   * @param fechaRegistro
   */
  public Comision(Integer idComision, Usuario idUsuario, Integer idResponsable, Archivo idArchivo,
      Estatus idEstatus, String fechaInicio, String fechaFin, Integer dias, String comision,
      String fechaRegistro, Horario idHorario) {
    super();
    this.idComision = idComision;
    this.idUsuario = idUsuario;
    this.idResponsable = idResponsable;
    this.idArchivo = idArchivo;
    this.idEstatus = idEstatus;
    this.fechaInicio = fechaInicio;
    this.fechaFin = fechaFin;
    this.dias = dias;
    this.comision = comision;
    this.fechaRegistro = fechaRegistro;
    this.idHorario = idHorario;
  }
  
  public Comision() {
    
  }
  
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
    return comision;
  }
  /**
   * @param comision the comision to set
   */
  public void setComision(String comision) {
    this.comision = comision;
  }
  /**
   * @return the fechaRegistro
   */
  public String getFechaRegistro() {
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
