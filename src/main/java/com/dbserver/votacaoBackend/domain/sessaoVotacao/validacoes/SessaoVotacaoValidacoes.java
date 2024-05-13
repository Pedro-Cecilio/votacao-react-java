package com.dbserver.votacaoBackend.domain.sessaoVotacao.validacoes;

import org.springframework.stereotype.Component;

import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;

@Component
public class SessaoVotacaoValidacoes {

    public void validarSessaoVotacaoNaoNula(SessaoVotacao sessaoVotacao) {
        if (sessaoVotacao == null)
            throw new IllegalArgumentException("SessaoVotacao não deve ser nula.");
    }

    public void validarSessaoVotacaoAtiva(SessaoVotacao sessaoVotacao) {
        if (!sessaoVotacao.isAtiva())
            throw new IllegalStateException("Sessão de votação não está ativa.");
    }
}
