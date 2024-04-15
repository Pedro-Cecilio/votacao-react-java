package com.dbserver.votacaoBackend.domain.pauta;

import com.dbserver.votacaoBackend.domain.usuario.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Pauta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne
    private Usuario usuario;

    public Pauta(String nome, Usuario usuario) {
        setNome(nome);
        setUsuario(usuario);
    }

    public void setUsuario(Usuario usuario) {
        if(usuario == null){
            throw new IllegalArgumentException("Usuario deve ser informado.");
        }
        if(!usuario.isAdmin()){
            throw new IllegalArgumentException("Usuario deve ser admin.");
        }
        this.usuario = usuario;
    }
    public void setNome(String nome) {
        if(nome == null || nome.trim().isEmpty()){
            throw new IllegalArgumentException("Nome deve ser informado.");
        }
        this.nome = nome;
    }


}
