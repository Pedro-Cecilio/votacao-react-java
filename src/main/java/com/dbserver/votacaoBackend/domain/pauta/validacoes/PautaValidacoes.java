package com.dbserver.votacaoBackend.domain.pauta.validacoes;

import org.springframework.stereotype.Component;

import com.dbserver.votacaoBackend.domain.pauta.dto.CriarPautaDto;

@Component
public class PautaValidacoes{
    
    public void validarCriarPautaDtoNaoNula(CriarPautaDto dto){
        if(dto == null) throw new IllegalArgumentException("Criar Pauta Dto não deve ser nulo.");
    }
}
