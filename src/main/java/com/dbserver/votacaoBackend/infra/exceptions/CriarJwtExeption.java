package com.dbserver.votacaoBackend.infra.exceptions;

public class CriarJwtExeption extends RuntimeException {
    public CriarJwtExeption() {
       super();
   }

   public CriarJwtExeption(String mensagem) {
       super(mensagem);
   }

   public CriarJwtExeption(String mensagem, Throwable causa) {
       super(mensagem, causa);
   }
}