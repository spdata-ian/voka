package br.com.voca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.voca.model.IdiomaUsuario;
import br.com.voca.model.Idiomas;
import br.com.voca.model.Usuario;

public interface IdiomaUsuarioRepo extends JpaRepository<IdiomaUsuario, Integer> {

	public List<IdiomaUsuario> findByUsuario(Usuario usuario);

	@Query("from IdiomaUsuario u where u.usuario = :usuario and lower(u.idioma.idioma) like %:idioma%")
	public List<IdiomaUsuario> findByUsuarioAndIdiomaLike(@Param("usuario") Usuario usuario, @Param("idioma") String idioma);

	@Query("select new br.com.voca.model.Idiomas(p.idioma.id, p.idioma.idioma, count(p)) from Palavras p where p.usuario = :usuario group by p.idioma")
	public List<Idiomas> findByUsuarioTotalPalavras(@Param("usuario") Usuario usuario);

	@Query("select new br.com.voca.model.Idiomas(p.idioma.id, p.idioma.idioma, count(p)) from Palavras p where p.usuario = :usuario and p.acerto is not null and p.acerto = false group by p.idioma")
	public List<Idiomas> findByUsuarioTotalPalavrasErradas(@Param("usuario") Usuario usuario);

	@Query("select count(i) from IdiomaUsuario i where i.usuario = :usuario")
	public Long totalIdiomas(@Param("usuario") Usuario usuario);

	public IdiomaUsuario findByIdiomaAndUsuario(Idiomas idiomas, Usuario usuario);

}
