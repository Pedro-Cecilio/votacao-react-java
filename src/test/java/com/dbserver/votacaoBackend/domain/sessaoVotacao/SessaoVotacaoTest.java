package com.dbserver.votacaoBackend.domain.sessaoVotacao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDateTime;
import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.voto.Voto;

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
        this.usuarioAdminMock = new Usuario(1L, "João", "Silva", "12345678900", true);
        this.pautaMock = new Pauta("Você está feliz hoje?", Categoria.CULTURA_LAZER.toString(), this.usuarioAdminMock);
        this.dataAbertura = LocalDateTime.now();
        this.novaDataAbertura = null;
        this.novaDataFechamento = null;
        this.dataFechamento = this.dataAbertura.plusMinutes(5);
        this.sessaoVotacaoAtivaMock = new SessaoVotacao(this.pautaMock, this.dataAbertura, this.dataFechamento);
        this.votoMock = new Voto(this.usuarioAdminMock.getCpf(), this.usuarioAdminMock);
    }

    @Test
    @DisplayName("Deve ser possível setar uma pauta corretamente")
    void givenPossuoUmaPautaValidaWhenTentoSetarPautaThenDefinirNovaPauta() {
        Pauta pautaSetMock = new Pauta("Você sabe dirigir?", Categoria.TRANSPORTE.toString(), this.usuarioAdminMock);

        assertDoesNotThrow(() -> this.sessaoVotacaoAtivaMock.setPauta(pautaSetMock));
        assertEquals(pautaSetMock, this.sessaoVotacaoAtivaMock.getPauta());
    }

    @Test
    @DisplayName("Não deve ser possível setar uma pauta nula")
    void givenPossuoUmaPautaNulaWhenTentoSetarPautaThenRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> this.sessaoVotacaoAtivaMock.setPauta(null));
    }

    @Test
    @DisplayName("Deve ser possível setar uma data de abertura corretamente")
    void givenPossuoUmaDataDeAberturaValidaWhenTentoSetarDataDeAberturaThenDefinirNovaDataDeAbertura() {
        this.novaDataAbertura = this.dataFechamento.minusMinutes(5);

        assertDoesNotThrow(() -> this.sessaoVotacaoAtivaMock.setDataAbertura(novaDataAbertura));
        assertTrue(this.sessaoVotacaoAtivaMock.getDataAbertura().isEqual(novaDataAbertura));
    }
    @Test
    @DisplayName("Não deve ser possível setar uma data de abertura nula")
    void givenPossuoUmaDataDeAberturaNulaWhenTentoSetarDataDeAberturaThenRetornarRErro() {
        assertThrows(IllegalArgumentException.class, () -> this.sessaoVotacaoAtivaMock.setDataAbertura(null));
    }

    @Test
    @DisplayName("Não deve ser possível setar uma data de abertura menor que a data atual")
    void givenPossuoUmaDataDeAberturaMenorQueDataAtualWhenTentoSetarDataDeAberturaThenRetornarErro() {
        this.novaDataAbertura = LocalDateTime.now().minusMinutes(5);

        assertThrows(IllegalArgumentException.class, () -> this.sessaoVotacaoAtivaMock.setDataAbertura(this.novaDataAbertura));
    }
    
    @Test
    @DisplayName("Deve ser possível setar data de fechamento corretamente")
    void givenPossuoUmaDataDeFechamentoValidaWhenTentoSetarDataDeFechamentoThenDefinirNovaDataDeFechamento() {
        this.novaDataFechamento = this.dataAbertura.plusMinutes(4);

        assertDoesNotThrow(() -> this.sessaoVotacaoAtivaMock.setDataFechamento(this.novaDataFechamento));
        assertEquals(this.novaDataFechamento, this.sessaoVotacaoAtivaMock.getDataFechamento());
    }

    @Test
    @DisplayName("Não deve ser possível setar uma data de fechamento nula")
    void givenPossuoUmaDataDeFechamentoNulaWhenTentoSetarDataDeFechamentoThenRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> this.sessaoVotacaoAtivaMock.setDataAbertura(null));
    }
    @Test
    @DisplayName("Não deve ser possível setar uma data de fechamento menor que a data de abertura")
    void givenPossuoUmaDataDeFechamentoMenorQueDataAberturaWhenTentoSetarDataDeFechamentoThenRetornarErro() {
        this.novaDataFechamento = this.dataAbertura.minusMinutes(5);

        assertThrows(IllegalArgumentException.class, () -> this.sessaoVotacaoAtivaMock.setDataAbertura(this.novaDataFechamento));
    }

    @Test
    @DisplayName("Deve ser possível setarVotoPositivo passando Voto corretamente")
    void givenPossuoVotoValidoThenTentoSetarVotoPositivoWhenAdicionarNovoVotoPositivo(){
        assertDoesNotThrow(() -> this.sessaoVotacaoAtivaMock.setVotosPositivos(this.votoMock));
        assertEquals(1, this.sessaoVotacaoAtivaMock.getVotosPositivos().size());
    }
    @Test
    @DisplayName("Não deve ser possível setarVotoPositivo passando Voto nulo")
    void givenPossuoVotoInvalidoThenTentoSetarVotoPositivoWhenRetornarErro(){
        assertThrows(IllegalArgumentException.class, () -> this.sessaoVotacaoAtivaMock.setVotosPositivos(null));
    }

    @Test
    @DisplayName("Deve ser possível setarVotoNegativo passando Voto corretamente")
    void givenPossuoVotoValidoThenTentoSetarVotoNegativoWhenAdicionarNovoVotoNegativo(){
        assertDoesNotThrow(() -> this.sessaoVotacaoAtivaMock.setVotosNegativos(this.votoMock));
        assertEquals(1, this.sessaoVotacaoAtivaMock.getVotosNegativos().size());
    }

    @Test
    @DisplayName("Não deve ser possível setarVotoNegativo passando Voto nulo")
    void givenPossuoVotoInvalidoThenTentoSetarVotoNegativoWhenRetornarErro(){
        assertThrows(IllegalArgumentException.class, () -> this.sessaoVotacaoAtivaMock.setVotosNegativos(null));
    }

    @Test
    @DisplayName("Deve obter ativa true")
    void givenPossuoUmaSessaoVotacaoAtivaWhenTentoObterAtivaThenRetornarTrue(){
        assertTrue(this.sessaoVotacaoAtivaMock.isAtiva());
    }
    @Test
    @DisplayName("Deve obter ativa false")
    void givenNaoPossuoUmaSessaoVotacaoAtivaWhenTentoObterAtivaThenRetornarTrue(){
        this.sessaoVotacaoAtivaMock.setDataFechamento(this.dataAbertura);
        
        assertFalse(this.sessaoVotacaoAtivaMock.isAtiva());
    }

}
