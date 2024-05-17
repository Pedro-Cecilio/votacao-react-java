package com.dbserver.votacaoBackend.fixture.sessaoVotacao;

import java.time.LocalDateTime;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;


public class SessaoVotacaoFixture {
    public static SessaoVotacao sessaoVotacaoAtiva(Pauta pauta) {
        return new SessaoVotacao(pauta, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5));
    }
    public static SessaoVotacao sessaoVotacaoInativa(Pauta pauta) {
        return new SessaoVotacao(pauta, LocalDateTime.now(),
                LocalDateTime.now());
    }

}
