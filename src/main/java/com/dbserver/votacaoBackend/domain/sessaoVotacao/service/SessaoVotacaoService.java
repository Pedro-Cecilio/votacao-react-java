package com.dbserver.votacaoBackend.domain.sessaoVotacao.service;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.StatusSessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.TipoDeVotoEnum;
import com.dbserver.votacaoBackend.domain.voto.Voto;

public interface SessaoVotacaoService {
        public SessaoVotacao abrirVotacao(Pauta pauta, Long minutos);

        public void verificarSeUsuarioPodeVotarSessaoVotacao(SessaoVotacao sessaoVotacao, Voto voto);

        public SessaoVotacao inserirVotoInterno(Voto voto, Long pautaId,
                        TipoDeVotoEnum tipoDeVoto);

        public SessaoVotacao inserirVotoExterno(Voto voto, Long pautaId, TipoDeVotoEnum tipoDeVoto, String cpf,
                        String senha);

        public StatusSessaoVotacao obterStatusSessaoVotacao(SessaoVotacao sessaoVotacao);

        public void verificarSePodeVotarExternamente(String cpf, String senha);

        public SessaoVotacao buscarSessaoVotacaoAtiva(Long pautaId);
}
