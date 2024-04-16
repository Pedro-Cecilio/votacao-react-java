package com.dbserver.votacaoBackend.domain.pauta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long>{
    Page<Pauta> findAllByUsuarioId(Long usuarioId, Pageable pageable);
}
