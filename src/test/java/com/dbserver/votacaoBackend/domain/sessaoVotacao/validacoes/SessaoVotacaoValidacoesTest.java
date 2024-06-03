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
import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.fixture.sessaoVotacao.SessaoVotacaoFixture;

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
        assertDoesNotThrow(() -> SessaoVotacaoValidacoes.validarSessaoVotacaoNaoNula(sessaoVotacaoMock));
    }

    @Test
    @DisplayName("Deve retornar erro ao validar ao passar sessao votacao nula ")
    void dadoPossuoSessaoVotacaoNulaQuandoTentoValidarEntaoRetornarErro() {
        assertThrows(IllegalArgumentException.class,
                () -> SessaoVotacaoValidacoes.validarSessaoVotacaoNaoNula(null));
    }

    @Test
    @DisplayName("Deve validar corretamente ao passar sessao votacao ativa ")
    void dadoPossuoSessaoVotacaoAtivaQuandoTentoValidarEntaoRetornarValidarCorretamente() {
        this.sessaoVotacaoMock = SessaoVotacaoFixture.sessaoVotacaoAtiva(pautaMock);

        assertDoesNotThrow(() -> SessaoVotacaoValidacoes.validarSessaoVotacaoAtiva(sessaoVotacaoMock));
    }

    @Test
    @DisplayName("Deve retornar erro ao passar sessao votacao inativa ")
    void dadoPossuoSessaoVotacaoInativaQuandoTentoValidarEntaoRetornarValidarCorretamente() {
        this.sessaoVotacaoMock = SessaoVotacaoFixture.sessaoVotacaoInativa(pautaMock);

        assertThrows(IllegalStateException.class,
                () -> SessaoVotacaoValidacoes.validarSessaoVotacaoAtiva(sessaoVotacaoMock));
    }

}
