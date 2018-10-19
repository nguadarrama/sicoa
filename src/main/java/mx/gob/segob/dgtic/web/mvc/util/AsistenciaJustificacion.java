package mx.gob.segob.dgtic.web.mvc.util;

import java.util.List;

import mx.gob.segob.dgtic.web.mvc.dto.Asistencia;
import mx.gob.segob.dgtic.web.mvc.dto.Justificacion;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;

public class AsistenciaJustificacion {
	private Asistencia asistencia;
	private List<Justificacion> listaJustificacion;
	private List<Usuario> listaAutorizador;
	
	public AsistenciaJustificacion() {
		
	}

	public Asistencia getAsistencia() {
		return asistencia;
	}

	public void setAsistencia(Asistencia asistencia) {
		this.asistencia = asistencia;
	}

	public List<Justificacion> getListaJustificacion() {
		return listaJustificacion;
	}

	public void setListaJustificacion(List<Justificacion> listaJustificacion) {
		this.listaJustificacion = listaJustificacion;
	}

	public List<Usuario> getListaAutorizador() {
		return listaAutorizador;
	}

	public void setListaAutorizador(List<Usuario> listaAutorizador) {
		this.listaAutorizador = listaAutorizador;
	}
	
	
	
}
