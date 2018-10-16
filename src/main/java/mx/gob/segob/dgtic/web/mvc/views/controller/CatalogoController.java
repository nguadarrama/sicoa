/*
* ****************************************************
* * Aplicacion Base
* * Version 1.0.0
* * Secretaria de Gobernacion - DGTIC
* ****************************************************
*/
package mx.gob.segob.dgtic.web.mvc.views.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import mx.gob.segob.dgtic.web.config.security.service.AutenticacionService;
import mx.gob.segob.dgtic.web.mvc.dto.DiaFestivo;
import mx.gob.segob.dgtic.web.mvc.dto.Horario;
import mx.gob.segob.dgtic.web.mvc.dto.Justificacion;
import mx.gob.segob.dgtic.web.mvc.dto.Perfil;
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
	private AutenticacionService autenticacionService;
	
	@Autowired 
	private UnidadAdministrativaService unidadAdministrativaService;
	
	@Autowired 
	private PerfilUsuarioService perfilUsuarioService;

	/**
	 * Vista donde se ubica el catálogo de horarios. Path :
	 * {contextoAplicacion}/catalogos/horario
	 * 
	 * @return La vista del menú de catálogos
	 */
	@RequestMapping(value = { "horario" }, method = RequestMethod.GET)
	public String obtieneHorarios(Model model) {

		model.addAttribute("listaHorarios", catalogoService.obtieneHorarios());

		return "/catalogos/horario";
	}

	@GetMapping("horario/busca")
	@ResponseBody
	public Horario buscaHorario(Integer id) {

		return catalogoService.buscaHorario(id);
	}

	@PostMapping("horario/modifica")
	public String modificaHorario(Integer id, String horaEntrada, String horaSalida, boolean activo) {
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

		catalogoService.modificaHorario(new Horario(id, SQLhoraEntrada, SQLhoraSalida, activo));

		return "redirect:/catalogos/horario";
	}

	@PostMapping("horario/agrega")
	public String agregaHorario(Integer id, String horaEntrada, String horaSalida, boolean activo) {
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

		catalogoService.agregaHorario(new Horario(id, SQLhoraEntrada, SQLhoraSalida, activo));

		return "redirect:/catalogos/horario";
	}

	@GetMapping("horario/elimina")
	public String eliminaHorario(Integer id) {

		catalogoService.eliminaHorario(id);

		return "redirect:/catalogos/horario";
	}

	/**
	 * Vista donde se ubica el catálogo de TipoDias. Path :
	 * {contextoAplicacion}/catalogos/tipoDia
	 * 
	 * @return La vista del menú de catálogos
	 */
	@RequestMapping(value = { "tipoDia" }, method = RequestMethod.GET)
	public String obtieneTipoDias(Model model) {

		model.addAttribute("listaTipoDias", catalogoService.obtieneTipoDias());

		return "/catalogos/tipoDia";
	}

	@GetMapping("tipoDia/busca")
	@ResponseBody
	public TipoDia buscaTipoDia(Integer id) {

		return catalogoService.buscaTipoDia(id);
	}

	@PostMapping("tipoDia/modifica")
	public String modificaTipoDia(Integer id, String nombre, String observaciones, boolean incidencia) {

		catalogoService.modificaTipoDia(new TipoDia(id, nombre, observaciones, incidencia));

		return "redirect:/catalogos/tipoDia";
	}

	@PostMapping("tipoDia/agrega")
	public String agregaTipoDia(Integer id, String nombre, String observaciones, boolean incidencia) {

		catalogoService.agregaTipoDia(new TipoDia(id, nombre, observaciones, incidencia));

		return "redirect:/catalogos/tipoDia";
	}

	@GetMapping("tipoDia/elimina")
	public String eliminaTipoDia(Integer id) {

		catalogoService.eliminaTipoDia(id);

		return "redirect:/catalogos/tipoDia";
	}

	@RequestMapping(value = { "usuario" }, method = RequestMethod.GET)
	public String obtieneUsuarios(Model model) {

		model.addAttribute("listaPerfiles", catalogoService.obtienePerfiles());
		model.addAttribute("listaHorarios", catalogoService.obtieneHorarios());
		model.addAttribute("listaUsuarios", usuarioService.obtieneUsuarios());
		model.addAttribute("listaUnidadAdministrativa", unidadAdministrativaService.obtenerListaUnidadAdministrativa());

		return "/catalogos/usuario";
	}

	@PostMapping("/usuario/modifica")
	public String modificaUsuario(Integer[] clavePerfil, Horario idHorario, String claveUsuario, String nombre,
			String apellidoPaterno, String apellidoMaterno, String bloqueado, String reiniciarPassword, Integer unidadAdministrativa) {
		System.out.println("unidadAdministrativa "+unidadAdministrativa);
		System.out.println("valor clavePerfil " + clavePerfil.length);
		perfilUsuarioService.guardaEliminaPerfilesUsuario(clavePerfil, claveUsuario);
		
		usuarioService.modificaUsuario(
				new Usuario(null, idHorario, claveUsuario, nombre, apellidoPaterno, apellidoMaterno, bloqueado));
		unidadAdministrativaService.consultaRegistraUsuarioUnidadAdministrativa(unidadAdministrativa, claveUsuario);
		char dato = reiniciarPassword.charAt(0);

		if (((int) dato) == 83) {
			System.out.println("valor reiniciarPassword1 " + reiniciarPassword);
			usuarioService.reiniciaContrasenia(claveUsuario);
		}

		return "redirect:/catalogos/usuario";
	}

	@GetMapping("usuario/busca")
	@ResponseBody
	public HashMap<String, Object> buscaUsuario(String id) {
		//cargaUsuarioPerfil(id, model);
		HashMap<String, Object> hmap = new HashMap<String, Object>();
		hmap.put("usuario", usuarioService.buscaUsuario(id));
		hmap.put("listaUsuarioPerfiles", perfilUsuarioService.recuperaPerfilesUsuario(id));
		//hmap.put("listaPerfiles", catalogoService.obtienePerfiles());
		return hmap;
	}
	

	@GetMapping("usuario/elimina")
	public String eliminaUsuario(String id) {
		usuarioService.eliminaUsuario(id);
		return "redirect:/catalogos/usuario";
	}
	
	/**
	 * Vista donde se ubica el catálogo de horarios. Path :
	 * {contextoAplicacion}/catalogos/horario
	 * 
	 * @return La vista del menú de catálogos
	 */
	@RequestMapping(value = { "justificacion" }, method = RequestMethod.GET)
	public String obtieneJustificacion(Model model) {

		model.addAttribute("listaJustificaciones", catalogoService.obtieneJustificaciones());

		return "/catalogos/justificacion";
	}

	@GetMapping("justificacion/busca")
	@ResponseBody
	public Justificacion buscaJustificacion(Integer id) {

		return catalogoService.buscaJustificacion(id);
	}

	@PostMapping("justificacion/modifica")
	public String modificaJustificacion(Integer id, String justificacion, Boolean activo) {

		catalogoService.modificaJustificacion(new Justificacion(id, justificacion, activo));

		return "redirect:/catalogos/justificacion";
	}

	@PostMapping("justificacion/agrega")
	public String agregaJustificacion(Integer id, String justificacion, boolean activo) {

		catalogoService.agregaJustificacion(new Justificacion(id, justificacion, activo));

		return "redirect:/catalogos/justificacion";
	}

	@GetMapping("justificacion/elimina")
	public String eliminaJustificacion(Integer id) {

		catalogoService.eliminaJustificacion(id);

		return "redirect:/catalogos/justificacion";
	}
	
	       /**
	+        * Vista donde se ubica el catálogo de periodo vacacional. Path :^M
	+        * {contextoAplicacion}/catalogos/periodoVacacional^M
	+        * ^M
	+        * @return La vista del menú de catálogos^M
	+        */
	       @RequestMapping(value = { "periodo/agrega" }, method = RequestMethod.GET)
	       public String agregaPeriodoVacacional(Periodo periodo) {
	
	              catalogoService.agregaPeriodoVacacional(periodo);
	              return "/catalogos/periodoVacacional";
	       }
	       
	       /**
	   	 * Vista donde se ubica el catálogo de dias Festivos. Path :
	   	 * {contextoAplicacion}/catalogos/diaFestivo
	   	 * 
	   	 * @return La vista del menú de catálogos
	   	 */
	   	@RequestMapping(value = {"diaFestivo"}, method = RequestMethod.GET)
	   	public String obtieneDiaFestivo(Model model) {

	   		model.addAttribute("listaDiasFestivos", catalogoService.obtieneDiaFestivo());

	   		return "/catalogos/diaFestivo";
	   	}

	   	@GetMapping("diaFestivo/busca")
	   	@ResponseBody
	   	public DiaFestivo buscaDiaFestivo(Integer id) {
	   		System.out.println("Hey");
	   		return catalogoService.buscaDiaFestivo(id);
	   	}

	   	@PostMapping("diaFestivo/modifica")
	   	public String modificaDiaFestivo(Integer id, String nombre, String fecha,  Boolean activo) {
	   		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	   		Date dia = new Date();
	   		try {
	   			dia = sdf.parse(fecha);
	   		} catch (ParseException e) {
	   			// TODO Auto-generated catch block
	   			e.printStackTrace();
	   		}
	   		catalogoService.modificaDiaFestivo(new DiaFestivo(id, nombre, sdf.format(dia), activo));

	   		return "redirect:/catalogos/diaFestivo";
	   	}

	   	@PostMapping("diaFestivo/agrega")
	   	public String agregaDiaFestivo( String nombre, String fecha,  Boolean activo) {

	   		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	   		Date dia = new Date();
	   		try {
	   			dia = sdf.parse(fecha);
	   		} catch (ParseException e) {
	   			// TODO Auto-generated catch block
	   			e.printStackTrace();
	   		}
	   		
	   		catalogoService.agregaDiaFestivo(new DiaFestivo(null,  nombre, sdf.format(dia), activo));

	   		return "redirect:/catalogos/diaFestivo";
	   	}
	
}