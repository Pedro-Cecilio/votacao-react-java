package com.dbserver.votacaoBackend.fixture;

import java.util.Locale;

import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutenticacaoDto;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.dto.CriarUsuarioDto;

import net.datafaker.Faker;

public class UsuarioFixture {
    private static final Faker faker = new Faker(new Locale("pt-BR"));

    public static final String CPF_ALEATORIO = faker.number().digits(11);
    public static final String CPF_ADMIN = faker.number().digits(11);
    public static final String CPF_USUARIO = faker.number().digits(11);
    public static final String NOME_ALEATORIO = faker.name().firstName();
    public static final String SOBRENOME_ALEATORIO = faker.name().firstName();


    public static Usuario usuarioAdmin() {
        return new Usuario(faker.name().firstName(), faker.name().lastName(), CPF_ADMIN, true);
    }

    public static Usuario usuarioNaoAdmin() {
        return new Usuario(faker.name().firstName(), faker.name().lastName(), CPF_USUARIO, false);
    }

    public static CriarUsuarioDto criarUsuarioDto(AutenticacaoDto autenticacaoDto) {
        return new CriarUsuarioDto(autenticacaoDto, faker.name().firstName(), faker.name().lastName(), CPF_ALEATORIO, false);
    }

    public static Usuario gerarUsuarioAtravesDoDto(CriarUsuarioDto dto) {
        return new Usuario(dto.nome(), dto.sobrenome(), dto.cpf(), dto.admin());
    }
}
