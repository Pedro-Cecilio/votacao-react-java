package com.dbserver.votacaoBackend.fixture.sessaoVotacao;

import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoExternoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoInternoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.TipoDeVotoEnum;
import com.dbserver.votacaoBackend.fixture.usuario.UsuarioFixture;

public class InserirVotoInternoDtoFixture {
    public static InserirVotoInternoDto inserirVotoInternoPositivoDto(Long pautaId) {
        return new InserirVotoInternoDto(pautaId, TipoDeVotoEnum.VOTO_POSITIVO);
    }

    public static InserirVotoExternoDto inserirVotoExternoNegativoDto(Long pautaId) {
        return new InserirVotoExternoDto(pautaId, TipoDeVotoEnum.VOTO_NEGATIVO, UsuarioFixture.CPF_ALEATORIO,
                null);
    }

    public static InserirVotoInternoDto inserirVotoInternoDto() {
        return new InserirVotoInternoDto(1L, TipoDeVotoEnum.VOTO_POSITIVO);
    }

    public static InserirVotoInternoDto inserirVotoInternoDtoVotoNull() {
        return new InserirVotoInternoDto(1L, null);
    }

    public static InserirVotoInternoDto inserirVotoInternoDtoVotoNegativo() {
        return new InserirVotoInternoDto(1L, TipoDeVotoEnum.VOTO_NEGATIVO);
    }
}
