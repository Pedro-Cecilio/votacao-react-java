package com.dbserver.votacaoBackend.testUtils;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DadosTestesParametrizados {

    public static Stream<Arguments> dadosInvalidosParaSetarSenha() {
        return Stream.of(
            Arguments.of(""),
            Arguments.of("123"), 
            Arguments.of((String)null)
        );
    }

    public static Stream<Arguments> dadosInvalidosParaSetarEmail() {
        return Stream.of(
            Arguments.of(""),
            Arguments.of("invalidemail.com"),
            Arguments.of((String)null)
        );
    }
    public static Stream<Arguments> dadosInvalidosParaAtualizarUsuario() {
        return Stream.of(
            Arguments.of(null, "NovoSobrenome"),
            Arguments.of(" ", "NovoSobrenome"),
            Arguments.of("no", "NovoSobrenome"),
            Arguments.of("NomeMuitoGrandeParaSerUsado", "NovoSobrenome"),
            Arguments.of("NovoNome", null),
            Arguments.of("NovoNome", " "),
            Arguments.of("NovoNome", "s"),
            Arguments.of("NovoNome", "SobrenomeMuitoGrandeParaSerUsado")
        );
    }
}
