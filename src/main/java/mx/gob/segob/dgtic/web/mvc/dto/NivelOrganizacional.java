package mx.gob.segob.dgtic.web.mvc.dto;

public class NivelOrganizacional {
	
	private Integer idNivel;
	private String nivel;
	private Integer idHorario;
	private String horario;
	private String mensaje;
	
	public NivelOrganizacional() {}

	public NivelOrganizacional(Integer idNivel, String nivel, Integer idHorario, String horario, String mensaje) {
		super();
		this.idNivel = idNivel;
		this.nivel = nivel;
		this.idHorario = idHorario;
		this.horario = horario;
		this.mensaje = mensaje;
	}

	public Integer getIdNivel() {
		return idNivel;
	}

	public void setIdNivel(Integer idNivel) {
		this.idNivel = idNivel;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public Integer getIdHorario() {
		return idHorario;
	}

	public void setIdHorario(Integer idHorario) {
		this.idHorario = idHorario;
	}

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
}
