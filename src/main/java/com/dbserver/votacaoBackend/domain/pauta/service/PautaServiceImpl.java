package com.dbserver.votacaoBackend.domain.pauta.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.dto.CriarPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.dto.DetalhesPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.dto.RespostaPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;
import com.dbserver.votacaoBackend.domain.pauta.mapper.PautaMapper;
import com.dbserver.votacaoBackend.domain.pauta.repository.PautaRepository;
import com.dbserver.votacaoBackend.domain.pauta.validacoes.PautaValidacoes;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.service.UsuarioServiceImpl;
import com.dbserver.votacaoBackend.utils.Utils;

import jakarta.transaction.Transactional;

@Service
public class PautaServiceImpl implements PautaService {
    private PautaRepository pautaRepository;
    private PautaValidacoes pautaValidacoes;
    private UsuarioServiceImpl usuarioService;
    private Utils utils;
    private PautaMapper pautaMapper;

    public PautaServiceImpl(PautaRepository pautaRepository, PautaValidacoes pautaValidacoes,
            UsuarioServiceImpl usuarioService, Utils utils, PautaMapper pautaMapper) {
        this.pautaRepository = pautaRepository;
        this.utils = utils;
        this.pautaValidacoes = pautaValidacoes;
        this.usuarioService = usuarioService;
        this.pautaMapper = pautaMapper;
    }

    @Transactional
    @Override
    public RespostaPautaDto criarPauta(CriarPautaDto dto) {
        this.pautaValidacoes.validarCriarPautaDtoNaoNula(dto);

        Usuario usuario = this.usuarioService.buscarUsuarioLogado();
        Pauta pauta = new Pauta(dto.assunto(), dto.categoria(), usuario);

        this.pautaRepository.save(pauta);

        return pautaMapper.toRespostaPautaDto(pauta);
    }

    @Override
    public List<RespostaPautaDto> buscarPautasUsuarioLogado(Categoria categoria) {
        Usuario usuario = usuarioService.buscarUsuarioLogado();
        List<Pauta> pautas;
        if (categoria != null) {
            pautas = this.pautaRepository.findAllByUsuarioIdAndCategoriaOrderByCreatedAtDesc(usuario.getId(),
                    categoria);
        } else {
            pautas = this.pautaRepository.findAllByUsuarioIdOrderByCreatedAtDesc(usuario.getId());
        }

        return pautaMapper.toListRespostaPautaDto(pautas);
    }

    @Override
    public List<RespostaPautaDto> buscarPautasAtivas(Categoria categoria) {
        LocalDateTime dataAtual = this.utils.obterDataAtual();
        List<Pauta> pautas;

        if (categoria != null) {
            pautas = this.pautaRepository.findAllByCategoriaAndSessaoVotacaoAtiva(categoria, dataAtual);
        } else {
            pautas = this.pautaRepository.findAllBySessaoVotacaoAtiva(dataAtual);
        }

        return pautaMapper.toListRespostaPautaDto(pautas);
    }

    @Override
    public Pauta buscarPautaPorIdEUsuarioId(Long pautaId, Long usuarioId) {
        return this.pautaRepository.findByIdAndUsuarioId(pautaId, usuarioId)
                .orElseThrow(() -> new NoSuchElementException("Pauta não encontrada."));
    }

    @Override
    public RespostaPautaDto buscarPautaAtivaPorId(Long pautaId) {
        LocalDateTime dataAtual = this.utils.obterDataAtual();

        Pauta pauta = this.pautaRepository.findByIdAndSessaoVotacaoAtiva(pautaId, dataAtual)
                .orElseThrow(() -> new NoSuchElementException("Pauta informada não possui sessão ativa."));

        return pautaMapper.toRespostaPautaDto(pauta);
    }

    @Override
    public Pauta bsucarPautaPorId(Long id){
        return this.pautaRepository.findById(id).orElseThrow(()-> new NoSuchElementException("Pauta não encontrada"));
    }
    
    @Override
    public DetalhesPautaDto obterDetalhePautaSessaoVotacaoNaoNula(Long pautaId) {
        Usuario usuario = usuarioService.buscarUsuarioLogado();
        Pauta pauta = this.pautaRepository.findByIdAndUsuarioIdAndSessaoVotacaoNotNull(pautaId, usuario.getId())
                .orElseThrow(() -> new NoSuchElementException("Pauta não encontrada."));
        return pautaMapper.toDetalhesPautaDto(pauta);
    }
}
