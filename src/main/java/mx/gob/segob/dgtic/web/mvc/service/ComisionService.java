package mx.gob.segob.dgtic.web.mvc.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import mx.gob.segob.dgtic.web.mvc.dto.Comision;
import mx.gob.segob.dgtic.web.mvc.dto.ComisionAux;
import mx.gob.segob.dgtic.web.mvc.dto.GeneraReporteArchivo;
import mx.gob.segob.dgtic.web.mvc.dto.GenerarReporteArchivoComision;
import mx.gob.segob.dgtic.web.mvc.dto.LicenciaMedica;
import mx.gob.segob.dgtic.web.mvc.dto.LicenciaMedicaAux;
import mx.gob.segob.dgtic.web.mvc.dto.Vacaciones;
import mx.gob.segob.dgtic.web.mvc.dto.reporte;

public interface ComisionService {

  public List<Comision> obtenerListaComisionesPorFiltros(String claveUsuario, String fechaInicio,
      String fechaFin, String idEstatus, Authentication authentication);

  public List<Comision> obtenerListaComisionesPorFiltrosEmpleados(String claveUsuario,
      String nombre, String apellidoPaterno, String apellidoMaterno, String idUnidad,
      String idEstatus, Authentication authentication);
  
  public List<Comision> obtenerComisionesPorUnidad(String idUnidad, String claveUsuario, String nombre, String apellidoPaterno, 
      String apellidoMaterno, Authentication authentication);

  public Comision obtieneComision(String idComision, Authentication authentication);

  public Comision agregarComision(ComisionAux comision, String claveUsuario, Authentication authentication);

  public Comision modificaComisiones(ComisionAux comision, String claveUsuario, Authentication authentication);
  
  public Comision modificaComisionEstatusArchivo(ComisionAux comisionAux, String claveUsuario, Authentication authentication);

  public void eliminaComisiones(String idComision, Authentication authentication);
  
  public reporte generarReporte(GenerarReporteArchivoComision generaReporteArchivo, Authentication authentication);

}
