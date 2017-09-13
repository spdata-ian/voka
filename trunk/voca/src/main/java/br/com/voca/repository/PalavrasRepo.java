package br.com.voca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.voca.model.Categorias;
import br.com.voca.model.Idiomas;
import br.com.voca.model.Palavras;
import br.com.voca.model.Usuario;

public interface PalavrasRepo extends JpaRepository<Palavras, Integer> {

	@Query("select count(p) from Palavras p where p.usuario = :usuario")
	public Long totalPalavras(@Param("usuario") Usuario usuario);

	@Query("select count(p) from Palavras p where p.usuario = :usuario and p.acerto is not null and p.acerto = true")
	public Long totalPalavrasCertas(@Param("usuario") Usuario usuario);

	public List<Palavras> findByUsuarioOrderByIdDesc(Usuario usuario);

	public List<Palavras> findByIdiomaAndUsuario(Idiomas idioma, Usuario usuario);

	public List<Palavras> findByIdiomaAndUsuarioAndCategoria(Idiomas idioma, Usuario usuario, Categorias categorias);

	@Query("from Palavras p where p.idioma = :idioma and p.usuario = :usuario and p.acerto is not null and p.acerto = false")
	public List<Palavras> findByErradasAndIdiomaAndUsuario(@Param("idioma") Idiomas idioma, @Param("usuario") Usuario usuario);

	@Transactional
	public void deleteByIdiomaAndUsuario(Idiomas idioma, Usuario usuario);

}
