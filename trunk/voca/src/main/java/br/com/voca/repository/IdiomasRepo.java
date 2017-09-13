package br.com.voca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.voca.model.Idiomas;

public interface IdiomasRepo extends JpaRepository<Idiomas, String> {

	@Query("from Idiomas i where i not in (:idiomas)")
	public List<Idiomas> findNotIn(@Param("idiomas") List<Idiomas> idiomas);

	@Query("from Idiomas i where i not in (:idiomas) and lower(i.idioma) like %:idioma%")
	public List<Idiomas> findNotIn(@Param("idiomas") List<Idiomas> idiomas, @Param("idioma") String idioma);

	@Query("from Idiomas i where lower(i.idioma) like %:idioma%")
	public List<Idiomas> findByIdiomaLike(@Param("idioma") String idioma);

	public Idiomas findByIdioma(String idioma);

}
