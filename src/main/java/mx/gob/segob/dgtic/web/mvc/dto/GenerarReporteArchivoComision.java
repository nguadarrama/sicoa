package mx.gob.segob.dgtic.web.mvc.dto;

public class GenerarReporteArchivoComision {
  
  private String nombre;
  private String unidad;
  private String comision;
  private String fechaInicio;
  private String fechaFinal;
  private String horario;

  /**
   * @param nombre
   * @param unidad
   * @param comision
   * @param fechaInicio
   * @param fechaFinal
   * @param horario
   */
  public GenerarReporteArchivoComision(String nombre, String unidad, String comision,
      String fechaInicio, String fechaFinal, String horario) {
    super();
    this.nombre = nombre;
    this.unidad = unidad;
    this.comision = comision;
    this.fechaInicio = fechaInicio;
    this.fechaFinal = fechaFinal;
    this.horario = horario;
  }
  /**
   * @return the nombre
   */
  public String getNombre() {
    return nombre;
  }
  /**
   * @param nombre the nombre to set
   */
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }
  /**
   * @return the unidad
   */
  public String getUnidad() {
    return unidad;
  }
  /**
   * @param unidad the unidad to set
   */
  public void setUnidad(String unidad) {
    this.unidad = unidad;
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
   * @return the fechaFinal
   */
  public String getFechaFinal() {
    return fechaFinal;
  }
  /**
   * @param fechaFinal the fechaFinal to set
   */
  public void setFechaFinal(String fechaFinal) {
    this.fechaFinal = fechaFinal;
  }
  /**
   * @return the horario
   */
  public String getHorario() {
    return horario;
  }
  /**
   * @param horario the horario to set
   */
  public void setHorario(String horario) {
    this.horario = horario;
  }

}
