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
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutenticacaoDto;
import com.dbserver.votacaoBackend.domain.autenticacao.repository.AutenticacaoRepository;
import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;
import com.dbserver.votacaoBackend.domain.pauta.repository.PautaRepository;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.AbrirVotacaoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoExternoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoInternoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.TipoDeVotoEnum;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.dto.CriarUsuarioDto;
import com.dbserver.votacaoBackend.domain.usuario.repository.UsuarioRepository;
import com.dbserver.votacaoBackend.infra.security.token.TokenService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
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
    private Usuario admin;
    private Usuario usuario;
    private String cpfUsuarioNaoCadastrado;

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
        this.admin = new Usuario("João", "Silva", "12345678900", true);
        Autenticacao adminAuth = new Autenticacao("example@example.com",
                "$2a$10$6V3ZuwVZxOfqS0IKfhqk1uUzdUe/8jl1flMBk5AVzmPSX2wYKd/vS");
        this.usuario = new Usuario("Pedro", "Cecilio", "12345678911", false);
        Autenticacao usuarioAuth = new Autenticacao("example2@example.com",
                "$2a$10$6V3ZuwVZxOfqS0IKfhqk1uUzdUe/8jl1flMBk5AVzmPSX2wYKd/vS");
        this.usuarioRepository.saveAll(List.of(this.admin, this.usuario));

        adminAuth.setUsuario(this.admin);
        usuarioAuth.setUsuario(this.usuario);
        this.autenticacaoRepository.saveAll(List.of(adminAuth, usuarioAuth));

        this.tokenAdmin = this.tokenService.gerarToken(adminAuth);
        this.tokenUsuario = this.tokenService.gerarToken(usuarioAuth);

        this.pautaTransporte = new Pauta("Sabe dirigir?", Categoria.TRANSPORTE.toString(), this.admin);
        
        this.pautaRepository.save(pautaTransporte);


        this.cpfUsuarioNaoCadastrado = "33322211100";
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

        AbrirVotacaoDto abrirVotacaoDto = new AbrirVotacaoDto(10L, this.pautaTransporte.getId());
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

    @ParameterizedTest
    @MethodSource("dadosInvalidosAbrirVotacao")
    @DisplayName("Não deve ser possível abrir sessão votação em uma pauta ao passar dados inválidos")
    void dadoPossuoAbrirVotacaoDtoIncorretoQuandoTentoAbrirSessaoVotacaoEntaoRetornarRespostaErro(Long minutos, Long pautaId, String mensagemErro) throws Exception {

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

    private static Stream<Arguments> dadosInvalidosAbrirVotacao() {
        return Stream.of(
                Arguments.of(0L, 1L, "Minutos deve ser maior que 0."),
                Arguments.of(null, 1L, "Minutos deve ser informado."),
                Arguments.of(5L, null, "PautaId deve ser informada.")
                );
    }

    @Test
    @DisplayName("Deve ser possível votar internamente em uma pauta")
    void dadoPossuoFecharVotacaoDtoCorretoQuandoTentoVotarInternamenteEntaoRetornarRespostaSessaoVotacao() throws Exception{
        SessaoVotacao sessaoVotacao = new SessaoVotacao(pautaTransporte, LocalDateTime.now(), LocalDateTime.now().plusMinutes(5));
        pautaTransporte.setSessaoVotacao(sessaoVotacao);

        this.pautaRepository.save(pautaTransporte);

        InserirVotoInternoDto inserirVotoInternoDto = new InserirVotoInternoDto(this.pautaTransporte.getId(), TipoDeVotoEnum.VOTO_POSITIVO );

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
    void dadoPossuoFecharVotacaoDtoCorretoQuandoTentoVotarExternamenteEntaoRetornarRespostaSessaoVotacao() throws Exception{
        SessaoVotacao sessaoVotacao = new SessaoVotacao(pautaTransporte, LocalDateTime.now(), LocalDateTime.now().plusMinutes(5));
        pautaTransporte.setSessaoVotacao(sessaoVotacao);

        this.pautaRepository.save(pautaTransporte);

        InserirVotoExternoDto inserirVotoExternoDto = new InserirVotoExternoDto(this.pautaTransporte.getId(), TipoDeVotoEnum.VOTO_NEGATIVO, this.cpfUsuarioNaoCadastrado, null);
        
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
