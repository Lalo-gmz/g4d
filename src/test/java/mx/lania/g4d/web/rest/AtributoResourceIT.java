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
import mx.lania.g4d.domain.Atributo;
import mx.lania.g4d.domain.Funcionalidad;
import mx.lania.g4d.repository.AtributoRepository;
import mx.lania.g4d.service.criteria.AtributoCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AtributoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AtributoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_MARCADO = false;
    private static final Boolean UPDATED_MARCADO = true;

    private static final Boolean DEFAULT_AUXILIAR = false;
    private static final Boolean UPDATED_AUXILIAR = true;

    private static final String ENTITY_API_URL = "/api/atributos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AtributoRepository atributoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAtributoMockMvc;

    private Atributo atributo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Atributo createEntity(EntityManager em) {
        Atributo atributo = new Atributo().nombre(DEFAULT_NOMBRE).marcado(DEFAULT_MARCADO).auxiliar(DEFAULT_AUXILIAR);
        return atributo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Atributo createUpdatedEntity(EntityManager em) {
        Atributo atributo = new Atributo().nombre(UPDATED_NOMBRE).marcado(UPDATED_MARCADO).auxiliar(UPDATED_AUXILIAR);
        return atributo;
    }

    @BeforeEach
    public void initTest() {
        atributo = createEntity(em);
    }

    @Test
    @Transactional
    void createAtributo() throws Exception {
        int databaseSizeBeforeCreate = atributoRepository.findAll().size();
        // Create the Atributo
        restAtributoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(atributo)))
            .andExpect(status().isCreated());

        // Validate the Atributo in the database
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeCreate + 1);
        Atributo testAtributo = atributoList.get(atributoList.size() - 1);
        assertThat(testAtributo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testAtributo.getMarcado()).isEqualTo(DEFAULT_MARCADO);
        assertThat(testAtributo.getAuxiliar()).isEqualTo(DEFAULT_AUXILIAR);
    }

    @Test
    @Transactional
    void createAtributoWithExistingId() throws Exception {
        // Create the Atributo with an existing ID
        atributo.setId(1L);

        int databaseSizeBeforeCreate = atributoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAtributoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(atributo)))
            .andExpect(status().isBadRequest());

        // Validate the Atributo in the database
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAtributos() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        // Get all the atributoList
        restAtributoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(atributo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].marcado").value(hasItem(DEFAULT_MARCADO.booleanValue())))
            .andExpect(jsonPath("$.[*].auxiliar").value(hasItem(DEFAULT_AUXILIAR.booleanValue())));
    }

    @Test
    @Transactional
    void getAtributo() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        // Get the atributo
        restAtributoMockMvc
            .perform(get(ENTITY_API_URL_ID, atributo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(atributo.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.marcado").value(DEFAULT_MARCADO.booleanValue()))
            .andExpect(jsonPath("$.auxiliar").value(DEFAULT_AUXILIAR.booleanValue()));
    }

    @Test
    @Transactional
    void getAtributosByIdFiltering() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        Long id = atributo.getId();

        defaultAtributoShouldBeFound("id.equals=" + id);
        defaultAtributoShouldNotBeFound("id.notEquals=" + id);

        defaultAtributoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAtributoShouldNotBeFound("id.greaterThan=" + id);

        defaultAtributoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAtributoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAtributosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        // Get all the atributoList where nombre equals to DEFAULT_NOMBRE
        defaultAtributoShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the atributoList where nombre equals to UPDATED_NOMBRE
        defaultAtributoShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllAtributosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        // Get all the atributoList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultAtributoShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the atributoList where nombre equals to UPDATED_NOMBRE
        defaultAtributoShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllAtributosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        // Get all the atributoList where nombre is not null
        defaultAtributoShouldBeFound("nombre.specified=true");

        // Get all the atributoList where nombre is null
        defaultAtributoShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllAtributosByNombreContainsSomething() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        // Get all the atributoList where nombre contains DEFAULT_NOMBRE
        defaultAtributoShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the atributoList where nombre contains UPDATED_NOMBRE
        defaultAtributoShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllAtributosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        // Get all the atributoList where nombre does not contain DEFAULT_NOMBRE
        defaultAtributoShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the atributoList where nombre does not contain UPDATED_NOMBRE
        defaultAtributoShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllAtributosByMarcadoIsEqualToSomething() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        // Get all the atributoList where marcado equals to DEFAULT_MARCADO
        defaultAtributoShouldBeFound("marcado.equals=" + DEFAULT_MARCADO);

        // Get all the atributoList where marcado equals to UPDATED_MARCADO
        defaultAtributoShouldNotBeFound("marcado.equals=" + UPDATED_MARCADO);
    }

    @Test
    @Transactional
    void getAllAtributosByMarcadoIsInShouldWork() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        // Get all the atributoList where marcado in DEFAULT_MARCADO or UPDATED_MARCADO
        defaultAtributoShouldBeFound("marcado.in=" + DEFAULT_MARCADO + "," + UPDATED_MARCADO);

        // Get all the atributoList where marcado equals to UPDATED_MARCADO
        defaultAtributoShouldNotBeFound("marcado.in=" + UPDATED_MARCADO);
    }

    @Test
    @Transactional
    void getAllAtributosByMarcadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        // Get all the atributoList where marcado is not null
        defaultAtributoShouldBeFound("marcado.specified=true");

        // Get all the atributoList where marcado is null
        defaultAtributoShouldNotBeFound("marcado.specified=false");
    }

    @Test
    @Transactional
    void getAllAtributosByAuxiliarIsEqualToSomething() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        // Get all the atributoList where auxiliar equals to DEFAULT_AUXILIAR
        defaultAtributoShouldBeFound("auxiliar.equals=" + DEFAULT_AUXILIAR);

        // Get all the atributoList where auxiliar equals to UPDATED_AUXILIAR
        defaultAtributoShouldNotBeFound("auxiliar.equals=" + UPDATED_AUXILIAR);
    }

    @Test
    @Transactional
    void getAllAtributosByAuxiliarIsInShouldWork() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        // Get all the atributoList where auxiliar in DEFAULT_AUXILIAR or UPDATED_AUXILIAR
        defaultAtributoShouldBeFound("auxiliar.in=" + DEFAULT_AUXILIAR + "," + UPDATED_AUXILIAR);

        // Get all the atributoList where auxiliar equals to UPDATED_AUXILIAR
        defaultAtributoShouldNotBeFound("auxiliar.in=" + UPDATED_AUXILIAR);
    }

    @Test
    @Transactional
    void getAllAtributosByAuxiliarIsNullOrNotNull() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        // Get all the atributoList where auxiliar is not null
        defaultAtributoShouldBeFound("auxiliar.specified=true");

        // Get all the atributoList where auxiliar is null
        defaultAtributoShouldNotBeFound("auxiliar.specified=false");
    }

    @Test
    @Transactional
    void getAllAtributosByFuncionalidadIsEqualToSomething() throws Exception {
        Funcionalidad funcionalidad;
        if (TestUtil.findAll(em, Funcionalidad.class).isEmpty()) {
            atributoRepository.saveAndFlush(atributo);
            funcionalidad = FuncionalidadResourceIT.createEntity(em);
        } else {
            funcionalidad = TestUtil.findAll(em, Funcionalidad.class).get(0);
        }
        em.persist(funcionalidad);
        em.flush();
        atributo.setFuncionalidad(funcionalidad);
        atributoRepository.saveAndFlush(atributo);
        Long funcionalidadId = funcionalidad.getId();

        // Get all the atributoList where funcionalidad equals to funcionalidadId
        defaultAtributoShouldBeFound("funcionalidadId.equals=" + funcionalidadId);

        // Get all the atributoList where funcionalidad equals to (funcionalidadId + 1)
        defaultAtributoShouldNotBeFound("funcionalidadId.equals=" + (funcionalidadId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAtributoShouldBeFound(String filter) throws Exception {
        restAtributoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(atributo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].marcado").value(hasItem(DEFAULT_MARCADO.booleanValue())))
            .andExpect(jsonPath("$.[*].auxiliar").value(hasItem(DEFAULT_AUXILIAR.booleanValue())));

        // Check, that the count call also returns 1
        restAtributoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAtributoShouldNotBeFound(String filter) throws Exception {
        restAtributoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAtributoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAtributo() throws Exception {
        // Get the atributo
        restAtributoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAtributo() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        int databaseSizeBeforeUpdate = atributoRepository.findAll().size();

        // Update the atributo
        Atributo updatedAtributo = atributoRepository.findById(atributo.getId()).get();
        // Disconnect from session so that the updates on updatedAtributo are not directly saved in db
        em.detach(updatedAtributo);
        updatedAtributo.nombre(UPDATED_NOMBRE).marcado(UPDATED_MARCADO).auxiliar(UPDATED_AUXILIAR);

        restAtributoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAtributo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAtributo))
            )
            .andExpect(status().isOk());

        // Validate the Atributo in the database
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeUpdate);
        Atributo testAtributo = atributoList.get(atributoList.size() - 1);
        assertThat(testAtributo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testAtributo.getMarcado()).isEqualTo(UPDATED_MARCADO);
        assertThat(testAtributo.getAuxiliar()).isEqualTo(UPDATED_AUXILIAR);
    }

    @Test
    @Transactional
    void putNonExistingAtributo() throws Exception {
        int databaseSizeBeforeUpdate = atributoRepository.findAll().size();
        atributo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAtributoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, atributo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atributo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Atributo in the database
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAtributo() throws Exception {
        int databaseSizeBeforeUpdate = atributoRepository.findAll().size();
        atributo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtributoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atributo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Atributo in the database
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAtributo() throws Exception {
        int databaseSizeBeforeUpdate = atributoRepository.findAll().size();
        atributo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtributoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(atributo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Atributo in the database
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAtributoWithPatch() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        int databaseSizeBeforeUpdate = atributoRepository.findAll().size();

        // Update the atributo using partial update
        Atributo partialUpdatedAtributo = new Atributo();
        partialUpdatedAtributo.setId(atributo.getId());

        partialUpdatedAtributo.nombre(UPDATED_NOMBRE).marcado(UPDATED_MARCADO).auxiliar(UPDATED_AUXILIAR);

        restAtributoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAtributo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAtributo))
            )
            .andExpect(status().isOk());

        // Validate the Atributo in the database
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeUpdate);
        Atributo testAtributo = atributoList.get(atributoList.size() - 1);
        assertThat(testAtributo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testAtributo.getMarcado()).isEqualTo(UPDATED_MARCADO);
        assertThat(testAtributo.getAuxiliar()).isEqualTo(UPDATED_AUXILIAR);
    }

    @Test
    @Transactional
    void fullUpdateAtributoWithPatch() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        int databaseSizeBeforeUpdate = atributoRepository.findAll().size();

        // Update the atributo using partial update
        Atributo partialUpdatedAtributo = new Atributo();
        partialUpdatedAtributo.setId(atributo.getId());

        partialUpdatedAtributo.nombre(UPDATED_NOMBRE).marcado(UPDATED_MARCADO).auxiliar(UPDATED_AUXILIAR);

        restAtributoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAtributo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAtributo))
            )
            .andExpect(status().isOk());

        // Validate the Atributo in the database
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeUpdate);
        Atributo testAtributo = atributoList.get(atributoList.size() - 1);
        assertThat(testAtributo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testAtributo.getMarcado()).isEqualTo(UPDATED_MARCADO);
        assertThat(testAtributo.getAuxiliar()).isEqualTo(UPDATED_AUXILIAR);
    }

    @Test
    @Transactional
    void patchNonExistingAtributo() throws Exception {
        int databaseSizeBeforeUpdate = atributoRepository.findAll().size();
        atributo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAtributoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, atributo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(atributo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Atributo in the database
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAtributo() throws Exception {
        int databaseSizeBeforeUpdate = atributoRepository.findAll().size();
        atributo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtributoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(atributo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Atributo in the database
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAtributo() throws Exception {
        int databaseSizeBeforeUpdate = atributoRepository.findAll().size();
        atributo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtributoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(atributo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Atributo in the database
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAtributo() throws Exception {
        // Initialize the database
        atributoRepository.saveAndFlush(atributo);

        int databaseSizeBeforeDelete = atributoRepository.findAll().size();

        // Delete the atributo
        restAtributoMockMvc
            .perform(delete(ENTITY_API_URL_ID, atributo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Atributo> atributoList = atributoRepository.findAll();
        assertThat(atributoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
