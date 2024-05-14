package com.dbserver.votacaoBackend.fixture;

import java.util.Locale;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutenticacaoDto;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutorizarVotoExternoDto;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;

import net.datafaker.Faker;

public class AutenticacaoFixture {
    private static final Faker faker = new Faker(new Locale("pt-BR"));

    public static String senhaCorreta = "senha123";
    public static String emailAdminCorreto = faker.internet().emailAddress();
    public static String emailUsuarioCorreto = faker.internet().emailAddress();
    public static String senhaIncorreta = faker.number().digits(8);

    public static Autenticacao autenticacaoAdmin(Usuario usuario) {
        Autenticacao autenticacao = new Autenticacao(emailAdminCorreto,
                "$2a$10$6V3ZuwVZxOfqS0IKfhqk1uUzdUe/8jl1flMBk5AVzmPSX2wYKd/vS");
        autenticacao.setUsuario(usuario);
        return autenticacao;
    }
    public static Autenticacao autenticacaoUsuario(Usuario usuario) {
        Autenticacao autenticacao = new Autenticacao(emailUsuarioCorreto,
                "$2a$10$6V3ZuwVZxOfqS0IKfhqk1uUzdUe/8jl1flMBk5AVzmPSX2wYKd/vS");
        autenticacao.setUsuario(usuario);
        return autenticacao;
    }

    public static AutenticacaoDto autenticacaoDtoAdminValido() {
        return new AutenticacaoDto(emailAdminCorreto, senhaCorreta);
    }
    public static AutenticacaoDto autenticacaoDtoUsuarioValido() {
        return new AutenticacaoDto(emailUsuarioCorreto, senhaCorreta);
    }

    public static AutenticacaoDto autenticacaoDtoSenhaIncorreta() {
        return new AutenticacaoDto(emailAdminCorreto, senhaIncorreta);
    }

    public static AutenticacaoDto autenticacaoDtoEmailIncorreto() {
        return new AutenticacaoDto(faker.internet().emailAddress(), senhaCorreta);
    }

    public static AutorizarVotoExternoDto autorizarVotoExternoDtoValido(String cpf) {
        return new AutorizarVotoExternoDto(cpf, senhaCorreta);
    }

    public static AutorizarVotoExternoDto autorizarVotoExternoDtoCpfInvalido() {
        return new AutorizarVotoExternoDto(faker.number().digits(11), senhaCorreta);
    }

    public static AutorizarVotoExternoDto autorizarVotoExternoDtoSenhaIncorreta(String cpf) {
        return new AutorizarVotoExternoDto(cpf, senhaIncorreta);
    }


}
