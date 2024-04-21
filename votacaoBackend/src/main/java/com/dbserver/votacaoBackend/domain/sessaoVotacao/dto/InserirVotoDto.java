package com.dbserver.votacaoBackend.domain.sessaoVotacao.dto;

import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.TipoDeVotoEnum;

import jakarta.validation.constraints.NotNull;

public record InserirVotoDto(


        
    @NotNull(message = "Id da pauta deve ser informado.")
    Long pautaId,

    @NotNull(message = "Id do usuario deve ser informado.")
    Long usuarioId,
    
    @NotNull(message = "O tipo do voto deve ser informado.")
    TipoDeVotoEnum tipoDeVoto
) {
    
}
