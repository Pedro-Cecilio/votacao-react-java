package com.dbserver.votacaoBackend.domain.autenticacao.validacoes;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.repository.AutenticacaoRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AutenticacaoValidacoesTest {

    @InjectMocks
    private AutenticacaoValidacoes autenticacaoValidacoes;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AutenticacaoRepository autenticacaoRepository;

    @Mock
    private Autenticacao autenticacao;

    @Test
    @DisplayName("Deve ser possível validar senha da autenticação")
    void dadoTenhoDadosCorretosQuandoTentoValidarSenhaDaAutenticacaoEntaoRetornarTrue() {
        when(this.passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        boolean resposta = this.autenticacaoValidacoes.validarSenhaDaAutenticacao(anyString(), anyString());
        assertTrue(resposta);
    }

    @Test
    @DisplayName("Deve retornar false ao validar senha da autenticação ao informar senhaEsperada null")
    void dadoTenhoSenhaEsperadaNullQuandoTentoValidarSenhaDaAutenticacaoEntaoRetornarFalse() {
        boolean resposta = this.autenticacaoValidacoes.validarSenhaDaAutenticacao(null, "senhaEncriptada");
        assertFalse(resposta);
    }

    @Test
    @DisplayName("Deve retornar false ao validar senha da autenticação ao informar senhaEncriptada null")
    void dadoTenhoSenhaEncriptadadNullQuandoTentoValidarSenhaDaAutenticacaoEntaoRetornarFalse() {
        boolean resposta = this.autenticacaoValidacoes.validarSenhaDaAutenticacao("senhaEsperada", null);
        assertFalse(resposta);
    }

    @Test
    @DisplayName("Deve validar se autenticacao não é nula")
    void dadoTenhoAutenticacaoNaoNulaQuandoTentoValidarSeAutenticacaoNaoENulaEntaoDeveValidarCorretametne() {

        assertDoesNotThrow(() -> this.autenticacaoValidacoes.validarAutenticacaoNaoNula(this.autenticacao));
    }

    @Test
    @DisplayName("Deve retornar erro ao validar se autenticacao não é nula")
    void dadoTenhoAutenticacaoNulaQuandoTentoValidarSeAutenticacaoNaoENulaEntaoDeveRetornarErro() {

        assertThrows(IllegalArgumentException.class,
                () -> this.autenticacaoValidacoes.validarAutenticacaoNaoNula(null));
    }


    @Test
    @DisplayName("Deve ser validar autenticacao por cpf e senha")
    void dadoTenhoCpfESenhaCompativeisQuandoTentoValidarAutenticacaoPorCpfESenhaEntaoDeveValidarCorretamente() {
        when(this.autenticacaoRepository.findByCpf(anyString())).thenReturn(Optional.of(this.autenticacao));
        when(this.autenticacao.getSenha()).thenReturn("senhaQualquer");
        when(this.passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        
        assertDoesNotThrow(() -> this.autenticacaoValidacoes.validarAutenticacaoPorCpfESenha("senhaQualquer", "12345678910"));
    }

    @Test
    @DisplayName("Deve retornar erro ao autenticacao por cpf e senha ao passar cpf inexistente")
    void dadoTenhoCpfInexistenteQuandoTentoValidarAutenticacaoPorCpfESenhaEntaoDeveRetornarErro() {
        when(this.autenticacaoRepository.findByCpf(anyString())).thenReturn(Optional.empty());
        
        assertThrows(BadCredentialsException.class, () -> this.autenticacaoValidacoes.validarAutenticacaoPorCpfESenha("senhaQualquer", "12345678910"));
    }
    @Test
    @DisplayName("Deve retornar erro ao autenticacao por cpf e senha ao passar senha incorreta")
    void dadoTenhoSenhaIncorretaQuandoTentoValidarAutenticacaoPorCpfESenhaEntaoDeveRetornarErro() {
        when(this.autenticacaoRepository.findByCpf(anyString())).thenReturn(Optional.of(this.autenticacao));

        when(this.autenticacao.getSenha()).thenReturn("senhaQualquer");
        when(this.passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> this.autenticacaoValidacoes.validarAutenticacaoPorCpfESenha("senhaQualquer", "12345678910"));
    }
}
