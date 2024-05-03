package com.dbserver.votacaoBackend.domain.usuario.service;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;

public interface UsuarioService {
    Usuario criarUsuario(Usuario usuario, Autenticacao autenticacao);
    Usuario buscarUsuarioLogado();
    boolean verificarSeExisteUsuarioPorCpf(String cpf);
    Usuario buscarUsuarioPorCpfSeHouver(String cpf);

}
