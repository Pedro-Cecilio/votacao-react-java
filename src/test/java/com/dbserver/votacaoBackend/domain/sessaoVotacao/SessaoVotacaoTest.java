package com.dbserver.votacaoBackend.domain.sessaoVotacao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.time.LocalDateTime;
import com.dbserver.votacaoBackend.domain.pauta.Pauta;
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
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    private LocalDateTime novaDataAbertura;
    private LocalDateTime novaDataFechamento;
    private Voto votoMock;

    @BeforeEach
    void configurar() {
        this.usuarioAdminMock = UsuarioFixture.usuarioAdmin();
        this.pautaMock = PautaFixture.pautaTransporte(usuarioAdminMock);
        this.dataAbertura = LocalDateTime.now();
        this.novaDataAbertura = null;
        this.novaDataFechamento = null;
        this.dataFechamento = this.dataAbertura.plusMinutes(5);
        this.sessaoVotacaoAtivaMock = SessaoVotacaoFixture.sessaoVotacaoAtiva(pautaMock);
        this.votoMock = VotoFixture.gerarVotoInterno(UsuarioFixture.usuarioNaoAdmin());
    }

    @Test
    @DisplayName("Deve ser possível setar uma pauta corretamente")
    void dadoPossuoUmaPautaValidaQuandoTentoSetarPautaEntaoDefinirNovaPauta() {
        Pauta pautaSetMock = PautaFixture.pautaSaude(this.usuarioAdminMock);

        assertDoesNotThrow(() -> this.sessaoVotacaoAtivaMock.setPauta(pautaSetMock));
        assertEquals(pautaSetMock, this.sessaoVotacaoAtivaMock.getPauta());
    }

    @Test
    @DisplayName("Não deve ser possível setar uma pauta nula")
    void dadoPossuoUmaPautaNulaQuandoTentoSetarPautaEntaoRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> this.sessaoVotacaoAtivaMock.setPauta(null));
    }

    @Test
    @DisplayName("Deve ser possível setar uma data de abertura corretamente")
    void dadoPossuoUmaDataDeAberturaValidaQuandoTentoSetarDataDeAberturaEntaoDefinirNovaDataDeAbertura() {
        this.novaDataAbertura = this.dataFechamento.minusMinutes(5);

        assertDoesNotThrow(() -> this.sessaoVotacaoAtivaMock.setDataAbertura(novaDataAbertura));
        assertTrue(this.sessaoVotacaoAtivaMock.getDataAbertura().isEqual(novaDataAbertura));
    }
    @Test
    @DisplayName("Não deve ser possível setar uma data de abertura nula")
    void dadoPossuoUmaDataDeAberturaNulaQuandoTentoSetarDataDeAberturaEntaoRetornarRErro() {
        assertThrows(IllegalArgumentException.class, () -> this.sessaoVotacaoAtivaMock.setDataAbertura(null));
    }

    @Test
    @DisplayName("Não deve ser possível setar uma data de abertura menor que a data atual")
    void dadoPossuoUmaDataDeAberturaMenorQueDataAtualQuandoTentoSetarDataDeAberturaEntaoRetornarErro() {
        this.novaDataAbertura = LocalDateTime.now().minusMinutes(5);

        assertThrows(IllegalArgumentException.class, () -> this.sessaoVotacaoAtivaMock.setDataAbertura(this.novaDataAbertura));
    }
    
    @Test
    @DisplayName("Deve ser possível setar data de fechamento corretamente")
    void dadoPossuoUmaDataDeFechamentoValidaQuandoTentoSetarDataDeFechamentoEntaoDefinirNovaDataDeFechamento() {
        this.novaDataFechamento = this.dataAbertura.plusMinutes(4);

        assertDoesNotThrow(() -> this.sessaoVotacaoAtivaMock.setDataFechamento(this.novaDataFechamento));
        assertEquals(this.novaDataFechamento, this.sessaoVotacaoAtivaMock.getDataFechamento());
    }

    @Test
    @DisplayName("Não deve ser possível setar uma data de fechamento nula")
    void dadoPossuoUmaDataDeFechamentoNulaQuandoTentoSetarDataDeFechamentoEntaoRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> this.sessaoVotacaoAtivaMock.setDataAbertura(null));
    }
    @Test
    @DisplayName("Não deve ser possível setar uma data de fechamento menor que a data de abertura")
    void dadoPossuoUmaDataDeFechamentoMenorQueDataAberturaQuandoTentoSetarDataDeFechamentoEntaoRetornarErro() {
        this.novaDataFechamento = this.dataAbertura.minusMinutes(5);

        assertThrows(IllegalArgumentException.class, () -> this.sessaoVotacaoAtivaMock.setDataAbertura(this.novaDataFechamento));
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

}
