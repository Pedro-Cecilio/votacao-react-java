package com.dbserver.votacaoBackend.fixture.pauta;

import com.dbserver.votacaoBackend.domain.pauta.dto.RespostaPautaDto;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;

public class RespostaPautaDtoFixture {
    
     public static RespostaPautaDto respostaPautaDto(Usuario usuario) {
        return new RespostaPautaDto(PautaFixture.pautaTransporte(usuario), null);
    }
}
