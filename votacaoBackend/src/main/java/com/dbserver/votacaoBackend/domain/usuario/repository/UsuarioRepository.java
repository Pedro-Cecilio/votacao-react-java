package com.dbserver.votacaoBackend.domain.usuario.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dbserver.votacaoBackend.domain.usuario.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Page<Usuario> buscarTodosUsuarios(Pageable pageable);
}
