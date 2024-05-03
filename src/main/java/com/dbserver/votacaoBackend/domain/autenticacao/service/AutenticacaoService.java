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
        if(usuarioSalvo == null) throw new IllegalArgumentException("Usuario não deve ser nulo.");

        if(autenticacao == null) throw new IllegalArgumentException("Autenticação não deve ser nula.");
        
        autenticacao.setUsuario(usuarioSalvo);

        return this.autenticacaoRepository.save(autenticacao);
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
        if(senha == null || senha.trim().isEmpty())throw new IllegalArgumentException("Senha deve ser informada.");

        if (senha.trim().length() < 8)
            throw new IllegalArgumentException("Senha deve conter 8 caracteres no mínimo.");

        return this.passwordEncoder.encode(senha);
    }

    @Override
    public void validarAutenticacaoPorCpfESenha(String cpf, String senha){
        Autenticacao autenticacao = this.autenticacaoRepository.findByCpf(cpf).orElseThrow(()-> new BadCredentialsException("Dados de autenticação inválidos."));

        boolean valido = this.validarSenhaDaAutenticacao(senha, autenticacao.getSenha());
        
        if(!valido) throw new BadCredentialsException("Dados de autenticação inválidos.");
    }

    @Override
    public boolean verificarEmailJaEstaCadastrado(String email){
        return this.autenticacaoRepository.findByEmail(email).isPresent();
    }
   
}
