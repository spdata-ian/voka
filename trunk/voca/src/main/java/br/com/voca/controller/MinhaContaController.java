package br.com.voca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.voca.model.Categorias;
import br.com.voca.model.Idiomas;
import br.com.voca.model.Usuario;
import br.com.voca.repository.CategoriaRepo;
import br.com.voca.repository.IdiomaUsuarioRepo;
import br.com.voca.services.UsuarioService;

@Controller
@RequestMapping("/minhaConta")
public class MinhaContaController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private IdiomaUsuarioRepo idiomaUsuarioRepo;

	@Autowired
	private CategoriaRepo categoriaRepo;

	@RequestMapping
	public ModelAndView minhaContra() {
		final ModelAndView mv = new ModelAndView("user/minhaConta");
		return mv;
	}

	@RequestMapping(value = "/alterarSenha", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void alterarSenha(@RequestParam("novaSenha") final String novaSenha) {
		usuarioService.alterarSenha(getUsuario(), novaSenha);
	}

	@RequestMapping(value = "/alterar", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean alterar(final Usuario usuario) {
		return usuarioService.alterarDados(getUsuario(), usuario);
	}

	@RequestMapping(value = "/remover", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void remover() {
		usuarioService.removerConta(getUsuario());
	}

	// @RequestMapping(value = "/enviar", method = RequestMethod.GET, produces =
	// "application/json")
	// @ResponseBody
	// public void enviarEmail() {
	// emailService.sendSimpleMessage("iancesarvidinharego@gmail.com", "Teste",
	// "Enviando E-mail");
	// }
	//
	// @RequestMapping("/mailTemplate")
	// public ModelAndView template() {
	// final ModelAndView mv = new ModelAndView("basic");
	// mv.addObject("titulo", "Teste");
	// mv.addObject("mensagem", "Customize your template by clicking on the
	// style editor tabs above. Set your fonts, colors, and styles. After
	// setting your styling is all done you can click here in this area, delete
	// the text, and start adding your own awesome content. ");
	// return mv;
	// }

	@ModelAttribute("usuario")
	public Usuario getUsuario() {
		return usuarioService.getUsuarioDefault();
	}

	@ModelAttribute("idiomas")
	public List<Idiomas> getIdiomas() {
		return idiomaUsuarioRepo.findByUsuarioTotalPalavras(getUsuario());
	}

	@ModelAttribute("categorias")
	public List<Categorias> getCategorias() {
		return categoriaRepo.findTotalCategoriasAndPalavrasByUsuario(getUsuario());
	}

}
