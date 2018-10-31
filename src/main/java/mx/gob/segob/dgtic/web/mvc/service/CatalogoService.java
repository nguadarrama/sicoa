package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;
import mx.gob.segob.dgtic.web.mvc.dto.Perfil;
import mx.gob.segob.dgtic.web.mvc.dto.Periodo;
import mx.gob.segob.dgtic.web.mvc.dto.DiaFestivo;
import mx.gob.segob.dgtic.web.mvc.dto.Horario;
import mx.gob.segob.dgtic.web.mvc.dto.Justificacion;
import mx.gob.segob.dgtic.web.mvc.dto.NivelOrganizacional;
import mx.gob.segob.dgtic.web.mvc.dto.TipoDia;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;

public interface CatalogoService {
	
	//Catálogo horario
	public List<Horario> obtieneHorarios();
	public Horario modificaHorario(Horario horario);
	public Horario agregaHorario(Horario horario);
	public Horario guardaHorario(Horario horario);
	public void borraHorario(Horario horario);
	public Horario buscaHorario(Integer id);
	public void eliminaHorario(Integer id);
	public List<Perfil> obtienePerfiles();
	public List<Horario> obtieneHorariosCat();
	//Catálogo tipoDia
	
	public List<TipoDia> obtieneTipoDias();
	public TipoDia buscaTipoDia(Integer id);
	
	//Catálogo justificacion
	public List<Justificacion> obtieneListaJ();
	public List<Justificacion> obtieneJustificaciones();
	public Justificacion modificaJustificacion(Justificacion justificacion);
	public Justificacion agregaJustificacion(Justificacion justificacion);
	public Justificacion buscaJustificacion(Integer id);
	public void eliminaJustificacion(Integer id);
	//demás catálogos
	
	// Catálogo de periodo vacacional
	public Periodo agregaPeriodoVacacional (Periodo periodo);
	public void modificaPeriodoVacacional(Periodo periodo);
	public List<Periodo> obtienePeriodos ();
	public Periodo buscaPeriodo(Integer id);
	public Periodo modificaEstatusPeriodo(Periodo periodo);
	public List<Periodo> obtienePeriodosCat();
	
	//Catálogo Día Festivo
	public DiaFestivo agregaDiaFestivo(DiaFestivo dia);
	public DiaFestivo modificaDiaFestivo(DiaFestivo dia);
	public List<DiaFestivo> obtieneDiaFestivo();
	public DiaFestivo buscaDiaFestivo(Integer id);
	public List<DiaFestivo> obtieneDiaFestivoCat();
	
	// Catálogo Niveles
	public List<Usuario> nivelesEmpleado();
	public List<NivelOrganizacional> obtieneNiveles();
	public NivelOrganizacional nivelAgrega(NivelOrganizacional nivel);
	public NivelOrganizacional nivelBusca(Integer idNivel);
	public NivelOrganizacional modificaNivel(NivelOrganizacional nivel);
	public String obtieneDiaFestivoParaBloquear();
	
	
	
	
}
