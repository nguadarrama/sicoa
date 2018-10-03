package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;
import mx.gob.segob.dgtic.web.mvc.dto.Perfil;
import mx.gob.segob.dgtic.web.mvc.dto.Horario;
import mx.gob.segob.dgtic.web.mvc.dto.Justificacion;
import mx.gob.segob.dgtic.web.mvc.dto.TipoDia;

public interface CatalogoService {
	
	//Catálogo horario
	public List<Horario> obtieneHorarios();
	public void modificaHorario(Horario horario);
	public void agregaHorario(Horario horario);
	public void guardaHorario(Horario horario);
	public void borraHorario(Horario horario);
	public Horario buscaHorario(Integer id);
	public void eliminaHorario(Integer id);
	public List<Perfil> obtienePerfiles();
	//Catálogo tipoDia
	public List<TipoDia> obtieneTipoDias();
	public void modificaTipoDia(TipoDia horario);
	public void agregaTipoDia(TipoDia horario);
	public void guardaTipoDia(TipoDia horario);
	public void borraTipoDia(TipoDia horario);
	public TipoDia buscaTipoDia(Integer id);
	public void eliminaTipoDia(Integer id);
	//Catálogo justificacion
	public List<Justificacion> obtieneJustificaciones();
	public void modificaJustificacion(Justificacion justificacion);
	public void agregaJustificacion(Justificacion justificacion);
	public void guardaJustificacion(Justificacion justificacion);
	public void borraJustificacion(Justificacion justificacion);
	public Justificacion buscaJustificacion(Integer id);
	public void eliminaJustificacion(Integer id);
	//demás catálogos
}
