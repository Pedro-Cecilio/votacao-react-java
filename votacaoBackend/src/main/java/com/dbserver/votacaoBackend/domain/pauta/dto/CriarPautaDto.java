package com.dbserver.votacaoBackend.domain.pauta.dto;

import jakarta.validation.constraints.NotEmpty;

public record CriarPautaDto(
        @NotEmpty(message = "Assunto deve ser informado.")
        String assunto,

        @NotEmpty(message = "Categoria deve ser informada.")
        String categoria
        
    ) {

}
