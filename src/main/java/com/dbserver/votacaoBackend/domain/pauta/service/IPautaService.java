package com.dbserver.votacaoBackend.domain.pauta.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;

public interface IPautaService {
    public Pauta criarPauta(Pauta pauta);
    public List<Pauta> buscarPautasPorUsuarioId(Long usuarioId, Categoria categoria, Pageable pageable);
    public List<Pauta> buscarPautasAtivas(Pageable pageable, Categoria categoria);
    public Pauta buscarPautaPorIdEUsuarioId(Long pautaId, Long usuarioId);
    public Pauta buscarPautaAtivaPorId(Long pautaId);
    public Pauta buscarPautaPorIdEUsuarioIdComSessaoVotacaoNaoNula(Long pautaId, Long usuarioId);
}