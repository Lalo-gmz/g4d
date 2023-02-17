package mx.lania.g4d.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import mx.lania.g4d.IntegrationTest;
import mx.lania.g4d.domain.AtributoFuncionalidad;
import mx.lania.g4d.domain.Comentario;
import mx.lania.g4d.domain.EstatusFuncionalidad;
import mx.lania.g4d.domain.Etiqueta;
import mx.lania.g4d.domain.Funcionalidad;
import mx.lania.g4d.domain.Iteracion;
import mx.lania.g4d.domain.Prioridad;
import mx.lania.g4d.domain.User;
import mx.lania.g4d.repository.FuncionalidadRepository;
import mx.lania.g4d.service.FuncionalidadService;
import mx.lania.g4d.service.criteria.FuncionalidadCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FuncionalidadResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FuncionalidadResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_URL_GIT_LAB = "AAAAAAAAAA";
    private static final String UPDATED_URL_GIT_LAB = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_ENTREGA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ENTREGA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_ENTREGA = LocalDate.ofEpochDay(-1L);

    private static final Instant DEFAULT_CREADO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREADO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFICADO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFICADO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/funcionalidads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FuncionalidadRepository funcionalidadRepository;

    @Mock
    private FuncionalidadRepository funcionalidadRepositoryMock;

    @Mock
    private FuncionalidadService funcionalidadServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFuncionalidadMockMvc;

    private Funcionalidad funcionalidad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Funcionalidad createEntity(EntityManager em) {
        Funcionalidad funcionalidad = new Funcionalidad()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .urlGitLab(DEFAULT_URL_GIT_LAB)
            .fechaEntrega(DEFAULT_FECHA_ENTREGA)
            .creado(DEFAULT_CREADO)
            .modificado(DEFAULT_MODIFICADO);
        return funcionalidad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Funcionalidad createUpdatedEntity(EntityManager em) {
        Funcionalidad funcionalidad = new Funcionalidad()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .urlGitLab(UPDATED_URL_GIT_LAB)
            .fechaEntrega(UPDATED_FECHA_ENTREGA)
            .creado(UPDATED_CREADO)
            .modificado(UPDATED_MODIFICADO);
        return funcionalidad;
    }

    @BeforeEach
    public void initTest() {
        funcionalidad = createEntity(em);
    }

    @Test
    @Transactional
    void createFuncionalidad() throws Exception {
        int databaseSizeBeforeCreate = funcionalidadRepository.findAll().size();
        // Create the Funcionalidad
        restFuncionalidadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcionalidad)))
            .andExpect(status().isCreated());

        // Validate the Funcionalidad in the database
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeCreate + 1);
        Funcionalidad testFuncionalidad = funcionalidadList.get(funcionalidadList.size() - 1);
        assertThat(testFuncionalidad.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testFuncionalidad.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testFuncionalidad.getUrlGitLab()).isEqualTo(DEFAULT_URL_GIT_LAB);
        assertThat(testFuncionalidad.getFechaEntrega()).isEqualTo(DEFAULT_FECHA_ENTREGA);
        assertThat(testFuncionalidad.getCreado()).isEqualTo(DEFAULT_CREADO);
        assertThat(testFuncionalidad.getModificado()).isEqualTo(DEFAULT_MODIFICADO);
    }

    @Test
    @Transactional
    void createFuncionalidadWithExistingId() throws Exception {
        // Create the Funcionalidad with an existing ID
        funcionalidad.setId(1L);

        int databaseSizeBeforeCreate = funcionalidadRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFuncionalidadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcionalidad)))
            .andExpect(status().isBadRequest());

        // Validate the Funcionalidad in the database
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFuncionalidads() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList
        restFuncionalidadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(funcionalidad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].urlGitLab").value(hasItem(DEFAULT_URL_GIT_LAB)))
            .andExpect(jsonPath("$.[*].fechaEntrega").value(hasItem(DEFAULT_FECHA_ENTREGA.toString())))
            .andExpect(jsonPath("$.[*].creado").value(hasItem(DEFAULT_CREADO.toString())))
            .andExpect(jsonPath("$.[*].modificado").value(hasItem(DEFAULT_MODIFICADO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFuncionalidadsWithEagerRelationshipsIsEnabled() throws Exception {
        when(funcionalidadServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFuncionalidadMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(funcionalidadServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFuncionalidadsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(funcionalidadServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFuncionalidadMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(funcionalidadRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFuncionalidad() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get the funcionalidad
        restFuncionalidadMockMvc
            .perform(get(ENTITY_API_URL_ID, funcionalidad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(funcionalidad.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.urlGitLab").value(DEFAULT_URL_GIT_LAB))
            .andExpect(jsonPath("$.fechaEntrega").value(DEFAULT_FECHA_ENTREGA.toString()))
            .andExpect(jsonPath("$.creado").value(DEFAULT_CREADO.toString()))
            .andExpect(jsonPath("$.modificado").value(DEFAULT_MODIFICADO.toString()));
    }

    @Test
    @Transactional
    void getFuncionalidadsByIdFiltering() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        Long id = funcionalidad.getId();

        defaultFuncionalidadShouldBeFound("id.equals=" + id);
        defaultFuncionalidadShouldNotBeFound("id.notEquals=" + id);

        defaultFuncionalidadShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFuncionalidadShouldNotBeFound("id.greaterThan=" + id);

        defaultFuncionalidadShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFuncionalidadShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where nombre equals to DEFAULT_NOMBRE
        defaultFuncionalidadShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the funcionalidadList where nombre equals to UPDATED_NOMBRE
        defaultFuncionalidadShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultFuncionalidadShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the funcionalidadList where nombre equals to UPDATED_NOMBRE
        defaultFuncionalidadShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where nombre is not null
        defaultFuncionalidadShouldBeFound("nombre.specified=true");

        // Get all the funcionalidadList where nombre is null
        defaultFuncionalidadShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByNombreContainsSomething() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where nombre contains DEFAULT_NOMBRE
        defaultFuncionalidadShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the funcionalidadList where nombre contains UPDATED_NOMBRE
        defaultFuncionalidadShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where nombre does not contain DEFAULT_NOMBRE
        defaultFuncionalidadShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the funcionalidadList where nombre does not contain UPDATED_NOMBRE
        defaultFuncionalidadShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where descripcion equals to DEFAULT_DESCRIPCION
        defaultFuncionalidadShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the funcionalidadList where descripcion equals to UPDATED_DESCRIPCION
        defaultFuncionalidadShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultFuncionalidadShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the funcionalidadList where descripcion equals to UPDATED_DESCRIPCION
        defaultFuncionalidadShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where descripcion is not null
        defaultFuncionalidadShouldBeFound("descripcion.specified=true");

        // Get all the funcionalidadList where descripcion is null
        defaultFuncionalidadShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where descripcion contains DEFAULT_DESCRIPCION
        defaultFuncionalidadShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the funcionalidadList where descripcion contains UPDATED_DESCRIPCION
        defaultFuncionalidadShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultFuncionalidadShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the funcionalidadList where descripcion does not contain UPDATED_DESCRIPCION
        defaultFuncionalidadShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByUrlGitLabIsEqualToSomething() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where urlGitLab equals to DEFAULT_URL_GIT_LAB
        defaultFuncionalidadShouldBeFound("urlGitLab.equals=" + DEFAULT_URL_GIT_LAB);

        // Get all the funcionalidadList where urlGitLab equals to UPDATED_URL_GIT_LAB
        defaultFuncionalidadShouldNotBeFound("urlGitLab.equals=" + UPDATED_URL_GIT_LAB);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByUrlGitLabIsInShouldWork() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where urlGitLab in DEFAULT_URL_GIT_LAB or UPDATED_URL_GIT_LAB
        defaultFuncionalidadShouldBeFound("urlGitLab.in=" + DEFAULT_URL_GIT_LAB + "," + UPDATED_URL_GIT_LAB);

        // Get all the funcionalidadList where urlGitLab equals to UPDATED_URL_GIT_LAB
        defaultFuncionalidadShouldNotBeFound("urlGitLab.in=" + UPDATED_URL_GIT_LAB);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByUrlGitLabIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where urlGitLab is not null
        defaultFuncionalidadShouldBeFound("urlGitLab.specified=true");

        // Get all the funcionalidadList where urlGitLab is null
        defaultFuncionalidadShouldNotBeFound("urlGitLab.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByUrlGitLabContainsSomething() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where urlGitLab contains DEFAULT_URL_GIT_LAB
        defaultFuncionalidadShouldBeFound("urlGitLab.contains=" + DEFAULT_URL_GIT_LAB);

        // Get all the funcionalidadList where urlGitLab contains UPDATED_URL_GIT_LAB
        defaultFuncionalidadShouldNotBeFound("urlGitLab.contains=" + UPDATED_URL_GIT_LAB);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByUrlGitLabNotContainsSomething() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where urlGitLab does not contain DEFAULT_URL_GIT_LAB
        defaultFuncionalidadShouldNotBeFound("urlGitLab.doesNotContain=" + DEFAULT_URL_GIT_LAB);

        // Get all the funcionalidadList where urlGitLab does not contain UPDATED_URL_GIT_LAB
        defaultFuncionalidadShouldBeFound("urlGitLab.doesNotContain=" + UPDATED_URL_GIT_LAB);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByFechaEntregaIsEqualToSomething() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where fechaEntrega equals to DEFAULT_FECHA_ENTREGA
        defaultFuncionalidadShouldBeFound("fechaEntrega.equals=" + DEFAULT_FECHA_ENTREGA);

        // Get all the funcionalidadList where fechaEntrega equals to UPDATED_FECHA_ENTREGA
        defaultFuncionalidadShouldNotBeFound("fechaEntrega.equals=" + UPDATED_FECHA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByFechaEntregaIsInShouldWork() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where fechaEntrega in DEFAULT_FECHA_ENTREGA or UPDATED_FECHA_ENTREGA
        defaultFuncionalidadShouldBeFound("fechaEntrega.in=" + DEFAULT_FECHA_ENTREGA + "," + UPDATED_FECHA_ENTREGA);

        // Get all the funcionalidadList where fechaEntrega equals to UPDATED_FECHA_ENTREGA
        defaultFuncionalidadShouldNotBeFound("fechaEntrega.in=" + UPDATED_FECHA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByFechaEntregaIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where fechaEntrega is not null
        defaultFuncionalidadShouldBeFound("fechaEntrega.specified=true");

        // Get all the funcionalidadList where fechaEntrega is null
        defaultFuncionalidadShouldNotBeFound("fechaEntrega.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByFechaEntregaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where fechaEntrega is greater than or equal to DEFAULT_FECHA_ENTREGA
        defaultFuncionalidadShouldBeFound("fechaEntrega.greaterThanOrEqual=" + DEFAULT_FECHA_ENTREGA);

        // Get all the funcionalidadList where fechaEntrega is greater than or equal to UPDATED_FECHA_ENTREGA
        defaultFuncionalidadShouldNotBeFound("fechaEntrega.greaterThanOrEqual=" + UPDATED_FECHA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByFechaEntregaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where fechaEntrega is less than or equal to DEFAULT_FECHA_ENTREGA
        defaultFuncionalidadShouldBeFound("fechaEntrega.lessThanOrEqual=" + DEFAULT_FECHA_ENTREGA);

        // Get all the funcionalidadList where fechaEntrega is less than or equal to SMALLER_FECHA_ENTREGA
        defaultFuncionalidadShouldNotBeFound("fechaEntrega.lessThanOrEqual=" + SMALLER_FECHA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByFechaEntregaIsLessThanSomething() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where fechaEntrega is less than DEFAULT_FECHA_ENTREGA
        defaultFuncionalidadShouldNotBeFound("fechaEntrega.lessThan=" + DEFAULT_FECHA_ENTREGA);

        // Get all the funcionalidadList where fechaEntrega is less than UPDATED_FECHA_ENTREGA
        defaultFuncionalidadShouldBeFound("fechaEntrega.lessThan=" + UPDATED_FECHA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByFechaEntregaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where fechaEntrega is greater than DEFAULT_FECHA_ENTREGA
        defaultFuncionalidadShouldNotBeFound("fechaEntrega.greaterThan=" + DEFAULT_FECHA_ENTREGA);

        // Get all the funcionalidadList where fechaEntrega is greater than SMALLER_FECHA_ENTREGA
        defaultFuncionalidadShouldBeFound("fechaEntrega.greaterThan=" + SMALLER_FECHA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByCreadoIsEqualToSomething() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where creado equals to DEFAULT_CREADO
        defaultFuncionalidadShouldBeFound("creado.equals=" + DEFAULT_CREADO);

        // Get all the funcionalidadList where creado equals to UPDATED_CREADO
        defaultFuncionalidadShouldNotBeFound("creado.equals=" + UPDATED_CREADO);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByCreadoIsInShouldWork() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where creado in DEFAULT_CREADO or UPDATED_CREADO
        defaultFuncionalidadShouldBeFound("creado.in=" + DEFAULT_CREADO + "," + UPDATED_CREADO);

        // Get all the funcionalidadList where creado equals to UPDATED_CREADO
        defaultFuncionalidadShouldNotBeFound("creado.in=" + UPDATED_CREADO);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByCreadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where creado is not null
        defaultFuncionalidadShouldBeFound("creado.specified=true");

        // Get all the funcionalidadList where creado is null
        defaultFuncionalidadShouldNotBeFound("creado.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByModificadoIsEqualToSomething() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where modificado equals to DEFAULT_MODIFICADO
        defaultFuncionalidadShouldBeFound("modificado.equals=" + DEFAULT_MODIFICADO);

        // Get all the funcionalidadList where modificado equals to UPDATED_MODIFICADO
        defaultFuncionalidadShouldNotBeFound("modificado.equals=" + UPDATED_MODIFICADO);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByModificadoIsInShouldWork() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where modificado in DEFAULT_MODIFICADO or UPDATED_MODIFICADO
        defaultFuncionalidadShouldBeFound("modificado.in=" + DEFAULT_MODIFICADO + "," + UPDATED_MODIFICADO);

        // Get all the funcionalidadList where modificado equals to UPDATED_MODIFICADO
        defaultFuncionalidadShouldNotBeFound("modificado.in=" + UPDATED_MODIFICADO);
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByModificadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList where modificado is not null
        defaultFuncionalidadShouldBeFound("modificado.specified=true");

        // Get all the funcionalidadList where modificado is null
        defaultFuncionalidadShouldNotBeFound("modificado.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            funcionalidadRepository.saveAndFlush(funcionalidad);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        funcionalidad.addUser(user);
        funcionalidadRepository.saveAndFlush(funcionalidad);
        Long userId = user.getId();

        // Get all the funcionalidadList where user equals to userId
        defaultFuncionalidadShouldBeFound("userId.equals=" + userId);

        // Get all the funcionalidadList where user equals to (userId + 1)
        defaultFuncionalidadShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByEstatusFuncionalidadIsEqualToSomething() throws Exception {
        EstatusFuncionalidad estatusFuncionalidad;
        if (TestUtil.findAll(em, EstatusFuncionalidad.class).isEmpty()) {
            funcionalidadRepository.saveAndFlush(funcionalidad);
            estatusFuncionalidad = EstatusFuncionalidadResourceIT.createEntity(em);
        } else {
            estatusFuncionalidad = TestUtil.findAll(em, EstatusFuncionalidad.class).get(0);
        }
        em.persist(estatusFuncionalidad);
        em.flush();
        funcionalidad.setEstatusFuncionalidad(estatusFuncionalidad);
        funcionalidadRepository.saveAndFlush(funcionalidad);
        Long estatusFuncionalidadId = estatusFuncionalidad.getId();

        // Get all the funcionalidadList where estatusFuncionalidad equals to estatusFuncionalidadId
        defaultFuncionalidadShouldBeFound("estatusFuncionalidadId.equals=" + estatusFuncionalidadId);

        // Get all the funcionalidadList where estatusFuncionalidad equals to (estatusFuncionalidadId + 1)
        defaultFuncionalidadShouldNotBeFound("estatusFuncionalidadId.equals=" + (estatusFuncionalidadId + 1));
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByIteracionIsEqualToSomething() throws Exception {
        Iteracion iteracion;
        if (TestUtil.findAll(em, Iteracion.class).isEmpty()) {
            funcionalidadRepository.saveAndFlush(funcionalidad);
            iteracion = IteracionResourceIT.createEntity(em);
        } else {
            iteracion = TestUtil.findAll(em, Iteracion.class).get(0);
        }
        em.persist(iteracion);
        em.flush();
        funcionalidad.setIteracion(iteracion);
        funcionalidadRepository.saveAndFlush(funcionalidad);
        Long iteracionId = iteracion.getId();

        // Get all the funcionalidadList where iteracion equals to iteracionId
        defaultFuncionalidadShouldBeFound("iteracionId.equals=" + iteracionId);

        // Get all the funcionalidadList where iteracion equals to (iteracionId + 1)
        defaultFuncionalidadShouldNotBeFound("iteracionId.equals=" + (iteracionId + 1));
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByPrioridadIsEqualToSomething() throws Exception {
        Prioridad prioridad;
        if (TestUtil.findAll(em, Prioridad.class).isEmpty()) {
            funcionalidadRepository.saveAndFlush(funcionalidad);
            prioridad = PrioridadResourceIT.createEntity(em);
        } else {
            prioridad = TestUtil.findAll(em, Prioridad.class).get(0);
        }
        em.persist(prioridad);
        em.flush();
        funcionalidad.setPrioridad(prioridad);
        funcionalidadRepository.saveAndFlush(funcionalidad);
        Long prioridadId = prioridad.getId();

        // Get all the funcionalidadList where prioridad equals to prioridadId
        defaultFuncionalidadShouldBeFound("prioridadId.equals=" + prioridadId);

        // Get all the funcionalidadList where prioridad equals to (prioridadId + 1)
        defaultFuncionalidadShouldNotBeFound("prioridadId.equals=" + (prioridadId + 1));
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByEtiquetaIsEqualToSomething() throws Exception {
        Etiqueta etiqueta;
        if (TestUtil.findAll(em, Etiqueta.class).isEmpty()) {
            funcionalidadRepository.saveAndFlush(funcionalidad);
            etiqueta = EtiquetaResourceIT.createEntity(em);
        } else {
            etiqueta = TestUtil.findAll(em, Etiqueta.class).get(0);
        }
        em.persist(etiqueta);
        em.flush();
        funcionalidad.addEtiqueta(etiqueta);
        funcionalidadRepository.saveAndFlush(funcionalidad);
        Long etiquetaId = etiqueta.getId();

        // Get all the funcionalidadList where etiqueta equals to etiquetaId
        defaultFuncionalidadShouldBeFound("etiquetaId.equals=" + etiquetaId);

        // Get all the funcionalidadList where etiqueta equals to (etiquetaId + 1)
        defaultFuncionalidadShouldNotBeFound("etiquetaId.equals=" + (etiquetaId + 1));
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByAtributoFuncionalidadIsEqualToSomething() throws Exception {
        AtributoFuncionalidad atributoFuncionalidad;
        if (TestUtil.findAll(em, AtributoFuncionalidad.class).isEmpty()) {
            funcionalidadRepository.saveAndFlush(funcionalidad);
            atributoFuncionalidad = AtributoFuncionalidadResourceIT.createEntity(em);
        } else {
            atributoFuncionalidad = TestUtil.findAll(em, AtributoFuncionalidad.class).get(0);
        }
        em.persist(atributoFuncionalidad);
        em.flush();
        funcionalidad.addAtributoFuncionalidad(atributoFuncionalidad);
        funcionalidadRepository.saveAndFlush(funcionalidad);
        Long atributoFuncionalidadId = atributoFuncionalidad.getId();

        // Get all the funcionalidadList where atributoFuncionalidad equals to atributoFuncionalidadId
        defaultFuncionalidadShouldBeFound("atributoFuncionalidadId.equals=" + atributoFuncionalidadId);

        // Get all the funcionalidadList where atributoFuncionalidad equals to (atributoFuncionalidadId + 1)
        defaultFuncionalidadShouldNotBeFound("atributoFuncionalidadId.equals=" + (atributoFuncionalidadId + 1));
    }

    @Test
    @Transactional
    void getAllFuncionalidadsByComentarioIsEqualToSomething() throws Exception {
        Comentario comentario;
        if (TestUtil.findAll(em, Comentario.class).isEmpty()) {
            funcionalidadRepository.saveAndFlush(funcionalidad);
            comentario = ComentarioResourceIT.createEntity(em);
        } else {
            comentario = TestUtil.findAll(em, Comentario.class).get(0);
        }
        em.persist(comentario);
        em.flush();
        funcionalidad.addComentario(comentario);
        funcionalidadRepository.saveAndFlush(funcionalidad);
        Long comentarioId = comentario.getId();

        // Get all the funcionalidadList where comentario equals to comentarioId
        defaultFuncionalidadShouldBeFound("comentarioId.equals=" + comentarioId);

        // Get all the funcionalidadList where comentario equals to (comentarioId + 1)
        defaultFuncionalidadShouldNotBeFound("comentarioId.equals=" + (comentarioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFuncionalidadShouldBeFound(String filter) throws Exception {
        restFuncionalidadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(funcionalidad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].urlGitLab").value(hasItem(DEFAULT_URL_GIT_LAB)))
            .andExpect(jsonPath("$.[*].fechaEntrega").value(hasItem(DEFAULT_FECHA_ENTREGA.toString())))
            .andExpect(jsonPath("$.[*].creado").value(hasItem(DEFAULT_CREADO.toString())))
            .andExpect(jsonPath("$.[*].modificado").value(hasItem(DEFAULT_MODIFICADO.toString())));

        // Check, that the count call also returns 1
        restFuncionalidadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFuncionalidadShouldNotBeFound(String filter) throws Exception {
        restFuncionalidadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFuncionalidadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFuncionalidad() throws Exception {
        // Get the funcionalidad
        restFuncionalidadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFuncionalidad() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        int databaseSizeBeforeUpdate = funcionalidadRepository.findAll().size();

        // Update the funcionalidad
        Funcionalidad updatedFuncionalidad = funcionalidadRepository.findById(funcionalidad.getId()).get();
        // Disconnect from session so that the updates on updatedFuncionalidad are not directly saved in db
        em.detach(updatedFuncionalidad);
        updatedFuncionalidad
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .urlGitLab(UPDATED_URL_GIT_LAB)
            .fechaEntrega(UPDATED_FECHA_ENTREGA)
            .creado(UPDATED_CREADO)
            .modificado(UPDATED_MODIFICADO);

        restFuncionalidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFuncionalidad.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFuncionalidad))
            )
            .andExpect(status().isOk());

        // Validate the Funcionalidad in the database
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeUpdate);
        Funcionalidad testFuncionalidad = funcionalidadList.get(funcionalidadList.size() - 1);
        assertThat(testFuncionalidad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testFuncionalidad.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testFuncionalidad.getUrlGitLab()).isEqualTo(UPDATED_URL_GIT_LAB);
        assertThat(testFuncionalidad.getFechaEntrega()).isEqualTo(UPDATED_FECHA_ENTREGA);
        assertThat(testFuncionalidad.getCreado()).isEqualTo(UPDATED_CREADO);
        assertThat(testFuncionalidad.getModificado()).isEqualTo(UPDATED_MODIFICADO);
    }

    @Test
    @Transactional
    void putNonExistingFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = funcionalidadRepository.findAll().size();
        funcionalidad.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFuncionalidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, funcionalidad.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(funcionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funcionalidad in the database
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = funcionalidadRepository.findAll().size();
        funcionalidad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncionalidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(funcionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funcionalidad in the database
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = funcionalidadRepository.findAll().size();
        funcionalidad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncionalidadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcionalidad)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Funcionalidad in the database
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFuncionalidadWithPatch() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        int databaseSizeBeforeUpdate = funcionalidadRepository.findAll().size();

        // Update the funcionalidad using partial update
        Funcionalidad partialUpdatedFuncionalidad = new Funcionalidad();
        partialUpdatedFuncionalidad.setId(funcionalidad.getId());

        partialUpdatedFuncionalidad.nombre(UPDATED_NOMBRE).urlGitLab(UPDATED_URL_GIT_LAB);

        restFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFuncionalidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFuncionalidad))
            )
            .andExpect(status().isOk());

        // Validate the Funcionalidad in the database
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeUpdate);
        Funcionalidad testFuncionalidad = funcionalidadList.get(funcionalidadList.size() - 1);
        assertThat(testFuncionalidad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testFuncionalidad.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testFuncionalidad.getUrlGitLab()).isEqualTo(UPDATED_URL_GIT_LAB);
        assertThat(testFuncionalidad.getFechaEntrega()).isEqualTo(DEFAULT_FECHA_ENTREGA);
        assertThat(testFuncionalidad.getCreado()).isEqualTo(DEFAULT_CREADO);
        assertThat(testFuncionalidad.getModificado()).isEqualTo(DEFAULT_MODIFICADO);
    }

    @Test
    @Transactional
    void fullUpdateFuncionalidadWithPatch() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        int databaseSizeBeforeUpdate = funcionalidadRepository.findAll().size();

        // Update the funcionalidad using partial update
        Funcionalidad partialUpdatedFuncionalidad = new Funcionalidad();
        partialUpdatedFuncionalidad.setId(funcionalidad.getId());

        partialUpdatedFuncionalidad
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .urlGitLab(UPDATED_URL_GIT_LAB)
            .fechaEntrega(UPDATED_FECHA_ENTREGA)
            .creado(UPDATED_CREADO)
            .modificado(UPDATED_MODIFICADO);

        restFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFuncionalidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFuncionalidad))
            )
            .andExpect(status().isOk());

        // Validate the Funcionalidad in the database
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeUpdate);
        Funcionalidad testFuncionalidad = funcionalidadList.get(funcionalidadList.size() - 1);
        assertThat(testFuncionalidad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testFuncionalidad.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testFuncionalidad.getUrlGitLab()).isEqualTo(UPDATED_URL_GIT_LAB);
        assertThat(testFuncionalidad.getFechaEntrega()).isEqualTo(UPDATED_FECHA_ENTREGA);
        assertThat(testFuncionalidad.getCreado()).isEqualTo(UPDATED_CREADO);
        assertThat(testFuncionalidad.getModificado()).isEqualTo(UPDATED_MODIFICADO);
    }

    @Test
    @Transactional
    void patchNonExistingFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = funcionalidadRepository.findAll().size();
        funcionalidad.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, funcionalidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(funcionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funcionalidad in the database
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = funcionalidadRepository.findAll().size();
        funcionalidad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(funcionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funcionalidad in the database
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = funcionalidadRepository.findAll().size();
        funcionalidad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(funcionalidad))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Funcionalidad in the database
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFuncionalidad() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        int databaseSizeBeforeDelete = funcionalidadRepository.findAll().size();

        // Delete the funcionalidad
        restFuncionalidadMockMvc
            .perform(delete(ENTITY_API_URL_ID, funcionalidad.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
