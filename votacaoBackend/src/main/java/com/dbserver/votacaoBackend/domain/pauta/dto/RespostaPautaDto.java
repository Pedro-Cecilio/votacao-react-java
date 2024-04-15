package com.dbserver.votacaoBackend.domain.pauta.dto;

import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;

public record RespostaPautaDto(
    Long id, 
    String assunto,
    Categoria categoria,
    Long usuario_id

) {
    
}
