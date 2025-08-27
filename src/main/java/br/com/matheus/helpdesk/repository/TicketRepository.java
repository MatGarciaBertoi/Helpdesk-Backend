package br.com.matheus.helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.matheus.helpdesk.model.Ticket; // Nova importação

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    // Conta tickets por cliente ou técnico (para a regra de exclusão de usuário)
    int countByClienteIdOrTecnicoId(Long clienteId, Long tecnicoId);

    // --- NOSSO NOVO MÉTODO ---
    // Encontra todos os tickets onde o ID do cliente corresponde ao fornecido
    List<Ticket> findByClienteId(Long clienteId);
}