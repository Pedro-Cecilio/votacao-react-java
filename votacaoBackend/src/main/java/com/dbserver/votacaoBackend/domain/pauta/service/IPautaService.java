package com.dbserver.votacaoBackend.domain.pauta.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;

public interface IPautaService {
    public Pauta criarPauta(Pauta pauta);
    public List<Pauta> buscarPautasPorUsuarioId(Long usuarioId, Pageable pageable);
    public List<Pauta> buscarPautas(Pageable pageable, Categoria categoria);
}