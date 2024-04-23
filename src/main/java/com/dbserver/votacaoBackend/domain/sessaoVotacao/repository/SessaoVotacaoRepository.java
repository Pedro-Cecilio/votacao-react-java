package com.dbserver.votacaoBackend.domain.sessaoVotacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;

@Repository
public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Long>{
    
}
