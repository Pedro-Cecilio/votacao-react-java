package com.dbserver.votacaoBackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.dto.CriarPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.dto.RespostaPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.service.IPautaService;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.dto.UsuarioRespostaDto;
import com.dbserver.votacaoBackend.domain.usuario.service.IUsuarioService;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping(value = "/pauta")
public class PautaController {
    private IPautaService pautaService;
    private IUsuarioService usuarioService;

    public PautaController(IPautaService pautaService, IUsuarioService usuarioService) {
        this.pautaService = pautaService;
        this.usuarioService = usuarioService;
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

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<RespostaPautaDto>> listarTodasPautasDoUsuario(Pageable pageable,
            @PathVariable("usuarioId") String usuarioId) {
        try {
            Long usuarioIdLong = Long.parseLong(usuarioId);
            List<Pauta> pautas = this.pautaService.buscarPautasPorUsuarioId(usuarioIdLong, pageable);
            List<RespostaPautaDto> resposta = pautas.stream().map(RespostaPautaDto::new).toList();
            return ResponseEntity.ok().body(resposta);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
    }

    @GetMapping()
    public ResponseEntity<List<RespostaPautaDto>> listarTodasPautas(Pageable pageable) {
        List<Pauta> pautas = this.pautaService.buscarTodasPautas(pageable);
        List<RespostaPautaDto> resposta = pautas.stream().map(RespostaPautaDto::new).toList();
        return ResponseEntity.ok().body(resposta);
    }

}
