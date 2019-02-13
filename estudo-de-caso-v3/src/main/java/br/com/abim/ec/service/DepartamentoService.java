package br.com.abim.ec.service;

import br.com.abim.ec.domain.Departamento;
import br.com.abim.ec.domain.Municipio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Departamento.
 */
public interface DepartamentoService {

    /**
     * Save a departamento.
     *
     * @param departamento the entity to save
     * @return the persisted entity
     */
    Departamento save(Departamento departamento);

    /**
     * Get all the departamentos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Departamento> findAll(Pageable pageable);


    /**
     * Get the "id" departamento.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Departamento> findOne(Long id);

    /**
     * Delete the "id" departamento.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
    
    /**
     * Find Departamento by nomeDepartamento.
     *
     * @param nomeDepartamento the name
     * @return the entity
     */
   Page<Departamento> findDepartamento(String nomeDepartamento,Pageable pageable);
    
    
    /**
     * Only one Departamento is permitted by Municpio
     * 
     * @param municipio
     * @return True or False
     */
    Boolean isUniqueDepartamentoInMunicipio(Municipio municipio);


    /**
     * 
     * @param sigla
     * @return True or False
     */
    Boolean isUniqueSgilaDepartamento(String sigla);
    
}
