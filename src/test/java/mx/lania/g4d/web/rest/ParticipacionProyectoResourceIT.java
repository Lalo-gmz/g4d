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
import mx.lania.g4d.domain.ParticipacionProyecto;
import mx.lania.g4d.repository.ParticipacionProyectoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ParticipacionProyectoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParticipacionProyectoResourceIT {

    private static final Boolean DEFAULT_ES_ADMIN = false;
    private static final Boolean UPDATED_ES_ADMIN = true;

    private static final String ENTITY_API_URL = "/api/participacion-proyectos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParticipacionProyectoRepository participacionProyectoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParticipacionProyectoMockMvc;

    private ParticipacionProyecto participacionProyecto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParticipacionProyecto createEntity(EntityManager em) {
        ParticipacionProyecto participacionProyecto = new ParticipacionProyecto().esAdmin(DEFAULT_ES_ADMIN);
        return participacionProyecto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParticipacionProyecto createUpdatedEntity(EntityManager em) {
        ParticipacionProyecto participacionProyecto = new ParticipacionProyecto().esAdmin(UPDATED_ES_ADMIN);
        return participacionProyecto;
    }

    @BeforeEach
    public void initTest() {
        participacionProyecto = createEntity(em);
    }

    @Transactional
    void createParticipacionProyecto() throws Exception {
        int databaseSizeBeforeCreate = participacionProyectoRepository.findAll().size();
        // Create the ParticipacionProyecto
        restParticipacionProyectoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(participacionProyecto))
            )
            .andExpect(status().isCreated());

        // Validate the ParticipacionProyecto in the database
        List<ParticipacionProyecto> participacionProyectoList = participacionProyectoRepository.findAll();
        assertThat(participacionProyectoList).hasSize(databaseSizeBeforeCreate + 1);
        ParticipacionProyecto testParticipacionProyecto = participacionProyectoList.get(participacionProyectoList.size() - 1);
        assertThat(testParticipacionProyecto.getEsAdmin()).isEqualTo(DEFAULT_ES_ADMIN);
    }

    @Transactional
    void createParticipacionProyectoWithExistingId() throws Exception {
        // Create the ParticipacionProyecto with an existing ID
        participacionProyecto.setId(1L);

        int databaseSizeBeforeCreate = participacionProyectoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParticipacionProyectoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(participacionProyecto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParticipacionProyecto in the database
        List<ParticipacionProyecto> participacionProyectoList = participacionProyectoRepository.findAll();
        assertThat(participacionProyectoList).hasSize(databaseSizeBeforeCreate);
    }

    @Transactional
    void getAllParticipacionProyectos() throws Exception {
        // Initialize the database
        participacionProyectoRepository.saveAndFlush(participacionProyecto);

        // Get all the participacionProyectoList
        restParticipacionProyectoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(participacionProyecto.getId().intValue())))
            .andExpect(jsonPath("$.[*].esAdmin").value(hasItem(DEFAULT_ES_ADMIN.booleanValue())));
    }

    @Transactional
    void getParticipacionProyecto() throws Exception {
        // Initialize the database
        participacionProyectoRepository.saveAndFlush(participacionProyecto);

        // Get the participacionProyecto
        restParticipacionProyectoMockMvc
            .perform(get(ENTITY_API_URL_ID, participacionProyecto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(participacionProyecto.getId().intValue()))
            .andExpect(jsonPath("$.esAdmin").value(DEFAULT_ES_ADMIN.booleanValue()));
    }

    @Transactional
    void getNonExistingParticipacionProyecto() throws Exception {
        // Get the participacionProyecto
        restParticipacionProyectoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Transactional
    void putExistingParticipacionProyecto() throws Exception {
        // Initialize the database
        participacionProyectoRepository.saveAndFlush(participacionProyecto);

        int databaseSizeBeforeUpdate = participacionProyectoRepository.findAll().size();

        // Update the participacionProyecto
        ParticipacionProyecto updatedParticipacionProyecto = participacionProyectoRepository.findById(participacionProyecto.getId()).get();
        // Disconnect from session so that the updates on updatedParticipacionProyecto are not directly saved in db
        em.detach(updatedParticipacionProyecto);
        updatedParticipacionProyecto.esAdmin(UPDATED_ES_ADMIN);

        restParticipacionProyectoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedParticipacionProyecto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedParticipacionProyecto))
            )
            .andExpect(status().isOk());

        // Validate the ParticipacionProyecto in the database
        List<ParticipacionProyecto> participacionProyectoList = participacionProyectoRepository.findAll();
        assertThat(participacionProyectoList).hasSize(databaseSizeBeforeUpdate);
        ParticipacionProyecto testParticipacionProyecto = participacionProyectoList.get(participacionProyectoList.size() - 1);
        assertThat(testParticipacionProyecto.getEsAdmin()).isEqualTo(UPDATED_ES_ADMIN);
    }

    @Transactional
    void putNonExistingParticipacionProyecto() throws Exception {
        int databaseSizeBeforeUpdate = participacionProyectoRepository.findAll().size();
        participacionProyecto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParticipacionProyectoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, participacionProyecto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(participacionProyecto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParticipacionProyecto in the database
        List<ParticipacionProyecto> participacionProyectoList = participacionProyectoRepository.findAll();
        assertThat(participacionProyectoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Transactional
    void putWithIdMismatchParticipacionProyecto() throws Exception {
        int databaseSizeBeforeUpdate = participacionProyectoRepository.findAll().size();
        participacionProyecto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParticipacionProyectoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(participacionProyecto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParticipacionProyecto in the database
        List<ParticipacionProyecto> participacionProyectoList = participacionProyectoRepository.findAll();
        assertThat(participacionProyectoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Transactional
    void putWithMissingIdPathParamParticipacionProyecto() throws Exception {
        int databaseSizeBeforeUpdate = participacionProyectoRepository.findAll().size();
        participacionProyecto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParticipacionProyectoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(participacionProyecto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParticipacionProyecto in the database
        List<ParticipacionProyecto> participacionProyectoList = participacionProyectoRepository.findAll();
        assertThat(participacionProyectoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Transactional
    void partialUpdateParticipacionProyectoWithPatch() throws Exception {
        // Initialize the database
        participacionProyectoRepository.saveAndFlush(participacionProyecto);

        int databaseSizeBeforeUpdate = participacionProyectoRepository.findAll().size();

        // Update the participacionProyecto using partial update
        ParticipacionProyecto partialUpdatedParticipacionProyecto = new ParticipacionProyecto();
        partialUpdatedParticipacionProyecto.setId(participacionProyecto.getId());

        partialUpdatedParticipacionProyecto.esAdmin(UPDATED_ES_ADMIN);

        restParticipacionProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParticipacionProyecto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParticipacionProyecto))
            )
            .andExpect(status().isOk());

        // Validate the ParticipacionProyecto in the database
        List<ParticipacionProyecto> participacionProyectoList = participacionProyectoRepository.findAll();
        assertThat(participacionProyectoList).hasSize(databaseSizeBeforeUpdate);
        ParticipacionProyecto testParticipacionProyecto = participacionProyectoList.get(participacionProyectoList.size() - 1);
        assertThat(testParticipacionProyecto.getEsAdmin()).isEqualTo(UPDATED_ES_ADMIN);
    }

    @Transactional
    void fullUpdateParticipacionProyectoWithPatch() throws Exception {
        // Initialize the database
        participacionProyectoRepository.saveAndFlush(participacionProyecto);

        int databaseSizeBeforeUpdate = participacionProyectoRepository.findAll().size();

        // Update the participacionProyecto using partial update
        ParticipacionProyecto partialUpdatedParticipacionProyecto = new ParticipacionProyecto();
        partialUpdatedParticipacionProyecto.setId(participacionProyecto.getId());

        partialUpdatedParticipacionProyecto.esAdmin(UPDATED_ES_ADMIN);

        restParticipacionProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParticipacionProyecto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParticipacionProyecto))
            )
            .andExpect(status().isOk());

        // Validate the ParticipacionProyecto in the database
        List<ParticipacionProyecto> participacionProyectoList = participacionProyectoRepository.findAll();
        assertThat(participacionProyectoList).hasSize(databaseSizeBeforeUpdate);
        ParticipacionProyecto testParticipacionProyecto = participacionProyectoList.get(participacionProyectoList.size() - 1);
        assertThat(testParticipacionProyecto.getEsAdmin()).isEqualTo(UPDATED_ES_ADMIN);
    }

    @Transactional
    void patchNonExistingParticipacionProyecto() throws Exception {
        int databaseSizeBeforeUpdate = participacionProyectoRepository.findAll().size();
        participacionProyecto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParticipacionProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, participacionProyecto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(participacionProyecto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParticipacionProyecto in the database
        List<ParticipacionProyecto> participacionProyectoList = participacionProyectoRepository.findAll();
        assertThat(participacionProyectoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Transactional
    void patchWithIdMismatchParticipacionProyecto() throws Exception {
        int databaseSizeBeforeUpdate = participacionProyectoRepository.findAll().size();
        participacionProyecto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParticipacionProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(participacionProyecto))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParticipacionProyecto in the database
        List<ParticipacionProyecto> participacionProyectoList = participacionProyectoRepository.findAll();
        assertThat(participacionProyectoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Transactional
    void patchWithMissingIdPathParamParticipacionProyecto() throws Exception {
        int databaseSizeBeforeUpdate = participacionProyectoRepository.findAll().size();
        participacionProyecto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParticipacionProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(participacionProyecto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParticipacionProyecto in the database
        List<ParticipacionProyecto> participacionProyectoList = participacionProyectoRepository.findAll();
        assertThat(participacionProyectoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Transactional
    void deleteParticipacionProyecto() throws Exception {
        // Initialize the database
        participacionProyectoRepository.saveAndFlush(participacionProyecto);

        int databaseSizeBeforeDelete = participacionProyectoRepository.findAll().size();

        // Delete the participacionProyecto
        restParticipacionProyectoMockMvc
            .perform(delete(ENTITY_API_URL_ID, participacionProyecto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ParticipacionProyecto> participacionProyectoList = participacionProyectoRepository.findAll();
        assertThat(participacionProyectoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
