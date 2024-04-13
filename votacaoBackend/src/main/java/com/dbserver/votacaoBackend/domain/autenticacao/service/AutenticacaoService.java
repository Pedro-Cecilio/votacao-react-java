package com.dbserver.votacaoBackend.domain.autenticacao.service;

import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.repository.AutenticacaoRepository;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.utils.Utils;

@Service
public class AutenticacaoService implements IAutenticacaoService {
    private AutenticacaoRepository autenticacaoRepository;
    private Utils utils;

    public AutenticacaoService(AutenticacaoRepository autenticacaoRepository, Utils utils) {
        this.autenticacaoRepository = autenticacaoRepository;
        this.utils = utils;
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
    public boolean validarDadosAutenticacao(String email, String senha) {
        Optional<Autenticacao> autenticacao = this.autenticacaoRepository.findByEmail(email);
        if(autenticacao.isPresent()){
            return utils.validarSenha(senha, autenticacao.get().getSenha());
        }
        return false;
    }

    @Override
    public Autenticacao buscarAutenticacaoPeloEmail(String email) {
        return this.autenticacaoRepository.findByEmail(email).orElseThrow(()-> new NoSuchElementException("Autenticação não encontrada."));
    }
}
