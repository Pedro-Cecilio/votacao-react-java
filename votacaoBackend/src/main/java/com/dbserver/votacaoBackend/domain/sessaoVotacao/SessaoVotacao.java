package com.dbserver.votacaoBackend.domain.sessaoVotacao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SessaoVotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = { CascadeType.REFRESH })
    @JoinColumn(name = "pauta_id")
    private Pauta pauta;

    @ManyToMany
    private List<Usuario> votosPositivos = new ArrayList<>();

    @ManyToMany
    private List<Usuario> votosNegativos = new ArrayList<>();

    @Column
    private LocalDateTime dataAbertura;

    @Column
    private LocalDateTime dataFechamento;

    public SessaoVotacao(Pauta pauta, LocalDateTime dataAbertura, LocalDateTime dataFechamento) {
        setPauta(pauta);
        setDataAbertura(dataAbertura);
        setDataFechamento(dataFechamento);
    }

    public void setPauta(Pauta pauta) {
        if (pauta == null)
            throw new IllegalArgumentException("Pauta não deve ser nula");
        this.pauta = pauta;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        LocalDateTime dataAtual = LocalDateTime.now().withNano(0);
        if (dataAbertura == null) {
            throw new IllegalArgumentException("A data de abertura não deve ser nula.");
        }
        if (dataAbertura.isBefore(dataAtual)) {
            throw new IllegalArgumentException("A data de abertura não deve ser menor que a data atual");
        }
        this.dataAbertura = dataAbertura;
    }

    public void setDataFechamento(LocalDateTime dataFechamento) {
        if (dataFechamento == null) {
            throw new IllegalArgumentException("A data de abertura não deve ser nula.");
        }
        if (dataFechamento.isBefore(this.dataAbertura)) {
            throw new IllegalArgumentException("A data de fechamento deve ser maior que a data de abertura.");
        }
        this.dataFechamento = dataFechamento;
    }
}
