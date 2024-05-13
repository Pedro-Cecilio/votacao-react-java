package com.dbserver.votacaoBackend.domain.voto;

import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.utils.Utils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private String cpf;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;

    public Voto(String cpf, Usuario usuario) {
        setCpf(cpf);
        this.usuario = usuario;
    }

    public void setCpf(String cpf) {
        if (cpf == null || !Utils.validarRegex(Utils.REGEX_CPF, cpf.trim()))
            throw new IllegalArgumentException("Cpf deve conter 11 caracteres numéricos.");

        this.cpf = cpf.trim();
    }
}
