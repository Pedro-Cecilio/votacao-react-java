package com.dbserver.votacaoBackend.infra.security.token;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.infra.exeptions.CriarJwtExeption;
import com.dbserver.votacaoBackend.infra.exeptions.ValidarJwtExeption;

public interface ITokenService {
    public String gerarToken(Autenticacao autenticacao) throws CriarJwtExeption;
    public String validarToken(String token) throws ValidarJwtExeption;
}
