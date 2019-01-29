package br.com.abim.ec.service;

import br.com.abim.ec.domain.Departamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
}
