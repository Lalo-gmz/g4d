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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PrioridadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PrioridadResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIORIDAD_NUMERICA = 1;
    private static final Integer UPDATED_PRIORIDAD_NUMERICA = 2;

    private static final String ENTITY_API_URL = "/api/prioridads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrioridadRepository prioridadRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrioridadMockMvc;

    private Prioridad prioridad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prioridad createEntity(EntityManager em) {
        Prioridad prioridad = new Prioridad().nombre(DEFAULT_NOMBRE).prioridadNumerica(DEFAULT_PRIORIDAD_NUMERICA);
        return prioridad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prioridad createUpdatedEntity(EntityManager em) {
        Prioridad prioridad = new Prioridad().nombre(UPDATED_NOMBRE).prioridadNumerica(UPDATED_PRIORIDAD_NUMERICA);
        return prioridad;
    }

    @BeforeEach
    public void initTest() {
        prioridad = createEntity(em);
    }

    @Test
    @Transactional
    void createPrioridad() throws Exception {
        int databaseSizeBeforeCreate = prioridadRepository.findAll().size();
        // Create the Prioridad
        restPrioridadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prioridad)))
            .andExpect(status().isCreated());

        // Validate the Prioridad in the database
        List<Prioridad> prioridadList = prioridadRepository.findAll();
        assertThat(prioridadList).hasSize(databaseSizeBeforeCreate + 1);
        Prioridad testPrioridad = prioridadList.get(prioridadList.size() - 1);
        assertThat(testPrioridad.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPrioridad.getPrioridadNumerica()).isEqualTo(DEFAULT_PRIORIDAD_NUMERICA);
    }

    @Test
    @Transactional
    void createPrioridadWithExistingId() throws Exception {
        // Create the Prioridad with an existing ID
        prioridad.setId(1L);

        int databaseSizeBeforeCreate = prioridadRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrioridadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prioridad)))
            .andExpect(status().isBadRequest());

        // Validate the Prioridad in the database
        List<Prioridad> prioridadList = prioridadRepository.findAll();
        assertThat(prioridadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPrioridads() throws Exception {
        // Initialize the database
        prioridadRepository.saveAndFlush(prioridad);

        // Get all the prioridadList
        restPrioridadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prioridad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].prioridadNumerica").value(hasItem(DEFAULT_PRIORIDAD_NUMERICA)));
    }

    @Test
    @Transactional
    void getPrioridad() throws Exception {
        // Initialize the database
        prioridadRepository.saveAndFlush(prioridad);

        // Get the prioridad
        restPrioridadMockMvc
            .perform(get(ENTITY_API_URL_ID, prioridad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prioridad.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.prioridadNumerica").value(DEFAULT_PRIORIDAD_NUMERICA));
    }

    @Test
    @Transactional
    void getNonExistingPrioridad() throws Exception {
        // Get the prioridad
        restPrioridadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPrioridad() throws Exception {
        // Initialize the database
        prioridadRepository.saveAndFlush(prioridad);

        int databaseSizeBeforeUpdate = prioridadRepository.findAll().size();

        // Update the prioridad
        Prioridad updatedPrioridad = prioridadRepository.findById(prioridad.getId()).get();
        // Disconnect from session so that the updates on updatedPrioridad are not directly saved in db
        em.detach(updatedPrioridad);
        updatedPrioridad.nombre(UPDATED_NOMBRE).prioridadNumerica(UPDATED_PRIORIDAD_NUMERICA);

        restPrioridadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPrioridad.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPrioridad))
            )
            .andExpect(status().isOk());

        // Validate the Prioridad in the database
        List<Prioridad> prioridadList = prioridadRepository.findAll();
        assertThat(prioridadList).hasSize(databaseSizeBeforeUpdate);
        Prioridad testPrioridad = prioridadList.get(prioridadList.size() - 1);
        assertThat(testPrioridad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPrioridad.getPrioridadNumerica()).isEqualTo(UPDATED_PRIORIDAD_NUMERICA);
    }

    @Test
    @Transactional
    void putNonExistingPrioridad() throws Exception {
        int databaseSizeBeforeUpdate = prioridadRepository.findAll().size();
        prioridad.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrioridadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prioridad.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prioridad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prioridad in the database
        List<Prioridad> prioridadList = prioridadRepository.findAll();
        assertThat(prioridadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrioridad() throws Exception {
        int databaseSizeBeforeUpdate = prioridadRepository.findAll().size();
        prioridad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrioridadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prioridad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prioridad in the database
        List<Prioridad> prioridadList = prioridadRepository.findAll();
        assertThat(prioridadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrioridad() throws Exception {
        int databaseSizeBeforeUpdate = prioridadRepository.findAll().size();
        prioridad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrioridadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prioridad)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prioridad in the database
        List<Prioridad> prioridadList = prioridadRepository.findAll();
        assertThat(prioridadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrioridadWithPatch() throws Exception {
        // Initialize the database
        prioridadRepository.saveAndFlush(prioridad);

        int databaseSizeBeforeUpdate = prioridadRepository.findAll().size();

        // Update the prioridad using partial update
        Prioridad partialUpdatedPrioridad = new Prioridad();
        partialUpdatedPrioridad.setId(prioridad.getId());

        partialUpdatedPrioridad.nombre(UPDATED_NOMBRE).prioridadNumerica(UPDATED_PRIORIDAD_NUMERICA);

        restPrioridadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrioridad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrioridad))
            )
            .andExpect(status().isOk());

        // Validate the Prioridad in the database
        List<Prioridad> prioridadList = prioridadRepository.findAll();
        assertThat(prioridadList).hasSize(databaseSizeBeforeUpdate);
        Prioridad testPrioridad = prioridadList.get(prioridadList.size() - 1);
        assertThat(testPrioridad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPrioridad.getPrioridadNumerica()).isEqualTo(UPDATED_PRIORIDAD_NUMERICA);
    }

    @Test
    @Transactional
    void fullUpdatePrioridadWithPatch() throws Exception {
        // Initialize the database
        prioridadRepository.saveAndFlush(prioridad);

        int databaseSizeBeforeUpdate = prioridadRepository.findAll().size();

        // Update the prioridad using partial update
        Prioridad partialUpdatedPrioridad = new Prioridad();
        partialUpdatedPrioridad.setId(prioridad.getId());

        partialUpdatedPrioridad.nombre(UPDATED_NOMBRE).prioridadNumerica(UPDATED_PRIORIDAD_NUMERICA);

        restPrioridadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrioridad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrioridad))
            )
            .andExpect(status().isOk());

        // Validate the Prioridad in the database
        List<Prioridad> prioridadList = prioridadRepository.findAll();
        assertThat(prioridadList).hasSize(databaseSizeBeforeUpdate);
        Prioridad testPrioridad = prioridadList.get(prioridadList.size() - 1);
        assertThat(testPrioridad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPrioridad.getPrioridadNumerica()).isEqualTo(UPDATED_PRIORIDAD_NUMERICA);
    }

    @Test
    @Transactional
    void patchNonExistingPrioridad() throws Exception {
        int databaseSizeBeforeUpdate = prioridadRepository.findAll().size();
        prioridad.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrioridadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prioridad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prioridad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prioridad in the database
        List<Prioridad> prioridadList = prioridadRepository.findAll();
        assertThat(prioridadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrioridad() throws Exception {
        int databaseSizeBeforeUpdate = prioridadRepository.findAll().size();
        prioridad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrioridadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prioridad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prioridad in the database
        List<Prioridad> prioridadList = prioridadRepository.findAll();
        assertThat(prioridadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrioridad() throws Exception {
        int databaseSizeBeforeUpdate = prioridadRepository.findAll().size();
        prioridad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrioridadMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(prioridad))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prioridad in the database
        List<Prioridad> prioridadList = prioridadRepository.findAll();
        assertThat(prioridadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrioridad() throws Exception {
        // Initialize the database
        prioridadRepository.saveAndFlush(prioridad);

        int databaseSizeBeforeDelete = prioridadRepository.findAll().size();

        // Delete the prioridad
        restPrioridadMockMvc
            .perform(delete(ENTITY_API_URL_ID, prioridad.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Prioridad> prioridadList = prioridadRepository.findAll();
        assertThat(prioridadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
