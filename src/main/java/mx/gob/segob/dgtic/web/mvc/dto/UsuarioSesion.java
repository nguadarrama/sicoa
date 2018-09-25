/*
* ****************************************************
* * Aplicaci&oacute;n Base
* * Versi&oacute;n 1.0.0
* * Secretar&iacute;a de Gobernaci&oacute;n - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.mvc.dto;

import java.util.Set;

/**
 * Informaci&oacute;n del usuario en sesi&oacute;n
 */
public class UsuarioSesion {
	
	/** La clave usuario. */
	private String claveUsuario;
	
	/** Los permisos asignados. */
	private Set<String> permisos;
	
	/** Los perfiles asignados. */
	private Set<String> perfiles;
	
	/**Primera vez*/
	
	private String PrimeraVez;
	
	/**
	 * Obtiene la clave de usuario
	 *
	 * @return La clave de usuario
	 */
	public String getClaveUsuario() {
		return claveUsuario;
	}
	
	/**
	 * Asigna la clave de usuario
	 *
	 * @param claveUsuario la clave de usuario
	 */
	public void setClaveUsuario(String claveUsuario) {
		this.claveUsuario = claveUsuario;
	}
	
	/**
	 * Obtiene los permisos asignados al usuario.
	 *
	 * @return los permisos
	 */
	public Set<String> getPermisos() {
		return permisos;
	}
	
	/**
	 * Asigna los permisos asignados al usuario
	 *
	 * @param permisos Los permisos
	 */
	public void setPermisos(Set<String> permisos) {
		this.permisos = permisos;
	}
	
	/**
	 * Obtiene los perfiles asignados al usuario
	 * 
	 * @return los perfiles asignados al usuario
	 */
	public Set<String> getPerfiles() {
		return perfiles;
	}
	
	/**
	 * Asigna los perfiles asignados al usuario
	 *
	 * @param perfiles los perfiles asignados al usuario
	 */
	public void setPerfiles(Set<String> perfiles) {
		this.perfiles = perfiles;
	}
	
	/**
	 * Representaci&oacute;n de objeto de usuario mediante una cadena.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Usuario autenticado : "+claveUsuario;
	}

	public String getPrimeraVez() {
		return PrimeraVez;
	}

	public void setPrimeraVez(String primeraVez) {
		PrimeraVez = primeraVez;
	}
}
