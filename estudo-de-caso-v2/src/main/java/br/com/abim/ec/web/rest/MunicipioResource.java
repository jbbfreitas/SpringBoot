package br.com.abim.ec.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.abim.ec.domain.Municipio;
import br.com.abim.ec.service.MunicipioService;
import br.com.abim.ec.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Municipio.
 */
@RestController
@RequestMapping("/api")
public class MunicipioResource {

    private final Logger log = LoggerFactory.getLogger(MunicipioResource.class);

    private static final String ENTITY_NAME = "municipio";

    @Autowired
    private MunicipioService municipioService;

//    public MunicipioResource(MunicipioService municipioService) {
//        this.municipioService = municipioService;
//    }

   

    /**
     * GET  /municipios : get all the municipios.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of municipios in body
     */
    @GetMapping("/municipios")
    public ResponseEntity<List<Municipio>> getAllMunicipios(Pageable pageable) {
        log.debug("REST request to get a page of Municipios");
        Page<Municipio> page = municipioService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/municipios");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

  
}
