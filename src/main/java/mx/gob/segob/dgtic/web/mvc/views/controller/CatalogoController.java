/*
* ****************************************************
* * Aplicacion Base
* * Version 1.0.0
* * Secretaria de Gobernacion - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.mvc.views.controller;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.gob.segob.dgtic.web.mvc.dto.DiaFestivo;
import mx.gob.segob.dgtic.web.mvc.dto.Horario;
import mx.gob.segob.dgtic.web.mvc.dto.JustificacionDto;
import mx.gob.segob.dgtic.web.mvc.dto.NivelOrganizacional;
import mx.gob.segob.dgtic.web.mvc.dto.Periodo;
import mx.gob.segob.dgtic.web.mvc.dto.TipoDia;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;
import mx.gob.segob.dgtic.web.mvc.service.CatalogoService;
import mx.gob.segob.dgtic.web.mvc.service.PerfilUsuarioService;
import mx.gob.segob.dgtic.web.mvc.service.UnidadAdministrativaService;
import mx.gob.segob.dgtic.web.mvc.service.UsuarioService;
import mx.gob.segob.dgtic.web.mvc.views.controller.constants.ConstantsController;

/**
 * Controller donde se registran las vistas del aplicativo
 * 
 * Path : {contextoAplicacion}/
 */
@Controller
@RequestMapping(value = "catalogos")
public class CatalogoController {

	@Autowired
	private CatalogoService catalogoService;

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired 
	private UnidadAdministrativaService unidadAdministrativaService;
	
	@Autowired 
	private PerfilUsuarioService perfilUsuarioService;
	
	private String mensajes = "";
	
	private static final Logger logger = LoggerFactory.getLogger(CatalogoController.class);
	private static final String MENSAJE = "MENSAJE";
	private static final String MENSAJE_EXCEPTION = "MENSAJE_EXCEPCION";
	private static final String REDIRECT_CATALOGOS_HORARIO = "redirect:/catalogos/horario";
	private static final String REDIRECT_CATALOGOS_JUSTIFICACION = "redirect:/catalogos/justificacion";
	private static final String REDIRECT_CATALOGOS_PERIODO = "redirect:/catalogos/periodo";
	private static final String DD_MM_YYYY = "dd/MM/yyyy";
	private static final String YYYY_MM_DD = "yyyy-MM-dd";
	/**
	 * Vista donde se ubica el catálogo de horarios. Path :
	 * {contextoAplicacion}/catalogos/horario
	 * 
	 * @return La vista del menú de catálogos
	 */
	@RequestMapping(value = { "horario" }, method = RequestMethod.GET)
	public String obtieneHorarios(Model model, Authentication authentication) {
		model.addAttribute(ConstantsController.LISTA_HORARIOS, catalogoService.obtieneHorariosCat(authentication));
		if(!this.mensajes.equals("")){
			if(this.mensajes.contains(ConstantsController.CORRECTAMENTE))
				model.addAttribute(MENSAJE, this.mensajes);
			else
				model.addAttribute(MENSAJE_EXCEPTION, this.mensajes);
		}
		this.mensajes = "";
		return "/catalogos/horario";
	}

	@GetMapping("horario/busca")
	@ResponseBody
	public Horario buscaHorario(Integer id, Authentication authentication) {
		return catalogoService.buscaHorario(id, authentication);
	}

	@PostMapping("horario/modifica")
	public String modificaHorario(String nombre, Integer id, String horaEntrada, String horaSalida, Boolean activo, Authentication authentication) {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		long milisegundosHoraEntrada = 0;
		long milisegundosHoraSalida = 0;

		try {
			milisegundosHoraEntrada = sdf.parse(horaEntrada).getTime();
			milisegundosHoraSalida = sdf.parse(horaSalida).getTime();
		} catch (ParseException e) {
			logger.warn("Warn: {} ",e);
		}
		Time horaEntradaSql = new Time(milisegundosHoraEntrada);
		Time horaSalidaSql = new Time(milisegundosHoraSalida);
		Horario horario = new Horario(id, nombre, horaEntradaSql, horaSalidaSql, activo, "");
		horario = catalogoService.modificaHorario(horario, authentication);
		this.mensajes  = horario.getMensaje();
		return REDIRECT_CATALOGOS_HORARIO;
	}

	@PostMapping("horario/agrega")
	public String agregaHorario(Integer id, String nombre, String horaEntrada, String horaSalida, boolean activo, Authentication authentication) {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		long milisegundosHoraEntrada = 0;
		long milisegundosHoraSalida = 0;

		try {
			milisegundosHoraEntrada = sdf.parse(horaEntrada).getTime();
			milisegundosHoraSalida = sdf.parse(horaSalida).getTime();
		} catch (ParseException e) {
			logger.info("Warn. : {} ",e);
		}
		Time horaEntradaServer = new Time(milisegundosHoraEntrada);
		Time horaSalidaServer = new Time(milisegundosHoraSalida);
		Horario horario = new Horario(id, nombre, horaEntradaServer, horaSalidaServer, activo, "");
		horario = catalogoService.agregaHorario(horario, authentication);
		this.mensajes  = horario.getMensaje();
		return REDIRECT_CATALOGOS_HORARIO;
	}

	@GetMapping("horario/elimina")
	public String eliminaHorario(Integer id, Authentication authentication) {

		catalogoService.eliminaHorario(id, authentication);

		return REDIRECT_CATALOGOS_HORARIO;
	}

	/**
	 * Vista donde se ubica el catálogo de TipoDias. Path :
	 * {contextoAplicacion}/catalogos/tipoDia
	 * 
	 * @return La vista del menú de catálogos
	 */
	@RequestMapping(value = { "tipoDia" }, method = RequestMethod.GET)
	public String obtieneTipoDias(Model model, Authentication authentication) {
		model.addAttribute("listaTipoDias", catalogoService.obtieneTipoDias(authentication));
		this.mensajes = "";
		return "/catalogos/tipoDia";
	}

	@GetMapping("tipoDia/busca")
	@ResponseBody
	public TipoDia buscaTipoDia(Integer id, Authentication authentication) {

		return catalogoService.buscaTipoDia(id, authentication);
	}

	
	@RequestMapping(value = { "usuario" }, method = RequestMethod.GET)
	public String obtieneUsuarios(Model model, Authentication authentication) {

		model.addAttribute("listaPerfiles", catalogoService.obtienePerfiles(authentication));
		model.addAttribute(ConstantsController.LISTA_HORARIOS, catalogoService.obtieneHorarios(authentication));
		model.addAttribute("listaUsuarios", usuarioService.obtieneUsuarios(authentication));
		model.addAttribute("listaUnidadAdministrativa", unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
		if(!this.mensajes.equals("")){
			if(this.mensajes.contains(ConstantsController.CORRECTAMENTE))
				model.addAttribute(MENSAJE, this.mensajes);
			else
				model.addAttribute(MENSAJE_EXCEPTION, this.mensajes);
		}
		this.mensajes = "";
		return "/catalogos/usuario";
	}

	@PostMapping("/usuario/modifica")
	public String modificaUsuario( Horario idHorario, String claveUsuario, String nombre,
			String apellidoPaterno, String apellidoMaterno, String estatus, String reiniciarPassword, Integer unidadAdministrativa, 
			String coordinador, String empleado, String director, String administrador, Authentication authentication) {
		logger.info("claveUsuario: {}  ",claveUsuario);
		logger.info("unidadAdministrativa:. {} ",unidadAdministrativa);
		logger.info("coordinador. {} ",coordinador);
		logger.info("empleado : {}",empleado);
		logger.info("director : {}",director);
		logger.info("administrador: {} ",administrador);
		logger.info(" estatus : {}",estatus);
		
		Integer clavePerfil[ ]= new Integer[4]; 
		Integer result = 0;
		
		if(coordinador!=null && !coordinador.trim().isEmpty()){
			clavePerfil[2]=Integer.parseInt(coordinador);
		}
		if(empleado!=null && !empleado.trim().isEmpty()){
			clavePerfil[3]=Integer.parseInt(empleado);
		}
		if(director!=null && !director.trim().isEmpty()){
			clavePerfil[1]=Integer.parseInt(director);
		}
		if(administrador!=null && !administrador.trim().isEmpty()){
			clavePerfil[0]=Integer.parseInt(administrador);
		}
	
		logger.info("valor clavePerfil: {} ",clavePerfil.length);
		for(int i=0; i<clavePerfil.length;i++){
			logger.info("valor clavePerfil posicion: {}  " , clavePerfil[i]);
		}
		
		
		result += perfilUsuarioService.guardaEliminaPerfilesUsuario(clavePerfil, claveUsuario, authentication);
		result += usuarioService.modificaUsuario(
				new Usuario(null, idHorario, claveUsuario, nombre, apellidoPaterno, apellidoMaterno, estatus), authentication);
		result += unidadAdministrativaService.consultaRegistraUsuarioUnidadAdministrativa(unidadAdministrativa, claveUsuario, authentication);
		char dato = reiniciarPassword.charAt(0);
		if(result == 0)
			this.mensajes = "Se han modificado correctamente los datos del usuario.";
		else
			this.mensajes = "Se generó un error al modificar, intenta nuevamente.";
			
		if (((int) dato) == 83) {
			logger.info("valor reiniciarPassword1: {} " ,reiniciarPassword);
			usuarioService.reiniciaContrasenia(claveUsuario, authentication);
		}

		return "redirect:/catalogos/usuario";
	}

	@GetMapping("usuario/busca")
	@ResponseBody
	public Map<String, Object> buscaUsuario(String id, Authentication authentication) {
		HashMap<String, Object> hmap = new HashMap<>();
		hmap.put("usuario", usuarioService.buscaUsuario(id, authentication));
		hmap.put("listaUsuarioPerfiles", perfilUsuarioService.recuperaPerfilesUsuario(id, authentication));
		return hmap;
	}
	

	@GetMapping("usuario/elimina")
	public String eliminaUsuario(String id, Authentication authentication) {
		usuarioService.eliminaUsuario(id, authentication);
		return "redirect:/catalogos/usuario";
	}
	
	/**
	 * Vista donde se ubica el catálogo de horarios. Path :
	 * {contextoAplicacion}/catalogos/horario
	 * 
	 * @return La vista del menú de catálogos
	 */

	@RequestMapping(value = { "justificacion" }, method = RequestMethod.GET)
	public String obtieneJustificaciones(Model model, Authentication authentication) {
		model.addAttribute("listaJustificaciones", catalogoService.obtieneListaJ(authentication));
		if(!this.mensajes.equals("")){
			if(this.mensajes.contains(ConstantsController.CORRECTAMENTE))
				model.addAttribute(MENSAJE, this.mensajes);
			else
				model.addAttribute(MENSAJE_EXCEPTION, this.mensajes);
		}
		this.mensajes = "";
		return "/catalogos/justificacion";
	}
	
	
	@GetMapping("justificacion/busca")
	@ResponseBody
	public JustificacionDto buscaJustificacion(Integer id, Authentication authentication) {
		return catalogoService.buscaJustificacion(id, authentication);
	}

	@PostMapping("justificacion/modifica")
	public String modificaJustificacion(Integer id, String clave, String justificacion, Boolean activo, Authentication authentication) {
		JustificacionDto  justi  = new JustificacionDto(id, clave,justificacion, activo, "");
		justi = catalogoService.modificaJustificacion(justi, authentication);
		this.mensajes = justi.getMensaje();
		return REDIRECT_CATALOGOS_JUSTIFICACION;
	}

	@PostMapping("justificacion/agrega")
	public String agregaJustificacion(Integer id, String clave, String justificacion, boolean activo, Authentication authentication) {
		JustificacionDto  justi  = new JustificacionDto(id, clave,justificacion, activo, "");
		justi = catalogoService.agregaJustificacion(justi, authentication);
		this.mensajes = justi.getMensaje();
		return REDIRECT_CATALOGOS_JUSTIFICACION;
	}

	@GetMapping("justificacion/elimina")
	public String eliminaJustificacion(Integer id, Authentication authentication) {

		catalogoService.eliminaJustificacion(id, authentication);

		return REDIRECT_CATALOGOS_JUSTIFICACION;
	}
	
	       /**
	+        * Vista donde se ubica el catálogo de periodo vacacional. Path :^M
	+        * {contextoAplicacion}/catalogos/periodoVacacional^M
	+        * ^M
	+        * @return La vista del menú de catálogos^M
	+        */
	 @RequestMapping(value = { "periodo/agrega" }, method = RequestMethod.GET)
     public String agregaPeriodoVacacional(Periodo periodo, Authentication authentication) {
		 Periodo p;	
		 p = catalogoService.agregaPeriodoVacacional(periodo, authentication);
	   		this.mensajes = p.getMensaje()== null ? ""  : p.getMensaje();
	   		return REDIRECT_CATALOGOS_PERIODO;
     }
	       
	       /**
	   	 * Vista donde se ubica el catálogo de dias Festivos. Path :
	   	 * {contextoAplicacion}/catalogos/diaFestivo
	   	 * 
	   	 * @return La vista del menú de catálogos
	   	 */
	   	@RequestMapping(value = {"diaFestivo"}, method = RequestMethod.GET)
	   	public String obtieneDiaFestivo(Model model, Authentication authentication) {
	   		model.addAttribute("listaDiasFestivos", catalogoService.obtieneDiaFestivoCat(authentication));
	   		if(!this.mensajes.equals("")){
				if(this.mensajes.contains(ConstantsController.CORRECTAMENTE))
					model.addAttribute(MENSAJE, this.mensajes);
				else
					model.addAttribute(MENSAJE_EXCEPTION, this.mensajes);
			}
	   		this.mensajes = ""; 
	   		return "/catalogos/diaFestivo";
	   	}

	   	@GetMapping("diaFestivo/busca")
	   	@ResponseBody
	   	public DiaFestivo buscaDiaFestivo(Integer id, Authentication authentication) {
	   		logger.info("Hey");
	   		return catalogoService.buscaDiaFestivo(id, authentication);
	   	}

	   	@PostMapping("diaFestivo/modifica")
	   	public String modificaDiaFestivo(Integer id, String nombre, String fecha,  Boolean activo, Authentication authentication) {
	   		
	   		SimpleDateFormat sdff = new SimpleDateFormat(DD_MM_YYYY);
	   		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
	   		Date dia = null;
	   		try {
	   			dia = sdff.parse(fecha);
	   		} catch (ParseException e) {
	   			logger.warn("Error: {}",e);
	   		}
	   		
	   		DiaFestivo diaFest = new DiaFestivo(id, nombre, sdf.format(dia), activo, "");
	   		diaFest = catalogoService.modificaDiaFestivo(diaFest, authentication);
	   		this.mensajes = diaFest.getMensaje();
	   		return "redirect:/catalogos/diaFestivo";
	   	}

	   	@PostMapping("diaFestivo/agrega")
	   	public String agregaDiaFestivo( String nombre, String fecha,  Boolean activo, Authentication authentication) {
	   		
	   		SimpleDateFormat sdff = new SimpleDateFormat(DD_MM_YYYY);
	   		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
	   		Date dia = null;
	   		try {
	   			dia = sdff.parse(fecha);
	   		} catch (ParseException e) {
	   			logger.info("Warn. {} ",e);
	   		}
	   		
	   		DiaFestivo diaFest = new DiaFestivo(null, nombre, sdf.format(dia), activo, "");
	   		diaFest = catalogoService.agregaDiaFestivo(diaFest, authentication);
	   		this.mensajes = diaFest.getMensaje();
	   		
	   		return "redirect:/catalogos/diaFestivo";
	   	}
	
	   	@RequestMapping(value = { "periodo" }, method = RequestMethod.GET)
		public String periodos(Model model, Authentication authentication) {
			model.addAttribute("listaPeriodos", catalogoService.obtienePeriodosCat(authentication));
			if(!this.mensajes.equals("")){
				if(this.mensajes.contains(ConstantsController.CORRECTAMENTE))
					model.addAttribute(MENSAJE, this.mensajes);
				else
					model.addAttribute(MENSAJE_EXCEPTION, this.mensajes);
			}
			this.mensajes = "";
			return "/catalogos/PeriodoVacacional";
		}
	   	
		@PostMapping("periodo/agrega")
	   	public String periodoAgrega(Periodo periodo, Authentication authentication) {
			SimpleDateFormat sdff = new SimpleDateFormat(DD_MM_YYYY);
	   		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
	   		Date inicio = null;
	   		Date fin = null;
	   		try {
	   			inicio = sdff.parse(periodo.getFechaInicio());
	   			fin = sdff.parse(periodo.getFechaFin());
	   		} catch (ParseException e) {
	   			logger.info("Warn.- {} ",e);
	   		}
	   		periodo.setFechaInicio(sdf.format(inicio));
	   		periodo.setFechaFin(sdf.format(fin));
			periodo = catalogoService.agregaPeriodoVacacional(periodo, authentication);
		   	this.mensajes = periodo.getMensaje()== null ? ""  : periodo.getMensaje();
		   	return REDIRECT_CATALOGOS_PERIODO;
	   	}
	   	
		@GetMapping ("periodo/modifica")
	   	public String periodoModifica(Periodo periodo, Authentication authentication) {
			Periodo p;
			
	   		p = catalogoService.modificaEstatusPeriodo(periodo, authentication);
	   		this.mensajes = p.getMensaje() == null ? ""  : p.getMensaje();
	   		
	   		return REDIRECT_CATALOGOS_PERIODO;
	   	}
		
		@GetMapping("periodo/busca")
		@ResponseBody
		public Periodo periodoBusca(Integer id, Authentication authentication) {
			
			return catalogoService.buscaPeriodo(id, authentication);
		}
		
		@RequestMapping(value = { "nivelOrganizacional" }, method = RequestMethod.GET)
		public String inicioNivel(Model model, Authentication authentication) {
			model.addAttribute("listaNiveles", catalogoService.obtieneNiveles(authentication)); // niveles en c_nivel_organizacional
			model.addAttribute("listaUnidades", catalogoService.nivelesEmpleado(authentication)); // niveles de empleados en m_usuario
			model.addAttribute(ConstantsController.LISTA_HORARIOS, catalogoService.obtieneHorarios(authentication)); // horarios en c_horario
			logger.info("CatalogoController--method--inicioNivel: {} ",this.mensajes);
			logger.info("CatalogoController--method--inicioNivel: this.getMensajes {} ",this.getMensaje());
			if(!this.getMensaje().equals("")){
				if(this.mensajes.contains(ConstantsController.CORRECTAMENTE)) {
					model.addAttribute(MENSAJE, this.mensajes);
				}
				else {
					model.addAttribute(MENSAJE_EXCEPTION, this.mensajes);
				}
			}
			this.mensajes="";
			return "/catalogos/NivelOrganizacional";
		}
		
		@PostMapping("nivel/agrega")
	   	public String nivelAgrega(NivelOrganizacional nivel, Authentication authentication) {
			NivelOrganizacional nv;
			nv = catalogoService.nivelAgrega(nivel, authentication);
			logger.info("CatalogoControler-.mensaje: {}",nv.getMensaje());
			this.mensajes = nv.getMensaje()== null ? ""  : nv.getMensaje();
	   		return "redirect:/catalogos/nivelOrganizacional";
	   	}

		public String getMensaje() {
			
			return mensajes == null ? "" : this.mensajes;
		}

		public void setMensaje(String mensaje) {
			this.mensajes = mensaje;
		}
		
		@GetMapping("nivel/busca")
		@ResponseBody
		public NivelOrganizacional nivelBusca(Integer id, Authentication authentication) {
			logger.info("id a consultar: {} ",id);
			 return catalogoService.nivelBusca(id, authentication);
		}
		
		@PostMapping ("nivel/modifica")
	   	public String nivelModifica(NivelOrganizacional nivel, Authentication authentication) {
			NivelOrganizacional nv;
	   		nv = catalogoService.modificaNivel(nivel, authentication);
	   		this.mensajes = nv.getMensaje() == null ? "" :nv.getMensaje();
	   		logger.info("this.mensajes: {} ",this.mensajes);
	   		return "redirect:/catalogos/nivelOrganizacional";
	   	}
}