package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;
import mx.gob.segob.dgtic.web.mvc.dto.Perfil;
import mx.gob.segob.dgtic.web.mvc.dto.Periodo;
import mx.gob.segob.dgtic.web.mvc.dto.DiaFestivo;
import mx.gob.segob.dgtic.web.mvc.dto.Horario;
import mx.gob.segob.dgtic.web.mvc.dto.Justificacion;
import mx.gob.segob.dgtic.web.mvc.dto.TipoDia;

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
	public Periodo modificaPeriodoVacacional (Periodo periodo);
	
	//Catálogo Día Festivo
	public DiaFestivo agregaDiaFestivo(DiaFestivo dia);
	public DiaFestivo modificaDiaFestivo(DiaFestivo dia);
	public List<DiaFestivo> obtieneDiaFestivo();
	public DiaFestivo buscaDiaFestivo(Integer id);
}
