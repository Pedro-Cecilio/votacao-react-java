package com.dbserver.votacaoBackend.domain.autenticacao.service;

import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.repository.AutenticacaoRepository;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.infra.security.token.TokenService;
import com.dbserver.votacaoBackend.utils.Utils;


@Service
public class AutenticacaoService implements IAutenticacaoService {
    private AutenticacaoRepository autenticacaoRepository;
    private Utils utils;
    private TokenService tokenService;

    public AutenticacaoService(AutenticacaoRepository autenticacaoRepository, Utils utils, TokenService tokenService) {
        this.autenticacaoRepository = autenticacaoRepository;
        this.utils = utils;
        this.tokenService = tokenService;
    }

    @Override
    public Autenticacao criarAutenticacao(Autenticacao autenticacao, Usuario usuarioSalvo) {
        autenticacao.setUsuario(usuarioSalvo);
        return this.autenticacaoRepository.save(autenticacao);
    }

    @Override
    public void deletarAutenticacao(Long id) {
        Autenticacao autenticacao = this.autenticacaoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado."));
        this.autenticacaoRepository.delete(autenticacao);
    }

    @Override
    public String autenticar(String email, String senha) {
        Autenticacao autenticacao = this.autenticacaoRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("Dados de autenticação invalidos."));
        if(!utils.validarSenha(senha, autenticacao.getSenha())){
            throw new IllegalArgumentException("Dados de autenticação invalidos.");
        }
        return tokenService.gerarToken(autenticacao);
    }
}
