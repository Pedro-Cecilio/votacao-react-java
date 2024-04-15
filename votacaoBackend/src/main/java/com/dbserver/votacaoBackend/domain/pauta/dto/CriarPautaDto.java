package com.dbserver.votacaoBackend.domain.pauta.dto;

import jakarta.validation.constraints.NotEmpty;

public record CriarPautaDto(
        @NotEmpty
        String assunto,

        @NotEmpty
        String categoria
        
    ) {

}
