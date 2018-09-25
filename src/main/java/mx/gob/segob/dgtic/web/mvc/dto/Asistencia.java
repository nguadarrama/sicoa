package mx.gob.segob.dgtic.web.mvc.dto;

import java.sql.Timestamp;

public class Asistencia {
	private UsuarioSesion usuarioDto;
	private Timestamp entrada;
	private Timestamp salida;
	
	public Asistencia() {
		
	}

	public UsuarioSesion getUsuarioDto() {
		return usuarioDto;
	}

	public void setUsuarioDto(UsuarioSesion usuarioDto) {
		this.usuarioDto = usuarioDto;
	}

	public Timestamp getEntrada() {
		return entrada;
	}

	public void setEntrada(Timestamp entrada) {
		this.entrada = entrada;
	}

	public Timestamp getSalida() {
		return salida;
	}

	public void setSalida(Timestamp salida) {
		this.salida = salida;
	}
	
}
