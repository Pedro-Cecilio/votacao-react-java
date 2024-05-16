package com.dbserver.votacaoBackend.fixture;

import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.voto.Voto;

public class VotoFixture {
    public static Voto gerarVotoInterno(Usuario usuario){
        return new Voto(usuario.getCpf(), usuario);
    }
    public static Voto gerarVotoExterno(String cpf, Usuario usuario){
        return new Voto(cpf, usuario);
    }
}
