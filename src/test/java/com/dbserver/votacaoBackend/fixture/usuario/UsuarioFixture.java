package com.dbserver.votacaoBackend.fixture.usuario;

import java.util.Locale;

import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.dto.CriarUsuarioDto;

import net.datafaker.Faker;

public class UsuarioFixture {
    private static final Faker faker = new Faker(new Locale("pt-BR"));

    public static final String CPF_ALEATORIO = faker.number().digits(11);
    public static final String CPF_INVALIDO = faker.number().digits(10);
    public static final String CPF_ADMIN = faker.number().digits(11);
    public static final String CPF_USUARIO = faker.number().digits(11);
    public static final String NOME_ALEATORIO = faker.name().firstName();
    public static final String SOBRENOME_ALEATORIO = faker.name().firstName();


    public static Usuario usuarioAdmin() {
        return Usuario.builder().nome(faker.name().firstName())
        .sobrenome(faker.name().lastName())
        .cpf(CPF_ADMIN)
        .admin(true)
        .build();
    }

    public static Usuario usuarioNaoAdmin() {
        return Usuario.builder().nome(faker.name().firstName())
        .sobrenome(faker.name().lastName())
        .cpf(CPF_USUARIO)
        .admin(false)
        .build();
    }

    public static Usuario gerarUsuarioAtravesDoDto(CriarUsuarioDto dto) {
        return Usuario.builder().nome(dto.nome())
        .sobrenome(dto.sobrenome())
        .cpf(dto.cpf())
        .admin(dto.admin())
        .build();
    }
}
