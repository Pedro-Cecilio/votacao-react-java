package com.dbserver.votacaoBackend.domain.voto.validacoes;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class VotoValidacoesTest {
    private VotoValidacoes votoValidacoes;

    @BeforeEach
    void configurar() {
        this.votoValidacoes = new VotoValidacoes();
    }

    @Test
    @DisplayName("Deve retornar erro ao validar se voto não é nulo ao enviar voto nulo")
    void dadoTenhoVotoNuloQuandoTentoValidarSeVotoNaoENuloEntaoDeveRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> this.votoValidacoes.validarVotoNaoNulo(null));
    }
}   
