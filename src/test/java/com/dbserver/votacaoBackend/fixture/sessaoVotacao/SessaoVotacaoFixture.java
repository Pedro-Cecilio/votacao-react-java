package com.dbserver.votacaoBackend.fixture.sessaoVotacao;

import java.time.LocalDateTime;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;


public class SessaoVotacaoFixture {
    public static SessaoVotacao sessaoVotacaoAtiva(Pauta pauta) {
        return SessaoVotacao.builder()
            .pauta(pauta)
            .dataAbertura(LocalDateTime.now())
            .dataFechamento(LocalDateTime.now().plusMinutes(5))
            .build();
    }
    public static SessaoVotacao sessaoVotacaoInativa(Pauta pauta) {
        return SessaoVotacao.builder()
            .pauta(pauta)
            .dataAbertura(LocalDateTime.now())
            .dataFechamento(LocalDateTime.now())
            .build();
    }


}
