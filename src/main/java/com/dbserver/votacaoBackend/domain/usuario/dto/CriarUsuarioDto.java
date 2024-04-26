package com.dbserver.votacaoBackend.domain.usuario.dto;

import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutenticacaoDto;
import com.dbserver.votacaoBackend.utils.Utils;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CriarUsuarioDto(

    @NotNull(message = "AutenticacaoDto deve ser informado.")
    AutenticacaoDto autenticacaoDto,
    
    @NotEmpty(message = "Nome deve ser informado.")
    String nome,

    @NotEmpty(message = "Sobrenome deve ser informado.")
    String sobrenome,

    @Pattern(regexp = Utils.REGEX_CPF, message = "Cpf deve conter 11 caracteres numéricos.")
    String cpf,

    @NotNull(message = "Admin deve ser informado.")
    boolean admin

) {
    
}
