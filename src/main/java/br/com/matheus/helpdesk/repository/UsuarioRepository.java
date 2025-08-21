package br.com.matheus.helpdesk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.matheus.helpdesk.model.Usuario; // Nova importação

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Declaração do método para buscar um usuário pelo email
    Optional<Usuario> findByEmail(String email);
}