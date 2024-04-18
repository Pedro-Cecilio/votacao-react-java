package com.dbserver.votacaoBackend.domain.sessaoVotacao.dto;

import java.time.LocalDateTime;

import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;

public record RespostaSessaoVotacao(

        Long id,
        Long pauta_id,

        int votosPositivos,

        int votosNegativos,

        LocalDateTime dataAbertura,

        LocalDateTime dataFechamento) {
    public RespostaSessaoVotacao(SessaoVotacao sessaoVotacao){
        this(sessaoVotacao.getId(), sessaoVotacao.getPauta().getId(), sessaoVotacao.getVotosPositivos().size(), sessaoVotacao.getVotosNegativos().size(), sessaoVotacao.getDataAbertura(), sessaoVotacao.getDataFechamento());
    }
}
