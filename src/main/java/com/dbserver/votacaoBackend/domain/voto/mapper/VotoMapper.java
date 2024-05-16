package com.dbserver.votacaoBackend.domain.voto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.voto.Voto;

@Mapper(componentModel = "spring")
public interface VotoMapper {

    @Mapping(target = "cpf", source = "cpf")
    @Mapping(target = "usuario", source = "usuario")
    Voto toVoto(String cpf, Usuario usuario);
}
