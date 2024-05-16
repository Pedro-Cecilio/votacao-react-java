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
import com.dbserver.votacaoBackend.fixture.AutenticacaoFixture;
import com.dbserver.votacaoBackend.fixture.UsuarioFixture;
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
        this.usuario = UsuarioFixture.usuarioAdmin();
        this.autenticacao = AutenticacaoFixture.autenticacaoAdmin(usuario);
    }

    @Test
    @DisplayName("Deve ser possível criar um token ao passar uma autenticação valida")
    void dadoPossuoUmaAutenticacaoValidaQuandoTentoCriarTokenEntaoRetornarToken() {

        String token = this.tokenService.gerarToken(this.autenticacao);
        assertNotNull(token);
    }

    @Test
    @DisplayName("Deve falhar ao tentar criar um token com autenticação sem usuário definido")
    void dadoPossuoUmaAutenticacaoSemUsuarioQuandoTentoCriarTokenEntaoRetornarErro() {
        Autenticacao autenticacao = AutenticacaoFixture.gerarAutenticacaoComDadosDeUsuario();

        assertThrows(IllegalArgumentException.class, () -> this.tokenService.gerarToken(autenticacao));
    }

    @Test
    @DisplayName("Deve falhar ao tentar criar um token com autenticação nula")
    void dadoPossuoUmaAutenticacaoNulaQuandoTentoCriarTokenEntaoRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> this.tokenService.gerarToken(null));
    }

    @Test
    @DisplayName("Deve ser possível validar um token corretamente")
    void dadoPossuoUmTokenValidoQuandoTentoValidarTokenEntaoRetornarToken() {
        String token = this.tokenService.gerarToken(this.autenticacao);
        String resposta = this.tokenService.validarToken(token);
        assertEquals(resposta, autenticacao.getEmail());
    }
    

}
