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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.repository.AutenticacaoRepository;
import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.dto.CriarPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.dto.RespostaPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;
import com.dbserver.votacaoBackend.domain.pauta.repository.PautaRepository;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.StatusSessaoVotacao;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.repository.UsuarioRepository;
import com.dbserver.votacaoBackend.infra.security.token.TokenService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureJsonTesters
class PautaControllerTest {
        private AutenticacaoRepository autenticacaoRepository;
        private UsuarioRepository usuarioRepository;
        private Autenticacao autenticacao;
        private MockMvc mockMvc;
        private JacksonTester<CriarPautaDto> criarPautaDtoJson;
        private CriarPautaDto criarPautaDto;
        private TokenService tokenService;
        private String token;
        private Usuario usuarioCadastrado;
        private PautaRepository pautaRepository;
        private ObjectMapper objectMapper;

        @Autowired
        public PautaControllerTest(AutenticacaoRepository autenticacaoRepository,
                        UsuarioRepository usuarioRepository, MockMvc mockMvc,
                        JacksonTester<CriarPautaDto> criarPautaDtoJson,
                        TokenService tokenService, PautaRepository pautaRepository, ObjectMapper objectMapper) {
                this.usuarioRepository = usuarioRepository;
                this.autenticacaoRepository = autenticacaoRepository;
                this.mockMvc = mockMvc;
                this.criarPautaDtoJson = criarPautaDtoJson;
                this.tokenService = tokenService;
                this.pautaRepository = pautaRepository;
                this.objectMapper = objectMapper;
        }

        @BeforeEach
        void configurar() {
                this.usuarioCadastrado = new Usuario("João", "Silva", "12345678900", true);
                this.autenticacao = new Autenticacao("example@example.com",
                                "$2a$10$6V3ZuwVZxOfqS0IKfhqk1uUzdUe/8jl1flMBk5AVzmPSX2wYKd/vS");
                this.usuarioRepository.save(this.usuarioCadastrado);
                this.autenticacao.setUsuario(this.usuarioCadastrado);
                this.autenticacaoRepository.save(this.autenticacao);
                this.token = this.tokenService.gerarToken(autenticacao);

        }

        @AfterEach
        @Transactional
        void limpar() {
                this.pautaRepository.deleteAll();
                this.autenticacaoRepository.deleteAll();
                this.usuarioRepository.deleteAll();
        }

        @Test
        @DisplayName("Deve ser possível criar uma pauta corretamente")
        void dadoTenhoCriarPautaDtoComDadosCorretosQuandoTentoCriarPautaEntaoRetornarRespostaPautaDto() throws Exception {
                this.criarPautaDto = new CriarPautaDto("Você sabe dirigir?", Categoria.TRANSPORTE.toString());
                String json = this.criarPautaDtoJson.write(criarPautaDto).getJson();

                mockMvc.perform(MockMvcRequestBuilders
                                .post("/pauta")
                                .header("Authorization", "Bearer " + this.token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").isNumber())
                                .andExpect(jsonPath("$.assunto").value(this.criarPautaDto.assunto()))
                                .andExpect(jsonPath("$.usuario.id").value(this.usuarioCadastrado.getId()))
                                .andExpect(jsonPath("$.usuario.sobrenome").value(this.usuarioCadastrado.getSobrenome()))
                                .andExpect(jsonPath("$.usuario.cpf").value(this.usuarioCadastrado.getCpf()))
                                .andExpect(jsonPath("$.usuario.admin").value(this.usuarioCadastrado.isAdmin()))
                                .andExpect(jsonPath("$.sessaoVotacao").isEmpty());
        }

        @ParameterizedTest
        @MethodSource("dadosInvalidosCriarPauta")
        @DisplayName("Deve ser possível criar uma pauta corretamente")
        void dadoTenhoCriarPautaDtoComDadosInvalidosQuandoTentoCriarPautaEntaoRetornarRespostaErro(String assunto,
                        String categoria, String mensagemErro) throws Exception {

                this.criarPautaDto = new CriarPautaDto(assunto, categoria);
                String json = this.criarPautaDtoJson.write(criarPautaDto).getJson();

                mockMvc.perform(MockMvcRequestBuilders
                                .post("/pauta")
                                .header("Authorization", "Bearer " + this.token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.erro").value(mensagemErro));
        }

        private static Stream<Arguments> dadosInvalidosCriarPauta() {
                return Stream.of(
                                Arguments.of(null, Categoria.TRANSPORTE.toString(),
                                                "Assunto deve ser informado."),
                                Arguments.of("", Categoria.TRANSPORTE.toString(),
                                                "Assunto deve ser informado."),
                                Arguments.of("Você sabe dirigir?", null,
                                                "Categoria deve ser informada."),
                                Arguments.of("Você sabe dirigir?", " ",
                                                "Categoria inválida."),
                                Arguments.of("Você sabe dirigir?", "CATEGORIA_INVALIDA",
                                                "Categoria inválida."));
        }

        @Test
        @DisplayName("deve ser possível lista pautas do usuário logado")
        void dadoNaoEnvioCategoriaQuandoBuscoTodasMinhasPautasQuandoRetornarListaDePautas() throws Exception {
                Pauta pautaSaude = new Pauta("Você está bem de saúde?", Categoria.SAUDE.toString(),
                                this.usuarioCadastrado);

                Pauta pautaTransporte = new Pauta("Sabe dirigir?", Categoria.TRANSPORTE.toString(),
                                this.usuarioCadastrado);

                List<Pauta> pautas = List.of(pautaSaude, pautaTransporte);

                this.pautaRepository.saveAll(pautas);

                MockHttpServletResponse resposta = mockMvc.perform(MockMvcRequestBuilders
                                .get("/pauta/usuarioLogado")
                                .header("Authorization", "Bearer " + this.token)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray()).andReturn().getResponse();

                List<RespostaPautaDto> pautasDoUsuario = this.objectMapper.readValue(resposta.getContentAsString(),
                                new TypeReference<List<RespostaPautaDto>>() {
                                });
                assertEquals(pautas.size(), pautasDoUsuario.size());
        }

        @Test
        @DisplayName("deve ser possível lista pautas do usuário logado")
        void dadoEnvioCategoriaQuandoBuscoTodasMinhasPautasQuandoRetornarListaDePautas() throws Exception {
                Pauta pautaSaude = new Pauta("Você está bem de saúde?", Categoria.SAUDE.toString(),
                                this.usuarioCadastrado);

                Pauta pautaTransporte = new Pauta("Sabe dirigir?", Categoria.TRANSPORTE.toString(),
                                this.usuarioCadastrado);

                List<Pauta> pautas = List.of(pautaSaude, pautaTransporte);
                this.pautaRepository.saveAll(pautas);

                MockHttpServletResponse resposta = mockMvc.perform(MockMvcRequestBuilders
                                .get("/pauta/usuarioLogado?categoria=TRANSPORTE")
                                .header("Authorization", "Bearer " + this.token)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andReturn().getResponse();

                List<RespostaPautaDto> pautasDoUsuario = this.objectMapper.readValue(resposta.getContentAsString(),
                                new TypeReference<List<RespostaPautaDto>>() {
                                });
                assertEquals(1, pautasDoUsuario.size());
        }

        @Test
        @DisplayName("deve ser possível listar todas pautas ativas")
        void dadoEstouLogadoQuandoBuscoTodasPautasAtivasQuandoRetornarListaDePautas() throws Exception {
                Pauta pautaSaude = new Pauta("Você está bem de saúde?", Categoria.SAUDE.toString(),
                                this.usuarioCadastrado);

                Pauta pautaTransporte = new Pauta("Sabe dirigir?", Categoria.TRANSPORTE.toString(),
                                this.usuarioCadastrado);

                SessaoVotacao sessaoVotacao = new SessaoVotacao(pautaTransporte, LocalDateTime.now(),
                                LocalDateTime.now().plusMinutes(5));

                pautaTransporte.setSessaoVotacao(sessaoVotacao);

                List<Pauta> pautas = List.of(pautaSaude, pautaTransporte);
                this.pautaRepository.saveAll(pautas);

                MockHttpServletResponse resposta = mockMvc.perform(MockMvcRequestBuilders
                                .get("/pauta/ativas")
                                .header("Authorization", "Bearer " + this.token)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andReturn().getResponse();

                List<RespostaPautaDto> pautasDoUsuario = this.objectMapper.readValue(resposta.getContentAsString(),
                                new TypeReference<List<RespostaPautaDto>>() {
                                });

                assertEquals(1, pautasDoUsuario.size());
        }

        @Test
        @DisplayName("deve ser possível buscar pauta ativa por Id")
        void dadoPossuoPautaIdQuandoBuscoAtivaPorIdQuandoRetornarRespostaPautaDto() throws Exception {
                Pauta pautaTransporte = new Pauta("Sabe dirigir?", Categoria.TRANSPORTE.toString(),
                                this.usuarioCadastrado);

                SessaoVotacao sessaoVotacao = new SessaoVotacao(pautaTransporte, LocalDateTime.now(),
                                LocalDateTime.now().plusMinutes(5));

                pautaTransporte.setSessaoVotacao(sessaoVotacao);

                this.pautaRepository.save(pautaTransporte);

                mockMvc.perform(MockMvcRequestBuilders
                                .get("/pauta/{id}", pautaTransporte.getId())
                                .header("Authorization", "Bearer " + this.token)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(pautaTransporte.getId()))
                                .andExpect(jsonPath("$.usuario.id").value(this.usuarioCadastrado.getId()))
                                .andExpect(jsonPath("$.sessaoVotacao.id")
                                                .value(pautaTransporte.getSessaoVotacao().getId()));
        }

        @Test
        @DisplayName("Não deve ser possível buscar pauta ativa por Id ao passar id de pauta não ativa")
        void dadoPossuoPautaIdInvalidoQuandoBuscoAtivaPorIdQuandoRetornarRespostaErro() throws Exception {

                mockMvc.perform(MockMvcRequestBuilders
                                .get("/pauta/{id}", 50)
                                .header("Authorization", "Bearer " + this.token)
                                .contentType(MediaType.APPLICATION_JSON))
                                // .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.erro").value("Pauta informada não possui sessão ativa."));

        }

        @Test
        @DisplayName("Deve ser possível buscar detalhes de uma pauta ")
        void dadoPossuoPautaIdQuandoBuscoDetalhesPautaQuandoRetornarRespostaPautaDto() throws Exception {
                Pauta pautaTransporte = new Pauta("Sabe dirigir?", Categoria.TRANSPORTE.toString(),
                                this.usuarioCadastrado);

                SessaoVotacao sessaoVotacao = new SessaoVotacao(pautaTransporte, LocalDateTime.now(),
                                LocalDateTime.now().plusMinutes(5));
                                
                pautaTransporte.setSessaoVotacao(sessaoVotacao);

                this.pautaRepository.save(pautaTransporte);

                mockMvc.perform(MockMvcRequestBuilders
                                .get("/pauta/detalhes/{id}", pautaTransporte.getId())
                                .header("Authorization", "Bearer " + this.token)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.dadosPauta.id").value(pautaTransporte.getId()))
                                .andExpect(jsonPath("$.status").value(StatusSessaoVotacao.EM_ANDAMENTO.toString()));
        }

        @Test
        @DisplayName("Não deve ser possível buscar detalhes de uma pauta ao passar id inválido ou de pauta não ativa")
        void dadoPossuoPautaIdInvalidoQuandoBuscoDetalhesPautaQuandoRetornarRespostaErro() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders
                                .get("/pauta/detalhes/{id}", 1)
                                .header("Authorization", "Bearer " + this.token)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.erro").value("Pauta não encontrada."));
        }
}
