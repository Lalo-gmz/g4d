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
import mx.lania.g4d.domain.Etiqueta;
import mx.lania.g4d.domain.Funcionalidad;
import mx.lania.g4d.repository.EtiquetaRepository;
import mx.lania.g4d.service.criteria.EtiquetaCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EtiquetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EtiquetaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIORIDAD = 1;
    private static final Integer UPDATED_PRIORIDAD = 2;
    private static final Integer SMALLER_PRIORIDAD = 1 - 1;

    private static final String ENTITY_API_URL = "/api/etiquetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EtiquetaRepository etiquetaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEtiquetaMockMvc;

    private Etiqueta etiqueta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etiqueta createEntity(EntityManager em) {
        Etiqueta etiqueta = new Etiqueta().nombre(DEFAULT_NOMBRE).prioridad(DEFAULT_PRIORIDAD);
        return etiqueta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etiqueta createUpdatedEntity(EntityManager em) {
        Etiqueta etiqueta = new Etiqueta().nombre(UPDATED_NOMBRE).prioridad(UPDATED_PRIORIDAD);
        return etiqueta;
    }

    @BeforeEach
    public void initTest() {
        etiqueta = createEntity(em);
    }

    @Test
    @Transactional
    void createEtiqueta() throws Exception {
        int databaseSizeBeforeCreate = etiquetaRepository.findAll().size();
        // Create the Etiqueta
        restEtiquetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etiqueta)))
            .andExpect(status().isCreated());

        // Validate the Etiqueta in the database
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeCreate + 1);
        Etiqueta testEtiqueta = etiquetaList.get(etiquetaList.size() - 1);
        assertThat(testEtiqueta.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEtiqueta.getPrioridad()).isEqualTo(DEFAULT_PRIORIDAD);
    }

    @Test
    @Transactional
    void createEtiquetaWithExistingId() throws Exception {
        // Create the Etiqueta with an existing ID
        etiqueta.setId(1L);

        int databaseSizeBeforeCreate = etiquetaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtiquetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etiqueta)))
            .andExpect(status().isBadRequest());

        // Validate the Etiqueta in the database
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = etiquetaRepository.findAll().size();
        // set the field null
        etiqueta.setNombre(null);

        // Create the Etiqueta, which fails.

        restEtiquetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etiqueta)))
            .andExpect(status().isBadRequest());

        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEtiquetas() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        // Get all the etiquetaList
        restEtiquetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etiqueta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].prioridad").value(hasItem(DEFAULT_PRIORIDAD)));
    }

    @Test
    @Transactional
    void getEtiqueta() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        // Get the etiqueta
        restEtiquetaMockMvc
            .perform(get(ENTITY_API_URL_ID, etiqueta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(etiqueta.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.prioridad").value(DEFAULT_PRIORIDAD));
    }

    @Test
    @Transactional
    void getEtiquetasByIdFiltering() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        Long id = etiqueta.getId();

        defaultEtiquetaShouldBeFound("id.equals=" + id);
        defaultEtiquetaShouldNotBeFound("id.notEquals=" + id);

        defaultEtiquetaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEtiquetaShouldNotBeFound("id.greaterThan=" + id);

        defaultEtiquetaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEtiquetaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEtiquetasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        // Get all the etiquetaList where nombre equals to DEFAULT_NOMBRE
        defaultEtiquetaShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the etiquetaList where nombre equals to UPDATED_NOMBRE
        defaultEtiquetaShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEtiquetasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        // Get all the etiquetaList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultEtiquetaShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the etiquetaList where nombre equals to UPDATED_NOMBRE
        defaultEtiquetaShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEtiquetasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        // Get all the etiquetaList where nombre is not null
        defaultEtiquetaShouldBeFound("nombre.specified=true");

        // Get all the etiquetaList where nombre is null
        defaultEtiquetaShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllEtiquetasByNombreContainsSomething() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        // Get all the etiquetaList where nombre contains DEFAULT_NOMBRE
        defaultEtiquetaShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the etiquetaList where nombre contains UPDATED_NOMBRE
        defaultEtiquetaShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEtiquetasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        // Get all the etiquetaList where nombre does not contain DEFAULT_NOMBRE
        defaultEtiquetaShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the etiquetaList where nombre does not contain UPDATED_NOMBRE
        defaultEtiquetaShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEtiquetasByPrioridadIsEqualToSomething() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        // Get all the etiquetaList where prioridad equals to DEFAULT_PRIORIDAD
        defaultEtiquetaShouldBeFound("prioridad.equals=" + DEFAULT_PRIORIDAD);

        // Get all the etiquetaList where prioridad equals to UPDATED_PRIORIDAD
        defaultEtiquetaShouldNotBeFound("prioridad.equals=" + UPDATED_PRIORIDAD);
    }

    @Test
    @Transactional
    void getAllEtiquetasByPrioridadIsInShouldWork() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        // Get all the etiquetaList where prioridad in DEFAULT_PRIORIDAD or UPDATED_PRIORIDAD
        defaultEtiquetaShouldBeFound("prioridad.in=" + DEFAULT_PRIORIDAD + "," + UPDATED_PRIORIDAD);

        // Get all the etiquetaList where prioridad equals to UPDATED_PRIORIDAD
        defaultEtiquetaShouldNotBeFound("prioridad.in=" + UPDATED_PRIORIDAD);
    }

    @Test
    @Transactional
    void getAllEtiquetasByPrioridadIsNullOrNotNull() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        // Get all the etiquetaList where prioridad is not null
        defaultEtiquetaShouldBeFound("prioridad.specified=true");

        // Get all the etiquetaList where prioridad is null
        defaultEtiquetaShouldNotBeFound("prioridad.specified=false");
    }

    @Test
    @Transactional
    void getAllEtiquetasByPrioridadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        // Get all the etiquetaList where prioridad is greater than or equal to DEFAULT_PRIORIDAD
        defaultEtiquetaShouldBeFound("prioridad.greaterThanOrEqual=" + DEFAULT_PRIORIDAD);

        // Get all the etiquetaList where prioridad is greater than or equal to UPDATED_PRIORIDAD
        defaultEtiquetaShouldNotBeFound("prioridad.greaterThanOrEqual=" + UPDATED_PRIORIDAD);
    }

    @Test
    @Transactional
    void getAllEtiquetasByPrioridadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        // Get all the etiquetaList where prioridad is less than or equal to DEFAULT_PRIORIDAD
        defaultEtiquetaShouldBeFound("prioridad.lessThanOrEqual=" + DEFAULT_PRIORIDAD);

        // Get all the etiquetaList where prioridad is less than or equal to SMALLER_PRIORIDAD
        defaultEtiquetaShouldNotBeFound("prioridad.lessThanOrEqual=" + SMALLER_PRIORIDAD);
    }

    @Test
    @Transactional
    void getAllEtiquetasByPrioridadIsLessThanSomething() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        // Get all the etiquetaList where prioridad is less than DEFAULT_PRIORIDAD
        defaultEtiquetaShouldNotBeFound("prioridad.lessThan=" + DEFAULT_PRIORIDAD);

        // Get all the etiquetaList where prioridad is less than UPDATED_PRIORIDAD
        defaultEtiquetaShouldBeFound("prioridad.lessThan=" + UPDATED_PRIORIDAD);
    }

    @Test
    @Transactional
    void getAllEtiquetasByPrioridadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        // Get all the etiquetaList where prioridad is greater than DEFAULT_PRIORIDAD
        defaultEtiquetaShouldNotBeFound("prioridad.greaterThan=" + DEFAULT_PRIORIDAD);

        // Get all the etiquetaList where prioridad is greater than SMALLER_PRIORIDAD
        defaultEtiquetaShouldBeFound("prioridad.greaterThan=" + SMALLER_PRIORIDAD);
    }

    @Test
    @Transactional
    void getAllEtiquetasByFuncionalidadIsEqualToSomething() throws Exception {
        Funcionalidad funcionalidad;
        if (TestUtil.findAll(em, Funcionalidad.class).isEmpty()) {
            etiquetaRepository.saveAndFlush(etiqueta);
            funcionalidad = FuncionalidadResourceIT.createEntity(em);
        } else {
            funcionalidad = TestUtil.findAll(em, Funcionalidad.class).get(0);
        }
        em.persist(funcionalidad);
        em.flush();
        etiqueta.setFuncionalidad(funcionalidad);
        etiquetaRepository.saveAndFlush(etiqueta);
        Long funcionalidadId = funcionalidad.getId();

        // Get all the etiquetaList where funcionalidad equals to funcionalidadId
        defaultEtiquetaShouldBeFound("funcionalidadId.equals=" + funcionalidadId);

        // Get all the etiquetaList where funcionalidad equals to (funcionalidadId + 1)
        defaultEtiquetaShouldNotBeFound("funcionalidadId.equals=" + (funcionalidadId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEtiquetaShouldBeFound(String filter) throws Exception {
        restEtiquetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etiqueta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].prioridad").value(hasItem(DEFAULT_PRIORIDAD)));

        // Check, that the count call also returns 1
        restEtiquetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEtiquetaShouldNotBeFound(String filter) throws Exception {
        restEtiquetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEtiquetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEtiqueta() throws Exception {
        // Get the etiqueta
        restEtiquetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEtiqueta() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        int databaseSizeBeforeUpdate = etiquetaRepository.findAll().size();

        // Update the etiqueta
        Etiqueta updatedEtiqueta = etiquetaRepository.findById(etiqueta.getId()).get();
        // Disconnect from session so that the updates on updatedEtiqueta are not directly saved in db
        em.detach(updatedEtiqueta);
        updatedEtiqueta.nombre(UPDATED_NOMBRE).prioridad(UPDATED_PRIORIDAD);

        restEtiquetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEtiqueta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEtiqueta))
            )
            .andExpect(status().isOk());

        // Validate the Etiqueta in the database
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeUpdate);
        Etiqueta testEtiqueta = etiquetaList.get(etiquetaList.size() - 1);
        assertThat(testEtiqueta.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEtiqueta.getPrioridad()).isEqualTo(UPDATED_PRIORIDAD);
    }

    @Test
    @Transactional
    void putNonExistingEtiqueta() throws Exception {
        int databaseSizeBeforeUpdate = etiquetaRepository.findAll().size();
        etiqueta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtiquetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, etiqueta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etiqueta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etiqueta in the database
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEtiqueta() throws Exception {
        int databaseSizeBeforeUpdate = etiquetaRepository.findAll().size();
        etiqueta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtiquetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etiqueta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etiqueta in the database
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEtiqueta() throws Exception {
        int databaseSizeBeforeUpdate = etiquetaRepository.findAll().size();
        etiqueta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtiquetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etiqueta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etiqueta in the database
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEtiquetaWithPatch() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        int databaseSizeBeforeUpdate = etiquetaRepository.findAll().size();

        // Update the etiqueta using partial update
        Etiqueta partialUpdatedEtiqueta = new Etiqueta();
        partialUpdatedEtiqueta.setId(etiqueta.getId());

        partialUpdatedEtiqueta.prioridad(UPDATED_PRIORIDAD);

        restEtiquetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtiqueta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtiqueta))
            )
            .andExpect(status().isOk());

        // Validate the Etiqueta in the database
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeUpdate);
        Etiqueta testEtiqueta = etiquetaList.get(etiquetaList.size() - 1);
        assertThat(testEtiqueta.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEtiqueta.getPrioridad()).isEqualTo(UPDATED_PRIORIDAD);
    }

    @Test
    @Transactional
    void fullUpdateEtiquetaWithPatch() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        int databaseSizeBeforeUpdate = etiquetaRepository.findAll().size();

        // Update the etiqueta using partial update
        Etiqueta partialUpdatedEtiqueta = new Etiqueta();
        partialUpdatedEtiqueta.setId(etiqueta.getId());

        partialUpdatedEtiqueta.nombre(UPDATED_NOMBRE).prioridad(UPDATED_PRIORIDAD);

        restEtiquetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtiqueta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtiqueta))
            )
            .andExpect(status().isOk());

        // Validate the Etiqueta in the database
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeUpdate);
        Etiqueta testEtiqueta = etiquetaList.get(etiquetaList.size() - 1);
        assertThat(testEtiqueta.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEtiqueta.getPrioridad()).isEqualTo(UPDATED_PRIORIDAD);
    }

    @Test
    @Transactional
    void patchNonExistingEtiqueta() throws Exception {
        int databaseSizeBeforeUpdate = etiquetaRepository.findAll().size();
        etiqueta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtiquetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, etiqueta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etiqueta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etiqueta in the database
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEtiqueta() throws Exception {
        int databaseSizeBeforeUpdate = etiquetaRepository.findAll().size();
        etiqueta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtiquetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etiqueta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etiqueta in the database
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEtiqueta() throws Exception {
        int databaseSizeBeforeUpdate = etiquetaRepository.findAll().size();
        etiqueta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtiquetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(etiqueta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etiqueta in the database
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEtiqueta() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        int databaseSizeBeforeDelete = etiquetaRepository.findAll().size();

        // Delete the etiqueta
        restEtiquetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, etiqueta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}