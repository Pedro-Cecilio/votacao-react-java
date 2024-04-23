package com.dbserver.votacaoBackend.infra.exeptions;

public class RespostaErro{
    private String erro;

    public RespostaErro() {
    }
    public RespostaErro(String erro){
        this.erro = erro;
    }
    public String getErro(){
        return this.erro;
    }
}