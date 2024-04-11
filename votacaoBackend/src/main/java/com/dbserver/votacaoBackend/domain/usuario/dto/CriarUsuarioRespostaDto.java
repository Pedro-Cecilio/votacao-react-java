package com.dbserver.votacaoBackend.domain.usuario.dto;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;

public record CriarUsuarioRespostaDto(
    Long id,
    String email,
    String nome,
    String sobrenome,
    String cpf,
    boolean admin 
) {
    public CriarUsuarioRespostaDto(Usuario usuario, Autenticacao autenticacao){
        this(usuario.getId(), autenticacao.getEmail(), usuario.getNome(), usuario.getSobrenome(), usuario.getCpf(), usuario.isAdmin());
    }
}
