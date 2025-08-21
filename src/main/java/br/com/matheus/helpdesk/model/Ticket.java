package br.com.matheus.helpdesk.model;

import java.time.LocalDateTime; // Nova importação
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Ticket {

    // ... todos os outros campos continuam iguais ...
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Lob
    private String descricao;

    private LocalDateTime dataAbertura = LocalDateTime.now();

    private LocalDateTime dataFechamento;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ABERTO;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    @ManyToOne
    private Usuario cliente;

    @ManyToOne
    private Usuario tecnico;
    
    // --- MUDANÇA AQUI ---
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Lado "pai" da relação. Será incluído no JSON.
    private List<Comentario> comentarios = new ArrayList<>();


    public enum Status {
        ABERTO,
        EM_ANDAMENTO,
        FECHADO
    }

    public enum Prioridade {
        BAIXA,
        MEDIA,
        ALTA
    }
}