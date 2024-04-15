package com.dbserver.votacaoBackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.dto.CriarPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.dto.RespostaPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.service.IPautaService;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.utils.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(value = "/pauta")
public class PautaController {
    private IPautaService pautaService;
    private Utils utils;

    public PautaController(IPautaService pautaService, Utils utils) {
        this.pautaService = pautaService;
        this.utils = utils;
    }

    @PostMapping
    public ResponseEntity<RespostaPautaDto> postMethodName(@RequestBody CriarPautaDto dto) {
        Usuario usuario = utils.pegarUsuarioLogado();
        Pauta pauta = new Pauta(dto.nome(), usuario);
        this.pautaService.criarPauta(pauta);
        RespostaPautaDto resposta = new RespostaPautaDto(pauta.getId(), pauta.getNome(), pauta.getUsuario().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

}
