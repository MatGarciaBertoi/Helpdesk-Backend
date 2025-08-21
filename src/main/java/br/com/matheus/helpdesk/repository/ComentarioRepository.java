package br.com.matheus.helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Nova importação
import org.springframework.stereotype.Repository;

import br.com.matheus.helpdesk.model.Comentario; // Nova importação

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    
    // ESTA É A NOVA CONSULTA CUSTOMIZADA
    @Query("SELECT c FROM Comentario c JOIN FETCH c.autor WHERE c.ticket.id = :ticketId")
    List<Comentario> findByTicketIdWithAutor(Long ticketId);
}