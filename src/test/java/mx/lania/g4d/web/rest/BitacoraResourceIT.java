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
import mx.lania.g4d.domain.Proyecto;
import mx.lania.g4d.domain.Usuario;
import mx.lania.g4d.repository.BitacoraRepository;
import mx.lania.g4d.service.criteria.BitacoraCriteria;
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

    private static final String DEFAULT_TABLA = "AAAAAAAAAA";
    private static final String UPDATED_TABLA = "BBBBBBBBBB";

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
        Bitacora bitacora = new Bitacora().tabla(DEFAULT_TABLA).accion(DEFAULT_ACCION).creado(DEFAULT_CREADO);
        return bitacora;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bitacora createUpdatedEntity(EntityManager em) {
        Bitacora bitacora = new Bitacora().tabla(UPDATED_TABLA).accion(UPDATED_ACCION).creado(UPDATED_CREADO);
        return bitacora;
    }

    @BeforeEach
    public void initTest() {
        bitacora = createEntity(em);
    }

    @Test
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
        assertThat(testBitacora.getTabla()).isEqualTo(DEFAULT_TABLA);
        assertThat(testBitacora.getAccion()).isEqualTo(DEFAULT_ACCION);
        assertThat(testBitacora.getCreado()).isEqualTo(DEFAULT_CREADO);
    }

    @Test
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

    @Test
    @Transactional
    void checkTablaIsRequired() throws Exception {
        int databaseSizeBeforeTest = bitacoraRepository.findAll().size();
        // set the field null
        bitacora.setTabla(null);

        // Create the Bitacora, which fails.

        restBitacoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bitacora)))
            .andExpect(status().isBadRequest());

        List<Bitacora> bitacoraList = bitacoraRepository.findAll();
        assertThat(bitacoraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
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

    @Test
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

    @Test
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
            .andExpect(jsonPath("$.[*].tabla").value(hasItem(DEFAULT_TABLA)))
            .andExpect(jsonPath("$.[*].accion").value(hasItem(DEFAULT_ACCION)))
            .andExpect(jsonPath("$.[*].creado").value(hasItem(DEFAULT_CREADO.toString())));
    }

    @Test
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
            .andExpect(jsonPath("$.tabla").value(DEFAULT_TABLA))
            .andExpect(jsonPath("$.accion").value(DEFAULT_ACCION))
            .andExpect(jsonPath("$.creado").value(DEFAULT_CREADO.toString()));
    }

    @Test
    @Transactional
    void getBitacorasByIdFiltering() throws Exception {
        // Initialize the database
        bitacoraRepository.saveAndFlush(bitacora);

        Long id = bitacora.getId();

        defaultBitacoraShouldBeFound("id.equals=" + id);
        defaultBitacoraShouldNotBeFound("id.notEquals=" + id);

        defaultBitacoraShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBitacoraShouldNotBeFound("id.greaterThan=" + id);

        defaultBitacoraShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBitacoraShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBitacorasByTablaIsEqualToSomething() throws Exception {
        // Initialize the database
        bitacoraRepository.saveAndFlush(bitacora);

        // Get all the bitacoraList where tabla equals to DEFAULT_TABLA
        defaultBitacoraShouldBeFound("tabla.equals=" + DEFAULT_TABLA);

        // Get all the bitacoraList where tabla equals to UPDATED_TABLA
        defaultBitacoraShouldNotBeFound("tabla.equals=" + UPDATED_TABLA);
    }

    @Test
    @Transactional
    void getAllBitacorasByTablaIsInShouldWork() throws Exception {
        // Initialize the database
        bitacoraRepository.saveAndFlush(bitacora);

        // Get all the bitacoraList where tabla in DEFAULT_TABLA or UPDATED_TABLA
        defaultBitacoraShouldBeFound("tabla.in=" + DEFAULT_TABLA + "," + UPDATED_TABLA);

        // Get all the bitacoraList where tabla equals to UPDATED_TABLA
        defaultBitacoraShouldNotBeFound("tabla.in=" + UPDATED_TABLA);
    }

    @Test
    @Transactional
    void getAllBitacorasByTablaIsNullOrNotNull() throws Exception {
        // Initialize the database
        bitacoraRepository.saveAndFlush(bitacora);

        // Get all the bitacoraList where tabla is not null
        defaultBitacoraShouldBeFound("tabla.specified=true");

        // Get all the bitacoraList where tabla is null
        defaultBitacoraShouldNotBeFound("tabla.specified=false");
    }

    @Test
    @Transactional
    void getAllBitacorasByTablaContainsSomething() throws Exception {
        // Initialize the database
        bitacoraRepository.saveAndFlush(bitacora);

        // Get all the bitacoraList where tabla contains DEFAULT_TABLA
        defaultBitacoraShouldBeFound("tabla.contains=" + DEFAULT_TABLA);

        // Get all the bitacoraList where tabla contains UPDATED_TABLA
        defaultBitacoraShouldNotBeFound("tabla.contains=" + UPDATED_TABLA);
    }

    @Test
    @Transactional
    void getAllBitacorasByTablaNotContainsSomething() throws Exception {
        // Initialize the database
        bitacoraRepository.saveAndFlush(bitacora);

        // Get all the bitacoraList where tabla does not contain DEFAULT_TABLA
        defaultBitacoraShouldNotBeFound("tabla.doesNotContain=" + DEFAULT_TABLA);

        // Get all the bitacoraList where tabla does not contain UPDATED_TABLA
        defaultBitacoraShouldBeFound("tabla.doesNotContain=" + UPDATED_TABLA);
    }

    @Test
    @Transactional
    void getAllBitacorasByAccionIsEqualToSomething() throws Exception {
        // Initialize the database
        bitacoraRepository.saveAndFlush(bitacora);

        // Get all the bitacoraList where accion equals to DEFAULT_ACCION
        defaultBitacoraShouldBeFound("accion.equals=" + DEFAULT_ACCION);

        // Get all the bitacoraList where accion equals to UPDATED_ACCION
        defaultBitacoraShouldNotBeFound("accion.equals=" + UPDATED_ACCION);
    }

    @Test
    @Transactional
    void getAllBitacorasByAccionIsInShouldWork() throws Exception {
        // Initialize the database
        bitacoraRepository.saveAndFlush(bitacora);

        // Get all the bitacoraList where accion in DEFAULT_ACCION or UPDATED_ACCION
        defaultBitacoraShouldBeFound("accion.in=" + DEFAULT_ACCION + "," + UPDATED_ACCION);

        // Get all the bitacoraList where accion equals to UPDATED_ACCION
        defaultBitacoraShouldNotBeFound("accion.in=" + UPDATED_ACCION);
    }

    @Test
    @Transactional
    void getAllBitacorasByAccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        bitacoraRepository.saveAndFlush(bitacora);

        // Get all the bitacoraList where accion is not null
        defaultBitacoraShouldBeFound("accion.specified=true");

        // Get all the bitacoraList where accion is null
        defaultBitacoraShouldNotBeFound("accion.specified=false");
    }

    @Test
    @Transactional
    void getAllBitacorasByAccionContainsSomething() throws Exception {
        // Initialize the database
        bitacoraRepository.saveAndFlush(bitacora);

        // Get all the bitacoraList where accion contains DEFAULT_ACCION
        defaultBitacoraShouldBeFound("accion.contains=" + DEFAULT_ACCION);

        // Get all the bitacoraList where accion contains UPDATED_ACCION
        defaultBitacoraShouldNotBeFound("accion.contains=" + UPDATED_ACCION);
    }

    @Test
    @Transactional
    void getAllBitacorasByAccionNotContainsSomething() throws Exception {
        // Initialize the database
        bitacoraRepository.saveAndFlush(bitacora);

        // Get all the bitacoraList where accion does not contain DEFAULT_ACCION
        defaultBitacoraShouldNotBeFound("accion.doesNotContain=" + DEFAULT_ACCION);

        // Get all the bitacoraList where accion does not contain UPDATED_ACCION
        defaultBitacoraShouldBeFound("accion.doesNotContain=" + UPDATED_ACCION);
    }

    @Test
    @Transactional
    void getAllBitacorasByCreadoIsEqualToSomething() throws Exception {
        // Initialize the database
        bitacoraRepository.saveAndFlush(bitacora);

        // Get all the bitacoraList where creado equals to DEFAULT_CREADO
        defaultBitacoraShouldBeFound("creado.equals=" + DEFAULT_CREADO);

        // Get all the bitacoraList where creado equals to UPDATED_CREADO
        defaultBitacoraShouldNotBeFound("creado.equals=" + UPDATED_CREADO);
    }

    @Test
    @Transactional
    void getAllBitacorasByCreadoIsInShouldWork() throws Exception {
        // Initialize the database
        bitacoraRepository.saveAndFlush(bitacora);

        // Get all the bitacoraList where creado in DEFAULT_CREADO or UPDATED_CREADO
        defaultBitacoraShouldBeFound("creado.in=" + DEFAULT_CREADO + "," + UPDATED_CREADO);

        // Get all the bitacoraList where creado equals to UPDATED_CREADO
        defaultBitacoraShouldNotBeFound("creado.in=" + UPDATED_CREADO);
    }

    @Test
    @Transactional
    void getAllBitacorasByCreadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        bitacoraRepository.saveAndFlush(bitacora);

        // Get all the bitacoraList where creado is not null
        defaultBitacoraShouldBeFound("creado.specified=true");

        // Get all the bitacoraList where creado is null
        defaultBitacoraShouldNotBeFound("creado.specified=false");
    }

    @Test
    @Transactional
    void getAllBitacorasByUsuarioIsEqualToSomething() throws Exception {
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            bitacoraRepository.saveAndFlush(bitacora);
            usuario = UsuarioResourceIT.createEntity(em);
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        bitacora.setUsuario(usuario);
        bitacoraRepository.saveAndFlush(bitacora);
        Long usuarioId = usuario.getId();

        // Get all the bitacoraList where usuario equals to usuarioId
        defaultBitacoraShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the bitacoraList where usuario equals to (usuarioId + 1)
        defaultBitacoraShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    @Test
    @Transactional
    void getAllBitacorasByProyectoIsEqualToSomething() throws Exception {
        Proyecto proyecto;
        if (TestUtil.findAll(em, Proyecto.class).isEmpty()) {
            bitacoraRepository.saveAndFlush(bitacora);
            proyecto = ProyectoResourceIT.createEntity(em);
        } else {
            proyecto = TestUtil.findAll(em, Proyecto.class).get(0);
        }
        em.persist(proyecto);
        em.flush();
        bitacora.setProyecto(proyecto);
        bitacoraRepository.saveAndFlush(bitacora);
        Long proyectoId = proyecto.getId();

        // Get all the bitacoraList where proyecto equals to proyectoId
        defaultBitacoraShouldBeFound("proyectoId.equals=" + proyectoId);

        // Get all the bitacoraList where proyecto equals to (proyectoId + 1)
        defaultBitacoraShouldNotBeFound("proyectoId.equals=" + (proyectoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBitacoraShouldBeFound(String filter) throws Exception {
        restBitacoraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bitacora.getId().intValue())))
            .andExpect(jsonPath("$.[*].tabla").value(hasItem(DEFAULT_TABLA)))
            .andExpect(jsonPath("$.[*].accion").value(hasItem(DEFAULT_ACCION)))
            .andExpect(jsonPath("$.[*].creado").value(hasItem(DEFAULT_CREADO.toString())));

        // Check, that the count call also returns 1
        restBitacoraMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBitacoraShouldNotBeFound(String filter) throws Exception {
        restBitacoraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBitacoraMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBitacora() throws Exception {
        // Get the bitacora
        restBitacoraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBitacora() throws Exception {
        // Initialize the database
        bitacoraRepository.saveAndFlush(bitacora);

        int databaseSizeBeforeUpdate = bitacoraRepository.findAll().size();

        // Update the bitacora
        Bitacora updatedBitacora = bitacoraRepository.findById(bitacora.getId()).get();
        // Disconnect from session so that the updates on updatedBitacora are not directly saved in db
        em.detach(updatedBitacora);
        updatedBitacora.tabla(UPDATED_TABLA).accion(UPDATED_ACCION).creado(UPDATED_CREADO);

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
        assertThat(testBitacora.getTabla()).isEqualTo(UPDATED_TABLA);
        assertThat(testBitacora.getAccion()).isEqualTo(UPDATED_ACCION);
        assertThat(testBitacora.getCreado()).isEqualTo(UPDATED_CREADO);
    }

    @Test
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

    @Test
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

    @Test
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

    @Test
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
        assertThat(testBitacora.getTabla()).isEqualTo(DEFAULT_TABLA);
        assertThat(testBitacora.getAccion()).isEqualTo(DEFAULT_ACCION);
        assertThat(testBitacora.getCreado()).isEqualTo(DEFAULT_CREADO);
    }

    @Test
    @Transactional
    void fullUpdateBitacoraWithPatch() throws Exception {
        // Initialize the database
        bitacoraRepository.saveAndFlush(bitacora);

        int databaseSizeBeforeUpdate = bitacoraRepository.findAll().size();

        // Update the bitacora using partial update
        Bitacora partialUpdatedBitacora = new Bitacora();
        partialUpdatedBitacora.setId(bitacora.getId());

        partialUpdatedBitacora.tabla(UPDATED_TABLA).accion(UPDATED_ACCION).creado(UPDATED_CREADO);

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
        assertThat(testBitacora.getTabla()).isEqualTo(UPDATED_TABLA);
        assertThat(testBitacora.getAccion()).isEqualTo(UPDATED_ACCION);
        assertThat(testBitacora.getCreado()).isEqualTo(UPDATED_CREADO);
    }

    @Test
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

    @Test
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

    @Test
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

    @Test
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
