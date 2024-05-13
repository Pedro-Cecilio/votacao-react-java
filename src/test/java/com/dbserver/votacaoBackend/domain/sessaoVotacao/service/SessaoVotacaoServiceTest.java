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
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;
import com.dbserver.votacaoBackend.domain.pauta.service.PautaServiceImpl;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.AbrirVotacaoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoExternoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoInternoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.TipoDeVotoEnum;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.mapper.SessaoVotacaoMapper;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.repository.SessaoVotacaoRepository;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.validacoes.SessaoVotacaoValidacoes;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.service.UsuarioServiceImpl;
import com.dbserver.votacaoBackend.domain.voto.Voto;
import com.dbserver.votacaoBackend.domain.voto.validacoes.VotoValidacoes;
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
    private SessaoVotacaoValidacoes sessaoVotacaoValidacoes;

    @Mock
    private VotoValidacoes votoValidacoes;

    @Mock
    private AutenticacaoValidacoes autenticacaoValidacoes;

    private LocalDateTime dataAbertura;

    private LocalDateTime dataFechamento;

    private Pauta pautaMock;

    private Usuario usuarioDonoDaPautaMock;

    private Usuario usuarioVotanteMock;

    private SessaoVotacao sessaoVotacaoMock;

    private Voto votoDoUsuarioVotanteMock;


    @BeforeEach
    void configurar() {
        this.usuarioDonoDaPautaMock = new Usuario(1L, "João", "Silva", "12345678900", true);
        this.usuarioVotanteMock = new Usuario(2L, "Pedro", "Cecilio", "12345678912", true);
        this.pautaMock = new Pauta("Você está feliz hoje?", Categoria.CULTURA_LAZER.toString(),
                this.usuarioDonoDaPautaMock);

        this.dataAbertura = LocalDateTime.now();
        this.dataFechamento = this.dataAbertura.plusMinutes(5);
        this.sessaoVotacaoMock = new SessaoVotacao(this.pautaMock, this.dataAbertura, this.dataFechamento);
        this.votoDoUsuarioVotanteMock = new Voto(this.usuarioVotanteMock.getCpf(), this.usuarioVotanteMock);
    }

    @Test
    @DisplayName("Deve ser possível abrir uma votação corretamente")
    void dadoPossuoUmSessaoVotacaoValidaQuandoTentoAbrirSessaoVotacaoEntaoRetornarSessaoVotacao() {
        AbrirVotacaoDto dto = new AbrirVotacaoDto(5L, 1L);

        when(this.usuarioService.buscarUsuarioLogado()).thenReturn(this.usuarioDonoDaPautaMock);
        when(this.pautaService.buscarPautaPorIdEUsuarioId(1L, this.usuarioDonoDaPautaMock.getId()))
                .thenReturn(this.pautaMock);
        when(this.utils.obterDataAtual()).thenReturn(this.dataAbertura);

        this.sessaoVotacaoService.abrirVotacao(dto);

        verify(this.sessaoVotacaoRepository).save(any(SessaoVotacao.class));
        verify(this.sessaoVotacaoMapper).toRespostaSessaoVotacaoDto(any(SessaoVotacao.class));
    }

    @Test
    @DisplayName("Não deve ser possível abrir uma votação ao passar que possua uma pauta com sessãoVotacao diferente de null")
    void dadoPossuoUmSessaoVotacaoComPautaInvalidaQuandoTentoAbrirSessaoVotacaoEntaoRetornarErro() {
        this.pautaMock.setSessaoVotacao(sessaoVotacaoMock);
        AbrirVotacaoDto dto = new AbrirVotacaoDto(5L, 1L);

        when(this.usuarioService.buscarUsuarioLogado()).thenReturn(this.usuarioDonoDaPautaMock);
        when(this.pautaService.buscarPautaPorIdEUsuarioId(1L, this.usuarioDonoDaPautaMock.getId()))
                .thenReturn(this.pautaMock);

        assertThrows(IllegalStateException.class, () -> this.sessaoVotacaoService.abrirVotacao(dto));
    }

    @Test
    @DisplayName("Deve ser possível inserir voto interno positivo corretamente")
    void dadoPossuoDadosValidosQuandoTentoInserirVotoInternoPositivoEntaoRetornarSessaoVotacao() {
        InserirVotoInternoDto dto = new InserirVotoInternoDto(1L, TipoDeVotoEnum.VOTO_POSITIVO);

        when(this.usuarioService.buscarUsuarioLogado()).thenReturn(this.usuarioVotanteMock);
        when(utils.obterDataAtual()).thenReturn(dataAbertura);
        when(this.sessaoVotacaoRepository.findByPautaIdAndSessaoVotacaoAtiva(1L, dataAbertura))
                .thenReturn(Optional.of(this.sessaoVotacaoMock));

        assertDoesNotThrow(() -> this.sessaoVotacaoService.inserirVotoInterno(dto));

        assertEquals(1, this.sessaoVotacaoMock.getVotosPositivos().size());
        verify(this.sessaoVotacaoRepository).save(this.sessaoVotacaoMock);
        verify(this.sessaoVotacaoMapper).toRespostaSessaoVotacaoDto(sessaoVotacaoMock);
    }

    @Test
    @DisplayName("Deve ser possível inserir voto negativo corretamente")
    void dadoPossuoDadosValidosQuandoTentoInserirVotoNegativoEntaoRetornarSessaoVotacao() {
        InserirVotoInternoDto dto = new InserirVotoInternoDto(1L, TipoDeVotoEnum.VOTO_NEGATIVO);

        when(this.usuarioService.buscarUsuarioLogado()).thenReturn(this.usuarioVotanteMock);
        when(utils.obterDataAtual()).thenReturn(dataAbertura);
        when(this.sessaoVotacaoRepository.findByPautaIdAndSessaoVotacaoAtiva(1L, dataAbertura))
                .thenReturn(Optional.of(this.sessaoVotacaoMock));

        assertDoesNotThrow(() -> this.sessaoVotacaoService.inserirVotoInterno(dto));

        assertEquals(1, this.sessaoVotacaoMock.getVotosNegativos().size());
        verify(this.sessaoVotacaoRepository).save(this.sessaoVotacaoMock);
        verify(this.sessaoVotacaoMapper).toRespostaSessaoVotacaoDto(sessaoVotacaoMock);
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar inserir com tipo de voto nulo")
    void dadoTipoDeVotoNullQuandoTentoInserirVotoEntaoRetornarErro() {
        InserirVotoInternoDto dto = new InserirVotoInternoDto(1L, null);

        when(this.usuarioService.buscarUsuarioLogado()).thenReturn(this.usuarioVotanteMock);
        when(utils.obterDataAtual()).thenReturn(dataAbertura);
        when(this.sessaoVotacaoRepository.findByPautaIdAndSessaoVotacaoAtiva(1L, dataAbertura))
                .thenReturn(Optional.of(this.sessaoVotacaoMock));


        assertThrows(IllegalArgumentException.class,
                () -> this.sessaoVotacaoService.inserirVotoInterno(dto));
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar inserir voto em pauta que não está ativa")
    void dadoPautaInativaQuandoTentoInserirVotoEntaoRetornarErro() {
        InserirVotoInternoDto dto = new InserirVotoInternoDto(1L, TipoDeVotoEnum.VOTO_POSITIVO);

        when(this.usuarioService.buscarUsuarioLogado()).thenReturn(this.usuarioVotanteMock);
        when(utils.obterDataAtual()).thenReturn(dataAbertura);

        assertThrows(IllegalArgumentException.class,
                () -> this.sessaoVotacaoService.inserirVotoInterno(dto));
    }
    @Test
    @DisplayName("Deve retornar erro quando criador tentar votar na pauta criada")
    void dadoSouDonoDaPautaQuandoTentoInserirVotoEntaoRetornarErro() {
        InserirVotoInternoDto dto = new InserirVotoInternoDto(1L, TipoDeVotoEnum.VOTO_POSITIVO);

        when(this.usuarioService.buscarUsuarioLogado()).thenReturn(this.usuarioDonoDaPautaMock);
        when(utils.obterDataAtual()).thenReturn(dataAbertura);
        when(this.sessaoVotacaoRepository.findByPautaIdAndSessaoVotacaoAtiva(1L, dataAbertura))
                .thenReturn(Optional.of(this.sessaoVotacaoMock));
                
        assertThrows(IllegalArgumentException.class,
                () -> this.sessaoVotacaoService.inserirVotoInterno(dto));
    }
    @Test
    @DisplayName("Deve retornar erro ao tentar votar duas vezes na mesma pauta")
    void dadoSouUsuarioVotanteQuandoTentoInserirVotoDuasVezesNaMesmaPautaEntaoRetornarErro() {
        this.sessaoVotacaoMock.setVotosPositivos(votoDoUsuarioVotanteMock);
        InserirVotoInternoDto dto = new InserirVotoInternoDto(1L, TipoDeVotoEnum.VOTO_POSITIVO);

        when(this.usuarioService.buscarUsuarioLogado()).thenReturn(this.usuarioVotanteMock);
        when(utils.obterDataAtual()).thenReturn(dataAbertura);
        when(this.sessaoVotacaoRepository.findByPautaIdAndSessaoVotacaoAtiva(1L, dataAbertura))
                .thenReturn(Optional.of(this.sessaoVotacaoMock));

        assertThrows(IllegalStateException.class,
                () -> this.sessaoVotacaoService.inserirVotoInterno(dto));
    }

    @Test
    @DisplayName("Deve ser possível inserir voto externo positivo corretamente")
    void dadoPossuoDadosValidosQuandoTentoInserirVotoExternoPositivoEntaoRetornarSessaoVotacao() {
        InserirVotoExternoDto dto = new InserirVotoExternoDto(1L, TipoDeVotoEnum.VOTO_POSITIVO, this.usuarioVotanteMock.getCpf(), "senhaValida");

        when(utils.obterDataAtual()).thenReturn(dataAbertura);
        when(this.sessaoVotacaoRepository.findByPautaIdAndSessaoVotacaoAtiva(1L, dataAbertura))
                .thenReturn(Optional.of(this.sessaoVotacaoMock));
        when(this.usuarioService.verificarSeExisteUsuarioPorCpf(this.usuarioVotanteMock.getCpf())).thenReturn(true);

        assertDoesNotThrow(() -> this.sessaoVotacaoService.inserirVotoExterno(dto));

        assertEquals(1, this.sessaoVotacaoMock.getVotosPositivos().size());
        verify(this.sessaoVotacaoRepository).save(this.sessaoVotacaoMock);
        verify(this.sessaoVotacaoMapper).toRespostaSessaoVotacaoDto(sessaoVotacaoMock);
    }

}
