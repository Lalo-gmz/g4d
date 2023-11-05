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
import mx.lania.g4d.domain.Bitacora;
import mx.lania.g4d.repository.BitacoraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BitacoraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BitacoraResourceIT {

    private static final String DEFAULT_ACCION = "AAAAAAAAAA";
    private static final String UPDATED_ACCION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREADO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREADO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/bitacoras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BitacoraRepository bitacoraRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBitacoraMockMvc;

    private Bitacora bitacora;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bitacora createEntity(EntityManager em) {
        Bitacora bitacora = new Bitacora().accion(DEFAULT_ACCION).creado(DEFAULT_CREADO);
        return bitacora;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bitacora createUpdatedEntity(EntityManager em) {
        Bitacora bitacora = new Bitacora().accion(UPDATED_ACCION).creado(UPDATED_CREADO);
        return bitacora;
    }

    @BeforeEach
    public void initTest() {
        bitacora = createEntity(em);
    }

    @Transactional
    void createBitacora() throws Exception {
        int databaseSizeBeforeCreate = bitacoraRepository.findAll().size();
        // Create the Bitacora
        restBitacoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bitacora)))
            .andExpect(status().isCreated());

        // Validate the Bitacora in the database
        List<Bitacora> bitacoraList = bitacoraRepository.findAll();
        assertThat(bitacoraList).hasSize(databaseSizeBeforeCreate + 1);
        Bitacora testBitacora = bitacoraList.get(bitacoraList.size() - 1);
        assertThat(testBitacora.getAccion()).isEqualTo(DEFAULT_ACCION);
        assertThat(testBitacora.getCreado()).isEqualTo(DEFAULT_CREADO);
    }

    @Transactional
    void createBitacoraWithExistingId() throws Exception {
        // Create the Bitacora with an existing ID
        bitacora.setId(1L);

        int databaseSizeBeforeCreate = bitacoraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBitacoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bitacora)))
            .andExpect(status().isBadRequest());

        // Validate the Bitacora in the database
        List<Bitacora> bitacoraList = bitacoraRepository.findAll();
        assertThat(bitacoraList).hasSize(databaseSizeBeforeCreate);
    }

    @Transactional
    void checkAccionIsRequired() throws Exception {
        int databaseSizeBeforeTest = bitacoraRepository.findAll().size();
        // set the field null
        bitacora.setAccion(null);

        // Create the Bitacora, which fails.

        restBitacoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bitacora)))
            .andExpect(status().isBadRequest());

        List<Bitacora> bitacoraList = bitacoraRepository.findAll();
        assertThat(bitacoraList).hasSize(databaseSizeBeforeTest);
    }

    @Transactional
    void checkCreadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = bitacoraRepository.findAll().size();
        // set the field null
        bitacora.setCreado(null);

        // Create the Bitacora, which fails.

        restBitacoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bitacora)))
            .andExpect(status().isBadRequest());

        List<Bitacora> bitacoraList = bitacoraRepository.findAll();
        assertThat(bitacoraList).hasSize(databaseSizeBeforeTest);
    }

    @Transactional
    void getAllBitacoras() throws Exception {
        // Initialize the database
        bitacoraRepository.saveAndFlush(bitacora);

        // Get all the bitacoraList
        restBitacoraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bitacora.getId().intValue())))
            .andExpect(jsonPath("$.[*].accion").value(hasItem(DEFAULT_ACCION)))
            .andExpect(jsonPath("$.[*].creado").value(hasItem(DEFAULT_CREADO.toString())));
    }

    @Transactional
    void getBitacora() throws Exception {
        // Initialize the database
        bitacoraRepository.saveAndFlush(bitacora);

        // Get the bitacora
        restBitacoraMockMvc
            .perform(get(ENTITY_API_URL_ID, bitacora.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bitacora.getId().intValue()))
            .andExpect(jsonPath("$.accion").value(DEFAULT_ACCION))
            .andExpect(jsonPath("$.creado").value(DEFAULT_CREADO.toString()));
    }

    @Transactional
    void getNonExistingBitacora() throws Exception {
        // Get the bitacora
        restBitacoraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Transactional
    void putExistingBitacora() throws Exception {
        // Initialize the database
        bitacoraRepository.saveAndFlush(bitacora);

        int databaseSizeBeforeUpdate = bitacoraRepository.findAll().size();

        // Update the bitacora
        Bitacora updatedBitacora = bitacoraRepository.findById(bitacora.getId()).get();
        // Disconnect from session so that the updates on updatedBitacora are not directly saved in db
        em.detach(updatedBitacora);
        updatedBitacora.accion(UPDATED_ACCION).creado(UPDATED_CREADO);

        restBitacoraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBitacora.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBitacora))
            )
            .andExpect(status().isOk());

        // Validate the Bitacora in the database
        List<Bitacora> bitacoraList = bitacoraRepository.findAll();
        assertThat(bitacoraList).hasSize(databaseSizeBeforeUpdate);
        Bitacora testBitacora = bitacoraList.get(bitacoraList.size() - 1);
        assertThat(testBitacora.getAccion()).isEqualTo(UPDATED_ACCION);
        assertThat(testBitacora.getCreado()).isEqualTo(UPDATED_CREADO);
    }

    @Transactional
    void putNonExistingBitacora() throws Exception {
        int databaseSizeBeforeUpdate = bitacoraRepository.findAll().size();
        bitacora.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBitacoraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bitacora.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bitacora))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bitacora in the database
        List<Bitacora> bitacoraList = bitacoraRepository.findAll();
        assertThat(bitacoraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Transactional
    void putWithIdMismatchBitacora() throws Exception {
        int databaseSizeBeforeUpdate = bitacoraRepository.findAll().size();
        bitacora.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBitacoraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bitacora))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bitacora in the database
        List<Bitacora> bitacoraList = bitacoraRepository.findAll();
        assertThat(bitacoraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Transactional
    void putWithMissingIdPathParamBitacora() throws Exception {
        int databaseSizeBeforeUpdate = bitacoraRepository.findAll().size();
        bitacora.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBitacoraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bitacora)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bitacora in the database
        List<Bitacora> bitacoraList = bitacoraRepository.findAll();
        assertThat(bitacoraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Transactional
    void partialUpdateBitacoraWithPatch() throws Exception {
        // Initialize the database
        bitacoraRepository.saveAndFlush(bitacora);

        int databaseSizeBeforeUpdate = bitacoraRepository.findAll().size();

        // Update the bitacora using partial update
        Bitacora partialUpdatedBitacora = new Bitacora();
        partialUpdatedBitacora.setId(bitacora.getId());

        restBitacoraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBitacora.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBitacora))
            )
            .andExpect(status().isOk());

        // Validate the Bitacora in the database
        List<Bitacora> bitacoraList = bitacoraRepository.findAll();
        assertThat(bitacoraList).hasSize(databaseSizeBeforeUpdate);
        Bitacora testBitacora = bitacoraList.get(bitacoraList.size() - 1);
        assertThat(testBitacora.getAccion()).isEqualTo(DEFAULT_ACCION);
        assertThat(testBitacora.getCreado()).isEqualTo(DEFAULT_CREADO);
    }

    @Transactional
    void fullUpdateBitacoraWithPatch() throws Exception {
        // Initialize the database
        bitacoraRepository.saveAndFlush(bitacora);

        int databaseSizeBeforeUpdate = bitacoraRepository.findAll().size();

        // Update the bitacora using partial update
        Bitacora partialUpdatedBitacora = new Bitacora();
        partialUpdatedBitacora.setId(bitacora.getId());

        partialUpdatedBitacora.accion(UPDATED_ACCION).creado(UPDATED_CREADO);

        restBitacoraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBitacora.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBitacora))
            )
            .andExpect(status().isOk());

        // Validate the Bitacora in the database
        List<Bitacora> bitacoraList = bitacoraRepository.findAll();
        assertThat(bitacoraList).hasSize(databaseSizeBeforeUpdate);
        Bitacora testBitacora = bitacoraList.get(bitacoraList.size() - 1);
        assertThat(testBitacora.getAccion()).isEqualTo(UPDATED_ACCION);
        assertThat(testBitacora.getCreado()).isEqualTo(UPDATED_CREADO);
    }

    @Transactional
    void patchNonExistingBitacora() throws Exception {
        int databaseSizeBeforeUpdate = bitacoraRepository.findAll().size();
        bitacora.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBitacoraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bitacora.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bitacora))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bitacora in the database
        List<Bitacora> bitacoraList = bitacoraRepository.findAll();
        assertThat(bitacoraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Transactional
    void patchWithIdMismatchBitacora() throws Exception {
        int databaseSizeBeforeUpdate = bitacoraRepository.findAll().size();
        bitacora.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBitacoraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bitacora))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bitacora in the database
        List<Bitacora> bitacoraList = bitacoraRepository.findAll();
        assertThat(bitacoraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Transactional
    void patchWithMissingIdPathParamBitacora() throws Exception {
        int databaseSizeBeforeUpdate = bitacoraRepository.findAll().size();
        bitacora.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBitacoraMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bitacora)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bitacora in the database
        List<Bitacora> bitacoraList = bitacoraRepository.findAll();
        assertThat(bitacoraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Transactional
    void deleteBitacora() throws Exception {
        // Initialize the database
        bitacoraRepository.saveAndFlush(bitacora);

        int databaseSizeBeforeDelete = bitacoraRepository.findAll().size();

        // Delete the bitacora
        restBitacoraMockMvc
            .perform(delete(ENTITY_API_URL_ID, bitacora.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bitacora> bitacoraList = bitacoraRepository.findAll();
        assertThat(bitacoraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
