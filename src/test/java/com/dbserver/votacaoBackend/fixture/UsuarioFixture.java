package com.dbserver.votacaoBackend.fixture;

import java.util.Locale;

import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutenticacaoDto;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.dto.CriarUsuarioDto;

import net.datafaker.Faker;

public class UsuarioFixture {
    private static final Faker faker = new Faker(new Locale("pt-BR"));

    public static String cpfNaoCadastrado = faker.number().digits(11);

    public static Usuario usuarioAdmin() {
        return new Usuario("João", "Silva", "12345678900", true);
    }

    public static Usuario usuarioNaoAdmin() {
        return new Usuario("Pedro", "Cecilio", "12345678911", false);
    }

    public static CriarUsuarioDto criarUsuarioDto(AutenticacaoDto autenticacaoDto) {
        return new CriarUsuarioDto(autenticacaoDto, "Pedro", "Cecilio", "12345678910", true);
    }
}
