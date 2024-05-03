package com.dbserver.votacaoBackend.domain.sessaoVotacao.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dbserver.votacaoBackend.domain.sessaoVotacao.SessaoVotacao;

@Repository
public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Long>{
    
    @Query("SELECT s FROM SessaoVotacao s WHERE s.pauta.id = :pautaId AND s.dataFechamento > :dataAtual")
    Optional<SessaoVotacao> findByPautaIdAndSessaoVotacaoAtiva(@Param("pautaId") Long pautaId, @Param("dataAtual") LocalDateTime dataAtual);
}
