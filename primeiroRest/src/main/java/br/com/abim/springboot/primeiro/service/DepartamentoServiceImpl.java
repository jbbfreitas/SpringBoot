package br.com.abim.springboot.primeiro.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.abim.springboot.primeiro.domain.Departamento;
import br.com.abim.springboot.primeiro.repository.DepartamentoRepository;
import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoServiceImpl implements DepartamentoService {

    private final Logger log = LoggerFactory.getLogger(DepartamentoService.class);
	
    @Autowired
    private DepartamentoRepository departamentoRepository;


    public Departamento createDepartamento(String nome, Long id) {
    	Departamento departamento = new Departamento();
    	if(id!=null) {
        	departamento.setId(id);
    	}
    	departamento.setNome(nome);
    	departamento.setUf("MT");
    	Departamento resultado=departamentoRepository.save(departamento);
        log.info("Criado o Departamento: {}", resultado);
    	return resultado;
    } 	
    @Override
       public Departamento createDepartamento(Departamento depto) {
    	Departamento resultado=departamentoRepository.save(depto);
        log.info("Criado o Departamento: {}", resultado);
    	return resultado;
    } 	
    public void deleteDepartamento(Long id) {
    	departamentoRepository.findById(id).ifPresent(depto -> {
    		departamentoRepository.delete(depto);
            log.info("Deletado o Departamento: {}", depto);
        });
    }

    @Override
    public List<Departamento> findAllDepartamento() {
        return departamentoRepository.findAll();
    }

    @Override
    public Optional<Departamento> findByIdDepartamento(Long id) {
        Optional<Departamento> departamento = departamentoRepository.findById(id);
        return departamento;
    }

    @Override
    public Departamento updateDepartamento(Departamento dept) {
        return departamentoRepository.save(dept);
    }

	
}
