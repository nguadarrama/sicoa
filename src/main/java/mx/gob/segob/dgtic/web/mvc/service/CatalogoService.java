package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import mx.gob.segob.dgtic.web.mvc.dto.Perfil;
import mx.gob.segob.dgtic.web.mvc.dto.Periodo;
import mx.gob.segob.dgtic.web.mvc.dto.DiaFestivo;
import mx.gob.segob.dgtic.web.mvc.dto.Horario;
import mx.gob.segob.dgtic.web.mvc.dto.JustificacionDto;
import mx.gob.segob.dgtic.web.mvc.dto.NivelOrganizacional;
import mx.gob.segob.dgtic.web.mvc.dto.TipoDia;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;

public interface CatalogoService {
	
	//Catálogo horario
	public List<Horario> obtieneHorarios(Authentication authentication);
	public Horario modificaHorario(Horario horario, Authentication authentication);
	public Horario agregaHorario(Horario horario, Authentication authentication);
	public Horario guardaHorario(Horario horario, Authentication authentication);
	public void borraHorario(Horario horario, Authentication authentication);
	public Horario buscaHorario(Integer id, Authentication authentication);
	public void eliminaHorario(Integer id, Authentication authentication);
	public List<Perfil> obtienePerfiles(Authentication authentication);
	public List<Horario> obtieneHorariosCat(Authentication authentication);
	//Catálogo tipoDia
	
	public List<TipoDia> obtieneTipoDias(Authentication authentication);
	public TipoDia buscaTipoDia(Integer id, Authentication authentication);
	
	//Catálogo justificacion
	public List<JustificacionDto> obtieneListaJ(Authentication authentication);
	public List<JustificacionDto> obtieneJustificaciones(Authentication authentication);
	public JustificacionDto modificaJustificacion(JustificacionDto justificacion, Authentication authentication);
	public JustificacionDto agregaJustificacion(JustificacionDto justificacion, Authentication authentication);
	public JustificacionDto buscaJustificacion(Integer id, Authentication authentication);
	public void eliminaJustificacion(Integer id, Authentication authentication);
	//demás catálogos
	
	// Catálogo de periodo vacacional
	public Periodo agregaPeriodoVacacional (Periodo periodo, Authentication authentication);
	public void modificaPeriodoVacacional(Periodo periodo, Authentication authentication);
	public List<Periodo> obtienePeriodos (Authentication authentication);
	public Periodo buscaPeriodo(Integer id, Authentication authentication);
	public Periodo modificaEstatusPeriodo(Periodo periodo, Authentication authentication);
	public List<Periodo> obtienePeriodosCat(Authentication authentication);
	
	//Catálogo Día Festivo
	public DiaFestivo agregaDiaFestivo(DiaFestivo dia, Authentication authentication);
	public DiaFestivo modificaDiaFestivo(DiaFestivo dia, Authentication authentication);
	public List<DiaFestivo> obtieneDiaFestivo(Authentication authentication);
	public DiaFestivo buscaDiaFestivo(Integer id, Authentication authentication);
	public List<DiaFestivo> obtieneDiaFestivoCat(Authentication authentication);
	
	// Catálogo Niveles
	public List<Usuario> nivelesEmpleado(Authentication authentication);
	public List<NivelOrganizacional> obtieneNiveles(Authentication authentication);
	public NivelOrganizacional nivelAgrega(NivelOrganizacional nivel, Authentication authentication);
	public NivelOrganizacional nivelBusca(Integer idNivel, Authentication authentication);
	public NivelOrganizacional modificaNivel(NivelOrganizacional nivel, Authentication authentication);
	public String obtieneDiaFestivoParaBloquear(Authentication authentication);
	
}
