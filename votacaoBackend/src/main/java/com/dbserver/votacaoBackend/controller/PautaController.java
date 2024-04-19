package com.dbserver.votacaoBackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.dto.CriarPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.dto.RespostaPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;
import com.dbserver.votacaoBackend.domain.pauta.service.IPautaService;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.dto.UsuarioRespostaDto;
import com.dbserver.votacaoBackend.domain.usuario.service.IUsuarioService;
import com.dbserver.votacaoBackend.utils.Utils;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping(value = "/pauta")
public class PautaController {
    private IPautaService pautaService;
    private IUsuarioService usuarioService;
    private Utils utils;

    public PautaController(IPautaService pautaService, IUsuarioService usuarioService, Utils utils) {
        this.pautaService = pautaService;
        this.usuarioService = usuarioService;
        this.utils = utils;
    }

    @PostMapping
    public ResponseEntity<RespostaPautaDto> criarPauta(@RequestBody CriarPautaDto dto) {
        Usuario usuario = usuarioService.buscarUsuarioLogado();
        Pauta pauta = new Pauta(dto.assunto(), dto.categoria(), usuario);
        this.pautaService.criarPauta(pauta);
        UsuarioRespostaDto usuarioRespostaDto = new UsuarioRespostaDto(usuario);
        RespostaPautaDto resposta = new RespostaPautaDto(pauta.getId(), pauta.getAssunto(), pauta.getCategoria(),
                usuarioRespostaDto, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping("/usuarioLogado")
    public ResponseEntity<List<RespostaPautaDto>> listarTodasPautasDoUsuario(
            @RequestParam(name = "categoria", required = false) final Categoria categoria,
            Pageable pageable) {
        Usuario usuario = usuarioService.buscarUsuarioLogado();
        List<Pauta> pautas = this.pautaService.buscarPautasPorUsuarioId(usuario.getId(), categoria, pageable);
        List<RespostaPautaDto> resposta = utils.criarListaRespostaPautaDto(pautas);
        return ResponseEntity.ok().body(resposta);
    }

    @GetMapping("/ativas")
    public ResponseEntity<List<RespostaPautaDto>> listarPautas(
            @RequestParam(name = "categoria", required = false) final Categoria categoria,
            Pageable pageable) {
        List<Pauta> pautas = this.pautaService.buscarPautasAtivas(pageable, categoria);
        List<RespostaPautaDto> resposta = utils.criarListaRespostaPautaDto(pautas);
        return ResponseEntity.ok().body(resposta);
    }

}
