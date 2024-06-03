package com.dbserver.votacaoBackend.domain.voto.validacoes;

import org.springframework.stereotype.Component;

import com.dbserver.votacaoBackend.domain.voto.Voto;

@Component
public class VotoValidacoes {
    
    public static void validarVotoNaoNulo(Voto voto){
        if(voto == null) throw new IllegalArgumentException("Voto não deve ser nulo.");
    }
}
