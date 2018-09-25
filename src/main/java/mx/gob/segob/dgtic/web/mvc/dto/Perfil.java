package mx.gob.segob.dgtic.web.mvc.dto;

public class Perfil {
	private String clavePerfil;
	private String descripcion;
	private String estatus;

public Perfil (){
	
}

public Perfil(String clavePerfil, String descripcion, String estatus){
	super();
	this.clavePerfil=clavePerfil;
	this.descripcion=descripcion;
	this.estatus=estatus;
}

public String getClavePerfil() {
	return clavePerfil;
}

public void setClavePerfil(String clavePerfil) {
	this.clavePerfil = clavePerfil;
}

public String getDescripcion() {
	return descripcion;
}

public void setDescripcion(String descripcion) {
	this.descripcion = descripcion;
}

public String getEstatus() {
	return estatus;
}

public void setEstatus(String estatus) {
	this.estatus = estatus;
}
}