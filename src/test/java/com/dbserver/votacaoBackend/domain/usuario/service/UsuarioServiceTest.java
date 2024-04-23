package com.dbserver.votacaoBackend.domain.usuario.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.repository.AutenticacaoRepository;
import com.dbserver.votacaoBackend.domain.autenticacao.service.IAutenticacaoService;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.repository.UsuarioRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @InjectMocks
    private UsuarioService usuarioService;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private IAutenticacaoService autenticacaoService;
    @Mock
    private AutenticacaoRepository autenticacaoRepository;
    @Mock
    private Pageable pageable;

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
        this.usuarioRepository.deleteAll();
        this.autenticacaoRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve ser possível criar um Usuario corretamente")
    void givenTenhoUmUsuarioEUmAutenticacaoComDadosCorretosWhenTentoCriarUsuarioThenRetornarUsuarioCriado() {
        when(this.usuarioRepository.save(this.usuarioMock)).thenReturn(this.usuarioMock);
        Usuario resposta = this.usuarioService.criarUsuario(usuarioMock, autenticacaoMock);
        verify(this.usuarioRepository, times(1)).save(this.usuarioMock);
        verify(this.autenticacaoService, times(1)).criarAutenticacao(this.autenticacaoMock, this.usuarioMock);
        assertEquals(this.usuarioMock.getId(), resposta.getId());
    }

    @Test
    @DisplayName("Deve ser possível atualizar um Usuario corretamente")
    void givenTenhoUmUsuarioIdEDadosParaAtualizarWhenTentoAtualizarUsuarioThenRetornarUsuarioAtualizado() {
        when(this.usuarioRepository.findById(this.usuarioMock.getId())).thenReturn(Optional.of(this.usuarioMock));
        this.usuarioService.atualizarUsuario(this.usuarioMock.getId(), "NovoNome", "NovoSobrenome");
        verify(this.usuarioRepository, times(1)).save(this.usuarioMock);
    }

    @Test
    @DisplayName("Não deve ser possível atualizar um Usuario inexistente")
    void givenTenhoUmUsuarioIdInexistenteEDadosParaAtualizarWhenTentoAtualizarUsuarioThenRetornarUmErro() {
        when(this.usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,
                () -> this.usuarioService.atualizarUsuario(1L, "NovoNome", "NovoSobrenome"));
    }

    @ParameterizedTest
    @MethodSource("com.dbserver.votacaoBackend.testUtils.DadosTestesParametrizados#dadosInvalidosParaAtualizarUsuario")
    @DisplayName("Não deve ser possível atualizar um Usuario com dados inválidos")
    void givenTenhoUmUsuarioIdExistenteEDadosInvalidosParaAtualizarWhenTentoAtualizarUsuarioThenRetornarUmErro(String nome, String sobrenome) {
        when(this.usuarioRepository.findById(1L)).thenReturn(Optional.of(this.usuarioMock));
        assertThrows(IllegalArgumentException.class,
                () -> this.usuarioService.atualizarUsuario(1L, nome, sobrenome));
    }

    @Test
    @DisplayName("Deve ser possível deletar um Usuario corretamente")
    void givenTenhoUmUsuarioIdExistenteWhenTentoDeletarUsuarioThenDeletarUsuario() {
        when(this.usuarioRepository.findById(this.usuarioMock.getId())).thenReturn(Optional.of(this.usuarioMock));
        this.usuarioService.deletarUsuario(this.usuarioMock.getId());
        verify(this.autenticacaoService, times(1)).deletarAutenticacao(this.usuarioMock.getId());
        verify(this.usuarioRepository, times(1)).delete(this.usuarioMock);
    }

    @Test
    @DisplayName("Não deve ser possível deletar um Usuario inexistente")
    void givenTenhoUmUsuarioIdInexistenteWhenTentoDeletarUsuarioThenRetornarUmErro() {
        when(this.usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,
                () -> this.usuarioService.deletarUsuario(1L));
    }

    @Test
    @DisplayName("Deve ser possível buscar um Usuario corretamente")
    void givenTenhoUmUsuarioIdExistenteWhenTentoBuscarUsuarioThenRetornarUsuario() {
        when(this.usuarioRepository.findById(this.usuarioMock.getId())).thenReturn(Optional.of(this.usuarioMock));
        Usuario resposta = this.usuarioService.buscarUsuarioPorId(this.usuarioMock.getId());
        assertEquals(this.usuarioMock.getId(), resposta.getId());
    }

    @Test
    @DisplayName("Não deve ser possível buscar um Usuario inexistente")
    void givenTenhoUmUsuarioIdInexistenteWhenTentoBuscarUsuarioThenRetornarUmErro() {
        when(this.usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,
                () -> this.usuarioService.buscarUsuarioPorId(1L));
    }

    @Test
    @DisplayName("Deve ser possível buscar todos usuarios corretamente")
    void givenTenhoUmPageableWhenTentoBuscarTodosUsuariosThenRetornarListaDeUsuarios() {
        List<Usuario> listaDeUsuarios = List.of(this.usuarioMock);
        when(this.usuarioRepository.findAll(this.pageable)).thenReturn(new PageImpl<>(listaDeUsuarios));
        List<Usuario> resposta = this.usuarioService.buscarTodosUsuarios(this.pageable);
        assertEquals(listaDeUsuarios.size(), resposta.size());
    }

}
