package com.dbserver.votacaoBackend.fixture.sessaoVotacao;

import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.AbrirVotacaoDto;

public class AbrirVotacaoDtoFixture {

    public static AbrirVotacaoDto abrirVotacaoDtoValido(Long pautaId) {
        return new AbrirVotacaoDto(10L, pautaId);
    }

    public static AbrirVotacaoDto abrirVotacaoDto() {
        return new AbrirVotacaoDto(5L, 1L);
    }
}
