package br.com.voca.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.voca.dao.UsuarioDAO;
import br.com.voca.model.IdiomaUsuario;
import br.com.voca.model.Idiomas;
import br.com.voca.model.Usuario;
import br.com.voca.repository.IdiomaUsuarioRepo;
import br.com.voca.repository.IdiomasRepo;
import br.com.voca.repository.PalavrasRepo;

@Service
public class IdiomaUsuarioService {

	@Autowired
	private IdiomaUsuarioRepo idiomaUsuarioRepo;

	@Autowired
	private IdiomasRepo idiomasRepo;

	@Autowired
	private UsuarioDAO usuarioDAO;

	@Autowired
	private PalavrasRepo palavrasRepo;

	public List<Idiomas> getIdiomasDisponiveis(final Usuario usuario) {
		List<Idiomas> disponiveis = new ArrayList<>();
		final List<IdiomaUsuario> iUsuario = idiomaUsuarioRepo.findByUsuario(usuario);
		final List<Idiomas> i = iUsuario.stream().map(e -> e.getIdioma()).collect(Collectors.toList());
		if (i.isEmpty()) {
			disponiveis = idiomasRepo.findAll();
		} else {
			disponiveis = idiomasRepo.findNotIn(i);
		}
		return disponiveis;
	}

	/**
	 * Usado no autocomplet
	 *
	 * @param usuario
	 * @param idioma
	 * @return
	 */
	public List<Idiomas> getIdiomasDisponiveis(final Usuario usuario, final String idioma) {
		List<Idiomas> disponiveis = new ArrayList<>();
		final List<IdiomaUsuario> iUsuario = idiomaUsuarioRepo.findByUsuarioAndIdiomaLike(usuario, idioma.toLowerCase());
		final List<Idiomas> i = iUsuario.stream().map(e -> e.getIdioma()).collect(Collectors.toList());
		if (i.isEmpty()) {
			disponiveis = idiomasRepo.findByIdiomaLike(idioma);
		} else {
			disponiveis = idiomasRepo.findNotIn(i, idioma);
		}
		return disponiveis;
	}

	public String getMeusIdiomasJson(final Usuario usuario) {
		final List<IdiomaUsuario> idiomas = idiomaUsuarioRepo.findByUsuario(usuario);
		final Map<Idiomas, Long> totais = usuarioDAO.getTotalPalavras(usuario);
		final JSONObject data = new JSONObject();
		final JSONArray list = new JSONArray();

		idiomas.forEach(i -> {
			final JSONObject obj = new JSONObject();
			obj.put("id", i.getId());
			obj.put("idioma", i.getIdioma().getIdioma());
			final Long total = totais.get(i.getIdioma());
			obj.put("palavras", total == null ? 0 : total);
			list.add(obj);

		});

		data.put("data", list);

		return data.toJSONString();
	}

	public void removeIdioma(final Integer idiomaUsuario) {
		final IdiomaUsuario iu = idiomaUsuarioRepo.findOne(idiomaUsuario);
		palavrasRepo.deleteByIdiomaAndUsuario(iu.getIdioma(), iu.getUsuario());
		idiomaUsuarioRepo.delete(iu);
	}

}
