package br.com.abim.ec.service;

import br.com.abim.ec.domain.Empregado;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Empregado.
 */
public interface EmpregadoService {

    /**
     * Save a empregado.
     *
     * @param empregado the entity to save
     * @return the persisted entity
     */
    Empregado save(Empregado empregado);

    /**
     * Get all the empregados.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Empregado> findAll(Pageable pageable);


    /**
     * Get the "id" empregado.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Empregado> findOne(Long id);

    /**
     * Delete the "id" empregado.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
