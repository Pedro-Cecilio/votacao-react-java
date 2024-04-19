package com.dbserver.votacaoBackend.domain.pauta.dto;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.RespostaSessaoVotacaoDto;
import com.dbserver.votacaoBackend.domain.usuario.dto.UsuarioRespostaDto;

public record RespostaPautaDto(
    Long id, 
    String assunto,
    Categoria categoria,
    UsuarioRespostaDto usuario,
    RespostaSessaoVotacaoDto sessaoVotacao
) {
    public RespostaPautaDto(Pauta pauta, RespostaSessaoVotacaoDto respostaSessaoVotacao){
        this(pauta.getId(), pauta.getAssunto(), pauta.getCategoria(), new UsuarioRespostaDto(pauta.getUsuario()), respostaSessaoVotacao);
    }
}
