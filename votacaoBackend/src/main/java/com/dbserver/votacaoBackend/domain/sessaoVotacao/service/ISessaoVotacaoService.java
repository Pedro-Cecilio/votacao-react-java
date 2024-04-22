package com.dbserver.votacaoBackend.domain.sessaoVotacao.service;

import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.StatusSessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.TipoDeVotoEnum;
import com.dbserver.votacaoBackend.domain.voto.Voto;

public interface ISessaoVotacaoService {
    public SessaoVotacao abrirVotacao(SessaoVotacao sessaoVotacao);

    public boolean verificarSeSessaoVotacaoEstaAtiva(SessaoVotacao sessaoVotacao);

    public void verificarSeUsuarioPodeVotarSessaoVotacao(SessaoVotacao sessaoVotacao, Voto voto);

    public SessaoVotacao inserirVoto(SessaoVotacao sessaoVotacao, TipoDeVotoEnum tipoDeVoto, Voto voto);

    public StatusSessaoVotacao obterStatusSessaoVotacao(SessaoVotacao sessaoVotacao);

}
