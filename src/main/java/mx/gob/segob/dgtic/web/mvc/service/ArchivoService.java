package mx.gob.segob.dgtic.web.mvc.service;

import org.springframework.web.multipart.MultipartFile;


public interface ArchivoService {
	public Integer guardaArchivo(MultipartFile archivo, String claveUsuario, String accion);
	public void actualizaArchivo(MultipartFile archivo, String claveUsuario, String accion, Integer idArchivo);
}
