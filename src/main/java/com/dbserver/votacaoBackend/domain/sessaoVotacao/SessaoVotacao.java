package com.dbserver.votacaoBackend.domain.sessaoVotacao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.validacoes.PautaValidacoes;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.validacoes.SessaoVotacaoValidacoes;
import com.dbserver.votacaoBackend.domain.voto.Voto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SessaoVotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = { CascadeType.REFRESH }, optional = false)
    @JoinColumn(name = "pauta_id")
    private Pauta pauta;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Voto> votosPositivos;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Voto> votosNegativos;

    @Column
    private LocalDateTime dataAbertura;

    @Column
    private LocalDateTime dataFechamento;

    @Transient
    private boolean ativa;

    public static class SessaoVotacaoBuilder {
        private List<Voto> votosPositivos = new ArrayList<>();
        private List<Voto> votosNegativos = new ArrayList<>();

        public SessaoVotacao build() {
            PautaValidacoes.validarPautaNaoNula(this.pauta);
            SessaoVotacaoValidacoes.validarDataDeAbertura(this.dataAbertura);
            SessaoVotacaoValidacoes.validarDataDeFechamento(this.dataFechamento, this.dataAbertura);
            return new SessaoVotacao(id, pauta, votosPositivos, votosNegativos, dataAbertura, dataFechamento, false);
        }
    }

    public void setVotosPositivos(Voto voto) {
        if (voto == null)
            throw new IllegalArgumentException("Voto não deve ser nulo.");

        this.votosPositivos.add(voto);
    }

    public void setVotosNegativos(Voto voto) {
        if (voto == null)
            throw new IllegalArgumentException("Voto não deve ser nulo.");

        this.votosNegativos.add(voto);
    }

    public boolean isAtiva() {
        return LocalDateTime.now().isBefore(this.dataFechamento);
    }

}
