package br.com.abim.ec.web.rest;

import br.com.abim.ec.domain.Departamento;
import br.com.abim.ec.service.DepartamentoService;
import br.com.abim.ec.web.rest.errors.BadRequestAlertException;
import br.com.abim.ec.web.rest.util.HeaderUtil;
import br.com.abim.ec.web.rest.util.PaginationUtil;
import br.com.abim.ec.web.rest.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Departamento.
 */
@RestController
@RequestMapping("/api")
public class DepartamentoResource {

    private final Logger log = LoggerFactory.getLogger(DepartamentoResource.class);

    private static final String ENTITY_NAME = "departamento";

    private DepartamentoService departamentoService;

    public DepartamentoResource(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    /**
     * POST  /departamentos : Create a new departamento.
     *
     * @param departamento the departamento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new departamento, or with status 400 (Bad Request) if the departamento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/departamentos")
    public ResponseEntity<Departamento> createDepartamento(@Valid @RequestBody Departamento departamento) throws URISyntaxException {
        log.debug("REST request to save Departamento : {}", departamento);
        if (departamento.getId() != null) {
            throw new BadRequestAlertException("A new departamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
//Desafio 2 V3
        if (!departamentoService.isUniqueDepartamentoInMunicipio(departamento.getMunicipio())) {
            throw new BadRequestAlertException("Já existe um departamento para esse município", ENTITY_NAME, "departamentoexists");
        }
        if (!departamentoService.isUniqueSgilaDepartamento(departamento.getSiglaDepartamento())) {
            throw new BadRequestAlertException("Esta sigla de departamento já foi utilizada", ENTITY_NAME, "siglaexists");
        	
        }
        Departamento result = departamentoService.save(departamento);
        return ResponseEntity.created(new URI("/api/departamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /departamentos : Updates an existing departamento.
     *
     * @param departamento the departamento to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated departamento,
     * or with status 400 (Bad Request) if the departamento is not valid,
     * or with status 500 (Internal Server Error) if the departamento couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/departamentos")
    public ResponseEntity<Departamento> updateDepartamento(@Valid @RequestBody Departamento departamento) throws URISyntaxException {
        log.debug("REST request to update Departamento : {}", departamento);
        if (departamento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Departamento result = departamentoService.save(departamento);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, departamento.getId().toString()))
            .body(result);
    }

    /**
     * GET  /departamentos : get all the departamentos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of departamentos in body
     */
    @GetMapping("/departamentos")
    public ResponseEntity<List<Departamento>> getAllDepartamentos(Pageable pageable) {
        log.debug("REST request to get a page of Departamentos");
        Page<Departamento> page = departamentoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/departamentos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /departamentos/:id : get the "id" departamento.
     *
     * @param id the id of the departamento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the departamento, or with status 404 (Not Found)
     */
    @GetMapping("/departamentos/{id}")
    public ResponseEntity<Departamento> getDepartamento(@PathVariable Long id) {
        log.debug("REST request to get Departamento : {}", id);
        Optional<Departamento> departamento = departamentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(departamento);
    }

    /**
     * DELETE  /departamentos/:id : delete the "id" departamento.
     *
     * @param id the id of the departamento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/departamentos/{id}")
    public ResponseEntity<Void> deleteDepartamento(@PathVariable Long id) {
        log.debug("REST request to delete Departamento : {}", id);
        departamentoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
 //Desafio 1 V3
    /**
     * GET  /departamentos/:nomeDepartamento : get the departamento by nomeDepartamento.
     *
     * @param id the id of the departamento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the departamento, or with status 404 (Not Found)
     */
    @GetMapping("/departamentos/nome/{nomeDepartamento}")
    public ResponseEntity<List<Departamento>>  getDepartamentoByNome(@PathVariable String nomeDepartamento,Pageable pageable) {
        log.debug("REST request to get Departamento : {}", nomeDepartamento);
        Page<Departamento> page = departamentoService.findDepartamento(nomeDepartamento,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/departamentos/nome/");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    
}
