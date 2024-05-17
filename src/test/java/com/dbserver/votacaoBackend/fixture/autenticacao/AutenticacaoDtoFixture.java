package com.dbserver.votacaoBackend.fixture.autenticacao;

import java.util.Locale;

import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutenticacaoDto;

import net.datafaker.Faker;

public class AutenticacaoDtoFixture {
    private static final Faker faker = new Faker(new Locale("pt-BR"));

    public static AutenticacaoDto autenticacaoDtoAdminValido() {
        return new AutenticacaoDto(AutenticacaoFixture.EMAIL_ADMIN_CORRETO, AutenticacaoFixture.SENHA);
    }

    public static AutenticacaoDto autenticacaoDtoUsuarioValido() {
        return new AutenticacaoDto(AutenticacaoFixture.EMAIL_USUARIO_CORRETO, AutenticacaoFixture.SENHA);
    }

    public static AutenticacaoDto autenticacaoDtoSenhaIncorreta() {
        return new AutenticacaoDto(AutenticacaoFixture.EMAIL_ADMIN_CORRETO, AutenticacaoFixture.SENHA_ALEATORIA);
    }

    public static AutenticacaoDto autenticacaoDtoEmailIncorreto() {
        return new AutenticacaoDto(faker.internet().emailAddress(), AutenticacaoFixture.SENHA);
    }
}
