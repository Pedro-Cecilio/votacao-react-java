package com.dbserver.votacaoBackend.domain.pauta;

import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String assunto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;

    @Setter
    @OneToOne(mappedBy = "pauta", optional = true, cascade = CascadeType.ALL)
    private SessaoVotacao sessaoVotacao;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Pauta(String assunto, String categoria, Usuario usuario) {
        setAssunto(assunto);
        setUsuario(usuario);
        setCategoria(categoria);
    }

    public void setUsuario(Usuario usuario) {
        if (usuario == null)
            throw new IllegalArgumentException("Usuario deve ser informado.");

        if (!usuario.isAdmin())
            throw new IllegalArgumentException("Usuario deve ser admin.");

        this.usuario = usuario;
    }

    public void setAssunto(String assunto) {
        if (assunto == null || assunto.trim().isEmpty())
            throw new IllegalArgumentException("Assunto deve ser informado.");

        this.assunto = assunto;
    }

    public void setCategoria(String categoria) {
        try {
            this.categoria = Categoria.valueOf(categoria);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Categoria inválida.");
        }
    }

}
