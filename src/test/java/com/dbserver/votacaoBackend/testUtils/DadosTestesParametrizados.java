package com.dbserver.votacaoBackend.testUtils;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;


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

    public static Stream<Arguments> dadosInvalidosParaSetarNomeDeUsuario() {
        return Stream.of(
            Arguments.of((String)null),
            Arguments.of(" "),
            Arguments.of("No"),
            Arguments.of("NomeMuitoGrandeParaSerUsado")
        );
    }
    public static Stream<Arguments> dadosInvalidosParaSetarSobrenomeDeUsuario() {
        return Stream.of(
            Arguments.of((String)null),
            Arguments.of(" "),
            Arguments.of("C"),
            Arguments.of("SbrenomeMuitoGrandeParaSerUsado")
        );
    }
    public static Stream<Arguments> dadosInvalidosParaSetarCpfDeUsuario() {
        return Stream.of(
            Arguments.of((String)null),
            Arguments.of(" "),
            Arguments.of("123456789"),
            Arguments.of("12345adc910")
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
