package com.dbserver.votacaoBackend.domain.pauta.dto;

import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.StatusSessaoVotacao;

public record DetalhesPautaDto(
    RespostaPautaDto dadosPauta,
    StatusSessaoVotacao status
) {
    
}
