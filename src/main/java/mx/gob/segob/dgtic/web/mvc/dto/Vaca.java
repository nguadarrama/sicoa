package mx.gob.segob.dgtic.web.mvc.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class Vaca {

		
		private String fechaInicio;
		private String fechaFin;
		private Integer dias;
	    private String name;
	 
	    private String description;
	 
	    private List<MultipartFile> archivo;
		
		public Vaca(){
			
		}
		public Vaca( String fechaInicio, String fechaFin){
			super();
			
			this.fechaInicio=fechaInicio;
			this.fechaFin=fechaFin;
		}
		
		public String getFechaInicio() {
			
			return fechaInicio;
		}
		public void setFechaInicio(String fechaInicio) {
			 
			this.fechaInicio = fechaInicio;
		}
		public String getFechaFin() {
			return fechaFin;
		}
		public void setFechaFin(String fechaFin) {
			this.fechaFin = fechaFin;
		}
		public Integer getDias() {
			return dias;
		}
		public void setDias(Integer dias) {
			this.dias = dias;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public List<MultipartFile> getArchivo() {
			return archivo;
		}
		public void setArchivo(List<MultipartFile> archivo) {
			this.archivo = archivo;
		}
	

}
