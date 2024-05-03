package com.dbserver.votacaoBackend.domain.pauta.service;

import java.util.List;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;

public interface PautaService {
    Pauta criarPauta(Pauta pauta);

    List<Pauta> buscarPautasPorUsuarioId(Long usuarioId, Categoria categoria);

    List<Pauta> buscarPautasAtivas(Categoria categoria);

    Pauta buscarPautaPorIdEUsuarioId(Long pautaId, Long usuarioId);

    Pauta buscarPautaAtivaPorId(Long pautaId);

    Pauta buscarPautaPorIdEUsuarioIdComSessaoVotacaoNaoNula(Long pautaId, Long usuarioId);
}