package com.dbserver.votacaoBackend.fixture.pauta;

import java.util.ArrayList;
import java.util.List;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.dto.CriarPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.fixture.sessaoVotacao.SessaoVotacaoFixture;

public class PautaFixture {

    public static final Categoria CATEGORIA_TRANSPORTE = Categoria.TRANSPORTE;
    public static final String ASSUNTO_TRANSPORTE = "Sabe dirigir?";

    public static Pauta pautaTransporte(Usuario usuario) {
        return Pauta.builder()
                .assunto(ASSUNTO_TRANSPORTE)
                .categoria(CATEGORIA_TRANSPORTE)
                .usuario(usuario)
                .build();
    }

    public static Pauta pautaTransporteAtiva(Usuario usuario) {
        Pauta pauta = pautaTransporte(usuario);

        pauta.setSessaoVotacao(SessaoVotacaoFixture.sessaoVotacaoAtiva(pauta));
        return pauta;
    }

    public static CriarPautaDto criarPautaDtoValido() {
        return new CriarPautaDto(ASSUNTO_TRANSPORTE, CATEGORIA_TRANSPORTE);
    }

    public static Pauta pautaSaude(Usuario usuario) {
        return Pauta.builder()
                .assunto("Você está bem de saúde?")
                .categoria(Categoria.SAUDE)
                .usuario(usuario)
                .build();
    }

    public static List<Pauta> listaDePautas(Usuario usuario) {
        List<Pauta> lista = new ArrayList<>();
        lista.add(pautaSaude(usuario));
        lista.add(pautaTransporte(usuario));
        return lista;
    }

    public static List<Pauta> listaDePautasUmaPautaAtiva(Usuario usuario) {
        List<Pauta> lista = new ArrayList<>();
        lista.add(pautaSaude(usuario));
        lista.add(pautaTransporteAtiva(usuario));
        return lista;
    }
}
