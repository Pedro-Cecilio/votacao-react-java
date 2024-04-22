package com.dbserver.votacaoBackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutenticacaoDto;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutenticacaoRespostaDto;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.ValidarVotoExternoDto;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.ValidarVotoExternoRespostaDto;
import com.dbserver.votacaoBackend.domain.autenticacao.service.IAutenticacaoService;
import com.dbserver.votacaoBackend.infra.security.token.TokenService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
    private IAutenticacaoService autenticacaoService;
    private TokenService tokenService;
    public AutenticacaoController(IAutenticacaoService autenticacaoService, TokenService tokenService){
        this.autenticacaoService = autenticacaoService;
        this.tokenService = tokenService;
    }
    
    @PostMapping("/login")
    public ResponseEntity<AutenticacaoRespostaDto> autenticarUsuario(@RequestBody @Valid AutenticacaoDto dto) {
        Autenticacao autenticacao = this.autenticacaoService.buscarAutenticacaoPorEmailESenha(dto.email(), dto.senha());
        String token = this.tokenService.gerarToken(autenticacao);
        AutenticacaoRespostaDto resposta = new AutenticacaoRespostaDto(token, autenticacao.getUsuario().isAdmin());
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }

    @PostMapping("/votoExterno")
    public ResponseEntity<ValidarVotoExternoRespostaDto> validarUsuarioVotoExterno(@RequestBody ValidarVotoExternoDto dto) {
        boolean valido = this.autenticacaoService.validarAutenticacaoPorCpfESenha(dto.cpf(), dto.senha());
        ValidarVotoExternoRespostaDto resposta = new ValidarVotoExternoRespostaDto(valido);
        return ResponseEntity.ok(resposta);
    }
    
}
