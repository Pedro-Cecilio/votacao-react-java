package com.dbserver.votacaoBackend.domain.sessaoVotacao.dto;

import jakarta.validation.constraints.NotNull;

public record AbrirVotacaoDto(
    @NotNull(message = "Minutos deve ser informado.")
    Long minutos,
    @NotNull(message = "PautaId deve ser informada.")
    Long pautaId
) {
    
}
