package br.com.matheus.helpdesk.model;

import java.time.LocalDateTime; // Nova importação

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String texto;

    private LocalDateTime dataCriacao = LocalDateTime.now();

    // --- MUDANÇA AQUI ---
    @ManyToOne
    @JoinColumn(name = "ticket_id")
    @JsonBackReference // Lado "filho" da relação. Será omitido no JSON para evitar o loop.
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Usuario autor;
}