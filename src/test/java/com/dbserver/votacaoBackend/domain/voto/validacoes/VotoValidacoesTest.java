package com.dbserver.votacaoBackend.domain.voto.validacoes;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class VotoValidacoesTest {

    @Test
    @DisplayName("Deve retornar erro ao validar se voto não é nulo ao enviar voto nulo")
    void dadoTenhoVotoNuloQuandoTentoValidarSeVotoNaoENuloEntaoDeveRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> VotoValidacoes.validarVotoNaoNulo(null));
    }
}   
