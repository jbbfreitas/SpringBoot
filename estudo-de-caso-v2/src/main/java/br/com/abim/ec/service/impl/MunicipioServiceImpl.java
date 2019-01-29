package br.com.abim.ec.service.impl;

import br.com.abim.ec.service.MunicipioService;
import br.com.abim.ec.domain.Municipio;
import br.com.abim.ec.repository.MunicipioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Municipio.
 */
@Service
@Transactional
public class MunicipioServiceImpl implements MunicipioService {

    private final Logger log = LoggerFactory.getLogger(MunicipioServiceImpl.class);

    private MunicipioRepository municipioRepository ;

    public MunicipioServiceImpl(MunicipioRepository municipioRepository) {
        this.municipioRepository = municipioRepository;
    }

    /**
     * Grava um municipio.
     *
     * @param municipio the entity to save
     * @return the persisted entity
     */
    @Override
    public Municipio save(Municipio municipio) {
        log.debug("Solicitado ao repositório para salvar um Municipio : {}", municipio);
        return municipioRepository.save(municipio);
    }

    /**
     * Obtém todos municipios.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Municipio> findAll(Pageable pageable) {
        log.debug("Solicitado ao repositório obter todos os Municipios");
        return municipioRepository.findAll(pageable);
    }


    /**
     * Obtém um municipio pelo id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Municipio> findOne(Long id) {
        log.debug("Solicitado ao repositório obter o município de id : {}", id);
        return municipioRepository.findById(id);
    }

    /**
     * Exclui o municipio pelo id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Solicitado ao repositório para excluir o município de id : {}", id);
        municipioRepository.deleteById(id);
    }
}