package br.com.voca.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import br.com.voca.model.Idiomas;
import br.com.voca.model.Usuario;
import br.com.voca.repository.IdiomasRepo;
import br.com.voca.services.UsuarioService;

@Controller
public class LoginController {

	@Autowired
	private UsuarioService userService;

	@Autowired
	private IdiomasRepo idiomasRepo;

	@Autowired
	private DashBoardController dashBoardController;

	@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
	public ModelAndView login() {
		final ModelAndView modelAndView = new ModelAndView();
		if (userService.getUsuarioDefault() != null) {
			return dashBoardController.dashBoard();
		} else {
			final Usuario user = new Usuario();
			modelAndView.addObject("user", user);
			modelAndView.setViewName("login");
		}
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public ModelAndView registration() {
		final ModelAndView modelAndView = new ModelAndView();
		final Usuario user = new Usuario();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid final Usuario user, final BindingResult bindingResult) {
		final ModelAndView modelAndView = new ModelAndView();
		final Usuario userExists = userService.findByEmail(user.getEmail());
		boolean erros = false;
		final List<String> mensagens = new ArrayList<>();
		if (StringUtils.isEmpty(user.getEmail())) {
			mensagens.add("Preencha o e-mail");
		} else if (userExists != null) {
			mensagens.add("Já existe um usuário com esse e-mail cadastrado");
		}
		if (StringUtils.isEmpty(user.getNome())) {
			mensagens.add("Preencha o nome");
		}
		if (StringUtils.isEmpty(user.getSenha())) {
			mensagens.add("Preencha a senha");
		}
		if (!mensagens.isEmpty()) {
			erros = true;
			modelAndView.addObject("user", user);
			modelAndView.addObject("erroRegistro", erros);
			modelAndView.addObject("erros", mensagens);
			modelAndView.setViewName("login");
		} else {
			user.setAtivo(true);
			userService.registrarUsuario(user);
			modelAndView.addObject("user", new Usuario());
			modelAndView.addObject("erroRegistro", erros);
			modelAndView.setViewName("login");

		}
		return modelAndView;
	}

	@RequestMapping(value = "/recuperar", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public boolean recuperarSenha(@RequestParam("email") final String email) {
		return userService.recuperarSenha(email);
	}

	@ModelAttribute("idiomas")
	public List<Idiomas> getIdiomas() {
		return idiomasRepo.findAll();
	}

}
