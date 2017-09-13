package br.com.voca.imports;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import br.com.voca.model.Categorias;
import br.com.voca.model.Idiomas;

public class Import {

	public static void main(final String[] args) throws IOException {
		new Import().getIdiomas();
	}

	public List<Idiomas> getIdiomas() throws IOException {
		final List<Idiomas> idiomas = new ArrayList<>();
		final String file = FileUtils.readFileToString(new File(FileUtils.getUserDirectoryPath() + "/Documents/pt.txt"), Charset.forName("UTF-8"));
		// System.out.println(file + "\n\n\n");
		final String[] array = file.split("\n");
		for (final String linha : array) {
			final String[] split = linha.split("-");
			idiomas.add(new Idiomas(split[0].trim(), split[1].trim()));
		}

		return idiomas;
	}

	public List<Categorias> getCategorias() {
		// Categorias
		// Verbos
		// Expressões
		// Casa
		// Objetos
		// Partes do Corpo
		// Cores
		// Números
		// Profissões
		// Adjetivos
		// Veiculos
		// Comidas e Bebidas
		// Roupas

		final List<Categorias> list = new ArrayList<>();
		list.add(new Categorias("Verbos"));
		list.add(new Categorias("Expressões"));
		list.add(new Categorias("Partes da Casa"));
		list.add(new Categorias("Partes do Corpo"));
		list.add(new Categorias("Objetos"));
		list.add(new Categorias("Cores"));
		list.add(new Categorias("Profissões"));
		list.add(new Categorias("Adjetivos"));
		list.add(new Categorias("Veiculos"));
		list.add(new Categorias("Comidas e Bebidas"));
		list.add(new Categorias("Roupas"));
		list.add(new Categorias("Animais"));
		return list;
	}

}
