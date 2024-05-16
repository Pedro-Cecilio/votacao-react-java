package com.dbserver.votacaoBackend.domain.autenticacao.service;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutenticacaoDto;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutenticacaoRespostaDto;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutorizarVotoExternoDto;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutorizarVotoExternoRespostaDto;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;

public interface AutenticacaoService {
    Autenticacao criarAutenticacao(Autenticacao autenticacao, Usuario usuarioSalvo);

    String encriptarSenhaDaAutenticacao(String senha);

    boolean verificarEmailJaEstaCadastrado(String email);

    AutenticacaoRespostaDto autenticarUsuario(AutenticacaoDto dto);

    AutorizarVotoExternoRespostaDto autorizarUsuarioVotoExterno(AutorizarVotoExternoDto dto);
}
