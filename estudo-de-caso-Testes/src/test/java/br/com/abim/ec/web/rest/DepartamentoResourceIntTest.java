package br.com.abim.ec.web.rest;

import br.com.abim.ec.EstudoDeCasoApp;

import br.com.abim.ec.domain.Departamento;
import br.com.abim.ec.repository.DepartamentoRepository;
import br.com.abim.ec.service.DepartamentoService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static br.com.abim.ec.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DepartamentoResource REST controller.
 *
 * @see DepartamentoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EstudoDeCasoApp.class)
public class DepartamentoResourceIntTest {

    private static final String DEFAULT_NOME_DEPARTAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_DEPARTAMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLA_DEPARTAMENTO = "AAAAA";
    private static final String UPDATED_SIGLA_DEPARTAMENTO = "BBBBB";

    private static final String DEFAULT_CNPJ = "AAAAAAAAAAAAAA";
    private static final String UPDATED_CNPJ = "BBBBBBBBBBBBBB";

    @Autowired
    private DepartamentoRepository departamentoRepository;
    
    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;


    @Autowired
    private EntityManager em;

    private MockMvc restDepartamentoMockMvc;

    private Departamento departamento;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DepartamentoResource departamentoResource = new DepartamentoResource(departamentoService);
        this.restDepartamentoMockMvc = MockMvcBuilders.standaloneSetup(departamentoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departamento createEntity(EntityManager em) {
        Departamento departamento = new Departamento()
            .nomeDepartamento(DEFAULT_NOME_DEPARTAMENTO)
            .siglaDepartamento(DEFAULT_SIGLA_DEPARTAMENTO)
            .cnpj(DEFAULT_CNPJ);
        return departamento;
    }

    @Before
    public void initTest() {
        departamento = createEntity(em);
    }

    @Test
    @Transactional
    public void createDepartamento() throws Exception {
        int databaseSizeBeforeCreate = departamentoRepository.findAll().size();

        // Create the Departamento
        restDepartamentoMockMvc.perform(post("/api/departamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(departamento)))
            .andExpect(status().isCreated());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeCreate + 1);
        Departamento testDepartamento = departamentoList.get(departamentoList.size() - 1);
        assertThat(testDepartamento.getNomeDepartamento()).isEqualTo(DEFAULT_NOME_DEPARTAMENTO);
        assertThat(testDepartamento.getSiglaDepartamento()).isEqualTo(DEFAULT_SIGLA_DEPARTAMENTO);
        assertThat(testDepartamento.getCnpj()).isEqualTo(DEFAULT_CNPJ);
    }


    @Test
    @Transactional
    public void checkNomeDepartamentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = departamentoRepository.findAll().size();
        // set the field null
        departamento.setNomeDepartamento(null);

        // Create the Departamento, which fails.

        restDepartamentoMockMvc.perform(post("/api/departamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(departamento)))
            .andExpect(status().isBadRequest());

        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSiglaDepartamentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = departamentoRepository.findAll().size();
        // set the field null
        departamento.setSiglaDepartamento(null);

        // Create the Departamento, which fails.

        restDepartamentoMockMvc.perform(post("/api/departamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(departamento)))
            .andExpect(status().isBadRequest());

        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDepartamentos() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get all the departamentoList
        restDepartamentoMockMvc.perform(get("/api/departamentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeDepartamento").value(hasItem(DEFAULT_NOME_DEPARTAMENTO.toString())))
            .andExpect(jsonPath("$.[*].siglaDepartamento").value(hasItem(DEFAULT_SIGLA_DEPARTAMENTO.toString())))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ.toString())));
    }
    
    @Test
    @Transactional
    public void getDepartamento() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get the departamento
        restDepartamentoMockMvc.perform(get("/api/departamentos/{id}", departamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(departamento.getId().intValue()))
            .andExpect(jsonPath("$.nomeDepartamento").value(DEFAULT_NOME_DEPARTAMENTO.toString()))
            .andExpect(jsonPath("$.siglaDepartamento").value(DEFAULT_SIGLA_DEPARTAMENTO.toString()))
            .andExpect(jsonPath("$.cnpj").value(DEFAULT_CNPJ.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDepartamento() throws Exception {
        // Get the departamento
        restDepartamentoMockMvc.perform(get("/api/departamentos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDepartamento() throws Exception {
        // Initialize the database
        departamentoService.save(departamento);

        int databaseSizeBeforeUpdate = departamentoRepository.findAll().size();

        // Update the departamento
        Departamento updatedDepartamento = departamentoRepository.findById(departamento.getId()).get();
        // Disconnect from session so that the updates on updatedDepartamento are not directly saved in db
        em.detach(updatedDepartamento);
        updatedDepartamento
            .nomeDepartamento(UPDATED_NOME_DEPARTAMENTO)
            .siglaDepartamento(UPDATED_SIGLA_DEPARTAMENTO)
            .cnpj(UPDATED_CNPJ);

        restDepartamentoMockMvc.perform(put("/api/departamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDepartamento)))
            .andExpect(status().isOk());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeUpdate);
        Departamento testDepartamento = departamentoList.get(departamentoList.size() - 1);
        assertThat(testDepartamento.getNomeDepartamento()).isEqualTo(UPDATED_NOME_DEPARTAMENTO);
        assertThat(testDepartamento.getSiglaDepartamento()).isEqualTo(UPDATED_SIGLA_DEPARTAMENTO);
        assertThat(testDepartamento.getCnpj()).isEqualTo(UPDATED_CNPJ);
    }


    @Test
    @Transactional
    public void deleteDepartamento() throws Exception {
        // Initialize the database
        departamentoService.save(departamento);

        int databaseSizeBeforeDelete = departamentoRepository.findAll().size();

        // Get the departamento
        restDepartamentoMockMvc.perform(delete("/api/departamentos/{id}", departamento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Departamento.class);
        Departamento departamento1 = new Departamento();
        departamento1.setId(1L);
        Departamento departamento2 = new Departamento();
        departamento2.setId(departamento1.getId());
        assertThat(departamento1).isEqualTo(departamento2);
        departamento2.setId(2L);
        assertThat(departamento1).isNotEqualTo(departamento2);
        departamento1.setId(null);
        assertThat(departamento1).isNotEqualTo(departamento2);
    }
}
