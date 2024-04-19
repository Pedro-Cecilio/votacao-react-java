package com.dbserver.votacaoBackend.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.service.IPautaService;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.AbrirVotacaoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.RespostaSessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.service.ISessaoVotacaoService;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.service.IUsuarioService;

@RestController
@RequestMapping(value = "/votacao")
public class SessaoVotacaoController {
    private IPautaService pautaService;
    private IUsuarioService usuarioService;
    private ISessaoVotacaoService sessaoVotacaoService;

    public SessaoVotacaoController(IPautaService pautaService, IUsuarioService usuarioService, ISessaoVotacaoService sessaoVotacaoService) {
        this.pautaService = pautaService;
        this.usuarioService = usuarioService;
        this.sessaoVotacaoService = sessaoVotacaoService;
    }

    @PostMapping
    public ResponseEntity<RespostaSessaoVotacao> abrirSessaoVotacao(@RequestBody AbrirVotacaoDto dto){
        Usuario usuario = this.usuarioService.buscarUsuarioLogado();
        Pauta pauta = this.pautaService.buscarPautaPorIdEUsuarioId(dto.pauta_id(), usuario.getId());
        LocalDateTime dataAbertura = LocalDateTime.now();
        LocalDateTime dataFechamento = dataAbertura.plusMinutes(dto.minutos());
        SessaoVotacao sessaoVotacao = new SessaoVotacao(pauta, dataAbertura, dataFechamento);
        this.sessaoVotacaoService.abrirVotacao(sessaoVotacao);
        RespostaSessaoVotacao resposta = new RespostaSessaoVotacao(sessaoVotacao);
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }
}
