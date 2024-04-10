package com.dbserver.votacaoBackend.domain.autenticacao.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record CriarAutenticacaoDto(

        @NotEmpty
        @Email
        String email,

        @NotEmpty 
        String senha
) {

}
