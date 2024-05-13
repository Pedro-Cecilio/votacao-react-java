package com.dbserver.votacaoBackend.domain.sessaoVotacao.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.RespostaSessaoVotacaoDto;

@Mapper(componentModel = "spring")
public interface SessaoVotacaoMapper {

    @Mapping(target = "pautaId", expression = "java(sessaoVotacao.getPauta().getId())")
    @Mapping(target = "votosPositivos", expression = "java(sessaoVotacao.getVotosPositivos().size())")
    @Mapping(target = "votosNegativos", expression = "java(sessaoVotacao.getVotosNegativos().size())")
    @Mapping(target = "sessaoAtiva", expression = "java(sessaoVotacao.isAtiva())")
    RespostaSessaoVotacaoDto toRespostaSessaoVotacaoDto(SessaoVotacao sessaoVotacao);
}
