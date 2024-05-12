package com.dbserver.votacaoBackend.domain.autenticacao.service;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;

public interface AutenticacaoService {
    Autenticacao criarAutenticacao(Autenticacao autenticacao, Usuario usuarioSalvo);

    Autenticacao buscarAutenticacaoPorEmailESenha(String email, String senha);

    boolean validarSenhaDaAutenticacao(String senhaEsperada, String senhaEncriptada);

    String encriptarSenhaDaAutenticacao(String senha);

    void validarAutenticacaoPorCpfESenha(String cpf, String senha);

    boolean verificarEmailJaEstaCadastrado(String email);
}
