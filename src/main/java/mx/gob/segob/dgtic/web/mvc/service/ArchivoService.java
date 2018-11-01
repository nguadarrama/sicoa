package mx.gob.segob.dgtic.web.mvc.service;

import org.springframework.web.multipart.MultipartFile;

import mx.gob.segob.dgtic.web.mvc.dto.Archivo;


public interface ArchivoService {
	public Archivo guardaArchivo(MultipartFile archivo, String claveUsuario, String accion, String nombreArchivo);
	public Archivo actualizaArchivo(MultipartFile archivo, String claveUsuario, String accion, Integer idArchivo, String nombreArchivo);
	public Archivo consultaArchivo(Integer idArchivo);
}
