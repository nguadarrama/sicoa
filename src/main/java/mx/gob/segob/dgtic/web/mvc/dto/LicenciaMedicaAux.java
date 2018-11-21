package mx.gob.segob.dgtic.web.mvc.dto;

public class LicenciaMedicaAux {

	private Integer idLicencia;
	private Integer idUsuario;
	private Integer idResponsable;
	private Integer idArchivo;
	private Integer idEstatus;
	private String fechaInicio;
	private String fechaFin;
	private Integer dias;
	private String padecimiento;
	private String fechaRegistro;
	private String fechaInicioAux;
	private String fechaFinAux;
	
	public LicenciaMedicaAux(){
		/**
		 * 
		 */
	}

	public String getFechaInicioAux() {
		return fechaInicioAux;
	}
	public void setFechaInicioAux(String fechaInicioAux) {
		this.fechaInicioAux = fechaInicioAux;
	}
	public String getFechaFinAux() {
		return fechaFinAux;
	}
	public void setFechaFinAux(String fechaFinAux) {
		this.fechaFinAux = fechaFinAux;
	}
	public Integer getIdLicencia() {
		return idLicencia;
	}
	public void setIdLicencia(Integer idLicencia) {
		this.idLicencia = idLicencia;
	}
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	public Integer getIdResponsable() {
		return idResponsable;
	}
	public void setIdResponsable(Integer idResponsable) {
		this.idResponsable = idResponsable;
	}
	public Integer getIdArchivo() {
		return idArchivo;
	}
	public void setIdArchivo(Integer idArchivo) {
		this.idArchivo = idArchivo;
	}
	public Integer getIdEstatus() {
		return idEstatus;
	}
	public void setIdEstatus(Integer idEstatus) {
		this.idEstatus = idEstatus;
	}
	public String getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public String getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
	public Integer getDias() {
		return dias;
	}
	public void setDias(Integer dias) {
		this.dias = dias;
	}
	public String getPadecimiento() {
		return padecimiento;
	}
	public void setPadecimiento(String padecimiento) {
		this.padecimiento = padecimiento;
	}
	public String getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
}
