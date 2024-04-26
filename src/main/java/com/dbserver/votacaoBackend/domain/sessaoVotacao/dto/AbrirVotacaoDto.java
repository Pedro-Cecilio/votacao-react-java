package com.dbserver.votacaoBackend.domain.sessaoVotacao.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AbrirVotacaoDto(
    @NotNull(message = "Minutos deve ser informado.")
    @Positive(message = "Minutos deve ser maior que 0.")
    Long minutos,
    @NotNull(message = "PautaId deve ser informada.")
    Long pautaId
) {
    
}
