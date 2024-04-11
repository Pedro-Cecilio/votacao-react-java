package com.dbserver.votacaoBackend.domain.autenticacao.service;

import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.repository.AutenticacaoRepository;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;

@Service
public class AutenticacaoService implements IAutenticacaoService{
    private AutenticacaoRepository autenticacaoRepository;

    public AutenticacaoService(AutenticacaoRepository autenticacaoRepository) {
        this.autenticacaoRepository = autenticacaoRepository;
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
}
