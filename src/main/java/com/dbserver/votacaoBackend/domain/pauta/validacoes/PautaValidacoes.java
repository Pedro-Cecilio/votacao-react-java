package com.dbserver.votacaoBackend.domain.pauta.validacoes;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.dto.CriarPautaDto;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.validacoes.UsuarioValidacoes;

@Component
public class PautaValidacoes {

    public void validarCriarPautaDtoNaoNula(CriarPautaDto dto) {
        if (dto == null)
            throw new IllegalArgumentException("Criar Pauta Dto não deve ser nulo.");
    }

    public static void validarPautaNaoNula(Pauta pauta) {
        if (pauta == null)
            throw new IllegalArgumentException("Pauta não deve ser nula");
    }


    public static void validarUsuarioDaPauta(Usuario usuario) {
        UsuarioValidacoes.validarUsuarioNaoNulo(usuario);

        if (!usuario.isAdmin())
            throw new IllegalArgumentException("Usuario deve ser admin.");
    }

    public static void validarAssunto(String assunto) {
        if (assunto == null || assunto.trim().isEmpty())
            throw new IllegalArgumentException("Assunto deve ser informado.");
    }
}
