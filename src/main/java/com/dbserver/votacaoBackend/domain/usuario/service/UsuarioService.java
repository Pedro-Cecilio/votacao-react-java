package com.dbserver.votacaoBackend.domain.usuario.service;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;

public interface UsuarioService {
    public Usuario criarUsuario(Usuario usuario, Autenticacao autenticacao);
    public Usuario buscarUsuarioLogado();
    public boolean verificarSeExisteUsuarioPorCpf(String cpf);
    public Usuario buscarUsuarioPorCpfSeHouver(String cpf);

}
