package com.dbserver.votacaoBackend.domain.pauta.service;

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
    
}
