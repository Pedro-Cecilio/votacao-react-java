package com.dbserver.votacaoBackend.domain.autenticacao;

import org.springframework.stereotype.Service;

import com.dbserver.votacaoBackend.domain.usuario.Usuario;

@Service
public class AutenticacaoService {
    private AutenticacaoRepository autenticacaoRepository;

    public AutenticacaoService(AutenticacaoRepository autenticacaoRepository) {
        this.autenticacaoRepository = autenticacaoRepository;
    }

    public Autenticacao criarAutenticacao(Autenticacao autenticacao, Usuario usuarioSalvo) {
        autenticacao.setUsuario(usuarioSalvo);
        return this.autenticacaoRepository.save(autenticacao);
    }
}
