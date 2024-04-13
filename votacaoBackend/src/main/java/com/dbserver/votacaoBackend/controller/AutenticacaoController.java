package com.dbserver.votacaoBackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutenticacaoDto;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutenticacaoRespostaDto;
import com.dbserver.votacaoBackend.domain.autenticacao.service.IAutenticacaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
    private IAutenticacaoService autenticacaoService;
    public AutenticacaoController(IAutenticacaoService autenticacaoService){
        this.autenticacaoService = autenticacaoService;
    }
    
    @PostMapping("/login")
    public ResponseEntity<AutenticacaoRespostaDto> autenticarUsuario(@RequestBody @Valid AutenticacaoDto dto) {
        String token = this.autenticacaoService.autenticar(dto.email(), dto.senha());
        return ResponseEntity.status(HttpStatus.OK).body(new AutenticacaoRespostaDto(token));
    }
}
