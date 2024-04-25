package com.dbserver.votacaoBackend.domain.autenticacao.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Mock
    private PasswordEncoder passwordEncoder;

    private Usuario usuarioMock;
    private Autenticacao autenticacaoMock;
    private String senhaInvalida;
    private String senhaValida = "senha123";
    private String senhaValidaEncriptada = "$2a$10$/Y8PKwHdSFWafmyWpJDgHu2wFJ8ShhLGEJtfVAz15ZerS3O1rW8cm";

    @BeforeEach
    void configurar() {
        this.usuarioMock = new Usuario(1L, "João", "Silva", "12345678900", false);
        this.autenticacaoMock = new Autenticacao("example@example.com", senhaValida);
        this.senhaInvalida = "senhaInvalida";
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
        this.autenticacaoService.criarAutenticacao(this.autenticacaoMock, this.usuarioMock);
        verify(this.autenticacaoRepository, times(1)).save(this.autenticacaoMock);
    }

    @Test
    @DisplayName("Não deve ser possível criar uma autenticacao ao passar usuario nulo")
    void givenTenhoUmUsuarioNuloEDadosDeAutenticacaoWhenTentoCriarAutenticacaoThenRetornarErro() {
        assertThrows(IllegalArgumentException.class,
                () -> this.autenticacaoService.criarAutenticacao(this.autenticacaoMock, null));
    }

    @Test
    @DisplayName("Não deve ser possível criar uma autenticacao ao passar autenticacao nula")
    void givenTenhoUmUsuarioEDadosDeAutenticacaoNuloWhenTentoCriarAutenticacaoThenRetornarErro() {
        assertThrows(IllegalArgumentException.class,
                () -> this.autenticacaoService.criarAutenticacao(null, this.usuarioMock));
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
    @DisplayName("Não deve ser possível deletar uma autenticacao inexistente")
    void givenTenhoUmaAutenticacaoIdNuloWhenTentoDeletarAutenticacaoThenRetornarUmaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> this.autenticacaoService.deletarAutenticacao(null));
    }

    @Test
    @DisplayName("Deve ser possível buscar autenticação por email e senha")
    void givenTenhoUmEmailEUmSenhaValidosWhenTentoBuscarPorEmailESenhaThenRetornarAutenticacao() {
        when(this.autenticacaoRepository.findByEmail(this.autenticacaoMock.getEmail()))
                .thenReturn(Optional.of(this.autenticacaoMock));
        when(this.passwordEncoder.matches(this.autenticacaoMock.getSenha(), this.autenticacaoMock.getSenha()))
                .thenReturn(true);
        Autenticacao autenticacao = this.autenticacaoService
                .buscarAutenticacaoPorEmailESenha(this.autenticacaoMock.getEmail(), this.autenticacaoMock.getSenha());
        assertEquals(this.autenticacaoMock.getEmail(), autenticacao.getEmail());
        assertEquals(this.autenticacaoMock.getSenha(), autenticacao.getSenha());
    }

    @Test
    @DisplayName("Não deve ser possível buscar autenticação com email inexistente")
    void givenTenhoUmEmailInexistenteWhenTentoBuscarPorEmailESenhaThenRetornarAutenticacao() {
        when(this.autenticacaoRepository.findByEmail(this.autenticacaoMock.getEmail())).thenReturn(Optional.empty());
        String email = this.autenticacaoMock.getEmail();
        String senha = this.autenticacaoMock.getSenha();
        assertThrows(BadCredentialsException.class, () -> this.autenticacaoService
                .buscarAutenticacaoPorEmailESenha(email, senha));
    }

    @Test
    @DisplayName("Não deve ser possível buscar autenticação com senha invalida")
    void givenTenhoUmaSenhaInvalidaWhenTentoBuscarPorEmailESenhaThenRetornarAutenticacao() {
        when(this.autenticacaoRepository.findByEmail(this.autenticacaoMock.getEmail()))
                .thenReturn(Optional.of(this.autenticacaoMock));
        when(this.autenticacaoService.validarSenhaDaAutenticacao(this.senhaInvalida, this.autenticacaoMock.getSenha()))
                .thenReturn(false);
        String email = this.autenticacaoMock.getEmail();
        String senha = this.senhaInvalida;
        assertThrows(BadCredentialsException.class, () -> this.autenticacaoService
                .buscarAutenticacaoPorEmailESenha(email, senha));
    }

    @Test
    @DisplayName("Deve ser possível validar senha da autenticação")
    void givenTenhoSenhasCompativeisWhenTentoValidarSenhaThenValidarCorretamente() {
        when(this.passwordEncoder.matches(this.senhaValida, this.senhaValidaEncriptada))
                .thenReturn(true);
        this.autenticacaoService.validarSenhaDaAutenticacao(this.senhaValida, this.senhaValidaEncriptada);
        verify(this.passwordEncoder).matches(this.senhaValida, this.senhaValidaEncriptada);
    }

    @Test
    @DisplayName("Deve ser possível encriptar senha da autenticação ao passar senha válida")
    void givenTenhoUmaSenhaValidaWhenTentoEncriptarSenhaThenRetornarSenhaEncriptada() {
        when(this.passwordEncoder.encode(this.senhaValida)).thenReturn(this.senhaValidaEncriptada);
        this.autenticacaoService.encriptarSenhaDaAutenticacao(senhaInvalida);
        verify(this.passwordEncoder).encode(this.senhaValida);
    }

    @Test
    @DisplayName("Não deve ser possível encriptar senha da autenticação ao passar senha nula")
    void givenTenhoUmaSenhaNulaWhenTentoEncriptarSenhaThenRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> this.autenticacaoService.encriptarSenhaDaAutenticacao(null));
    }

    @Test
    @DisplayName("Não deve ser possível encriptar senha da autenticação ao passar senha vazia")
    void givenTenhoUmaSenhaVaziaWhenTentoEncriptarSenhaThenRetornarErro() {
        assertThrows(IllegalArgumentException.class,
                () -> this.autenticacaoService.encriptarSenhaDaAutenticacao("   "));
    }

    @Test
    @DisplayName("Deve ser possível validar autenticação ao passar cpf e senha validos")
    void givenTenhoUmCpfEUmaSenhaValidosWhenTentoValidarAutenticacaoPorCpfESenhaThenExecutarSemErros() {
        when(this.autenticacaoRepository.findByCpf(this.usuarioMock.getCpf()))
                .thenReturn(Optional.of(this.autenticacaoMock));
        when(this.autenticacaoService.validarSenhaDaAutenticacao(this.senhaValida, autenticacaoMock.getSenha()))
                .thenReturn(true);

        assertDoesNotThrow(() -> this.autenticacaoService.validarAutenticacaoPorCpfESenha(this.usuarioMock.getCpf(),
                this.senhaValida));
    }

    @Test
    @DisplayName("Não deve ser possível validar autenticação ao passar cpf não cadastrado")
    void givenTenhoUmCpfNaoCadastradoWhenTentoValidarAutenticacaoPorCpfESenhaThenRetornarErro() {
        String cpf = this.usuarioMock.getCpf();

        when(this.autenticacaoRepository.findByCpf(cpf))
                .thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,
                () -> this.autenticacaoService.validarAutenticacaoPorCpfESenha(cpf, this.senhaValida));
    }

    @Test
    @DisplayName("Não deve ser possível validar autenticação ao passar senha incompatível")
    void givenTenhoSenhaIncompativelWhenTentoValidarAutenticacaoPorCpfESenhaThenRetornarErro() {
        String cpf = this.usuarioMock.getCpf();

        when(this.autenticacaoRepository.findByCpf(this.usuarioMock.getCpf()))
                .thenReturn(Optional.of(this.autenticacaoMock));
        when(this.autenticacaoService.validarSenhaDaAutenticacao(this.senhaInvalida, autenticacaoMock.getSenha()))
                .thenReturn(false);
        assertThrows(IllegalArgumentException.class,
                () -> this.autenticacaoService.validarAutenticacaoPorCpfESenha(cpf, this.senhaInvalida));
    }
}
