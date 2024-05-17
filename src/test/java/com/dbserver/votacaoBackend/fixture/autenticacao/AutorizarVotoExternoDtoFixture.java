package com.dbserver.votacaoBackend.fixture.autenticacao;

import java.util.Locale;

import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutorizarVotoExternoDto;

import net.datafaker.Faker;

public class AutorizarVotoExternoDtoFixture {
    private static final Faker faker = new Faker(new Locale("pt-BR"));

    public static AutorizarVotoExternoDto autorizarVotoExternoDtoValido(String cpf) {
        return new AutorizarVotoExternoDto(cpf, AutenticacaoFixture.SENHA);
    }

    public static AutorizarVotoExternoDto autorizarVotoExternoDtoCpfInvalido() {
        return new AutorizarVotoExternoDto(faker.number().digits(11), AutenticacaoFixture.SENHA);
    }

    public static AutorizarVotoExternoDto autorizarVotoExternoDtoSenhaIncorreta(String cpf) {
        return new AutorizarVotoExternoDto(cpf, AutenticacaoFixture.SENHA_ALEATORIA);
    }
}
