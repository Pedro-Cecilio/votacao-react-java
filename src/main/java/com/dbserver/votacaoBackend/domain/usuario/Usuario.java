package com.dbserver.votacaoBackend.domain.usuario;

import com.dbserver.votacaoBackend.utils.Utils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// @AllArgsConstructor
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

    public Usuario(String nome, String sobrenome, String cpf, boolean admin) {
        setNome(nome);
        setSobrenome(sobrenome);
        setCpf(cpf);
        this.admin = admin;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty())
            throw new IllegalArgumentException("Nome deve ser informado.");

        if (nome.trim().length() < 3 || nome.trim().length() > 20)
            throw new IllegalArgumentException("Nome deve conter entre 3 e 20 caracteres.");

        this.nome = nome.trim();
    }

    public void setSobrenome(String sobrenome) {
        if (sobrenome == null || sobrenome.trim().isEmpty()) 
            throw new IllegalArgumentException("Sobrenome deve ser informado.");
        
        if (sobrenome.trim().length() < 2 || sobrenome.trim().length() > 20) 
            throw new IllegalArgumentException("Sobrenome deve conter entre 2 e 20 caracteres.");
        
        this.sobrenome = sobrenome.trim();
    }

    public void setCpf(String cpf) {
        if (cpf == null || !Utils.validarRegex(Utils.REGEX_CPF, cpf.trim())) 
            throw new IllegalArgumentException("Cpf deve conter 11 caracteres numéricos.");
        
        this.cpf = cpf.trim();
    }
}
