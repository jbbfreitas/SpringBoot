package br.com.abim.ec.service.impl;

import br.com.abim.ec.service.DepartamentoService;
import br.com.abim.ec.domain.Departamento;
import br.com.abim.ec.domain.Municipio;
import br.com.abim.ec.repository.DepartamentoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Departamento.
 */
@Service
@Transactional
public class DepartamentoServiceImpl implements DepartamentoService {

    private final Logger log = LoggerFactory.getLogger(DepartamentoServiceImpl.class);

    private DepartamentoRepository departamentoRepository;

    public DepartamentoServiceImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    /**
     * Save a departamento.
     *
     * @param departamento the entity to save
     * @return the persisted entity
     */
    @Override
    public Departamento save(Departamento departamento) {
        log.debug("Request to save Departamento : {}", departamento);
        return departamentoRepository.save(departamento);
    }

    /**
     * Get all the departamentos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Departamento> findAll(Pageable pageable) {
        log.debug("Request to get all Departamentos");
        return departamentoRepository.findAll(pageable);
    }


    /**
     * Get one departamento by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Departamento> findOne(Long id) {
        log.debug("Request to get Departamento : {}", id);
        return departamentoRepository.findById(id);
    }

    /**
     * Delete the departamento by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Departamento : {}", id);
        departamentoRepository.deleteById(id);
    }

    //Desafio 1 V3
	@Override
	public Page<Departamento> findDepartamento(String nomeDepartamento,Pageable pageable) {
        log.debug("Request to get Departamento : {}", nomeDepartamento);
        return departamentoRepository.findByNomeDepartamento(nomeDepartamento,pageable);
//      return departamentoRepository.findByNomeDepartamentoIgnoreCaseContaining(nomeDepartamento,pageable);
	}

	
	//Desafio 2 V3
	@Override
	public Boolean isUniqueDepartamentoInMunicipio(Municipio municipio) {
		Integer numDepto= departamentoRepository.countDepartamentoByMunicipio(municipio);
		if (numDepto >=1) {
			return false;
			
		}else
			return true;
	}

	@Override
	public Boolean isUniqueSgilaDepartamento(String sigla) {
		return (departamentoRepository.findDepartamentoBySiglaDepartamento(sigla)==null);
	}
}
