package com.dbserver.votacaoBackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.service.AutenticacaoService;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.dto.CriarUsuarioDto;
import com.dbserver.votacaoBackend.domain.usuario.dto.CriarUsuarioRespostaDto;
import com.dbserver.votacaoBackend.domain.usuario.dto.UsuarioRespostaDto;
import com.dbserver.votacaoBackend.domain.usuario.dto.VerificarSeUsuarioExisteRespostaDto;
import com.dbserver.votacaoBackend.domain.usuario.service.IUsuarioService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {
    private IUsuarioService usuarioService;
    private AutenticacaoService autenticacaoService;

    public UsuarioController(IUsuarioService usuarioService, AutenticacaoService autenticacaoService) {
        this.usuarioService = usuarioService;
        this.autenticacaoService = autenticacaoService;
    }

    @SecurityRequirement(name = "bearer-key")
    @PostMapping
    public ResponseEntity<CriarUsuarioRespostaDto> criarUsuario(@RequestBody @Valid CriarUsuarioDto dto) {
        Usuario usuario = new Usuario(dto.nome(), dto.sobrenome(), dto.cpf(), dto.admin());
        String senhaEncriptada = this.autenticacaoService.encriptarSenhaDaAutenticacao(dto.autenticacaoDto().senha());
        Autenticacao autenticacao = new Autenticacao(dto.autenticacaoDto().email(), senhaEncriptada);
        Usuario novoUsuario = usuarioService.criarUsuario(usuario, autenticacao);
        CriarUsuarioRespostaDto resposta = new CriarUsuarioRespostaDto(novoUsuario, autenticacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/usuarioLogado")
    public ResponseEntity<UsuarioRespostaDto> buscarUsuarioLogado() {
        Usuario usuario = this.usuarioService.buscarUsuarioLogado();
        UsuarioRespostaDto resposta = new UsuarioRespostaDto(usuario);
        return ResponseEntity.ok().body(resposta);
    }

    @GetMapping("/existe")
    public ResponseEntity<VerificarSeUsuarioExisteRespostaDto> verificarSeUsuarioExistePorCpf(@RequestParam(name = "cpf", required = false, defaultValue = "") final String cpf) {
        boolean existe = this.usuarioService.verificarSeExisteUsuarioPorCpf(cpf);
        VerificarSeUsuarioExisteRespostaDto resposta = new VerificarSeUsuarioExisteRespostaDto(existe);
        return ResponseEntity.ok().body(resposta);
    }

}
