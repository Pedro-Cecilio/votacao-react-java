package com.dbserver.votacaoBackend.domain.usuario;

import com.dbserver.votacaoBackend.domain.usuario.dto.CriarUsuarioDto;
import com.dbserver.votacaoBackend.utils.Utils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String sobrenome;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private boolean admin;

    public Usuario(CriarUsuarioDto dto){
        setNome(dto.nome());
        setSobrenome(dto.sobrenome());
        setCpf(dto.cpf());
        this.admin = dto.admin();
    }

    public void setNome(String nome){
        if(nome == null || nome.trim().isEmpty()){
            throw new IllegalArgumentException("Nome deve ser informado.");
        }
        if(nome.trim().length() < 3 || nome.trim().length() > 20){
            throw new IllegalArgumentException("Nome deve conter entre 3 e 20 caracteres.");
        }
        this.nome = nome.trim();
    }
    public void setSobrenome(String sobrenome){
        if(sobrenome == null || sobrenome.trim().isEmpty()){
            throw new IllegalArgumentException("Sobrenome deve ser informado.");
        }
        if(sobrenome.trim().length() < 2 || sobrenome.trim().length() > 20){
            throw new IllegalArgumentException("Sobrenome deve conter entre 2 e 20 caracteres.");
        }
        this.sobrenome = sobrenome.trim();
    }
    public void setCpf(String cpf){
        if(cpf == null || !Utils.validarRegex(Utils.REGEX_CPF, cpf.trim())){
            throw new IllegalArgumentException("Cpf deve conter 11 caracteres numéricos.");
        }
        this.cpf = cpf.trim();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Usuario other = (Usuario) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (cpf == null) {
            if (other.cpf != null)
                return false;
        } else if (!cpf.equals(other.cpf))
            return false;
        return true;
    }

    

    
}

