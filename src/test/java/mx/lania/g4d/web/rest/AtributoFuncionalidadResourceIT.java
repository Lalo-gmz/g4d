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
import mx.lania.g4d.domain.AtributoFuncionalidad;
import mx.lania.g4d.domain.Funcionalidad;
import mx.lania.g4d.repository.AtributoFuncionalidadRepository;
import mx.lania.g4d.service.criteria.AtributoFuncionalidadCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AtributoFuncionalidadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AtributoFuncionalidadResourceIT {

    private static final Boolean DEFAULT_MARCADO = false;
    private static final Boolean UPDATED_MARCADO = true;

    private static final String DEFAULT_VALOR = "AAAAAAAAAA";
    private static final String UPDATED_VALOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/atributo-funcionalidads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AtributoFuncionalidadRepository atributoFuncionalidadRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAtributoFuncionalidadMockMvc;

    private AtributoFuncionalidad atributoFuncionalidad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AtributoFuncionalidad createEntity(EntityManager em) {
        AtributoFuncionalidad atributoFuncionalidad = new AtributoFuncionalidad().marcado(DEFAULT_MARCADO).valor(DEFAULT_VALOR);
        return atributoFuncionalidad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AtributoFuncionalidad createUpdatedEntity(EntityManager em) {
        AtributoFuncionalidad atributoFuncionalidad = new AtributoFuncionalidad().marcado(UPDATED_MARCADO).valor(UPDATED_VALOR);
        return atributoFuncionalidad;
    }

    @BeforeEach
    public void initTest() {
        atributoFuncionalidad = createEntity(em);
    }

    @Test
    @Transactional
    void createAtributoFuncionalidad() throws Exception {
        int databaseSizeBeforeCreate = atributoFuncionalidadRepository.findAll().size();
        // Create the AtributoFuncionalidad
        restAtributoFuncionalidadMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atributoFuncionalidad))
            )
            .andExpect(status().isCreated());

        // Validate the AtributoFuncionalidad in the database
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeCreate + 1);
        AtributoFuncionalidad testAtributoFuncionalidad = atributoFuncionalidadList.get(atributoFuncionalidadList.size() - 1);
        assertThat(testAtributoFuncionalidad.getMarcado()).isEqualTo(DEFAULT_MARCADO);
        assertThat(testAtributoFuncionalidad.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Test
    @Transactional
    void createAtributoFuncionalidadWithExistingId() throws Exception {
        // Create the AtributoFuncionalidad with an existing ID
        atributoFuncionalidad.setId(1L);

        int databaseSizeBeforeCreate = atributoFuncionalidadRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAtributoFuncionalidadMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atributoFuncionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the AtributoFuncionalidad in the database
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAtributoFuncionalidads() throws Exception {
        // Initialize the database
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);

        // Get all the atributoFuncionalidadList
        restAtributoFuncionalidadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(atributoFuncionalidad.getId().intValue())))
            .andExpect(jsonPath("$.[*].marcado").value(hasItem(DEFAULT_MARCADO.booleanValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR)));
    }

    @Test
    @Transactional
    void getAtributoFuncionalidad() throws Exception {
        // Initialize the database
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);

        // Get the atributoFuncionalidad
        restAtributoFuncionalidadMockMvc
            .perform(get(ENTITY_API_URL_ID, atributoFuncionalidad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(atributoFuncionalidad.getId().intValue()))
            .andExpect(jsonPath("$.marcado").value(DEFAULT_MARCADO.booleanValue()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR));
    }

    @Test
    @Transactional
    void getAtributoFuncionalidadsByIdFiltering() throws Exception {
        // Initialize the database
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);

        Long id = atributoFuncionalidad.getId();

        defaultAtributoFuncionalidadShouldBeFound("id.equals=" + id);
        defaultAtributoFuncionalidadShouldNotBeFound("id.notEquals=" + id);

        defaultAtributoFuncionalidadShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAtributoFuncionalidadShouldNotBeFound("id.greaterThan=" + id);

        defaultAtributoFuncionalidadShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAtributoFuncionalidadShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAtributoFuncionalidadsByMarcadoIsEqualToSomething() throws Exception {
        // Initialize the database
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);

        // Get all the atributoFuncionalidadList where marcado equals to DEFAULT_MARCADO
        defaultAtributoFuncionalidadShouldBeFound("marcado.equals=" + DEFAULT_MARCADO);

        // Get all the atributoFuncionalidadList where marcado equals to UPDATED_MARCADO
        defaultAtributoFuncionalidadShouldNotBeFound("marcado.equals=" + UPDATED_MARCADO);
    }

    @Test
    @Transactional
    void getAllAtributoFuncionalidadsByMarcadoIsInShouldWork() throws Exception {
        // Initialize the database
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);

        // Get all the atributoFuncionalidadList where marcado in DEFAULT_MARCADO or UPDATED_MARCADO
        defaultAtributoFuncionalidadShouldBeFound("marcado.in=" + DEFAULT_MARCADO + "," + UPDATED_MARCADO);

        // Get all the atributoFuncionalidadList where marcado equals to UPDATED_MARCADO
        defaultAtributoFuncionalidadShouldNotBeFound("marcado.in=" + UPDATED_MARCADO);
    }

    @Test
    @Transactional
    void getAllAtributoFuncionalidadsByMarcadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);

        // Get all the atributoFuncionalidadList where marcado is not null
        defaultAtributoFuncionalidadShouldBeFound("marcado.specified=true");

        // Get all the atributoFuncionalidadList where marcado is null
        defaultAtributoFuncionalidadShouldNotBeFound("marcado.specified=false");
    }

    @Test
    @Transactional
    void getAllAtributoFuncionalidadsByValorIsEqualToSomething() throws Exception {
        // Initialize the database
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);

        // Get all the atributoFuncionalidadList where valor equals to DEFAULT_VALOR
        defaultAtributoFuncionalidadShouldBeFound("valor.equals=" + DEFAULT_VALOR);

        // Get all the atributoFuncionalidadList where valor equals to UPDATED_VALOR
        defaultAtributoFuncionalidadShouldNotBeFound("valor.equals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllAtributoFuncionalidadsByValorIsInShouldWork() throws Exception {
        // Initialize the database
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);

        // Get all the atributoFuncionalidadList where valor in DEFAULT_VALOR or UPDATED_VALOR
        defaultAtributoFuncionalidadShouldBeFound("valor.in=" + DEFAULT_VALOR + "," + UPDATED_VALOR);

        // Get all the atributoFuncionalidadList where valor equals to UPDATED_VALOR
        defaultAtributoFuncionalidadShouldNotBeFound("valor.in=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllAtributoFuncionalidadsByValorIsNullOrNotNull() throws Exception {
        // Initialize the database
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);

        // Get all the atributoFuncionalidadList where valor is not null
        defaultAtributoFuncionalidadShouldBeFound("valor.specified=true");

        // Get all the atributoFuncionalidadList where valor is null
        defaultAtributoFuncionalidadShouldNotBeFound("valor.specified=false");
    }

    @Test
    @Transactional
    void getAllAtributoFuncionalidadsByValorContainsSomething() throws Exception {
        // Initialize the database
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);

        // Get all the atributoFuncionalidadList where valor contains DEFAULT_VALOR
        defaultAtributoFuncionalidadShouldBeFound("valor.contains=" + DEFAULT_VALOR);

        // Get all the atributoFuncionalidadList where valor contains UPDATED_VALOR
        defaultAtributoFuncionalidadShouldNotBeFound("valor.contains=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllAtributoFuncionalidadsByValorNotContainsSomething() throws Exception {
        // Initialize the database
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);

        // Get all the atributoFuncionalidadList where valor does not contain DEFAULT_VALOR
        defaultAtributoFuncionalidadShouldNotBeFound("valor.doesNotContain=" + DEFAULT_VALOR);

        // Get all the atributoFuncionalidadList where valor does not contain UPDATED_VALOR
        defaultAtributoFuncionalidadShouldBeFound("valor.doesNotContain=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllAtributoFuncionalidadsByFuncionalidadIsEqualToSomething() throws Exception {
        Funcionalidad funcionalidad;
        if (TestUtil.findAll(em, Funcionalidad.class).isEmpty()) {
            atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);
            funcionalidad = FuncionalidadResourceIT.createEntity(em);
        } else {
            funcionalidad = TestUtil.findAll(em, Funcionalidad.class).get(0);
        }
        em.persist(funcionalidad);
        em.flush();
        atributoFuncionalidad.setFuncionalidad(funcionalidad);
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);
        Long funcionalidadId = funcionalidad.getId();

        // Get all the atributoFuncionalidadList where funcionalidad equals to funcionalidadId
        defaultAtributoFuncionalidadShouldBeFound("funcionalidadId.equals=" + funcionalidadId);

        // Get all the atributoFuncionalidadList where funcionalidad equals to (funcionalidadId + 1)
        defaultAtributoFuncionalidadShouldNotBeFound("funcionalidadId.equals=" + (funcionalidadId + 1));
    }

    @Test
    @Transactional
    void getAllAtributoFuncionalidadsByAtributoIsEqualToSomething() throws Exception {
        Atributo atributo;
        if (TestUtil.findAll(em, Atributo.class).isEmpty()) {
            atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);
            atributo = AtributoResourceIT.createEntity(em);
        } else {
            atributo = TestUtil.findAll(em, Atributo.class).get(0);
        }
        em.persist(atributo);
        em.flush();
        atributoFuncionalidad.setAtributo(atributo);
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);
        Long atributoId = atributo.getId();

        // Get all the atributoFuncionalidadList where atributo equals to atributoId
        defaultAtributoFuncionalidadShouldBeFound("atributoId.equals=" + atributoId);

        // Get all the atributoFuncionalidadList where atributo equals to (atributoId + 1)
        defaultAtributoFuncionalidadShouldNotBeFound("atributoId.equals=" + (atributoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAtributoFuncionalidadShouldBeFound(String filter) throws Exception {
        restAtributoFuncionalidadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(atributoFuncionalidad.getId().intValue())))
            .andExpect(jsonPath("$.[*].marcado").value(hasItem(DEFAULT_MARCADO.booleanValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR)));

        // Check, that the count call also returns 1
        restAtributoFuncionalidadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAtributoFuncionalidadShouldNotBeFound(String filter) throws Exception {
        restAtributoFuncionalidadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAtributoFuncionalidadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAtributoFuncionalidad() throws Exception {
        // Get the atributoFuncionalidad
        restAtributoFuncionalidadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAtributoFuncionalidad() throws Exception {
        // Initialize the database
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);

        int databaseSizeBeforeUpdate = atributoFuncionalidadRepository.findAll().size();

        // Update the atributoFuncionalidad
        AtributoFuncionalidad updatedAtributoFuncionalidad = atributoFuncionalidadRepository.findById(atributoFuncionalidad.getId()).get();
        // Disconnect from session so that the updates on updatedAtributoFuncionalidad are not directly saved in db
        em.detach(updatedAtributoFuncionalidad);
        updatedAtributoFuncionalidad.marcado(UPDATED_MARCADO).valor(UPDATED_VALOR);

        restAtributoFuncionalidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAtributoFuncionalidad.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAtributoFuncionalidad))
            )
            .andExpect(status().isOk());

        // Validate the AtributoFuncionalidad in the database
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
        AtributoFuncionalidad testAtributoFuncionalidad = atributoFuncionalidadList.get(atributoFuncionalidadList.size() - 1);
        assertThat(testAtributoFuncionalidad.getMarcado()).isEqualTo(UPDATED_MARCADO);
        assertThat(testAtributoFuncionalidad.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    @Transactional
    void putNonExistingAtributoFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = atributoFuncionalidadRepository.findAll().size();
        atributoFuncionalidad.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAtributoFuncionalidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, atributoFuncionalidad.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atributoFuncionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the AtributoFuncionalidad in the database
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAtributoFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = atributoFuncionalidadRepository.findAll().size();
        atributoFuncionalidad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtributoFuncionalidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atributoFuncionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the AtributoFuncionalidad in the database
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAtributoFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = atributoFuncionalidadRepository.findAll().size();
        atributoFuncionalidad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtributoFuncionalidadMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atributoFuncionalidad))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AtributoFuncionalidad in the database
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAtributoFuncionalidadWithPatch() throws Exception {
        // Initialize the database
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);

        int databaseSizeBeforeUpdate = atributoFuncionalidadRepository.findAll().size();

        // Update the atributoFuncionalidad using partial update
        AtributoFuncionalidad partialUpdatedAtributoFuncionalidad = new AtributoFuncionalidad();
        partialUpdatedAtributoFuncionalidad.setId(atributoFuncionalidad.getId());

        partialUpdatedAtributoFuncionalidad.marcado(UPDATED_MARCADO);

        restAtributoFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAtributoFuncionalidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAtributoFuncionalidad))
            )
            .andExpect(status().isOk());

        // Validate the AtributoFuncionalidad in the database
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
        AtributoFuncionalidad testAtributoFuncionalidad = atributoFuncionalidadList.get(atributoFuncionalidadList.size() - 1);
        assertThat(testAtributoFuncionalidad.getMarcado()).isEqualTo(UPDATED_MARCADO);
        assertThat(testAtributoFuncionalidad.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Test
    @Transactional
    void fullUpdateAtributoFuncionalidadWithPatch() throws Exception {
        // Initialize the database
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);

        int databaseSizeBeforeUpdate = atributoFuncionalidadRepository.findAll().size();

        // Update the atributoFuncionalidad using partial update
        AtributoFuncionalidad partialUpdatedAtributoFuncionalidad = new AtributoFuncionalidad();
        partialUpdatedAtributoFuncionalidad.setId(atributoFuncionalidad.getId());

        partialUpdatedAtributoFuncionalidad.marcado(UPDATED_MARCADO).valor(UPDATED_VALOR);

        restAtributoFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAtributoFuncionalidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAtributoFuncionalidad))
            )
            .andExpect(status().isOk());

        // Validate the AtributoFuncionalidad in the database
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
        AtributoFuncionalidad testAtributoFuncionalidad = atributoFuncionalidadList.get(atributoFuncionalidadList.size() - 1);
        assertThat(testAtributoFuncionalidad.getMarcado()).isEqualTo(UPDATED_MARCADO);
        assertThat(testAtributoFuncionalidad.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    @Transactional
    void patchNonExistingAtributoFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = atributoFuncionalidadRepository.findAll().size();
        atributoFuncionalidad.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAtributoFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, atributoFuncionalidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(atributoFuncionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the AtributoFuncionalidad in the database
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAtributoFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = atributoFuncionalidadRepository.findAll().size();
        atributoFuncionalidad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtributoFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(atributoFuncionalidad))
            )
            .andExpect(status().isBadRequest());

        // Validate the AtributoFuncionalidad in the database
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAtributoFuncionalidad() throws Exception {
        int databaseSizeBeforeUpdate = atributoFuncionalidadRepository.findAll().size();
        atributoFuncionalidad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtributoFuncionalidadMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(atributoFuncionalidad))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AtributoFuncionalidad in the database
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAtributoFuncionalidad() throws Exception {
        // Initialize the database
        atributoFuncionalidadRepository.saveAndFlush(atributoFuncionalidad);

        int databaseSizeBeforeDelete = atributoFuncionalidadRepository.findAll().size();

        // Delete the atributoFuncionalidad
        restAtributoFuncionalidadMockMvc
            .perform(delete(ENTITY_API_URL_ID, atributoFuncionalidad.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AtributoFuncionalidad> atributoFuncionalidadList = atributoFuncionalidadRepository.findAll();
        assertThat(atributoFuncionalidadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
