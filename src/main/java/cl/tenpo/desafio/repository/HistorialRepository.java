package cl.tenpo.desafio.repository;

import cl.tenpo.desafio.entity.HistorialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialRepository extends JpaRepository<HistorialEntity, Long> {
}
