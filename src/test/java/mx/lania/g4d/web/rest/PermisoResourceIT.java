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
import mx.lania.g4d.domain.Permiso;
import mx.lania.g4d.domain.Rol;
import mx.lania.g4d.repository.PermisoRepository;
import mx.lania.g4d.service.criteria.PermisoCriteria;
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
        restPermisoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permiso)))
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

        int databaseSizeBeforeCreate = permisoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPermisoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permiso)))
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
    void getPermisosByIdFiltering() throws Exception {
        // Initialize the database
        permisoRepository.saveAndFlush(permiso);

        Long id = permiso.getId();

        defaultPermisoShouldBeFound("id.equals=" + id);
        defaultPermisoShouldNotBeFound("id.notEquals=" + id);

        defaultPermisoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPermisoShouldNotBeFound("id.greaterThan=" + id);

        defaultPermisoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPermisoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPermisosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        permisoRepository.saveAndFlush(permiso);

        // Get all the permisoList where nombre equals to DEFAULT_NOMBRE
        defaultPermisoShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the permisoList where nombre equals to UPDATED_NOMBRE
        defaultPermisoShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPermisosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        permisoRepository.saveAndFlush(permiso);

        // Get all the permisoList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultPermisoShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the permisoList where nombre equals to UPDATED_NOMBRE
        defaultPermisoShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPermisosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        permisoRepository.saveAndFlush(permiso);

        // Get all the permisoList where nombre is not null
        defaultPermisoShouldBeFound("nombre.specified=true");

        // Get all the permisoList where nombre is null
        defaultPermisoShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllPermisosByNombreContainsSomething() throws Exception {
        // Initialize the database
        permisoRepository.saveAndFlush(permiso);

        // Get all the permisoList where nombre contains DEFAULT_NOMBRE
        defaultPermisoShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the permisoList where nombre contains UPDATED_NOMBRE
        defaultPermisoShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPermisosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        permisoRepository.saveAndFlush(permiso);

        // Get all the permisoList where nombre does not contain DEFAULT_NOMBRE
        defaultPermisoShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the permisoList where nombre does not contain UPDATED_NOMBRE
        defaultPermisoShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPermisosByRolIsEqualToSomething() throws Exception {
        Rol rol;
        if (TestUtil.findAll(em, Rol.class).isEmpty()) {
            permisoRepository.saveAndFlush(permiso);
            rol = RolResourceIT.createEntity(em);
        } else {
            rol = TestUtil.findAll(em, Rol.class).get(0);
        }
        em.persist(rol);
        em.flush();
        permiso.setRol(rol);
        permisoRepository.saveAndFlush(permiso);
        Long rolId = rol.getId();

        // Get all the permisoList where rol equals to rolId
        defaultPermisoShouldBeFound("rolId.equals=" + rolId);

        // Get all the permisoList where rol equals to (rolId + 1)
        defaultPermisoShouldNotBeFound("rolId.equals=" + (rolId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPermisoShouldBeFound(String filter) throws Exception {
        restPermisoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permiso.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));

        // Check, that the count call also returns 1
        restPermisoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPermisoShouldNotBeFound(String filter) throws Exception {
        restPermisoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPermisoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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

        restPermisoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPermiso.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPermiso))
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

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermisoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permiso.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permiso))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermisoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permiso))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermisoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permiso)))
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

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermisoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, permiso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permiso))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermisoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permiso))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermisoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(permiso)))
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
