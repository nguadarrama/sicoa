package mx.gob.segob.dgtic.web.mvc.service;

import org.springframework.web.multipart.MultipartFile;

import mx.gob.segob.dgtic.web.mvc.dto.Archivo;


public interface ArchivoService {
	public Integer guardaArchivo(MultipartFile archivo, String claveUsuario, String accion);
	public void actualizaArchivo(MultipartFile archivo, String claveUsuario, String accion, Integer idArchivo);
	public Archivo consultaArchivo(Integer idArchivo);
}
