package com.dbserver.votacaoBackend.domain.sessaoVotacao.service;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.StatusSessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.TipoDeVotoEnum;
import com.dbserver.votacaoBackend.domain.voto.Voto;

public interface SessaoVotacaoService {
        SessaoVotacao abrirVotacao(Pauta pauta, Long minutos);

        void verificarSeUsuarioPodeVotarSessaoVotacao(SessaoVotacao sessaoVotacao, Voto voto);

        SessaoVotacao inserirVotoInterno(Voto voto, Long pautaId,
                        TipoDeVotoEnum tipoDeVoto);

        SessaoVotacao inserirVotoExterno(Voto voto, Long pautaId, TipoDeVotoEnum tipoDeVoto, String cpf,
                        String senha);

        StatusSessaoVotacao obterStatusSessaoVotacao(SessaoVotacao sessaoVotacao);

        void verificarSePodeVotarExternamente(String cpf, String senha);

        SessaoVotacao buscarSessaoVotacaoAtiva(Long pautaId);
}
