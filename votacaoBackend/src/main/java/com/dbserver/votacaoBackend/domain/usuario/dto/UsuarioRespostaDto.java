package com.dbserver.votacaoBackend.domain.usuario.dto;

import com.dbserver.votacaoBackend.domain.usuario.Usuario;

public record UsuarioRespostaDto(
    Long id,
    String nome,
    String sobrenome,
    String cpf,
    boolean admin 
) {
    public UsuarioRespostaDto(Usuario usuario){
        this(usuario.getId(), usuario.getNome(), usuario.getSobrenome(), usuario.getCpf(), usuario.isAdmin());
    }
}
