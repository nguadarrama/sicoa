/*
* ****************************************************
* * Aplicacion Base
* * Version 1.0.0
* * Secretaria de Gobernacion - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.mvc.views.controller;

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
import mx.gob.segob.dgtic.web.mvc.dto.Justificacion;
import mx.gob.segob.dgtic.web.mvc.dto.NivelOrganizacional;
import mx.gob.segob.dgtic.web.mvc.dto.Periodo;
import mx.gob.segob.dgtic.web.mvc.dto.TipoDia;
import mx.gob.segob.dgtic.web.mvc.dto.Usuario;
import mx.gob.segob.dgtic.web.mvc.service.CatalogoService;
import mx.gob.segob.dgtic.web.mvc.service.PerfilUsuarioService;
import mx.gob.segob.dgtic.web.mvc.service.UnidadAdministrativaService;
import mx.gob.segob.dgtic.web.mvc.service.UsuarioService;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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
	
	private String mensaje = "";

	/**
	 * Vista donde se ubica el catálogo de horarios. Path :
	 * {contextoAplicacion}/catalogos/horario
	 * 
	 * @return La vista del menú de catálogos
	 */
	@RequestMapping(value = { "horario" }, method = RequestMethod.GET)
	public String obtieneHorarios(Model model, Authentication authentication) {
		model.addAttribute("listaHorarios", catalogoService.obtieneHorariosCat(authentication));
		if(!this.mensaje.equals("")){
			if(this.mensaje.contains("correctamente"))
				model.addAttribute("MENSAJE", this.mensaje);
			else
				model.addAttribute("MENSAJE_EXCEPCION", this.mensaje);
		}
		this.mensaje = "";
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Time SQLhoraEntrada = new Time(milisegundosHoraEntrada);
		Time SQLhoraSalida = new Time(milisegundosHoraSalida);
		Horario horario = new Horario(id, nombre, SQLhoraEntrada, SQLhoraSalida, activo, "");
		horario = catalogoService.modificaHorario(horario, authentication);
		this.mensaje  = horario.getMensaje();
		return "redirect:/catalogos/horario";
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Time SQLhoraEntrada = new Time(milisegundosHoraEntrada);
		Time SQLhoraSalida = new Time(milisegundosHoraSalida);
		Horario horario = new Horario(id, nombre, SQLhoraEntrada, SQLhoraSalida, activo, "");
		horario = catalogoService.agregaHorario(horario, authentication);
		this.mensaje  = horario.getMensaje();
		return "redirect:/catalogos/horario";
	}

	@GetMapping("horario/elimina")
	public String eliminaHorario(Integer id, Authentication authentication) {

		catalogoService.eliminaHorario(id, authentication);

		return "redirect:/catalogos/horario";
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
		this.mensaje = "";
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
		model.addAttribute("listaHorarios", catalogoService.obtieneHorarios(authentication));
		model.addAttribute("listaUsuarios", usuarioService.obtieneUsuarios(authentication));
		model.addAttribute("listaUnidadAdministrativa", unidadAdministrativaService.obtenerUnidadesAdministrativas(authentication));
		if(!this.mensaje.equals("")){
			if(this.mensaje.contains("correctamente"))
				model.addAttribute("MENSAJE", this.mensaje);
			else
				model.addAttribute("MENSAJE_EXCEPCION", this.mensaje);
		}
		this.mensaje = "";
		return "/catalogos/usuario";
	}

	@PostMapping("/usuario/modifica")
	public String modificaUsuario( Horario idHorario, String claveUsuario, String nombre,
			String apellidoPaterno, String apellidoMaterno, String estatus, String reiniciarPassword, Integer unidadAdministrativa, 
			String coordinador, String empleado, String director, String administrador, Authentication authentication) {
		System.out.println("claveUsuario "+claveUsuario+"unidadAdministrativa "+unidadAdministrativa +" coordinador "+coordinador+" empleado "+empleado+" director "+director+" administrador "+administrador
				+" estatus "+estatus);
		
		Integer clavePerfil[]= new Integer[4]; 
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
	
		System.out.println("valor clavePerfil " + clavePerfil.length);
		for(int i=0; i<clavePerfil.length;i++){
			System.out.println("valor clavePerfil posicion "+i+" " + clavePerfil[i]);
		}
		
		
		result += perfilUsuarioService.guardaEliminaPerfilesUsuario(clavePerfil, claveUsuario, authentication);
		result += usuarioService.modificaUsuario(
				new Usuario(null, idHorario, claveUsuario, nombre, apellidoPaterno, apellidoMaterno, estatus), authentication);
		result += unidadAdministrativaService.consultaRegistraUsuarioUnidadAdministrativa(unidadAdministrativa, claveUsuario, authentication);
		char dato = reiniciarPassword.charAt(0);
		if(result == 0)
			this.mensaje = "Se han modificado correctamente los datos del usuario.";
		else
			this.mensaje = "Se generó un error al modificar, intenta nuevamente.";
			
		if (((int) dato) == 83) {
			System.out.println("valor reiniciarPassword1 " + reiniciarPassword);
			usuarioService.reiniciaContrasenia(claveUsuario, authentication);
		}

		return "redirect:/catalogos/usuario";
	}

	@GetMapping("usuario/busca")
	@ResponseBody
	public HashMap<String, Object> buscaUsuario(String id, Authentication authentication) {
		//cargaUsuarioPerfil(id, model);
		HashMap<String, Object> hmap = new HashMap<String, Object>();
		hmap.put("usuario", usuarioService.buscaUsuario(id, authentication));
		hmap.put("listaUsuarioPerfiles", perfilUsuarioService.recuperaPerfilesUsuario(id, authentication));
		//hmap.put("listaPerfiles", catalogoService.obtienePerfiles());
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
		if(!this.mensaje.equals("")){
			if(this.mensaje.contains("correctamente"))
				model.addAttribute("MENSAJE", this.mensaje);
			else
				model.addAttribute("MENSAJE_EXCEPCION", this.mensaje);
		}
		this.mensaje = "";
		return "/catalogos/justificacion";
	}
	
	
	@GetMapping("justificacion/busca")
	@ResponseBody
	public Justificacion buscaJustificacion(Integer id, Authentication authentication) {
		return catalogoService.buscaJustificacion(id, authentication);
	}

	@PostMapping("justificacion/modifica")
	public String modificaJustificacion(Integer id, String clave, String justificacion, Boolean activo, Authentication authentication) {
		Justificacion  justi  = new Justificacion(id, clave,justificacion, activo, "");
		justi = catalogoService.modificaJustificacion(justi, authentication);
		this.mensaje = justi.getMensaje();
		return "redirect:/catalogos/justificacion";
	}

	@PostMapping("justificacion/agrega")
	public String agregaJustificacion(Integer id, String clave, String justificacion, boolean activo, Authentication authentication) {
		Justificacion  justi  = new Justificacion(id, clave,justificacion, activo, "");
		justi = catalogoService.agregaJustificacion(justi, authentication);
		this.mensaje = justi.getMensaje();
		return "redirect:/catalogos/justificacion";
	}

	@GetMapping("justificacion/elimina")
	public String eliminaJustificacion(Integer id, Authentication authentication) {

		catalogoService.eliminaJustificacion(id, authentication);

		return "redirect:/catalogos/justificacion";
	}
	
	       /**
	+        * Vista donde se ubica el catálogo de periodo vacacional. Path :^M
	+        * {contextoAplicacion}/catalogos/periodoVacacional^M
	+        * ^M
	+        * @return La vista del menú de catálogos^M
	+        */
	 @RequestMapping(value = { "periodo/agrega" }, method = RequestMethod.GET)
     public String agregaPeriodoVacacional(Periodo periodo, Authentication authentication) {
		 Periodo p = new Periodo();	
		 p = catalogoService.agregaPeriodoVacacional(periodo, authentication);
	   		this.mensaje = p.getMensaje()== null ? ""  : p.getMensaje();
	   		return "redirect:/catalogos/periodo";
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
	   		if(!this.mensaje.equals("")){
				if(this.mensaje.contains("correctamente"))
					model.addAttribute("MENSAJE", this.mensaje);
				else
					model.addAttribute("MENSAJE_EXCEPCION", this.mensaje);
			}
	   		this.mensaje = ""; 
	   		return "/catalogos/diaFestivo";
	   	}

	   	@GetMapping("diaFestivo/busca")
	   	@ResponseBody
	   	public DiaFestivo buscaDiaFestivo(Integer id, Authentication authentication) {
	   		System.out.println("Hey");
	   		return catalogoService.buscaDiaFestivo(id, authentication);
	   	}

	   	@PostMapping("diaFestivo/modifica")
	   	public String modificaDiaFestivo(Integer id, String nombre, String fecha,  Boolean activo, Authentication authentication) {
	   		System.out.println("Datos nombre "+nombre+" fecha "+fecha+" activo "+activo);
	   		SimpleDateFormat sdff = new SimpleDateFormat("dd/MM/yyyy");
	   		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	   		Date dia = null;
	   		try {
	   			dia = sdff.parse(fecha);
	   		} catch (ParseException e) {
	   			// TODO Auto-generated catch block
	   			e.printStackTrace();
	   		}
	   		
	   		DiaFestivo diaFest = new DiaFestivo(id, nombre, sdf.format(dia), activo, "");
	   		diaFest = catalogoService.modificaDiaFestivo(diaFest, authentication);
	   		this.mensaje = diaFest.getMensaje();
	   		return "redirect:/catalogos/diaFestivo";
	   	}

	   	@PostMapping("diaFestivo/agrega")
	   	public String agregaDiaFestivo( String nombre, String fecha,  Boolean activo, Authentication authentication) {
	   		System.out.println("Datos nombre "+nombre+" fecha "+fecha+" activo "+activo);
	   		SimpleDateFormat sdff = new SimpleDateFormat("dd/MM/yyyy");
	   		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	   		Date dia = null;
	   		try {
	   			dia = sdff.parse(fecha);
	   		} catch (ParseException e) {
	   			// TODO Auto-generated catch block
	   			e.printStackTrace();
	   		}
	   		
	   		DiaFestivo diaFest = new DiaFestivo(null, nombre, sdf.format(dia), activo, "");
	   		diaFest = catalogoService.agregaDiaFestivo(diaFest, authentication);
	   		this.mensaje = diaFest.getMensaje();
	   		
	   		return "redirect:/catalogos/diaFestivo";
	   	}
	
	   	@RequestMapping(value = { "periodo" }, method = RequestMethod.GET)
		public String periodos(Model model, Authentication authentication) {
			model.addAttribute("listaPeriodos", catalogoService.obtienePeriodosCat(authentication));
			if(!this.mensaje.equals("")){
				if(this.mensaje.contains("correctamente"))
					model.addAttribute("MENSAJE", this.mensaje);
				else
					model.addAttribute("MENSAJE_EXCEPCION", this.mensaje);
			}
			this.mensaje = "";
			return "/catalogos/PeriodoVacacional";
		}
	   	
		@PostMapping("periodo/agrega")
	   	public String periodoAgrega(Periodo periodo, Authentication authentication) {
			SimpleDateFormat sdff = new SimpleDateFormat("dd/MM/yyyy");
	   		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	   		Date inicio = null;
	   		Date fin = null;
	   		try {
	   			inicio = sdff.parse(periodo.getFechaInicio());
	   			fin = sdff.parse(periodo.getFechaFin());
	   		} catch (ParseException e) {
	   			// TODO Auto-generated catch block
	   			e.printStackTrace();
	   		}
	   		periodo.setFechaInicio(sdf.format(inicio));
	   		periodo.setFechaFin(sdf.format(fin));
			periodo = catalogoService.agregaPeriodoVacacional(periodo, authentication);
		   	this.mensaje = periodo.getMensaje()== null ? ""  : periodo.getMensaje();
		   	return "redirect:/catalogos/periodo";
	   	}
	   	
		@GetMapping ("periodo/modifica")
	   	public String periodoModifica(Periodo periodo, Authentication authentication) {
			Periodo p = new Periodo();
			System.out.println("idRecibido: "+periodo.getIdPeriodo()+" activoRecibido: "+periodo.getActivo());
	   		p = catalogoService.modificaEstatusPeriodo(periodo, authentication);
	   		this.mensaje = p.getMensaje() == null ? ""  : p.getMensaje();
	   		System.out.println("this.mensaje "+this.mensaje);
	   		return "redirect:/catalogos/periodo";
	   	}
		
		@GetMapping("periodo/busca")
		@ResponseBody
		public Periodo periodoBusca(Integer id, Authentication authentication) {
			System.out.println("Id a buscar: "+id);
			return catalogoService.buscaPeriodo(id, authentication);
		}
		
		@RequestMapping(value = { "nivelOrganizacional" }, method = RequestMethod.GET)
		public String inicioNivel(Model model, Authentication authentication) {
			model.addAttribute("listaNiveles", catalogoService.obtieneNiveles(authentication)); // niveles en c_nivel_organizacional
			model.addAttribute("listaUnidades", catalogoService.nivelesEmpleado(authentication)); // niveles de empleados en m_usuario
			model.addAttribute("listaHorarios", catalogoService.obtieneHorarios(authentication)); // horarios en c_horario
			if(!this.getMensaje().equals("")){
				if(this.mensaje.contains("correctamente"))
					model.addAttribute("MENSAJE", this.mensaje);
				else
					model.addAttribute("MENSAJE_EXCEPCION", this.mensaje);
			}
			this.mensaje="";
			return "/catalogos/NivelOrganizacional";
		}
		
		@PostMapping("nivel/agrega")
	   	public String nivelAgrega(NivelOrganizacional nivel, Authentication authentication) {
			NivelOrganizacional nv = new NivelOrganizacional();
			nv = catalogoService.nivelAgrega(nivel, authentication);
			System.out.println("CatalogoControler-.mensaje: "+nv.getMensaje());
			this.mensaje = nv.getMensaje()== null ? ""  : nv.getMensaje();
	   		return "redirect:/catalogos/nivelOrganizacional";
	   	}

		public String getMensaje() {
			
			return mensaje == null ? "" : this.mensaje;
		}

		public void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}
		
		@GetMapping("nivel/busca")
		@ResponseBody
		public NivelOrganizacional nivelBusca(Integer id, Authentication authentication) {
			System.out.println("id a consultar: "+id);
			 return catalogoService.nivelBusca(id, authentication);
		}
		
		@PostMapping ("nivel/modifica")
	   	public String nivelModifica(NivelOrganizacional nivel, Authentication authentication) {
			NivelOrganizacional nv = new NivelOrganizacional();
	   		nv = catalogoService.modificaNivel(nivel, authentication);
	   		this.mensaje = nv.getMensaje() == null ? "" :nv.getMensaje();
	   		System.out.println("this.mensaje: "+this.mensaje);
	   		return "redirect:/catalogos/nivelOrganizacional";
	   	}
}