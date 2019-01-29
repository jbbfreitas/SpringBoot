package br.com.abim.ec.web.rest;

import br.com.abim.ec.domain.Empregado;
import br.com.abim.ec.service.EmpregadoService;
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
 * REST controller for managing Empregado.
 */
@RestController
@RequestMapping("/api")
public class EmpregadoResource {

    private final Logger log = LoggerFactory.getLogger(EmpregadoResource.class);

    private static final String ENTITY_NAME = "empregado";

    private EmpregadoService empregadoService;

    public EmpregadoResource(EmpregadoService empregadoService) {
        this.empregadoService = empregadoService;
    }

    /**
     * POST  /empregados : Create a new empregado.
     *
     * @param empregado the empregado to create
     * @return the ResponseEntity with status 201 (Created) and with body the new empregado, or with status 400 (Bad Request) if the empregado has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/empregados")
    public ResponseEntity<Empregado> createEmpregado(@Valid @RequestBody Empregado empregado) throws URISyntaxException {
        log.debug("REST request to save Empregado : {}", empregado);
        if (empregado.getId() != null) {
            throw new BadRequestAlertException("A new empregado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Empregado result = empregadoService.save(empregado);
        return ResponseEntity.created(new URI("/api/empregados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /empregados : Updates an existing empregado.
     *
     * @param empregado the empregado to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated empregado,
     * or with status 400 (Bad Request) if the empregado is not valid,
     * or with status 500 (Internal Server Error) if the empregado couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/empregados")
    public ResponseEntity<Empregado> updateEmpregado(@Valid @RequestBody Empregado empregado) throws URISyntaxException {
        log.debug("REST request to update Empregado : {}", empregado);
        if (empregado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Empregado result = empregadoService.save(empregado);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, empregado.getId().toString()))
            .body(result);
    }

    /**
     * GET  /empregados : get all the empregados.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of empregados in body
     */
    @GetMapping("/empregados")
    public ResponseEntity<List<Empregado>> getAllEmpregados(Pageable pageable) {
        log.debug("REST request to get a page of Empregados");
        Page<Empregado> page = empregadoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/empregados");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /empregados/:id : get the "id" empregado.
     *
     * @param id the id of the empregado to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the empregado, or with status 404 (Not Found)
     */
    @GetMapping("/empregados/{id}")
    public ResponseEntity<Empregado> getEmpregado(@PathVariable Long id) {
        log.debug("REST request to get Empregado : {}", id);
        Optional<Empregado> empregado = empregadoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(empregado);
    }

    /**
     * DELETE  /empregados/:id : delete the "id" empregado.
     *
     * @param id the id of the empregado to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/empregados/{id}")
    public ResponseEntity<Void> deleteEmpregado(@PathVariable Long id) {
        log.debug("REST request to delete Empregado : {}", id);
        empregadoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
