package com.dbserver.votacaoBackend.domain.pauta.dto;

import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CriarPautaDto(
        @NotEmpty(message = "Assunto deve ser informado.")
        String assunto,

        @NotNull(message = "Categoria deve ser informada.")
        Categoria categoria
        
    ) {

}
