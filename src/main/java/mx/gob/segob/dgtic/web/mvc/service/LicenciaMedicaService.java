package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import mx.gob.segob.dgtic.web.mvc.dto.LicenciaMedica;
import mx.gob.segob.dgtic.web.mvc.dto.LicenciaMedicaAux;

public interface LicenciaMedicaService {

	public List<LicenciaMedica> obtenerListaLicenciaMedicaPorFiltros(String claveUsuario, String fechaInicio,
			String fechaFin, String idEstatus, Authentication authentication);
	public List<LicenciaMedica> obtenerListaLicenciaMedicaEmpleados(String claveUsuario ,String nombre,String apellidoPaterno, String apellidoMaterno, 
			String idEstatus,String idUnidad, Authentication authentication);
	public LicenciaMedica AgregaLicenciaMedica(LicenciaMedicaAux licenciaMedica, String claveUsuario, Authentication authentication);
	public LicenciaMedica buscaLicenciaMedica(Integer idLicencia, Authentication authentication);
	public List<LicenciaMedica> obtenerLicenciasPorUnidad(String idUnidad, String claveUsuario, String nombre, String apellidoPaterno, 
			String apellidoMaterno, Authentication authentication);
	public LicenciaMedica modificaLicenciaMedica(LicenciaMedicaAux licenciaMedica, String claveUsuario, Authentication authentication);
	public LicenciaMedica buscaDiasLicenciaMedica(String claveUsuario, Authentication authentication);
	public String consultaDiasPorBloquear(String claveUsuario, Authentication authentication);
}
