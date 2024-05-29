package com.dbserver.votacaoBackend.domain.sessaoVotacao.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import com.dbserver.votacaoBackend.domain.autenticacao.repository.AutenticacaoRepository;
import com.dbserver.votacaoBackend.domain.autenticacao.service.AutenticacaoServiceImpl;
import com.dbserver.votacaoBackend.domain.autenticacao.validacoes.AutenticacaoValidacoes;
import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.service.PautaServiceImpl;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.AbrirVotacaoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoExternoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoInternoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.mapper.SessaoVotacaoMapper;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.repository.SessaoVotacaoRepository;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.validacoes.SessaoVotacaoValidacoes;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.service.UsuarioServiceImpl;
import com.dbserver.votacaoBackend.domain.voto.Voto;
import com.dbserver.votacaoBackend.domain.voto.mapper.VotoMapper;
import com.dbserver.votacaoBackend.domain.voto.validacoes.VotoValidacoes;
import com.dbserver.votacaoBackend.fixture.pauta.PautaFixture;
import com.dbserver.votacaoBackend.fixture.sessaoVotacao.AbrirVotacaoDtoFixture;
import com.dbserver.votacaoBackend.fixture.sessaoVotacao.InserirVotoExternoDtoFixture;
import com.dbserver.votacaoBackend.fixture.sessaoVotacao.InserirVotoInternoDtoFixture;
import com.dbserver.votacaoBackend.fixture.sessaoVotacao.SessaoVotacaoFixture;
import com.dbserver.votacaoBackend.fixture.usuario.UsuarioFixture;
import com.dbserver.votacaoBackend.fixture.voto.VotoFixture;
import com.dbserver.votacaoBackend.utils.Utils;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class SessaoVotacaoServiceTest {

        @InjectMocks
        private SessaoVotacaoServiceImpl sessaoVotacaoService;

        @Mock
        private SessaoVotacaoRepository sessaoVotacaoRepository;

        @Mock
        private UsuarioServiceImpl usuarioService;
        @Mock
        private AutenticacaoServiceImpl autenticacaoService;

        @Mock
        private AutenticacaoRepository autenticacaoRepository;

        @Mock
        private Utils utils;
        @Mock
        private PasswordEncoder passwordEncoder;

        @Mock
        private PautaServiceImpl pautaService;

        @Mock
        private SessaoVotacaoMapper sessaoVotacaoMapper;

        @Mock
        private VotoMapper votoMapper;

        @Mock
        private SessaoVotacaoValidacoes sessaoVotacaoValidacoes;

        @Mock
        private VotoValidacoes votoValidacoes;

        @Mock
        private AutenticacaoValidacoes autenticacaoValidacoes;

        private LocalDateTime dataAbertura;

        private Pauta pautaMock;

        private Usuario usuarioDonoDaPautaMock;

        private Usuario usuarioVotanteMock;

        private SessaoVotacao sessaoVotacaoMock;

        private Voto votoDoUsuarioVotanteMock;

        @BeforeEach
        void configurar() {
                this.usuarioDonoDaPautaMock = UsuarioFixture.usuarioAdmin();
                this.usuarioVotanteMock = UsuarioFixture.usuarioNaoAdmin();
                this.pautaMock = PautaFixture.pautaTransporte(usuarioDonoDaPautaMock);

                this.sessaoVotacaoMock = SessaoVotacaoFixture.sessaoVotacaoAtiva(pautaMock);
                this.dataAbertura = this.sessaoVotacaoMock.getDataAbertura();
                this.votoDoUsuarioVotanteMock = VotoFixture.gerarVotoInterno(usuarioVotanteMock);
        }

        @Test
        @DisplayName("Deve ser possível abrir uma votação corretamente")
        void dadoSessaoVotacaoValidaQuandoTentoAbrirSessaoVotacaoEntaoRetornarSessaoVotacao() {
                AbrirVotacaoDto dto = AbrirVotacaoDtoFixture.abrirVotacaoDto();

                when(this.usuarioService.buscarUsuarioLogado()).thenReturn(this.usuarioDonoDaPautaMock);
                when(this.pautaService.buscarPautaPorIdEUsuarioId(dto.pautaId(), this.usuarioDonoDaPautaMock.getId()))
                                .thenReturn(this.pautaMock);

                when(this.utils.obterDataAtual()).thenReturn(this.dataAbertura);
                when(this.sessaoVotacaoMapper.toSessaoVotacao(pautaMock, this.sessaoVotacaoMock.getDataAbertura(),
                                this.sessaoVotacaoMock.getDataFechamento())).thenReturn(this.sessaoVotacaoMock);

                this.sessaoVotacaoService.abrirVotacao(dto);

                verify(this.sessaoVotacaoRepository).save(this.sessaoVotacaoMock);
                verify(this.sessaoVotacaoMapper).toRespostaSessaoVotacaoDto(any(SessaoVotacao.class));
        }

        @Test
        @DisplayName("Não deve ser possível abrir uma votação ao passar que possua uma pauta com sessãoVotacao diferente de null")
        void dadoSessaoVotacaoComPautaInvalidaQuandoTentoAbrirSessaoVotacaoEntaoRetornarErro() {
                this.pautaMock.setSessaoVotacao(sessaoVotacaoMock);
                AbrirVotacaoDto dto = AbrirVotacaoDtoFixture.abrirVotacaoDto();

                when(this.usuarioService.buscarUsuarioLogado()).thenReturn(this.usuarioDonoDaPautaMock);
                when(this.pautaService.buscarPautaPorIdEUsuarioId(dto.pautaId(), this.usuarioDonoDaPautaMock.getId()))
                                .thenReturn(this.pautaMock);

                assertThrows(IllegalStateException.class, () -> this.sessaoVotacaoService.abrirVotacao(dto));
        }

        @Test
        @DisplayName("Deve ser possível inserir voto interno positivo corretamente")
        void dadoDadosValidosQuandoTentoInserirVotoInternoPositivoEntaoRetornarSessaoVotacao() {
                InserirVotoInternoDto dto = InserirVotoInternoDtoFixture.inserirVotoInternoDto();
                Voto voto = VotoFixture.gerarVotoInterno(this.usuarioVotanteMock);

                when(this.usuarioService.buscarUsuarioLogado()).thenReturn(this.usuarioVotanteMock);
                when(utils.obterDataAtual()).thenReturn(dataAbertura);
                when(this.sessaoVotacaoRepository.findByPautaIdAndSessaoVotacaoAtiva(1L, dataAbertura))
                                .thenReturn(Optional.of(this.sessaoVotacaoMock));
                when(this.votoMapper.toVoto(this.usuarioVotanteMock.getCpf(), this.usuarioVotanteMock))
                                .thenReturn(voto);

                assertDoesNotThrow(() -> this.sessaoVotacaoService.inserirVotoInterno(dto));
                assertEquals(1, this.sessaoVotacaoMock.getVotosPositivos().size());
                verify(this.sessaoVotacaoRepository).save(this.sessaoVotacaoMock);
                verify(this.sessaoVotacaoMapper).toRespostaSessaoVotacaoDto(sessaoVotacaoMock);
        }

        @Test
        @DisplayName("Deve ser possível inserir voto negativo corretamente")
        void dadoPossuoDadosValidosQuandoTentoInserirVotoNegativoEntaoRetornarSessaoVotacao() {
                InserirVotoInternoDto dto = InserirVotoInternoDtoFixture.inserirVotoInternoDtoVotoNegativo();
                Voto voto = VotoFixture.gerarVotoInterno(this.usuarioVotanteMock);

                when(this.usuarioService.buscarUsuarioLogado()).thenReturn(this.usuarioVotanteMock);
                when(utils.obterDataAtual()).thenReturn(dataAbertura);
                when(this.sessaoVotacaoRepository.findByPautaIdAndSessaoVotacaoAtiva(dto.pautaId(), dataAbertura))
                                .thenReturn(Optional.of(this.sessaoVotacaoMock));
                when(this.votoMapper.toVoto(this.usuarioVotanteMock.getCpf(), this.usuarioVotanteMock))
                                .thenReturn(voto);

                assertDoesNotThrow(() -> this.sessaoVotacaoService.inserirVotoInterno(dto));
                assertEquals(1, this.sessaoVotacaoMock.getVotosNegativos().size());
                verify(this.sessaoVotacaoRepository).save(this.sessaoVotacaoMock);
                verify(this.sessaoVotacaoMapper).toRespostaSessaoVotacaoDto(sessaoVotacaoMock);
        }

        @Test
        @DisplayName("Deve retornar erro ao tentar inserir com tipo de voto nulo")
        void dadoTipoDeVotoNullQuandoTentoInserirVotoEntaoRetornarErro() {
                InserirVotoInternoDto dto = InserirVotoInternoDtoFixture.inserirVotoInternoDtoVotoNull();
                Voto voto = VotoFixture.gerarVotoInterno(this.usuarioVotanteMock);

                when(this.usuarioService.buscarUsuarioLogado()).thenReturn(this.usuarioVotanteMock);
                when(utils.obterDataAtual()).thenReturn(dataAbertura);
                when(this.sessaoVotacaoRepository.findByPautaIdAndSessaoVotacaoAtiva(dto.pautaId(), dataAbertura))
                                .thenReturn(Optional.of(this.sessaoVotacaoMock));
                when(this.votoMapper.toVoto(this.usuarioVotanteMock.getCpf(), this.usuarioVotanteMock))
                                .thenReturn(voto);

                assertThrows(IllegalArgumentException.class,
                                () -> this.sessaoVotacaoService.inserirVotoInterno(dto));
        }

        @Test
        @DisplayName("Deve retornar erro ao tentar inserir voto em pauta que não está ativa")
        void dadoPautaInativaQuandoTentoInserirVotoEntaoRetornarErro() {
                InserirVotoInternoDto dto = InserirVotoInternoDtoFixture.inserirVotoInternoDto();

                when(this.usuarioService.buscarUsuarioLogado()).thenReturn(this.usuarioVotanteMock);
                when(utils.obterDataAtual()).thenReturn(dataAbertura);

                assertThrows(IllegalArgumentException.class,
                                () -> this.sessaoVotacaoService.inserirVotoInterno(dto));
        }

        @Test
        @DisplayName("Deve retornar erro quando criador tentar votar na pauta criada")
        void dadoSouDonoDaPautaQuandoTentoInserirVotoEntaoRetornarErro() {
                InserirVotoInternoDto dto = InserirVotoInternoDtoFixture.inserirVotoInternoDto();
                Voto voto = VotoFixture.gerarVotoInterno(this.usuarioDonoDaPautaMock);

                when(this.usuarioService.buscarUsuarioLogado()).thenReturn(this.usuarioDonoDaPautaMock);
                when(utils.obterDataAtual()).thenReturn(dataAbertura);
                when(this.sessaoVotacaoRepository.findByPautaIdAndSessaoVotacaoAtiva(dto.pautaId(), dataAbertura))
                                .thenReturn(Optional.of(this.sessaoVotacaoMock));
                when(this.votoMapper.toVoto(this.usuarioDonoDaPautaMock.getCpf(), this.usuarioDonoDaPautaMock))
                                .thenReturn(voto);

                assertThrows(IllegalArgumentException.class,
                                () -> this.sessaoVotacaoService.inserirVotoInterno(dto));
        }

        @Test
        @DisplayName("Deve retornar erro ao tentar votar duas vezes na mesma pauta")
        void dadoSouUsuarioVotanteQuandoTentoInserirVotoDuasVezesNaMesmaPautaEntaoRetornarErro() {
                this.sessaoVotacaoMock.setVotosPositivos(votoDoUsuarioVotanteMock);
                InserirVotoInternoDto dto = InserirVotoInternoDtoFixture.inserirVotoInternoDto();
                Voto voto = VotoFixture.gerarVotoInterno(this.usuarioVotanteMock);

                when(this.usuarioService.buscarUsuarioLogado()).thenReturn(this.usuarioVotanteMock);
                when(utils.obterDataAtual()).thenReturn(dataAbertura);
                when(this.sessaoVotacaoRepository.findByPautaIdAndSessaoVotacaoAtiva(dto.pautaId(), dataAbertura))
                                .thenReturn(Optional.of(this.sessaoVotacaoMock));
                when(this.votoMapper.toVoto(this.usuarioVotanteMock.getCpf(), this.usuarioVotanteMock))
                                .thenReturn(voto);

                assertThrows(IllegalStateException.class,
                                () -> this.sessaoVotacaoService.inserirVotoInterno(dto));
        }

        @Test
        @DisplayName("Deve ser possível inserir voto externo positivo corretamente")
        void dadoPossuoDadosValidosQuandoTentoInserirVotoExternoPositivoEntaoRetornarSessaoVotacao() {
                InserirVotoExternoDto dto = InserirVotoExternoDtoFixture.inserirVotoExternoDtoUsuarioExistenteValido();
                Voto voto = VotoFixture.gerarVotoExterno(dto.cpf(), null);
                when(utils.obterDataAtual()).thenReturn(dataAbertura);
                when(this.sessaoVotacaoRepository.findByPautaIdAndSessaoVotacaoAtiva(dto.pautaId(), dataAbertura))
                                .thenReturn(Optional.of(this.sessaoVotacaoMock));
                when(this.votoMapper.toVoto(dto.cpf(), null))
                                .thenReturn(voto);
                assertDoesNotThrow(() -> this.sessaoVotacaoService.inserirVotoExterno(dto));

                assertEquals(1, this.sessaoVotacaoMock.getVotosPositivos().size());
                verify(this.sessaoVotacaoRepository).save(this.sessaoVotacaoMock);
                verify(this.sessaoVotacaoMapper).toRespostaSessaoVotacaoDto(sessaoVotacaoMock);
        }

}
