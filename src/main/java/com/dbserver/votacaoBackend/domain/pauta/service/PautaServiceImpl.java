package com.dbserver.votacaoBackend.domain.pauta.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;
import com.dbserver.votacaoBackend.domain.pauta.repository.PautaRepository;
import com.dbserver.votacaoBackend.utils.Utils;

import jakarta.transaction.Transactional;

@Service
public class PautaServiceImpl implements PautaService {
    private PautaRepository pautaRepository;
    private Utils utils;

    public PautaServiceImpl(PautaRepository pautaRepository, Utils utils) {
        this.pautaRepository = pautaRepository;
        this.utils = utils;
    }

    @Transactional
    @Override
    public Pauta criarPauta(Pauta pauta) {
        if(pauta == null) throw new IllegalArgumentException("Pauta não deve ser nula.");

        return this.pautaRepository.save(pauta);
    }

    @Override
    public List<Pauta> buscarPautasPorUsuarioId(Long usuarioId, Categoria categoria) {
        if(usuarioId == null) throw new IllegalArgumentException("Id do usuário não deve ser nulo.");

        if (categoria != null) {
            return this.pautaRepository.findAllByUsuarioIdAndCategoria(usuarioId, categoria);
        }

        return this.pautaRepository.findAllByUsuarioId(usuarioId);
    }

    @Override
    public List<Pauta> buscarPautasAtivas(Categoria categoria) {
        LocalDateTime dataAtual = this.utils.obterDataAtual();

        if (categoria != null) {
            return this.pautaRepository.findAllByCategoriaAndSessaoVotacaoAtiva(categoria, dataAtual);
        }

        return this.pautaRepository.findAllBySessaoVotacaoAtiva(dataAtual);
    }
    
    @Override
    public Pauta buscarPautaPorIdEUsuarioId(Long pautaId, Long usuarioId) {
        return this.pautaRepository.findByIdAndUsuarioId(pautaId, usuarioId).orElseThrow(()-> new NoSuchElementException("Pauta não encontrada."));
    }

    @Override
    public Pauta buscarPautaAtivaPorId(Long pautaId) {
        LocalDateTime dataAtual = this.utils.obterDataAtual();
        
        return this.pautaRepository.findByIdAndSessaoVotacaoAtiva(pautaId, dataAtual).orElseThrow(()-> new NoSuchElementException("Pauta informada não possui sessão ativa."));
    }

    @Override
    public Pauta buscarPautaPorIdEUsuarioIdComSessaoVotacaoNaoNula(Long pautaId, Long usuarioId) {
        return this.pautaRepository.findByIdAndUsuarioIdAndSessaoVotacaoNotNull(pautaId, usuarioId).orElseThrow(()-> new NoSuchElementException("Pauta não encontrada."));
    }
    
}
