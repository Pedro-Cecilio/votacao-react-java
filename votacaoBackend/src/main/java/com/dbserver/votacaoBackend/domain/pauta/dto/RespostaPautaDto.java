package com.dbserver.votacaoBackend.domain.pauta.dto;

import com.dbserver.votacaoBackend.domain.usuario.Usuario;

public record RespostaPautaDto(
    Long id, 
    String nome,
    Long usuario_id

) {
    
}
