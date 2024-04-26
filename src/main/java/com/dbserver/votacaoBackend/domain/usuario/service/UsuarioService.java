package com.dbserver.votacaoBackend.domain.usuario.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.service.IAutenticacaoService;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService implements IUsuarioService{
    private UsuarioRepository usuarioRepository;
    private IAutenticacaoService autenticacaoService;

    public UsuarioService(UsuarioRepository usuarioRepository, IAutenticacaoService autenticacaoService) {
        this.usuarioRepository = usuarioRepository;
        this.autenticacaoService = autenticacaoService;
    }

    @Override
    @Transactional
    public Usuario criarUsuario(Usuario usuario, Autenticacao autenticacao) {
        if(usuario == null) throw new IllegalArgumentException("Usuario não deve ser nulo.");
        if(autenticacao == null) throw new IllegalArgumentException("Autenticação não deve ser nula.");

        Usuario usuarioCriado = this.usuarioRepository.save(usuario);
        this.autenticacaoService.criarAutenticacao(autenticacao, usuarioCriado);
        return usuarioCriado;
    }

    @Override
    public Usuario buscarUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) authentication.getPrincipal();
    }
    
    @Override
    public boolean verificarSeExisteUsuarioPorCpf(String cpf){
        return this.usuarioRepository.findByCpf(cpf).isPresent();
    }

    @Override
    public Usuario buscarUsuarioPorCpfSeHouver(String cpf){
        return this.usuarioRepository.findByCpf(cpf).orElse(null);
    }
}
