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
import mx.lania.mca.g4d.domain.Proyecto;
import mx.lania.mca.g4d.repository.ProyectoRepository;
import mx.lania.mca.g4d.service.dto.ProyectoDTO;
import mx.lania.mca.g4d.service.mapper.ProyectoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProyectoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProyectoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_ID_PROYECTO_GIT_LAB = "AAAAAAAAAA";
    private static final String UPDATED_ID_PROYECTO_GIT_LAB = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/proyectos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private ProyectoMapper proyectoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProyectoMockMvc;

    private Proyecto proyecto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proyecto createEntity(EntityManager em) {
        Proyecto proyecto = new Proyecto().nombre(DEFAULT_NOMBRE).idProyectoGitLab(DEFAULT_ID_PROYECTO_GIT_LAB);
        return proyecto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proyecto createUpdatedEntity(EntityManager em) {
        Proyecto proyecto = new Proyecto().nombre(UPDATED_NOMBRE).idProyectoGitLab(UPDATED_ID_PROYECTO_GIT_LAB);
        return proyecto;
    }

    @BeforeEach
    public void initTest() {
        proyecto = createEntity(em);
    }

    @Test
    @Transactional
    void createProyecto() throws Exception {
        int databaseSizeBeforeCreate = proyectoRepository.findAll().size();
        // Create the Proyecto
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);
        restProyectoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proyectoDTO)))
            .andExpect(status().isCreated());

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll();
        assertThat(proyectoList).hasSize(databaseSizeBeforeCreate + 1);
        Proyecto testProyecto = proyectoList.get(proyectoList.size() - 1);
        assertThat(testProyecto.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testProyecto.getIdProyectoGitLab()).isEqualTo(DEFAULT_ID_PROYECTO_GIT_LAB);
    }

    @Test
    @Transactional
    void createProyectoWithExistingId() throws Exception {
        // Create the Proyecto with an existing ID
        proyecto.setId(1L);
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        int databaseSizeBeforeCreate = proyectoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProyectoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proyectoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll();
        assertThat(proyectoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdProyectoGitLabIsRequired() throws Exception {
        int databaseSizeBeforeTest = proyectoRepository.findAll().size();
        // set the field null
        proyecto.setIdProyectoGitLab(null);

        // Create the Proyecto, which fails.
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        restProyectoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proyectoDTO)))
            .andExpect(status().isBadRequest());

        List<Proyecto> proyectoList = proyectoRepository.findAll();
        assertThat(proyectoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProyectos() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        // Get all the proyectoList
        restProyectoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proyecto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].idProyectoGitLab").value(hasItem(DEFAULT_ID_PROYECTO_GIT_LAB)));
    }

    @Test
    @Transactional
    void getProyecto() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        // Get the proyecto
        restProyectoMockMvc
            .perform(get(ENTITY_API_URL_ID, proyecto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proyecto.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.idProyectoGitLab").value(DEFAULT_ID_PROYECTO_GIT_LAB));
    }

    @Test
    @Transactional
    void getNonExistingProyecto() throws Exception {
        // Get the proyecto
        restProyectoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProyecto() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        int databaseSizeBeforeUpdate = proyectoRepository.findAll().size();

        // Update the proyecto
        Proyecto updatedProyecto = proyectoRepository.findById(proyecto.getId()).get();
        // Disconnect from session so that the updates on updatedProyecto are not directly saved in db
        em.detach(updatedProyecto);
        updatedProyecto.nombre(UPDATED_NOMBRE).idProyectoGitLab(UPDATED_ID_PROYECTO_GIT_LAB);
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(updatedProyecto);

        restProyectoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proyectoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proyectoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll();
        assertThat(proyectoList).hasSize(databaseSizeBeforeUpdate);
        Proyecto testProyecto = proyectoList.get(proyectoList.size() - 1);
        assertThat(testProyecto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testProyecto.getIdProyectoGitLab()).isEqualTo(UPDATED_ID_PROYECTO_GIT_LAB);
    }

    @Test
    @Transactional
    void putNonExistingProyecto() throws Exception {
        int databaseSizeBeforeUpdate = proyectoRepository.findAll().size();
        proyecto.setId(count.incrementAndGet());

        // Create the Proyecto
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proyectoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proyectoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll();
        assertThat(proyectoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProyecto() throws Exception {
        int databaseSizeBeforeUpdate = proyectoRepository.findAll().size();
        proyecto.setId(count.incrementAndGet());

        // Create the Proyecto
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proyectoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll();
        assertThat(proyectoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProyecto() throws Exception {
        int databaseSizeBeforeUpdate = proyectoRepository.findAll().size();
        proyecto.setId(count.incrementAndGet());

        // Create the Proyecto
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proyectoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll();
        assertThat(proyectoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProyectoWithPatch() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        int databaseSizeBeforeUpdate = proyectoRepository.findAll().size();

        // Update the proyecto using partial update
        Proyecto partialUpdatedProyecto = new Proyecto();
        partialUpdatedProyecto.setId(proyecto.getId());

        partialUpdatedProyecto.idProyectoGitLab(UPDATED_ID_PROYECTO_GIT_LAB);

        restProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProyecto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProyecto))
            )
            .andExpect(status().isOk());

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll();
        assertThat(proyectoList).hasSize(databaseSizeBeforeUpdate);
        Proyecto testProyecto = proyectoList.get(proyectoList.size() - 1);
        assertThat(testProyecto.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testProyecto.getIdProyectoGitLab()).isEqualTo(UPDATED_ID_PROYECTO_GIT_LAB);
    }

    @Test
    @Transactional
    void fullUpdateProyectoWithPatch() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        int databaseSizeBeforeUpdate = proyectoRepository.findAll().size();

        // Update the proyecto using partial update
        Proyecto partialUpdatedProyecto = new Proyecto();
        partialUpdatedProyecto.setId(proyecto.getId());

        partialUpdatedProyecto.nombre(UPDATED_NOMBRE).idProyectoGitLab(UPDATED_ID_PROYECTO_GIT_LAB);

        restProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProyecto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProyecto))
            )
            .andExpect(status().isOk());

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll();
        assertThat(proyectoList).hasSize(databaseSizeBeforeUpdate);
        Proyecto testProyecto = proyectoList.get(proyectoList.size() - 1);
        assertThat(testProyecto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testProyecto.getIdProyectoGitLab()).isEqualTo(UPDATED_ID_PROYECTO_GIT_LAB);
    }

    @Test
    @Transactional
    void patchNonExistingProyecto() throws Exception {
        int databaseSizeBeforeUpdate = proyectoRepository.findAll().size();
        proyecto.setId(count.incrementAndGet());

        // Create the Proyecto
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, proyectoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proyectoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll();
        assertThat(proyectoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProyecto() throws Exception {
        int databaseSizeBeforeUpdate = proyectoRepository.findAll().size();
        proyecto.setId(count.incrementAndGet());

        // Create the Proyecto
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proyectoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll();
        assertThat(proyectoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProyecto() throws Exception {
        int databaseSizeBeforeUpdate = proyectoRepository.findAll().size();
        proyecto.setId(count.incrementAndGet());

        // Create the Proyecto
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(proyectoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll();
        assertThat(proyectoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProyecto() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        int databaseSizeBeforeDelete = proyectoRepository.findAll().size();

        // Delete the proyecto
        restProyectoMockMvc
            .perform(delete(ENTITY_API_URL_ID, proyecto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Proyecto> proyectoList = proyectoRepository.findAll();
        assertThat(proyectoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
