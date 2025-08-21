package br.com.matheus.helpdesk.dto;

import br.com.matheus.helpdesk.model.Ticket.Prioridade;

// Esta classe representa os dados que o cliente vai enviar para criar um ticket.
// Note que não tem ID, data de abertura, status, etc. Apenas o essencial.
public class TicketCreateDTO {

    private String titulo;
    private String descricao;
    private Prioridade prioridade;

    // Getters e Setters
    // O Spring usa eles para preencher o objeto com os dados do JSON
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }
}