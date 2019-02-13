package br.com.abim.ec.repository;

import br.com.abim.ec.domain.Departamento;
import br.com.abim.ec.domain.Municipio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Departamento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

	
	
	
}
