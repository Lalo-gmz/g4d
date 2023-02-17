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
import mx.lania.g4d.domain.Bitacora;
import mx.lania.g4d.domain.Configuracion;
import mx.lania.g4d.domain.Iteracion;
import mx.lania.g4d.domain.ParticipacionProyecto;
import mx.lania.g4d.domain.Proyecto;
import mx.lania.g4d.repository.ProyectoRepository;
import mx.lania.g4d.service.criteria.ProyectoCriteria;
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
        restProyectoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proyecto)))
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

        int databaseSizeBeforeCreate = proyectoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProyectoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proyecto)))
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

        restProyectoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proyecto)))
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
    void getProyectosByIdFiltering() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        Long id = proyecto.getId();

        defaultProyectoShouldBeFound("id.equals=" + id);
        defaultProyectoShouldNotBeFound("id.notEquals=" + id);

        defaultProyectoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProyectoShouldNotBeFound("id.greaterThan=" + id);

        defaultProyectoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProyectoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProyectosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        // Get all the proyectoList where nombre equals to DEFAULT_NOMBRE
        defaultProyectoShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the proyectoList where nombre equals to UPDATED_NOMBRE
        defaultProyectoShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProyectosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        // Get all the proyectoList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultProyectoShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the proyectoList where nombre equals to UPDATED_NOMBRE
        defaultProyectoShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProyectosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        // Get all the proyectoList where nombre is not null
        defaultProyectoShouldBeFound("nombre.specified=true");

        // Get all the proyectoList where nombre is null
        defaultProyectoShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllProyectosByNombreContainsSomething() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        // Get all the proyectoList where nombre contains DEFAULT_NOMBRE
        defaultProyectoShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the proyectoList where nombre contains UPDATED_NOMBRE
        defaultProyectoShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProyectosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        // Get all the proyectoList where nombre does not contain DEFAULT_NOMBRE
        defaultProyectoShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the proyectoList where nombre does not contain UPDATED_NOMBRE
        defaultProyectoShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProyectosByIdProyectoGitLabIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        // Get all the proyectoList where idProyectoGitLab equals to DEFAULT_ID_PROYECTO_GIT_LAB
        defaultProyectoShouldBeFound("idProyectoGitLab.equals=" + DEFAULT_ID_PROYECTO_GIT_LAB);

        // Get all the proyectoList where idProyectoGitLab equals to UPDATED_ID_PROYECTO_GIT_LAB
        defaultProyectoShouldNotBeFound("idProyectoGitLab.equals=" + UPDATED_ID_PROYECTO_GIT_LAB);
    }

    @Test
    @Transactional
    void getAllProyectosByIdProyectoGitLabIsInShouldWork() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        // Get all the proyectoList where idProyectoGitLab in DEFAULT_ID_PROYECTO_GIT_LAB or UPDATED_ID_PROYECTO_GIT_LAB
        defaultProyectoShouldBeFound("idProyectoGitLab.in=" + DEFAULT_ID_PROYECTO_GIT_LAB + "," + UPDATED_ID_PROYECTO_GIT_LAB);

        // Get all the proyectoList where idProyectoGitLab equals to UPDATED_ID_PROYECTO_GIT_LAB
        defaultProyectoShouldNotBeFound("idProyectoGitLab.in=" + UPDATED_ID_PROYECTO_GIT_LAB);
    }

    @Test
    @Transactional
    void getAllProyectosByIdProyectoGitLabIsNullOrNotNull() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        // Get all the proyectoList where idProyectoGitLab is not null
        defaultProyectoShouldBeFound("idProyectoGitLab.specified=true");

        // Get all the proyectoList where idProyectoGitLab is null
        defaultProyectoShouldNotBeFound("idProyectoGitLab.specified=false");
    }

    @Test
    @Transactional
    void getAllProyectosByIdProyectoGitLabContainsSomething() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        // Get all the proyectoList where idProyectoGitLab contains DEFAULT_ID_PROYECTO_GIT_LAB
        defaultProyectoShouldBeFound("idProyectoGitLab.contains=" + DEFAULT_ID_PROYECTO_GIT_LAB);

        // Get all the proyectoList where idProyectoGitLab contains UPDATED_ID_PROYECTO_GIT_LAB
        defaultProyectoShouldNotBeFound("idProyectoGitLab.contains=" + UPDATED_ID_PROYECTO_GIT_LAB);
    }

    @Test
    @Transactional
    void getAllProyectosByIdProyectoGitLabNotContainsSomething() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        // Get all the proyectoList where idProyectoGitLab does not contain DEFAULT_ID_PROYECTO_GIT_LAB
        defaultProyectoShouldNotBeFound("idProyectoGitLab.doesNotContain=" + DEFAULT_ID_PROYECTO_GIT_LAB);

        // Get all the proyectoList where idProyectoGitLab does not contain UPDATED_ID_PROYECTO_GIT_LAB
        defaultProyectoShouldBeFound("idProyectoGitLab.doesNotContain=" + UPDATED_ID_PROYECTO_GIT_LAB);
    }

    @Test
    @Transactional
    void getAllProyectosByParticipacionProyectoIsEqualToSomething() throws Exception {
        ParticipacionProyecto participacionProyecto;
        if (TestUtil.findAll(em, ParticipacionProyecto.class).isEmpty()) {
            proyectoRepository.saveAndFlush(proyecto);
            participacionProyecto = ParticipacionProyectoResourceIT.createEntity(em);
        } else {
            participacionProyecto = TestUtil.findAll(em, ParticipacionProyecto.class).get(0);
        }
        em.persist(participacionProyecto);
        em.flush();
        proyecto.addParticipacionProyecto(participacionProyecto);
        proyectoRepository.saveAndFlush(proyecto);
        Long participacionProyectoId = participacionProyecto.getId();

        // Get all the proyectoList where participacionProyecto equals to participacionProyectoId
        defaultProyectoShouldBeFound("participacionProyectoId.equals=" + participacionProyectoId);

        // Get all the proyectoList where participacionProyecto equals to (participacionProyectoId + 1)
        defaultProyectoShouldNotBeFound("participacionProyectoId.equals=" + (participacionProyectoId + 1));
    }

    @Test
    @Transactional
    void getAllProyectosByConfiguracionIsEqualToSomething() throws Exception {
        Configuracion configuracion;
        if (TestUtil.findAll(em, Configuracion.class).isEmpty()) {
            proyectoRepository.saveAndFlush(proyecto);
            configuracion = ConfiguracionResourceIT.createEntity(em);
        } else {
            configuracion = TestUtil.findAll(em, Configuracion.class).get(0);
        }
        em.persist(configuracion);
        em.flush();
        proyecto.addConfiguracion(configuracion);
        proyectoRepository.saveAndFlush(proyecto);
        Long configuracionId = configuracion.getId();

        // Get all the proyectoList where configuracion equals to configuracionId
        defaultProyectoShouldBeFound("configuracionId.equals=" + configuracionId);

        // Get all the proyectoList where configuracion equals to (configuracionId + 1)
        defaultProyectoShouldNotBeFound("configuracionId.equals=" + (configuracionId + 1));
    }

    @Test
    @Transactional
    void getAllProyectosByBitacoraIsEqualToSomething() throws Exception {
        Bitacora bitacora;
        if (TestUtil.findAll(em, Bitacora.class).isEmpty()) {
            proyectoRepository.saveAndFlush(proyecto);
            bitacora = BitacoraResourceIT.createEntity(em);
        } else {
            bitacora = TestUtil.findAll(em, Bitacora.class).get(0);
        }
        em.persist(bitacora);
        em.flush();
        proyecto.addBitacora(bitacora);
        proyectoRepository.saveAndFlush(proyecto);
        Long bitacoraId = bitacora.getId();

        // Get all the proyectoList where bitacora equals to bitacoraId
        defaultProyectoShouldBeFound("bitacoraId.equals=" + bitacoraId);

        // Get all the proyectoList where bitacora equals to (bitacoraId + 1)
        defaultProyectoShouldNotBeFound("bitacoraId.equals=" + (bitacoraId + 1));
    }

    @Test
    @Transactional
    void getAllProyectosByIteracionIsEqualToSomething() throws Exception {
        Iteracion iteracion;
        if (TestUtil.findAll(em, Iteracion.class).isEmpty()) {
            proyectoRepository.saveAndFlush(proyecto);
            iteracion = IteracionResourceIT.createEntity(em);
        } else {
            iteracion = TestUtil.findAll(em, Iteracion.class).get(0);
        }
        em.persist(iteracion);
        em.flush();
        proyecto.addIteracion(iteracion);
        proyectoRepository.saveAndFlush(proyecto);
        Long iteracionId = iteracion.getId();

        // Get all the proyectoList where iteracion equals to iteracionId
        defaultProyectoShouldBeFound("iteracionId.equals=" + iteracionId);

        // Get all the proyectoList where iteracion equals to (iteracionId + 1)
        defaultProyectoShouldNotBeFound("iteracionId.equals=" + (iteracionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProyectoShouldBeFound(String filter) throws Exception {
        restProyectoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proyecto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].idProyectoGitLab").value(hasItem(DEFAULT_ID_PROYECTO_GIT_LAB)));

        // Check, that the count call also returns 1
        restProyectoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProyectoShouldNotBeFound(String filter) throws Exception {
        restProyectoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProyectoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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

        restProyectoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProyecto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProyecto))
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

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proyecto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proyecto))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proyecto))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proyecto)))
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

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, proyecto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proyecto))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proyecto))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(proyecto)))
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
