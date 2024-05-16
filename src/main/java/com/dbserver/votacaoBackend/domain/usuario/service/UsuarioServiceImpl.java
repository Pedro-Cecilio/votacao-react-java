package com.dbserver.votacaoBackend.domain.usuario.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.service.AutenticacaoServiceImpl;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.dto.CriarUsuarioDto;
import com.dbserver.votacaoBackend.domain.usuario.dto.CriarUsuarioRespostaDto;
import com.dbserver.votacaoBackend.domain.usuario.dto.UsuarioRespostaDto;
import com.dbserver.votacaoBackend.domain.usuario.dto.VerificarSeUsuarioExisteRespostaDto;
import com.dbserver.votacaoBackend.domain.usuario.mapper.UsuarioMapper;
import com.dbserver.votacaoBackend.domain.usuario.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private UsuarioRepository usuarioRepository;
    private AutenticacaoServiceImpl autenticacaoService;
    private UsuarioMapper usuarioMapper;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, AutenticacaoServiceImpl autenticacaoService,
            UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.autenticacaoService = autenticacaoService;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    @Transactional
    public CriarUsuarioRespostaDto criarUsuario(CriarUsuarioDto dto) {
        Usuario usuario = new Usuario(dto.nome(), dto.sobrenome(), dto.cpf(), dto.admin());

        String senhaEncriptada = this.autenticacaoService.encriptarSenhaDaAutenticacao(dto.autenticacaoDto().senha());

        Autenticacao autenticacao = new Autenticacao(dto.autenticacaoDto().email(), senhaEncriptada);

        if (this.autenticacaoService.verificarEmailJaEstaCadastrado(autenticacao.getEmail()))
            throw new IllegalArgumentException("Email já cadastrado.");

        if (this.usuarioRepository.findByCpf(usuario.getCpf()).isPresent())
            throw new IllegalArgumentException("Cpf já cadastrado.");

        this.usuarioRepository.save(usuario);
        this.autenticacaoService.criarAutenticacao(autenticacao, usuario);
        return usuarioMapper.toCriarUsuarioRespostaDto(usuario, autenticacao);
    }

    @Override
    public Usuario buscarUsuarioLogado() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return (Usuario) authentication.getPrincipal();
        } catch (Exception e) {
            throw new AccessDeniedException("Acesso negado.");
        }
    }

    @Override
    public UsuarioRespostaDto buscarUsuarioLogadoComoDto() {
        Usuario usuario = this.buscarUsuarioLogado();
        return usuarioMapper.toUsuarioRespostaDto(usuario);
    }

    @Override
    public boolean verificarSeExisteUsuarioPorCpf(String cpf) {
        return this.usuarioRepository.findByCpf(cpf).isPresent();
    }

    @Override
    public VerificarSeUsuarioExisteRespostaDto verificarSeExisteUsuarioPorCpfComoDto(String cpf) {
        boolean existe = verificarSeExisteUsuarioPorCpf(cpf);
        return usuarioMapper.toVerificarSeUsuarioExisteRespostaDto(existe);
    }

    @Override
    public Usuario buscarUsuarioPorCpfSeHouver(String cpf) {
        return this.usuarioRepository.findByCpf(cpf).orElse(null);
    }
}
