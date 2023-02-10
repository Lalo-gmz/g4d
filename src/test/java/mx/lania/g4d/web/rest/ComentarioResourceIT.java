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
import mx.lania.g4d.domain.Comentario;
import mx.lania.g4d.domain.Funcionalidad;
import mx.lania.g4d.domain.Usuario;
import mx.lania.g4d.repository.ComentarioRepository;
import mx.lania.g4d.service.criteria.ComentarioCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ComentarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComentarioResourceIT {

    private static final String DEFAULT_MENSAJE = "AAAAAAAAAA";
    private static final String UPDATED_MENSAJE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREADO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREADO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFICADO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFICADO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/comentarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComentarioMockMvc;

    private Comentario comentario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comentario createEntity(EntityManager em) {
        Comentario comentario = new Comentario().mensaje(DEFAULT_MENSAJE).creado(DEFAULT_CREADO).modificado(DEFAULT_MODIFICADO);
        return comentario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comentario createUpdatedEntity(EntityManager em) {
        Comentario comentario = new Comentario().mensaje(UPDATED_MENSAJE).creado(UPDATED_CREADO).modificado(UPDATED_MODIFICADO);
        return comentario;
    }

    @BeforeEach
    public void initTest() {
        comentario = createEntity(em);
    }

    @Test
    @Transactional
    void createComentario() throws Exception {
        int databaseSizeBeforeCreate = comentarioRepository.findAll().size();
        // Create the Comentario
        restComentarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comentario)))
            .andExpect(status().isCreated());

        // Validate the Comentario in the database
        List<Comentario> comentarioList = comentarioRepository.findAll();
        assertThat(comentarioList).hasSize(databaseSizeBeforeCreate + 1);
        Comentario testComentario = comentarioList.get(comentarioList.size() - 1);
        assertThat(testComentario.getMensaje()).isEqualTo(DEFAULT_MENSAJE);
        assertThat(testComentario.getCreado()).isEqualTo(DEFAULT_CREADO);
        assertThat(testComentario.getModificado()).isEqualTo(DEFAULT_MODIFICADO);
    }

    @Test
    @Transactional
    void createComentarioWithExistingId() throws Exception {
        // Create the Comentario with an existing ID
        comentario.setId(1L);

        int databaseSizeBeforeCreate = comentarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComentarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comentario)))
            .andExpect(status().isBadRequest());

        // Validate the Comentario in the database
        List<Comentario> comentarioList = comentarioRepository.findAll();
        assertThat(comentarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMensajeIsRequired() throws Exception {
        int databaseSizeBeforeTest = comentarioRepository.findAll().size();
        // set the field null
        comentario.setMensaje(null);

        // Create the Comentario, which fails.

        restComentarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comentario)))
            .andExpect(status().isBadRequest());

        List<Comentario> comentarioList = comentarioRepository.findAll();
        assertThat(comentarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllComentarios() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        // Get all the comentarioList
        restComentarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comentario.getId().intValue())))
            .andExpect(jsonPath("$.[*].mensaje").value(hasItem(DEFAULT_MENSAJE)))
            .andExpect(jsonPath("$.[*].creado").value(hasItem(DEFAULT_CREADO.toString())))
            .andExpect(jsonPath("$.[*].modificado").value(hasItem(DEFAULT_MODIFICADO.toString())));
    }

    @Test
    @Transactional
    void getComentario() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        // Get the comentario
        restComentarioMockMvc
            .perform(get(ENTITY_API_URL_ID, comentario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(comentario.getId().intValue()))
            .andExpect(jsonPath("$.mensaje").value(DEFAULT_MENSAJE))
            .andExpect(jsonPath("$.creado").value(DEFAULT_CREADO.toString()))
            .andExpect(jsonPath("$.modificado").value(DEFAULT_MODIFICADO.toString()));
    }

    @Test
    @Transactional
    void getComentariosByIdFiltering() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        Long id = comentario.getId();

        defaultComentarioShouldBeFound("id.equals=" + id);
        defaultComentarioShouldNotBeFound("id.notEquals=" + id);

        defaultComentarioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultComentarioShouldNotBeFound("id.greaterThan=" + id);

        defaultComentarioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultComentarioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllComentariosByMensajeIsEqualToSomething() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        // Get all the comentarioList where mensaje equals to DEFAULT_MENSAJE
        defaultComentarioShouldBeFound("mensaje.equals=" + DEFAULT_MENSAJE);

        // Get all the comentarioList where mensaje equals to UPDATED_MENSAJE
        defaultComentarioShouldNotBeFound("mensaje.equals=" + UPDATED_MENSAJE);
    }

    @Test
    @Transactional
    void getAllComentariosByMensajeIsInShouldWork() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        // Get all the comentarioList where mensaje in DEFAULT_MENSAJE or UPDATED_MENSAJE
        defaultComentarioShouldBeFound("mensaje.in=" + DEFAULT_MENSAJE + "," + UPDATED_MENSAJE);

        // Get all the comentarioList where mensaje equals to UPDATED_MENSAJE
        defaultComentarioShouldNotBeFound("mensaje.in=" + UPDATED_MENSAJE);
    }

    @Test
    @Transactional
    void getAllComentariosByMensajeIsNullOrNotNull() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        // Get all the comentarioList where mensaje is not null
        defaultComentarioShouldBeFound("mensaje.specified=true");

        // Get all the comentarioList where mensaje is null
        defaultComentarioShouldNotBeFound("mensaje.specified=false");
    }

    @Test
    @Transactional
    void getAllComentariosByMensajeContainsSomething() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        // Get all the comentarioList where mensaje contains DEFAULT_MENSAJE
        defaultComentarioShouldBeFound("mensaje.contains=" + DEFAULT_MENSAJE);

        // Get all the comentarioList where mensaje contains UPDATED_MENSAJE
        defaultComentarioShouldNotBeFound("mensaje.contains=" + UPDATED_MENSAJE);
    }

    @Test
    @Transactional
    void getAllComentariosByMensajeNotContainsSomething() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        // Get all the comentarioList where mensaje does not contain DEFAULT_MENSAJE
        defaultComentarioShouldNotBeFound("mensaje.doesNotContain=" + DEFAULT_MENSAJE);

        // Get all the comentarioList where mensaje does not contain UPDATED_MENSAJE
        defaultComentarioShouldBeFound("mensaje.doesNotContain=" + UPDATED_MENSAJE);
    }

    @Test
    @Transactional
    void getAllComentariosByCreadoIsEqualToSomething() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        // Get all the comentarioList where creado equals to DEFAULT_CREADO
        defaultComentarioShouldBeFound("creado.equals=" + DEFAULT_CREADO);

        // Get all the comentarioList where creado equals to UPDATED_CREADO
        defaultComentarioShouldNotBeFound("creado.equals=" + UPDATED_CREADO);
    }

    @Test
    @Transactional
    void getAllComentariosByCreadoIsInShouldWork() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        // Get all the comentarioList where creado in DEFAULT_CREADO or UPDATED_CREADO
        defaultComentarioShouldBeFound("creado.in=" + DEFAULT_CREADO + "," + UPDATED_CREADO);

        // Get all the comentarioList where creado equals to UPDATED_CREADO
        defaultComentarioShouldNotBeFound("creado.in=" + UPDATED_CREADO);
    }

    @Test
    @Transactional
    void getAllComentariosByCreadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        // Get all the comentarioList where creado is not null
        defaultComentarioShouldBeFound("creado.specified=true");

        // Get all the comentarioList where creado is null
        defaultComentarioShouldNotBeFound("creado.specified=false");
    }

    @Test
    @Transactional
    void getAllComentariosByModificadoIsEqualToSomething() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        // Get all the comentarioList where modificado equals to DEFAULT_MODIFICADO
        defaultComentarioShouldBeFound("modificado.equals=" + DEFAULT_MODIFICADO);

        // Get all the comentarioList where modificado equals to UPDATED_MODIFICADO
        defaultComentarioShouldNotBeFound("modificado.equals=" + UPDATED_MODIFICADO);
    }

    @Test
    @Transactional
    void getAllComentariosByModificadoIsInShouldWork() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        // Get all the comentarioList where modificado in DEFAULT_MODIFICADO or UPDATED_MODIFICADO
        defaultComentarioShouldBeFound("modificado.in=" + DEFAULT_MODIFICADO + "," + UPDATED_MODIFICADO);

        // Get all the comentarioList where modificado equals to UPDATED_MODIFICADO
        defaultComentarioShouldNotBeFound("modificado.in=" + UPDATED_MODIFICADO);
    }

    @Test
    @Transactional
    void getAllComentariosByModificadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        // Get all the comentarioList where modificado is not null
        defaultComentarioShouldBeFound("modificado.specified=true");

        // Get all the comentarioList where modificado is null
        defaultComentarioShouldNotBeFound("modificado.specified=false");
    }

    @Test
    @Transactional
    void getAllComentariosByFuncionalidadIsEqualToSomething() throws Exception {
        Funcionalidad funcionalidad;
        if (TestUtil.findAll(em, Funcionalidad.class).isEmpty()) {
            comentarioRepository.saveAndFlush(comentario);
            funcionalidad = FuncionalidadResourceIT.createEntity(em);
        } else {
            funcionalidad = TestUtil.findAll(em, Funcionalidad.class).get(0);
        }
        em.persist(funcionalidad);
        em.flush();
        comentario.setFuncionalidad(funcionalidad);
        comentarioRepository.saveAndFlush(comentario);
        Long funcionalidadId = funcionalidad.getId();

        // Get all the comentarioList where funcionalidad equals to funcionalidadId
        defaultComentarioShouldBeFound("funcionalidadId.equals=" + funcionalidadId);

        // Get all the comentarioList where funcionalidad equals to (funcionalidadId + 1)
        defaultComentarioShouldNotBeFound("funcionalidadId.equals=" + (funcionalidadId + 1));
    }

    @Test
    @Transactional
    void getAllComentariosByUsuarioIsEqualToSomething() throws Exception {
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            comentarioRepository.saveAndFlush(comentario);
            usuario = UsuarioResourceIT.createEntity(em);
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        comentario.setUsuario(usuario);
        comentarioRepository.saveAndFlush(comentario);
        Long usuarioId = usuario.getId();

        // Get all the comentarioList where usuario equals to usuarioId
        defaultComentarioShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the comentarioList where usuario equals to (usuarioId + 1)
        defaultComentarioShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultComentarioShouldBeFound(String filter) throws Exception {
        restComentarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comentario.getId().intValue())))
            .andExpect(jsonPath("$.[*].mensaje").value(hasItem(DEFAULT_MENSAJE)))
            .andExpect(jsonPath("$.[*].creado").value(hasItem(DEFAULT_CREADO.toString())))
            .andExpect(jsonPath("$.[*].modificado").value(hasItem(DEFAULT_MODIFICADO.toString())));

        // Check, that the count call also returns 1
        restComentarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultComentarioShouldNotBeFound(String filter) throws Exception {
        restComentarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restComentarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingComentario() throws Exception {
        // Get the comentario
        restComentarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingComentario() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        int databaseSizeBeforeUpdate = comentarioRepository.findAll().size();

        // Update the comentario
        Comentario updatedComentario = comentarioRepository.findById(comentario.getId()).get();
        // Disconnect from session so that the updates on updatedComentario are not directly saved in db
        em.detach(updatedComentario);
        updatedComentario.mensaje(UPDATED_MENSAJE).creado(UPDATED_CREADO).modificado(UPDATED_MODIFICADO);

        restComentarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedComentario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedComentario))
            )
            .andExpect(status().isOk());

        // Validate the Comentario in the database
        List<Comentario> comentarioList = comentarioRepository.findAll();
        assertThat(comentarioList).hasSize(databaseSizeBeforeUpdate);
        Comentario testComentario = comentarioList.get(comentarioList.size() - 1);
        assertThat(testComentario.getMensaje()).isEqualTo(UPDATED_MENSAJE);
        assertThat(testComentario.getCreado()).isEqualTo(UPDATED_CREADO);
        assertThat(testComentario.getModificado()).isEqualTo(UPDATED_MODIFICADO);
    }

    @Test
    @Transactional
    void putNonExistingComentario() throws Exception {
        int databaseSizeBeforeUpdate = comentarioRepository.findAll().size();
        comentario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComentarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, comentario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comentario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comentario in the database
        List<Comentario> comentarioList = comentarioRepository.findAll();
        assertThat(comentarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComentario() throws Exception {
        int databaseSizeBeforeUpdate = comentarioRepository.findAll().size();
        comentario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComentarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comentario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comentario in the database
        List<Comentario> comentarioList = comentarioRepository.findAll();
        assertThat(comentarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComentario() throws Exception {
        int databaseSizeBeforeUpdate = comentarioRepository.findAll().size();
        comentario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComentarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comentario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Comentario in the database
        List<Comentario> comentarioList = comentarioRepository.findAll();
        assertThat(comentarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComentarioWithPatch() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        int databaseSizeBeforeUpdate = comentarioRepository.findAll().size();

        // Update the comentario using partial update
        Comentario partialUpdatedComentario = new Comentario();
        partialUpdatedComentario.setId(comentario.getId());

        partialUpdatedComentario.mensaje(UPDATED_MENSAJE);

        restComentarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComentario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComentario))
            )
            .andExpect(status().isOk());

        // Validate the Comentario in the database
        List<Comentario> comentarioList = comentarioRepository.findAll();
        assertThat(comentarioList).hasSize(databaseSizeBeforeUpdate);
        Comentario testComentario = comentarioList.get(comentarioList.size() - 1);
        assertThat(testComentario.getMensaje()).isEqualTo(UPDATED_MENSAJE);
        assertThat(testComentario.getCreado()).isEqualTo(DEFAULT_CREADO);
        assertThat(testComentario.getModificado()).isEqualTo(DEFAULT_MODIFICADO);
    }

    @Test
    @Transactional
    void fullUpdateComentarioWithPatch() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        int databaseSizeBeforeUpdate = comentarioRepository.findAll().size();

        // Update the comentario using partial update
        Comentario partialUpdatedComentario = new Comentario();
        partialUpdatedComentario.setId(comentario.getId());

        partialUpdatedComentario.mensaje(UPDATED_MENSAJE).creado(UPDATED_CREADO).modificado(UPDATED_MODIFICADO);

        restComentarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComentario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComentario))
            )
            .andExpect(status().isOk());

        // Validate the Comentario in the database
        List<Comentario> comentarioList = comentarioRepository.findAll();
        assertThat(comentarioList).hasSize(databaseSizeBeforeUpdate);
        Comentario testComentario = comentarioList.get(comentarioList.size() - 1);
        assertThat(testComentario.getMensaje()).isEqualTo(UPDATED_MENSAJE);
        assertThat(testComentario.getCreado()).isEqualTo(UPDATED_CREADO);
        assertThat(testComentario.getModificado()).isEqualTo(UPDATED_MODIFICADO);
    }

    @Test
    @Transactional
    void patchNonExistingComentario() throws Exception {
        int databaseSizeBeforeUpdate = comentarioRepository.findAll().size();
        comentario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComentarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, comentario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(comentario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comentario in the database
        List<Comentario> comentarioList = comentarioRepository.findAll();
        assertThat(comentarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComentario() throws Exception {
        int databaseSizeBeforeUpdate = comentarioRepository.findAll().size();
        comentario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComentarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(comentario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comentario in the database
        List<Comentario> comentarioList = comentarioRepository.findAll();
        assertThat(comentarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComentario() throws Exception {
        int databaseSizeBeforeUpdate = comentarioRepository.findAll().size();
        comentario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComentarioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(comentario))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Comentario in the database
        List<Comentario> comentarioList = comentarioRepository.findAll();
        assertThat(comentarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComentario() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        int databaseSizeBeforeDelete = comentarioRepository.findAll().size();

        // Delete the comentario
        restComentarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, comentario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Comentario> comentarioList = comentarioRepository.findAll();
        assertThat(comentarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
