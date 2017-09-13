package br.com.voca.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.voca.model.Idiomas;
import br.com.voca.model.Palavras;
import br.com.voca.model.Usuario;

@Repository
public class UsuarioDAO {

	@PersistenceContext
	EntityManager em;

	public Map<Idiomas, Long> getTotalPalavras(final Usuario usuario) {
		final String sql = "select new br.com.voca.model.Palavras(p.idioma, count(p.id)) from Palavras p where p.usuario = :usuario group by p.idioma";
		final TypedQuery<Palavras> query = em.createQuery(sql, Palavras.class);
		query.setParameter("usuario", usuario);
		final List<Palavras> list = query.getResultList();
		final Map<Idiomas, Long> mapa = new HashMap<>();
		list.forEach(i -> {
			mapa.put(i.getIdioma(), i.getTotalPalavras());
		});
		return mapa;
	}

}
