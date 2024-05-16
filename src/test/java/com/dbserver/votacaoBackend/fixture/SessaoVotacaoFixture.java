package com.dbserver.votacaoBackend.fixture;

import java.time.LocalDateTime;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.AbrirVotacaoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoExternoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.InserirVotoInternoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.TipoDeVotoEnum;


public class SessaoVotacaoFixture {
    public static SessaoVotacao sessaoVotacaoAtiva(Pauta pauta) {
        return new SessaoVotacao(pauta, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5));
    }
    public static SessaoVotacao sessaoVotacaoInativa(Pauta pauta) {
        return new SessaoVotacao(pauta, LocalDateTime.now(),
                LocalDateTime.now());
    }

    public static AbrirVotacaoDto abrirVotacaoDtoValido(Long pautaId) {
        return new AbrirVotacaoDto(10L, pautaId);
    }

    public static InserirVotoInternoDto inserirVotoInternoPositivoDto(Long pautaId) {
        return new InserirVotoInternoDto(pautaId, TipoDeVotoEnum.VOTO_POSITIVO);
    }

    public static InserirVotoExternoDto inserirVotoExternoNegativoDto(Long pautaId) {
        return new InserirVotoExternoDto(pautaId, TipoDeVotoEnum.VOTO_NEGATIVO, UsuarioFixture.CPF_ALEATORIO,
                null);
    }

    public static AbrirVotacaoDto abrirVotacaoDto() {
        return new AbrirVotacaoDto(5L, 1L);
    }

    public static InserirVotoInternoDto inserirVotoInternoDto() {
        return new InserirVotoInternoDto(1L, TipoDeVotoEnum.VOTO_POSITIVO);
    }

    public static InserirVotoInternoDto inserirVotoInternoDtoVotoNull() {
        return new InserirVotoInternoDto(1L, null);
    }

    public static InserirVotoInternoDto inserirVotoInternoDtoVotoNegativo() {
        return new InserirVotoInternoDto(1L, TipoDeVotoEnum.VOTO_NEGATIVO);
    }

    public static InserirVotoExternoDto inserirVotoExternoDtoUsuarioExistenteValido(){
        return new InserirVotoExternoDto(1L, TipoDeVotoEnum.VOTO_POSITIVO,
                                UsuarioFixture.CPF_ALEATORIO, "senhaValida");
    }
}
