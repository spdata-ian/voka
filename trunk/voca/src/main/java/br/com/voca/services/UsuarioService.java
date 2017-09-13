package br.com.voca.services;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.voca.model.IdiomaUsuario;
import br.com.voca.model.Idiomas;
import br.com.voca.model.Role;
import br.com.voca.model.Usuario;
import br.com.voca.repository.IdiomaUsuarioRepo;
import br.com.voca.repository.RoleRepository;
import br.com.voca.repository.UsuarioRepo;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepo usuarioRepo;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private IdiomaUsuarioRepo idiomaUsuarioRepo;

	private Usuario usuario;

	public Usuario getUsuarioDefault() {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		usuario = findByEmail(auth.getName());
		return usuario;
	}

	public void registrarUsuario(Usuario usuario) {
		usuario.setSenha(bCryptPasswordEncoder.encode(usuario.getSenha()));
		final Role userRole = roleRepository.findByRole("USER");
		usuario.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		final Idiomas idioma = usuario.getIdioma();
		usuario = usuarioRepo.save(usuario);
		if (idioma != null) {
			final IdiomaUsuario iu = new IdiomaUsuario();
			iu.setUsuario(usuario);
			iu.setIdioma(idioma);
			idiomaUsuarioRepo.save(iu);
		}
	}

	public Usuario findByEmail(final String email) {
		return usuarioRepo.findByEmail(email);
	}

}
