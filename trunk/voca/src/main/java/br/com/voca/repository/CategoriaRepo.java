package br.com.voca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.voca.model.Categorias;
import br.com.voca.model.Idiomas;
import br.com.voca.model.Usuario;

public interface CategoriaRepo extends JpaRepository<Categorias, Integer> {

	@Query("from Categorias c order by c.categoria asc")
	public List<Categorias> findAllOrderByCategoriaAsc();

	@Query("select p.categoria from Palavras p where p.usuario = :usuario")
	public List<Categorias> findCategoriasUsuario(@Param("usuario") Usuario usuario);

	@Query("select distinct p.categoria from Palavras p where p.usuario = :usuario and p.idioma = :idioma")
	public List<Categorias> findCategoriasUsuarioAndIdioma(@Param("usuario") Usuario usuario, @Param("idioma") Idiomas idioma);

	public Categorias findByCategoria(String categoria);

	@Query("select new br.com.voca.model.Categorias(p.categoria.categoria, count(p) as c) from Palavras p where p.usuario = :usuario group by p.categoria order by c desc")
	public List<Categorias> findTotalCategoriasAndPalavrasByUsuario(@Param("usuario") Usuario usuario);

}
