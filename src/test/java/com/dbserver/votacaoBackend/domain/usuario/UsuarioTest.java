package com.dbserver.votacaoBackend.domain.usuario;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class UsuarioTest {
    private Usuario usuarioMock;
    private String nomeValido;
    private String sobrenomeValido;
    private String cpfValido;

    @BeforeEach
    void configurar(){
        this.usuarioMock = new Usuario(1L, "João", "Silva", "12345678900", true);
        this.nomeValido = "Pedro";
        this.sobrenomeValido = "Cecilio";
        this.cpfValido = "12345678910";
    }

    @AfterEach
    void limpar(){
        this.usuarioMock = null;
    }

    @Test
    @DisplayName("Deve ser possível setar nome de Usuario corretemante")
    void dadoPossuoNomeValidoQuandoSetarNomeEntaoSetarNome(){
        assertDoesNotThrow(()-> this.usuarioMock.setNome(this.nomeValido));
        assertEquals(this.nomeValido, this.usuarioMock.getNome());
    }

    @ParameterizedTest
    @MethodSource("com.dbserver.votacaoBackend.testUtils.DadosTestesParametrizados#dadosInvalidosParaSetarNomeDeUsuario")
    @DisplayName("Não deve ser possível setar nome ao passar dado invalido")
    void dadoPossuoNomeInvalidoQuandoTentoSetarNomeEntaoRetornarUmErro(String nomeInvalido){
        assertThrows(IllegalArgumentException.class, ()->this.usuarioMock.setNome(nomeInvalido));
    }

    @Test
    @DisplayName("Deve ser possível setar sobrenome de Usuario corretemante")
    void dadoPossuoSobrenomeValidoQuandoSetarSobrenomeEntaoSetarSobrenome(){
        assertDoesNotThrow(()-> this.usuarioMock.setSobrenome(this.sobrenomeValido));
        assertEquals(this.sobrenomeValido, this.usuarioMock.getSobrenome());
    }

    @ParameterizedTest
    @MethodSource("com.dbserver.votacaoBackend.testUtils.DadosTestesParametrizados#dadosInvalidosParaSetarSobrenomeDeUsuario")
    @DisplayName("Não deve ser possível setar sobrenome ao passar dado invalido")
    void dadoPossuoSobrenomeInvalidoQuandoTentoSetarSobrenomeEntaoRetornarUmErro(String sobrenomeInvalido){
        assertThrows(IllegalArgumentException.class, ()->this.usuarioMock.setNome(sobrenomeInvalido));
    }

    @Test
    @DisplayName("Deve ser possível setar cpf de Usuario corretemante")
    void dadoPossuoCpfValidoQuandoSetarCpfEntaoSetarCpf(){
        assertDoesNotThrow(()-> this.usuarioMock.setCpf(this.cpfValido));
        assertEquals(this.cpfValido, this.usuarioMock.getCpf());
    }

    @ParameterizedTest
    @MethodSource("com.dbserver.votacaoBackend.testUtils.DadosTestesParametrizados#dadosInvalidosParaSetarCpfDeUsuario")
    @DisplayName("Não deve ser possível setar cpf ao passar dado invalido")
    void dadoPossuoCpfInvalidoQuandoTentoSetarCpfEntaoRetornarUmErro(String cpfInvalido){
        assertThrows(IllegalArgumentException.class, ()->this.usuarioMock.setCpf(cpfInvalido));
    }
}
