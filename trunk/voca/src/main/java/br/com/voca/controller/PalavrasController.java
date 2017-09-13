package br.com.voca.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import br.com.voca.model.Categorias;
import br.com.voca.model.Idiomas;
import br.com.voca.model.Palavras;
import br.com.voca.repository.CategoriaRepo;
import br.com.voca.repository.IdiomaUsuarioRepo;
import br.com.voca.repository.PalavrasRepo;
import br.com.voca.services.PalavrasService;
import br.com.voca.services.UsuarioService;

@Controller
@RequestMapping("/palavras")
public class PalavrasController {

	@Autowired
	private IdiomaUsuarioRepo idiomaUsuarioRepo;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private PalavrasRepo palavraRepo;

	@Autowired
	private PalavrasService palavrasService;

	@Autowired
	private CategoriaRepo categoriaRepo;

	@RequestMapping
	public ModelAndView palavras() {
		final ModelAndView mv = new ModelAndView("user/palavras");
		mv.addObject("p", new Palavras());
		return mv;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public String list() {
		return palavrasService.getMinhasPalavras(usuarioService.getUsuarioDefault());
	}

	@RequestMapping(value = "/remover", method = RequestMethod.POST)
	public @ResponseBody String remover(@RequestParam("id") final Integer id) {
		palavraRepo.delete(id);
		return "redirect:/user/palavras";
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public List<String> salvar(final Palavras palavra, @RequestParam("file") final MultipartFile file) throws IOException {
		final List<String> mensagens = new ArrayList<>();

		final String type = file.getContentType();
		final List<String> types = Arrays.asList(new String[] { "image/png", "image/jpg", "image/jpeg" });
		final boolean hasUploaded = file.getBytes().length > 0;
		// palavra.setPalavra(null);
		if (hasUploaded && !types.contains(type)) {
			mensagens.add("Formato da imagem inválido");
		}

		if (StringUtils.isEmpty(palavra.getPalavra())) {
			mensagens.add("Preencha a palavra");
		}
		if (StringUtils.isEmpty(palavra.getTraducao())) {
			mensagens.add("Preencha a tradução");
		}
		if (mensagens.isEmpty()) {
			if (palavra.getId() != null && !hasUploaded) {
				palavra.setImagem(palavraRepo.findOne(palavra.getId()).getImagem());
			}
			if (file != null && hasUploaded) {
				palavra.setImagem(file.getBytes());
			}
			palavra.setUsuario(usuarioService.getUsuarioDefault());
			palavraRepo.save(palavra);
		}
		return mensagens;
	}

	@ModelAttribute("idiomas")
	public List<Idiomas> getIdiomas() {
		return idiomaUsuarioRepo.findByUsuario(usuarioService.getUsuarioDefault()).stream().map(i -> i.getIdioma()).collect(Collectors.toList());
	}

	@ModelAttribute("categorias")
	public List<Categorias> getCategorias() {
		return categoriaRepo.findAllOrderByCategoriaAsc();
	}

}
