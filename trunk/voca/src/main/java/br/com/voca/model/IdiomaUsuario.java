package br.com.voca.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity(name = "IdiomaUsuario")
public class IdiomaUsuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "usuario")
	@NotNull
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "idioma")
	@NotNull
	private Idiomas idioma;

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(final Usuario usuario) {
		this.usuario = usuario;
	}

	public Idiomas getIdioma() {
		return idioma;
	}

	public void setIdioma(final Idiomas idioma) {
		this.idioma = idioma;
	}

}
