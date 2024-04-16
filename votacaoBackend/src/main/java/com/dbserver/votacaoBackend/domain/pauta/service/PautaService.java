package com.dbserver.votacaoBackend.domain.pauta.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.repository.PautaRepository;

import jakarta.transaction.Transactional;

@Service
public class PautaService implements IPautaService{
    private PautaRepository pautaRepository;
    public PautaService(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    @Transactional
    @Override
    public Pauta criarPauta(Pauta pauta) {
        return this.pautaRepository.save(pauta);
    }

    @Override
    public List<Pauta> buscarPautasPorUsuarioId(Long usuarioId, Pageable pageable) {
        return this.pautaRepository.findAllByUsuarioId(usuarioId, pageable).toList();
    }

    @Override
    public List<Pauta> buscarTodasPautas(Pageable pageable) {
        return this.pautaRepository.findAll(pageable).toList();
    }
    
}
