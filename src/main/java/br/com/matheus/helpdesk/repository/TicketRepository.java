package br.com.matheus.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.matheus.helpdesk.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    // Novo método para contar tickets por cliente ou técnico
    int countByClienteIdOrTecnicoId(Long clienteId, Long tecnicoId);
}