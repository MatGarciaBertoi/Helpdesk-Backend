package br.com.matheus.helpdesk.dto;

import java.time.LocalDateTime;

import br.com.matheus.helpdesk.model.Ticket;
import br.com.matheus.helpdesk.model.Ticket.Prioridade;
import br.com.matheus.helpdesk.model.Ticket.Status;

public class TicketResponseDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    private Status status;
    private Prioridade prioridade;
    private UsuarioResponseDTO cliente;
    private UsuarioResponseDTO tecnico;

    public TicketResponseDTO(Ticket ticket) {
        this.id = ticket.getId();
        this.titulo = ticket.getTitulo();
        this.descricao = ticket.getDescricao();
        this.dataAbertura = ticket.getDataAbertura();
        this.dataFechamento = ticket.getDataFechamento();
        this.status = ticket.getStatus();
        this.prioridade = ticket.getPrioridade();
        // Converte o Usuario (entidade) para UsuarioResponseDTO para evitar expor a senha
        if (ticket.getCliente() != null) {
            this.cliente = new UsuarioResponseDTO(ticket.getCliente());
        }
        if (ticket.getTecnico() != null) {
            this.tecnico = new UsuarioResponseDTO(ticket.getTecnico());
        }
    }

    // Apenas Getters são necessários
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public LocalDateTime getDataFechamento() { return dataFechamento; }
    public Status getStatus() { return status; }
    public Prioridade getPrioridade() { return prioridade; }
    public UsuarioResponseDTO getCliente() { return cliente; }
    public UsuarioResponseDTO getTecnico() { return tecnico; }
}