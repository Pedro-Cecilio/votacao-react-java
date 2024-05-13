package com.dbserver.votacaoBackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.AbrirVotacaoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoExternoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoInternoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.RespostaSessaoVotacaoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.service.SessaoVotacaoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/votacao")
public class SessaoVotacaoController {
    private SessaoVotacaoService sessaoVotacaoService;

    public SessaoVotacaoController(
            SessaoVotacaoService sessaoVotacaoService) {
        this.sessaoVotacaoService = sessaoVotacaoService;
    }

    @SecurityRequirement(name = "bearer-key")
    @PostMapping("/abrir")
    public ResponseEntity<RespostaSessaoVotacaoDto> abrirSessaoVotacao(@Valid @RequestBody AbrirVotacaoDto dto) {
        RespostaSessaoVotacaoDto resposta = this.sessaoVotacaoService.abrirVotacao(dto);

        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }

    @SecurityRequirement(name = "bearer-key")
    @PatchMapping("/votoInterno")
    public ResponseEntity<RespostaSessaoVotacaoDto> votoInterno(@Valid @RequestBody InserirVotoInternoDto dto) {
        RespostaSessaoVotacaoDto resposta = this.sessaoVotacaoService.inserirVotoInterno(dto);

        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }

    @PatchMapping("/votoExterno")
    public ResponseEntity<RespostaSessaoVotacaoDto> votoExterno(@Valid @RequestBody InserirVotoExternoDto dto) {
        RespostaSessaoVotacaoDto resposta = this.sessaoVotacaoService.inserirVotoExterno(dto);

        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }
}
