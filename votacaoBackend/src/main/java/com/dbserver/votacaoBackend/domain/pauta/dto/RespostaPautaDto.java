package com.dbserver.votacaoBackend.domain.pauta.dto;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.RespostaSessaoVotacao;
import com.dbserver.votacaoBackend.domain.usuario.dto.UsuarioRespostaDto;

public record RespostaPautaDto(
    Long id, 
    String assunto,
    Categoria categoria,
    UsuarioRespostaDto usuario,
    RespostaSessaoVotacao sessaoVotacao
) {
    public RespostaPautaDto(Pauta pauta, RespostaSessaoVotacao respostaSessaoVotacao){
        this(pauta.getId(), pauta.getAssunto(), pauta.getCategoria(), new UsuarioRespostaDto(pauta.getUsuario()), respostaSessaoVotacao);
    }
}
