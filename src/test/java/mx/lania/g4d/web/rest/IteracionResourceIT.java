package mx.lania.g4d.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import mx.lania.g4d.IntegrationTest;
import mx.lania.g4d.domain.Funcionalidad;
import mx.lania.g4d.domain.Iteracion;
import mx.lania.g4d.domain.Proyecto;
import mx.lania.g4d.repository.IteracionRepository;
import mx.lania.g4d.service.criteria.IteracionCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link IteracionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IteracionResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INICIO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_INICIO = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FIN = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/iteracions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IteracionRepository iteracionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIteracionMockMvc;

    private Iteracion iteracion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Iteracion createEntity(EntityManager em) {
        Iteracion iteracion = new Iteracion().nombre(DEFAULT_NOMBRE).inicio(DEFAULT_INICIO).fin(DEFAULT_FIN);
        return iteracion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Iteracion createUpdatedEntity(EntityManager em) {
        Iteracion iteracion = new Iteracion().nombre(UPDATED_NOMBRE).inicio(UPDATED_INICIO).fin(UPDATED_FIN);
        return iteracion;
    }

    @BeforeEach
    public void initTest() {
        iteracion = createEntity(em);
    }

    @Test
    @Transactional
    void createIteracion() throws Exception {
        int databaseSizeBeforeCreate = iteracionRepository.findAll().size();
        // Create the Iteracion
        restIteracionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(iteracion)))
            .andExpect(status().isCreated());

        // Validate the Iteracion in the database
        List<Iteracion> iteracionList = iteracionRepository.findAll();
        assertThat(iteracionList).hasSize(databaseSizeBeforeCreate + 1);
        Iteracion testIteracion = iteracionList.get(iteracionList.size() - 1);
        assertThat(testIteracion.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testIteracion.getInicio()).isEqualTo(DEFAULT_INICIO);
        assertThat(testIteracion.getFin()).isEqualTo(DEFAULT_FIN);
    }

    @Test
    @Transactional
    void createIteracionWithExistingId() throws Exception {
        // Create the Iteracion with an existing ID
        iteracion.setId(1L);

        int databaseSizeBeforeCreate = iteracionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIteracionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(iteracion)))
            .andExpect(status().isBadRequest());

        // Validate the Iteracion in the database
        List<Iteracion> iteracionList = iteracionRepository.findAll();
        assertThat(iteracionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = iteracionRepository.findAll().size();
        // set the field null
        iteracion.setNombre(null);

        // Create the Iteracion, which fails.

        restIteracionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(iteracion)))
            .andExpect(status().isBadRequest());

        List<Iteracion> iteracionList = iteracionRepository.findAll();
        assertThat(iteracionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIteracions() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        // Get all the iteracionList
        restIteracionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iteracion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].inicio").value(hasItem(DEFAULT_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fin").value(hasItem(DEFAULT_FIN.toString())));
    }

    @Test
    @Transactional
    void getIteracion() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        // Get the iteracion
        restIteracionMockMvc
            .perform(get(ENTITY_API_URL_ID, iteracion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(iteracion.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.inicio").value(DEFAULT_INICIO.toString()))
            .andExpect(jsonPath("$.fin").value(DEFAULT_FIN.toString()));
    }

    @Test
    @Transactional
    void getIteracionsByIdFiltering() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        Long id = iteracion.getId();

        defaultIteracionShouldBeFound("id.equals=" + id);
        defaultIteracionShouldNotBeFound("id.notEquals=" + id);

        defaultIteracionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultIteracionShouldNotBeFound("id.greaterThan=" + id);

        defaultIteracionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultIteracionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIteracionsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        // Get all the iteracionList where nombre equals to DEFAULT_NOMBRE
        defaultIteracionShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the iteracionList where nombre equals to UPDATED_NOMBRE
        defaultIteracionShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllIteracionsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        // Get all the iteracionList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultIteracionShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the iteracionList where nombre equals to UPDATED_NOMBRE
        defaultIteracionShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllIteracionsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        // Get all the iteracionList where nombre is not null
        defaultIteracionShouldBeFound("nombre.specified=true");

        // Get all the iteracionList where nombre is null
        defaultIteracionShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllIteracionsByNombreContainsSomething() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        // Get all the iteracionList where nombre contains DEFAULT_NOMBRE
        defaultIteracionShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the iteracionList where nombre contains UPDATED_NOMBRE
        defaultIteracionShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllIteracionsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        // Get all the iteracionList where nombre does not contain DEFAULT_NOMBRE
        defaultIteracionShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the iteracionList where nombre does not contain UPDATED_NOMBRE
        defaultIteracionShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllIteracionsByInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        // Get all the iteracionList where inicio equals to DEFAULT_INICIO
        defaultIteracionShouldBeFound("inicio.equals=" + DEFAULT_INICIO);

        // Get all the iteracionList where inicio equals to UPDATED_INICIO
        defaultIteracionShouldNotBeFound("inicio.equals=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    void getAllIteracionsByInicioIsInShouldWork() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        // Get all the iteracionList where inicio in DEFAULT_INICIO or UPDATED_INICIO
        defaultIteracionShouldBeFound("inicio.in=" + DEFAULT_INICIO + "," + UPDATED_INICIO);

        // Get all the iteracionList where inicio equals to UPDATED_INICIO
        defaultIteracionShouldNotBeFound("inicio.in=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    void getAllIteracionsByInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        // Get all the iteracionList where inicio is not null
        defaultIteracionShouldBeFound("inicio.specified=true");

        // Get all the iteracionList where inicio is null
        defaultIteracionShouldNotBeFound("inicio.specified=false");
    }

    @Test
    @Transactional
    void getAllIteracionsByInicioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        // Get all the iteracionList where inicio is greater than or equal to DEFAULT_INICIO
        defaultIteracionShouldBeFound("inicio.greaterThanOrEqual=" + DEFAULT_INICIO);

        // Get all the iteracionList where inicio is greater than or equal to UPDATED_INICIO
        defaultIteracionShouldNotBeFound("inicio.greaterThanOrEqual=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    void getAllIteracionsByInicioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        // Get all the iteracionList where inicio is less than or equal to DEFAULT_INICIO
        defaultIteracionShouldBeFound("inicio.lessThanOrEqual=" + DEFAULT_INICIO);

        // Get all the iteracionList where inicio is less than or equal to SMALLER_INICIO
        defaultIteracionShouldNotBeFound("inicio.lessThanOrEqual=" + SMALLER_INICIO);
    }

    @Test
    @Transactional
    void getAllIteracionsByInicioIsLessThanSomething() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        // Get all the iteracionList where inicio is less than DEFAULT_INICIO
        defaultIteracionShouldNotBeFound("inicio.lessThan=" + DEFAULT_INICIO);

        // Get all the iteracionList where inicio is less than UPDATED_INICIO
        defaultIteracionShouldBeFound("inicio.lessThan=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    void getAllIteracionsByInicioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        // Get all the iteracionList where inicio is greater than DEFAULT_INICIO
        defaultIteracionShouldNotBeFound("inicio.greaterThan=" + DEFAULT_INICIO);

        // Get all the iteracionList where inicio is greater than SMALLER_INICIO
        defaultIteracionShouldBeFound("inicio.greaterThan=" + SMALLER_INICIO);
    }

    @Test
    @Transactional
    void getAllIteracionsByFinIsEqualToSomething() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        // Get all the iteracionList where fin equals to DEFAULT_FIN
        defaultIteracionShouldBeFound("fin.equals=" + DEFAULT_FIN);

        // Get all the iteracionList where fin equals to UPDATED_FIN
        defaultIteracionShouldNotBeFound("fin.equals=" + UPDATED_FIN);
    }

    @Test
    @Transactional
    void getAllIteracionsByFinIsInShouldWork() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        // Get all the iteracionList where fin in DEFAULT_FIN or UPDATED_FIN
        defaultIteracionShouldBeFound("fin.in=" + DEFAULT_FIN + "," + UPDATED_FIN);

        // Get all the iteracionList where fin equals to UPDATED_FIN
        defaultIteracionShouldNotBeFound("fin.in=" + UPDATED_FIN);
    }

    @Test
    @Transactional
    void getAllIteracionsByFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        // Get all the iteracionList where fin is not null
        defaultIteracionShouldBeFound("fin.specified=true");

        // Get all the iteracionList where fin is null
        defaultIteracionShouldNotBeFound("fin.specified=false");
    }

    @Test
    @Transactional
    void getAllIteracionsByFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        // Get all the iteracionList where fin is greater than or equal to DEFAULT_FIN
        defaultIteracionShouldBeFound("fin.greaterThanOrEqual=" + DEFAULT_FIN);

        // Get all the iteracionList where fin is greater than or equal to UPDATED_FIN
        defaultIteracionShouldNotBeFound("fin.greaterThanOrEqual=" + UPDATED_FIN);
    }

    @Test
    @Transactional
    void getAllIteracionsByFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        // Get all the iteracionList where fin is less than or equal to DEFAULT_FIN
        defaultIteracionShouldBeFound("fin.lessThanOrEqual=" + DEFAULT_FIN);

        // Get all the iteracionList where fin is less than or equal to SMALLER_FIN
        defaultIteracionShouldNotBeFound("fin.lessThanOrEqual=" + SMALLER_FIN);
    }

    @Test
    @Transactional
    void getAllIteracionsByFinIsLessThanSomething() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        // Get all the iteracionList where fin is less than DEFAULT_FIN
        defaultIteracionShouldNotBeFound("fin.lessThan=" + DEFAULT_FIN);

        // Get all the iteracionList where fin is less than UPDATED_FIN
        defaultIteracionShouldBeFound("fin.lessThan=" + UPDATED_FIN);
    }

    @Test
    @Transactional
    void getAllIteracionsByFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        // Get all the iteracionList where fin is greater than DEFAULT_FIN
        defaultIteracionShouldNotBeFound("fin.greaterThan=" + DEFAULT_FIN);

        // Get all the iteracionList where fin is greater than SMALLER_FIN
        defaultIteracionShouldBeFound("fin.greaterThan=" + SMALLER_FIN);
    }

    @Test
    @Transactional
    void getAllIteracionsByFuncionalidadIsEqualToSomething() throws Exception {
        Funcionalidad funcionalidad;
        if (TestUtil.findAll(em, Funcionalidad.class).isEmpty()) {
            iteracionRepository.saveAndFlush(iteracion);
            funcionalidad = FuncionalidadResourceIT.createEntity(em);
        } else {
            funcionalidad = TestUtil.findAll(em, Funcionalidad.class).get(0);
        }
        em.persist(funcionalidad);
        em.flush();
        iteracion.addFuncionalidad(funcionalidad);
        iteracionRepository.saveAndFlush(iteracion);
        Long funcionalidadId = funcionalidad.getId();

        // Get all the iteracionList where funcionalidad equals to funcionalidadId
        defaultIteracionShouldBeFound("funcionalidadId.equals=" + funcionalidadId);

        // Get all the iteracionList where funcionalidad equals to (funcionalidadId + 1)
        defaultIteracionShouldNotBeFound("funcionalidadId.equals=" + (funcionalidadId + 1));
    }

    @Test
    @Transactional
    void getAllIteracionsByProyectoIsEqualToSomething() throws Exception {
        Proyecto proyecto;
        if (TestUtil.findAll(em, Proyecto.class).isEmpty()) {
            iteracionRepository.saveAndFlush(iteracion);
            proyecto = ProyectoResourceIT.createEntity(em);
        } else {
            proyecto = TestUtil.findAll(em, Proyecto.class).get(0);
        }
        em.persist(proyecto);
        em.flush();
        iteracion.setProyecto(proyecto);
        iteracionRepository.saveAndFlush(iteracion);
        Long proyectoId = proyecto.getId();

        // Get all the iteracionList where proyecto equals to proyectoId
        defaultIteracionShouldBeFound("proyectoId.equals=" + proyectoId);

        // Get all the iteracionList where proyecto equals to (proyectoId + 1)
        defaultIteracionShouldNotBeFound("proyectoId.equals=" + (proyectoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIteracionShouldBeFound(String filter) throws Exception {
        restIteracionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iteracion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].inicio").value(hasItem(DEFAULT_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fin").value(hasItem(DEFAULT_FIN.toString())));

        // Check, that the count call also returns 1
        restIteracionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIteracionShouldNotBeFound(String filter) throws Exception {
        restIteracionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIteracionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIteracion() throws Exception {
        // Get the iteracion
        restIteracionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIteracion() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        int databaseSizeBeforeUpdate = iteracionRepository.findAll().size();

        // Update the iteracion
        Iteracion updatedIteracion = iteracionRepository.findById(iteracion.getId()).get();
        // Disconnect from session so that the updates on updatedIteracion are not directly saved in db
        em.detach(updatedIteracion);
        updatedIteracion.nombre(UPDATED_NOMBRE).inicio(UPDATED_INICIO).fin(UPDATED_FIN);

        restIteracionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIteracion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIteracion))
            )
            .andExpect(status().isOk());

        // Validate the Iteracion in the database
        List<Iteracion> iteracionList = iteracionRepository.findAll();
        assertThat(iteracionList).hasSize(databaseSizeBeforeUpdate);
        Iteracion testIteracion = iteracionList.get(iteracionList.size() - 1);
        assertThat(testIteracion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testIteracion.getInicio()).isEqualTo(UPDATED_INICIO);
        assertThat(testIteracion.getFin()).isEqualTo(UPDATED_FIN);
    }

    @Test
    @Transactional
    void putNonExistingIteracion() throws Exception {
        int databaseSizeBeforeUpdate = iteracionRepository.findAll().size();
        iteracion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIteracionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, iteracion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iteracion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Iteracion in the database
        List<Iteracion> iteracionList = iteracionRepository.findAll();
        assertThat(iteracionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIteracion() throws Exception {
        int databaseSizeBeforeUpdate = iteracionRepository.findAll().size();
        iteracion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIteracionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iteracion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Iteracion in the database
        List<Iteracion> iteracionList = iteracionRepository.findAll();
        assertThat(iteracionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIteracion() throws Exception {
        int databaseSizeBeforeUpdate = iteracionRepository.findAll().size();
        iteracion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIteracionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(iteracion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Iteracion in the database
        List<Iteracion> iteracionList = iteracionRepository.findAll();
        assertThat(iteracionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIteracionWithPatch() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        int databaseSizeBeforeUpdate = iteracionRepository.findAll().size();

        // Update the iteracion using partial update
        Iteracion partialUpdatedIteracion = new Iteracion();
        partialUpdatedIteracion.setId(iteracion.getId());

        restIteracionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIteracion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIteracion))
            )
            .andExpect(status().isOk());

        // Validate the Iteracion in the database
        List<Iteracion> iteracionList = iteracionRepository.findAll();
        assertThat(iteracionList).hasSize(databaseSizeBeforeUpdate);
        Iteracion testIteracion = iteracionList.get(iteracionList.size() - 1);
        assertThat(testIteracion.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testIteracion.getInicio()).isEqualTo(DEFAULT_INICIO);
        assertThat(testIteracion.getFin()).isEqualTo(DEFAULT_FIN);
    }

    @Test
    @Transactional
    void fullUpdateIteracionWithPatch() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        int databaseSizeBeforeUpdate = iteracionRepository.findAll().size();

        // Update the iteracion using partial update
        Iteracion partialUpdatedIteracion = new Iteracion();
        partialUpdatedIteracion.setId(iteracion.getId());

        partialUpdatedIteracion.nombre(UPDATED_NOMBRE).inicio(UPDATED_INICIO).fin(UPDATED_FIN);

        restIteracionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIteracion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIteracion))
            )
            .andExpect(status().isOk());

        // Validate the Iteracion in the database
        List<Iteracion> iteracionList = iteracionRepository.findAll();
        assertThat(iteracionList).hasSize(databaseSizeBeforeUpdate);
        Iteracion testIteracion = iteracionList.get(iteracionList.size() - 1);
        assertThat(testIteracion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testIteracion.getInicio()).isEqualTo(UPDATED_INICIO);
        assertThat(testIteracion.getFin()).isEqualTo(UPDATED_FIN);
    }

    @Test
    @Transactional
    void patchNonExistingIteracion() throws Exception {
        int databaseSizeBeforeUpdate = iteracionRepository.findAll().size();
        iteracion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIteracionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, iteracion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(iteracion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Iteracion in the database
        List<Iteracion> iteracionList = iteracionRepository.findAll();
        assertThat(iteracionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIteracion() throws Exception {
        int databaseSizeBeforeUpdate = iteracionRepository.findAll().size();
        iteracion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIteracionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(iteracion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Iteracion in the database
        List<Iteracion> iteracionList = iteracionRepository.findAll();
        assertThat(iteracionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIteracion() throws Exception {
        int databaseSizeBeforeUpdate = iteracionRepository.findAll().size();
        iteracion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIteracionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(iteracion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Iteracion in the database
        List<Iteracion> iteracionList = iteracionRepository.findAll();
        assertThat(iteracionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIteracion() throws Exception {
        // Initialize the database
        iteracionRepository.saveAndFlush(iteracion);

        int databaseSizeBeforeDelete = iteracionRepository.findAll().size();

        // Delete the iteracion
        restIteracionMockMvc
            .perform(delete(ENTITY_API_URL_ID, iteracion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Iteracion> iteracionList = iteracionRepository.findAll();
        assertThat(iteracionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
