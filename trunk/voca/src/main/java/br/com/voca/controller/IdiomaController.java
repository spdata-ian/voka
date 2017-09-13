package br.com.voca.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import br.com.voca.model.IdiomaUsuario;
import br.com.voca.model.Idiomas;
import br.com.voca.repository.IdiomaUsuarioRepo;
import br.com.voca.repository.IdiomasRepo;
import br.com.voca.services.IdiomaUsuarioService;
import br.com.voca.services.UsuarioService;

@RequestMapping("/idiomas")
@Controller
public class IdiomaController {

	@Autowired
	private IdiomaUsuarioRepo idiomaUsuarioRepo;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private IdiomaUsuarioService idiomaUsuarioService;

	@Autowired
	private IdiomasRepo idomasRepo;

	@RequestMapping
	public ModelAndView adicionarIdiomas() {
		final ModelAndView mv = new ModelAndView("user/adicionarIdioma");
		mv.addObject("idiomaUsuario", new IdiomaUsuario());
		return mv;
	}

	@RequestMapping(value = "/salvar", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public void salvar(@RequestBody final IdiomaUsuario idiomaUsuario) {
		idiomaUsuario.setIdioma(idomasRepo.findByIdioma(idiomaUsuario.getIdioma().getIdioma()));
		idiomaUsuario.setUsuario(usuarioService.getUsuarioDefault());
		idiomaUsuarioRepo.save(idiomaUsuario);
	}

	@RequestMapping(value = "/validaIdioma", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String validaIdiomaSelecionado(@RequestParam("idioma") final String idioma) {
		final Idiomas i = idomasRepo.findByIdioma(idioma);
		if (i == null) {
			return "<strong>" + idioma + "</strong> não é um idioma válido";
		}
		final boolean jaCadastrado = idiomaUsuarioRepo.findByIdiomaAndUsuario(i, usuarioService.getUsuarioDefault()) != null;
		if (jaCadastrado) {
			return "<strong>" + idioma + "</strong> Já está cadastrado. Selecione outro idioma";
		}
		return "";
	}

	@RequestMapping(value = "/remover", method = RequestMethod.POST)
	public @ResponseBody String remover(@RequestParam("id") final Integer id) {
		idiomaUsuarioService.removeIdioma(id);
		// idiomaUsuarioRepo.delete(id);
		idiomasDisponiveis();
		return "redirect:/user/idiomas";
	}

	@RequestMapping(value = "updateIdiomasList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<String> getIdiomasDisponiveis(@RequestParam final String query) {
		return idiomaUsuarioService.getIdiomasDisponiveis(usuarioService.getUsuarioDefault(), query).stream().map(i -> i.getIdioma()).filter(i -> i.toLowerCase().contains(query)).collect(Collectors.toList());
	}

	/**
	 * https://datatables.net/examples/ajax/null_data_source.html
	 *
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public String getMeusIdiomas() throws IOException {
		return idiomaUsuarioService.getMeusIdiomasJson(usuarioService.getUsuarioDefault());
	}

	public List<Idiomas> idiomasDisponiveis() {
		return idiomaUsuarioService.getIdiomasDisponiveis(usuarioService.getUsuarioDefault());
	}

}
