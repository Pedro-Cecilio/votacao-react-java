package com.dbserver.votacaoBackend.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.repository.AutenticacaoRepository;
import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.repository.PautaRepository;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.AbrirVotacaoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoExternoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoInternoDto;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.repository.UsuarioRepository;
import com.dbserver.votacaoBackend.fixture.autenticacao.AutenticacaoFixture;
import com.dbserver.votacaoBackend.fixture.pauta.PautaFixture;
import com.dbserver.votacaoBackend.fixture.sessaoVotacao.AbrirVotacaoDtoFixture;
import com.dbserver.votacaoBackend.fixture.sessaoVotacao.InserirVotoInternoDtoFixture;
import com.dbserver.votacaoBackend.fixture.sessaoVotacao.SessaoVotacaoFixture;
import com.dbserver.votacaoBackend.fixture.usuario.UsuarioFixture;
import com.dbserver.votacaoBackend.infra.security.token.TokenService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;
import java.util.stream.Stream;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureJsonTesters
class SessaoVotacaoControllerTest {

    private AutenticacaoRepository autenticacaoRepository;
    private UsuarioRepository usuarioRepository;
    private MockMvc mockMvc;
    private JacksonTester<AbrirVotacaoDto> abrirVotacaoDtoJson;
    private JacksonTester<InserirVotoInternoDto> inserirVotoInternoDtoJson;
    private JacksonTester<InserirVotoExternoDto> inserirVotoExternoDtoJson;
    private TokenService tokenService;
    private String tokenAdmin;
    private String tokenUsuario;

    private PautaRepository pautaRepository;

    private Pauta pautaTransporte;

    @Autowired
    public SessaoVotacaoControllerTest(AutenticacaoRepository autenticacaoRepository,
            UsuarioRepository usuario, PautaRepository pautaRepository, MockMvc mockMvc,
            JacksonTester<AbrirVotacaoDto> abrirVotacaoDtoJson,
            TokenService tokenService, UsuarioRepository usuarioRepository,
            JacksonTester<InserirVotoInternoDto> inserirVotoInternoDtoJson,
            JacksonTester<InserirVotoExternoDto> inserirVotoExternoDtoJson) {

        this.usuarioRepository = usuarioRepository;
        this.autenticacaoRepository = autenticacaoRepository;
        this.mockMvc = mockMvc;
        this.abrirVotacaoDtoJson = abrirVotacaoDtoJson;
        this.tokenService = tokenService;
        this.pautaRepository = pautaRepository;
        this.inserirVotoInternoDtoJson = inserirVotoInternoDtoJson;
        this.inserirVotoExternoDtoJson = inserirVotoExternoDtoJson;

    }

    @BeforeEach
    void configurar() {
        Usuario admin = UsuarioFixture.usuarioAdmin();
        Usuario usuario = UsuarioFixture.usuarioNaoAdmin();
        this.usuarioRepository.saveAll(List.of(admin, usuario));

        Autenticacao adminAuth = AutenticacaoFixture.autenticacaoAdmin(admin);
        Autenticacao usuarioAuth = AutenticacaoFixture.autenticacaoUsuario(usuario);

        this.autenticacaoRepository.saveAll(List.of(adminAuth, usuarioAuth));

        this.tokenAdmin = this.tokenService.gerarToken(adminAuth);
        this.tokenUsuario = this.tokenService.gerarToken(usuarioAuth);

        this.pautaTransporte = PautaFixture.pautaTransporte(admin);
        this.pautaRepository.save(pautaTransporte);
    }

    @AfterEach
    @Transactional
    void limpar() {
        this.pautaRepository.deleteAll();
        this.autenticacaoRepository.deleteAll();
        this.usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve ser possível abrir sessão votação em uma pauta")
    void dadoPossuoAbrirVotacaoDtoCorretoQuandoTentoAbrirSessaoVotacaoEntaoRetornarRespostaSessaoVotacao()
            throws Exception {

        AbrirVotacaoDto abrirVotacaoDto = AbrirVotacaoDtoFixture.abrirVotacaoDtoValido(this.pautaTransporte.getId());
        String json = this.abrirVotacaoDtoJson.write(abrirVotacaoDto).getJson();

        mockMvc.perform(MockMvcRequestBuilders
                .post("/votacao/abrir")
                .header("Authorization", "Bearer " + this.tokenAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.pautaId").value(this.pautaTransporte.getId()))
                .andExpect(jsonPath("$.sessaoAtiva").value(true));
    }

    private static Stream<Arguments> dadosInvalidosAbrirVotacao() {
        return Stream.of(
                Arguments.of(0L, 1L, "Minutos deve ser maior que 0."),
                Arguments.of(null, 1L, "Minutos deve ser informado."),
                Arguments.of(5L, null, "PautaId deve ser informada."));
    }
    
    @ParameterizedTest
    @MethodSource("dadosInvalidosAbrirVotacao")
    @DisplayName("Não deve ser possível abrir sessão votação em uma pauta ao passar dados inválidos")
    void dadoPossuoAbrirVotacaoDtoIncorretoQuandoTentoAbrirSessaoVotacaoEntaoRetornarRespostaErro(Long minutos,
            Long pautaId, String mensagemErro) throws Exception {

        AbrirVotacaoDto abrirVotacaoDto = new AbrirVotacaoDto(minutos, pautaId);
        String json = this.abrirVotacaoDtoJson.write(abrirVotacaoDto).getJson();

        mockMvc.perform(MockMvcRequestBuilders
                .post("/votacao/abrir")
                .header("Authorization", "Bearer " + this.tokenAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value(mensagemErro));
    }

    @Test
    @DisplayName("Deve ser possível votar internamente em uma pauta")
    void dadoPossuoFecharVotacaoDtoCorretoQuandoTentoVotarInternamenteEntaoRetornarRespostaSessaoVotacao()
            throws Exception {
        SessaoVotacao sessaoVotacao = SessaoVotacaoFixture.sessaoVotacaoAtiva(pautaTransporte);
        pautaTransporte.setSessaoVotacao(sessaoVotacao);
        this.pautaRepository.save(pautaTransporte);

        InserirVotoInternoDto inserirVotoInternoDto = InserirVotoInternoDtoFixture
                .inserirVotoInternoPositivoDto(this.pautaTransporte.getId());

        String json = this.inserirVotoInternoDtoJson.write(inserirVotoInternoDto).getJson();

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/votacao/votoInterno")
                .header("Authorization", "Bearer " + this.tokenUsuario)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.pautaId").value(this.pautaTransporte.getId()))
                .andExpect(jsonPath("$.votosPositivos").value(1));
    }

    @Test
    @DisplayName("Deve ser possível votar externamente em uma pauta")
    void dadoPossuoFecharVotacaoDtoCorretoQuandoTentoVotarExternamenteEntaoRetornarRespostaSessaoVotacao()
            throws Exception {
        SessaoVotacao sessaoVotacao = SessaoVotacaoFixture.sessaoVotacaoAtiva(pautaTransporte);
        pautaTransporte.setSessaoVotacao(sessaoVotacao);
        this.pautaRepository.save(pautaTransporte);

        InserirVotoExternoDto inserirVotoExternoDto = InserirVotoInternoDtoFixture
                .inserirVotoExternoNegativoDto(this.pautaTransporte.getId());

        String json = this.inserirVotoExternoDtoJson.write(inserirVotoExternoDto).getJson();

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/votacao/votoExterno")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.pautaId").value(this.pautaTransporte.getId()))
                .andExpect(jsonPath("$.votosNegativos").value(1));
    }
}
