package com.dbserver.votacaoBackend.domain.usuario.service;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface IUsuarioService {
    public Usuario criarUsuario(Usuario usuario, Autenticacao autenticacao);
    public Usuario atualizarUsuario(Long usuarioId, String nome, String sobrenome);
    public void deletarUsuario(Long usuarioId);
    public Usuario buscarUsuarioPorId(Long usuarioId);
    public List<Usuario> buscarTodosUsuarios(Pageable pageable);
    public Usuario buscarUsuarioLogado();
    public boolean verificarSeExisteUsuárioPorCpf(String cpf);
    public Usuario buscarUsuarioPorCpfSeHouver(String cpf);

}
