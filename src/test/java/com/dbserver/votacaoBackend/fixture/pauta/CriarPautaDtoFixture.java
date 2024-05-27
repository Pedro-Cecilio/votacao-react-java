package com.dbserver.votacaoBackend.fixture.pauta;

import com.dbserver.votacaoBackend.domain.pauta.dto.CriarPautaDto;

public class CriarPautaDtoFixture {
    
    public static CriarPautaDto criarPautaDtoValido() {
        return new CriarPautaDto(PautaFixture.ASSUNTO_TRANSPORTE, PautaFixture.CATEGORIA_TRANSPORTE);
    }
}
