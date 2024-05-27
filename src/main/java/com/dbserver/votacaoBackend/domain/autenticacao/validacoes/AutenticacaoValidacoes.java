package com.dbserver.votacaoBackend.domain.autenticacao.validacoes;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.repository.AutenticacaoRepository;
import com.dbserver.votacaoBackend.utils.Utils;

@Component
public class AutenticacaoValidacoes {
    
    private PasswordEncoder passwordEncoder;
    private AutenticacaoRepository autenticacaoRepository;

    public AutenticacaoValidacoes(AutenticacaoRepository autenticacaoRepository, PasswordEncoder passwordEncoder) {
        this.autenticacaoRepository = autenticacaoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static void validarFormatoDaSenha(String senha){
        if (senha == null || senha.isEmpty())
            throw new IllegalArgumentException("Senha deve ser informada.");

        if (senha.trim().length() < 8)
            throw new IllegalArgumentException("Senha deve conter 8 caracteres no mínimo.");
    }

    public static void validarFormatoDoEmail(String email){
            if (email == null || !Utils.validarRegex(Utils.REGEX_EMAIL, email.trim()))
                throw new IllegalArgumentException("Email com formato inválido.");
          
    }


    public boolean validarSenhaDaAutenticacao(String senhaEsperada, String senhaEncriptada) {
        if(senhaEsperada == null || senhaEncriptada == null) return false;
        return this.passwordEncoder.matches(senhaEsperada, senhaEncriptada);
    }

    public void validarAutenticacaoNaoNula(Autenticacao autenticacao){
        if(autenticacao == null) throw new IllegalArgumentException("Autenticação não deve ser nula.");
    }

    public void validarAutenticacaoPorCpfESenha(String cpf, String senha){
        Autenticacao autenticacao = this.autenticacaoRepository.findByCpf(cpf).orElseThrow(()-> new BadCredentialsException("Dados de autenticação inválidos."));

        boolean valido = this.validarSenhaDaAutenticacao(senha, autenticacao.getSenha());

        if(!valido) throw new BadCredentialsException("Dados de autenticação inválidos.");
    }

    
}
