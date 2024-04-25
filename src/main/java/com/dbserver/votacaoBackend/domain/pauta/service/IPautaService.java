package com.dbserver.votacaoBackend.domain.pauta.service;

import java.util.List;


import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;
import java.time.LocalDateTime;

public interface IPautaService {
    public Pauta criarPauta(Pauta pauta);
    public List<Pauta> buscarPautasPorUsuarioId(Long usuarioId, Categoria categoria);
    public List<Pauta> buscarPautasAtivas(Categoria categoria, LocalDateTime dataAtual);
    public Pauta buscarPautaPorIdEUsuarioId(Long pautaId, Long usuarioId);
    public Pauta buscarPautaAtivaPorId(Long pautaId, LocalDateTime dataAtual);
    public Pauta buscarPautaPorIdEUsuarioIdComSessaoVotacaoNaoNula(Long pautaId, Long usuarioId);
}