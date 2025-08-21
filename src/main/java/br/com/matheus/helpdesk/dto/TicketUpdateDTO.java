package br.com.matheus.helpdesk.dto;

import br.com.matheus.helpdesk.model.Ticket.Prioridade;
import br.com.matheus.helpdesk.model.Ticket.Status;

// Esta classe representa os dados que podem ser enviados para ATUALIZAR um ticket.
public class TicketUpdateDTO {

    private Prioridade prioridade;
    private Status status;

    // Getters e Setters
    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}