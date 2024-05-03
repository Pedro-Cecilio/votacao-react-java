package com.dbserver.votacaoBackend.domain.sessaoVotacao.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dbserver.votacaoBackend.domain.autenticacao.service.AutenticacaoService;
import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.StatusSessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.TipoDeVotoEnum;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.repository.SessaoVotacaoRepository;
import com.dbserver.votacaoBackend.domain.usuario.service.UsuarioService;
import com.dbserver.votacaoBackend.domain.voto.Voto;
import com.dbserver.votacaoBackend.utils.Utils;

@Service
public class SessaoVotacaoService implements ISessaoVotacaoService {
    private SessaoVotacaoRepository sessaoVotacaoRepository;
    private UsuarioService usuarioService;
    private AutenticacaoService autenticacaoService;
    private Utils utils;

    public SessaoVotacaoService(SessaoVotacaoRepository sessaoVotacaoRepository, UsuarioService usuarioService,
            AutenticacaoService autenticacaoService, Utils utils) {
        this.sessaoVotacaoRepository = sessaoVotacaoRepository;
        this.usuarioService = usuarioService;
        this.autenticacaoService = autenticacaoService;
        this.utils = utils;
    }

    @Override
    public SessaoVotacao abrirVotacao(Pauta pauta, Long minutos) {
        if (pauta == null)
            throw new IllegalArgumentException("Pauta deve ser informada");
        if (minutos == null)
            throw new IllegalArgumentException("Minutos devem ser informados");
        if (pauta.getSessaoVotacao() != null)
            throw new IllegalStateException("Pauta já possui uma votação aberta.");

        LocalDateTime dataAbertura = this.utils.obterDataAtual();

        LocalDateTime dataFechamento = dataAbertura.plusMinutes(minutos);

        SessaoVotacao sessaoVotacao = new SessaoVotacao(pauta, dataAbertura, dataFechamento);

        return this.sessaoVotacaoRepository.save(sessaoVotacao);
    }

    // @Override
    // public boolean verificarSeSessaoVotacaoEstaAtiva(SessaoVotacao sessaoVotacao)
    // {
    // if (sessaoVotacao == null)
    // throw new IllegalArgumentException("SessaoVotacao não deve ser nula");
    // return sessaoVotacao.getDataFechamento().isAfter(LocalDateTime.now());
    // }

    @Override
    public void verificarSeUsuarioPodeVotarSessaoVotacao(SessaoVotacao sessaoVotacao, Voto voto) {
        if (sessaoVotacao == null)
            throw new IllegalArgumentException("SessaoVotacao não deve ser nula.");

        if (voto == null)
            throw new IllegalArgumentException("Voto não deve ser nulo.");
        if (!sessaoVotacao.isAtiva())
            throw new IllegalStateException("Sessão de votação não está ativa.");
        if (sessaoVotacao.getPauta().getUsuario().getCpf().equals(voto.getCpf()))
            throw new IllegalArgumentException("O criador não pode votar na pauta criada.");
        List<Voto> todosVotantes = new ArrayList<>(sessaoVotacao.getVotosPositivos());
        todosVotantes.addAll(sessaoVotacao.getVotosNegativos());
        if (todosVotantes.contains(voto))
            throw new IllegalStateException("Não é possível votar duas vezes.");
    }

    @Override
    public SessaoVotacao inserirVotoInterno(Voto voto, Long pautaId,
            TipoDeVotoEnum tipoDeVoto) {

        SessaoVotacao sessaoVotacao = this.buscarSessaoVotacaoAtiva(pautaId);

        this.verificarSeUsuarioPodeVotarSessaoVotacao(sessaoVotacao, voto);

        if (tipoDeVoto == null)
            throw new IllegalArgumentException("O tipo do voto deve ser informado.");

        if (tipoDeVoto == TipoDeVotoEnum.VOTO_NEGATIVO)
            sessaoVotacao.setVotosNegativos(voto);

        if (tipoDeVoto == TipoDeVotoEnum.VOTO_POSITIVO)
            sessaoVotacao.setVotosPositivos(voto);

        return this.sessaoVotacaoRepository.save(sessaoVotacao);
    }

    @Override
    public SessaoVotacao inserirVotoExterno(Voto voto, Long pautaId, TipoDeVotoEnum tipoDeVoto, String cpf,
            String senha) {

        SessaoVotacao sessaoVotacao = this.buscarSessaoVotacaoAtiva(pautaId);

        this.verificarSePodeVotarExternamente(cpf,
                senha);
        this.verificarSeUsuarioPodeVotarSessaoVotacao(sessaoVotacao, voto);

        if (tipoDeVoto == null)
            throw new IllegalArgumentException("O tipo do voto deve ser informado.");

        if (tipoDeVoto == TipoDeVotoEnum.VOTO_NEGATIVO)
            sessaoVotacao.setVotosNegativos(voto);

        if (tipoDeVoto == TipoDeVotoEnum.VOTO_POSITIVO)
            sessaoVotacao.setVotosPositivos(voto);

        return this.sessaoVotacaoRepository.save(sessaoVotacao);
    }

    @Override
    public StatusSessaoVotacao obterStatusSessaoVotacao(SessaoVotacao sessaoVotacao) {
        if (sessaoVotacao == null)
            throw new IllegalArgumentException("SessaoVotacao não deve ser nula.");
        if (sessaoVotacao.getDataFechamento().isAfter(LocalDateTime.now())) {
            return StatusSessaoVotacao.EM_ANDAMENTO;
        }
        if (sessaoVotacao.getVotosPositivos().size() > sessaoVotacao.getVotosNegativos().size()) {
            return StatusSessaoVotacao.APROVADA;
        }
        return StatusSessaoVotacao.REPROVADA;
    }

    @Override
    public void verificarSePodeVotarExternamente(String cpf, String senha) {
        boolean existe = this.usuarioService.verificarSeExisteUsuarioPorCpf(cpf);
        if (existe)
            this.autenticacaoService.validarAutenticacaoPorCpfESenha(cpf, senha);
    }

    @Override
    public SessaoVotacao buscarSessaoVotacaoAtiva(Long pautaId) {
        LocalDateTime dataAtual = utils.obterDataAtual();

        return this.sessaoVotacaoRepository.findByPautaIdAndSessaoVotacaoAtiva(pautaId, dataAtual)
                .orElseThrow(() -> new IllegalArgumentException("Pauta não possui sessão ativa."));
    }
}