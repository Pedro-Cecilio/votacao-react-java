package com.dbserver.votacaoBackend.domain.autenticacao.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.repository.AutenticacaoRepository;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.utils.Utils;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTest {
    @InjectMocks
    private AutenticacaoService autenticacaoService;

    @Mock
    private AutenticacaoRepository autenticacaoRepository;

    @Mock
    private Utils utils;

    private Usuario usuarioMock;
    private Autenticacao autenticacaoMock;

    @BeforeEach
    void configurar() {
        this.usuarioMock = new Usuario(1L, "João", "Silva", "12345678900", false);
        this.autenticacaoMock = new Autenticacao("example@example.com", "senha123");
    }

    @AfterEach
    void limpar() {
        this.usuarioMock = null;
        this.autenticacaoMock = null;
        this.autenticacaoRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve ser possível criar uma autenticacao")
    void givenTenhoUmUsuarioCadastradoEDadosDeAutenticacaoWhenTentoCriarAutenticacaoThenRetornarAutenticacaoCriada() {
        when(this.autenticacaoRepository.save(this.autenticacaoMock)).thenReturn(this.autenticacaoMock);
        this.autenticacaoService.criarAutenticacao(autenticacaoMock, usuarioMock);
        verify(this.autenticacaoRepository, times(1)).save(this.autenticacaoMock);
    }

    @Test
    @DisplayName("Deve ser possível deletar uma autenticacao")
    void givenTenhoUmaAutenticacaoIdWhenTentoDeletarAutenticacaoThenDeletarAutenticacao() {
        when(this.autenticacaoRepository.findById(1L)).thenReturn(Optional.of(this.autenticacaoMock));
        this.autenticacaoService.deletarAutenticacao(1L);
        verify(this.autenticacaoRepository, times(1)).delete(this.autenticacaoMock);
    }

    @Test
    @DisplayName("Não deve ser possível deletar uma autenticacao inexistente")
    void givenTenhoUmaAutenticacaoIdInexistenteWhenTentoDeletarAutenticacaoThenRetornarUmaExcecao() {
        when(this.autenticacaoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> this.autenticacaoService.deletarAutenticacao(1L));

    }

    @Test
    @DisplayName("Deve ser possível se validar dados de autenticacao ao passar dados válidos")
    void givenTenhoDadosDeAutenticacaoValidosWhenValidoEssesDadosThenRetornarTrue() {
        when(this.autenticacaoRepository.findByEmail(this.autenticacaoMock.getEmail()))
                .thenReturn(Optional.of(this.autenticacaoMock));
        when(this.utils.validarSenha(this.autenticacaoMock.getSenha(), this.autenticacaoMock.getSenha()))
                .thenReturn(true);

        boolean resposta = this.autenticacaoService.validarDadosAutenticacao(this.autenticacaoMock.getEmail(),
                this.autenticacaoMock.getSenha());

        assertTrue(resposta);
    }

    @Test
    @DisplayName("Deve retornar false ao validar dados de autenticação ao passar email inexistente")
    void givenTenhoDadosDeAutenticacaoComEmailInexistenteWhenValidoEssesDadosThenRetornarFalse() {
        when(this.autenticacaoRepository.findByEmail(this.autenticacaoMock.getEmail())).thenReturn(Optional.empty());
        boolean resposta = this.autenticacaoService.validarDadosAutenticacao(this.autenticacaoMock.getEmail(),
                this.autenticacaoMock.getSenha());
        assertFalse(resposta);
    }

    @Test
    @DisplayName("Deve retornar false ao validar dados ao passar senha incorreta")
    void givenTenhoDadosDeAutenticacaoComSenhaIncorretaWhenValidoEssesDadosThenRetornarFalse() {
        when(this.autenticacaoRepository.findByEmail(this.autenticacaoMock.getEmail()))
                .thenReturn(Optional.of(this.autenticacaoMock));
        when(this.utils.validarSenha(this.autenticacaoMock.getSenha(),
                this.autenticacaoMock.getSenha()))
                .thenReturn(false);

        boolean resposta = this.autenticacaoService.validarDadosAutenticacao(this.autenticacaoMock.getEmail(),
                this.autenticacaoMock.getSenha());
        assertFalse(resposta);
    }

    @Test
    @DisplayName("Deve ser possivel buscar autenticação existente pelo email")
    void givenTenhoEmailDeUmaAutenticacaoExistenteWhenBuscoAutenticacaoPeloEmailThenRetornarAutenticacao() {
        when(this.autenticacaoRepository.findByEmail(this.autenticacaoMock.getEmail()))
                .thenReturn(Optional.of(this.autenticacaoMock));
        Autenticacao resposta = this.autenticacaoService.buscarAutenticacaoPeloEmail(this.autenticacaoMock.getEmail());
        assertEquals(this.autenticacaoMock.getId(), resposta.getId());
    }

    @Test
    @DisplayName("Deve ser falhar ao buscar autenticação inexistente pelo email")
    void givenTenhoEmailDeUmaAutenticacaoInexistenteWhenBuscoAutenticacaoPeloEmailThenRetornarErro() {
        when(this.autenticacaoRepository.findByEmail(this.autenticacaoMock.getEmail())).thenReturn(Optional.empty());
        String email = this.autenticacaoMock.getEmail();
        assertThrows(NoSuchElementException.class,
                () -> this.autenticacaoService.buscarAutenticacaoPeloEmail(email));
    }
}
