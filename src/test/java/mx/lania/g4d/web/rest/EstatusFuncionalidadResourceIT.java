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
 * Integration tests for the {@link EstatusFuncionalidadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EstatusFuncionalidadResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

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
        EstatusFuncionalidad estatusFuncionalidad = new EstatusFuncionalidad().nombre(DEFAULT_NOMBRE);
        return estatusFuncionalidad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EstatusFuncionalidad createUpdatedEntity(EntityManager em) {
        EstatusFuncionalidad estatusFuncionalidad = new EstatusFuncionalidad().nombre(UPDATED_NOMBRE);
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
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
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
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
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
        updatedEstatusFuncionalidad.nombre(UPDATED_NOMBRE);

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
