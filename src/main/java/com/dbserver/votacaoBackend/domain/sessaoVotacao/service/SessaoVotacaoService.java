package com.dbserver.votacaoBackend.domain.sessaoVotacao.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dbserver.votacaoBackend.domain.autenticacao.service.AutenticacaoService;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.StatusSessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.TipoDeVotoEnum;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.repository.SessaoVotacaoRepository;
import com.dbserver.votacaoBackend.domain.usuario.service.UsuarioService;
import com.dbserver.votacaoBackend.domain.voto.Voto;

@Service
public class SessaoVotacaoService implements ISessaoVotacaoService {
    private SessaoVotacaoRepository sessaoVotacaoRepository;
    private UsuarioService usuarioService;
    private AutenticacaoService autenticacaoService;

    public SessaoVotacaoService(SessaoVotacaoRepository sessaoVotacaoRepository, UsuarioService usuarioService,
            AutenticacaoService autenticacaoService) {
        this.sessaoVotacaoRepository = sessaoVotacaoRepository;
        this.usuarioService = usuarioService;
        this.autenticacaoService = autenticacaoService;
    }

    @Override
    public SessaoVotacao abrirVotacao(SessaoVotacao sessaoVotacao) {
        if (sessaoVotacao == null)
            throw new IllegalArgumentException("SessaoVotacao não deve ser nula.");
        if (sessaoVotacao.getPauta().getSessaoVotacao() != null)
            throw new IllegalStateException("Pauta já possui uma votação aberta.");

        return this.sessaoVotacaoRepository.save(sessaoVotacao);
    }

    @Override
    public boolean verificarSeSessaoVotacaoEstaAtiva(SessaoVotacao sessaoVotacao) {
        if (sessaoVotacao == null)
            throw new IllegalArgumentException("SessaoVotacao não deve ser nula");
        return sessaoVotacao.getDataFechamento().isAfter(LocalDateTime.now());
    }

    @Override
    public void verificarSeUsuarioPodeVotarSessaoVotacao(SessaoVotacao sessaoVotacao, Voto voto) {
        if (sessaoVotacao == null)
            throw new IllegalArgumentException("SessaoVotacao não deve ser nula.");

        if (voto == null)
            throw new IllegalArgumentException("Voto não deve ser nulo.");
        if (!this.verificarSeSessaoVotacaoEstaAtiva(sessaoVotacao))
            throw new IllegalStateException("Sessão de votação não está ativa.");
        if (sessaoVotacao.getPauta().getUsuario().getCpf().equals(voto.getCpf()))
            throw new IllegalArgumentException("O criador não pode votar na pauta criada.");
        List<Voto> todosVotantes = new ArrayList<>(sessaoVotacao.getVotosPositivos());
        todosVotantes.addAll(sessaoVotacao.getVotosNegativos());
        if (todosVotantes.contains(voto))
            throw new IllegalStateException("Não é possível votar duas vezes.");
    }

    @Override
    public SessaoVotacao inserirVoto(SessaoVotacao sessaoVotacao, TipoDeVotoEnum tipoDeVoto, Voto voto) {
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

    public void verificarSePodeVotarExternamente(String cpf, String senha) {
        boolean existe = this.usuarioService.verificarSeExisteUsuarioPorCpf(cpf);
        if (existe)
            this.autenticacaoService.validarAutenticacaoPorCpfESenha(cpf, senha);

    }
}