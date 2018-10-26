package mx.gob.segob.dgtic.web.mvc.dto;

public class LicenciaMedica {

	private Integer idLicencia;
	private Usuario idUsuario;
	private Integer idResponsable;
	private Archivo idArchivo;
	private Estatus idEstatus;
	private String fechaInicio;
	public String getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	private String fechaFin;
	private Integer dias;
	private String padecimiento;
	private String fechaRegistro;
	
	public LicenciaMedica(){
		
	}
	public LicenciaMedica(Integer idLicencia, Usuario idUsuario, Integer idResponsable, Archivo idArchivo, Estatus idEstatus, String fechaInicio, 
			String fechaFin, Integer dias, String padecimiento){
		this.idLicencia=idLicencia;
		this.idUsuario=idUsuario;
		this.idResponsable=idResponsable;
		this.idArchivo=idArchivo;
		this.idEstatus=idEstatus;
		this.fechaInicio=fechaInicio;
		this.fechaFin=fechaFin;
		this.dias=dias;
		this.padecimiento=padecimiento;
	}
	public Integer getIdLicencia() {
		return idLicencia;
	}
	public void setIdLicencia(Integer idLicencia) {
		this.idLicencia = idLicencia;
	}
	public Usuario getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}
	public Integer getIdResponsable() {
		return idResponsable;
	}
	public void setIdResponsable(Integer idResponsable) {
		this.idResponsable = idResponsable;
	}
	public Archivo getIdArchivo() {
		return idArchivo;
	}
	public void setIdArchivo(Archivo idArchivo) {
		this.idArchivo = idArchivo;
	}
	public Estatus getIdEstatus() {
		return idEstatus;
	}
	public void setIdEstatus(Estatus idEstatus) {
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
	
}
