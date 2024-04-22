package com.dbserver.votacaoBackend.domain.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCpf(String cpf);
}
