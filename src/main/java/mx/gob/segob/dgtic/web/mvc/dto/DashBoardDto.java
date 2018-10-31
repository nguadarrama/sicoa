package mx.gob.segob.dgtic.web.mvc.dto;


import java.util.List;

public class DashBoardDto {
	
		private Integer asistencia;
		 
		private Integer incidencias;
		
		private Integer justificaciones;
		
		private Integer descuentos;
		
		private Integer licencias;
		
		private Integer diasIncapacidad;
		
		private Integer comisionesT;
	 
		private Integer comisiones;
		
		private DashVacDto vacacion;
		
		private DashVacDto vacacion1;
		
		private DashVacDto vacacion2;
		
		private List<DashVacDto> vacaciones;

		/**
		 * 
		 */
		public DashBoardDto() {
			super();
			// TODO Auto-generated constructor stub
		}

		/**
		 * @return the asistencia
		 */
		public String getAsistencia() {
			return String.format("%02d", this.asistencia);
		}

		/**
		 * @param asistencia the asistencia to set
		 */
		public void setAsistencia(Integer asistencia) {
			this.asistencia = asistencia;
		}

		/**
		 * @return the incidencias
		 */
		public String getIncidencias() {
			return String.format("%02d", this.incidencias);
		}

		/**
		 * @param incidencias the incidencias to set
		 */
		public void setIncidencias(Integer incidencias) {
			this.incidencias = incidencias;
		}

		/**
		 * @return the justificaciones
		 */
		public String getJustificaciones() {
			return String.format("%02d", this.justificaciones);
		}

		/**
		 * @param justificaciones the justificaciones to set
		 */
		public void setJustificaciones(Integer justificaciones) {
			this.justificaciones = justificaciones;
		}

		/**
		 * @return the descuentos
		 */
		public String getDescuentos() {
			return String.format("%02d", this.descuentos);
		}

		/**
		 * @param descuentos the descuentos to set
		 */
		public void setDescuentos(Integer descuentos) {
			this.descuentos = descuentos;
		}

		/**
		 * @return the licencias
		 */
		public String getLicencias() {
			return String.format("%02d", this.licencias);
		}

		/**
		 * @param licencias the licencias to set
		 */
		public void setLicencias(Integer licencias) {
			this.licencias = licencias;
		}

		/**
		 * @return the diasIncapacidad
		 */
		public String getDiasIncapacidad() {
			return String.format("%02d", this.diasIncapacidad);
		}

		/**
		 * @param diasIncapacidad the diasIncapacidad to set
		 */
		public void setDiasIncapacidad(Integer diasIncapacidad) {
			this.diasIncapacidad = diasIncapacidad;
		}

		/**
		 * @return the comisionesT
		 */
		public String getComisionesT() {
			return String.format("%02d", this.comisionesT);
		}

		/**
		 * @param comisionesT the comisionesT to set
		 */
		public void setComisionesT(Integer comisionesT) {
			this.comisionesT = comisionesT;
		}

		/**
		 * @return the comisiones
		 */
		public String getComisiones() {
			return String.format("%02d", this.comisiones);
		}

		/**
		 * @param comisiones the comisiones to set
		 */
		public void setComisiones(Integer comisiones) {
			this.comisiones = comisiones;
		}
		

		/**
		 * @return the vacacion
		 */
		public DashVacDto getVacacion() {
			return vacacion;
		}
		

		/**
		 * @return the vacacion1
		 */
		public DashVacDto getVacacion1() {
			return vacacion1;
		}

		/**
		 * @param vacacion1 the vacacion1 to set
		 */
		public void setVacacion1(DashVacDto vacacion1) {
			this.vacacion1 = vacacion1;
		}

		/**
		 * @return the vacacion2
		 */
		public DashVacDto getVacacion2() {
			return vacacion2;
		}

		/**
		 * @param vacacion2 the vacacion2 to set
		 */
		public void setVacacion2(DashVacDto vacacion2) {
			this.vacacion2 = vacacion2;
		}

		/**
		 * @param vacacion the vacacion to set
		 */
		public void setVacacion(DashVacDto vacacion) {
			this.vacacion = vacacion;
		}

		/**
		 * @return the vacaciones
		 */
		public List<DashVacDto> getVacaciones() {
			return vacaciones;
		}

		/**
		 * @param vacaciones the vacaciones to set
		 */
		public void setVacaciones(List<DashVacDto> vacaciones) {
			this.vacaciones = vacaciones;
		}
		
}
