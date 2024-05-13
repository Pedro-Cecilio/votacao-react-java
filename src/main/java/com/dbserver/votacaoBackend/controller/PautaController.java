package com.dbserver.votacaoBackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dbserver.votacaoBackend.domain.pauta.dto.CriarPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.dto.DetalhesPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.dto.RespostaPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;
import com.dbserver.votacaoBackend.domain.pauta.service.PautaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping(value = "/pauta")
public class PautaController {
    private PautaService pautaService;

    public PautaController(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    @SecurityRequirement(name = "bearer-key")
    @PostMapping
    public ResponseEntity<RespostaPautaDto> criarPauta(@Valid @RequestBody CriarPautaDto dto) {
        RespostaPautaDto resposta = this.pautaService.criarPauta(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/usuarioLogado")
    public ResponseEntity<List<RespostaPautaDto>> listarTodasPautasDoUsuario(
            @RequestParam(name = "categoria", required = false) final Categoria categoria) {
        List<RespostaPautaDto> resposta = this.pautaService.buscarPautasUsuarioLogado(categoria);

        return ResponseEntity.ok().body(resposta);
    }

    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/ativas")
    public ResponseEntity<List<RespostaPautaDto>> listarPautas(
            @RequestParam(name = "categoria", required = false) final Categoria categoria) {
        List<RespostaPautaDto> resposta = this.pautaService.buscarPautasAtivas(categoria);
        return ResponseEntity.ok().body(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespostaPautaDto> buscarPautaAtivaPorId(@PathVariable("id") Long id) {
        RespostaPautaDto resposta = this.pautaService.buscarPautaAtivaPorId(id);

        return ResponseEntity.ok().body(resposta);
    }

    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/detalhes/{id}")
    public ResponseEntity<DetalhesPautaDto> buscarDetalhesPautaSessaoVotacaoNaoNula(@PathVariable("id") Long id) {
        DetalhesPautaDto resposta = this.pautaService.obterDetalhePautaSessaoVotacaoNaoNula(id);

        return ResponseEntity.ok().body(resposta);

    }

}
