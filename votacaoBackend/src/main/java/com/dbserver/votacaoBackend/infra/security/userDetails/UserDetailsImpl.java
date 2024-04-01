package com.dbserver.votacaoBackend.infra.security.userDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class UserDetailsImpl implements UserDetails {

    @NonNull
    private Usuario usuario;
    @Nonnull
    private Autenticacao autenticacao;

    public UserDetailsImpl(Autenticacao autenticacao) {
        this.autenticacao = autenticacao;
        this.usuario = autenticacao.getUsuario();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> autoridades = new ArrayList<>();
        if (usuario.isAdmin()) {
            autoridades.add(new SimpleGrantedAuthority("ADMIN"));
            return autoridades;
        }
        autoridades.add(new SimpleGrantedAuthority("USER"));
        return autoridades;
    }

    @Override
    public String getPassword() {
        return this.autenticacao.getSenha();
    }

    @Override
    public String getUsername() {
        return this.autenticacao.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
