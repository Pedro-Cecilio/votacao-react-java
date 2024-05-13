package com.dbserver.votacaoBackend.domain.usuario.validacoes;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.dbserver.votacaoBackend.domain.usuario.Usuario;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UsuarioValidacoesTest {

    @InjectMocks
    private UsuarioValidacoes usuarioValidacoes;
    @Mock
    private Usuario usuarioMock;

    @Test
    @DisplayName("Deve validar ao passar usuário não nulo")
    void dadoUsuarioNaoNuloQuandoTentoValidarSeNaoENuloDeveValidarCorretamente(){
        assertDoesNotThrow(()-> this.usuarioValidacoes.validarUsuarioNaoNulo(usuarioMock));
    }
    @Test
    @DisplayName("Deve retornar erro ao validar ao passar usuário nulo")
    void dadoUsuarioNuloQuandoTentoValidarSeNaoENuloDeveRetornarErro(){
        assertThrows(IllegalArgumentException.class, ()-> this.usuarioValidacoes.validarUsuarioNaoNulo(null));
    }
}
