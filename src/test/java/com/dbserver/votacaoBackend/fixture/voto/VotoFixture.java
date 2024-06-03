package com.dbserver.votacaoBackend.fixture.voto;

import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.voto.Voto;

public class VotoFixture {
    public static Voto gerarVotoInterno(Usuario usuario) {
        return Voto.builder()
                .cpf(usuario.getCpf())
                .usuario(usuario)
                .build();
    }

    public static Voto gerarVotoExterno(String cpf, Usuario usuario) {
        return Voto.builder()
                .cpf(cpf)
                .usuario(usuario)
                .build();
    }
}
