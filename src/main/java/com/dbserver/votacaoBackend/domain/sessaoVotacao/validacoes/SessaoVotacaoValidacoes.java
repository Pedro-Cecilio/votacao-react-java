package com.dbserver.votacaoBackend.domain.sessaoVotacao.validacoes;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;

@Component
public class SessaoVotacaoValidacoes {

    public void validarSessaoVotacaoNaoNula(SessaoVotacao sessaoVotacao) {
        if (sessaoVotacao == null)
            throw new IllegalArgumentException("SessaoVotacao não deve ser nula.");
    }

    public void validarSessaoVotacaoAtiva(SessaoVotacao sessaoVotacao) {
        if (!sessaoVotacao.isAtiva())
            throw new IllegalStateException("Sessão de votação não está ativa.");
    }

     public static void validarDataDeAbertura(LocalDateTime dataAbertura) {
        LocalDateTime dataAtual = LocalDateTime.now().withNano(0);

        if (dataAbertura == null)
            throw new IllegalArgumentException("A data de abertura não deve ser nula.");

        if (dataAbertura.isBefore(dataAtual))
            throw new IllegalArgumentException("A data de abertura não deve ser menor que a data atual");
    }

    public static void validarDataDeFechamento(LocalDateTime dataFechamento, LocalDateTime dataAbertura) {
        if (dataFechamento == null)
            throw new IllegalArgumentException("A data de abertura não deve ser nula.");

        if (dataFechamento.isBefore(dataAbertura))
            throw new IllegalArgumentException("A data de fechamento deve ser maior que a data de abertura.");
    }
}
