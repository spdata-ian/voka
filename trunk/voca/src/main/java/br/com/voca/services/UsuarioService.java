package br.com.voca.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.voca.mail.EmailService;
import br.com.voca.model.IdiomaUsuario;
import br.com.voca.model.Idiomas;
import br.com.voca.model.Role;
import br.com.voca.model.Usuario;
import br.com.voca.repository.IdiomaUsuarioRepo;
import br.com.voca.repository.RoleRepository;
import br.com.voca.repository.UsuarioRepo;

@Service
public class UsuarioService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public UsuarioRepo usuarioRepo;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private IdiomaUsuarioRepo idiomaUsuarioRepo;

	@Autowired
	private IdiomaUsuarioService idiomaUsuarioService;

	@Autowired
	private EmailService emailService;

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
		emailService.sendSimpleMessage(usuario.getEmail(), "Bem vindo(a) " + usuario.getNome(), "Espero que possamos lhe ajudar no seu aprendizado!");
	}

	public Usuario findByEmail(final String email) {
		return usuarioRepo.findByEmail(email);
	}

	public void alterarSenha(final Usuario usuario, final String senha) {
		usuario.setSenha(bCryptPasswordEncoder.encode(senha));
		usuarioRepo.save(usuario);
	}

	public boolean alterarDados(final Usuario atual, final Usuario dados) {
		final Usuario existente = findByEmail(dados.getEmail());
		if (existente != null && !existente.getId().equals(atual.getId())) {
			return false;
		}
		atual.setNome(dados.getNome());
		atual.setEmail(dados.getEmail());
		usuarioRepo.save(atual);
		return true;

	}

	@Transactional
	public void removerConta(final Usuario usuario2) {
		final List<IdiomaUsuario> idiomas = idiomaUsuarioRepo.findByUsuario(usuario2);
		idiomas.forEach(i -> {
			idiomaUsuarioService.removeIdioma(i.getId());
		});
		final String removeRole = "delete from usuarioRole  where usuario = :usuario";
		entityManager.createNativeQuery(removeRole).setParameter("usuario", usuario2.getId()).executeUpdate();
		usuarioRepo.delete(usuario2);
		emailService.sendSimpleMessage(usuario2.getEmail(), "Conta removida", usuario2.getNome() + ", foi um imenso prazer ter você por aqui! Espero que tenha gostado e que tenhamos lhe ajudado no seu aprendizado!");

	}

	public boolean recuperarSenha(final String email) {
		final Usuario u = usuarioRepo.findByEmail(email);
		if (u == null) {
			return false;
		}
		u.setSenha(bCryptPasswordEncoder.encode("abc123"));
		usuarioRepo.save(u);
		emailService.sendSimpleMessage(u.getEmail(), "Recuperação de senha", u.getNome() + ", sua senha foi recuperar, e agora é: abc123");
		return true;

	}

}
