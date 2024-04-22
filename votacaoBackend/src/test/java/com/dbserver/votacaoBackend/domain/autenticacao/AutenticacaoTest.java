package com.dbserver.votacaoBackend.domain.autenticacao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dbserver.votacaoBackend.utils.Utils;

@SpringBootTest
class AutenticacaoTest {
    
    private Autenticacao autenticacaoMock;
    private String emailValido;
    private String senhaValida;
    
    @Autowired
    private Utils utils;

    @BeforeEach
    void configurar(){
        this.autenticacaoMock = new Autenticacao("example@example.com", "senha123");
        this.emailValido = "test@example.com";
        this.senhaValida = "password123";
    
    }
    @AfterEach
    void limpar(){
        this.autenticacaoMock = null;
    }

    @Test
    @DisplayName("Deve ser possível setar um email corretamente")
    void givenPossuoUmEmailComFormatoValidoWhenTentoSetarEmailThenDefinirNovoEmail(){
        assertDoesNotThrow(()->this.autenticacaoMock.setEmail(this.emailValido));
        assertEquals(this.emailValido, this.autenticacaoMock.getEmail());
    }


    
    @ParameterizedTest
    @MethodSource("com.dbserver.votacaoBackend.testUtils.DadosTestesParametrizados#dadosInvalidosParaSetarEmail")
    @DisplayName("Deve ser falhar ao setar um email com dado invalido")
    void givenPossuoUmEmailInvalidoWhenTentoSetarEmailThenRetornarUmaExcecao(String emailInvalido){
        assertThrows(IllegalArgumentException.class, ()->this.autenticacaoMock.setEmail(emailInvalido));
    }

    // @Test
    // @DisplayName("Deve ser possível setar uma senha corretamente")
    // void givenPossuoUmaSenhaComFormatoValidoWhenTentoSetarSenhaThenDefinirNovaSenha(){ 
    //     assertDoesNotThrow(()->this.autenticacaoMock.setSenha(this.senhaValida));
    //     assertTrue(utils.validarSenha(senhaValida, this.autenticacaoMock.getSenha()));
    // }


    @ParameterizedTest
    @MethodSource("com.dbserver.votacaoBackend.testUtils.DadosTestesParametrizados#dadosInvalidosParaSetarSenha")
    @DisplayName("Deve falhar ao setar uma senha com dado inválido")
    void givenPossuoUmaSenhaInvalidaWhenTentoSetarSenhaThenRetornarUmaExcecao(String senhaInvalida) {
        assertThrows(IllegalArgumentException.class, () -> this.autenticacaoMock.setSenha(senhaInvalida));
    }
}
