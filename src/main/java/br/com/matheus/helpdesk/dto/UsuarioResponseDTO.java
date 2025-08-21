package br.com.matheus.helpdesk.dto;

import br.com.matheus.helpdesk.model.Usuario;
import br.com.matheus.helpdesk.model.Usuario.Perfil;

// Representa os dados de um usuário que SÃO SEGUROS para serem enviados na resposta da API
public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private Perfil perfil;

    // Construtor que converte uma Entidade Usuario em um DTO de resposta
    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.perfil = usuario.getPerfil();
    }

    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public Perfil getPerfil() { return perfil; }
}