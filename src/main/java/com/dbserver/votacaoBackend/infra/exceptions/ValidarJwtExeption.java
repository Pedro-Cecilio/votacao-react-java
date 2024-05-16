package com.dbserver.votacaoBackend.infra.exceptions;

public class ValidarJwtExeption extends RuntimeException{
    public ValidarJwtExeption() {
        super();
    }

    public ValidarJwtExeption(String mensagem) {
        super(mensagem);
    }

    public ValidarJwtExeption(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
