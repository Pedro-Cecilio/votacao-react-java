package com.dbserver.votacaoBackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.dto.CriarPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.dto.DetalhesPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.dto.RespostaPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;
import com.dbserver.votacaoBackend.domain.pauta.service.PautaService;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.StatusSessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.service.SessaoVotacaoService;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.service.UsuarioService;
import com.dbserver.votacaoBackend.utils.Utils;

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
    private UsuarioService usuarioService;
    private SessaoVotacaoService sessaoVotacaoService;
    private Utils utils;

    public PautaController(PautaService pautaService, UsuarioService usuarioService, Utils utils,
            SessaoVotacaoService sessaoVotacaoService) {
        this.pautaService = pautaService;
        this.usuarioService = usuarioService;
        this.utils = utils;
        this.sessaoVotacaoService = sessaoVotacaoService;
    }

    @SecurityRequirement(name = "bearer-key")
    @PostMapping
    public ResponseEntity<RespostaPautaDto> criarPauta(@Valid @RequestBody CriarPautaDto dto) {
        Usuario usuario = usuarioService.buscarUsuarioLogado();

        Pauta pauta = new Pauta(dto.assunto(), dto.categoria(), usuario);

        this.pautaService.criarPauta(pauta);

        RespostaPautaDto resposta = new RespostaPautaDto(pauta, null);

        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/usuarioLogado")
    public ResponseEntity<List<RespostaPautaDto>> listarTodasPautasDoUsuario(
            @RequestParam(name = "categoria", required = false) final Categoria categoria) {
        Usuario usuario = usuarioService.buscarUsuarioLogado();

        List<Pauta> pautas = this.pautaService.buscarPautasPorUsuarioId(usuario.getId(), categoria);

        List<RespostaPautaDto> resposta = utils.criarListaRespostaPautaDto(pautas);

        return ResponseEntity.ok().body(resposta);
    }

    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/ativas")
    public ResponseEntity<List<RespostaPautaDto>> listarPautas(
            @RequestParam(name = "categoria", required = false) final Categoria categoria) {
        
        List<Pauta> pautas = this.pautaService.buscarPautasAtivas(categoria);

        List<RespostaPautaDto> resposta = utils.criarListaRespostaPautaDto(pautas);

        return ResponseEntity.ok().body(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespostaPautaDto> buscarPautaAtivaPorId(@PathVariable("id") Long id) {
        Pauta pauta = this.pautaService.buscarPautaAtivaPorId(id);

        RespostaPautaDto resposta = new RespostaPautaDto(pauta);

        return ResponseEntity.ok().body(resposta);
    }

    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/detalhes/{id}")
    public ResponseEntity<DetalhesPautaDto> buscarDetalhesPautaSessaoVotacaoNaoNula(@PathVariable("id") Long id) {
        Usuario usuario = usuarioService.buscarUsuarioLogado();

        Pauta pauta = this.pautaService.buscarPautaPorIdEUsuarioIdComSessaoVotacaoNaoNula(id, usuario.getId());

        RespostaPautaDto respostaPautaDto = new RespostaPautaDto(pauta);

        StatusSessaoVotacao status = this.sessaoVotacaoService.obterStatusSessaoVotacao(pauta.getSessaoVotacao());

        DetalhesPautaDto resposta = new DetalhesPautaDto(respostaPautaDto, status);

        return ResponseEntity.ok().body(resposta);

    }

}
