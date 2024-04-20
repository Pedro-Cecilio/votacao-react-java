package com.dbserver.votacaoBackend.domain.pauta.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;
import com.dbserver.votacaoBackend.domain.pauta.repository.PautaRepository;

import jakarta.transaction.Transactional;

@Service
public class PautaService implements IPautaService {
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
    public List<Pauta> buscarPautasPorUsuarioId(Long usuarioId, Categoria categoria, Pageable pageable) {
        if (categoria != null) {
            return this.pautaRepository.findAllByUsuarioIdAndCategoria(usuarioId, categoria, pageable);
        }
        return this.pautaRepository.findAllByUsuarioId(usuarioId, pageable);
    }

    @Override
    public List<Pauta> buscarPautasAtivas(Pageable pageable, Categoria categoria) {
        LocalDateTime dataAtual = LocalDateTime.now();

        if (categoria != null) {
            return this.pautaRepository.findAllByCategoriaAndSessaoVotacaoAtiva(categoria, dataAtual);
        }
        return this.pautaRepository.findAllBySessaoVotacaoAtiva(dataAtual);
    }
    
    @Override
    public Pauta buscarPautaPorIdEUsuarioId(Long pautaId, Long usuarioId) {
        return this.pautaRepository.findByIdAndUsuarioId(pautaId, usuarioId).orElseThrow(()-> new IllegalArgumentException("Usuário não possui essa pauta."));
    }

    public Pauta buscarPautaAtivaPorId(Long pautaId) {
        return this.pautaRepository.findByIdAndSessaoVotacaoAtiva(pautaId, LocalDateTime.now()).orElseThrow(()-> new IllegalArgumentException("Pauta informada não possui sessão ativa."));
    }

    
    
}
