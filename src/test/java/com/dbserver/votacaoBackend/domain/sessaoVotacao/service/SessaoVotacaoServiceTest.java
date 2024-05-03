package com.dbserver.votacaoBackend.domain.sessaoVotacao.service;

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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import com.dbserver.votacaoBackend.domain.autenticacao.repository.AutenticacaoRepository;
import com.dbserver.votacaoBackend.domain.autenticacao.service.AutenticacaoService;
import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.StatusSessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.TipoDeVotoEnum;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.repository.SessaoVotacaoRepository;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.service.UsuarioService;
import com.dbserver.votacaoBackend.domain.voto.Voto;
import com.dbserver.votacaoBackend.utils.Utils;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class SessaoVotacaoServiceTest {

    @InjectMocks
    private SessaoVotacaoService sessaoVotacaoService;

    @Mock
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Mock
    private UsuarioService usuarioService;
    @Mock
    private AutenticacaoService autenticacaoService;

    @Mock
    private AutenticacaoRepository autenticacaoRepository;

    @Mock
    private Utils utils;
    @Mock
    private PasswordEncoder passwordEncoder;

    private LocalDateTime dataAbertura;

    private LocalDateTime dataFechamento;

    private Pauta pautaMock;

    private Usuario usuarioDonoDaPautaMock;

    private Usuario usuarioVotanteMock;

    private SessaoVotacao sessaoVotacaoMock;

    private Voto votoDoUsuarioVotanteMock;

    private Voto votoDoDonoDaPautaMock;

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
        this.votoDoDonoDaPautaMock = new Voto(this.usuarioDonoDaPautaMock.getCpf(), this.usuarioDonoDaPautaMock);
    }

    @Test
    @DisplayName("Deve ser possível abrir uma votação corretamente")
    void givenPossuoUmSessaoVotacaoValidaWhenTentoAbrirSessaoVotacaoThenRetornarSessaoVotacao() {
        when(this.utils.obterDataAtual()).thenReturn(this.dataAbertura);

        when(this.sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenReturn(this.sessaoVotacaoMock);

        SessaoVotacao resposta = this.sessaoVotacaoService.abrirVotacao(this.pautaMock, 5L);

        verify(this.sessaoVotacaoRepository).save(any(SessaoVotacao.class));

        assertEquals(this.sessaoVotacaoMock, resposta);
    }

    @Test
    @DisplayName("Não deve ser possível abrir uma votação ao passar pauta nula")
    void givenPossuoPautaNulaWhenTentoAbrirSessaoVotacaoThenRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> this.sessaoVotacaoService.abrirVotacao(null, null));
    }

    @Test
    @DisplayName("Não deve ser possível abrir uma votação ao passar minutos nulo")
    void givenPossuoMinutosNuloWhenTentoAbrirSessaoVotacaoThenRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> this.sessaoVotacaoService.abrirVotacao(this.pautaMock, null));
    }

    @Test
    @DisplayName("Não deve ser possível abrir uma votação ao passar que possua uma pauta com sessãoVotacao diferente de null")
    void givenPossuoUmSessaoVotacaoComPautaInvalidaWhenTentoAbrirSessaoVotacaoThenRetornarErro() {
        this.pautaMock.setSessaoVotacao(sessaoVotacaoMock);
        assertThrows(IllegalStateException.class, () -> this.sessaoVotacaoService.abrirVotacao(this.pautaMock, 5L));
    }

    @Test
    @DisplayName("Deve ser possível verificar se Usuário pode votar na sessão de votação corretemente")
    void givenPossuoSessaoVotacaoEVotoValidosWhenVerificoSeUsuarioPodeVotarThenNaoDeveRetornarErro() {
        assertDoesNotThrow(() -> this.sessaoVotacaoService.verificarSeUsuarioPodeVotarSessaoVotacao(sessaoVotacaoMock,
                votoDoUsuarioVotanteMock));
    }

    @Test
    @DisplayName("Deve lançar um erro ao verificar se Usuário pode votar na sessão de votação ao enviar voto nulo")
    void givenPossuoVotoNuloWhenVerificoSeUsuarioPodeVotarThenRetornarErro() {

        assertThrows(IllegalArgumentException.class,
                () -> this.sessaoVotacaoService.verificarSeUsuarioPodeVotarSessaoVotacao(this.sessaoVotacaoMock,
                        null));
    }

    @Test
    @DisplayName("Deve lançar um erro ao verificar se Usuário pode votar na sessão de votação ao enviar sessaoVotacao nula")
    void givenSessaovotacaoNuloWhenVerificoSeUsuarioPodeVotarThenRetornarErro() {

        assertThrows(IllegalArgumentException.class,
                () -> this.sessaoVotacaoService.verificarSeUsuarioPodeVotarSessaoVotacao(null,
                        this.votoDoUsuarioVotanteMock));
    }

    @Test
    @DisplayName("Deve lançar um erro ao verificar se Usuário pode votar na sessão de votação quando sessão não estiver ativa")
    void givenPossuoSessaoVotacaoNaoAtivaWhenVerificoSeUsuarioPodeVotarThenRetornarErro() {

        this.sessaoVotacaoMock.setDataFechamento(LocalDateTime.now());
        assertThrows(IllegalStateException.class,
                () -> this.sessaoVotacaoService.verificarSeUsuarioPodeVotarSessaoVotacao(sessaoVotacaoMock,
                        this.votoDoUsuarioVotanteMock));
    }

    @Test
    @DisplayName("Deve lançar um erro ao verificar se Usuário pode votar na sessão de votação quando usuario votante for o dono da pauta")
    void givenVotoComDonoDaPautaWhenVerificoSeUsuarioPodeVotarThenRetornarErro() {

        assertThrows(IllegalArgumentException.class,
                () -> this.sessaoVotacaoService.verificarSeUsuarioPodeVotarSessaoVotacao(sessaoVotacaoMock,
                        this.votoDoDonoDaPautaMock));
    }

    @Test
    @DisplayName("Deve lançar um erro ao verificar se Usuário pode votar na sessão de votação quando usuario ja votou anteriormente")
    void givenVotoRepetidoWhenVerificoSeUsuarioPodeVotarThenRetornarErro() {
        this.sessaoVotacaoMock.setVotosNegativos(this.votoDoUsuarioVotanteMock);
        assertThrows(IllegalStateException.class,
                () -> this.sessaoVotacaoService.verificarSeUsuarioPodeVotarSessaoVotacao(this.sessaoVotacaoMock,
                        this.votoDoUsuarioVotanteMock));
    }

    @Test
    @DisplayName("Deve ser possível inserir voto positivo corretamente")
    void givenPossuoDadosValidosWhenTentoInserirVotoPositivoThenRetornarSessaoVotacao() {
        when(this.utils.obterDataAtual()).thenReturn(this.dataAbertura);
        when(this.sessaoVotacaoRepository.findByPautaIdAndSessaoVotacaoAtiva(this.pautaMock.getId(), this.dataAbertura)).thenReturn(Optional.of(this.sessaoVotacaoMock));

        assertDoesNotThrow(() -> this.sessaoVotacaoService.inserirVotoInterno(this.votoDoUsuarioVotanteMock, this.pautaMock.getId(),
                TipoDeVotoEnum.VOTO_POSITIVO));

        assertEquals(1, this.sessaoVotacaoMock.getVotosPositivos().size());
        verify(this.sessaoVotacaoRepository).save(this.sessaoVotacaoMock);
    }

    @Test
    @DisplayName("Deve ser possível inserir voto negativo corretamente")
    void givenPossuoDadosValidosWhenTentoInserirVotoNegativoThenRetornarSessaoVotacao() {
        when(this.utils.obterDataAtual()).thenReturn(this.dataAbertura);
        when(this.sessaoVotacaoRepository.findByPautaIdAndSessaoVotacaoAtiva(this.pautaMock.getId(), this.dataAbertura)).thenReturn(Optional.of(this.sessaoVotacaoMock));
        assertDoesNotThrow(() -> this.sessaoVotacaoService.inserirVotoInterno(this.votoDoUsuarioVotanteMock, this.pautaMock.getId(),
                TipoDeVotoEnum.VOTO_NEGATIVO));
        assertEquals(1, this.sessaoVotacaoMock.getVotosNegativos().size());
        verify(this.sessaoVotacaoRepository).save(this.sessaoVotacaoMock);
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar inserir com tipo de voto nulo")
    void givenTipoDeVotoNullWhenTentoInserirVotoThenRetornarErro() {
        Long pautaId = this.pautaMock.getId();
        assertThrows(IllegalArgumentException.class, () -> this.sessaoVotacaoService.inserirVotoInterno(this.votoDoUsuarioVotanteMock, pautaId,
                null));
    }

    @Test
    @DisplayName("Deve ser possível obter status da sessão de votação em andamento")
    void givenPossuoUmaSessaoVotacaoValidaEmAndamentoWhenTentoObterStatusThenRetornarStatusSessaoVotacao() {
        StatusSessaoVotacao status = this.sessaoVotacaoService.obterStatusSessaoVotacao(this.sessaoVotacaoMock);
        assertEquals(StatusSessaoVotacao.EM_ANDAMENTO, status);
    }

    @Test
    @DisplayName("Deve ser possível obter status da sessão de votação reprovada")
    void givenPossuoUmaSessaoVotacaoValidaReprovadaWhenTentoObterStatusThenRetornarStatusSessaoVotacao() {
        this.sessaoVotacaoMock.setDataFechamento(LocalDateTime.now());
        this.sessaoVotacaoMock.setVotosNegativos(this.votoDoUsuarioVotanteMock);
        StatusSessaoVotacao status = this.sessaoVotacaoService.obterStatusSessaoVotacao(this.sessaoVotacaoMock);
        assertEquals(StatusSessaoVotacao.REPROVADA, status);
    }

    @Test
    @DisplayName("Deve ser possível obter status da sessão de votação aprovada")
    void givenPossuoUmaSessaoVotacaoValidaAprovadaWhenTentoObterStatusThenRetornarStatusSessaoVotacao() {
        this.sessaoVotacaoMock.setDataFechamento(LocalDateTime.now());
        this.sessaoVotacaoMock.setVotosPositivos(this.votoDoUsuarioVotanteMock);
        StatusSessaoVotacao status = this.sessaoVotacaoService.obterStatusSessaoVotacao(this.sessaoVotacaoMock);
        assertEquals(StatusSessaoVotacao.APROVADA, status);
    }

    @Test
    @DisplayName("Deve retornar um erro ao passar sessão de votação nula")
    void givenPossuoUmaSessaoVotacaoNulaWhenTentoObterStatusThenRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> this.sessaoVotacaoService.obterStatusSessaoVotacao(null));
    }

    @Test
    @DisplayName("Deve buscar sessão de votação ativa ao passar pautaId")
    void givenPossuoPautaIdWhenBuscoSessaoVotacaoAtivaThenRetornarSessaoVotacaoAtiva(){
        when(this.utils.obterDataAtual()).thenReturn(this.dataAbertura);
        when(this.sessaoVotacaoRepository.findByPautaIdAndSessaoVotacaoAtiva(this.pautaMock.getId(), this.dataAbertura)).thenReturn(Optional.of(this.sessaoVotacaoMock));
        assertDoesNotThrow(()->{
            this.sessaoVotacaoService.buscarSessaoVotacaoAtiva(this.pautaMock.getId());
        });
    }
    @Test
    @DisplayName("Deve falhar ao buscar sessão de votação ativa ao passar pautaId de sessao inexistente ou inativa")
    void givenPossuoPautaIdInvalidaWhenBuscoSessaoVotacaoAtivaThenRetornarSessaoVotacaoAtiva(){
        when(this.utils.obterDataAtual()).thenReturn(this.dataAbertura);
        assertThrows(IllegalArgumentException.class, ()->{
            this.sessaoVotacaoService.buscarSessaoVotacaoAtiva(this.pautaMock.getId());
        });
    }
}
