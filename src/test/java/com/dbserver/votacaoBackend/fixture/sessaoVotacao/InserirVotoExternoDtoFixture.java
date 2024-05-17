package com.dbserver.votacaoBackend.fixture.sessaoVotacao;

import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoExternoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.TipoDeVotoEnum;
import com.dbserver.votacaoBackend.fixture.autenticacao.AutenticacaoFixture;
import com.dbserver.votacaoBackend.fixture.usuario.UsuarioFixture;

public class InserirVotoExternoDtoFixture {
    public static InserirVotoExternoDto inserirVotoExternoDtoUsuarioExistenteValido() {
        return new InserirVotoExternoDto(1L, TipoDeVotoEnum.VOTO_POSITIVO,
                UsuarioFixture.CPF_ALEATORIO, AutenticacaoFixture.SENHA_ALEATORIA);
    }
}
