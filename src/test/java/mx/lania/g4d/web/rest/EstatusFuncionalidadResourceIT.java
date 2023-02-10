package mx.lania.g4d.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import mx.lania.g4d.IntegrationTest;
import mx.lania.g4d.domain.EstatusFuncionalidad;
import mx.lania.g4d.domain.Funcionalidad;
import mx.lania.g4d.repository.EstatusFuncionalidadRepository;
import mx.lania.g4d.service.criteria.EstatusFuncionalidadCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EstatusFuncionalidadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EstatusFuncionalidadResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIORIDAD = 1;
    private static final Integer UPDATED_PRIORIDAD = 2;
    private static final Integer SMALLER_PRIORIDAD = 1 - 1;

    private static final String ENTITY_API_URL = "/api/estatus-funcionalidads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EstatusFuncionalidadRepository estatusFuncionalidadRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEstatusFuncionalidadMockMvc;

    private EstatusFuncionalidad estatusFuncionalidad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EstatusFuncionalidad createEntity(EntityManager em) {
        EstatusFuncionalidad estatusFuncionalidad = new EstatusFuncionalidad().nombre(DEFAULT_NOMBRE).prioridad(DEFAULT_PRIORIDAD);
        return estatusFuncionalidad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EstatusFuncionalidad createUpdatedEntity(EntityManager em) {
        EstatusFuncionalidad estatusFuncionalidad = new EstatusFuncionalidad().nombre(UPDATED_NOMBRE).prioridad(UPDATED_PRIORIDAD);
        return estatusFuncionalidad;
    }

    @BeforeEach
    public void initTest() {
        estatusFuncionalidad = createEntity(em);
    }

    @Test
    @Transactional
    void createEstatusFuncionalidad() throws Exception {
        int databaseSizeBeforeCreate = estatusFuncionalidadRepository.findAll().size();
        // Create the EstatusFuncionalidad
        restEstatusFuncionalidadMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(estatusFuncionalidad))
            )
            .andExpect(status().isCreated());

        // Validate the EstatusFuncionalidad in the database
        List<EstatusFuncionalidad> estatusFuncionalidadList = estatusFuncionalidadRepository.findAll();
        assertThat(estatusFuncionalidadList).hasSize(databaseSizeBeforeCreate + 1);
        EstatusFuncionalidad testEstatusFuncionalidad = estatusFuncionalidadList.get(estatusFuncionalidadList.size() - 1);
        assertThat(testEstatusFuncionalidad.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEstatusFuncionalidad.getPrioridad()).isEqualTo(DEFAULT_PRIORIDAD);
    }

    @Test
    @Transactional
    void createEstatusFuncionalidadWithExistingId() throws Exception {
        // Create the EstatusFuncionalidad with an existing ID
        estatusFuncionalidad.setId(1L);

        int databaseSizeBeforeCreate = estatusFuncionalidadRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEstatusFuncionalidadMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(estatusFuncionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the EstatusFuncionalidad in the database
        List<EstatusFuncionalidad> estatusFuncionalidadList = estatusFuncionalidadRepository.findAll();
        assertThat(estatusFuncionalidadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEstatusFuncionalidads() throws Exception {
        // Initialize the database
        estatusFuncionalidadRepository.saveAndFlush(estatusFuncionalidad);

        // Get all the estatusFuncionalidadList
        restEstatusFuncionalidadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estatusFuncionalidad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].prioridad").value(hasItem(DEFAULT_PRIORIDAD)));
    }

    @Test
    @Transactional
    void getEstatusFuncionalidad() throws Exception {
        // Initialize the database
        estatusFuncionalidadRepository.saveAndFlush(estatusFuncionalidad);

        // Get the estatusFuncionalidad
        restEstatusFuncionalidadMockMvc
            .perform(get(ENTITY_API_URL_ID, estatusFuncionalidad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(estatusFuncionalidad.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.prioridad").value(DEFAULT_PRIORIDAD));
    }

    @Test
    @Transactional
    void getEstatusFuncionalidadsByIdFiltering() throws Exception {
        // Initialize the database
        estatusFuncionalidadRepository.saveAndFlush(estatusFuncionalidad);

        Long id = estatusFuncionalidad.getId();

        defaultEstatusFuncionalidadShouldBeFound("id.equals=" + id);
        defaultEstatusFuncionalidadShouldNotBeFound("id.notEquals=" + id);

        defaultEstatusFuncionalidadShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEstatusFuncionalidadShouldNotBeFound("id.greaterThan=" + id);

        defaultEstatusFuncionalidadShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEstatusFuncionalidadShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEstatusFuncionalidadsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        estatusFuncionalidadRepository.saveAndFlush(estatusFuncionalidad);

        // Get all the estatusFuncionalidadList where nombre equals to DEFAULT_NOMBRE
        defaultEstatusFuncionalidadShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the estatusFuncionalidadList where nombre equals to UPDATED_NOMBRE
        defaultEstatusFuncionalidadShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstatusFuncionalidadsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        estatusFuncionalidadRepository.saveAndFlush(estatusFuncionalidad);

        // Get all the estatusFuncionalidadList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultEstatusFuncionalidadShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the estatusFuncionalidadList where nombre equals to UPDATED_NOMBRE
        defaultEstatusFuncionalidadShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstatusFuncionalidadsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        estatusFuncionalidadRepository.saveAndFlush(estatusFuncionalidad);

        // Get all the estatusFuncionalidadList where nombre is not null
        defaultEstatusFuncionalidadShouldBeFound("nombre.specified=true");

        // Get all the estatusFuncionalidadList where nombre is null
        defaultEstatusFuncionalidadShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllEstatusFuncionalidadsByNombreContainsSomething() throws Exception {
        // Initialize the database
        estatusFuncionalidadRepository.saveAndFlush(estatusFuncionalidad);

        // Get all the estatusFuncionalidadList where nombre contains DEFAULT_NOMBRE
        defaultEstatusFuncionalidadShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the estatusFuncionalidadList where nombre contains UPDATED_NOMBRE
        defaultEstatusFuncionalidadShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstatusFuncionalidadsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        estatusFuncionalidadRepository.saveAndFlush(estatusFuncionalidad);

        // Get all the estatusFuncionalidadList where nombre does not contain DEFAULT_NOMBRE
        defaultEstatusFuncionalidadShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the estatusFuncionalidadList where nombre does not contain UPDATED_NOMBRE
        defaultEstatusFuncionalidadShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstatusFuncionalidadsByPrioridadIsEqualToSomething() throws Exception {
        // Initialize the database
        estatusFuncionalidadRepository.saveAndFlush(estatusFuncionalidad);

        // Get all the estatusFuncionalidadList where prioridad equals to DEFAULT_PRIORIDAD
        defaultEstatusFuncionalidadShouldBeFound("prioridad.equals=" + DEFAULT_PRIORIDAD);

        // Get all the estatusFuncionalidadList where prioridad equals to UPDATED_PRIORIDAD
        defaultEstatusFuncionalidadShouldNotBeFound("prioridad.equals=" + UPDATED_PRIORIDAD);
    }

    @Test
    @Transactional
    void getAllEstatusFuncionalidadsByPrioridadIsInShouldWork() throws Exception {
        // Initialize the database
        estatusFuncionalidadRepository.saveAndFlush(estatusFuncionalidad);

        // Get all the estatusFuncionalidadList where prioridad in DEFAULT_PRIORIDAD or UPDATED_PRIORIDAD
        defaultEstatusFuncionalidadShouldBeFound("prioridad.in=" + DEFAULT_PRIORIDAD + "," + UPDATED_PRIORIDAD);

        // Get all the estatusFuncionalidadList where prioridad equals to UPDATED_PRIORIDAD
        defaultEstatusFuncionalidadShouldNotBeFound("prioridad.in=" + UPDATED_PRIORIDAD);
    }

    @Test
    @Transactional
    void getAllEstatusFuncionalidadsByPrioridadIsNullOrNotNull() throws Exception {
        // Initialize the database
        estatusFuncionalidadRepository.saveAndFlush(estatusFuncionalidad);

        // Get all the estatusFuncionalidadList where prioridad is not null
        defaultEstatusFuncionalidadShouldBeFound("prioridad.specified=true");

        // Get all the estatusFuncionalidadList where prioridad is null
        defaultEstatusFuncionalidadShouldNotBeFound("prioridad.specified=false");
    }

    @Test
    @Transactional
    void getAllEstatusFuncionalidadsByPrioridadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        estatusFuncionalidadRepository.saveAndFlush(estatusFuncionalidad);

        // Get all the estatusFuncionalidadList where prioridad is greater than or equal to DEFAULT_PRIORIDAD
        defaultEstatusFuncionalidadShouldBeFound("prioridad.greaterThanOrEqual=" + DEFAULT_PRIORIDAD);

        // Get all the estatusFuncionalidadList where prioridad is greater than or equal to UPDATED_PRIORIDAD
        defaultEstatusFuncionalidadShouldNotBeFound("prioridad.greaterThanOrEqual=" + UPDATED_PRIORIDAD);
    }

    @Test
    @Transactional
    void getAllEstatusFuncionalidadsByPrioridadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        estatusFuncionalidadRepository.saveAndFlush(estatusFuncionalidad);

        // Get all the estatusFuncionalidadList where prioridad is less than or equal to DEFAULT_PRIORIDAD
        defaultEstatusFuncionalidadShouldBeFound("prioridad.lessThanOrEqual=" + DEFAULT_PRIORIDAD);

        // Get all the estatusFuncionalidadList where prioridad is less than or equal to SMALLER_PRIORIDAD
        defaultEstatusFuncionalidadShouldNotBeFound("prioridad.lessThanOrEqual=" + SMALLER_PRIORIDAD);
    }

    @Test
    @Transactional
    void getAllEstatusFuncionalidadsByPrioridadIsLessThanSomething() throws Exception {
        // Initialize the database
        estatusFuncionalidadRepository.saveAndFlush(estatusFuncionalidad);

        // Get all the estatusFuncionalidadList where prioridad is less than DEFAULT_PRIORIDAD
        defaultEstatusFuncionalidadShouldNotBeFound("prioridad.lessThan=" + DEFAULT_PRIORIDAD);

        // Get all the estatusFuncionalidadList where prioridad is less than UPDATED_PRIORIDAD
        defaultEstatusFuncionalidadShouldBeFound("prioridad.lessThan=" + UPDATED_PRIORIDAD);
    }

    @Test
    @Transactional
    void getAllEstatusFuncionalidadsByPrioridadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        estatusFuncionalidadRepository.saveAndFlush(estatusFuncionalidad);

        // Get all the estatusFuncionalidadList where prioridad is greater than DEFAULT_PRIORIDAD
        defaultEstatusFuncionalidadShouldNotBeFound("prioridad.greaterThan=" + DEFAULT_PRIORIDAD);

        // Get all the estatusFuncionalidadList where prioridad is greater than SMALLER_PRIORIDAD
        defaultEstatusFuncionalidadShouldBeFound("prioridad.greaterThan=" + SMALLER_PRIORIDAD);
    }

    @Test
    @Transactional
    void getAllEstatusFuncionalidadsByFuncionalidadIsEqualToSomething() throws Exception {
        Funcionalidad funcionalidad;
        if (TestUtil.findAll(em, Funcionalidad.class).isEmpty()) {
            estatusFuncionalidadRepository.saveAndFlush(estatusFuncionalidad);
            funcionalidad = FuncionalidadResourceIT.createEntity(em);
        } else {
            funcionalidad = TestUtil.findAll(em, Funcionalidad.class).get(0);
        }
        em.persist(funcionalidad);
        em.flush();
        estatusFuncionalidad.addFuncionalidad(funcionalidad);
        estatusFuncionalidadRepository.saveAndFlush(estatusFuncionalidad);
        Long funcionalidadId = funcionalidad.getId();

        // Get all the estatusFuncionalidadList where funcionalidad equals to funcionalidadId
        defaultEstatusFuncionalidadShouldBeFound("funcionalidadId.equals=" + funcionalidadId);

        // Get all the estatusFuncionalidadList where funcionalidad equals to (funcionalidadId + 1)
        defaultEstatusFuncionalidadShouldNotBeFound("funcionalidadId.equals=" + (funcionalidadId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEstatusFuncionalidadShouldBeFound(String filter) throws Exception {
        restEstatusFuncionalidadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estatusFuncionalidad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].prioridad").value(hasItem(DEFAULT_PRIORIDAD)));

        // Check, that the count call also returns 1
        restEstatusFuncionalidadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEstatusFuncionalidadShouldNotBeFound(String filter) throws Exception {
        restEstatusFuncionalidadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEstatusFuncionalidadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEstatusFuncionalidad() throws Exception {
        // Get the estatusFuncionalidad
        restEstatusFuncionalidadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEstatusFuncionalidad() throws Exception {
        // Initialize the database
        estatusFuncionalidadRepository.saveAndFlush(estatusFuncionalidad);

        int databaseSizeBeforeUpdate = estatusFuncionalidadRepository.findAll().size();

        // Update the estatusFuncionalidad
        EstatusFuncionalidad updatedEstatusFuncionalidad = estatusFuncionalidadRepository.findById(estatusFuncionalidad.getId()).get();
        // Disconnect from session so that the updates on updatedEstatusFuncionalidad are not directly saved in db
        em.detach(updatedEstatusFuncionalidad);
        updatedEstatusFuncionalidad.nombre(UPDATED_NOMBRE).prioridad(UPDATED_PRIORIDAD);

        restEstatusFuncionalidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEstatusFuncionalidad.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEstatusFuncionalidad))
            )
            .andExpect(status().isOk());

        // Validate the EstatusFuncionalidad in the database
        List<EstatusFuncionalidad> estatusFuncionalidadList = estatusFuncionalidadRepository.findAll();
        assertThat(estatusFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
        EstatusFuncionalidad testEstatusFuncionalidad = estatusFuncionalidadList.get(estatusFuncionalidadList.size() - 1);
        assertThat(testEstatusFuncionalidad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEstatusFuncionalidad.getPrioridad()).isEqualTo(UPDATED_PRIORIDAD);
    }

    @Test
    @Transactional
    void putNonExistingEstatusFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = estatusFuncionalidadRepository.findAll().size();
        estatusFuncionalidad.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstatusFuncionalidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, estatusFuncionalidad.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(estatusFuncionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the EstatusFuncionalidad in the database
        List<EstatusFuncionalidad> estatusFuncionalidadList = estatusFuncionalidadRepository.findAll();
        assertThat(estatusFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEstatusFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = estatusFuncionalidadRepository.findAll().size();
        estatusFuncionalidad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstatusFuncionalidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(estatusFuncionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the EstatusFuncionalidad in the database
        List<EstatusFuncionalidad> estatusFuncionalidadList = estatusFuncionalidadRepository.findAll();
        assertThat(estatusFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEstatusFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = estatusFuncionalidadRepository.findAll().size();
        estatusFuncionalidad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstatusFuncionalidadMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(estatusFuncionalidad))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EstatusFuncionalidad in the database
        List<EstatusFuncionalidad> estatusFuncionalidadList = estatusFuncionalidadRepository.findAll();
        assertThat(estatusFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEstatusFuncionalidadWithPatch() throws Exception {
        // Initialize the database
        estatusFuncionalidadRepository.saveAndFlush(estatusFuncionalidad);

        int databaseSizeBeforeUpdate = estatusFuncionalidadRepository.findAll().size();

        // Update the estatusFuncionalidad using partial update
        EstatusFuncionalidad partialUpdatedEstatusFuncionalidad = new EstatusFuncionalidad();
        partialUpdatedEstatusFuncionalidad.setId(estatusFuncionalidad.getId());

        partialUpdatedEstatusFuncionalidad.nombre(UPDATED_NOMBRE);

        restEstatusFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstatusFuncionalidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEstatusFuncionalidad))
            )
            .andExpect(status().isOk());

        // Validate the EstatusFuncionalidad in the database
        List<EstatusFuncionalidad> estatusFuncionalidadList = estatusFuncionalidadRepository.findAll();
        assertThat(estatusFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
        EstatusFuncionalidad testEstatusFuncionalidad = estatusFuncionalidadList.get(estatusFuncionalidadList.size() - 1);
        assertThat(testEstatusFuncionalidad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEstatusFuncionalidad.getPrioridad()).isEqualTo(DEFAULT_PRIORIDAD);
    }

    @Test
    @Transactional
    void fullUpdateEstatusFuncionalidadWithPatch() throws Exception {
        // Initialize the database
        estatusFuncionalidadRepository.saveAndFlush(estatusFuncionalidad);

        int databaseSizeBeforeUpdate = estatusFuncionalidadRepository.findAll().size();

        // Update the estatusFuncionalidad using partial update
        EstatusFuncionalidad partialUpdatedEstatusFuncionalidad = new EstatusFuncionalidad();
        partialUpdatedEstatusFuncionalidad.setId(estatusFuncionalidad.getId());

        partialUpdatedEstatusFuncionalidad.nombre(UPDATED_NOMBRE).prioridad(UPDATED_PRIORIDAD);

        restEstatusFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstatusFuncionalidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEstatusFuncionalidad))
            )
            .andExpect(status().isOk());

        // Validate the EstatusFuncionalidad in the database
        List<EstatusFuncionalidad> estatusFuncionalidadList = estatusFuncionalidadRepository.findAll();
        assertThat(estatusFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
        EstatusFuncionalidad testEstatusFuncionalidad = estatusFuncionalidadList.get(estatusFuncionalidadList.size() - 1);
        assertThat(testEstatusFuncionalidad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEstatusFuncionalidad.getPrioridad()).isEqualTo(UPDATED_PRIORIDAD);
    }

    @Test
    @Transactional
    void patchNonExistingEstatusFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = estatusFuncionalidadRepository.findAll().size();
        estatusFuncionalidad.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstatusFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, estatusFuncionalidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(estatusFuncionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the EstatusFuncionalidad in the database
        List<EstatusFuncionalidad> estatusFuncionalidadList = estatusFuncionalidadRepository.findAll();
        assertThat(estatusFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEstatusFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = estatusFuncionalidadRepository.findAll().size();
        estatusFuncionalidad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstatusFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(estatusFuncionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the EstatusFuncionalidad in the database
        List<EstatusFuncionalidad> estatusFuncionalidadList = estatusFuncionalidadRepository.findAll();
        assertThat(estatusFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEstatusFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = estatusFuncionalidadRepository.findAll().size();
        estatusFuncionalidad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstatusFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(estatusFuncionalidad))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EstatusFuncionalidad in the database
        List<EstatusFuncionalidad> estatusFuncionalidadList = estatusFuncionalidadRepository.findAll();
        assertThat(estatusFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEstatusFuncionalidad() throws Exception {
        // Initialize the database
        estatusFuncionalidadRepository.saveAndFlush(estatusFuncionalidad);

        int databaseSizeBeforeDelete = estatusFuncionalidadRepository.findAll().size();

        // Delete the estatusFuncionalidad
        restEstatusFuncionalidadMockMvc
            .perform(delete(ENTITY_API_URL_ID, estatusFuncionalidad.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EstatusFuncionalidad> estatusFuncionalidadList = estatusFuncionalidadRepository.findAll();
        assertThat(estatusFuncionalidadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
