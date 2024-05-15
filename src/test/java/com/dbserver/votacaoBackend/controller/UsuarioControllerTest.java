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
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.dto.CriarUsuarioDto;
import com.dbserver.votacaoBackend.domain.usuario.repository.UsuarioRepository;
import com.dbserver.votacaoBackend.fixture.AutenticacaoFixture;
import com.dbserver.votacaoBackend.fixture.UsuarioFixture;
import com.dbserver.votacaoBackend.infra.security.token.TokenService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.stream.Stream;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureJsonTesters
class UsuarioControllerTest {
    private AutenticacaoRepository autenticacaoRepository;
    private UsuarioRepository usuarioRepository;
    private MockMvc mockMvc;
    private JacksonTester<CriarUsuarioDto> criarUsuarioDtoJson;
    private CriarUsuarioDto criarUsuarioDto;
    private AutenticacaoDto autenticacaoDto;
    private TokenService tokenService;
    private String token;
    private Usuario usuarioCadastrado;

    @Autowired
    public UsuarioControllerTest(AutenticacaoRepository autenticacaoRepository,
            UsuarioRepository usuarioRepository, MockMvc mockMvc, JacksonTester<CriarUsuarioDto> criarUsuarioDtoJson,
            TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.autenticacaoRepository = autenticacaoRepository;
        this.mockMvc = mockMvc;
        this.criarUsuarioDtoJson = criarUsuarioDtoJson;
        this.tokenService = tokenService;
    }

    @BeforeEach
    void configurar() {
        this.usuarioCadastrado = UsuarioFixture.usuarioAdmin();
        this.usuarioRepository.save(this.usuarioCadastrado);

        System.out.println(this.usuarioCadastrado.getCpf());
        Autenticacao autenticacao = AutenticacaoFixture.autenticacaoAdmin(usuarioCadastrado);
        this.autenticacaoRepository.save(autenticacao);
        
        this.token = this.tokenService.gerarToken(autenticacao);

    }

    @AfterEach
    @Transactional
    void limpar() {
        this.autenticacaoRepository.deleteAll();
        this.usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve ser possível criar um usuário corretamente")
    void dadoCriarUsuarioDtoCorretoQuandoTentoCriarUsuarioEntaoRetornarCriarUsuarioRespostaDto() throws Exception {
        this.autenticacaoDto = AutenticacaoFixture.autenticacaoDtoUsuarioValido();

        this.criarUsuarioDto = UsuarioFixture.criarUsuarioDto(this.autenticacaoDto);

        String email = this.autenticacaoDto.email();
        String nome = this.criarUsuarioDto.nome();
        String sobrenome = this.criarUsuarioDto.sobrenome();
        String cpf = this.criarUsuarioDto.cpf();
        boolean admin = this.criarUsuarioDto.admin();

        String json = this.criarUsuarioDtoJson.write(this.criarUsuarioDto).getJson();
        mockMvc.perform(MockMvcRequestBuilders
                .post("/usuario")
                .header("Authorization", "Bearer " + this.token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.nome").value(nome))
                .andExpect(jsonPath("$.sobrenome").value(sobrenome))
                .andExpect(jsonPath("$.cpf").value(cpf))
                .andExpect(jsonPath("$.admin").value(admin));
    }

    private static Stream<Arguments> dadosInválidosParaCriarUsuário() {
        return Stream.of(
                Arguments.of("email", "senha123", "Pedro", "Cecilio", "12345678912", true,
                        "Email com formato inválido."),
                Arguments.of(null, "senha123", "Pedro", "Cecilio", "12345678912", true, "Email com formato inválido."),
                Arguments.of("", "senha123", "Pedro", "Cecilio", "12345678912", true, "Email com formato inválido."),
                Arguments.of(AutenticacaoFixture.EMAIL_ADMIN_CORRETO, "senha123", "Pedro", "Cecilio", "12345678912", true,
                        "Email já cadastrado."),

                Arguments.of("example2@example.com", null, "Pedro", "Cecilio", "12345678912", true,
                        "Senha deve ser informada."),
                Arguments.of("example2@example.com", "", "Pedro", "Cecilio", "12345678912", true,
                        "Senha deve ser informada."),
                Arguments.of("example2@example.com", "senha12", "Pedro", "Cecilio", "12345678912", true,
                        "Senha deve conter 8 caracteres no mínimo."),

                Arguments.of("example2@example.com", "senha123", null, "Cecilio", "12345678912", true,
                        "Nome deve ser informado."),
                Arguments.of("example2@example.com", "senha123", "Pe", "Cecilio", "12345678912", true,
                        "Nome deve conter entre 3 e 20 caracteres."),
                Arguments.of("example2@example.com", "senha123", "Pedro Samuel De Jesus", "Cecilio", "12345678912",
                        true, "Nome deve conter entre 3 e 20 caracteres."),

                Arguments.of("example2@example.com", "senha123", "Pedro", null, "12345678912", true,
                        "Sobrenome deve ser informado."),
                Arguments.of("example2@example.com", "senha123", "Pedro", "C", "12345678912", true,
                        "Sobrenome deve conter entre 2 e 20 caracteres."),
                Arguments.of("example2@example.com", "senha123", "Pedro", "SobrenomeMuitoGrande.", "12345678912", true,
                        "Sobrenome deve conter entre 2 e 20 caracteres."),

                Arguments.of("example2@example.com", "senha123", "Pedro", "Cecilio.", UsuarioFixture.CPF_ADMIN, true,
                        "Cpf já cadastrado."),
                Arguments.of("example2@example.com", "senha123", "Pedro", "Cecilio.", "123456789abc", true,
                        "Cpf deve conter 11 caracteres numéricos."),
                Arguments.of("example2@example.com", "senha123", "Pedro", "Cecilio.", "12345678", true,
                        "Cpf deve conter 11 caracteres numéricos."));

    }

    @ParameterizedTest
    @MethodSource("dadosInválidosParaCriarUsuário")
    @DisplayName("Deve falhar ao tentar criar um usuário corretamente")
    void dadoPossuoDadosInvalidosQuandoTentoCriarNovoUsuarioDeveRetornarErro(String email, String senha, String nome, String sobrenome, String cpf, boolean admin, String mensagemErro)
            throws Exception {
        this.autenticacaoDto = new AutenticacaoDto(email,
                senha);
                
        this.criarUsuarioDto = new CriarUsuarioDto(this.autenticacaoDto, nome, sobrenome, cpf, admin);

        String json = this.criarUsuarioDtoJson.write(this.criarUsuarioDto).getJson();
        mockMvc.perform(MockMvcRequestBuilders
                .post("/usuario")
                .header("Authorization", "Bearer " + this.token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value(mensagemErro));
    }

    @Test
    @DisplayName("Deve ser possível buscar usuário logado")
    void dadoEstouAutenticadoQuandoTentoBuscarUsuarioLogadoEntaoRetornarUsuarioRespostaDto() throws Exception {

        String nome = this.usuarioCadastrado.getNome();
        String sobrenome = this.usuarioCadastrado.getSobrenome();
        String cpf = this.usuarioCadastrado.getCpf();
        boolean admin = this.usuarioCadastrado.isAdmin();

        mockMvc.perform(MockMvcRequestBuilders
                .get("/usuario/usuarioLogado")
                .header("Authorization", "Bearer " + this.token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value(nome))
                .andExpect(jsonPath("$.sobrenome").value(sobrenome))
                .andExpect(jsonPath("$.cpf").value(cpf))
                .andExpect(jsonPath("$.admin").value(admin));
    }
    
    @Test
    @DisplayName("Deve retornar true ao verificar se usuario existe ao passar cpf cadastrado")
    void dadoPossuoCpfDeUmUsuarioExistenteQuandoVerificoSeEleExisteEntaoRetornarVerificarSeUsuarioExisteRespostaDtoTrue() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .get("/usuario/existe?cpf=" + this.usuarioCadastrado.getCpf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.existe").value(true));
               
    }
    @Test
    @DisplayName("Deve retornar false ao verificar se usuário existe ao passar cpf não cadastrado")
    void dadoPossuoCpfDeUmUsuarioInexistenteQuandoVerificoSeEleExisteEntaoRetornarVerificarSeUsuarioExisteRespostaDtoFalse() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .get("/usuario/existe?cpf=" + UsuarioFixture.CPF_ALEATORIO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.existe").value(false));
    }
    @Test
    @DisplayName("Deve retornar false ao verificar se usuário existe ao não informar cpf")
    void dadoNaoPossuoCpfQuandoVerificoSeEleExisteEntaoRetornarVerificarSeUsuarioExisteRespostaDtoFalse() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .get("/usuario/existe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.existe").value(false));
    }
}
