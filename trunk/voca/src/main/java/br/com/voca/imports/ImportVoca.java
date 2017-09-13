package br.com.voca.imports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Charsets;

import br.com.voca.model.Categorias;
import br.com.voca.model.Idiomas;
import br.com.voca.model.Palavras;
import br.com.voca.model.Usuario;
import br.com.voca.repository.CategoriaRepo;
import br.com.voca.repository.PalavrasRepo;

@Service
public class ImportVoca {

	@Autowired
	private CategoriaRepo categoriaRepo;

	@Autowired
	private PalavrasRepo palavrasRepo;

	public void importarVocabulario() throws FileNotFoundException, IOException {
		final String v = IOUtils.toString(new FileInputStream(new File(FileUtils.getUserDirectoryPath() + "/Documents/vocabulario.csv")), Charsets.UTF_8);
		final List<String> linhas = Arrays.asList(v.split("\n"));
		final List<Palavras> palavras = new ArrayList<>();

		final TreeSet<String> set = new TreeSet<>();
		for (final String li : linhas) {
			set.add(li.split(",")[2]);
		}

		importCategorias(set);

		for (final String li : linhas) {
			palavras.add(getPalavra(li.split(",")));
		}

		palavrasRepo.save(palavras);

	}

	public Palavras getPalavra(final String[] linha) {
		final Palavras p = new Palavras();
		p.setPalavra(linha[0]);
		p.setTraducao(linha[1]);
		p.setCategoria(categoriaRepo.findByCategoria(linha[2]));
		final Idiomas i = new Idiomas();
		i.setId("de");
		final Usuario u = new Usuario();
		u.setId(13);
		p.setIdioma(i);
		p.setUsuario(u);
		return p;
	}

	public void importCategorias(final TreeSet<String> set) {

		set.forEach(i -> {
			final Categorias c = new Categorias(i);
			categoriaRepo.save(c);

		});
	}

}
