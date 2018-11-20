package mx.gob.segob.dgtic.web.mvc.dto;


public class Archivo {
	private Integer idArchivo;
	private String nombre;
	private String url;
	private Integer size;
	private Boolean activo;
	private byte[] archivoByte;
	private String accion;
	private String claveUsuario;
	private String mensaje;
	public Archivo(){
		
	}
	public Archivo(byte[] archivo, String accion, String claveUsuario){
		super();
		this.archivoByte=archivo;
		this.accion=accion;
		this.claveUsuario=claveUsuario;
	}
	
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public Integer getIdArchivo() {
		return idArchivo;
	}
	public void setIdArchivo(Integer idArchivo) {
		this.idArchivo = idArchivo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Boolean getActivo() {
		return activo;
	}
	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
	public byte[] getArchivo() {
		return archivoByte;
	}
	public void setArchivo(byte[] archivo) {
		this.archivoByte = archivo;
	}
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	public String getClaveUsuario() {
		return claveUsuario;
	}
	public void setClaveUsuario(String claveUsuario) {
		this.claveUsuario = claveUsuario;
	}

}
