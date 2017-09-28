package br.com.voca.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity(name = "Categorias")
public class Categorias implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	@NotNull
	private String categoria;

	@Transient
	private Long count;

	public Categorias() {
	}

	public Categorias(final String categoria, final Long count) {
		this.categoria = categoria;
		this.count = count;
	}

	public Categorias(final String string) {
		categoria = string;
	}

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(final String categoria) {
		this.categoria = categoria;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(final Long count) {
		this.count = count;
	}

}
