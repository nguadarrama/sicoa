package mx.gob.segob.dgtic.web.mvc.service;

import org.springframework.web.multipart.MultipartFile;


public interface ArchivoService {
	public void guardaArchivo(MultipartFile archivo, String claveUsuario);
}
