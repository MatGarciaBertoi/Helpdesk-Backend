package br.com.matheus.helpdesk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.matheus.helpdesk.dto.ComentarioCreateDTO;
import br.com.matheus.helpdesk.dto.TicketCreateDTO;
import br.com.matheus.helpdesk.dto.TicketResponseDTO;
import br.com.matheus.helpdesk.dto.TicketUpdateDTO;
import br.com.matheus.helpdesk.model.Comentario;
import br.com.matheus.helpdesk.model.Ticket;
import br.com.matheus.helpdesk.service.TicketService;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMINISTRADOR')")
    public List<TicketResponseDTO> listarTodos() {
        return ticketService.listarTodosDTO();
    }

    // --- NOSSO NOVO ENDPOINT PARA O CLIENTE ---
    @GetMapping("/meus-tickets")
    @PreAuthorize("hasRole('CLIENTE')")
    public List<TicketResponseDTO> listarMeusTickets() {
        return ticketService.listarMeusTickets();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMINISTRADOR') or @ticketService.isOwner(authentication.name, #id)")
    public TicketResponseDTO buscarPorId(@PathVariable Long id) {
        try {
            return ticketService.buscarPorIdDTO(id);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('CLIENTE')")
    public Ticket criar(@RequestBody TicketCreateDTO ticketDTO) {
        return ticketService.criarTicket(ticketDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMINISTRADOR')")
    public TicketResponseDTO atualizar(@PathVariable Long id, @RequestBody TicketUpdateDTO ticketUpdateDTO) {
        try {
            return ticketService.atualizarTicketDTO(id, ticketUpdateDTO);
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMINISTRADOR')")
    public void deletar(@PathVariable Long id) {
        try {
            ticketService.deletarTicket(id);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping("/{ticketId}/comentarios")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMINISTRADOR') or @ticketService.isOwner(authentication.name, #ticketId)")
    public Comentario adicionarComentario(@PathVariable Long ticketId, @RequestBody ComentarioCreateDTO comentarioDTO) {
        try {
            return ticketService.adicionarComentario(ticketId, comentarioDTO);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/{ticketId}/comentarios")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMINISTRADOR') or @ticketService.isOwner(authentication.name, #ticketId)")
    public List<Comentario> listarComentarios(@PathVariable Long ticketId) {
        try {
            return ticketService.listarComentariosPorTicket(ticketId);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PatchMapping("/{id}/atribuir")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMINISTRADOR')")
    public ResponseEntity<Ticket> atribuir(@PathVariable Long id) {
        try {
            Ticket ticketAtualizado = ticketService.atribuirTicket(id);
            return ResponseEntity.ok(ticketAtualizado);
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}