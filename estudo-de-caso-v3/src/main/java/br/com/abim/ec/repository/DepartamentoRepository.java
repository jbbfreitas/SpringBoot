package br.com.abim.ec.repository;

import br.com.abim.ec.domain.Departamento;

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

	//Desafio 1 Baseando-se nos exemplos providos em V2, crie uma `Query` para encontrar um Departamento pelo Nome.
	
	Optional<Departamento> findByNomeDepartamento(String nomeDepartamento);	
	
	
	
}
