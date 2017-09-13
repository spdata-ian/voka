package br.com.voca.services;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.voca.model.Palavras;
import br.com.voca.model.Usuario;
import br.com.voca.repository.PalavrasRepo;

@Service
public class PalavrasService {

	@Autowired
	private PalavrasRepo palavrasRepo;

	@PersistenceContext
	private EntityManager entityManager;

	public String getMinhasPalavras(final Usuario usuario) {
		final List<Palavras> palavras = palavrasRepo.findByUsuarioOrderByIdDesc(usuario);
		final JSONObject data = new JSONObject();
		final JSONArray list = new JSONArray();

		palavras.forEach(i -> {

			final String categoria = i.getCategoria() != null ? i.getCategoria().getCategoria() : "Nenhuma";
			// final String base64 = i.getImagem() != null ?
			// Base64.getEncoder().encodeToString(i.getImagem()) : "";

			String base64 = null;
			if (i.getImagem() != null && i.getImagem().length > 0) {
				base64 = Base64.encodeBase64String(i.getImagem());
			} else {
				try {
					base64 = Base64.encodeBase64String(IOUtils.toByteArray(getClass().getResourceAsStream("/static/img/NO-IMAGE.png")));
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}

			final JSONObject obj = new JSONObject();
			obj.put("id", i.getId());
			obj.put("palavra", i.getPalavra());
			obj.put("traducao", i.getTraducao());
			obj.put("idioma", i.getIdioma().getIdioma());
			obj.put("categoria", categoria);
			obj.put("imagem", base64);
			list.add(obj);

		});

		data.put("data", list);

		return data.toJSONString();
	}

	@Transactional
	public void finalizarPratica(final org.json.JSONArray jsonArray) throws JSONException {
		final String sql = "update Palavras p set p.acerto = :acerto where p.id = :id";
		// entityManager.getTransaction().begin();
		for (int i = 0; i < jsonArray.length(); i++) {
			final org.json.JSONObject jsonobject = jsonArray.getJSONObject(i);
			final Query query = entityManager.createQuery(sql);
			query.setParameter("acerto", jsonobject.getBoolean("acerto"));
			query.setParameter("id", jsonobject.getInt("id"));
			query.executeUpdate();
		}
		// entityManager.getTransaction().commit();

	}

}
