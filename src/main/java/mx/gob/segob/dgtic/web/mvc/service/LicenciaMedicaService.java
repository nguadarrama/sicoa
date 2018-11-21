package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import mx.gob.segob.dgtic.web.mvc.dto.BusquedaDto;
import mx.gob.segob.dgtic.web.mvc.dto.LicenciaMedica;
import mx.gob.segob.dgtic.web.mvc.dto.LicenciaMedicaAux;

public interface LicenciaMedicaService {

	public List<LicenciaMedica> obtenerListaLicenciaMedicaPorFiltros(BusquedaDto busquedaDto, Authentication authentication);
	public List<LicenciaMedica> obtenerListaLicenciaMedicaEmpleados(BusquedaDto busquedaDto, Authentication authentication);
	public LicenciaMedica agregaLicenciaMedica(LicenciaMedicaAux licenciaMedica, String claveUsuario, Authentication authentication);
	public LicenciaMedica buscaLicenciaMedica(Integer idLicencia, Authentication authentication);
	public List<LicenciaMedica> obtenerLicenciasPorUnidad(BusquedaDto busquedaDto, Authentication authentication);
	public LicenciaMedica modificaLicenciaMedica(LicenciaMedicaAux licenciaMedica, String claveUsuario, Authentication authentication);
	public LicenciaMedica buscaDiasLicenciaMedica(String claveUsuario, Authentication authentication);
	public String consultaDiasPorBloquear(String claveUsuario, Authentication authentication);
}
