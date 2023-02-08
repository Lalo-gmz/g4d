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
import mx.lania.mca.g4d.domain.Permiso;
import mx.lania.mca.g4d.repository.PermisoRepository;
import mx.lania.mca.g4d.service.dto.PermisoDTO;
import mx.lania.mca.g4d.service.mapper.PermisoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PermisoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PermisoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/permisos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PermisoRepository permisoRepository;

    @Autowired
    private PermisoMapper permisoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPermisoMockMvc;

    private Permiso permiso;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Permiso createEntity(EntityManager em) {
        Permiso permiso = new Permiso().nombre(DEFAULT_NOMBRE);
        return permiso;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Permiso createUpdatedEntity(EntityManager em) {
        Permiso permiso = new Permiso().nombre(UPDATED_NOMBRE);
        return permiso;
    }

    @BeforeEach
    public void initTest() {
        permiso = createEntity(em);
    }

    @Test
    @Transactional
    void createPermiso() throws Exception {
        int databaseSizeBeforeCreate = permisoRepository.findAll().size();
        // Create the Permiso
        PermisoDTO permisoDTO = permisoMapper.toDto(permiso);
        restPermisoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permisoDTO)))
            .andExpect(status().isCreated());

        // Validate the Permiso in the database
        List<Permiso> permisoList = permisoRepository.findAll();
        assertThat(permisoList).hasSize(databaseSizeBeforeCreate + 1);
        Permiso testPermiso = permisoList.get(permisoList.size() - 1);
        assertThat(testPermiso.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void createPermisoWithExistingId() throws Exception {
        // Create the Permiso with an existing ID
        permiso.setId(1L);
        PermisoDTO permisoDTO = permisoMapper.toDto(permiso);

        int databaseSizeBeforeCreate = permisoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPermisoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permisoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Permiso in the database
        List<Permiso> permisoList = permisoRepository.findAll();
        assertThat(permisoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPermisos() throws Exception {
        // Initialize the database
        permisoRepository.saveAndFlush(permiso);

        // Get all the permisoList
        restPermisoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permiso.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    void getPermiso() throws Exception {
        // Initialize the database
        permisoRepository.saveAndFlush(permiso);

        // Get the permiso
        restPermisoMockMvc
            .perform(get(ENTITY_API_URL_ID, permiso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(permiso.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    void getNonExistingPermiso() throws Exception {
        // Get the permiso
        restPermisoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPermiso() throws Exception {
        // Initialize the database
        permisoRepository.saveAndFlush(permiso);

        int databaseSizeBeforeUpdate = permisoRepository.findAll().size();

        // Update the permiso
        Permiso updatedPermiso = permisoRepository.findById(permiso.getId()).get();
        // Disconnect from session so that the updates on updatedPermiso are not directly saved in db
        em.detach(updatedPermiso);
        updatedPermiso.nombre(UPDATED_NOMBRE);
        PermisoDTO permisoDTO = permisoMapper.toDto(updatedPermiso);

        restPermisoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permisoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permisoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Permiso in the database
        List<Permiso> permisoList = permisoRepository.findAll();
        assertThat(permisoList).hasSize(databaseSizeBeforeUpdate);
        Permiso testPermiso = permisoList.get(permisoList.size() - 1);
        assertThat(testPermiso.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void putNonExistingPermiso() throws Exception {
        int databaseSizeBeforeUpdate = permisoRepository.findAll().size();
        permiso.setId(count.incrementAndGet());

        // Create the Permiso
        PermisoDTO permisoDTO = permisoMapper.toDto(permiso);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermisoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permisoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permisoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permiso in the database
        List<Permiso> permisoList = permisoRepository.findAll();
        assertThat(permisoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPermiso() throws Exception {
        int databaseSizeBeforeUpdate = permisoRepository.findAll().size();
        permiso.setId(count.incrementAndGet());

        // Create the Permiso
        PermisoDTO permisoDTO = permisoMapper.toDto(permiso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermisoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permisoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permiso in the database
        List<Permiso> permisoList = permisoRepository.findAll();
        assertThat(permisoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPermiso() throws Exception {
        int databaseSizeBeforeUpdate = permisoRepository.findAll().size();
        permiso.setId(count.incrementAndGet());

        // Create the Permiso
        PermisoDTO permisoDTO = permisoMapper.toDto(permiso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermisoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permisoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Permiso in the database
        List<Permiso> permisoList = permisoRepository.findAll();
        assertThat(permisoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePermisoWithPatch() throws Exception {
        // Initialize the database
        permisoRepository.saveAndFlush(permiso);

        int databaseSizeBeforeUpdate = permisoRepository.findAll().size();

        // Update the permiso using partial update
        Permiso partialUpdatedPermiso = new Permiso();
        partialUpdatedPermiso.setId(permiso.getId());

        partialUpdatedPermiso.nombre(UPDATED_NOMBRE);

        restPermisoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermiso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPermiso))
            )
            .andExpect(status().isOk());

        // Validate the Permiso in the database
        List<Permiso> permisoList = permisoRepository.findAll();
        assertThat(permisoList).hasSize(databaseSizeBeforeUpdate);
        Permiso testPermiso = permisoList.get(permisoList.size() - 1);
        assertThat(testPermiso.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void fullUpdatePermisoWithPatch() throws Exception {
        // Initialize the database
        permisoRepository.saveAndFlush(permiso);

        int databaseSizeBeforeUpdate = permisoRepository.findAll().size();

        // Update the permiso using partial update
        Permiso partialUpdatedPermiso = new Permiso();
        partialUpdatedPermiso.setId(permiso.getId());

        partialUpdatedPermiso.nombre(UPDATED_NOMBRE);

        restPermisoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermiso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPermiso))
            )
            .andExpect(status().isOk());

        // Validate the Permiso in the database
        List<Permiso> permisoList = permisoRepository.findAll();
        assertThat(permisoList).hasSize(databaseSizeBeforeUpdate);
        Permiso testPermiso = permisoList.get(permisoList.size() - 1);
        assertThat(testPermiso.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void patchNonExistingPermiso() throws Exception {
        int databaseSizeBeforeUpdate = permisoRepository.findAll().size();
        permiso.setId(count.incrementAndGet());

        // Create the Permiso
        PermisoDTO permisoDTO = permisoMapper.toDto(permiso);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermisoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, permisoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permisoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permiso in the database
        List<Permiso> permisoList = permisoRepository.findAll();
        assertThat(permisoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPermiso() throws Exception {
        int databaseSizeBeforeUpdate = permisoRepository.findAll().size();
        permiso.setId(count.incrementAndGet());

        // Create the Permiso
        PermisoDTO permisoDTO = permisoMapper.toDto(permiso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermisoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permisoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permiso in the database
        List<Permiso> permisoList = permisoRepository.findAll();
        assertThat(permisoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPermiso() throws Exception {
        int databaseSizeBeforeUpdate = permisoRepository.findAll().size();
        permiso.setId(count.incrementAndGet());

        // Create the Permiso
        PermisoDTO permisoDTO = permisoMapper.toDto(permiso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermisoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(permisoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Permiso in the database
        List<Permiso> permisoList = permisoRepository.findAll();
        assertThat(permisoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePermiso() throws Exception {
        // Initialize the database
        permisoRepository.saveAndFlush(permiso);

        int databaseSizeBeforeDelete = permisoRepository.findAll().size();

        // Delete the permiso
        restPermisoMockMvc
            .perform(delete(ENTITY_API_URL_ID, permiso.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Permiso> permisoList = permisoRepository.findAll();
        assertThat(permisoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
