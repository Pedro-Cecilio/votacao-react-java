package com.dbserver.votacaoBackend.domain.usuario.validacoes;

import org.springframework.stereotype.Component;

import com.dbserver.votacaoBackend.domain.usuario.Usuario;

@Component
public class UsuarioValidacoes {
    
    public void validarUsuarioNaoNulo(Usuario usuario){
        if(usuario == null) throw new IllegalArgumentException("Usuario não deve ser nulo.");
    }
}
