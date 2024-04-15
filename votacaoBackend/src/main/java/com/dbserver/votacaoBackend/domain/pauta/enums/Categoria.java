package com.dbserver.votacaoBackend.domain.pauta.enums;

public enum Categoria {
    TRANSPORTE("Transporte"),
    EDUCACAO("Educação"),
    SAUDE("Saúde"),
    MORADIA("Moradia"),
    MEIO_AMBIENTE("Meio Ambiente"),
    CULTURA_LAZER("Cultura e Lazer"),
    SEGURANCA("Segurança"),
    EMPREGO("Emprego"),
    SERVICOS_PUBLICOS("Serviços Públicos"),
    ASSUNTOS_GERAIS("Assuntos gerais");

    private final String descricao;

    Categoria(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
