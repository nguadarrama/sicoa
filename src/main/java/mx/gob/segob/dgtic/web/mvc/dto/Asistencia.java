package mx.gob.segob.dgtic.web.mvc.dto;

import java.sql.Timestamp;
import java.util.Date;

public class Asistencia {
	private Integer idAsistencia;
	private Usuario usuarioDto;
	private Timestamp entrada;
	private Timestamp salida;
	private TipoDia idTipoDia;
	private Estatus idEstatus;
	private Incidencia incidencia;
	
	public Asistencia() {
		
	}
	
	public Integer getIdAsistencia() {
		return idAsistencia;
	}

	public void setIdAsistencia(Integer idAsistencia) {
		this.idAsistencia = idAsistencia;
	}

	public Usuario getUsuario() {
		return usuarioDto;
	}

	public void setUsuario(Usuario usuarioDto) {
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

	public Usuario getUsuarioDto() {
		return usuarioDto;
	}

	public void setUsuarioDto(Usuario usuarioDto) {
		this.usuarioDto = usuarioDto;
	}

	public TipoDia getIdTipoDia() {
		return idTipoDia;
	}

	public void setIdTipoDia(TipoDia idTipoDia) {
		this.idTipoDia = idTipoDia;
	}

	public Estatus getIdEstatus() {
		return idEstatus;
	}

	public void setIdEstatus(Estatus idEstatus) {
		this.idEstatus = idEstatus;
	}

	public Incidencia getIncidencia() {
		return incidencia;
	}

	public void setIncidencia(Incidencia incidencia) {
		this.incidencia = incidencia;
	}
	
}
