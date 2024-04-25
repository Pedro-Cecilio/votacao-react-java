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

@SpringBootTest
class PautaTest {

    private Pauta pautaMock;
    private Usuario usuarioAdminMock;
    private Usuario usuarioNaoAdminMock;
    private Usuario usuarioSetMock;
    private String assuntoValido;
    private String categoriaInvalida;


    @BeforeEach
    void configurar() {
        this.usuarioAdminMock = new Usuario(1L, "João", "Silva", "12345678900", true);
        this.usuarioNaoAdminMock = new Usuario(1L, "Pedro", "Cecilio", "12345678912", false);
        this.usuarioSetMock = new Usuario(1L, "Pedro", "Cecilio", "12345678912", true);

        this.assuntoValido = "Você sabe dirigir?";
        this.categoriaInvalida = "CATEGORIA_INVALIDA";

        this.pautaMock = new Pauta("Você está feliz hoje?", Categoria.CULTURA_LAZER.toString(), this.usuarioAdminMock);
    }
    
    @Test
    @DisplayName("Deve ser possível setar um usuário corretamente")
    void givenTenhoUmUsuarioValidoWhenTentoSetarUsuarioThenSetarUsuario(){
        assertDoesNotThrow(()-> this.pautaMock.setUsuario(usuarioSetMock));
        assertEquals(this.usuarioSetMock.getId(), this.pautaMock.getUsuario().getId());
    }
    @Test
    @DisplayName("Não deve ser possível setar um usuário nulo")
    void givenTenhoUmUsuarioNuloWhenTentoSetarUsuarioThenRetornarErro(){
        assertThrows(IllegalArgumentException.class, ()-> this.pautaMock.setUsuario(null));
    }
    @Test
    @DisplayName("Não deve ser possível setar um usuário não admin")
    void givenTenhoUmUsuarioQueNaoEAdminWhenTentoSetarUsuarioThenRetornarErro(){    
        assertThrows(IllegalArgumentException.class, ()-> this.pautaMock.setUsuario(this.usuarioNaoAdminMock));
    }

    @Test
    @DisplayName("Deve ser possível setar um assunto corretamente")
    void givenTenhoUmAssuntoValidoWhenTentoSetarAssuntoThenSetarAssunto(){
        assertDoesNotThrow(()-> this.pautaMock.setAssunto(this.assuntoValido));
        assertEquals(this.pautaMock.getAssunto(), this.pautaMock.getAssunto());
    }

    @Test
    @DisplayName("Deve ser possível setar um assunto vazio")
    void givenTenhoUmAssuntoVazioWhenTentoSetarAssuntoThenRetornarErro(){
        assertThrows(IllegalArgumentException.class, ()-> this.pautaMock.setAssunto("  "));
    }

    @Test
    @DisplayName("Deve ser possível setar uma categoria corretamente")
    void givenTenhoUmaCategoriaValidaWhenTentoSetarCategoriaThenSetarCategoria(){
        assertDoesNotThrow(()-> this.pautaMock.setCategoria(Categoria.EDUCACAO.toString()));
        assertEquals(this.pautaMock.getCategoria(), this.pautaMock.getCategoria());
    }
    @Test
    @DisplayName("Não deve ser possível setar uma categoria inválida")
    void givenTenhoUmaCategoriaInvalidaWhenTentoSetarCategoriaThenRetornarErro(){
        assertThrows(IllegalArgumentException.class, ()-> this.pautaMock.setCategoria(this.categoriaInvalida));
    }
 
}
