package br.com.voca.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity(name = "Idiomas")
public class Idiomas implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column
	@NotNull
	private String idioma;

	@Transient
	private Long count;

	public Idiomas() {
	}

	public Idiomas(final String idioma) {
		this.idioma = idioma;
	}

	public Idiomas(final String id, final String idioma) {
		this.idioma = idioma;
		this.id = id;
	}

	public Idiomas(final String id, final String idioma, final Long count) {
		this.id = id;
		this.idioma = idioma;
		this.count = count;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getIdioma() {
		return idioma;
	}

	public void setIdioma(final String idioma) {
		this.idioma = idioma;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(final Long count) {
		this.count = count;
	}

	public String getIdiomaComCount() {
		return idioma + " - " + count + " palavra(s)";
	}

}
