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
    void dadoTenhoPautaNulaQuandoTentoValidarSeNaoENulaEntaoRetornarErro(){
        assertThrows(IllegalArgumentException.class, () -> this.pautaValidacoes.validarCriarPautaDtoNaoNula(null));
    }
}
