package br.com.abim.ec.service.impl;

import br.com.abim.ec.service.EmpregadoService;
import br.com.abim.ec.domain.Empregado;
import br.com.abim.ec.repository.EmpregadoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Empregado.
 */
@Service
@Transactional
public class EmpregadoServiceImpl implements EmpregadoService {

    private final Logger log = LoggerFactory.getLogger(EmpregadoServiceImpl.class);

    private EmpregadoRepository empregadoRepository;

    public EmpregadoServiceImpl(EmpregadoRepository empregadoRepository) {
        this.empregadoRepository = empregadoRepository;
    }

    /**
     * Save a empregado.
     *
     * @param empregado the entity to save
     * @return the persisted entity
     */
    @Override
    public Empregado save(Empregado empregado) {
        log.debug("Request to save Empregado : {}", empregado);
        return empregadoRepository.save(empregado);
    }

    /**
     * Get all the empregados.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Empregado> findAll(Pageable pageable) {
        log.debug("Request to get all Empregados");
        return empregadoRepository.findAll(pageable);
    }


    /**
     * Get one empregado by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Empregado> findOne(Long id) {
        log.debug("Request to get Empregado : {}", id);
        return empregadoRepository.findById(id);
    }

    /**
     * Delete the empregado by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Empregado : {}", id);
        empregadoRepository.deleteById(id);
    }
}
