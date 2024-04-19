package com.dbserver.votacaoBackend.domain.sessaoVotacao.dto;

import java.time.LocalDateTime;

import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;

public record RespostaSessaoVotacaoDto(

        Long id,
        Long pautaId,

        int votosPositivos,

        int votosNegativos,

        LocalDateTime dataAbertura,

        LocalDateTime dataFechamento) {
    public RespostaSessaoVotacaoDto(SessaoVotacao sessaoVotacao){
        this(sessaoVotacao.getId(), sessaoVotacao.getPauta().getId(), sessaoVotacao.getVotosPositivos().size(), sessaoVotacao.getVotosNegativos().size(), sessaoVotacao.getDataAbertura(), sessaoVotacao.getDataFechamento());
    }
}