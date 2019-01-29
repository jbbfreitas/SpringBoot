package br.com.abim.springboot.primeiro.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.abim.springboot.primeiro.domain.Departamento;
import br.com.abim.springboot.primeiro.service.DepartamentoService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping({"/departamentos"})
public class DepartamentoController {

	    @Autowired
	    private DepartamentoService departamentoServiceImpl;

	    @PostMapping
	    public Departamento create(@RequestBody Departamento Departamento){
	        return departamentoServiceImpl.createDepartamento(Departamento);
	    }

	    @GetMapping(path = {"/{id}"})
	    public Optional<Departamento> findOne(@PathVariable("id") Long id){
	        return departamentoServiceImpl.findByIdDepartamento(id);
	    }

	    @PutMapping(path = {"/{id}"})
	    public Departamento update(@PathVariable("id") Long id, @RequestBody Departamento Departamento){
	        Departamento.setId(id);
	        return departamentoServiceImpl.createDepartamento(Departamento);
	    }

	    @DeleteMapping(path ={"/{id}"})
	    public void delete(@PathVariable("id") Long id) {
	        departamentoServiceImpl.deleteDepartamento(id);
	    }

	    @GetMapping
	    public List<Departamento> findAll(){
	        return departamentoServiceImpl.findAllDepartamento();
	    }


}
