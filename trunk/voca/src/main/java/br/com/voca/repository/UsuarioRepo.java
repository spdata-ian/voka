package br.com.voca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.voca.model.Usuario;

public interface UsuarioRepo extends JpaRepository<Usuario, Integer> {

	Usuario findByEmail(String email);

}
