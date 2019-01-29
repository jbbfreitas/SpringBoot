package br.com.abim.ec.repository;

import br.com.abim.ec.domain.Empregado;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Empregado entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmpregadoRepository extends JpaRepository<Empregado, Long> {

}
