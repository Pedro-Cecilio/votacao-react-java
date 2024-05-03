package com.dbserver.votacaoBackend.domain.pauta.service;

import java.util.List;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;

public interface PautaService {
    public Pauta criarPauta(Pauta pauta);

    public List<Pauta> buscarPautasPorUsuarioId(Long usuarioId, Categoria categoria);

    public List<Pauta> buscarPautasAtivas(Categoria categoria);

    public Pauta buscarPautaPorIdEUsuarioId(Long pautaId, Long usuarioId);

    public Pauta buscarPautaAtivaPorId(Long pautaId);

    public Pauta buscarPautaPorIdEUsuarioIdComSessaoVotacaoNaoNula(Long pautaId, Long usuarioId);
}