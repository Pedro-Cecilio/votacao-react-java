package com.dbserver.votacaoBackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.service.PautaService;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.AbrirVotacaoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoExternoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoInternoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.RespostaSessaoVotacaoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.service.SessaoVotacaoService;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.service.UsuarioService;
import com.dbserver.votacaoBackend.domain.voto.Voto;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/votacao")
public class SessaoVotacaoController {
    private PautaService pautaService;
    private UsuarioService usuarioService;
    private SessaoVotacaoService sessaoVotacaoService;

    public SessaoVotacaoController(PautaService pautaService, UsuarioService usuarioService,
            SessaoVotacaoService sessaoVotacaoService) {
        this.pautaService = pautaService;
        this.usuarioService = usuarioService;
        this.sessaoVotacaoService = sessaoVotacaoService;
    }

    @SecurityRequirement(name = "bearer-key")
    @PostMapping("/abrir")
    public ResponseEntity<RespostaSessaoVotacaoDto> abrirSessaoVotacao(@Valid @RequestBody AbrirVotacaoDto dto) {
        Usuario usuario = this.usuarioService.buscarUsuarioLogado();

        Pauta pauta = this.pautaService.buscarPautaPorIdEUsuarioId(dto.pautaId(), usuario.getId());

        SessaoVotacao sessaoVotacao = this.sessaoVotacaoService.abrirVotacao(pauta, dto.minutos());

        RespostaSessaoVotacaoDto resposta = new RespostaSessaoVotacaoDto(sessaoVotacao);

        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }

    @SecurityRequirement(name = "bearer-key")
    @PatchMapping("/votoInterno")
    public ResponseEntity<RespostaSessaoVotacaoDto> votoInterno(@Valid @RequestBody InserirVotoInternoDto dto) {
        Usuario usuario = this.usuarioService.buscarUsuarioLogado();

        Voto voto = new Voto(usuario.getCpf(), usuario);

        SessaoVotacao sessaoVotacao = this.sessaoVotacaoService.inserirVotoInterno(voto, dto.pautaId(), dto.tipoDeVoto());

        RespostaSessaoVotacaoDto resposta = new RespostaSessaoVotacaoDto(sessaoVotacao);

        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }

    @PatchMapping("/votoExterno")
    public ResponseEntity<RespostaSessaoVotacaoDto> votoExterno(@Valid @RequestBody InserirVotoExternoDto dto) {

        Usuario usuario = this.usuarioService.buscarUsuarioPorCpfSeHouver(dto.cpf());

        Voto voto = new Voto(dto.cpf(), usuario);

        SessaoVotacao sessaoVotacao = this.sessaoVotacaoService.inserirVotoExterno(voto, dto.pautaId(), dto.tipoDeVoto(), dto.cpf(), dto.senha());

        RespostaSessaoVotacaoDto resposta = new RespostaSessaoVotacaoDto(sessaoVotacao);

        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }
}
