package com.dbserver.votacaoBackend.fixture;

import java.util.Locale;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutenticacaoDto;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutorizarVotoExternoDto;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;

import net.datafaker.Faker;

public class AutenticacaoFixture {
    private static final Faker faker = new Faker(new Locale("pt-BR"));

    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static final String SENHA = faker.regexify("[a-zA-Z0-9]{8}");
    
    public static final String SENHA_ENCRIPTADA = passwordEncoder.encode(SENHA);

    public static final String EMAIL_ADMIN_CORRETO = faker.internet().emailAddress();
    
    public static final String EMAIL_USUARIO_CORRETO = faker.internet().emailAddress();

    public static final String SENHA_INCORRETA = faker.number().digits(8);

    public static Autenticacao autenticacaoAdmin(Usuario usuario) {
        Autenticacao autenticacao = new Autenticacao(EMAIL_ADMIN_CORRETO,
        SENHA_ENCRIPTADA);
        autenticacao.setUsuario(usuario);
        return autenticacao;
    }

    public static Autenticacao autenticacaoUsuario(Usuario usuario) {
        Autenticacao autenticacao = new Autenticacao(EMAIL_USUARIO_CORRETO,
        SENHA_ENCRIPTADA);
        autenticacao.setUsuario(usuario);
        return autenticacao;
    }

    public static AutenticacaoDto autenticacaoDtoAdminValido() {
        return new AutenticacaoDto(EMAIL_ADMIN_CORRETO, SENHA);
    }

    public static AutenticacaoDto autenticacaoDtoUsuarioValido() {
        return new AutenticacaoDto(EMAIL_USUARIO_CORRETO, SENHA);
    }

    public static AutenticacaoDto autenticacaoDtoSenhaIncorreta() {
        return new AutenticacaoDto(EMAIL_ADMIN_CORRETO, SENHA_INCORRETA);
    }

    public static AutenticacaoDto autenticacaoDtoEmailIncorreto() {
        return new AutenticacaoDto(faker.internet().emailAddress(), SENHA);
    }

    public static AutorizarVotoExternoDto autorizarVotoExternoDtoValido(String cpf) {
        return new AutorizarVotoExternoDto(cpf, SENHA);
    }

    public static AutorizarVotoExternoDto autorizarVotoExternoDtoCpfInvalido() {
        return new AutorizarVotoExternoDto(faker.number().digits(11), SENHA);
    }

    public static AutorizarVotoExternoDto autorizarVotoExternoDtoSenhaIncorreta(String cpf) {
        return new AutorizarVotoExternoDto(cpf, SENHA_INCORRETA);
    }

}
