package com.dbserver.votacaoBackend.domain.pauta.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.enums.Categoria;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {
    List<Pauta> findAllByUsuarioId(Long usuarioId, Pageable pageable);
    List<Pauta> findAllByCategoria(Categoria categoria, Pageable pageable);
}
