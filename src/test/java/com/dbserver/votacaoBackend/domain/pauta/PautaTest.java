package com.dbserver.votacaoBackend.domain.pauta;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.fixture.PautaFixture;
import com.dbserver.votacaoBackend.fixture.UsuarioFixture;

@SpringBootTest
class PautaTest {

    private Pauta pautaMock;
    private Usuario usuarioAdminMock;
    private Usuario usuarioNaoAdminMock;
    private Usuario usuarioSetMock;


    @BeforeEach
    void configurar() {
        this.usuarioAdminMock = UsuarioFixture.usuarioAdmin();
        this.usuarioNaoAdminMock = UsuarioFixture.usuarioNaoAdmin();
        this.usuarioSetMock = UsuarioFixture.usuarioAdmin();

        this.pautaMock = PautaFixture.pautaTransporte(usuarioAdminMock);
    }
    
    @Test
    @DisplayName("Deve ser possível setar um usuário corretamente")
    void dadoTenhoUmUsuarioValidoQuandoTentoSetarUsuarioEntaoSetarUsuario(){
        assertDoesNotThrow(()-> this.pautaMock.setUsuario(usuarioSetMock));
        assertEquals(this.usuarioSetMock.getId(), this.pautaMock.getUsuario().getId());
    }
    @Test
    @DisplayName("Não deve ser possível setar um usuário nulo")
    void dadoTenhoUmUsuarioNuloQuandoTentoSetarUsuarioEntaoRetornarErro(){
        assertThrows(IllegalArgumentException.class, ()-> this.pautaMock.setUsuario(null));
    }
    @Test
    @DisplayName("Não deve ser possível setar um usuário não admin")
    void dadoTenhoUmUsuarioQueNaoEAdminQuandoTentoSetarUsuarioEntaoRetornarErro(){    
        assertThrows(IllegalArgumentException.class, ()-> this.pautaMock.setUsuario(this.usuarioNaoAdminMock));
    }

    @Test
    @DisplayName("Deve ser possível setar um assunto corretamente")
    void dadoTenhoUmAssuntoValidoQuandoTentoSetarAssuntoEntaoSetarAssunto(){
        String novoAssunto = "Você sabe dirigir?";
        assertDoesNotThrow(()-> this.pautaMock.setAssunto(novoAssunto));
        assertEquals(this.pautaMock.getAssunto(), this.pautaMock.getAssunto());
    }

    @Test
    @DisplayName("Deve ser possível setar um assunto vazio")
    void dadoTenhoUmAssuntoVazioQuandoTentoSetarAssuntoEntaoRetornarErro(){
        assertThrows(IllegalArgumentException.class, ()-> this.pautaMock.setAssunto("  "));
    }

    @Test
    @DisplayName("Deve ser possível setar uma categoria corretamente")
    void dadoTenhoUmaCategoriaValidaQuandoTentoSetarCategoriaEntaoSetarCategoria(){
        assertDoesNotThrow(()-> this.pautaMock.setCategoria(Categoria.EDUCACAO.toString()));
        assertEquals(this.pautaMock.getCategoria(), this.pautaMock.getCategoria());
    }
    @Test
    @DisplayName("Não deve ser possível setar uma categoria inválida")
    void dadoTenhoUmaCategoriaInvalidaQuandoTentoSetarCategoriaEntaoRetornarErro(){
        String categoriaInvalida = "CATEGORIA_INVALIDA";
        assertThrows(IllegalArgumentException.class, ()-> this.pautaMock.setCategoria(categoriaInvalida));
    }
 
}
