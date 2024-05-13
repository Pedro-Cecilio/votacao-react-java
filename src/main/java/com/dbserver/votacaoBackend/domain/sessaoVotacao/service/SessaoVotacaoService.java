package com.dbserver.votacaoBackend.domain.sessaoVotacao.service;

import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.AbrirVotacaoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoExternoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoInternoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.RespostaSessaoVotacaoDto;

public interface SessaoVotacaoService {
        RespostaSessaoVotacaoDto abrirVotacao(AbrirVotacaoDto dto);

        RespostaSessaoVotacaoDto inserirVotoInterno(InserirVotoInternoDto dto);

        RespostaSessaoVotacaoDto inserirVotoExterno(InserirVotoExternoDto dto);

}
