package com.dbserver.votacaoBackend.domain.usuario.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.dbserver.votacaoBackend.domain.usuario.dto.VerificarSeUsuarioExisteRespostaDto;
import com.dbserver.votacaoBackend.domain.usuario.mapper.UsuarioMapper;
import com.dbserver.votacaoBackend.domain.usuario.repository.UsuarioRepository;
import com.dbserver.votacaoBackend.fixture.AutenticacaoFixture;
import com.dbserver.votacaoBackend.fixture.UsuarioFixture;
import com.dbserver.votacaoBackend.fixture.VerificarSeUsuarioExisteRespostaDtoFixture;

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
    private AutenticacaoDto autenticacaoDtoMock;

    @BeforeEach
    void configurar() {
        this.autenticacaoDtoMock = AutenticacaoFixture.autenticacaoDtoUsuarioValido();
        this.criarUsuarioDtoMock = UsuarioFixture.criarUsuarioDto(this.autenticacaoDtoMock);
        this.usuarioMock = UsuarioFixture.gerarUsuarioAtravesDoDto(this.criarUsuarioDtoMock);
        this.autenticacaoMock = AutenticacaoFixture.gerarAutenticacaoComDadosDeUsuario();
    }

    @AfterEach
    void limpar() {
        this.usuarioMock = null;
        this.usuarioRepository.deleteAll();
        this.autenticacaoRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve ser possível criar um Usuario corretamente")
    void dadoTenhoUmUsuarioEUmAutenticacaoComDadosCorretosQuandoTentoCriarUsuarioEntaoRetornarUsuarioCriado() {
        when(usuarioMapper.toUsuario(this.criarUsuarioDtoMock)).thenReturn(this.usuarioMock);

        when(this.autenticacaoService.encriptarSenhaDaAutenticacao(this.autenticacaoDtoMock.senha()))
                .thenReturn(AutenticacaoFixture.SENHA_ENCRIPTADA);
        when(this.autenticacaoService.verificarEmailJaEstaCadastrado(this.autenticacaoDtoMock.email()))
                .thenReturn(false);
        when(this.usuarioRepository.findByCpf(this.criarUsuarioDtoMock.cpf())).thenReturn(Optional.empty());
        this.usuarioService.criarUsuario(this.criarUsuarioDtoMock);

        verify(this.usuarioRepository, times(1)).save(this.usuarioMock);
        verify(this.autenticacaoService, times(1)).criarAutenticacao(this.autenticacaoMock, this.usuarioMock);
        verify(this.usuarioMapper).toCriarUsuarioRespostaDto(this.usuarioMock, this.autenticacaoMock);
    }

    @Test
    @DisplayName("Não deve ser possível criar um Usuario passando ao passar cpf existente")
    void dadoTenhoUmCpfJaCadastradoQuandoTentoCriarUsuarioEntaoRetornarUmErro() {
        when(usuarioMapper.toUsuario(this.criarUsuarioDtoMock)).thenReturn(this.usuarioMock);
        when(this.autenticacaoService.encriptarSenhaDaAutenticacao(this.autenticacaoDtoMock.senha()))
                .thenReturn(AutenticacaoFixture.SENHA_ENCRIPTADA);
        when(this.autenticacaoService.verificarEmailJaEstaCadastrado(autenticacaoDtoMock.email())).thenReturn(false);
        when(this.usuarioRepository.findByCpf(this.usuarioMock.getCpf()))
                .thenReturn(Optional.of(this.usuarioMock));

        assertThrows(IllegalArgumentException.class,
                () -> this.usuarioService.criarUsuario(this.criarUsuarioDtoMock));
    }

    @Test
    @DisplayName("Não deve ser possível criar um Usuario passando ao passar email existente")
    void dadoTenhoUmEmailJaCadastradoQuandoTentoCriarUsuarioEntaoRetornarUmErro() {
        when(this.autenticacaoService.encriptarSenhaDaAutenticacao(this.autenticacaoDtoMock.senha()))
                .thenReturn("senhaEncriptada");
        when(this.autenticacaoService.verificarEmailJaEstaCadastrado(autenticacaoDtoMock.email())).thenReturn(true);

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

        assertNotNull(usuarioLogado);
    }

    @Test
    @DisplayName("Deve falhar ao tentar buscar o usuário logado")
    void dadoNaoTenhoUmUsuarioLogadoQuandoTentoBuscarUsuarioLogadoEntaoRetornarErro() {
        assertThrows(AccessDeniedException.class, () -> this.usuarioService.buscarUsuarioLogado());
    }

    @Test
    @DisplayName("Deve ser possível buscar o usuário logado como DTO")
    void dadoTenhoUmUsuarioLogadoQuandoTentoBuscarUsuarioLogadoComoDtoEntaoRetornarUsuarioLogado() {
        SecurityContextHolder.setContext(this.securityContext);

        when(this.securityContext.getAuthentication()).thenReturn(this.securityAuthenticationMock);
        when(securityAuthenticationMock.getPrincipal()).thenReturn(this.usuarioMock);

        assertDoesNotThrow(() -> this.usuarioService.buscarUsuarioLogadoComoDto());

        verify(usuarioMapper).toUsuarioRespostaDto(this.usuarioMock);
    }

    @Test
    @DisplayName("Deve falhar ao tentar buscar o usuário logado como Dto")
    void dadoNãoTenhoUmUsuarioLogadoQuandoTentoBuscarUsuarioLogadoComoDtoEntaoRetornarErro() {
        assertThrows(AccessDeniedException.class, () -> this.usuarioService.buscarUsuarioLogadoComoDto());
    }

    @Test
    @DisplayName("Deve retornar true ao verificar se existe usuário ao passar cpf existente")
    void dadoTenhoUmCpfExistenteQuandoTentoVerificarSeExisteUsuarioPorCpfEntaoRetornarTrue() {
        when(this.usuarioRepository.findByCpf(this.usuarioMock.getCpf())).thenReturn(Optional.of(this.usuarioMock));

        boolean resposta = this.usuarioService.verificarSeExisteUsuarioPorCpf(this.usuarioMock.getCpf());

        assertTrue(resposta);
    }

    @Test
    @DisplayName("Deve retornar VerificarSeUsuarioExisteRespostaDto ao verificar se existe usuário ao passar cpf inexistente")
    void dadoTenhoUmCpfInexistenteQuandoTentoVerificarSeExisteUsuarioPorCpfComoDtoEntaoRetornarFalse() {
        when(this.usuarioRepository.findByCpf(UsuarioFixture.CPF_ALEATORIO)).thenReturn(Optional.empty());
        VerificarSeUsuarioExisteRespostaDto dto = VerificarSeUsuarioExisteRespostaDtoFixture
                .gerarVerificarSeUsuarioExisteRespostaDtoFalse();
        when(usuarioMapper.toVerificarSeUsuarioExisteRespostaDto(false)).thenReturn(dto);
        VerificarSeUsuarioExisteRespostaDto resposta = this.usuarioService
                .verificarSeExisteUsuarioPorCpfComoDto(UsuarioFixture.CPF_ALEATORIO);

        assertFalse(resposta.existe());
    }

    @Test
    @DisplayName("Deve retornar true ao verificar se existe usuário como dto ao passar cpf existente")
    void dadoTenhoUmCpfExistenteQuandoTentoVerificarSeExisteUsuarioPorCpfComoDtoEntaoRetornarTrue() {
        when(this.usuarioRepository.findByCpf(UsuarioFixture.CPF_ALEATORIO)).thenReturn(Optional.of(this.usuarioMock));
        VerificarSeUsuarioExisteRespostaDto dto = VerificarSeUsuarioExisteRespostaDtoFixture
                .gerarVerificarSeUsuarioExisteRespostaDtoTrue();
        when(usuarioMapper.toVerificarSeUsuarioExisteRespostaDto(true)).thenReturn(dto);
        VerificarSeUsuarioExisteRespostaDto resposta = this.usuarioService
                .verificarSeExisteUsuarioPorCpfComoDto(UsuarioFixture.CPF_ALEATORIO);

        assertTrue(resposta.existe());
    }

    @Test
    @DisplayName("Deve retornar false ao verificar se existe usuário ao passar cpf inexistente")
    void dadoTenhoUmCpfInexistenteQuandoTentoVerificarSeExisteUsuarioPorCpfEntaoRetornarFalse() {
        when(this.usuarioRepository.findByCpf(UsuarioFixture.CPF_ALEATORIO)).thenReturn(Optional.empty());

        boolean resposta = this.usuarioService.verificarSeExisteUsuarioPorCpf(UsuarioFixture.CPF_ALEATORIO);

        assertFalse(resposta);
    }

    @Test
    @DisplayName("Deve retornar usuário ao buscar usuario com cpf existete")
    void dadoTenhoUmCpfExistenteQuandoTentoBuscarUsuarioPorCpfEntaoRetornarUsuario() {
        when(this.usuarioRepository.findByCpf(UsuarioFixture.CPF_ALEATORIO)).thenReturn(Optional.of(this.usuarioMock));

        Usuario resposta = this.usuarioService.buscarUsuarioPorCpfSeHouver(UsuarioFixture.CPF_ALEATORIO);

        assertEquals(this.usuarioMock.getId(), resposta.getId());
    }

    @Test
    @DisplayName("Deve retornar null ao buscar usuario com cpf inexistente")
    void dadoTenhoUmCpfInexistenteQuandoTentoBuscarUsuarioPorCpfEntaoRetornarNull() {
        when(this.usuarioRepository.findByCpf(UsuarioFixture.CPF_ALEATORIO)).thenReturn(Optional.empty());

        Usuario resposta = this.usuarioService.buscarUsuarioPorCpfSeHouver(UsuarioFixture.CPF_ALEATORIO);

        assertNull(resposta);
    }

}
