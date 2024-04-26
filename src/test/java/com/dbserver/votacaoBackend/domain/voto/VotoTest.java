package com.dbserver.votacaoBackend.domain.voto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.dbserver.votacaoBackend.domain.usuario.Usuario;

@SpringBootTest
class VotoTest {

    private Voto votoMock;
    private Usuario usuario;
    private String novoCpfValido;
    private String novoCpfInvalido;

    @BeforeEach
    void configurar() {
        this.usuario = new Usuario(1L, "João", "Silva", "12345678900", true);
        this.votoMock = new Voto(this.usuario.getCpf(), this.usuario);
        this.novoCpfValido = "12345678910";
        this.novoCpfInvalido = "123456789";
    }

    @Test
    @DisplayName("Deve ser possível definir cpf valido")
    void givenPossuoCpfValidoWhenSetarCpfThenSetarCpf() {
        assertDoesNotThrow(() -> this.votoMock.setCpf(this.novoCpfValido));
        assertEquals(this.novoCpfValido, this.votoMock.getCpf());
    }
    @Test
    @DisplayName("Deve retornar um erro ao tentar definir cpf inválido")
    void givenPossuoCpfInalidoWhenSetarCpfThenRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> this.votoMock.setCpf(this.novoCpfInvalido));
    }
    @Test
    @DisplayName("Deve retornar um erro ao tentar definir cpf nulo")
    void givenPossuoCpfNuloWhenSetarCpfThenRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> this.votoMock.setCpf(null));
    }

}
