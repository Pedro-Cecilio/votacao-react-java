package com.dbserver.votacaoBackend.domain.sessaoVotacao.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.service.PautaServiceImpl;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.AbrirVotacaoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoExternoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoInternoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.RespostaSessaoVotacaoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.TipoDeVotoEnum;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.mapper.SessaoVotacaoMapper;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.repository.SessaoVotacaoRepository;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.validacoes.SessaoVotacaoValidacoes;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.service.UsuarioServiceImpl;
import com.dbserver.votacaoBackend.domain.voto.Voto;
import com.dbserver.votacaoBackend.domain.voto.mapper.VotoMapper;
import com.dbserver.votacaoBackend.utils.Utils;

@Service
public class SessaoVotacaoServiceImpl implements SessaoVotacaoService {
    private SessaoVotacaoRepository sessaoVotacaoRepository;
    private UsuarioServiceImpl usuarioService;
    private PautaServiceImpl pautaService;
    private Utils utils;
    private SessaoVotacaoMapper sessaoVotacaoMapper;
    private SessaoVotacaoValidacoes sessaoVotacaoValidacoes;
    private VotoMapper votoMapper;

    public SessaoVotacaoServiceImpl(SessaoVotacaoRepository sessaoVotacaoRepository,
            UsuarioServiceImpl usuarioService,
            Utils utils,
            PautaServiceImpl pautaService,
            SessaoVotacaoMapper sessaoVotacaoMapper,
            SessaoVotacaoValidacoes sessaoVotacaoValidacoes,
            VotoMapper votoMapper) {
        this.sessaoVotacaoRepository = sessaoVotacaoRepository;
        this.usuarioService = usuarioService;
        this.utils = utils;
        this.pautaService = pautaService;
        this.sessaoVotacaoMapper = sessaoVotacaoMapper;
        this.sessaoVotacaoValidacoes = sessaoVotacaoValidacoes;
        this.votoMapper = votoMapper;
    }

    @Override
    public RespostaSessaoVotacaoDto abrirVotacao(AbrirVotacaoDto dto) {
        Usuario usuario = this.usuarioService.buscarUsuarioLogado();

        Pauta pauta = this.pautaService.buscarPautaPorIdEUsuarioId(dto.pautaId(), usuario.getId());

        if (pauta.getSessaoVotacao() != null)
            throw new IllegalStateException("Pauta já possui uma votação aberta.");

        LocalDateTime dataAbertura = this.utils.obterDataAtual();

        LocalDateTime dataFechamento = dataAbertura.plusMinutes(dto.minutos());

        SessaoVotacao sessaoVotacao = sessaoVotacaoMapper.toSessaoVotacao(pauta, dataAbertura, dataFechamento);

        this.sessaoVotacaoRepository.save(sessaoVotacao);

        return sessaoVotacaoMapper.toRespostaSessaoVotacaoDto(sessaoVotacao);
    }

    @Override
    public RespostaSessaoVotacaoDto inserirVotoInterno(InserirVotoInternoDto dto) {
        Usuario usuario = this.usuarioService.buscarUsuarioLogado();
        SessaoVotacao sessaoVotacao = this.buscarSessaoVotacaoAtivaPorPautaId(dto.pautaId());

        return inserirVoto(usuario.getCpf(), usuario, sessaoVotacao, dto.tipoDeVoto());
    }

    @Override
    public RespostaSessaoVotacaoDto inserirVotoExterno(InserirVotoExternoDto dto) {
        Usuario usuario = this.usuarioService.buscarUsuarioPorCpfSeHouver(dto.cpf());
        SessaoVotacao sessaoVotacao = this.buscarSessaoVotacaoAtivaPorPautaId(dto.pautaId());
        this.sessaoVotacaoValidacoes.validarSePodeVotarExternamente(dto.cpf(),
                dto.senha());

        return inserirVoto(dto.cpf(), usuario, sessaoVotacao, dto.tipoDeVoto());
    }

    private RespostaSessaoVotacaoDto inserirVoto(String cpf, Usuario usuario, SessaoVotacao sessaoVotacao, TipoDeVotoEnum tipoDeVoto){
        Voto voto = votoMapper.toVoto(cpf, usuario);

        SessaoVotacaoValidacoes.validarSeUsuarioPodeVotarSessaoVotacao(sessaoVotacao, voto);

        inserirVotoPorTipoDeVoto(sessaoVotacao, voto, tipoDeVoto);

        this.sessaoVotacaoRepository.save(sessaoVotacao);

        return sessaoVotacaoMapper.toRespostaSessaoVotacaoDto(sessaoVotacao);
    }

    @Override
    public SessaoVotacao buscarSessaoVotacaoAtivaPorPautaId(Long pautaId) {
        LocalDateTime dataAtual = utils.obterDataAtual();

        return this.sessaoVotacaoRepository.findByPautaIdAndSessaoVotacaoAtiva(pautaId, dataAtual)
                .orElseThrow(() -> new IllegalArgumentException("Pauta não possui sessão ativa."));
    }

    @Override
    public SessaoVotacao inserirVotoPorTipoDeVoto(SessaoVotacao sessaoVotacao, Voto voto, TipoDeVotoEnum tipoDeVoto) {
        if (tipoDeVoto == null)
            throw new IllegalArgumentException("O tipo do voto deve ser informado.");

        if (tipoDeVoto == TipoDeVotoEnum.VOTO_NEGATIVO)
            sessaoVotacao.setVotosNegativos(voto);

        if (tipoDeVoto == TipoDeVotoEnum.VOTO_POSITIVO)
            sessaoVotacao.setVotosPositivos(voto);

        return sessaoVotacao;
    }

}