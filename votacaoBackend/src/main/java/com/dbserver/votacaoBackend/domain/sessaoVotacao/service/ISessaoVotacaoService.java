package com.dbserver.votacaoBackend.domain.sessaoVotacao.service;

import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;

public interface ISessaoVotacaoService {
    public SessaoVotacao abrirVotacao(SessaoVotacao sessaoVotacao);
    public boolean verificarSeSessaoVotacaoEstaAtiva(SessaoVotacao sessaoVotacao);
}
