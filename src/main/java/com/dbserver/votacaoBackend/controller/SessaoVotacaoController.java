// package com.dbserver.votacaoBackend.controller;

// import java.time.LocalDateTime;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PatchMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.dbserver.votacaoBackend.domain.pauta.Pauta;
// import com.dbserver.votacaoBackend.domain.pauta.service.IPautaService;
// import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
// import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.AbrirVotacaoDto;
// import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoExternoDto;
// import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoInternoDto;
// import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.RespostaSessaoVotacaoDto;
// import com.dbserver.votacaoBackend.domain.sessaoVotacao.service.ISessaoVotacaoService;
// import com.dbserver.votacaoBackend.domain.usuario.Usuario;
// import com.dbserver.votacaoBackend.domain.usuario.service.IUsuarioService;
// import com.dbserver.votacaoBackend.domain.voto.Voto;

// import io.swagger.v3.oas.annotations.security.SecurityRequirement;
// import jakarta.validation.Valid;

// @RestController
// @RequestMapping(value = "/votacao")
// public class SessaoVotacaoController {
//     private IPautaService pautaService;
//     private IUsuarioService usuarioService;
//     private ISessaoVotacaoService sessaoVotacaoService;

//     public SessaoVotacaoController(IPautaService pautaService, IUsuarioService usuarioService,
//             ISessaoVotacaoService sessaoVotacaoService) {
//         this.pautaService = pautaService;
//         this.usuarioService = usuarioService;
//         this.sessaoVotacaoService = sessaoVotacaoService;
//     }

//     @SecurityRequirement(name = "bearer-key")
//     @PostMapping("/abrir")
//     public ResponseEntity<RespostaSessaoVotacaoDto> abrirSessaoVotacao(@Valid @RequestBody AbrirVotacaoDto dto) {
//         Usuario usuario = this.usuarioService.buscarUsuarioLogado();
//         Pauta pauta = this.pautaService.buscarPautaPorIdEUsuarioId(dto.pautaId(), usuario.getId());
//         LocalDateTime dataAbertura = LocalDateTime.now();
//         LocalDateTime dataFechamento = dataAbertura.plusMinutes(dto.minutos());
//         SessaoVotacao sessaoVotacao = new SessaoVotacao(pauta, dataAbertura, dataFechamento);
//         this.sessaoVotacaoService.abrirVotacao(sessaoVotacao);
//         RespostaSessaoVotacaoDto resposta = new RespostaSessaoVotacaoDto(sessaoVotacao, true);
//         return ResponseEntity.status(HttpStatus.OK).body(resposta);
//     }

//     @SecurityRequirement(name = "bearer-key")
//     @PatchMapping("/votoInterno")
//     public ResponseEntity<RespostaSessaoVotacaoDto> votoInterno(@Valid @RequestBody InserirVotoInternoDto dto) {
//         Usuario usuario = this.usuarioService.buscarUsuarioLogado();
//         LocalDateTime dataAtual = LocalDateTime.now();
//         Pauta pauta = this.pautaService.buscarPautaAtivaPorId(dto.pautaId(), dataAtual);
//         SessaoVotacao sessaoVotacao = pauta.getSessaoVotacao();
//         Voto voto = new Voto(usuario.getCpf(), usuario);
//         this.sessaoVotacaoService.inserirVoto(sessaoVotacao, dto.tipoDeVoto(), voto);
//         boolean sessaoEstaAtiva = sessaoVotacao.getDataFechamento().isAfter(LocalDateTime.now());
//         RespostaSessaoVotacaoDto resposta = new RespostaSessaoVotacaoDto(sessaoVotacao, sessaoEstaAtiva);
//         return ResponseEntity.status(HttpStatus.OK).body(resposta);
//     }

//     @PatchMapping("/votoExterno")
//     public ResponseEntity<RespostaSessaoVotacaoDto> votoExterno(@Valid @RequestBody InserirVotoExternoDto dto) {
//         this.sessaoVotacaoService.verificarSePodeVotarExternamente(dto.cpf(), dto.senha());
//         LocalDateTime dataAtual = LocalDateTime.now();
//         Usuario usuario = this.usuarioService.buscarUsuarioPorCpfSeHouver(dto.cpf());
//         Pauta pauta = this.pautaService.buscarPautaAtivaPorId(dto.pautaId(), dataAtual);
//         SessaoVotacao sessaoVotacao = pauta.getSessaoVotacao();
//         Voto voto = new Voto(dto.cpf(), usuario);
//         this.sessaoVotacaoService.inserirVoto(sessaoVotacao, dto.tipoDeVoto(), voto);
//         boolean sessaoEstaAtiva = sessaoVotacao.getDataFechamento().isAfter(LocalDateTime.now());
//         RespostaSessaoVotacaoDto resposta = new RespostaSessaoVotacaoDto(sessaoVotacao, sessaoEstaAtiva);
//         return ResponseEntity.status(HttpStatus.OK).body(resposta);
//     }
// }
