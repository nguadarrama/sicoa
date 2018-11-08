package mx.gob.segob.dgtic.web.mvc.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import mx.gob.segob.dgtic.web.mvc.dto.Archivo;


public interface ArchivoService {
	public Archivo guardaArchivo(MultipartFile archivo, String claveUsuario, String accion, String nombreArchivo, Authentication authentication);
	public Archivo actualizaArchivo(MultipartFile archivo, String claveUsuario, String accion, Integer idArchivo, String nombreArchivo, Authentication authentication);
	public Archivo consultaArchivo(Integer idArchivo, Authentication authentication);
}
