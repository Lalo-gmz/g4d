package mx.lania.mca.g4d.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import mx.lania.mca.g4d.IntegrationTest;
import mx.lania.mca.g4d.domain.Atributo;
import mx.lania.mca.g4d.repository.AtributoRepository;
import mx.lania.mca.g4d.service.dto.AtributoDTO;
import mx.lania.mca.g4d.service.mapper.AtributoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AtributoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AtributoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_MARCADO = false;
    private static final Boolean UPDATED_MARCADO = true;

    private static final Boolean DEFAULT_AUXILIAR = false;
    private static final Boolean UPDATED_AUXILIAR = true;

    private static final String ENTITY_API_URL = "/api/atributos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AtributoRepository atributoRepository;

    @Autowired
    private AtributoMapper atributoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAtributoMockMvc;

    private Atributo atributo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Atributo createEntity(EntityManager em) {
        Atributo atributo = new Atributo().nombre(DEFAULT_NOMBRE).marcado(DEFAULT_MARCADO).auxiliar(DEFAULT_AUXILIAR);
        return atributo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Atributo createUpdatedEntity(EntityManager em) {
        Atributo atributo = new Atributo().nombre(UPDATED_NOMBRE).marcado(UPDATED_MARCADO).auxiliar(UPDATED_AUXILIAR);
        return atributo;
    }

    @BeforeEach
    public void initTest() {
        atributo = createEntity(em);
    }

    @Test
    @Transactional
    void createAtributo() throws Exception {
        int databaseSizeBeforeCreate = atributoRepository.findAll().size();
        // Create the Atributo
        AtributoDTO atributoDTO = atributoMapper.toDto(atributo);
        restAtributoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(atributoDTO)))
            .andExpect(status().isCreated());

        // Validate the Atributo in the database
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeCreate + 1);
        Atributo testAtributo = atributoList.get(atributoList.size() - 1);
        assertThat(testAtributo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testAtributo.getMarcado()).isEqualTo(DEFAULT_MARCADO);
        assertThat(testAtributo.getAuxiliar()).isEqualTo(DEFAULT_AUXILIAR);
    }

    @Test
    @Transactional
    void createAtributoWithExistingId() throws Exception {
        // Create the Atributo with an existing ID
        atributo.setId(1L);
        AtributoDTO atributoDTO = atributoMapper.toDto(atributo);

        int databaseSizeBeforeCreate = atributoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAtributoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(atributoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Atributo in the database
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAtributos() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        // Get all the atributoList
        restAtributoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(atributo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].marcado").value(hasItem(DEFAULT_MARCADO.booleanValue())))
            .andExpect(jsonPath("$.[*].auxiliar").value(hasItem(DEFAULT_AUXILIAR.booleanValue())));
    }

    @Test
    @Transactional
    void getAtributo() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        // Get the atributo
        restAtributoMockMvc
            .perform(get(ENTITY_API_URL_ID, atributo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(atributo.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.marcado").value(DEFAULT_MARCADO.booleanValue()))
            .andExpect(jsonPath("$.auxiliar").value(DEFAULT_AUXILIAR.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAtributo() throws Exception {
        // Get the atributo
        restAtributoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAtributo() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        int databaseSizeBeforeUpdate = atributoRepository.findAll().size();

        // Update the atributo
        Atributo updatedAtributo = atributoRepository.findById(atributo.getId()).get();
        // Disconnect from session so that the updates on updatedAtributo are not directly saved in db
        em.detach(updatedAtributo);
        updatedAtributo.nombre(UPDATED_NOMBRE).marcado(UPDATED_MARCADO).auxiliar(UPDATED_AUXILIAR);
        AtributoDTO atributoDTO = atributoMapper.toDto(updatedAtributo);

        restAtributoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, atributoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atributoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Atributo in the database
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeUpdate);
        Atributo testAtributo = atributoList.get(atributoList.size() - 1);
        assertThat(testAtributo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testAtributo.getMarcado()).isEqualTo(UPDATED_MARCADO);
        assertThat(testAtributo.getAuxiliar()).isEqualTo(UPDATED_AUXILIAR);
    }

    @Test
    @Transactional
    void putNonExistingAtributo() throws Exception {
        int databaseSizeBeforeUpdate = atributoRepository.findAll().size();
        atributo.setId(count.incrementAndGet());

        // Create the Atributo
        AtributoDTO atributoDTO = atributoMapper.toDto(atributo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAtributoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, atributoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atributoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Atributo in the database
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAtributo() throws Exception {
        int databaseSizeBeforeUpdate = atributoRepository.findAll().size();
        atributo.setId(count.incrementAndGet());

        // Create the Atributo
        AtributoDTO atributoDTO = atributoMapper.toDto(atributo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtributoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atributoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Atributo in the database
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAtributo() throws Exception {
        int databaseSizeBeforeUpdate = atributoRepository.findAll().size();
        atributo.setId(count.incrementAndGet());

        // Create the Atributo
        AtributoDTO atributoDTO = atributoMapper.toDto(atributo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtributoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(atributoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Atributo in the database
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAtributoWithPatch() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        int databaseSizeBeforeUpdate = atributoRepository.findAll().size();

        // Update the atributo using partial update
        Atributo partialUpdatedAtributo = new Atributo();
        partialUpdatedAtributo.setId(atributo.getId());

        partialUpdatedAtributo.nombre(UPDATED_NOMBRE).marcado(UPDATED_MARCADO).auxiliar(UPDATED_AUXILIAR);

        restAtributoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAtributo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAtributo))
            )
            .andExpect(status().isOk());

        // Validate the Atributo in the database
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeUpdate);
        Atributo testAtributo = atributoList.get(atributoList.size() - 1);
        assertThat(testAtributo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testAtributo.getMarcado()).isEqualTo(UPDATED_MARCADO);
        assertThat(testAtributo.getAuxiliar()).isEqualTo(UPDATED_AUXILIAR);
    }

    @Test
    @Transactional
    void fullUpdateAtributoWithPatch() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        int databaseSizeBeforeUpdate = atributoRepository.findAll().size();

        // Update the atributo using partial update
        Atributo partialUpdatedAtributo = new Atributo();
        partialUpdatedAtributo.setId(atributo.getId());

        partialUpdatedAtributo.nombre(UPDATED_NOMBRE).marcado(UPDATED_MARCADO).auxiliar(UPDATED_AUXILIAR);

        restAtributoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAtributo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAtributo))
            )
            .andExpect(status().isOk());

        // Validate the Atributo in the database
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeUpdate);
        Atributo testAtributo = atributoList.get(atributoList.size() - 1);
        assertThat(testAtributo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testAtributo.getMarcado()).isEqualTo(UPDATED_MARCADO);
        assertThat(testAtributo.getAuxiliar()).isEqualTo(UPDATED_AUXILIAR);
    }

    @Test
    @Transactional
    void patchNonExistingAtributo() throws Exception {
        int databaseSizeBeforeUpdate = atributoRepository.findAll().size();
        atributo.setId(count.incrementAndGet());

        // Create the Atributo
        AtributoDTO atributoDTO = atributoMapper.toDto(atributo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAtributoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, atributoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(atributoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Atributo in the database
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAtributo() throws Exception {
        int databaseSizeBeforeUpdate = atributoRepository.findAll().size();
        atributo.setId(count.incrementAndGet());

        // Create the Atributo
        AtributoDTO atributoDTO = atributoMapper.toDto(atributo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtributoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(atributoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Atributo in the database
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAtributo() throws Exception {
        int databaseSizeBeforeUpdate = atributoRepository.findAll().size();
        atributo.setId(count.incrementAndGet());

        // Create the Atributo
        AtributoDTO atributoDTO = atributoMapper.toDto(atributo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtributoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(atributoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Atributo in the database
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAtributo() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        int databaseSizeBeforeDelete = atributoRepository.findAll().size();

        // Delete the atributo
        restAtributoMockMvc
            .perform(delete(ENTITY_API_URL_ID, atributo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
