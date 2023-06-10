package mx.lania.g4d.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import mx.lania.g4d.IntegrationTest;
import mx.lania.g4d.domain.Captura;
import mx.lania.g4d.repository.CapturaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CapturaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CapturaResourceIT {

    private static final String DEFAULT_FUNCIONALIDADES = "AAAAAAAAAA";
    private static final String UPDATED_FUNCIONALIDADES = "BBBBBBBBBB";

    private static final String DEFAULT_ESTATUS = "AAAAAAAAAA";
    private static final String UPDATED_ESTATUS = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/capturas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CapturaRepository capturaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCapturaMockMvc;

    private Captura captura;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Captura createEntity(EntityManager em) {
        Captura captura = new Captura().funcionalidades(DEFAULT_FUNCIONALIDADES).fecha(DEFAULT_FECHA);
        return captura;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Captura createUpdatedEntity(EntityManager em) {
        Captura captura = new Captura().funcionalidades(UPDATED_FUNCIONALIDADES).fecha(UPDATED_FECHA);
        return captura;
    }

    @BeforeEach
    public void initTest() {
        captura = createEntity(em);
    }

    @Test
    @Transactional
    void createCaptura() throws Exception {
        int databaseSizeBeforeCreate = capturaRepository.findAll().size();
        // Create the Captura
        restCapturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(captura)))
            .andExpect(status().isCreated());

        // Validate the Captura in the database
        List<Captura> capturaList = capturaRepository.findAll();
        assertThat(capturaList).hasSize(databaseSizeBeforeCreate + 1);
        Captura testCaptura = capturaList.get(capturaList.size() - 1);
        assertThat(testCaptura.getFuncionalidades()).isEqualTo(DEFAULT_FUNCIONALIDADES);
        assertThat(testCaptura.getEstatus()).isEqualTo(DEFAULT_ESTATUS);
        assertThat(testCaptura.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    void createCapturaWithExistingId() throws Exception {
        // Create the Captura with an existing ID
        captura.setId(1L);

        int databaseSizeBeforeCreate = capturaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCapturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(captura)))
            .andExpect(status().isBadRequest());

        // Validate the Captura in the database
        List<Captura> capturaList = capturaRepository.findAll();
        assertThat(capturaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCapturas() throws Exception {
        // Initialize the database
        capturaRepository.saveAndFlush(captura);

        // Get all the capturaList
        restCapturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(captura.getId().intValue())))
            .andExpect(jsonPath("$.[*].funcionalidades").value(hasItem(DEFAULT_FUNCIONALIDADES)))
            .andExpect(jsonPath("$.[*].estatus").value(hasItem(DEFAULT_ESTATUS)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));
    }

    @Test
    @Transactional
    void getCaptura() throws Exception {
        // Initialize the database
        capturaRepository.saveAndFlush(captura);

        // Get the captura
        restCapturaMockMvc
            .perform(get(ENTITY_API_URL_ID, captura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(captura.getId().intValue()))
            .andExpect(jsonPath("$.funcionalidades").value(DEFAULT_FUNCIONALIDADES))
            .andExpect(jsonPath("$.estatus").value(DEFAULT_ESTATUS))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCaptura() throws Exception {
        // Get the captura
        restCapturaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCaptura() throws Exception {
        // Initialize the database
        capturaRepository.saveAndFlush(captura);

        int databaseSizeBeforeUpdate = capturaRepository.findAll().size();

        // Update the captura
        Captura updatedCaptura = capturaRepository.findById(captura.getId()).get();
        // Disconnect from session so that the updates on updatedCaptura are not directly saved in db
        em.detach(updatedCaptura);
        updatedCaptura.funcionalidades(UPDATED_FUNCIONALIDADES).estatus(UPDATED_ESTATUS).fecha(UPDATED_FECHA);

        restCapturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCaptura.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCaptura))
            )
            .andExpect(status().isOk());

        // Validate the Captura in the database
        List<Captura> capturaList = capturaRepository.findAll();
        assertThat(capturaList).hasSize(databaseSizeBeforeUpdate);
        Captura testCaptura = capturaList.get(capturaList.size() - 1);
        assertThat(testCaptura.getFuncionalidades()).isEqualTo(UPDATED_FUNCIONALIDADES);
        assertThat(testCaptura.getEstatus()).isEqualTo(UPDATED_ESTATUS);
        assertThat(testCaptura.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void putNonExistingCaptura() throws Exception {
        int databaseSizeBeforeUpdate = capturaRepository.findAll().size();
        captura.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCapturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, captura.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(captura))
            )
            .andExpect(status().isBadRequest());

        // Validate the Captura in the database
        List<Captura> capturaList = capturaRepository.findAll();
        assertThat(capturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCaptura() throws Exception {
        int databaseSizeBeforeUpdate = capturaRepository.findAll().size();
        captura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(captura))
            )
            .andExpect(status().isBadRequest());

        // Validate the Captura in the database
        List<Captura> capturaList = capturaRepository.findAll();
        assertThat(capturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCaptura() throws Exception {
        int databaseSizeBeforeUpdate = capturaRepository.findAll().size();
        captura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapturaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(captura)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Captura in the database
        List<Captura> capturaList = capturaRepository.findAll();
        assertThat(capturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCapturaWithPatch() throws Exception {
        // Initialize the database
        capturaRepository.saveAndFlush(captura);

        int databaseSizeBeforeUpdate = capturaRepository.findAll().size();

        // Update the captura using partial update
        Captura partialUpdatedCaptura = new Captura();
        partialUpdatedCaptura.setId(captura.getId());

        restCapturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaptura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCaptura))
            )
            .andExpect(status().isOk());

        // Validate the Captura in the database
        List<Captura> capturaList = capturaRepository.findAll();
        assertThat(capturaList).hasSize(databaseSizeBeforeUpdate);
        Captura testCaptura = capturaList.get(capturaList.size() - 1);
        assertThat(testCaptura.getFuncionalidades()).isEqualTo(DEFAULT_FUNCIONALIDADES);
        assertThat(testCaptura.getEstatus()).isEqualTo(DEFAULT_ESTATUS);
        assertThat(testCaptura.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    void fullUpdateCapturaWithPatch() throws Exception {
        // Initialize the database
        capturaRepository.saveAndFlush(captura);

        int databaseSizeBeforeUpdate = capturaRepository.findAll().size();

        // Update the captura using partial update
        Captura partialUpdatedCaptura = new Captura();
        partialUpdatedCaptura.setId(captura.getId());

        partialUpdatedCaptura.funcionalidades(UPDATED_FUNCIONALIDADES).estatus(UPDATED_ESTATUS).fecha(UPDATED_FECHA);

        restCapturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaptura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCaptura))
            )
            .andExpect(status().isOk());

        // Validate the Captura in the database
        List<Captura> capturaList = capturaRepository.findAll();
        assertThat(capturaList).hasSize(databaseSizeBeforeUpdate);
        Captura testCaptura = capturaList.get(capturaList.size() - 1);
        assertThat(testCaptura.getFuncionalidades()).isEqualTo(UPDATED_FUNCIONALIDADES);
        assertThat(testCaptura.getEstatus()).isEqualTo(UPDATED_ESTATUS);
        assertThat(testCaptura.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void patchNonExistingCaptura() throws Exception {
        int databaseSizeBeforeUpdate = capturaRepository.findAll().size();
        captura.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCapturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, captura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(captura))
            )
            .andExpect(status().isBadRequest());

        // Validate the Captura in the database
        List<Captura> capturaList = capturaRepository.findAll();
        assertThat(capturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCaptura() throws Exception {
        int databaseSizeBeforeUpdate = capturaRepository.findAll().size();
        captura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(captura))
            )
            .andExpect(status().isBadRequest());

        // Validate the Captura in the database
        List<Captura> capturaList = capturaRepository.findAll();
        assertThat(capturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCaptura() throws Exception {
        int databaseSizeBeforeUpdate = capturaRepository.findAll().size();
        captura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapturaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(captura)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Captura in the database
        List<Captura> capturaList = capturaRepository.findAll();
        assertThat(capturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCaptura() throws Exception {
        // Initialize the database
        capturaRepository.saveAndFlush(captura);

        int databaseSizeBeforeDelete = capturaRepository.findAll().size();

        // Delete the captura
        restCapturaMockMvc
            .perform(delete(ENTITY_API_URL_ID, captura.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Captura> capturaList = capturaRepository.findAll();
        assertThat(capturaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
