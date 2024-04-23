package com.dbserver.votacaoBackend.domain.autenticacao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;

@Repository
public interface AutenticacaoRepository extends JpaRepository<Autenticacao, Long>{
    
    Optional<Autenticacao> findByEmail(String email);

    @Query("SELECT a FROM Autenticacao a WHERE a.usuario.cpf = ?1")
    Optional<Autenticacao> findByCpf(String cpf);


}   
