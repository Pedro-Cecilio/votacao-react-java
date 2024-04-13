package com.dbserver.votacaoBackend.domain.autenticacao.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record AutenticacaoDto(

        @NotEmpty(message = "Email deve ser informado.")
        @Email(message = "Email com formato inválido.")
        String email,

        @NotEmpty(message = "Senha deve ser informada.")
        String senha
) {

}
