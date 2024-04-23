package com.dbserver.votacaoBackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.dto.CriarPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.dto.DetalhesPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.dto.RespostaPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;
import com.dbserver.votacaoBackend.domain.pauta.service.IPautaService;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.RespostaSessaoVotacaoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.StatusSessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.service.ISessaoVotacaoService;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.dto.UsuarioRespostaDto;
import com.dbserver.votacaoBackend.domain.usuario.service.IUsuarioService;
import com.dbserver.votacaoBackend.utils.Utils;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;
import java.util.NoSuchElementException;
import java.time.LocalDateTime;
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
    private ISessaoVotacaoService sessaoVotacaoService;
    private Utils utils;

    public PautaController(IPautaService pautaService, IUsuarioService usuarioService, Utils utils, ISessaoVotacaoService sessaoVotacaoService) {
        this.pautaService = pautaService;
        this.usuarioService = usuarioService;
        this.utils = utils;
        this.sessaoVotacaoService = sessaoVotacaoService;
    }

    @SecurityRequirement(name = "bearer-key")
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

    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/usuarioLogado")
    public ResponseEntity<List<RespostaPautaDto>> listarTodasPautasDoUsuario(
            @RequestParam(name = "categoria", required = false) final Categoria categoria,
            Pageable pageable) {
        Usuario usuario = usuarioService.buscarUsuarioLogado();
        List<Pauta> pautas = this.pautaService.buscarPautasPorUsuarioId(usuario.getId(), categoria, pageable);
        List<RespostaPautaDto> resposta = utils.criarListaRespostaPautaDto(pautas);
        return ResponseEntity.ok().body(resposta);
    }

    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/ativas")
    public ResponseEntity<List<RespostaPautaDto>> listarPautas(
            @RequestParam(name = "categoria", required = false) final Categoria categoria,
            Pageable pageable) {
        List<Pauta> pautas = this.pautaService.buscarPautasAtivas(pageable, categoria);
        List<RespostaPautaDto> resposta = utils.criarListaRespostaPautaDto(pautas);
        return ResponseEntity.ok().body(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespostaPautaDto> buscarPautaAtivaPorId(@PathVariable("id") String id) {
        try {
            Long pautaId = Long.parseLong(id);
            Pauta pauta = this.pautaService.buscarPautaAtivaPorId(pautaId);
            UsuarioRespostaDto usuarioRespostaDto = new UsuarioRespostaDto(pauta.getUsuario());
            boolean sessaoAtiva = pauta.getSessaoVotacao().getDataFechamento().isAfter(LocalDateTime.now());
            RespostaSessaoVotacaoDto respostaSessaoDto = new RespostaSessaoVotacaoDto(pauta.getSessaoVotacao(), sessaoAtiva);
            RespostaPautaDto resposta = new RespostaPautaDto(pauta.getId(), pauta.getAssunto(), pauta.getCategoria(),
                usuarioRespostaDto, respostaSessaoDto);
            return ResponseEntity.ok().body(resposta);
        } catch (Exception e) {
            throw new NoSuchElementException("Pauta não encontrada.");
        }       
    }

    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/detalhes/{id}")
    public ResponseEntity<DetalhesPautaDto> buscarDetalhesPautaSessaoEncerrada(@PathVariable("id") String id) {
        try {
            Usuario usuario = usuarioService.buscarUsuarioLogado();
            Long pautaId = Long.parseLong(id);
            Pauta pauta = this.pautaService.buscarPautaPorIdEUsuarioIdComSessaoVotacaoNaoNula(pautaId, usuario.getId());
            boolean sessaoAtiva = pauta.getSessaoVotacao().getDataFechamento().isAfter(LocalDateTime.now());
            UsuarioRespostaDto usuarioRespostaDto = new UsuarioRespostaDto(usuario);
            RespostaSessaoVotacaoDto respostaSessaoDto = new RespostaSessaoVotacaoDto(pauta.getSessaoVotacao(), sessaoAtiva);
            RespostaPautaDto respostaPautaDto = new RespostaPautaDto(pauta.getId(), pauta.getAssunto(), pauta.getCategoria(), usuarioRespostaDto, respostaSessaoDto);
            StatusSessaoVotacao status = this.sessaoVotacaoService.obterStatusSessaoVotacao(pauta.getSessaoVotacao());
            DetalhesPautaDto resposta = new DetalhesPautaDto(respostaPautaDto, status);
            return ResponseEntity.ok().body(resposta);
        } catch (NumberFormatException e) {
            throw new NoSuchElementException("Pauta encerrada não encontrada.");
        }
    }

}
