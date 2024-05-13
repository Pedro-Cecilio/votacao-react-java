package com.dbserver.votacaoBackend.domain.usuario.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;
import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutenticacaoDto;
import com.dbserver.votacaoBackend.domain.autenticacao.repository.AutenticacaoRepository;
import com.dbserver.votacaoBackend.domain.autenticacao.service.AutenticacaoServiceImpl;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.dto.CriarUsuarioDto;
import com.dbserver.votacaoBackend.domain.usuario.mapper.UsuarioMapper;
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
    @Mock
    private UsuarioMapper usuarioMapper;

    private Usuario usuarioMock;
    private Autenticacao autenticacaoMock;
    private CriarUsuarioDto criarUsuarioDtoMock;

    @BeforeEach
    void configurar() {
        this.usuarioMock = new Usuario(1L, "João", "Silva", "12345678900", false);
        this.autenticacaoMock = new Autenticacao("example@example.com", "senha123");
        AutenticacaoDto autenticacaoDto = new AutenticacaoDto(this.autenticacaoMock.getEmail(),
                this.autenticacaoMock.getSenha());
        this.criarUsuarioDtoMock = new CriarUsuarioDto(autenticacaoDto, "João", "Silva", "12345678900", false);
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
        when(this.autenticacaoService.encriptarSenhaDaAutenticacao(this.autenticacaoMock.getSenha()))
                .thenReturn("senhaEncriptada");
        when(this.autenticacaoService.verificarEmailJaEstaCadastrado(autenticacaoMock.getEmail())).thenReturn(false);
        when(this.usuarioRepository.findByCpf(this.usuarioMock.getCpf())).thenReturn(Optional.empty());

        this.usuarioService.criarUsuario(this.criarUsuarioDtoMock);

        verify(this.usuarioRepository, times(1)).save(any(Usuario.class));
        verify(this.autenticacaoService, times(1)).criarAutenticacao(any(Autenticacao.class), any(Usuario.class));
        verify(this.usuarioMapper).toCriarUsuarioRespostaDto(any(Usuario.class), any(Autenticacao.class));
    }

    @Test
    @DisplayName("Não deve ser possível criar um Usuario passando ao passar cpf existente")
    void dadoTenhoUmCpfJaCadastradoQuandoTentoCriarUsuarioEntaoRetornarUmErro() {
        when(this.autenticacaoService.encriptarSenhaDaAutenticacao(this.autenticacaoMock.getSenha()))
                .thenReturn("senhaEncriptada");
        when(this.autenticacaoService.verificarEmailJaEstaCadastrado(autenticacaoMock.getEmail())).thenReturn(false);
        when(this.usuarioRepository.findByCpf(this.usuarioMock.getCpf())).thenReturn(Optional.of(this.usuarioMock));

        assertThrows(IllegalArgumentException.class,
                () -> this.usuarioService.criarUsuario(this.criarUsuarioDtoMock));
    }

    @Test
    @DisplayName("Não deve ser possível criar um Usuario passando ao passar email existente")
    void dadoTenhoUmEmailJaCadastradoQuandoTentoCriarUsuarioEntaoRetornarUmErro() {
        when(this.autenticacaoService.encriptarSenhaDaAutenticacao(this.autenticacaoMock.getSenha()))
                .thenReturn("senhaEncriptada");
        when(this.autenticacaoService.verificarEmailJaEstaCadastrado(autenticacaoMock.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> this.usuarioService.criarUsuario(this.criarUsuarioDtoMock));
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
    @DisplayName("Deve falhar ao tentar buscar o usuário logado")
    void dadoNãoTenhoUmUsuarioLogadoQuandoTentoBuscarUsuarioLogadoEntaoRetornarErro() {
        assertThrows(AccessDeniedException.class, () -> this.usuarioService.buscarUsuarioLogado());
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
