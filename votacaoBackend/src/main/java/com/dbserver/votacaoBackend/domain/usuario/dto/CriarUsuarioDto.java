package com.dbserver.votacaoBackend.domain.usuario.dto;

import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutenticacaoDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CriarUsuarioDto(

    @NotNull
    AutenticacaoDto autenticacaoDto,
    
    @NotEmpty
    String nome,

    @NotEmpty
    String sobrenome,

    @NotEmpty
    String cpf,

    @NotNull
    boolean admin

) {
    
}
