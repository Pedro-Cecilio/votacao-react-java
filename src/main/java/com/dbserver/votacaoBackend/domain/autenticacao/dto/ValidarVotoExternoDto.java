package com.dbserver.votacaoBackend.domain.autenticacao.dto;

import com.dbserver.votacaoBackend.utils.Utils;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record ValidarVotoExternoDto(
        @Pattern(regexp = Utils.REGEX_CPF, message = "Cpf deve conter 11 caracteres numéricos.") String cpf,

        @NotEmpty(message = "Senha deve ser informada.") String senha

) {

}
