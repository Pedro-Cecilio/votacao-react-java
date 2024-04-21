package com.dbserver.votacaoBackend.domain.usuario.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Pageable;
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
        Usuario usuarioCriado = this.usuarioRepository.save(usuario);
        this.autenticacaoService.criarAutenticacao(autenticacao, usuarioCriado);
        return usuarioCriado;
    }

    @Override
    @Transactional
    public Usuario atualizarUsuario(Long usuarioId, String nome, String sobrenome) {
        Usuario usuarioASerAtualizado = this.usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
        usuarioASerAtualizado.setNome(nome);
        usuarioASerAtualizado.setSobrenome(sobrenome);
        return this.usuarioRepository.save(usuarioASerAtualizado);
    }

    @Override
    @Transactional
    public void deletarUsuario(Long usuarioId) {
        Usuario usuario = this.usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado."));
        this.autenticacaoService.deletarAutenticacao(usuario.getId());
        this.usuarioRepository.delete(usuario);
    }

    @Override
    public Usuario buscarUsuarioPorId(Long usuarioId) {
        return this.usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado."));
    }

    @Override
    public List<Usuario> buscarTodosUsuarios(Pageable pageable) {
        return this.usuarioRepository.findAll(pageable).toList();
    }

    @Override
    public Usuario buscarUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) authentication.getPrincipal();
    }
    
    @Override
    public Usuario buscarUsuarioPorCpf(String cpf){
        return this.usuarioRepository.findByCpf(cpf).orElseThrow(() -> new NoSuchElementException("Usuário não encontrado."));
    }
}
