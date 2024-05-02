package com.dbserver.votacaoBackend.domain.pauta.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;
import com.dbserver.votacaoBackend.domain.pauta.repository.PautaRepository;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.utils.Utils;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @Mock 
    private Utils utils;

    @InjectMocks
    private PautaService pautaService;

    private Pauta pautaMock;

    private Usuario usuarioAdminMock;
    private Categoria categoria;
    private LocalDateTime dataAtual;

    @BeforeEach
    void configurar() {
        this.usuarioAdminMock = new Usuario(1L, "João", "Silva", "12345678900", true);
        this.pautaMock = new Pauta("Você está feliz hoje?", Categoria.CULTURA_LAZER.toString(), this.usuarioAdminMock);
        this.categoria = Categoria.SAUDE;
        this.dataAtual = LocalDateTime.now();
    }

    @Test
    @DisplayName("Deve ser possível criar uma pauta corretamente")
    void givenTenhoUmaPautaComDadosCorretosWhenTentoCriarPautaThenRetornarPautaCriada() {
        when(this.pautaRepository.save(this.pautaMock)).thenReturn(this.pautaMock);
        Pauta resposta = this.pautaService.criarPauta(this.pautaMock);
        assertEquals(this.pautaMock.getAssunto(), resposta.getAssunto());
        assertEquals(this.pautaMock.getCategoria(), resposta.getCategoria());
        assertEquals(this.pautaMock.getUsuario().getId(), resposta.getUsuario().getId());
    }

    @Test
    @DisplayName("Não deve ser possível criar uma pauta ao passar valor nulo")
    void givenTenhoUmaPautaNulaWhenTentoCriarPautaThenRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> this.pautaService.criarPauta(null));
    }

    @Test
    @DisplayName("Deve ser possível todas buscar pautas por usuarioId sem passar categoria")
    void givenTenhoUsuarioIdCorretoECategoriaNullWhenTentoBuscarPautasPorUsuarioIdThenRetornarListaDePautas() {
        this.pautaService.buscarPautasPorUsuarioId(this.usuarioAdminMock.getId(), null);
        verify(this.pautaRepository).findAllByUsuarioId(this.usuarioAdminMock.getId());
    }

    @Test
    @DisplayName("Deve ser possível todas buscar pautas por usuarioId com a categoria informada")
    void givenTenhoUsuarioIdECategoriaCorretosCorretoWhenTentoBuscarPautasPorUsuarioIdThenRetornarListaDePautas() {
        this.pautaService.buscarPautasPorUsuarioId(this.usuarioAdminMock.getId(),
                this.pautaMock.getCategoria());
        verify(this.pautaRepository).findAllByUsuarioIdAndCategoria(this.usuarioAdminMock.getId(), this.pautaMock.getCategoria());
    }

    @Test
    @DisplayName("Não deve ser possível buscar todas pautas por usuarioId, ao passar UsuarioId nulo")
    void givenTenhoUsuarioIdNuloWhenTentoBuscarPautasPorUsuarioIdThenRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> this.pautaService.buscarPautasPorUsuarioId(null, categoria));
    }

    @Test
    @DisplayName("Deve ser possível buscar todas pautas ativas")
    void givenTenhoCategoriaCorretaWhenTentoBuscarPautasAtivasThenRetornarListaDePautas() {
        when(this.utils.obterHoraAtual()).thenReturn(this.dataAtual);
        this.pautaService.buscarPautasAtivas(null);
        verify(this.pautaRepository).findAllBySessaoVotacaoAtiva(this.dataAtual);
    }

    @Test
    @DisplayName("Deve ser possível buscar todas pautas ativas por categoria")
    void givenTenhoCategoriaNullWhenTentoBuscarPautasAtivasThenRetornarListaDePautas() {
        when(this.utils.obterHoraAtual()).thenReturn(this.dataAtual);
        this.pautaService.buscarPautasAtivas(this.pautaMock.getCategoria());
        verify(this.pautaRepository).findAllByCategoriaAndSessaoVotacaoAtiva(this.pautaMock.getCategoria(), this.dataAtual);
    }

    @Test
    @DisplayName("Deve ser possível buscar pauta por id e usuarioId")
    void givenTenhoPautaIdEUsuarioIdCorretosWhenTentoBuscarPautaPorIdEUsuarioIdThenRetornarPauta() {
        when(this.pautaRepository.findByIdAndUsuarioId(1L, this.usuarioAdminMock.getId()))
                .thenReturn(Optional.of(this.pautaMock));
        assertDoesNotThrow(() -> this.pautaService.buscarPautaPorIdEUsuarioId(1L,
                this.usuarioAdminMock.getId()));
    }

    @Test
    @DisplayName("Deve falhar ao buscar pauta por id e usuarioId não encontrar")
    void givenTenhoPautaIdEUsuarioIdWhenTentoBuscarPautaPorIdEUsuarioIdThenRetornarErro() {
        Long usuarioId = this.usuarioAdminMock.getId();
        assertThrows(NoSuchElementException.class, () -> this.pautaService.buscarPautaPorIdEUsuarioId(1L, usuarioId));
    }

    @Test
    @DisplayName("Deve buscar pauta ativa por id")
    void givenTenhoPautaIdCorretoWhenTentoBuscarPautaAtivaPorIdThenRetornarPauta() {
        when(this.utils.obterHoraAtual()).thenReturn(this.dataAtual);
        when(this.pautaRepository.findByIdAndSessaoVotacaoAtiva(1L, this.dataAtual))
                .thenReturn(Optional.of(this.pautaMock));
        assertDoesNotThrow(() -> this.pautaService.buscarPautaAtivaPorId(1L));
    }

    @Test
    @DisplayName("Deve falhar buscar pauta ativa por id, ao passar inexiste ou de uma pauta com sessão inativa")
    void givenTenhoPautaIdInvalidaWhenTentoBuscarPautaAtivaPorIdThenRetornarPauta() {
        when(this.utils.obterHoraAtual()).thenReturn(this.dataAtual);
        assertThrows(NoSuchElementException.class, () -> this.pautaService.buscarPautaAtivaPorId(1L));
    }

    @Test
    @DisplayName("Deve buscar pauta com sessão votação não nula por id e usuario id validos")
    void givenTenhoPautaIdEUsuarioIdCorretosWhenTentoBuscarPautaComSessaoVotacaoNaoNulaThenRetornarPauta() {
        when(this.pautaRepository.findByIdAndUsuarioIdAndSessaoVotacaoNotNull(1L, this.usuarioAdminMock.getId()))
                .thenReturn(Optional.of(this.pautaMock));
        assertDoesNotThrow(() -> this.pautaService.buscarPautaPorIdEUsuarioIdComSessaoVotacaoNaoNula(1L,
                this.usuarioAdminMock.getId()));
    }

    @Test
    @DisplayName("Deve falhar ao buscar pauta com sessão votação não nula por id e usuario id invalidos")
    void givenTenhoPautaIdEUsuarioIdInvalidosWhenTentoBuscarPautaComSessaoVotacaoNaoNulaThenRetornarErro() {
        Long usuarioId = this.usuarioAdminMock.getId();
        assertThrows(NoSuchElementException.class,
                () -> this.pautaService.buscarPautaPorIdEUsuarioIdComSessaoVotacaoNaoNula(1L, usuarioId));
    }
}
