package com.dbserver.votacaoBackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutenticacaoDto;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutenticacaoRespostaDto;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutorizarVotoExternoDto;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutorizarVotoExternoRespostaDto;
import com.dbserver.votacaoBackend.domain.autenticacao.service.AutenticacaoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
    private AutenticacaoService autenticacaoService;
    public AutenticacaoController(AutenticacaoService autenticacaoService){
        this.autenticacaoService = autenticacaoService;
    }
    
    @PostMapping("/login")
    public ResponseEntity<AutenticacaoRespostaDto> autenticarUsuario(@Valid @RequestBody AutenticacaoDto dto) {
        AutenticacaoRespostaDto resposta = this.autenticacaoService.autenticarUsuario(dto);
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }

    @PostMapping("/votoExterno")
    public ResponseEntity<AutorizarVotoExternoRespostaDto> autorizarUsuarioVotoExterno(@RequestBody AutorizarVotoExternoDto dto) {
        AutorizarVotoExternoRespostaDto resposta = this.autenticacaoService.autorizarUsuarioVotoExterno(dto);
        return ResponseEntity.ok(resposta);
    }
    
}
