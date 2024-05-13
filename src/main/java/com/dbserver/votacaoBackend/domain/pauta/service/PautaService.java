package com.dbserver.votacaoBackend.domain.pauta.service;

import java.util.List;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.dto.CriarPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.dto.DetalhesPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.dto.RespostaPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;

public interface PautaService {
    RespostaPautaDto criarPauta(CriarPautaDto dto);

    List<RespostaPautaDto> buscarPautasUsuarioLogado(Categoria categoria);

    List<RespostaPautaDto> buscarPautasAtivas(Categoria categoria);

    Pauta buscarPautaPorIdEUsuarioId(Long pautaId, Long usuarioId);

    RespostaPautaDto buscarPautaAtivaPorId(Long pautaId);

    DetalhesPautaDto obterDetalhePautaSessaoVotacaoNaoNula(Long pautaId);

}