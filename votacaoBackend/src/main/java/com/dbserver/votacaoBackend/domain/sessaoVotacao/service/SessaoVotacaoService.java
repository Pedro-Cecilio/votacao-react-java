package com.dbserver.votacaoBackend.domain.sessaoVotacao.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.repository.SessaoVotacaoRepository;

@Service
public class SessaoVotacaoService implements ISessaoVotacaoService{
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    public SessaoVotacaoService(SessaoVotacaoRepository sessaoVotacaoRepository) {
        this.sessaoVotacaoRepository = sessaoVotacaoRepository;
    }

    @Override
    public SessaoVotacao abrirVotacao(SessaoVotacao sessaoVotacao){
        if(sessaoVotacao == null) throw new IllegalArgumentException("SessaoVotacao não deve ser nula.");
        if(sessaoVotacao.getPauta().getSessaoVotacao() != null) throw new IllegalStateException("Pauta já possui uma votação aberta.");
        
        return this.sessaoVotacaoRepository.save(sessaoVotacao);
    }

    @Override
    public boolean verificarSeSessaoVotacaoEstaAtiva(SessaoVotacao sessaoVotacao){
        return sessaoVotacao.getDataFechamento().isAfter(LocalDateTime.now());
    }
}
