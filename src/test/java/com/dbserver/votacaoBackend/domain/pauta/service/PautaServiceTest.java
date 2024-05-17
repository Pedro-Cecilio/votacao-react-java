package com.dbserver.votacaoBackend.domain.pauta.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.util.Optional;
import java.util.NoSuchElementException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.dto.CriarPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.dto.RespostaPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;
import com.dbserver.votacaoBackend.domain.pauta.mapper.PautaMapper;
import com.dbserver.votacaoBackend.domain.pauta.repository.PautaRepository;
import com.dbserver.votacaoBackend.domain.pauta.validacoes.PautaValidacoes;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.service.UsuarioServiceImpl;
import com.dbserver.votacaoBackend.fixture.pauta.CriarPautaDtoFixture;
import com.dbserver.votacaoBackend.fixture.pauta.PautaFixture;
import com.dbserver.votacaoBackend.fixture.pauta.RespostaPautaDtoFixture;
import com.dbserver.votacaoBackend.fixture.usuario.UsuarioFixture;
import com.dbserver.votacaoBackend.utils.Utils;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private Utils utils;

    @InjectMocks
    private PautaServiceImpl pautaService;

    @Mock
    private UsuarioServiceImpl usuarioService;

    @Mock
    private PautaValidacoes pautaValidacoes;

    @Mock
    private PautaMapper pautaMapper;

    @Mock
    private Pauta pautaMock;

    private CriarPautaDto criarPautaMock;
    private RespostaPautaDto respostaPautaDtoMock;
    private Usuario usuarioAdminMock;
    private Categoria categoria;
    private LocalDateTime dataAtual;

    @BeforeEach
    void configurar() {
        this.usuarioAdminMock = UsuarioFixture.usuarioAdmin();
        this.criarPautaMock = CriarPautaDtoFixture.criarPautaDtoValido();
        this.respostaPautaDtoMock = RespostaPautaDtoFixture.respostaPautaDto(usuarioAdminMock);
        this.categoria = PautaFixture.CATEGORIA_TRANSPORTE;
        this.dataAtual = LocalDateTime.now();
    }

    @Test
    @DisplayName("Deve ser possível criar uma pauta corretamente")
    void dadoTenhoUmaPautaComDadosCorretosQuandoTentoCriarPautaEntaoRetornarPautaCriada() {
        Pauta pauta = PautaFixture.pautaTransporte(usuarioAdminMock);

        when(this.usuarioService.buscarUsuarioLogado()).thenReturn(usuarioAdminMock);
        when(this.pautaMapper.toPauta(criarPautaMock, usuarioAdminMock)).thenReturn(pauta);
        when(this.pautaMapper.toRespostaPautaDto(pauta)).thenReturn(this.respostaPautaDtoMock);

        RespostaPautaDto resposta = this.pautaService.criarPauta(this.criarPautaMock);
        
        assertEquals(this.criarPautaMock.assunto(), resposta.assunto());
        assertEquals(this.criarPautaMock.categoria(), resposta.categoria().toString());
    }

    @Test
    @DisplayName("Não deve ser possível criar uma pauta ao passar valor nulo")
    void dadoTenhoUmaPautaNulaQuandoTentoCriarPautaEntaoRetornarErro() {
        doThrow(IllegalArgumentException.class).when(this.pautaValidacoes).validarCriarPautaDtoNaoNula(null);
        assertThrows(IllegalArgumentException.class, () -> this.pautaService.criarPauta(null));
    }

    @Test
    @DisplayName("Deve ser possível todas buscar pautas do usuário logado")
    void dadoTenhoUsuarioIdCorretoECategoriaNullQuandoTentoBuscarPautasUsuarioLogadoEntaoRetornarListaDePautas() {
        when(this.usuarioService.buscarUsuarioLogado()).thenReturn(usuarioAdminMock);
        this.pautaService.buscarPautasUsuarioLogado(null);
        verify(this.pautaRepository, times(1)).findAllByUsuarioIdOrderByCreatedAtDesc(this.usuarioAdminMock.getId());
        verify(this.pautaMapper, times(1)).toListRespostaPautaDto(anyList());
    }

    @Test
    @DisplayName("Deve ser possível todas buscar pautas do usuario logado com a categoria informada")
    void dadoTenhoUsuarioIdECategoriaCorretosCorretoQuandoTentobuscarPautasUsuarioLogadoEntaoRetornarListaDePautas() {
        when(this.usuarioService.buscarUsuarioLogado()).thenReturn(usuarioAdminMock);
        this.pautaService.buscarPautasUsuarioLogado(this.categoria);
        verify(this.pautaRepository).findAllByUsuarioIdAndCategoriaOrderByCreatedAtDesc(this.usuarioAdminMock.getId(),
                this.categoria);
        verify(this.pautaMapper, times(1)).toListRespostaPautaDto(anyList());

    }

    @Test
    @DisplayName("Deve ser possível buscar todas pautas ativas")
    void dadoTenhoCategoriaNulaQuandoTentoBuscarPautasAtivasEntaoRetornarListaDePautas() {
        when(this.utils.obterDataAtual()).thenReturn(this.dataAtual);
        this.pautaService.buscarPautasAtivas(null);
        verify(this.pautaRepository).findAllBySessaoVotacaoAtiva(this.dataAtual);
        verify(this.pautaMapper, times(1)).toListRespostaPautaDto(anyList());

    }

    @Test
    @DisplayName("Deve ser possível buscar todas pautas ativas por categoria")
    void dadoTenhoCategoriaNullQuandoTentoBuscarPautasAtivasEntaoRetornarListaDePautas() {
        when(this.utils.obterDataAtual()).thenReturn(this.dataAtual);
        this.pautaService.buscarPautasAtivas(this.categoria);
        verify(this.pautaRepository).findAllByCategoriaAndSessaoVotacaoAtiva(this.categoria,
                this.dataAtual);
    }

    @Test
    @DisplayName("Deve ser possível buscar pauta por id e usuarioId")
    void dadoTenhoPautaIdEUsuarioIdCorretosQuandoTentoBuscarPautaPorIdEUsuarioIdEntaoRetornarPauta() {
        when(this.pautaRepository.findByIdAndUsuarioId(1L,
                1L))
                .thenReturn(Optional.of(this.pautaMock));
        assertDoesNotThrow(() -> this.pautaService.buscarPautaPorIdEUsuarioId(1L,
                1L));
    }

    @Test
    @DisplayName("Deve falhar ao buscar pauta por id e usuarioId ao não encontrar")
    void dadoTenhoPautaIdEUsuarioIdIncompativeisQuandoTentoBuscarPautaPorIdEUsuarioIdEntaoRetornarErro() {
        assertThrows(NoSuchElementException.class, () -> this.pautaService.buscarPautaPorIdEUsuarioId(1L, 1L));
    }

    @Test
    @DisplayName("Deve buscar pauta ativa por id")
    void dadoTenhoPautaIdCorretoQuandoTentoBuscarPautaAtivaPorIdEntaoRetornarPauta() {
        when(this.utils.obterDataAtual()).thenReturn(this.dataAtual);
        when(this.pautaRepository.findByIdAndSessaoVotacaoAtiva(1L, this.dataAtual))
                .thenReturn(Optional.of(this.pautaMock));
        this.pautaService.buscarPautaAtivaPorId(1L);
        verify(this.pautaMapper, times(1)).toRespostaPautaDto(this.pautaMock);
    }

    @Test
    @DisplayName("Deve falhar buscar pauta ativa por id, ao passar inexiste ou de uma pauta com sessão inativa")
    void dadoTenhoPautaIdInvalidaQuandoTentoBuscarPautaAtivaPorIdEntaoRetornarPauta() {
        when(this.utils.obterDataAtual()).thenReturn(this.dataAtual);
        assertThrows(NoSuchElementException.class, () -> this.pautaService.buscarPautaAtivaPorId(1L));
    }

    @Test
    @DisplayName("Deve ser possível obter detalhes da pauta com sessão votação não nula")
    void dadoTenhoPautaIdEEstouLogadoNaAplicacaoQuandoTentoObterDetalhesDaPautaEntaoDeveRetornarDetalhes(){
        when(this.usuarioService.buscarUsuarioLogado()).thenReturn(this.usuarioAdminMock);
        when(this.pautaRepository.findByIdAndUsuarioIdAndSessaoVotacaoNotNull(1L, this.usuarioAdminMock.getId()))
                .thenReturn(Optional.of(this.pautaMock));
        this.pautaService.obterDetalhePautaSessaoVotacaoNaoNula(1L);
        verify(this.pautaMapper, times(1)).toDetalhesPautaDto(this.pautaMock);
    }
    @Test
    @DisplayName("Deve falhar ao tentar obter detalhes da pauta com sessão votação não nula, ao não encontrar pauta com id informado")
    void dadoTenhoPautaIdInexistenteEEstouLogadoNaAplicacaoQuandoTentoObterDetalhesDaPautaEntaoDeveRetornarErro(){
        when(this.usuarioService.buscarUsuarioLogado()).thenReturn(this.usuarioAdminMock);
        when(this.pautaRepository.findByIdAndUsuarioIdAndSessaoVotacaoNotNull(1L, this.usuarioAdminMock.getId()))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, ()->this.pautaService.obterDetalhePautaSessaoVotacaoNaoNula(1L));
    }
}
