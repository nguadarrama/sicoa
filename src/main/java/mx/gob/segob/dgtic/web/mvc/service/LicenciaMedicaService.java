package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import mx.gob.segob.dgtic.web.mvc.dto.LicenciaMedica;
import mx.gob.segob.dgtic.web.mvc.dto.LicenciaMedicaAux;

public interface LicenciaMedicaService {

	public List<LicenciaMedica> obtenerListaLicenciaMedicaPorFiltros(String claveUsuario, String fechaInicio,
			String fechaFin, String idEstatus);
	public List<LicenciaMedica> obtenerListaLicenciaMedicaEmpleados(String claveUsuario ,String nombre,String apellidoPaterno, String apellidoMaterno, 
			String idEstatus,String idUnidad);
	public void AgregaLicenciaMedica(LicenciaMedicaAux licenciaMedica, String claveUsuario);
	public LicenciaMedica buscaLicenciaMedica(Integer idLicencia);
	public List<LicenciaMedica> obtenerLicenciasPorUnidad(String idUnidad, String claveUsuario, String nombre, String apellidoPaterno, 
			String apellidoMaterno);
	public void modificaLicenciaMedica(LicenciaMedicaAux licenciaMedica, String claveUsuario);
}
