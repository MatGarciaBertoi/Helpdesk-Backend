package br.com.matheus.helpdesk.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.matheus.helpdesk.dto.ComentarioCreateDTO;
import br.com.matheus.helpdesk.dto.TicketCreateDTO;
import br.com.matheus.helpdesk.dto.TicketResponseDTO;
import br.com.matheus.helpdesk.dto.TicketUpdateDTO;
import br.com.matheus.helpdesk.model.Comentario;
import br.com.matheus.helpdesk.model.Ticket;
import br.com.matheus.helpdesk.model.Usuario;
import br.com.matheus.helpdesk.repository.ComentarioRepository;
import br.com.matheus.helpdesk.repository.TicketRepository;
import br.com.matheus.helpdesk.repository.UsuarioRepository;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Transactional
    public Ticket criarTicket(TicketCreateDTO ticketDTO) {
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario cliente = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));

        Ticket novoTicket = new Ticket();
        novoTicket.setTitulo(ticketDTO.getTitulo());
        novoTicket.setDescricao(ticketDTO.getDescricao());
        novoTicket.setPrioridade(ticketDTO.getPrioridade());
        novoTicket.setCliente(cliente);

        return ticketRepository.save(novoTicket);
    }

    @Transactional(readOnly = true)
    public Ticket buscarPorId(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket não encontrado!"));
    }

    @Transactional(readOnly = true)
    public TicketResponseDTO buscarPorIdDTO(Long id) {
        Ticket ticket = buscarPorId(id);
        return new TicketResponseDTO(ticket);
    }

    @Transactional(readOnly = true)
    public List<TicketResponseDTO> listarTodosDTO() {
        return ticketRepository.findAll()
                .stream()
                .map(TicketResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public TicketResponseDTO atualizarTicketDTO(Long id, TicketUpdateDTO ticketUpdateDTO) {
        Ticket ticketExistente = buscarPorId(id);

        if (ticketExistente.getStatus() == Ticket.Status.FECHADO) {
            throw new IllegalStateException("Não é possível alterar um ticket que já foi fechado.");
        }
        if (ticketUpdateDTO.getPrioridade() != null) {
            ticketExistente.setPrioridade(ticketUpdateDTO.getPrioridade());
        }
        if (ticketUpdateDTO.getStatus() != null) {
            if (ticketUpdateDTO.getStatus() == Ticket.Status.ABERTO && ticketExistente.getTecnico() != null) {
                throw new IllegalStateException("Não é possível reabrir um ticket que já está atribuído a um técnico.");
            }
            ticketExistente.setStatus(ticketUpdateDTO.getStatus());
            if (ticketUpdateDTO.getStatus() == Ticket.Status.FECHADO) {
                ticketExistente.setDataFechamento(LocalDateTime.now());
            }
        }
        Ticket ticketAtualizado = ticketRepository.save(ticketExistente);
        return new TicketResponseDTO(ticketAtualizado);
    }

    @Transactional
    public void deletarTicket(Long id) {
        buscarPorId(id);
        ticketRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean isOwner(String userEmail, Long ticketId) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            return false;
        }
        Ticket ticket = ticketOpt.get();
        if (ticket.getCliente() == null) {
            return false;
        }
        return userEmail.equals(ticket.getCliente().getEmail());
    }

    @Transactional
    public Comentario adicionarComentario(Long ticketId, ComentarioCreateDTO comentarioDTO) {
        Ticket ticket = buscarPorId(ticketId);
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario autor = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Autor não encontrado!"));

        Comentario novoComentario = new Comentario();
        novoComentario.setTexto(comentarioDTO.getTexto());
        novoComentario.setTicket(ticket);
        novoComentario.setAutor(autor);

        return comentarioRepository.save(novoComentario);
    }

    @Transactional(readOnly = true)
    public List<Comentario> listarComentariosPorTicket(Long ticketId) {
        buscarPorId(ticketId);
        return comentarioRepository.findByTicketIdWithAutor(ticketId);
    }

    @Transactional
    public Ticket atribuirTicket(Long ticketId) {
        String emailTecnicoLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario tecnico = usuarioRepository.findByEmail(emailTecnicoLogado)
                .orElseThrow(() -> new RuntimeException("Técnico não encontrado!"));

        Ticket ticket = buscarPorId(ticketId);

        if (ticket.getTecnico() != null || ticket.getStatus() == Ticket.Status.FECHADO) {
            throw new IllegalStateException("Este ticket já foi atribuído ou está fechado.");
        }

        ticket.setTecnico(tecnico);
        ticket.setStatus(Ticket.Status.EM_ANDAMENTO);

        return ticketRepository.save(ticket);
    }
    
    // --- NOSSO NOVO MÉTODO PARA BUSCAR TICKETS DO CLIENTE ---
    @Transactional(readOnly = true)
    public List<TicketResponseDTO> listarMeusTickets() {
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario cliente = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));

        return ticketRepository.findByClienteId(cliente.getId())
                .stream()
                .map(TicketResponseDTO::new)
                .collect(Collectors.toList());
    }
}