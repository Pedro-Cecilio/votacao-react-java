package com.dbserver.votacaoBackend.domain.infra.security.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.infra.security.token.TokenService;

@SpringBootTest
class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    @Value("${api.security.token.senha}")
    private String senha;

    private Usuario usuario;
    private Autenticacao autenticacao;

    @BeforeEach
    void setUp() throws Exception {
        this.usuario = new Usuario(1L, "João", "Silva", "12345678900", true);
        this.autenticacao = new Autenticacao("example@example.com", "senha123");
        this.autenticacao.setUsuario(this.usuario);
    }

    @Test
    @DisplayName("Deve ser possível criar um token ao passar uma autenticação valida")
    void givenPossuoUmaAutenticacaoValidaWhenTentoCriarTokenThenRetornarToken() {

        String token = this.tokenService.gerarToken(this.autenticacao);
        assertNotNull(token);
    }

    @Test
    @DisplayName("Deve falhar ao tentar criar um token com autenticação sem usuário")
    void givenPossuoUmaAutenticacaoSemUsuarioWhenTentoCriarTokenThenRetornarErro() {
        Autenticacao autenticacao = new Autenticacao("example@example.com", "senha123");

        assertThrows(IllegalArgumentException.class, () -> this.tokenService.gerarToken(autenticacao));
    }

    @Test
    @DisplayName("Deve falhar ao tentar criar um token com autenticação nula")
    void givenPossuoUmaAutenticacaoNulaWhenTentoCriarTokenThenRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> this.tokenService.gerarToken(null));
    }

    @Test
    @DisplayName("Deve ser possível validar um token corretamente")
    void givenPossuoUmTokenValidoWhenTentoValidarTokenThenRetornarToken() {

        String token = this.tokenService.gerarToken(this.autenticacao);
        String resposta = this.tokenService.validarToken(token);
        assertEquals(resposta, autenticacao.getEmail());
    }
    

}
