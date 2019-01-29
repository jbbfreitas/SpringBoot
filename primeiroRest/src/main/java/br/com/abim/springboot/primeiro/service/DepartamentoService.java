package br.com.abim.springboot.primeiro.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import br.com.abim.springboot.primeiro.domain.Departamento;

public interface DepartamentoService {

	Departamento createDepartamento(String nome, Long id);

	Departamento createDepartamento(Departamento Departamento);

	void deleteDepartamento(Long id);

	List<Departamento> findAllDepartamento();

	Optional<Departamento> findByIdDepartamento(Long id);

	Departamento updateDepartamento(Departamento Departamento);
}
