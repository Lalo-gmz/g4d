package mx.lania.mca.g4d.web.rest;

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
import mx.lania.mca.g4d.IntegrationTest;
import mx.lania.mca.g4d.domain.Iteracion;
import mx.lania.mca.g4d.repository.IteracionRepository;
import mx.lania.mca.g4d.service.dto.IteracionDTO;
import mx.lania.mca.g4d.service.mapper.IteracionMapper;
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

    private static final LocalDate DEFAULT_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/iteracions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IteracionRepository iteracionRepository;

    @Autowired
    private IteracionMapper iteracionMapper;

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
        IteracionDTO iteracionDTO = iteracionMapper.toDto(iteracion);
        restIteracionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(iteracionDTO)))
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
        IteracionDTO iteracionDTO = iteracionMapper.toDto(iteracion);

        int databaseSizeBeforeCreate = iteracionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIteracionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(iteracionDTO)))
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
        IteracionDTO iteracionDTO = iteracionMapper.toDto(iteracion);

        restIteracionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(iteracionDTO)))
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
        IteracionDTO iteracionDTO = iteracionMapper.toDto(updatedIteracion);

        restIteracionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, iteracionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iteracionDTO))
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

        // Create the Iteracion
        IteracionDTO iteracionDTO = iteracionMapper.toDto(iteracion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIteracionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, iteracionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iteracionDTO))
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

        // Create the Iteracion
        IteracionDTO iteracionDTO = iteracionMapper.toDto(iteracion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIteracionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iteracionDTO))
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

        // Create the Iteracion
        IteracionDTO iteracionDTO = iteracionMapper.toDto(iteracion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIteracionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(iteracionDTO)))
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

        // Create the Iteracion
        IteracionDTO iteracionDTO = iteracionMapper.toDto(iteracion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIteracionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, iteracionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(iteracionDTO))
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

        // Create the Iteracion
        IteracionDTO iteracionDTO = iteracionMapper.toDto(iteracion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIteracionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(iteracionDTO))
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

        // Create the Iteracion
        IteracionDTO iteracionDTO = iteracionMapper.toDto(iteracion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIteracionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(iteracionDTO))
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
