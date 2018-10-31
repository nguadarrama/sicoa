package mx.gob.segob.dgtic.web.mvc.dto;


public class DashVacDto {
	
		private Integer id_periodo;
		
		private Integer dias;

		private String descripcion;

		/**
		 * 
		 */
		public DashVacDto() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		
		/**
		 * @param id_periodo
		 * @param dias
		 * @param descripcion
		 */
		
		
		public DashVacDto(Integer id_periodo, Integer dias, String descripcion) {
			super();
			this.id_periodo = id_periodo;
			this.dias = dias;
			this.descripcion = descripcion;
		}


		/**
		 * @return the id_periodo
		 */
		public Integer getId_periodo() {
			return id_periodo;
		}


		/**
		 * @param id_periodo the id_periodo to set
		 */
		public void setId_periodo(Integer id_periodo) {
			this.id_periodo = id_periodo;
		}


		/**
		 * @return the dias
		 */
		public Integer getDias() {
			return dias;
		}


		/**
		 * @param dias the dias to set
		 */
		public void setDias(Integer dias) {
			this.dias = dias;
		}


		/**
		 * @return the descripcion
		 */
		public String getDescripcion() {
			return descripcion;
		}


		/**
		 * @param descripcion the descripcion to set
		 */
		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}
	
}
