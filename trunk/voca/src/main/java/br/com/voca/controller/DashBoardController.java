package br.com.voca.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.voca.repository.IdiomaUsuarioRepo;
import br.com.voca.repository.PalavrasRepo;
import br.com.voca.services.UsuarioService;

@Controller
@RequestMapping("/dashBoard")
public class DashBoardController {

	@Autowired
	private PalavrasRepo palavraRepo;

	@Autowired
	private IdiomaUsuarioRepo idiomaUsuarioRepo;

	@Autowired
	private UsuarioService usuarioService;

	@RequestMapping
	public ModelAndView dashBoard() {
		final ModelAndView md = new ModelAndView("user/dashBoard");
		final Long totalPalavras = palavraRepo.totalPalavras(usuarioService.getUsuarioDefault());
		final Float percent = calculaPorcentagem(palavraRepo.totalPalavrasCertas(usuarioService.getUsuarioDefault()), totalPalavras);
		md.addObject("totalIdiomas", idiomaUsuarioRepo.totalIdiomas(usuarioService.getUsuarioDefault()));
		md.addObject("totalPalavras", totalPalavras);
		md.addObject("percentualAcerto", String.format("%.0f", percent));
		md.addObject("stylePercent", getStylePercent(percent));
		md.addObject("u", usuarioService.getUsuarioDefault());
		return md;
	}

	public Float calculaPorcentagem(final Long totalAcertos, final Long totalPalavras) {
		final BigDecimal tp = new BigDecimal(totalPalavras);
		final BigDecimal ta = new BigDecimal(totalAcertos);
		return ta.floatValue() / tp.floatValue() * 100f;
	}

	public String getStylePercent(final Float percent) {
		final int intValue = percent.intValue();
		if (intValue <= 30) {
			return "#ff4b4b";
		}
		if (intValue > 30 && intValue < 60) {
			return "#ffb24b";
		}

		return "#5cc15f";
	}

}
