package com.dbserver.votacaoBackend.domain.autenticacao.service;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;

public interface IAutenticacaoService {
    public Autenticacao criarAutenticacao(Autenticacao autenticacao, Usuario usuarioSalvo);
    public void deletarAutenticacao(Long id);
    public Autenticacao buscarAutenticacaoPorEmailESenha(String email, String senha);
    public boolean validarSenhaDaAutenticacao(String senhaEsperada, String senhaEncriptada);
    public String encriptarSenhaDaAutenticacao(String senha);
}
