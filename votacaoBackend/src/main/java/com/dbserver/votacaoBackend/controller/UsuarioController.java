package com.dbserver.votacaoBackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.dto.CriarUsuarioDto;
import com.dbserver.votacaoBackend.domain.usuario.dto.CriarUsuarioRespostaDto;
import com.dbserver.votacaoBackend.domain.usuario.service.IUsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {
    private IUsuarioService usuarioService;
    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<CriarUsuarioRespostaDto> criarUsuario(@RequestBody CriarUsuarioDto dto) {
        Usuario usuario = new Usuario(dto);
        Autenticacao autenticacao = new Autenticacao(dto.criarAutenticacaoDto());
        Usuario novoUsuario = usuarioService.criarUsuario(usuario, autenticacao);
        CriarUsuarioRespostaDto resposta = new CriarUsuarioRespostaDto(novoUsuario, autenticacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }
    
}
