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
import mx.lania.g4d.domain.Configuracion;
import mx.lania.g4d.domain.Proyecto;
import mx.lania.g4d.domain.enumeration.EtiquetaVisual;
import mx.lania.g4d.repository.ConfiguracionRepository;
import mx.lania.g4d.service.criteria.ConfiguracionCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ConfiguracionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConfiguracionResourceIT {

    private static final EtiquetaVisual DEFAULT_CLAVE = EtiquetaVisual.FUNCIONALIDAD;
    private static final EtiquetaVisual UPDATED_CLAVE = EtiquetaVisual.ITERACION;

    private static final String DEFAULT_VALOR = "AAAAAAAAAA";
    private static final String UPDATED_VALOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/configuracions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConfiguracionRepository configuracionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfiguracionMockMvc;

    private Configuracion configuracion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Configuracion createEntity(EntityManager em) {
        Configuracion configuracion = new Configuracion().clave(DEFAULT_CLAVE).valor(DEFAULT_VALOR);
        return configuracion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Configuracion createUpdatedEntity(EntityManager em) {
        Configuracion configuracion = new Configuracion().clave(UPDATED_CLAVE).valor(UPDATED_VALOR);
        return configuracion;
    }

    @BeforeEach
    public void initTest() {
        configuracion = createEntity(em);
    }

    @Test
    @Transactional
    void createConfiguracion() throws Exception {
        int databaseSizeBeforeCreate = configuracionRepository.findAll().size();
        // Create the Configuracion
        restConfiguracionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configuracion)))
            .andExpect(status().isCreated());

        // Validate the Configuracion in the database
        List<Configuracion> configuracionList = configuracionRepository.findAll();
        assertThat(configuracionList).hasSize(databaseSizeBeforeCreate + 1);
        Configuracion testConfiguracion = configuracionList.get(configuracionList.size() - 1);
        assertThat(testConfiguracion.getClave()).isEqualTo(DEFAULT_CLAVE);
        assertThat(testConfiguracion.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Test
    @Transactional
    void createConfiguracionWithExistingId() throws Exception {
        // Create the Configuracion with an existing ID
        configuracion.setId(1L);

        int databaseSizeBeforeCreate = configuracionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfiguracionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configuracion)))
            .andExpect(status().isBadRequest());

        // Validate the Configuracion in the database
        List<Configuracion> configuracionList = configuracionRepository.findAll();
        assertThat(configuracionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConfiguracions() throws Exception {
        // Initialize the database
        configuracionRepository.saveAndFlush(configuracion);

        // Get all the configuracionList
        restConfiguracionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configuracion.getId().intValue())))
            .andExpect(jsonPath("$.[*].clave").value(hasItem(DEFAULT_CLAVE.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR)));
    }

    @Test
    @Transactional
    void getConfiguracion() throws Exception {
        // Initialize the database
        configuracionRepository.saveAndFlush(configuracion);

        // Get the configuracion
        restConfiguracionMockMvc
            .perform(get(ENTITY_API_URL_ID, configuracion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configuracion.getId().intValue()))
            .andExpect(jsonPath("$.clave").value(DEFAULT_CLAVE.toString()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR));
    }

    @Test
    @Transactional
    void getConfiguracionsByIdFiltering() throws Exception {
        // Initialize the database
        configuracionRepository.saveAndFlush(configuracion);

        Long id = configuracion.getId();

        defaultConfiguracionShouldBeFound("id.equals=" + id);
        defaultConfiguracionShouldNotBeFound("id.notEquals=" + id);

        defaultConfiguracionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultConfiguracionShouldNotBeFound("id.greaterThan=" + id);

        defaultConfiguracionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultConfiguracionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllConfiguracionsByClaveIsEqualToSomething() throws Exception {
        // Initialize the database
        configuracionRepository.saveAndFlush(configuracion);

        // Get all the configuracionList where clave equals to DEFAULT_CLAVE
        defaultConfiguracionShouldBeFound("clave.equals=" + DEFAULT_CLAVE);

        // Get all the configuracionList where clave equals to UPDATED_CLAVE
        defaultConfiguracionShouldNotBeFound("clave.equals=" + UPDATED_CLAVE);
    }

    @Test
    @Transactional
    void getAllConfiguracionsByClaveIsInShouldWork() throws Exception {
        // Initialize the database
        configuracionRepository.saveAndFlush(configuracion);

        // Get all the configuracionList where clave in DEFAULT_CLAVE or UPDATED_CLAVE
        defaultConfiguracionShouldBeFound("clave.in=" + DEFAULT_CLAVE + "," + UPDATED_CLAVE);

        // Get all the configuracionList where clave equals to UPDATED_CLAVE
        defaultConfiguracionShouldNotBeFound("clave.in=" + UPDATED_CLAVE);
    }

    @Test
    @Transactional
    void getAllConfiguracionsByClaveIsNullOrNotNull() throws Exception {
        // Initialize the database
        configuracionRepository.saveAndFlush(configuracion);

        // Get all the configuracionList where clave is not null
        defaultConfiguracionShouldBeFound("clave.specified=true");

        // Get all the configuracionList where clave is null
        defaultConfiguracionShouldNotBeFound("clave.specified=false");
    }

    @Test
    @Transactional
    void getAllConfiguracionsByValorIsEqualToSomething() throws Exception {
        // Initialize the database
        configuracionRepository.saveAndFlush(configuracion);

        // Get all the configuracionList where valor equals to DEFAULT_VALOR
        defaultConfiguracionShouldBeFound("valor.equals=" + DEFAULT_VALOR);

        // Get all the configuracionList where valor equals to UPDATED_VALOR
        defaultConfiguracionShouldNotBeFound("valor.equals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllConfiguracionsByValorIsInShouldWork() throws Exception {
        // Initialize the database
        configuracionRepository.saveAndFlush(configuracion);

        // Get all the configuracionList where valor in DEFAULT_VALOR or UPDATED_VALOR
        defaultConfiguracionShouldBeFound("valor.in=" + DEFAULT_VALOR + "," + UPDATED_VALOR);

        // Get all the configuracionList where valor equals to UPDATED_VALOR
        defaultConfiguracionShouldNotBeFound("valor.in=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllConfiguracionsByValorIsNullOrNotNull() throws Exception {
        // Initialize the database
        configuracionRepository.saveAndFlush(configuracion);

        // Get all the configuracionList where valor is not null
        defaultConfiguracionShouldBeFound("valor.specified=true");

        // Get all the configuracionList where valor is null
        defaultConfiguracionShouldNotBeFound("valor.specified=false");
    }

    @Test
    @Transactional
    void getAllConfiguracionsByValorContainsSomething() throws Exception {
        // Initialize the database
        configuracionRepository.saveAndFlush(configuracion);

        // Get all the configuracionList where valor contains DEFAULT_VALOR
        defaultConfiguracionShouldBeFound("valor.contains=" + DEFAULT_VALOR);

        // Get all the configuracionList where valor contains UPDATED_VALOR
        defaultConfiguracionShouldNotBeFound("valor.contains=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllConfiguracionsByValorNotContainsSomething() throws Exception {
        // Initialize the database
        configuracionRepository.saveAndFlush(configuracion);

        // Get all the configuracionList where valor does not contain DEFAULT_VALOR
        defaultConfiguracionShouldNotBeFound("valor.doesNotContain=" + DEFAULT_VALOR);

        // Get all the configuracionList where valor does not contain UPDATED_VALOR
        defaultConfiguracionShouldBeFound("valor.doesNotContain=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllConfiguracionsByProyectoIsEqualToSomething() throws Exception {
        Proyecto proyecto;
        if (TestUtil.findAll(em, Proyecto.class).isEmpty()) {
            configuracionRepository.saveAndFlush(configuracion);
            proyecto = ProyectoResourceIT.createEntity(em);
        } else {
            proyecto = TestUtil.findAll(em, Proyecto.class).get(0);
        }
        em.persist(proyecto);
        em.flush();
        configuracion.setProyecto(proyecto);
        configuracionRepository.saveAndFlush(configuracion);
        Long proyectoId = proyecto.getId();

        // Get all the configuracionList where proyecto equals to proyectoId
        defaultConfiguracionShouldBeFound("proyectoId.equals=" + proyectoId);

        // Get all the configuracionList where proyecto equals to (proyectoId + 1)
        defaultConfiguracionShouldNotBeFound("proyectoId.equals=" + (proyectoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConfiguracionShouldBeFound(String filter) throws Exception {
        restConfiguracionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configuracion.getId().intValue())))
            .andExpect(jsonPath("$.[*].clave").value(hasItem(DEFAULT_CLAVE.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR)));

        // Check, that the count call also returns 1
        restConfiguracionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConfiguracionShouldNotBeFound(String filter) throws Exception {
        restConfiguracionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConfiguracionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingConfiguracion() throws Exception {
        // Get the configuracion
        restConfiguracionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConfiguracion() throws Exception {
        // Initialize the database
        configuracionRepository.saveAndFlush(configuracion);

        int databaseSizeBeforeUpdate = configuracionRepository.findAll().size();

        // Update the configuracion
        Configuracion updatedConfiguracion = configuracionRepository.findById(configuracion.getId()).get();
        // Disconnect from session so that the updates on updatedConfiguracion are not directly saved in db
        em.detach(updatedConfiguracion);
        updatedConfiguracion.clave(UPDATED_CLAVE).valor(UPDATED_VALOR);

        restConfiguracionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConfiguracion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConfiguracion))
            )
            .andExpect(status().isOk());

        // Validate the Configuracion in the database
        List<Configuracion> configuracionList = configuracionRepository.findAll();
        assertThat(configuracionList).hasSize(databaseSizeBeforeUpdate);
        Configuracion testConfiguracion = configuracionList.get(configuracionList.size() - 1);
        assertThat(testConfiguracion.getClave()).isEqualTo(UPDATED_CLAVE);
        assertThat(testConfiguracion.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    @Transactional
    void putNonExistingConfiguracion() throws Exception {
        int databaseSizeBeforeUpdate = configuracionRepository.findAll().size();
        configuracion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfiguracionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configuracion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configuracion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Configuracion in the database
        List<Configuracion> configuracionList = configuracionRepository.findAll();
        assertThat(configuracionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConfiguracion() throws Exception {
        int databaseSizeBeforeUpdate = configuracionRepository.findAll().size();
        configuracion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfiguracionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configuracion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Configuracion in the database
        List<Configuracion> configuracionList = configuracionRepository.findAll();
        assertThat(configuracionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConfiguracion() throws Exception {
        int databaseSizeBeforeUpdate = configuracionRepository.findAll().size();
        configuracion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfiguracionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configuracion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Configuracion in the database
        List<Configuracion> configuracionList = configuracionRepository.findAll();
        assertThat(configuracionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConfiguracionWithPatch() throws Exception {
        // Initialize the database
        configuracionRepository.saveAndFlush(configuracion);

        int databaseSizeBeforeUpdate = configuracionRepository.findAll().size();

        // Update the configuracion using partial update
        Configuracion partialUpdatedConfiguracion = new Configuracion();
        partialUpdatedConfiguracion.setId(configuracion.getId());

        partialUpdatedConfiguracion.valor(UPDATED_VALOR);

        restConfiguracionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfiguracion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConfiguracion))
            )
            .andExpect(status().isOk());

        // Validate the Configuracion in the database
        List<Configuracion> configuracionList = configuracionRepository.findAll();
        assertThat(configuracionList).hasSize(databaseSizeBeforeUpdate);
        Configuracion testConfiguracion = configuracionList.get(configuracionList.size() - 1);
        assertThat(testConfiguracion.getClave()).isEqualTo(DEFAULT_CLAVE);
        assertThat(testConfiguracion.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    @Transactional
    void fullUpdateConfiguracionWithPatch() throws Exception {
        // Initialize the database
        configuracionRepository.saveAndFlush(configuracion);

        int databaseSizeBeforeUpdate = configuracionRepository.findAll().size();

        // Update the configuracion using partial update
        Configuracion partialUpdatedConfiguracion = new Configuracion();
        partialUpdatedConfiguracion.setId(configuracion.getId());

        partialUpdatedConfiguracion.clave(UPDATED_CLAVE).valor(UPDATED_VALOR);

        restConfiguracionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfiguracion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConfiguracion))
            )
            .andExpect(status().isOk());

        // Validate the Configuracion in the database
        List<Configuracion> configuracionList = configuracionRepository.findAll();
        assertThat(configuracionList).hasSize(databaseSizeBeforeUpdate);
        Configuracion testConfiguracion = configuracionList.get(configuracionList.size() - 1);
        assertThat(testConfiguracion.getClave()).isEqualTo(UPDATED_CLAVE);
        assertThat(testConfiguracion.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    @Transactional
    void patchNonExistingConfiguracion() throws Exception {
        int databaseSizeBeforeUpdate = configuracionRepository.findAll().size();
        configuracion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfiguracionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, configuracion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(configuracion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Configuracion in the database
        List<Configuracion> configuracionList = configuracionRepository.findAll();
        assertThat(configuracionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConfiguracion() throws Exception {
        int databaseSizeBeforeUpdate = configuracionRepository.findAll().size();
        configuracion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfiguracionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(configuracion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Configuracion in the database
        List<Configuracion> configuracionList = configuracionRepository.findAll();
        assertThat(configuracionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConfiguracion() throws Exception {
        int databaseSizeBeforeUpdate = configuracionRepository.findAll().size();
        configuracion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfiguracionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(configuracion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Configuracion in the database
        List<Configuracion> configuracionList = configuracionRepository.findAll();
        assertThat(configuracionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConfiguracion() throws Exception {
        // Initialize the database
        configuracionRepository.saveAndFlush(configuracion);

        int databaseSizeBeforeDelete = configuracionRepository.findAll().size();

        // Delete the configuracion
        restConfiguracionMockMvc
            .perform(delete(ENTITY_API_URL_ID, configuracion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Configuracion> configuracionList = configuracionRepository.findAll();
        assertThat(configuracionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
