package com.dbserver.votacaoBackend.domain.autenticacao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutenticacaoRepository extends JpaRepository<Autenticacao, Long>{
    
    Optional<Autenticacao> findByEmail(String email);
}
