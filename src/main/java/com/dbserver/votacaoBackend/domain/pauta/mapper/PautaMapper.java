package com.dbserver.votacaoBackend.domain.pauta.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.dto.CriarPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.dto.DetalhesPautaDto;
import com.dbserver.votacaoBackend.domain.pauta.dto.RespostaPautaDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.RespostaSessaoVotacaoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.enums.StatusSessaoVotacao;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.dto.UsuarioRespostaDto;

import java.time.LocalDateTime;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PautaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sessaoVotacao", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Pauta toPauta(CriarPautaDto dto, Usuario usuario);


    List<RespostaPautaDto> toListRespostaPautaDto(List<Pauta> pautas);

    RespostaPautaDto toRespostaPautaDto(Pauta pauta);

    @Mapping(target = "pautaId", expression = "java(sessaoVotacao.getPauta().getId())")
    @Mapping(target = "votosPositivos", expression = "java(sessaoVotacao.getVotosPositivos().size())")
    @Mapping(target = "votosNegativos", expression = "java(sessaoVotacao.getVotosNegativos().size())")
    @Mapping(target = "sessaoAtiva", expression = "java(sessaoVotacao.isAtiva())")
    RespostaSessaoVotacaoDto toRespostaSessaoVotacaoDto(SessaoVotacao sessaoVotacao);

    @Mapping(source = "pauta", target = "dadosPauta")
    @Mapping(target = "status", expression = "java(obterStatusSessaoVotacao(pauta.getSessaoVotacao()))")
    DetalhesPautaDto toDetalhesPautaDto(Pauta pauta);

    UsuarioRespostaDto toUsuarioRespostaDto(Usuario usuario);
    
    default StatusSessaoVotacao obterStatusSessaoVotacao(SessaoVotacao sessaoVotacao) {
        if (sessaoVotacao == null)
            throw new IllegalArgumentException("SessaoVotacao não deve ser nula.");

        if (sessaoVotacao.getDataFechamento().isAfter(LocalDateTime.now()))
            return StatusSessaoVotacao.EM_ANDAMENTO;

        if (sessaoVotacao.getVotosPositivos().size() > sessaoVotacao.getVotosNegativos().size())
            return StatusSessaoVotacao.APROVADA;

        return StatusSessaoVotacao.REPROVADA;
    }
}
