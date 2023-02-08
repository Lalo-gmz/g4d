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
import mx.lania.mca.g4d.domain.Rol;
import mx.lania.mca.g4d.repository.RolRepository;
import mx.lania.mca.g4d.service.dto.RolDTO;
import mx.lania.mca.g4d.service.mapper.RolMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RolResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RolResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rols";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private RolMapper rolMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRolMockMvc;

    private Rol rol;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rol createEntity(EntityManager em) {
        Rol rol = new Rol().nombre(DEFAULT_NOMBRE);
        return rol;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rol createUpdatedEntity(EntityManager em) {
        Rol rol = new Rol().nombre(UPDATED_NOMBRE);
        return rol;
    }

    @BeforeEach
    public void initTest() {
        rol = createEntity(em);
    }

    @Test
    @Transactional
    void createRol() throws Exception {
        int databaseSizeBeforeCreate = rolRepository.findAll().size();
        // Create the Rol
        RolDTO rolDTO = rolMapper.toDto(rol);
        restRolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rolDTO)))
            .andExpect(status().isCreated());

        // Validate the Rol in the database
        List<Rol> rolList = rolRepository.findAll();
        assertThat(rolList).hasSize(databaseSizeBeforeCreate + 1);
        Rol testRol = rolList.get(rolList.size() - 1);
        assertThat(testRol.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void createRolWithExistingId() throws Exception {
        // Create the Rol with an existing ID
        rol.setId(1L);
        RolDTO rolDTO = rolMapper.toDto(rol);

        int databaseSizeBeforeCreate = rolRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rolDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rol in the database
        List<Rol> rolList = rolRepository.findAll();
        assertThat(rolList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRols() throws Exception {
        // Initialize the database
        rolRepository.saveAndFlush(rol);

        // Get all the rolList
        restRolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rol.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    void getRol() throws Exception {
        // Initialize the database
        rolRepository.saveAndFlush(rol);

        // Get the rol
        restRolMockMvc
            .perform(get(ENTITY_API_URL_ID, rol.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rol.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    void getNonExistingRol() throws Exception {
        // Get the rol
        restRolMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRol() throws Exception {
        // Initialize the database
        rolRepository.saveAndFlush(rol);

        int databaseSizeBeforeUpdate = rolRepository.findAll().size();

        // Update the rol
        Rol updatedRol = rolRepository.findById(rol.getId()).get();
        // Disconnect from session so that the updates on updatedRol are not directly saved in db
        em.detach(updatedRol);
        updatedRol.nombre(UPDATED_NOMBRE);
        RolDTO rolDTO = rolMapper.toDto(updatedRol);

        restRolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rolDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rolDTO))
            )
            .andExpect(status().isOk());

        // Validate the Rol in the database
        List<Rol> rolList = rolRepository.findAll();
        assertThat(rolList).hasSize(databaseSizeBeforeUpdate);
        Rol testRol = rolList.get(rolList.size() - 1);
        assertThat(testRol.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void putNonExistingRol() throws Exception {
        int databaseSizeBeforeUpdate = rolRepository.findAll().size();
        rol.setId(count.incrementAndGet());

        // Create the Rol
        RolDTO rolDTO = rolMapper.toDto(rol);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rolDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rol in the database
        List<Rol> rolList = rolRepository.findAll();
        assertThat(rolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRol() throws Exception {
        int databaseSizeBeforeUpdate = rolRepository.findAll().size();
        rol.setId(count.incrementAndGet());

        // Create the Rol
        RolDTO rolDTO = rolMapper.toDto(rol);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rol in the database
        List<Rol> rolList = rolRepository.findAll();
        assertThat(rolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRol() throws Exception {
        int databaseSizeBeforeUpdate = rolRepository.findAll().size();
        rol.setId(count.incrementAndGet());

        // Create the Rol
        RolDTO rolDTO = rolMapper.toDto(rol);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRolMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rolDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rol in the database
        List<Rol> rolList = rolRepository.findAll();
        assertThat(rolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRolWithPatch() throws Exception {
        // Initialize the database
        rolRepository.saveAndFlush(rol);

        int databaseSizeBeforeUpdate = rolRepository.findAll().size();

        // Update the rol using partial update
        Rol partialUpdatedRol = new Rol();
        partialUpdatedRol.setId(rol.getId());

        partialUpdatedRol.nombre(UPDATED_NOMBRE);

        restRolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRol.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRol))
            )
            .andExpect(status().isOk());

        // Validate the Rol in the database
        List<Rol> rolList = rolRepository.findAll();
        assertThat(rolList).hasSize(databaseSizeBeforeUpdate);
        Rol testRol = rolList.get(rolList.size() - 1);
        assertThat(testRol.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void fullUpdateRolWithPatch() throws Exception {
        // Initialize the database
        rolRepository.saveAndFlush(rol);

        int databaseSizeBeforeUpdate = rolRepository.findAll().size();

        // Update the rol using partial update
        Rol partialUpdatedRol = new Rol();
        partialUpdatedRol.setId(rol.getId());

        partialUpdatedRol.nombre(UPDATED_NOMBRE);

        restRolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRol.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRol))
            )
            .andExpect(status().isOk());

        // Validate the Rol in the database
        List<Rol> rolList = rolRepository.findAll();
        assertThat(rolList).hasSize(databaseSizeBeforeUpdate);
        Rol testRol = rolList.get(rolList.size() - 1);
        assertThat(testRol.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void patchNonExistingRol() throws Exception {
        int databaseSizeBeforeUpdate = rolRepository.findAll().size();
        rol.setId(count.incrementAndGet());

        // Create the Rol
        RolDTO rolDTO = rolMapper.toDto(rol);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rolDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rol in the database
        List<Rol> rolList = rolRepository.findAll();
        assertThat(rolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRol() throws Exception {
        int databaseSizeBeforeUpdate = rolRepository.findAll().size();
        rol.setId(count.incrementAndGet());

        // Create the Rol
        RolDTO rolDTO = rolMapper.toDto(rol);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rol in the database
        List<Rol> rolList = rolRepository.findAll();
        assertThat(rolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRol() throws Exception {
        int databaseSizeBeforeUpdate = rolRepository.findAll().size();
        rol.setId(count.incrementAndGet());

        // Create the Rol
        RolDTO rolDTO = rolMapper.toDto(rol);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRolMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rolDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rol in the database
        List<Rol> rolList = rolRepository.findAll();
        assertThat(rolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRol() throws Exception {
        // Initialize the database
        rolRepository.saveAndFlush(rol);

        int databaseSizeBeforeDelete = rolRepository.findAll().size();

        // Delete the rol
        restRolMockMvc.perform(delete(ENTITY_API_URL_ID, rol.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rol> rolList = rolRepository.findAll();
        assertThat(rolList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
