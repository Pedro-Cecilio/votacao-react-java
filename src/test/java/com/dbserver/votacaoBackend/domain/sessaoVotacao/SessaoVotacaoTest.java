package com.dbserver.votacaoBackend.domain.sessaoVotacao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.validacoes.PautaValidacoes;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.validacoes.SessaoVotacaoValidacoes;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.voto.Voto;
import com.dbserver.votacaoBackend.fixture.pauta.PautaFixture;
import com.dbserver.votacaoBackend.fixture.sessaoVotacao.SessaoVotacaoFixture;
import com.dbserver.votacaoBackend.fixture.usuario.UsuarioFixture;
import com.dbserver.votacaoBackend.fixture.voto.VotoFixture;

@SpringBootTest
class SessaoVotacaoTest {

    private Pauta pautaMock;
    private Usuario usuarioAdminMock;
    private SessaoVotacao sessaoVotacaoAtivaMock;
    private Voto votoMock;

    @BeforeEach
    void configurar() {
        this.usuarioAdminMock = UsuarioFixture.usuarioAdmin();
        this.pautaMock = PautaFixture.pautaTransporte(usuarioAdminMock);
        this.sessaoVotacaoAtivaMock = SessaoVotacaoFixture.sessaoVotacaoAtiva(pautaMock);
        this.votoMock = VotoFixture.gerarVotoInterno(UsuarioFixture.usuarioNaoAdmin());
    }

    @Test
    @DisplayName("Deve ser possível setarVotoPositivo passando Voto corretamente")
    void dadoPossuoVotoValidoEntaoTentoSetarVotoPositivoQuandoAdicionarNovoVotoPositivo(){
        assertDoesNotThrow(() -> this.sessaoVotacaoAtivaMock.setVotosPositivos(this.votoMock));
        assertEquals(1, this.sessaoVotacaoAtivaMock.getVotosPositivos().size());
    }
    @Test
    @DisplayName("Não deve ser possível setarVotoPositivo passando Voto nulo")
    void dadoPossuoVotoInvalidoEntaoTentoSetarVotoPositivoQuandoRetornarErro(){
        assertThrows(IllegalArgumentException.class, () -> this.sessaoVotacaoAtivaMock.setVotosPositivos(null));
    }

    @Test
    @DisplayName("Deve ser possível setarVotoNegativo passando Voto corretamente")
    void dadoPossuoVotoValidoEntaoTentoSetarVotoNegativoQuandoAdicionarNovoVotoNegativo(){
        assertDoesNotThrow(() -> this.sessaoVotacaoAtivaMock.setVotosNegativos(this.votoMock));
        assertEquals(1, this.sessaoVotacaoAtivaMock.getVotosNegativos().size());
    }

    @Test
    @DisplayName("Não deve ser possível setarVotoNegativo passando Voto nulo")
    void dadoPossuoVotoInvalidoEntaoTentoSetarVotoNegativoQuandoRetornarErro(){
        assertThrows(IllegalArgumentException.class, () -> this.sessaoVotacaoAtivaMock.setVotosNegativos(null));
    }

    @Test
    @DisplayName("Deve obter ativa true")
    void dadoPossuoUmaSessaoVotacaoAtivaQuandoTentoObterAtivaEntaoRetornarTrue(){
        assertTrue(this.sessaoVotacaoAtivaMock.isAtiva());
    }

     @Test
    @DisplayName("Deve validar data de abertura válida")
    void dadoDataDeAberturaValidaQuandoValidarEntaoNaoRetornarErro() {
        LocalDateTime dataAbertura = LocalDateTime.now().plusHours(1);
        assertDoesNotThrow(() -> SessaoVotacaoValidacoes.validarDataDeAbertura(dataAbertura));
    }

    @Test
    @DisplayName("Deve retornar erro ao validar data de abertura nula")
    void dadoDataDeAberturaNulaQuandoValidarEntaoRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> SessaoVotacaoValidacoes.validarDataDeAbertura(null));
    }

    @Test
    @DisplayName("Deve retornar erro ao validar data de abertura menor que a data atual")
    void dadoDataDeAberturaNoPassadoQuandoValidarEntaoRetornarErro() {
        LocalDateTime dataAbertura = LocalDateTime.now().minusHours(1);
        assertThrows(IllegalArgumentException.class, () -> SessaoVotacaoValidacoes.validarDataDeAbertura(dataAbertura));
    }

    @Test
    @DisplayName("Deve validar data de fechamento válida")
    void dadoDataDeFechamentoValidaQuandoValidarEntaoNaoRetornarErro() {
        LocalDateTime dataAbertura = LocalDateTime.now().plusHours(1);
        LocalDateTime dataFechamento = dataAbertura.plusHours(2);
        assertDoesNotThrow(() -> SessaoVotacaoValidacoes.validarDataDeFechamento(dataFechamento, dataAbertura));
    }

    @Test
    @DisplayName("Deve retornar erro ao validar data de fechamento nula")
    void dadoDataDeFechamentoNulaQuandoValidarEntaoRetornarErro() {
        LocalDateTime dataAbertura = LocalDateTime.now().plusHours(1);
        assertThrows(IllegalArgumentException.class, () -> SessaoVotacaoValidacoes.validarDataDeFechamento(null, dataAbertura));
    }

    @Test
    @DisplayName("Deve retornar erro ao validar data de fechamento menor que a data de abertura")
    void dadoDataDeFechamentoAntesDaDataDeAberturaQuandoValidarEntaoRetornarErro() {
        LocalDateTime dataAbertura = LocalDateTime.now().plusHours(1);
        LocalDateTime dataFechamento = dataAbertura.minusHours(1);
        assertThrows(IllegalArgumentException.class, () -> SessaoVotacaoValidacoes.validarDataDeFechamento(dataFechamento, dataAbertura));
    }

}
