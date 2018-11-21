package mx.gob.segob.dgtic.web.mvc.dto;

import java.sql.Timestamp;

public class Asistencia {
	private Integer idAsistencia;
	private Usuario usuarioDto;
	private Timestamp entrada;
	private Timestamp salida;
	private TipoDia idTipoDia;
	private EstatusDto idEstatus;
	private Incidencia incidencia;
	
	public Integer getIdAsistencia() {
		return idAsistencia;
	}

	public void setIdAsistencia(Integer idAsistencia) {
		this.idAsistencia = idAsistencia;
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

	public EstatusDto getIdEstatus() {
		return idEstatus;
	}

	public void setIdEstatus(EstatusDto idEstatus) {
		this.idEstatus = idEstatus;
	}

	public Incidencia getIncidencia() {
		return incidencia;
	}

	public void setIncidencia(Incidencia incidencia) {
		this.incidencia = incidencia;
	}
	
}
