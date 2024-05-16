package com.dbserver.votacaoBackend.domain.voto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.fixture.UsuarioFixture;
import com.dbserver.votacaoBackend.fixture.VotoFixture;

@SpringBootTest
class VotoTest {

    private Voto votoMock;
    private Usuario usuario;
    private String novoCpfValido;
    private String novoCpfInvalido;

    @BeforeEach
    void configurar() {
        this.usuario = UsuarioFixture.usuarioAdmin();
        this.votoMock = VotoFixture.gerarVotoInterno(usuario);
        this.novoCpfValido = "12345678910";
        this.novoCpfInvalido = "123456789";
    }

    @Test
    @DisplayName("Deve ser possível definir cpf valido")
    void dadoPossuoCpfValidoQuandoSetarCpfEntaoSetarCpf() {
        assertDoesNotThrow(() -> this.votoMock.setCpf(this.novoCpfValido));
        assertEquals(this.novoCpfValido, this.votoMock.getCpf());
    }
    @Test
    @DisplayName("Deve retornar um erro ao tentar definir cpf inválido")
    void dadoPossuoCpfInalidoQuandoSetarCpfEntaoRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> this.votoMock.setCpf(this.novoCpfInvalido));
    }
    @Test
    @DisplayName("Deve retornar um erro ao tentar definir cpf nulo")
    void dadoPossuoCpfNuloQuandoSetarCpfEntaoRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> this.votoMock.setCpf(null));
    }

}
