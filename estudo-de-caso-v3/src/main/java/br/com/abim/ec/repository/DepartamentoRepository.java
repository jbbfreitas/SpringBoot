package br.com.abim.ec.repository;

import br.com.abim.ec.domain.Departamento;
import br.com.abim.ec.domain.Municipio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Departamento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

	//Desafio 1 Baseando-se nos exemplos providos em V2, crie uma `Query` para encontrar um Departamento pelo Nome.
	

	@Query("Select d from Departamento d where UPPER(d.nomeDepartamento) like %:nome%")
	Page<Departamento> findByNomeDepartamento(@Param("nome")String nomeDepartamento, Pageable pageable);	
	

	
//	Page<Departamento> findByNomeDepartamentoIgnoreCaseContaining(String nomeDepartamento, Pageable pageable);	
	
	
	//Desafio 2
	Integer countDepartamentoByMunicipio(Municipio municipio);
	
	Departamento findDepartamentoBySiglaDepartamento(String sigla);
	
}
