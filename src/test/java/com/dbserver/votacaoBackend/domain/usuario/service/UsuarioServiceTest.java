package com.dbserver.votacaoBackend.domain.usuario.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;
import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.repository.AutenticacaoRepository;
import com.dbserver.votacaoBackend.domain.autenticacao.service.AutenticacaoServiceImpl;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.repository.UsuarioRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @InjectMocks
    private UsuarioServiceImpl usuarioService;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private AutenticacaoServiceImpl autenticacaoService;
    @Mock
    private AutenticacaoRepository autenticacaoRepository;
    @Mock
    private Pageable pageable;

    @Mock
    private SecurityContextHolder securityContextHolder;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication securityAuthenticationMock;

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
    void dadoTenhoUmUsuarioEUmAutenticacaoComDadosCorretosQuandoTentoCriarUsuarioEntaoRetornarUsuarioCriado() {
        when(this.usuarioRepository.save(this.usuarioMock)).thenReturn(this.usuarioMock);
        when(this.autenticacaoService.verificarEmailJaEstaCadastrado(autenticacaoMock.getEmail())).thenReturn(false);
        when(this.usuarioRepository.findByCpf(this.usuarioMock.getCpf())).thenReturn(Optional.empty());

        Usuario resposta = this.usuarioService.criarUsuario(usuarioMock, autenticacaoMock);

        verify(this.usuarioRepository, times(1)).save(this.usuarioMock);
        verify(this.autenticacaoService, times(1)).criarAutenticacao(this.autenticacaoMock, this.usuarioMock);
        assertEquals(this.usuarioMock.getId(), resposta.getId());
    }

    @Test
    @DisplayName("Não deve ser possível criar um Usuario passando ao passar cpf existente")
    void dadoTenhoUmCpfJaCadastradoQuandoTentoCriarUsuarioEntaoRetornarUmErro() {
        when(this.usuarioRepository.findByCpf(this.usuarioMock.getCpf())).thenReturn(Optional.of(this.usuarioMock));

        assertThrows(IllegalArgumentException.class,
                () -> this.usuarioService.criarUsuario(this.usuarioMock, this.autenticacaoMock));
    }
    @Test
    @DisplayName("Não deve ser possível criar um Usuario passando ao passar email existente")
    void dadoTenhoUmEmailJaCadastradoQuandoTentoCriarUsuarioEntaoRetornarUmErro() {
        when(this.autenticacaoService.verificarEmailJaEstaCadastrado(autenticacaoMock.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> this.usuarioService.criarUsuario(this.usuarioMock, this.autenticacaoMock));
    }

    @Test
    @DisplayName("Não deve ser possível criar um Usuario passando usuario nulo")
    void dadoTenhoUmUsuarioNuloQuandoTentoCriarUsuarioEntaoRetornarUmErro() {
        assertThrows(IllegalArgumentException.class,
                () -> this.usuarioService.criarUsuario(null, this.autenticacaoMock));
    }

    @Test
    @DisplayName("Não deve ser possível criar um Usuario passando autenticação nula")
    void dadoTenhoUmaAutenticacaoNulaQuandoTentoCriarUsuarioEntaoRetornarUmErro() {
        assertThrows(IllegalArgumentException.class, () -> this.usuarioService.criarUsuario(this.usuarioMock, null));
    }

    @Test
    @DisplayName("Deve ser possível buscar o usuário logado")
    void dadoTenhoUmUsuarioLogadoQuandoTentoBuscarUsuarioLogadoEntaoRetornarUsuarioLogado() {
        SecurityContextHolder.setContext(this.securityContext);

        when(this.securityContext.getAuthentication()).thenReturn(this.securityAuthenticationMock);
        when(securityAuthenticationMock.getPrincipal()).thenReturn(this.usuarioMock);

        Usuario usuarioLogado = this.usuarioService.buscarUsuarioLogado();

        assertEquals(this.usuarioMock.getId(), usuarioLogado.getId());
    }

    @Test
    @DisplayName("Deve retornar true ao verificar se existe usuário ao passar cpf existente")
    void dadoTenhoUmCpfExistenteQuandoTentoVerificarSeExisteUsuarioPorCpfEntaoRetornarTrue() {
        when(this.usuarioRepository.findByCpf(this.usuarioMock.getCpf())).thenReturn(Optional.of(this.usuarioMock));

        boolean resposta = this.usuarioService.verificarSeExisteUsuarioPorCpf(this.usuarioMock.getCpf());

        assertTrue(resposta);
    }

    @Test
    @DisplayName("Deve retornar false ao verificar se existe usuário ao passar cpf inexistente")
    void dadoTenhoUmCpfInexistenteQuandoTentoVerificarSeExisteUsuarioPorCpfEntaoRetornarFalse() {
        when(this.usuarioRepository.findByCpf(this.usuarioMock.getCpf())).thenReturn(Optional.empty());

        boolean resposta = this.usuarioService.verificarSeExisteUsuarioPorCpf(this.usuarioMock.getCpf());

        assertFalse(resposta);
    }

    @Test
    @DisplayName("Deve retornar usuário ao buscar usuario com cpf existete")
    void dadoTenhoUmCpfExistenteQuandoTentoBuscarUsuarioPorCpfEntaoRetornarUsuario() {
        when(this.usuarioRepository.findByCpf(this.usuarioMock.getCpf())).thenReturn(Optional.of(this.usuarioMock));

        Usuario resposta = this.usuarioService.buscarUsuarioPorCpfSeHouver(this.usuarioMock.getCpf());

        assertEquals(this.usuarioMock.getId(), resposta.getId());
    }

    @Test
    @DisplayName("Deve retornar null ao buscar usuario com cpf inexistente")
    void dadoTenhoUmCpfInexistenteQuandoTentoBuscarUsuarioPorCpfEntaoRetornarNull() {
        when(this.usuarioRepository.findByCpf(this.usuarioMock.getCpf())).thenReturn(Optional.empty());

        Usuario resposta = this.usuarioService.buscarUsuarioPorCpfSeHouver(this.usuarioMock.getCpf());
        
        assertNull(resposta);
    }

}
