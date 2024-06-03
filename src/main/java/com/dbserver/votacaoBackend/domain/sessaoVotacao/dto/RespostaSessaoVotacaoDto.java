package com.dbserver.votacaoBackend.domain.sessaoVotacao.dto;

import java.time.LocalDateTime;


public record RespostaSessaoVotacaoDto(

        Long id,
        Long pautaId,

        int votosPositivos,

        int votosNegativos,

        LocalDateTime dataAbertura,

        LocalDateTime dataFechamento,
        
        boolean sessaoAtiva
        ) {
}