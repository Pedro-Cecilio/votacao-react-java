package com.dbserver.votacaoBackend.domain.voto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.fixture.usuario.UsuarioFixture;
import com.dbserver.votacaoBackend.fixture.voto.VotoFixture;

@SpringBootTest
class VotoTest {

    private Voto votoMock;

    @BeforeEach
    void configurar() {
        Usuario usuario = UsuarioFixture.usuarioAdmin();
        this.votoMock = VotoFixture.gerarVotoInterno(usuario);
    }

    @Test
    @DisplayName("Deve ser possível definir cpf valido")
    void dadoPossuoCpfValidoQuandoSetarCpfEntaoSetarCpf() {
        String novoCpfValido = UsuarioFixture.CPF_ALEATORIO;
        assertDoesNotThrow(() -> this.votoMock.setCpf(novoCpfValido));
        assertEquals(novoCpfValido, this.votoMock.getCpf());
    }
    @Test
    @DisplayName("Deve retornar um erro ao tentar definir cpf inválido")
    void dadoPossuoCpfInalidoQuandoSetarCpfEntaoRetornarErro() {
        String novoCpfInvalido = UsuarioFixture.CPF_INVALIDO;
        assertThrows(IllegalArgumentException.class, () -> this.votoMock.setCpf(novoCpfInvalido));
    }
    @Test
    @DisplayName("Deve retornar um erro ao tentar definir cpf nulo")
    void dadoPossuoCpfNuloQuandoSetarCpfEntaoRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> this.votoMock.setCpf(null));
    }

}
