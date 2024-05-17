package com.dbserver.votacaoBackend.fixture.usuario;

import java.util.Locale;

import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutenticacaoDto;
import com.dbserver.votacaoBackend.domain.usuario.dto.CriarUsuarioDto;

import net.datafaker.Faker;

public class CriarUsuarioDtoFixture {
    private static final Faker faker = new Faker(new Locale("pt-BR"));

    public static CriarUsuarioDto criarUsuarioDto(AutenticacaoDto autenticacaoDto) {
        return new CriarUsuarioDto(autenticacaoDto, faker.name().firstName(), faker.name().lastName(), UsuarioFixture.CPF_ALEATORIO,
                false);
    }
}
