package com.dbserver.votacaoBackend.domain.usuario.service;

import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.dto.CriarUsuarioDto;
import com.dbserver.votacaoBackend.domain.usuario.dto.CriarUsuarioRespostaDto;
import com.dbserver.votacaoBackend.domain.usuario.dto.UsuarioRespostaDto;
import com.dbserver.votacaoBackend.domain.usuario.dto.VerificarSeUsuarioExisteRespostaDto;

public interface UsuarioService {
    CriarUsuarioRespostaDto criarUsuario(CriarUsuarioDto dto);
    Usuario buscarUsuarioLogado();
    boolean verificarSeExisteUsuarioPorCpf(String cpf);
    VerificarSeUsuarioExisteRespostaDto verificarSeExisteUsuarioPorCpfComoDto(String cpf);
    Usuario buscarUsuarioPorCpfSeHouver(String cpf);
    UsuarioRespostaDto buscarUsuarioLogadoComoDto();
}
