package com.dbserver.votacaoBackend.domain.autenticacao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;

@Repository
public interface AutenticacaoRepository extends JpaRepository<Autenticacao, Long>{
    
    Optional<Autenticacao> findByEmail(String email);
}
