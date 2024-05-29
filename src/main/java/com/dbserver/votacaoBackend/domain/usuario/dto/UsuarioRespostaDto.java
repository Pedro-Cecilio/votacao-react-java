package com.dbserver.votacaoBackend.domain.usuario.dto;


public record UsuarioRespostaDto(
    Long id,
    String nome,
    String sobrenome,
    String cpf,
    boolean admin 
) {}
