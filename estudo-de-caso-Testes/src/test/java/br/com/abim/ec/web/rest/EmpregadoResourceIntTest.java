package br.com.abim.ec.web.rest;

import br.com.abim.ec.EstudoDeCasoApp;

import br.com.abim.ec.domain.Empregado;
import br.com.abim.ec.domain.Municipio;
import br.com.abim.ec.domain.Departamento;
import br.com.abim.ec.repository.EmpregadoRepository;
import br.com.abim.ec.service.EmpregadoService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static br.com.abim.ec.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EmpregadoResource REST controller.
 *
 * @see EmpregadoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EstudoDeCasoApp.class)
public class EmpregadoResourceIntTest {

    private static final String DEFAULT_NOME_EMPREGADO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_EMPREGADO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_NASCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_NASCIMENTO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CPF = "AAAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_ADMISSAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_ADMISSAO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATA_DEMISSAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_DEMISSAO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATA_OBITO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_OBITO = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private EmpregadoRepository empregadoRepository;
    
    @Autowired
    private EmpregadoService empregadoService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;


    @Autowired
    private EntityManager em;

    private MockMvc restEmpregadoMockMvc;

    private Empregado empregado;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmpregadoResource empregadoResource = new EmpregadoResource(empregadoService);
        this.restEmpregadoMockMvc = MockMvcBuilders.standaloneSetup(empregadoResource)
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
    public static Empregado createEntity(EntityManager em) {
        Empregado empregado = new Empregado()
            .nomeEmpregado(DEFAULT_NOME_EMPREGADO)
            .dataNascimento(DEFAULT_DATA_NASCIMENTO)
            .cpf(DEFAULT_CPF)
            .dataAdmissao(DEFAULT_DATA_ADMISSAO)
            .dataDemissao(DEFAULT_DATA_DEMISSAO)
            .dataObito(DEFAULT_DATA_OBITO);
        // Add required entity
        Municipio municipio = MunicipioResourceIntTest.createEntity(em);
        em.persist(municipio);
        em.flush();
        empregado.setMunicipioResidencial(municipio);
        // Add required entity
        Departamento departamento = DepartamentoResourceIntTest.createEntity(em);
        em.persist(departamento);
        em.flush();
        empregado.setDepartamento(departamento);
        return empregado;
    }

    @Before
    public void initTest() {
        empregado = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmpregado() throws Exception {
        int databaseSizeBeforeCreate = empregadoRepository.findAll().size();

        // Create the Empregado
        restEmpregadoMockMvc.perform(post("/api/empregados")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(empregado)))
            .andExpect(status().isCreated());

        // Validate the Empregado in the database
        List<Empregado> empregadoList = empregadoRepository.findAll();
        assertThat(empregadoList).hasSize(databaseSizeBeforeCreate + 1);
        Empregado testEmpregado = empregadoList.get(empregadoList.size() - 1);
        assertThat(testEmpregado.getNomeEmpregado()).isEqualTo(DEFAULT_NOME_EMPREGADO);
        assertThat(testEmpregado.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testEmpregado.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testEmpregado.getDataAdmissao()).isEqualTo(DEFAULT_DATA_ADMISSAO);
        assertThat(testEmpregado.getDataDemissao()).isEqualTo(DEFAULT_DATA_DEMISSAO);
        assertThat(testEmpregado.getDataObito()).isEqualTo(DEFAULT_DATA_OBITO);
    }


    @Test
    @Transactional
    public void checkNomeEmpregadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = empregadoRepository.findAll().size();
        // set the field null
        empregado.setNomeEmpregado(null);

        // Create the Empregado, which fails.

        restEmpregadoMockMvc.perform(post("/api/empregados")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(empregado)))
            .andExpect(status().isBadRequest());

        List<Empregado> empregadoList = empregadoRepository.findAll();
        assertThat(empregadoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataNascimentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = empregadoRepository.findAll().size();
        // set the field null
        empregado.setDataNascimento(null);

        // Create the Empregado, which fails.

        restEmpregadoMockMvc.perform(post("/api/empregados")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(empregado)))
            .andExpect(status().isBadRequest());

        List<Empregado> empregadoList = empregadoRepository.findAll();
        assertThat(empregadoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCpfIsRequired() throws Exception {
        int databaseSizeBeforeTest = empregadoRepository.findAll().size();
        // set the field null
        empregado.setCpf(null);

        // Create the Empregado, which fails.

        restEmpregadoMockMvc.perform(post("/api/empregados")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(empregado)))
            .andExpect(status().isBadRequest());

        List<Empregado> empregadoList = empregadoRepository.findAll();
        assertThat(empregadoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataAdmissaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = empregadoRepository.findAll().size();
        // set the field null
        empregado.setDataAdmissao(null);

        // Create the Empregado, which fails.

        restEmpregadoMockMvc.perform(post("/api/empregados")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(empregado)))
            .andExpect(status().isBadRequest());

        List<Empregado> empregadoList = empregadoRepository.findAll();
        assertThat(empregadoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmpregados() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList
        restEmpregadoMockMvc.perform(get("/api/empregados?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empregado.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeEmpregado").value(hasItem(DEFAULT_NOME_EMPREGADO.toString())))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF.toString())))
            .andExpect(jsonPath("$.[*].dataAdmissao").value(hasItem(DEFAULT_DATA_ADMISSAO.toString())))
            .andExpect(jsonPath("$.[*].dataDemissao").value(hasItem(DEFAULT_DATA_DEMISSAO.toString())))
            .andExpect(jsonPath("$.[*].dataObito").value(hasItem(DEFAULT_DATA_OBITO.toString())));
    }
    
    @Test
    @Transactional
    public void getEmpregado() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get the empregado
        restEmpregadoMockMvc.perform(get("/api/empregados/{id}", empregado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(empregado.getId().intValue()))
            .andExpect(jsonPath("$.nomeEmpregado").value(DEFAULT_NOME_EMPREGADO.toString()))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF.toString()))
            .andExpect(jsonPath("$.dataAdmissao").value(DEFAULT_DATA_ADMISSAO.toString()))
            .andExpect(jsonPath("$.dataDemissao").value(DEFAULT_DATA_DEMISSAO.toString()))
            .andExpect(jsonPath("$.dataObito").value(DEFAULT_DATA_OBITO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmpregado() throws Exception {
        // Get the empregado
        restEmpregadoMockMvc.perform(get("/api/empregados/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmpregado() throws Exception {
        // Initialize the database
        empregadoService.save(empregado);

        int databaseSizeBeforeUpdate = empregadoRepository.findAll().size();

        // Update the empregado
        Empregado updatedEmpregado = empregadoRepository.findById(empregado.getId()).get();
        // Disconnect from session so that the updates on updatedEmpregado are not directly saved in db
        em.detach(updatedEmpregado);
        updatedEmpregado
            .nomeEmpregado(UPDATED_NOME_EMPREGADO)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .cpf(UPDATED_CPF)
            .dataAdmissao(UPDATED_DATA_ADMISSAO)
            .dataDemissao(UPDATED_DATA_DEMISSAO)
            .dataObito(UPDATED_DATA_OBITO);

        restEmpregadoMockMvc.perform(put("/api/empregados")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmpregado)))
            .andExpect(status().isOk());

        // Validate the Empregado in the database
        List<Empregado> empregadoList = empregadoRepository.findAll();
        assertThat(empregadoList).hasSize(databaseSizeBeforeUpdate);
        Empregado testEmpregado = empregadoList.get(empregadoList.size() - 1);
        assertThat(testEmpregado.getNomeEmpregado()).isEqualTo(UPDATED_NOME_EMPREGADO);
        assertThat(testEmpregado.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testEmpregado.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testEmpregado.getDataAdmissao()).isEqualTo(UPDATED_DATA_ADMISSAO);
        assertThat(testEmpregado.getDataDemissao()).isEqualTo(UPDATED_DATA_DEMISSAO);
        assertThat(testEmpregado.getDataObito()).isEqualTo(UPDATED_DATA_OBITO);
    }


    @Test
    @Transactional
    public void deleteEmpregado() throws Exception {
        // Initialize the database
        empregadoService.save(empregado);

        int databaseSizeBeforeDelete = empregadoRepository.findAll().size();

        // Get the empregado
        restEmpregadoMockMvc.perform(delete("/api/empregados/{id}", empregado.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Empregado> empregadoList = empregadoRepository.findAll();
        assertThat(empregadoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Empregado.class);
        Empregado empregado1 = new Empregado();
        empregado1.setId(1L);
        Empregado empregado2 = new Empregado();
        empregado2.setId(empregado1.getId());
        assertThat(empregado1).isEqualTo(empregado2);
        empregado2.setId(2L);
        assertThat(empregado1).isNotEqualTo(empregado2);
        empregado1.setId(null);
        assertThat(empregado1).isNotEqualTo(empregado2);
    }
}
