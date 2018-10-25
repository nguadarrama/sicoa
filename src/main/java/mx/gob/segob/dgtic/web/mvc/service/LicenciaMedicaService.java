package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import mx.gob.segob.dgtic.web.mvc.dto.LicenciaMedica;

public interface LicenciaMedicaService {

	public List<LicenciaMedica> obtenerListaLicenciaMedicaPorFiltros(String claveUsuario, String fechaInicio,
			String fechaFin, String idEstatus);
	public List<LicenciaMedica> obtenerListaLicenciaMedicaEmpleados(String claveUsuario ,String nombre,String apellidoPaterno, String apellidoMaterno, 
			String idEstatus,String idUnidad);
}
