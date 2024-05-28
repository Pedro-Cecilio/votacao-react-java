package com.dbserver.votacaoBackend.domain.sessaoVotacao.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.time.LocalDateTime;
import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.RespostaSessaoVotacaoDto;

@Mapper(componentModel = "spring")
public interface SessaoVotacaoMapper {
    public static final SessaoVotacaoMapper INSTANCE = Mappers.getMapper(SessaoVotacaoMapper.class);

    @Mapping(target = "votosPositivos", ignore = true)
    @Mapping(target = "votosNegativos", ignore = true)
    @Mapping(target = "pauta", source = "pauta")
    @Mapping(target = "ativa", ignore = true)
    SessaoVotacao toSessaoVotacao(Pauta pauta, LocalDateTime dataAbertura, LocalDateTime dataFechamento);

    @Mapping(target = "pautaId", expression = "java(sessaoVotacao.getPauta().getId())")
    @Mapping(target = "votosPositivos", expression = "java(sessaoVotacao.getVotosPositivos().size())")
    @Mapping(target = "votosNegativos", expression = "java(sessaoVotacao.getVotosNegativos().size())")
    @Mapping(target = "sessaoAtiva", expression = "java(sessaoVotacao.isAtiva())")
    RespostaSessaoVotacaoDto toRespostaSessaoVotacaoDto(SessaoVotacao sessaoVotacao);
}
