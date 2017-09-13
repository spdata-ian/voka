package br.com.voca.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.voca.imports.Import;
import br.com.voca.imports.ImportVoca;
import br.com.voca.model.Idiomas;
import br.com.voca.repository.CategoriaRepo;
import br.com.voca.repository.IdiomasRepo;

@Controller
@RequestMapping("/index")
public class IndexController {

	@Autowired
	private IdiomasRepo idiomasRepo;

	@Autowired
	private CategoriaRepo categoriaRepo;

	@Autowired
	private ImportVoca importVoca;

	@RequestMapping
	public ModelAndView index() throws IOException {
		final boolean vazio = idiomasRepo.findAll().isEmpty();
		final boolean categoriasVazia = categoriaRepo.findAll().isEmpty();
		if (vazio) {
			final List<Idiomas> idiomas = new Import().getIdiomas();
			idiomasRepo.save(idiomas);
		}
		if (categoriasVazia) {
			// categoriaRepo.save(new Import().getCategorias());
			importVoca.importarVocabulario();
		}

		final ModelAndView mv = new ModelAndView("index");
		return mv;
	}

	@RequestMapping("/dashBoard")
	public ModelAndView dashBoard() {
		final ModelAndView mv = new ModelAndView("user/dashBoard");
		return mv;
	}

}
