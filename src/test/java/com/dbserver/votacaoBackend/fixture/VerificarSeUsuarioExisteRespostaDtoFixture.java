package com.dbserver.votacaoBackend.fixture;

import com.dbserver.votacaoBackend.domain.usuario.dto.VerificarSeUsuarioExisteRespostaDto;

public class VerificarSeUsuarioExisteRespostaDtoFixture {
    public static VerificarSeUsuarioExisteRespostaDto gerarVerificarSeUsuarioExisteRespostaDtoFalse(){
        return new VerificarSeUsuarioExisteRespostaDto(false);
    }
    public static VerificarSeUsuarioExisteRespostaDto gerarVerificarSeUsuarioExisteRespostaDtoTrue(){
        return new VerificarSeUsuarioExisteRespostaDto(true);
    }
}
