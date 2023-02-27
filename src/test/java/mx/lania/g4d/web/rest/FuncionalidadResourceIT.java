package mx.lania.g4d.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import mx.lania.g4d.IntegrationTest;
import mx.lania.g4d.domain.Funcionalidad;
import mx.lania.g4d.repository.FuncionalidadRepository;
import mx.lania.g4d.service.FuncionalidadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FuncionalidadResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FuncionalidadResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_URL_GIT_LAB = "AAAAAAAAAA";
    private static final String UPDATED_URL_GIT_LAB = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_ENTREGA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ENTREGA = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_CREADO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREADO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFICADO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFICADO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/funcionalidads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FuncionalidadRepository funcionalidadRepository;

    @Mock
    private FuncionalidadRepository funcionalidadRepositoryMock;

    @Mock
    private FuncionalidadService funcionalidadServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFuncionalidadMockMvc;

    private Funcionalidad funcionalidad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Funcionalidad createEntity(EntityManager em) {
        Funcionalidad funcionalidad = new Funcionalidad()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .urlGitLab(DEFAULT_URL_GIT_LAB)
            .fechaEntrega(DEFAULT_FECHA_ENTREGA)
            .creado(DEFAULT_CREADO)
            .modificado(DEFAULT_MODIFICADO);
        return funcionalidad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Funcionalidad createUpdatedEntity(EntityManager em) {
        Funcionalidad funcionalidad = new Funcionalidad()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .urlGitLab(UPDATED_URL_GIT_LAB)
            .fechaEntrega(UPDATED_FECHA_ENTREGA)
            .creado(UPDATED_CREADO)
            .modificado(UPDATED_MODIFICADO);
        return funcionalidad;
    }

    @BeforeEach
    public void initTest() {
        funcionalidad = createEntity(em);
    }

    @Test
    @Transactional
    void createFuncionalidad() throws Exception {
        int databaseSizeBeforeCreate = funcionalidadRepository.findAll().size();
        // Create the Funcionalidad
        restFuncionalidadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcionalidad)))
            .andExpect(status().isCreated());

        // Validate the Funcionalidad in the database
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeCreate + 1);
        Funcionalidad testFuncionalidad = funcionalidadList.get(funcionalidadList.size() - 1);
        assertThat(testFuncionalidad.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testFuncionalidad.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testFuncionalidad.getUrlGitLab()).isEqualTo(DEFAULT_URL_GIT_LAB);
        assertThat(testFuncionalidad.getFechaEntrega()).isEqualTo(DEFAULT_FECHA_ENTREGA);
        assertThat(testFuncionalidad.getCreado()).isEqualTo(DEFAULT_CREADO);
        assertThat(testFuncionalidad.getModificado()).isEqualTo(DEFAULT_MODIFICADO);
    }

    @Test
    @Transactional
    void createFuncionalidadWithExistingId() throws Exception {
        // Create the Funcionalidad with an existing ID
        funcionalidad.setId(1L);

        int databaseSizeBeforeCreate = funcionalidadRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFuncionalidadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcionalidad)))
            .andExpect(status().isBadRequest());

        // Validate the Funcionalidad in the database
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFuncionalidads() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get all the funcionalidadList
        restFuncionalidadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(funcionalidad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].urlGitLab").value(hasItem(DEFAULT_URL_GIT_LAB)))
            .andExpect(jsonPath("$.[*].fechaEntrega").value(hasItem(DEFAULT_FECHA_ENTREGA.toString())))
            .andExpect(jsonPath("$.[*].creado").value(hasItem(DEFAULT_CREADO.toString())))
            .andExpect(jsonPath("$.[*].modificado").value(hasItem(DEFAULT_MODIFICADO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFuncionalidadsWithEagerRelationshipsIsEnabled() throws Exception {
        when(funcionalidadServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFuncionalidadMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(funcionalidadServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFuncionalidadsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(funcionalidadServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFuncionalidadMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(funcionalidadRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFuncionalidad() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        // Get the funcionalidad
        restFuncionalidadMockMvc
            .perform(get(ENTITY_API_URL_ID, funcionalidad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(funcionalidad.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.urlGitLab").value(DEFAULT_URL_GIT_LAB))
            .andExpect(jsonPath("$.fechaEntrega").value(DEFAULT_FECHA_ENTREGA.toString()))
            .andExpect(jsonPath("$.creado").value(DEFAULT_CREADO.toString()))
            .andExpect(jsonPath("$.modificado").value(DEFAULT_MODIFICADO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFuncionalidad() throws Exception {
        // Get the funcionalidad
        restFuncionalidadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFuncionalidad() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        int databaseSizeBeforeUpdate = funcionalidadRepository.findAll().size();

        // Update the funcionalidad
        Funcionalidad updatedFuncionalidad = funcionalidadRepository.findById(funcionalidad.getId()).get();
        // Disconnect from session so that the updates on updatedFuncionalidad are not directly saved in db
        em.detach(updatedFuncionalidad);
        updatedFuncionalidad
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .urlGitLab(UPDATED_URL_GIT_LAB)
            .fechaEntrega(UPDATED_FECHA_ENTREGA)
            .creado(UPDATED_CREADO)
            .modificado(UPDATED_MODIFICADO);

        restFuncionalidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFuncionalidad.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFuncionalidad))
            )
            .andExpect(status().isOk());

        // Validate the Funcionalidad in the database
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeUpdate);
        Funcionalidad testFuncionalidad = funcionalidadList.get(funcionalidadList.size() - 1);
        assertThat(testFuncionalidad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testFuncionalidad.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testFuncionalidad.getUrlGitLab()).isEqualTo(UPDATED_URL_GIT_LAB);
        assertThat(testFuncionalidad.getFechaEntrega()).isEqualTo(UPDATED_FECHA_ENTREGA);
        assertThat(testFuncionalidad.getCreado()).isEqualTo(UPDATED_CREADO);
        assertThat(testFuncionalidad.getModificado()).isEqualTo(UPDATED_MODIFICADO);
    }

    @Test
    @Transactional
    void putNonExistingFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = funcionalidadRepository.findAll().size();
        funcionalidad.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFuncionalidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, funcionalidad.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(funcionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funcionalidad in the database
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = funcionalidadRepository.findAll().size();
        funcionalidad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncionalidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(funcionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funcionalidad in the database
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = funcionalidadRepository.findAll().size();
        funcionalidad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncionalidadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcionalidad)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Funcionalidad in the database
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFuncionalidadWithPatch() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        int databaseSizeBeforeUpdate = funcionalidadRepository.findAll().size();

        // Update the funcionalidad using partial update
        Funcionalidad partialUpdatedFuncionalidad = new Funcionalidad();
        partialUpdatedFuncionalidad.setId(funcionalidad.getId());

        partialUpdatedFuncionalidad.nombre(UPDATED_NOMBRE).urlGitLab(UPDATED_URL_GIT_LAB);

        restFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFuncionalidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFuncionalidad))
            )
            .andExpect(status().isOk());

        // Validate the Funcionalidad in the database
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeUpdate);
        Funcionalidad testFuncionalidad = funcionalidadList.get(funcionalidadList.size() - 1);
        assertThat(testFuncionalidad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testFuncionalidad.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testFuncionalidad.getUrlGitLab()).isEqualTo(UPDATED_URL_GIT_LAB);
        assertThat(testFuncionalidad.getFechaEntrega()).isEqualTo(DEFAULT_FECHA_ENTREGA);
        assertThat(testFuncionalidad.getCreado()).isEqualTo(DEFAULT_CREADO);
        assertThat(testFuncionalidad.getModificado()).isEqualTo(DEFAULT_MODIFICADO);
    }

    @Test
    @Transactional
    void fullUpdateFuncionalidadWithPatch() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        int databaseSizeBeforeUpdate = funcionalidadRepository.findAll().size();

        // Update the funcionalidad using partial update
        Funcionalidad partialUpdatedFuncionalidad = new Funcionalidad();
        partialUpdatedFuncionalidad.setId(funcionalidad.getId());

        partialUpdatedFuncionalidad
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .urlGitLab(UPDATED_URL_GIT_LAB)
            .fechaEntrega(UPDATED_FECHA_ENTREGA)
            .creado(UPDATED_CREADO)
            .modificado(UPDATED_MODIFICADO);

        restFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFuncionalidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFuncionalidad))
            )
            .andExpect(status().isOk());

        // Validate the Funcionalidad in the database
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeUpdate);
        Funcionalidad testFuncionalidad = funcionalidadList.get(funcionalidadList.size() - 1);
        assertThat(testFuncionalidad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testFuncionalidad.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testFuncionalidad.getUrlGitLab()).isEqualTo(UPDATED_URL_GIT_LAB);
        assertThat(testFuncionalidad.getFechaEntrega()).isEqualTo(UPDATED_FECHA_ENTREGA);
        assertThat(testFuncionalidad.getCreado()).isEqualTo(UPDATED_CREADO);
        assertThat(testFuncionalidad.getModificado()).isEqualTo(UPDATED_MODIFICADO);
    }

    @Test
    @Transactional
    void patchNonExistingFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = funcionalidadRepository.findAll().size();
        funcionalidad.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, funcionalidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(funcionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funcionalidad in the database
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = funcionalidadRepository.findAll().size();
        funcionalidad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(funcionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funcionalidad in the database
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = funcionalidadRepository.findAll().size();
        funcionalidad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(funcionalidad))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Funcionalidad in the database
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFuncionalidad() throws Exception {
        // Initialize the database
        funcionalidadRepository.saveAndFlush(funcionalidad);

        int databaseSizeBeforeDelete = funcionalidadRepository.findAll().size();

        // Delete the funcionalidad
        restFuncionalidadMockMvc
            .perform(delete(ENTITY_API_URL_ID, funcionalidad.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Funcionalidad> funcionalidadList = funcionalidadRepository.findAll();
        assertThat(funcionalidadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
