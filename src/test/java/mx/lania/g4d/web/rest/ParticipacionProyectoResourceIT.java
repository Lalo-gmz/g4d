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
import mx.lania.g4d.domain.Proyecto;
import mx.lania.g4d.domain.Rol;
import mx.lania.g4d.domain.User;
import mx.lania.g4d.repository.ParticipacionProyectoRepository;
import mx.lania.g4d.service.criteria.ParticipacionProyectoCriteria;
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
        ParticipacionProyecto participacionProyecto = new ParticipacionProyecto();
        return participacionProyecto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParticipacionProyecto createUpdatedEntity(EntityManager em) {
        ParticipacionProyecto participacionProyecto = new ParticipacionProyecto();
        return participacionProyecto;
    }

    @BeforeEach
    public void initTest() {
        participacionProyecto = createEntity(em);
    }

    @Test
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
    }

    @Test
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

    @Test
    @Transactional
    void getAllParticipacionProyectos() throws Exception {
        // Initialize the database
        participacionProyectoRepository.saveAndFlush(participacionProyecto);

        // Get all the participacionProyectoList
        restParticipacionProyectoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(participacionProyecto.getId().intValue())));
    }

    @Test
    @Transactional
    void getParticipacionProyecto() throws Exception {
        // Initialize the database
        participacionProyectoRepository.saveAndFlush(participacionProyecto);

        // Get the participacionProyecto
        restParticipacionProyectoMockMvc
            .perform(get(ENTITY_API_URL_ID, participacionProyecto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(participacionProyecto.getId().intValue()));
    }

    @Test
    @Transactional
    void getParticipacionProyectosByIdFiltering() throws Exception {
        // Initialize the database
        participacionProyectoRepository.saveAndFlush(participacionProyecto);

        Long id = participacionProyecto.getId();

        defaultParticipacionProyectoShouldBeFound("id.equals=" + id);
        defaultParticipacionProyectoShouldNotBeFound("id.notEquals=" + id);

        defaultParticipacionProyectoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultParticipacionProyectoShouldNotBeFound("id.greaterThan=" + id);

        defaultParticipacionProyectoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultParticipacionProyectoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllParticipacionProyectosByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            participacionProyectoRepository.saveAndFlush(participacionProyecto);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        participacionProyecto.setUser(user);
        participacionProyectoRepository.saveAndFlush(participacionProyecto);
        Long userId = user.getId();

        // Get all the participacionProyectoList where user equals to userId
        defaultParticipacionProyectoShouldBeFound("userId.equals=" + userId);

        // Get all the participacionProyectoList where user equals to (userId + 1)
        defaultParticipacionProyectoShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllParticipacionProyectosByProyectoIsEqualToSomething() throws Exception {
        Proyecto proyecto;
        if (TestUtil.findAll(em, Proyecto.class).isEmpty()) {
            participacionProyectoRepository.saveAndFlush(participacionProyecto);
            proyecto = ProyectoResourceIT.createEntity(em);
        } else {
            proyecto = TestUtil.findAll(em, Proyecto.class).get(0);
        }
        em.persist(proyecto);
        em.flush();
        participacionProyecto.setProyecto(proyecto);
        participacionProyectoRepository.saveAndFlush(participacionProyecto);
        Long proyectoId = proyecto.getId();

        // Get all the participacionProyectoList where proyecto equals to proyectoId
        defaultParticipacionProyectoShouldBeFound("proyectoId.equals=" + proyectoId);

        // Get all the participacionProyectoList where proyecto equals to (proyectoId + 1)
        defaultParticipacionProyectoShouldNotBeFound("proyectoId.equals=" + (proyectoId + 1));
    }

    @Test
    @Transactional
    void getAllParticipacionProyectosByRolIsEqualToSomething() throws Exception {
        Rol rol;
        if (TestUtil.findAll(em, Rol.class).isEmpty()) {
            participacionProyectoRepository.saveAndFlush(participacionProyecto);
            rol = RolResourceIT.createEntity(em);
        } else {
            rol = TestUtil.findAll(em, Rol.class).get(0);
        }
        em.persist(rol);
        em.flush();
        participacionProyecto.setRol(rol);
        participacionProyectoRepository.saveAndFlush(participacionProyecto);
        Long rolId = rol.getId();

        // Get all the participacionProyectoList where rol equals to rolId
        defaultParticipacionProyectoShouldBeFound("rolId.equals=" + rolId);

        // Get all the participacionProyectoList where rol equals to (rolId + 1)
        defaultParticipacionProyectoShouldNotBeFound("rolId.equals=" + (rolId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultParticipacionProyectoShouldBeFound(String filter) throws Exception {
        restParticipacionProyectoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(participacionProyecto.getId().intValue())));

        // Check, that the count call also returns 1
        restParticipacionProyectoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultParticipacionProyectoShouldNotBeFound(String filter) throws Exception {
        restParticipacionProyectoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restParticipacionProyectoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingParticipacionProyecto() throws Exception {
        // Get the participacionProyecto
        restParticipacionProyectoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingParticipacionProyecto() throws Exception {
        // Initialize the database
        participacionProyectoRepository.saveAndFlush(participacionProyecto);

        int databaseSizeBeforeUpdate = participacionProyectoRepository.findAll().size();

        // Update the participacionProyecto
        ParticipacionProyecto updatedParticipacionProyecto = participacionProyectoRepository.findById(participacionProyecto.getId()).get();
        // Disconnect from session so that the updates on updatedParticipacionProyecto are not directly saved in db
        em.detach(updatedParticipacionProyecto);

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
    }

    @Test
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

    @Test
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

    @Test
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

    @Test
    @Transactional
    void partialUpdateParticipacionProyectoWithPatch() throws Exception {
        // Initialize the database
        participacionProyectoRepository.saveAndFlush(participacionProyecto);

        int databaseSizeBeforeUpdate = participacionProyectoRepository.findAll().size();

        // Update the participacionProyecto using partial update
        ParticipacionProyecto partialUpdatedParticipacionProyecto = new ParticipacionProyecto();
        partialUpdatedParticipacionProyecto.setId(participacionProyecto.getId());

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
    }

    @Test
    @Transactional
    void fullUpdateParticipacionProyectoWithPatch() throws Exception {
        // Initialize the database
        participacionProyectoRepository.saveAndFlush(participacionProyecto);

        int databaseSizeBeforeUpdate = participacionProyectoRepository.findAll().size();

        // Update the participacionProyecto using partial update
        ParticipacionProyecto partialUpdatedParticipacionProyecto = new ParticipacionProyecto();
        partialUpdatedParticipacionProyecto.setId(participacionProyecto.getId());

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
    }

    @Test
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

    @Test
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

    @Test
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

    @Test
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
