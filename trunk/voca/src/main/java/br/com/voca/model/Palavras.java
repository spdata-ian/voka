package br.com.voca.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.codec.binary.Base64;

@Entity(name = "Palavras")
public class Palavras implements Serializable {

	private static final long serialVersionUID = 1L;

	public Palavras() {
	}

	public Palavras(final Idiomas idioma, final Long totalPalavras) {
		this.idioma = idioma;
		this.totalPalavras = totalPalavras;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	@NotNull
	private String palavra;

	@Column
	@NotNull
	private String traducao;

	@ManyToOne
	@JoinColumn(name = "idioma")
	@NotNull
	private Idiomas idioma;

	@ManyToOne
	@JoinColumn(name = "categoria")
	private Categorias categoria;

	@ManyToOne
	@JoinColumn(name = "usuario")
	@NotNull
	private Usuario usuario;

	@Lob
	private byte[] imagem;

	@Column(nullable = true)
	private Boolean acerto;

	@Transient
	private Long totalPalavras;

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public String getPalavra() {
		return palavra;
	}

	public void setPalavra(final String palavra) {
		this.palavra = palavra;
	}

	public String getTraducao() {
		return traducao;
	}

	public void setTraducao(final String traducao) {
		this.traducao = traducao;
	}

	public Idiomas getIdioma() {
		return idioma;
	}

	public void setIdioma(final Idiomas idioma) {
		this.idioma = idioma;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(final Usuario usuario) {
		this.usuario = usuario;
	}

	public Long getTotalPalavras() {
		return totalPalavras;
	}

	public void setTotalPalavras(final Long totalPalavras) {
		this.totalPalavras = totalPalavras;
	}

	public byte[] getImagem() {
		return imagem;
	}

	public void setImagem(final byte[] imagem) {
		this.imagem = imagem;
	}

	public Categorias getCategoria() {
		return categoria;
	}

	public void setCategoria(final Categorias categoria) {
		this.categoria = categoria;
	}

	public String getTab() {
		return "#tab" + id;
	}

	public Boolean getAcerto() {
		return acerto;
	}

	public void setAcerto(final boolean acerto) {
		this.acerto = acerto;
	}

	public String getImamgeBas64() {
		final String base64 = getImagem() != null ? Base64.encodeBase64String(getImagem()) : "";
		return base64;
	}

}
