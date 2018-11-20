package mx.gob.segob.dgtic.web.mvc.dto;

import mx.gob.segob.dgtic.web.mvc.service.constants.Constantes;

public class Comision {

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
  return Constantes.formatearFecha(fechaInicio);
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
  return Constantes.formatearFecha(fechaFin);
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
   * @return the fechaRegistro
   */
  public String getFechaRegistro() {
  return Constantes.formatearFecha(fechaRegistro);
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

  /**
   * @return the comisionDesc
   */
  public String getComisionDesc() {
    return comisionDesc;
  }

  /**
   * @param comisionDesc the comisionDesc to set
   */
  public void setComisionDesc(String comisionDesc) {
    this.comisionDesc = comisionDesc;
  } 
  

}
