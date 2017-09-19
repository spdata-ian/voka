package br.com.voca.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
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
import br.com.voca.model.Palavras;
import br.com.voca.repository.CategoriaRepo;
import br.com.voca.repository.IdiomaUsuarioRepo;
import br.com.voca.repository.PalavrasRepo;
import br.com.voca.services.PalavrasService;
import br.com.voca.services.UsuarioService;
import br.com.voca.synthesiser.Synthesiser;

@Controller
@RequestMapping({ "/praticar" })
public class PraticarController {

	@Autowired
	private IdiomaUsuarioRepo idiomaUsuarioRepo;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private CategoriaRepo categoriaRepo;

	@Autowired
	private PalavrasRepo palavrasRepo;

	@Autowired
	private PalavrasService palavrasService;

	@Autowired
	private Synthesiser synthesiser;

	@RequestMapping
	public ModelAndView opcoesPraticar() {
		final ModelAndView mv = new ModelAndView("user/praticar");
		mv.addObject("possuiPalavras", palavrasRepo.totalPalavras(usuarioService.getUsuarioDefault()) > 0);
		return mv;
	}

	/**
	 * Usado na tela de executarPratica
	 *
	 * @param idioma
	 * @param exibirTraducao
	 * @return
	 */
	@RequestMapping(value = "todas", method = RequestMethod.GET)
	public ModelAndView prepararPraticarTodos(@RequestParam("idiomaA") final Idiomas idioma, final Boolean exibirTraducao) {
		final ModelAndView mv = new ModelAndView("user/executaPratica");
		final List<Palavras> palavras = palavrasRepo.findByIdiomaAndUsuario(idioma, usuarioService.getUsuarioDefault());
		Collections.shuffle(palavras);
		mv.addObject("palavras", palavras);
		mv.addObject("idioma", idioma);
		mv.addObject("trd", exibirTraducao);
		return mv;
	}

	/**
	 * Usado na tela de executarPratica
	 *
	 * @param idioma
	 * @param exibirTraducao
	 * @return
	 */
	@RequestMapping(value = "erradas", method = RequestMethod.GET)
	public ModelAndView prepararPraticarErradas(@RequestParam("idiomaE") final Idiomas idioma, final Boolean exibirTraducao) {
		final ModelAndView mv = new ModelAndView("user/executaPratica");
		final List<Palavras> palavras = palavrasRepo.findByErradasAndIdiomaAndUsuario(idioma, usuarioService.getUsuarioDefault());
		Collections.shuffle(palavras);
		mv.addObject("palavras", palavras);
		mv.addObject("idioma", idioma);
		mv.addObject("trd", exibirTraducao);
		return mv;
	}

	/**
	 * Usado na tela de executarPratica
	 *
	 * @param idioma
	 * @param exibirTraducao
	 * @return
	 */
	@RequestMapping(value = "porCategorias", method = RequestMethod.GET)
	public ModelAndView prepararPraticarCategoria(@RequestParam("idiomaC") final Idiomas idioma, @RequestParam("categoria") final Categorias categoria, final Boolean exibirTraducao) {
		final ModelAndView mv = new ModelAndView("user/executaPratica");
		final List<Palavras> palavras = palavrasRepo.findByIdiomaAndUsuarioAndCategoria(idioma, usuarioService.getUsuarioDefault(), categoria);
		Collections.shuffle(palavras);
		mv.addObject("palavras", palavras);
		mv.addObject("idioma", idioma);
		mv.addObject("trd", exibirTraducao);
		return mv;

	}

	/**
	 * Usado na tela de executarPratica
	 *
	 * @param idioma
	 * @param exibirTraducao
	 * @return
	 */
	@RequestMapping(value = "porQuantidadePalavras", method = RequestMethod.GET)
	public ModelAndView prepararPraticarQuantidadePalavras(@RequestParam("idiomaQ") final Idiomas idioma, @RequestParam("totalPalavras") final Integer totalPalavras, final Boolean exibirTraducao) {
		final ModelAndView mv = new ModelAndView("user/executaPratica");
		final List<Palavras> palavras = palavrasRepo.findByIdiomaAndUsuario(idioma, usuarioService.getUsuarioDefault());
		Collections.shuffle(palavras);
		final int q = totalPalavras > palavras.size() ? palavras.size() : totalPalavras;
		mv.addObject("palavras", palavras.subList(0, q));
		mv.addObject("idioma", idioma);
		mv.addObject("trd", exibirTraducao);
		return mv;

	}

	/**
	 * Usado na tela de executarPratica
	 *
	 * @param idioma
	 * @param exibirTraducao
	 * @return
	 */
	@RequestMapping(value = "som", method = RequestMethod.GET, produces = "audio/mpeg")
	@ResponseBody
	public byte[] som(@RequestParam("acerto") final Boolean acertou) throws Exception {
		if (acertou) {
			return IOUtils.toByteArray(getClass().getResourceAsStream("/static/souds/certo2.mp3"));
		} else {
			return IOUtils.toByteArray(getClass().getResourceAsStream("/static/souds/errado.mp3"));
		}
	}

	@RequestMapping(value = "somFinalizar", method = RequestMethod.GET, produces = "audio/mpeg")
	@ResponseBody
	public byte[] somFinalizar() throws Exception {
		return IOUtils.toByteArray(getClass().getResourceAsStream("/static/souds/final.mp3"));
	}

	/**
	 * Usado na tela de executarPratica
	 *
	 * @param idioma
	 * @param exibirTraducao
	 * @return
	 */
	// @RequestMapping(value = "ouvir", method = RequestMethod.GET, produces =
	// "application/json")
	// @ResponseBody
	// public String ouvir(final String palavra, final String idioma) throws
	// IOException {
	// final InputStream audio =
	// synthesiser.setLanguage(idioma).getMP3Data(palavra);
	// return Base64.encodeBase64String(IOUtils.toByteArray(audio));
	// }

	/**
	 * Usado na tela de executarPratica
	 *
	 * @param idioma
	 * @param exibirTraducao
	 * @return
	 */
	@RequestMapping(value = "novoOuvir", method = RequestMethod.GET, produces = "audio/mpeg")
	@ResponseBody
	public byte[] novoOuvir(@RequestParam("l") final String language, @RequestParam("w") final String word) throws IOException {
		final InputStream audio = synthesiser.setLanguage(language).getMP3Data(word);
		return IOUtils.toByteArray(audio);
	}

	/**
	 * Usado na tela de executarPratica
	 *
	 * @param idioma
	 * @param exibirTraducao
	 * @return
	 */
	@RequestMapping(value = "finalizar", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void String(@RequestParam("dados") final String dados) throws Exception {
		final JSONArray jsonArray = new JSONArray(dados);
		palavrasService.finalizarPratica(jsonArray);
	}

	@RequestMapping(value = "categoriaPorIdioma", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Categorias> getCategoriasDeAcordoComIdioma(@RequestParam("idioma") final Idiomas idioma) {
		return categoriaRepo.findCategoriasUsuarioAndIdioma(usuarioService.getUsuarioDefault(), idioma);
	}

	@ModelAttribute("idiomas")
	public List<Idiomas> getIdiomas() {
		return idiomaUsuarioRepo.findByUsuarioTotalPalavras(usuarioService.getUsuarioDefault());
	}

	@ModelAttribute("idiomasPalavrasErradas")
	public List<Idiomas> getIdiomasPalavrasErradas() {
		return idiomaUsuarioRepo.findByUsuarioTotalPalavrasErradas(usuarioService.getUsuarioDefault());
	}

	// @ModelAttribute("categorias")
	// public List<Categorias> getCategorias() {
	// return
	// categoriaRepo.findCategoriasUsuario(usuarioService.getUsuarioDefault());
	// }

}
