package com.dbserver.votacaoBackend.domain.autenticacao.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutenticacaoDto;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutenticacaoRespostaDto;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutorizarVotoExternoDto;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutorizarVotoExternoRespostaDto;
import com.dbserver.votacaoBackend.domain.autenticacao.repository.AutenticacaoRepository;
import com.dbserver.votacaoBackend.domain.autenticacao.validacoes.AutenticacaoValidacoes;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.validacoes.UsuarioValidacoes;
import com.dbserver.votacaoBackend.fixture.AutenticacaoFixture;
import com.dbserver.votacaoBackend.fixture.UsuarioFixture;
import com.dbserver.votacaoBackend.infra.security.token.TokenService;
import com.dbserver.votacaoBackend.utils.Utils;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTest {
        @InjectMocks
        private AutenticacaoServiceImpl autenticacaoService;

        @Mock
        private AutenticacaoRepository autenticacaoRepository;

        @Mock
        private Utils utils;

        @Mock
        private PasswordEncoder passwordEncoder;

        @Mock
        private UsuarioValidacoes usuarioValidacoes;
        @Mock
        private AutenticacaoValidacoes autenticacaoValidacoes;

        @Mock
        private TokenService tokenService;

        private Usuario usuarioMock;
        private Autenticacao autenticacaoMock;
        private String senha;
        private String senhaEncriptada;
        private AutenticacaoDto autenticacaoDto;
        private AutorizarVotoExternoDto autorizarVotoExternoDto;

        @BeforeEach
        void configurar() {
                this.usuarioMock = UsuarioFixture.usuarioAdmin();
                this.autenticacaoMock = AutenticacaoFixture.autenticacaoAdmin(usuarioMock);
                this.senha = AutenticacaoFixture.SENHA;
                this.senhaEncriptada = this.autenticacaoMock.getSenha();
                this.autorizarVotoExternoDto = AutenticacaoFixture
                                .autorizarVotoExternoDtoValido(this.usuarioMock.getCpf());
                this.autenticacaoDto = AutenticacaoFixture.autenticacaoDtoAdminValido();
        }

        @AfterEach
        void limpar() {
                this.usuarioMock = null;
                this.autenticacaoMock = null;
                this.autenticacaoRepository.deleteAll();
        }

        @Test
        @DisplayName("Deve ser possível autenticar usuário corretamente")
        void dadoTenhoDadosDeAutenticacaoQuandoTentoAutenticarEntaoRetornarAutenticacaoRespostaDto() {
                String token = "tokenValido";

                when(this.autenticacaoRepository.findByEmail(this.autenticacaoDto.email()))
                                .thenReturn(Optional.of(this.autenticacaoMock));

                when(this.autenticacaoValidacoes.validarSenhaDaAutenticacao(this.autenticacaoDto.senha(),
                                this.senhaEncriptada)).thenReturn(true);

                when(this.tokenService.gerarToken(this.autenticacaoMock)).thenReturn(token);

                AutenticacaoRespostaDto resposta = this.autenticacaoService.autenticarUsuario(this.autenticacaoDto);

                verify(this.tokenService).gerarToken(this.autenticacaoMock);
                assertEquals(token, resposta.token());
        }

        @Test
        @DisplayName("Deve falhar ao tentar autenticar usuário ao não encontrar autenticacao por email")
        void dadoTenhoEmailInexistenteQuandoTentoAutenticarEntaoRetornarErro() {

                when(this.autenticacaoRepository.findByEmail(this.autenticacaoDto.email()))
                                .thenReturn(Optional.empty());

                assertThrows(BadCredentialsException.class,
                                () -> this.autenticacaoService.autenticarUsuario(this.autenticacaoDto));
        }

        @Test
        @DisplayName("Deve falhar ao tentar autenticar usuário ao informar senha incorreta")
        void dadoTenhoSenhaIncorretaQuandoTentoAutenticarEntaoRetornarErro() {

                when(this.autenticacaoRepository.findByEmail(this.autenticacaoDto.email()))
                                .thenReturn(Optional.of(this.autenticacaoMock));

                when(this.autenticacaoValidacoes.validarSenhaDaAutenticacao(this.senha,
                                this.senhaEncriptada)).thenReturn(false);

                assertThrows(BadCredentialsException.class,
                                () -> this.autenticacaoService.autenticarUsuario(autenticacaoDto));
        }

        @Test
        @DisplayName("Deve ser possivel autorizar usuário votar externamente")
        void dadoPossuoDadosParaAutorizarVotoExternoCorretosQuandoTentoAutorizarDeveRetornarAutorizarVotoExternoComTrue() {

                AutorizarVotoExternoRespostaDto resposta = this.autenticacaoService
                                .autorizarUsuarioVotoExterno(this.autorizarVotoExternoDto);
                assertTrue(resposta.valido());
        }

        @Test
        @DisplayName("Deve falhar ao autorizar usuário votar externamente ao passar cpf ou senha incompatíveis")
        void dadoPossuoDadosParaAutorizarVotoExternoIncorretosQuandoTentoAutorizarDeveRetornarErro() {

                doThrow(BadCredentialsException.class).when(this.autenticacaoValidacoes)
                                .validarAutenticacaoPorCpfESenha(this.autorizarVotoExternoDto.cpf(),
                                                this.autorizarVotoExternoDto.senha());
                assertThrows(BadCredentialsException.class,
                                () -> this.autenticacaoService
                                                .autorizarUsuarioVotoExterno(this.autorizarVotoExternoDto));
        }

        @Test
        @DisplayName("Deve ser possível criar uma autenticacao")
        void dadoTenhoUmUsuarioCadastradoEDadosDeAutenticacaoQuandoTentoCriarAutenticacaoEntaoRetornarAutenticacaoCriada() {
                when(this.autenticacaoRepository.save(this.autenticacaoMock)).thenReturn(this.autenticacaoMock);

                this.autenticacaoService.criarAutenticacao(this.autenticacaoMock, this.usuarioMock);

                verify(this.autenticacaoRepository, times(1)).save(this.autenticacaoMock);
        }

        @Test
        @DisplayName("Não deve ser possível criar uma autenticacao ao passar usuario nulo")
        void dadoTenhoUmUsuarioNuloEDadosDeAutenticacaoQuandoTentoCriarAutenticacaoEntaoRetornarErro() {
                doThrow(IllegalArgumentException.class).when(this.usuarioValidacoes).validarUsuarioNaoNulo(null);
                assertThrows(IllegalArgumentException.class,
                                () -> this.autenticacaoService.criarAutenticacao(this.autenticacaoMock, null));
        }

        @Test
        @DisplayName("Não deve ser possível criar uma autenticacao ao passar autenticacao nula")
        void dadoTenhoUmUsuarioEDadosDeAutenticacaoNuloQuandoTentoCriarAutenticacaoEntaoRetornarErro() {
                doThrow(IllegalArgumentException.class).when(this.autenticacaoValidacoes)
                                .validarAutenticacaoNaoNula(null);
                assertThrows(IllegalArgumentException.class,
                                () -> this.autenticacaoService.criarAutenticacao(null, this.usuarioMock));
        }

        @Test
        @DisplayName("Deve ser possível encriptar senha da autenticação ao passar senha válida")
        void dadoTenhoUmasenhaQuandoTentoEncriptarSenhaEntaoRetornarSenhaEncriptada() {
                when(this.passwordEncoder.encode(this.senha)).thenReturn(this.senhaEncriptada);

                this.autenticacaoService.encriptarSenhaDaAutenticacao(this.senha);

                verify(this.passwordEncoder).encode(this.senha);
        }

        @Test
        @DisplayName("Não deve ser possível encriptar senha da autenticação ao passar senha nula")
        void dadoTenhoUmaSenhaNulaQuandoTentoEncriptarSenhaEntaoRetornarErro() {
                assertThrows(IllegalArgumentException.class,
                                () -> this.autenticacaoService.encriptarSenhaDaAutenticacao(null));
        }

        @Test
        @DisplayName("Não deve ser possível encriptar senha com menos de 8 caracteres")
        void dadoTenhoUmaSenhaComMenosDe8CaracteresQuandoTentoEncriptarSenhaEntaoRetornarErro() {
                assertThrows(IllegalArgumentException.class,
                                () -> this.autenticacaoService.encriptarSenhaDaAutenticacao("1234567"));
        }

        @Test
        @DisplayName("Não deve ser possível encriptar senha da autenticação ao passar senha vazia")
        void dadoTenhoUmaSenhaVaziaQuandoTentoEncriptarSenhaEntaoRetornarErro() {
                assertThrows(IllegalArgumentException.class,
                                () -> this.autenticacaoService.encriptarSenhaDaAutenticacao(" "));
        }

        @Test
        @DisplayName("Deve ser possível verificar se email esta cadastrado e retornar true ao encontrar")
        void dadoTenhoEmailCadastradoQuandoVerificoSeEstaCadastradoRetornarTrue() {
                when(this.autenticacaoRepository.findByEmail(this.autenticacaoMock.getEmail()))
                                .thenReturn(Optional.of(this.autenticacaoMock));

                boolean resposta = this.autenticacaoService
                                .verificarEmailJaEstaCadastrado(this.autenticacaoMock.getEmail());

                assertTrue(resposta);

        }

        @Test
        @DisplayName("Deve ser possível verificar se email esta cadastrado e retornar false ao não encontrar")
        void dadoTenhoEmailNaoCadastradoQuandoVerificoSeEstaCadastradoRetornarTrue() {
                when(this.autenticacaoRepository.findByEmail(this.autenticacaoMock.getEmail()))
                                .thenReturn(Optional.empty());

                boolean resposta = this.autenticacaoService
                                .verificarEmailJaEstaCadastrado(this.autenticacaoMock.getEmail());

                assertFalse(resposta);
        }

}
