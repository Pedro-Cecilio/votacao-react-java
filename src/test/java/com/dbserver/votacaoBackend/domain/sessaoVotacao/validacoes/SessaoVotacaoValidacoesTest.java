package com.dbserver.votacaoBackend.domain.sessaoVotacao.validacoes;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.fixture.SessaoVotacaoFixture;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class SessaoVotacaoValidacoesTest {
    @InjectMocks
    private SessaoVotacaoValidacoes sessaoVotacaoValidacoes;
    @Mock
    private SessaoVotacao sessaoVotacaoMock;
    @Mock
    private Pauta pautaMock;

    @Test
    @DisplayName("Deve validar corretamente ao passar sessao votacao nao nula ")
    void dadoPossuoSessaoVotacaoNaoNulaQuandoTentoValidarEntaoRetornarValidarCorretamente() {
        assertDoesNotThrow(() -> this.sessaoVotacaoValidacoes.validarSessaoVotacaoNaoNula(sessaoVotacaoMock));
    }

    @Test
    @DisplayName("Deve retornar erro ao validar ao passar sessao votacao nula ")
    void dadoPossuoSessaoVotacaoNulaQuandoTentoValidarEntaoRetornarErro() {
        assertThrows(IllegalArgumentException.class,
                () -> this.sessaoVotacaoValidacoes.validarSessaoVotacaoNaoNula(null));
    }

    @Test
    @DisplayName("Deve validar corretamente ao passar sessao votacao ativa ")
    void dadoPossuoSessaoVotacaoAtivaQuandoTentoValidarEntaoRetornarValidarCorretamente() {
        this.sessaoVotacaoMock = SessaoVotacaoFixture.sessaoVotacaoAtiva(pautaMock);

        assertDoesNotThrow(() -> this.sessaoVotacaoValidacoes.validarSessaoVotacaoAtiva(sessaoVotacaoMock));
    }

    @Test
    @DisplayName("Deve retornar erro ao passar sessao votacao inativa ")
    void dadoPossuoSessaoVotacaoInativaQuandoTentoValidarEntaoRetornarValidarCorretamente() {
        this.sessaoVotacaoMock = SessaoVotacaoFixture.sessaoVotacaoInativa(pautaMock);

        assertThrows(IllegalStateException.class,
                () -> this.sessaoVotacaoValidacoes.validarSessaoVotacaoAtiva(sessaoVotacaoMock));
    }

}
