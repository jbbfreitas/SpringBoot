package br.com.abim.springboot.primeiro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.abim.springboot.primeiro.domain.Departamento;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

	Departamento findByNome(String nome);

	void delete(Departamento dept);

	List<Departamento> findAll();

	Departamento save(Departamento dept);

}
