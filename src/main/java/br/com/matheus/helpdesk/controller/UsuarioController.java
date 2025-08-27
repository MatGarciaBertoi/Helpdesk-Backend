package br.com.matheus.helpdesk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.matheus.helpdesk.dto.UsuarioCreateDTO;
import br.com.matheus.helpdesk.dto.UsuarioResponseDTO;
import br.com.matheus.helpdesk.dto.UsuarioUpdateDTO;
import br.com.matheus.helpdesk.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDTO criar(@RequestBody UsuarioCreateDTO usuarioDTO) {
        return usuarioService.criarUsuario(usuarioDTO);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public UsuarioResponseDTO buscarPorId(@PathVariable Long id) {
        try {
            return usuarioService.buscarPorId(id);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
    
    // Novo endpoint para retornar o usuário logado
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public UsuarioResponseDTO getMe() {
        return usuarioService.getMe();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public UsuarioResponseDTO atualizar(@PathVariable Long id, @RequestBody UsuarioUpdateDTO usuarioUpdateDTO) {
        try {
            return usuarioService.atualizarUsuario(id, usuarioUpdateDTO);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void deletar(@PathVariable Long id) {
        try {
            usuarioService.deletarUsuario(id);
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}