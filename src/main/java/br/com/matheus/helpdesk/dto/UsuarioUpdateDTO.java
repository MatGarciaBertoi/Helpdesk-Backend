package br.com.matheus.helpdesk.dto;

import br.com.matheus.helpdesk.model.Usuario.Perfil;

public class UsuarioUpdateDTO {

    private String nome;
    private Perfil perfil;

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}