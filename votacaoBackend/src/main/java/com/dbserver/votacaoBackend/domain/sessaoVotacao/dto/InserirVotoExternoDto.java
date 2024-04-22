package com.dbserver.votacaoBackend.domain.sessaoVotacao.dto;

import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.TipoDeVotoEnum;
import com.dbserver.votacaoBackend.utils.Utils;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record InserirVotoExternoDto(
    @NotNull(message = "Id da pauta deve ser informado.")
    Long pautaId,
    
    @NotNull(message = "O tipo do voto deve ser informado.")
    TipoDeVotoEnum tipoDeVoto,

    @Pattern(regexp = Utils.REGEX_CPF, message = "Cpf deve conter 11 caracteres numéricos.")
    String cpf
) {
    
}
