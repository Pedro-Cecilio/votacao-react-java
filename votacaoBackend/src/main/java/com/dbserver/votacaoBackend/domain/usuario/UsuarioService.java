package com.dbserver.votacaoBackend.domain.usuario;

import org.springframework.stereotype.Service;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.AutenticacaoService;

@Service
public class UsuarioService {
    private UsuarioRepository usuarioRepository;
    private AutenticacaoService autenticacaoService;
    public UsuarioService(UsuarioRepository usuarioRepository, AutenticacaoService autenticacaoService) {
        this.usuarioRepository = usuarioRepository;
        this.autenticacaoService = autenticacaoService;
    }

    public Usuario criarUsuario(Usuario usuario, Autenticacao autenticacao) {
        Usuario usuarioCriado = this.usuarioRepository.save(usuario);
        this.autenticacaoService.criarAutenticacao(autenticacao, usuario);
        return usuarioCriado;
    }
}
