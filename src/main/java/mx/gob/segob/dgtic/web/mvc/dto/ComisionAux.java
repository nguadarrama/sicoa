package mx.gob.segob.dgtic.web.mvc.dto;


public class ComisionAux {
  
  private Integer idComision;
  private Integer idUsuario;
  private Integer idResponsable;
  private Integer idArchivo;
  private Integer idEstatus;
  private String fechaInicio;
  private String fechaFin;
  private Integer dias;
  private String comision;
  private String fechaRegistro;
  private Integer idHorario;
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
  public ComisionAux(Integer idComision, Integer idUsuario, Integer idResponsable,
      Integer idArchivo, Integer idEstatus, String fechaInicio, String fechaFin, Integer dias,
      String comision, String fechaRegistro, Integer idHorario) {
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
  
  public ComisionAux() {
    
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
  public Integer getIdUsuario() {
    return idUsuario;
  }
  /**
   * @param idUsuario the idUsuario to set
   */
  public void setIdUsuario(Integer idUsuario) {
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
  public Integer getIdArchivo() {
    return idArchivo;
  }
  /**
   * @param idAarchivo the idAarchivo to set
   */
  public void setIdArchivo(Integer idAarchivo) {
    this.idArchivo = idAarchivo;
  }
  
  
  /**
   * @return the idEstatus
   */
  public Integer getIdEstatus() {
    return idEstatus;
  }
  /**
   * @param idEstatus the idEstatus to set
   */
  public void setIdEstatus(Integer idEstatus) {
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

  public Integer getIdHorario() {
    return idHorario;
  }

  public void setIdHorario(Integer idHorario) {
    this.idHorario = idHorario;
  }
  
  


}
