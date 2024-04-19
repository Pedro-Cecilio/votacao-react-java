package com.dbserver.votacaoBackend.domain.sessaoVotacao.dto;

public record AbrirVotacaoDto(
    Long minutos,
    Long pauta_id
) {
    
}
