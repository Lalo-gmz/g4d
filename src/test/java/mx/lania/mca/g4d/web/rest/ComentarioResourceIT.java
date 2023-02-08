package mx.lania.mca.g4d.web.rest;

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
import mx.lania.mca.g4d.IntegrationTest;
import mx.lania.mca.g4d.domain.Comentario;
import mx.lania.mca.g4d.repository.ComentarioRepository;
import mx.lania.mca.g4d.service.dto.ComentarioDTO;
import mx.lania.mca.g4d.service.mapper.ComentarioMapper;
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
    private ComentarioMapper comentarioMapper;

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
        ComentarioDTO comentarioDTO = comentarioMapper.toDto(comentario);
        restComentarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comentarioDTO)))
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
        ComentarioDTO comentarioDTO = comentarioMapper.toDto(comentario);

        int databaseSizeBeforeCreate = comentarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComentarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comentarioDTO)))
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
        ComentarioDTO comentarioDTO = comentarioMapper.toDto(comentario);

        restComentarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comentarioDTO)))
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
        ComentarioDTO comentarioDTO = comentarioMapper.toDto(updatedComentario);

        restComentarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, comentarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comentarioDTO))
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

        // Create the Comentario
        ComentarioDTO comentarioDTO = comentarioMapper.toDto(comentario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComentarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, comentarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comentarioDTO))
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

        // Create the Comentario
        ComentarioDTO comentarioDTO = comentarioMapper.toDto(comentario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComentarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comentarioDTO))
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

        // Create the Comentario
        ComentarioDTO comentarioDTO = comentarioMapper.toDto(comentario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComentarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comentarioDTO)))
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

        // Create the Comentario
        ComentarioDTO comentarioDTO = comentarioMapper.toDto(comentario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComentarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, comentarioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(comentarioDTO))
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

        // Create the Comentario
        ComentarioDTO comentarioDTO = comentarioMapper.toDto(comentario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComentarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(comentarioDTO))
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

        // Create the Comentario
        ComentarioDTO comentarioDTO = comentarioMapper.toDto(comentario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComentarioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(comentarioDTO))
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
