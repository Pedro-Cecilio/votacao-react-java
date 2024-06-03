package com.dbserver.votacaoBackend.domain.usuario;

import com.dbserver.votacaoBackend.domain.usuario.validacoes.UsuarioValidacoes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 20)
    private String nome;

    @Column(nullable = false, length = 20)
    private String sobrenome;

    @Column(nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String cpf;

    @Column(nullable = false)
    private boolean admin;


    public static class UsuarioBuilder{
        public Usuario build(){
            Usuario usuario = new Usuario(this.id, this.nome, this.sobrenome, this.cpf, this.admin);
            UsuarioValidacoes.validarformatoNome(this.nome);
            UsuarioValidacoes.validarFormatoSobrenome(this.sobrenome);
            UsuarioValidacoes.validarFormatoCpf(this.cpf);
            return usuario;
        }
    }
}
