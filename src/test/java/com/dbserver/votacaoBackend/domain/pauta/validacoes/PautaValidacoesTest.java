package com.dbserver.votacaoBackend.domain.pauta.validacoes;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.dbserver.votacaoBackend.domain.pauta.dto.CriarPautaDto;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.fixture.pauta.PautaFixture;
import com.dbserver.votacaoBackend.fixture.usuario.UsuarioFixture;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PautaValidacoesTest {

    @InjectMocks
    private PautaValidacoes pautaValidacoes;

    @Mock
    private CriarPautaDto criarPautaDtoMock;

    @Test
    @DisplayName("Deve validar se criarPautaDto não é nula")
    void dadoTenhoCriarPautaDtoNaoNulaQuandoTentoValidarSeNaoENulaEntaoValidarCorretamente(){
        assertDoesNotThrow(() -> this.pautaValidacoes.validarCriarPautaDtoNaoNula(this.criarPautaDtoMock));
    }

    @Test
    @DisplayName("Deve retornar erro ao validar se criarPautaDto não é nula, ao receber criarPautaDto nula")
    void dadoTenhoCriarPautaDtoaNuloQuandoTentoValidarSeNaoENulaEntaoRetornarErro(){
        assertThrows(IllegalArgumentException.class, () -> this.pautaValidacoes.validarCriarPautaDtoNaoNula(null));
    }

    @Test
    @DisplayName("Deve validar se o usuário da pauta não é nulo")
    void dadoTenhoUsuarioNaoNuloQuandoTentoValidarUsuarioDaPautaEntaoValidarCorretamente(){
        Usuario usuario = UsuarioFixture.usuarioAdmin();
        assertDoesNotThrow(() -> PautaValidacoes.validarUsuarioDaPauta(usuario));
    }

    @Test
    @DisplayName("Deve retornar erro ao validar se o usuário da pauta não é nulo, ao receber usuário nulo")
    void dadoTenhoUsuarioNuloQuandoTentoValidarUsuarioDaPautaEntaoRetornarErro(){
        assertThrows(IllegalArgumentException.class, () -> PautaValidacoes.validarUsuarioDaPauta(null));
    }
    @Test
    @DisplayName("Deve retornar erro ao validar se o usuário da pauta não é nulo, ao receber usuário nulo")
    void dadoTenhoUsuarioNaoAdminQuandoTentoValidarUsuarioDaPautaEntaoRetornarErro(){
        Usuario usuario = UsuarioFixture.usuarioNaoAdmin();
        assertThrows(IllegalArgumentException.class, () -> PautaValidacoes.validarUsuarioDaPauta(usuario));
    }

    @Test
    @DisplayName("Deve validar se o assunto da pauta é informado")
    void dadoTenhoAssuntoInformadoQuandoTentoValidarSeInformadoEntaoValidarCorretamente(){
        assertDoesNotThrow(() -> PautaValidacoes.validarAssunto(PautaFixture.ASSUNTO_TRANSPORTE));
    }

    @Test
    @DisplayName("Deve retornar erro ao validar se o assunto da pauta é informado, ao receber assunto nulo")
    void dadoTenhoAssuntoNuloQuandoTentoValidarSeInformadoEntaoRetornarErro(){
        assertThrows(IllegalArgumentException.class, () -> PautaValidacoes.validarAssunto(null));
    }

    @Test
    @DisplayName("Deve retornar erro ao validar se o assunto da pauta é informado, ao receber assunto vazio")
    void dadoTenhoAssuntoVazioQuandoTentoValidarSeInformadoEntaoRetornarErro(){
        assertThrows(IllegalArgumentException.class, () -> PautaValidacoes.validarAssunto(""));
    }

    @Test
    @DisplayName("Deve lançar erro ao validar se pauta não é nula ao informar pauta nula")
    void dadoTenhoPautaNulaQuandoTentoValidarSeNaoENulaEntaoRetornarErro(){
        assertThrows(IllegalArgumentException.class, () -> PautaValidacoes.validarPautaNaoNula(null));
    }

}
