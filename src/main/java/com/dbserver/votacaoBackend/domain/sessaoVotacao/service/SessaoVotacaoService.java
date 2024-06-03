package com.dbserver.votacaoBackend.domain.sessaoVotacao.service;

import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.AbrirVotacaoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoExternoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoInternoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.RespostaSessaoVotacaoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.TipoDeVotoEnum;
import com.dbserver.votacaoBackend.domain.voto.Voto;

public interface SessaoVotacaoService {
        RespostaSessaoVotacaoDto abrirVotacao(AbrirVotacaoDto dto);

        RespostaSessaoVotacaoDto inserirVotoInterno(InserirVotoInternoDto dto);

        RespostaSessaoVotacaoDto inserirVotoExterno(InserirVotoExternoDto dto);

        SessaoVotacao buscarSessaoVotacaoAtivaPorPautaId(Long pautaId);

        SessaoVotacao inserirVotoPorTipoDeVoto(SessaoVotacao sessaoVotacao, Voto voto, TipoDeVotoEnum tipoDeVoto);
}
