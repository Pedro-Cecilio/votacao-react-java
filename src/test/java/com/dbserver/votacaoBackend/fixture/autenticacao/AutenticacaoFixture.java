package com.dbserver.votacaoBackend.fixture.autenticacao;

import java.util.Locale;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;

import net.datafaker.Faker;

public class AutenticacaoFixture {
    private static final Faker faker = new Faker(new Locale("pt-BR"));

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static final String SENHA = faker.regexify("[a-zA-Z0-9]{8}");

    public static final String SENHA_ENCRIPTADA = passwordEncoder.encode(SENHA);

    public static final String EMAIL_ADMIN_CORRETO = faker.internet().emailAddress();

    public static final String EMAIL_USUARIO_CORRETO = faker.internet().emailAddress();

    public static final String SENHA_ALEATORIA = faker.number().digits(8);

    public static final String EMAIL_ALEATORIO = faker.internet().emailAddress();

    public static Autenticacao autenticacaoAdmin(Usuario usuario) {
        Autenticacao autenticacao = Autenticacao.builder().email(EMAIL_ADMIN_CORRETO).senha(SENHA_ENCRIPTADA).build();
        autenticacao.setUsuario(usuario);
        return autenticacao;
    }

    public static Autenticacao autenticacaoUsuario(Usuario usuario) {
        Autenticacao autenticacao = Autenticacao.builder().email(EMAIL_USUARIO_CORRETO).senha(SENHA_ENCRIPTADA).build();
        autenticacao.setUsuario(usuario);
        return autenticacao;
    }

    public static Autenticacao gerarAutenticacaoComDadosDeUsuario() {
        return Autenticacao.builder().email(EMAIL_USUARIO_CORRETO).senha(SENHA_ENCRIPTADA).build();
    }

}
