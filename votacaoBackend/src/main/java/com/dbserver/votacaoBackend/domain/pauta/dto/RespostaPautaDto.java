package com.dbserver.votacaoBackend.domain.pauta.dto;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.domain.usuario.dto.UsuarioRespostaDto;

public record RespostaPautaDto(
    Long id, 
    String assunto,
    Categoria categoria,
    UsuarioRespostaDto usuario,
    SessaoVotacao sessaoVotacao
) {
    public RespostaPautaDto(Pauta pauta){
        this(pauta.getId(), pauta.getAssunto(), pauta.getCategoria(), new UsuarioRespostaDto(pauta.getUsuario()), pauta.getSessaoVotacao());
    }
}
