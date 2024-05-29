package com.dbserver.votacaoBackend.domain.usuario.dto;

public record CriarUsuarioRespostaDto(
    Long id,
    String email,
    String nome,
    String sobrenome,
    String cpf,
    boolean admin 
) {

}
