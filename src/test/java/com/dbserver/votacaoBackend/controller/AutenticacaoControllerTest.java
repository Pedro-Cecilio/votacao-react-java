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
import com.dbserver.votacaoBackend.domain.autenticacao.dto.ValidarVotoExternoDto;
import com.dbserver.votacaoBackend.domain.autenticacao.repository.AutenticacaoRepository;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.repository.UsuarioRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.stream.Stream;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureJsonTesters
class AutenticacaoControllerTest {

    private AutenticacaoRepository autenticacaoRepository;
    private UsuarioRepository usuarioRepository;
    private Autenticacao autenticacao;
    private AutenticacaoDto autenticacaoDto;
    private MockMvc mockMvc;
    private JacksonTester<AutenticacaoDto> autenticacaoDtoJson;
    private JacksonTester<ValidarVotoExternoDto> validarVotoExternoDtoJson;
    private String senhaCorreta;
    private ValidarVotoExternoDto validarVotoExternoDto;

    @Autowired
    public AutenticacaoControllerTest(AutenticacaoRepository autenticacaoRepository,
            UsuarioRepository usuarioRepository, MockMvc mockMvc, JacksonTester<AutenticacaoDto> autenticacaoDtoJson,
            JacksonTester<ValidarVotoExternoDto> validarVotoExternoDtoJson) {
        this.usuarioRepository = usuarioRepository;
        this.autenticacaoRepository = autenticacaoRepository;
        this.mockMvc = mockMvc;
        this.autenticacaoDtoJson = autenticacaoDtoJson;
        this.validarVotoExternoDtoJson = validarVotoExternoDtoJson;
    }

    @BeforeEach
    void configurar() {
        Usuario usuario = new Usuario("João", "Silva", "12345678900", true);
        this.autenticacao = new Autenticacao("example@example.com",
                "$2a$10$6V3ZuwVZxOfqS0IKfhqk1uUzdUe/8jl1flMBk5AVzmPSX2wYKd/vS");
        this.senhaCorreta = "senha123";
        this.usuarioRepository.save(usuario);
        this.autenticacao.setUsuario(usuario);
        this.autenticacaoRepository.save(this.autenticacao);
        this.autenticacaoDto = null;
    }

    @AfterEach
    @Transactional
    void limpar() {
        this.autenticacaoRepository.deleteAll();
        this.usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve ser possível realizar login corretamente")
    void givenPossuoDadosDeAutenticacaCorretosoWhenTentoRealizarLoginThenRetornarAutenticacaoRespostaDto()
            throws Exception {

        this.autenticacaoDto = new AutenticacaoDto(this.autenticacao.getEmail(), this.senhaCorreta);

        String json = this.autenticacaoDtoJson.write(this.autenticacaoDto).getJson();

        mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.admin").value(this.autenticacao.getUsuario().isAdmin()));
    }

    @Test
    @DisplayName("Não deve ser possível realizar login com senha incorreta")
    void givenPossuoUmaSenhaIncorretaWhenTentoRealizarLoginThenRetornarRespostaErro()
            throws Exception {

        this.autenticacaoDto = new AutenticacaoDto(this.autenticacao.getEmail(), "senhaIncorreta");

        String json = this.autenticacaoDtoJson.write(this.autenticacaoDto).getJson();

        mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.erro").value("Dados de login inválidos."));
    }

    @Test
    @DisplayName("Não deve ser possível realizar login com email inexistente")
    void givenPossuoUmaEmailInexistenteWhenTentoRealizarLoginThenRetornarRespostaErro()
            throws Exception {

        this.autenticacaoDto = new AutenticacaoDto("emailInexistente@email.com", this.senhaCorreta);

        String json = this.autenticacaoDtoJson.write(this.autenticacaoDto).getJson();

        mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.erro").value("Dados de login inválidos."));
    }

    @ParameterizedTest
    @DisplayName("Testes de login com dados inválidos")
    @MethodSource("dadosInvalidosParaRealizarLogin")
    void givenAutenticacaoDtoDadosInvalidosWhenTentoRealizarLoginThenRetornarRespostaErro(String email, String senha,
            String mensagemErro)
            throws Exception {

        this.autenticacaoDto = new AutenticacaoDto(email, senha);

        String json = this.autenticacaoDtoJson.write(autenticacaoDto).getJson();

        mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value(mensagemErro));
    }

    private static Stream<Arguments> dadosInvalidosParaRealizarLogin() {
        return Stream.of(
                Arguments.of("email", "senha123", "Email com formato inválido."),
                Arguments.of("", "senha123", "Email deve ser informado."),
                Arguments.of(null, "senha123", "Email deve ser informado."),

                Arguments.of("example@example.com", "", "Senha deve ser informada."),
                Arguments.of("example@example.com", null, "Senha deve ser informada."));
    }

    @Test
    @DisplayName("Deve ser possível validar usuário existente com dados para validar voto externo validos ao tentar votar externamente")
    void givenPossuoDadosValidarVotoExternoCorretosWhenTentoValidarVotoExternoThenRetornarValidarVotoExternoComTrue()
            throws Exception {
        String cpf = this.autenticacao.getUsuario().getCpf();

        this.validarVotoExternoDto = new ValidarVotoExternoDto(cpf, senhaCorreta);

        String json = this.validarVotoExternoDtoJson.write(this.validarVotoExternoDto).getJson();
        mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/votoExterno")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valido").value(true));
    }

    @Test
    @DisplayName("Não deve ser possível validar usuário existente com ao passar cpf não cadastrado ao tentar votar externamente")
    void givenCpfNaoCadastradoWhenTentoValidarVotoExternoThenRetornarRespostaErro()
            throws Exception {
        String cpf = "33322211100";

        this.validarVotoExternoDto = new ValidarVotoExternoDto(cpf, senhaCorreta);

        String json = this.validarVotoExternoDtoJson.write(this.validarVotoExternoDto).getJson();

        mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/votoExterno")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.erro").value("Dados de autenticação inválidos."));
    }

    @Test
    @DisplayName("Não deve ser possível validar usuário existente com ao passar senha incorreta ao tentar votar externamente")
    void givenSenhaIncorretaWhenTentoValidarVotoExternoThenRetornarRespostaErro()
            throws Exception {
        String cpf = this.autenticacao.getUsuario().getCpf();

        this.validarVotoExternoDto = new ValidarVotoExternoDto(cpf, "senhaIncorreta");

        String json = this.validarVotoExternoDtoJson.write(this.validarVotoExternoDto).getJson();
        
        mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/votoExterno")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.erro").value("Dados de autenticação inválidos."));
    }
}
