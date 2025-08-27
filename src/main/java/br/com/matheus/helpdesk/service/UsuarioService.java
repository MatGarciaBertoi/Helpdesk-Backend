package br.com.matheus.helpdesk.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.matheus.helpdesk.dto.UsuarioCreateDTO;
import br.com.matheus.helpdesk.dto.UsuarioResponseDTO;
import br.com.matheus.helpdesk.dto.UsuarioUpdateDTO;
import br.com.matheus.helpdesk.model.Usuario;
import br.com.matheus.helpdesk.repository.TicketRepository;
import br.com.matheus.helpdesk.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TicketRepository ticketRepository;

    @Transactional
    public UsuarioResponseDTO criarUsuario(UsuarioCreateDTO usuarioDTO) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(usuarioDTO.getNome());
        novoUsuario.setEmail(usuarioDTO.getEmail());
        novoUsuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        novoUsuario.setPerfil(usuarioDTO.getPerfil());

        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
        return new UsuarioResponseDTO(usuarioSalvo);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
        return new UsuarioResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO atualizarUsuario(Long id, UsuarioUpdateDTO usuarioUpdateDTO) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        if (usuarioUpdateDTO.getNome() != null) {
            usuarioExistente.setNome(usuarioUpdateDTO.getNome());
        }
        if (usuarioUpdateDTO.getPerfil() != null) {
            usuarioExistente.setPerfil(usuarioUpdateDTO.getPerfil());
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);
        return new UsuarioResponseDTO(usuarioAtualizado);
    }

    @Transactional
    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado!");
        }
        int ticketCount = ticketRepository.countByClienteIdOrTecnicoId(id, id);
        if (ticketCount > 0) {
            throw new IllegalStateException("Não é possível deletar o usuário, pois ele possui " + ticketCount + " ticket(s) associado(s).");
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO getMe() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado no banco de dados"));
        return new UsuarioResponseDTO(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));

        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name())
        );

        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getSenha(),
                authorities
        );
    }
}