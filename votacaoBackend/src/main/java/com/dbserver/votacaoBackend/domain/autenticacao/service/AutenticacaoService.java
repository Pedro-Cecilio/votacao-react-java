package com.dbserver.votacaoBackend.domain.autenticacao.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.repository.AutenticacaoRepository;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;

@Service
public class AutenticacaoService implements IAutenticacaoService {
    private PasswordEncoder passwordEncoder;
    private AutenticacaoRepository autenticacaoRepository;

    public AutenticacaoService(AutenticacaoRepository autenticacaoRepository, PasswordEncoder passwordEncoder) {
        this.autenticacaoRepository = autenticacaoRepository;
        this.passwordEncoder = passwordEncoder;
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
    public Autenticacao buscarAutenticacaoPorEmailESenha(String email, String senha) {
        Autenticacao autenticacao = this.autenticacaoRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Dados de login inválidos."));

        boolean senhaValida = this.validarSenhaDaAutenticacao(senha, autenticacao.getSenha());
        if(!senhaValida) {
            throw new BadCredentialsException("Dados de login inválidos.");
        }
        return autenticacao;
        
    }
    
    @Override
    public boolean validarSenhaDaAutenticacao(String senhaEsperada, String senhaEncriptada) {
        return this.passwordEncoder.matches(senhaEsperada, senhaEncriptada);
    }
    @Override
    public String encriptarSenhaDaAutenticacao(String senha) {
        if(senha == null) throw new IllegalArgumentException("Senha não deve ser nula");
        return this.passwordEncoder.encode(senha);
    }


   
}
