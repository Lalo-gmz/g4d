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
import mx.lania.g4d.domain.AtributoFuncionalidad;
import mx.lania.g4d.repository.AtributoFuncionalidadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AtributoFuncionalidadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AtributoFuncionalidadResourceIT {

    private static final Boolean DEFAULT_MARCADO = false;
    private static final Boolean UPDATED_MARCADO = true;

    private static final String DEFAULT_VALOR = "AAAAAAAAAA";
    private static final String UPDATED_VALOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/atributo-funcionalidads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AtributoFuncionalidadRepository atributoFuncionalidadRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAtributoFuncionalidadMockMvc;

    private AtributoFuncionalidad atributoFuncionalidad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AtributoFuncionalidad createEntity(EntityManager em) {
        AtributoFuncionalidad atributoFuncionalidad = new AtributoFuncionalidad().marcado(DEFAULT_MARCADO).valor(DEFAULT_VALOR);
        return atributoFuncionalidad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AtributoFuncionalidad createUpdatedEntity(EntityManager em) {
        AtributoFuncionalidad atributoFuncionalidad = new AtributoFuncionalidad().marcado(UPDATED_MARCADO).valor(UPDATED_VALOR);
        return atributoFuncionalidad;
    }

    @BeforeEach
    public void initTest() {
        atributoFuncionalidad = createEntity(em);
    }

    @Transactional
    void createAtributoFuncionalidad() throws Exception {
        int databaseSizeBeforeCreate = atributoFuncionalidadRepository.findAll().size();
        // Create the AtributoFuncionalidad
        restAtributoFuncionalidadMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atributoFuncionalidad))
            )
            .andExpect(status().isCreated());

        // Validate the AtributoFuncionalidad in the database
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeCreate + 1);
        AtributoFuncionalidad testAtributoFuncionalidad = atributoFuncionalidadList.get(atributoFuncionalidadList.size() - 1);
        assertThat(testAtributoFuncionalidad.getMarcado()).isEqualTo(DEFAULT_MARCADO);
        assertThat(testAtributoFuncionalidad.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Transactional
    void createAtributoFuncionalidadWithExistingId() throws Exception {
        // Create the AtributoFuncionalidad with an existing ID
        atributoFuncionalidad.setId(1L);

        int databaseSizeBeforeCreate = atributoFuncionalidadRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAtributoFuncionalidadMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atributoFuncionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the AtributoFuncionalidad in the database
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeCreate);
    }

    @Transactional
    void getAllAtributoFuncionalidads() throws Exception {
        // Initialize the database
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);

        // Get all the atributoFuncionalidadList
        restAtributoFuncionalidadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(atributoFuncionalidad.getId().intValue())))
            .andExpect(jsonPath("$.[*].marcado").value(hasItem(DEFAULT_MARCADO.booleanValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR)));
    }

    @Transactional
    void getAtributoFuncionalidad() throws Exception {
        // Initialize the database
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);

        // Get the atributoFuncionalidad
        restAtributoFuncionalidadMockMvc
            .perform(get(ENTITY_API_URL_ID, atributoFuncionalidad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(atributoFuncionalidad.getId().intValue()))
            .andExpect(jsonPath("$.marcado").value(DEFAULT_MARCADO.booleanValue()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR));
    }

    @Transactional
    void getNonExistingAtributoFuncionalidad() throws Exception {
        // Get the atributoFuncionalidad
        restAtributoFuncionalidadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Transactional
    void putExistingAtributoFuncionalidad() throws Exception {
        // Initialize the database
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);

        int databaseSizeBeforeUpdate = atributoFuncionalidadRepository.findAll().size();

        // Update the atributoFuncionalidad
        AtributoFuncionalidad updatedAtributoFuncionalidad = atributoFuncionalidadRepository.findById(atributoFuncionalidad.getId()).get();
        // Disconnect from session so that the updates on updatedAtributoFuncionalidad are not directly saved in db
        em.detach(updatedAtributoFuncionalidad);
        updatedAtributoFuncionalidad.marcado(UPDATED_MARCADO).valor(UPDATED_VALOR);

        restAtributoFuncionalidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAtributoFuncionalidad.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAtributoFuncionalidad))
            )
            .andExpect(status().isOk());

        // Validate the AtributoFuncionalidad in the database
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
        AtributoFuncionalidad testAtributoFuncionalidad = atributoFuncionalidadList.get(atributoFuncionalidadList.size() - 1);
        assertThat(testAtributoFuncionalidad.getMarcado()).isEqualTo(UPDATED_MARCADO);
        assertThat(testAtributoFuncionalidad.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Transactional
    void putNonExistingAtributoFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = atributoFuncionalidadRepository.findAll().size();
        atributoFuncionalidad.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAtributoFuncionalidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, atributoFuncionalidad.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atributoFuncionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the AtributoFuncionalidad in the database
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Transactional
    void putWithIdMismatchAtributoFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = atributoFuncionalidadRepository.findAll().size();
        atributoFuncionalidad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtributoFuncionalidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atributoFuncionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the AtributoFuncionalidad in the database
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Transactional
    void putWithMissingIdPathParamAtributoFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = atributoFuncionalidadRepository.findAll().size();
        atributoFuncionalidad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtributoFuncionalidadMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atributoFuncionalidad))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AtributoFuncionalidad in the database
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Transactional
    void partialUpdateAtributoFuncionalidadWithPatch() throws Exception {
        // Initialize the database
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);

        int databaseSizeBeforeUpdate = atributoFuncionalidadRepository.findAll().size();

        // Update the atributoFuncionalidad using partial update
        AtributoFuncionalidad partialUpdatedAtributoFuncionalidad = new AtributoFuncionalidad();
        partialUpdatedAtributoFuncionalidad.setId(atributoFuncionalidad.getId());

        partialUpdatedAtributoFuncionalidad.marcado(UPDATED_MARCADO);

        restAtributoFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAtributoFuncionalidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAtributoFuncionalidad))
            )
            .andExpect(status().isOk());

        // Validate the AtributoFuncionalidad in the database
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
        AtributoFuncionalidad testAtributoFuncionalidad = atributoFuncionalidadList.get(atributoFuncionalidadList.size() - 1);
        assertThat(testAtributoFuncionalidad.getMarcado()).isEqualTo(UPDATED_MARCADO);
        assertThat(testAtributoFuncionalidad.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Transactional
    void fullUpdateAtributoFuncionalidadWithPatch() throws Exception {
        // Initialize the database
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);

        int databaseSizeBeforeUpdate = atributoFuncionalidadRepository.findAll().size();

        // Update the atributoFuncionalidad using partial update
        AtributoFuncionalidad partialUpdatedAtributoFuncionalidad = new AtributoFuncionalidad();
        partialUpdatedAtributoFuncionalidad.setId(atributoFuncionalidad.getId());

        partialUpdatedAtributoFuncionalidad.marcado(UPDATED_MARCADO).valor(UPDATED_VALOR);

        restAtributoFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAtributoFuncionalidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAtributoFuncionalidad))
            )
            .andExpect(status().isOk());

        // Validate the AtributoFuncionalidad in the database
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
        AtributoFuncionalidad testAtributoFuncionalidad = atributoFuncionalidadList.get(atributoFuncionalidadList.size() - 1);
        assertThat(testAtributoFuncionalidad.getMarcado()).isEqualTo(UPDATED_MARCADO);
        assertThat(testAtributoFuncionalidad.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Transactional
    void patchNonExistingAtributoFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = atributoFuncionalidadRepository.findAll().size();
        atributoFuncionalidad.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAtributoFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, atributoFuncionalidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(atributoFuncionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the AtributoFuncionalidad in the database
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Transactional
    void patchWithIdMismatchAtributoFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = atributoFuncionalidadRepository.findAll().size();
        atributoFuncionalidad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtributoFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(atributoFuncionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the AtributoFuncionalidad in the database
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Transactional
    void patchWithMissingIdPathParamAtributoFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = atributoFuncionalidadRepository.findAll().size();
        atributoFuncionalidad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtributoFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(atributoFuncionalidad))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AtributoFuncionalidad in the database
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Transactional
    void deleteAtributoFuncionalidad() throws Exception {
        // Initialize the database
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);

        int databaseSizeBeforeDelete = atributoFuncionalidadRepository.findAll().size();

        // Delete the atributoFuncionalidad
        restAtributoFuncionalidadMockMvc
            .perform(delete(ENTITY_API_URL_ID, atributoFuncionalidad.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
