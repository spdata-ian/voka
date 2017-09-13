package br.com.voca.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity(name = "Usuario")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	@NotNull
	@NotEmpty
	private String email;

	@Column
	@NotNull
	@NotEmpty
	private String senha;

	@Column
	@NotNull
	@NotEmpty
	private String nome;

	@Column
	private Boolean ativo;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "usuarioRole", joinColumns = @JoinColumn(name = "usuario"), inverseJoinColumns = @JoinColumn(name = "role"))
	private Set<Role> roles;

	@Transient
	private Idiomas idioma;

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(final String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(final Set<Role> roles) {
		this.roles = roles;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(final Boolean ativo) {
		this.ativo = ativo;
	}

	public Idiomas getIdioma() {
		return idioma;
	}

	public void setIdioma(final Idiomas idioma) {
		this.idioma = idioma;
	}

}
