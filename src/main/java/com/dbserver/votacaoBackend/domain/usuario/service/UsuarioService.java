package com.dbserver.votacaoBackend.domain.usuario.service;

import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.dto.CriarUsuarioDto;
import com.dbserver.votacaoBackend.domain.usuario.dto.CriarUsuarioRespostaDto;

public interface UsuarioService {
    public CriarUsuarioRespostaDto criarUsuario(CriarUsuarioDto dto);
    Usuario buscarUsuarioLogado();
    boolean verificarSeExisteUsuarioPorCpf(String cpf);
    Usuario buscarUsuarioPorCpfSeHouver(String cpf);

}
