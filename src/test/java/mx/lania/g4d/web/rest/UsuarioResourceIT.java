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
import mx.lania.g4d.domain.Comentario;
import mx.lania.g4d.domain.Funcionalidad;
import mx.lania.g4d.domain.Proyecto;
import mx.lania.g4d.domain.Rol;
import mx.lania.g4d.domain.Usuario;
import mx.lania.g4d.repository.UsuarioRepository;
import mx.lania.g4d.service.criteria.UsuarioCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UsuarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UsuarioResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_ID_GIT_LAB = "AAAAAAAAAA";
    private static final String UPDATED_ID_GIT_LAB = "BBBBBBBBBB";

    private static final String DEFAULT_TOKEN_IDENTIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN_IDENTIFICACION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsuarioMockMvc;

    private Usuario usuario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createEntity(EntityManager em) {
        Usuario usuario = new Usuario()
            .nombre(DEFAULT_NOMBRE)
            .idGitLab(DEFAULT_ID_GIT_LAB)
            .tokenIdentificacion(DEFAULT_TOKEN_IDENTIFICACION);
        return usuario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createUpdatedEntity(EntityManager em) {
        Usuario usuario = new Usuario()
            .nombre(UPDATED_NOMBRE)
            .idGitLab(UPDATED_ID_GIT_LAB)
            .tokenIdentificacion(UPDATED_TOKEN_IDENTIFICACION);
        return usuario;
    }

    @BeforeEach
    public void initTest() {
        usuario = createEntity(em);
    }

    @Test
    @Transactional
    void createUsuario() throws Exception {
        int databaseSizeBeforeCreate = usuarioRepository.findAll().size();
        // Create the Usuario
        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuario)))
            .andExpect(status().isCreated());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate + 1);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testUsuario.getIdGitLab()).isEqualTo(DEFAULT_ID_GIT_LAB);
        assertThat(testUsuario.getTokenIdentificacion()).isEqualTo(DEFAULT_TOKEN_IDENTIFICACION);
    }

    @Test
    @Transactional
    void createUsuarioWithExistingId() throws Exception {
        // Create the Usuario with an existing ID
        usuario.setId(1L);

        int databaseSizeBeforeCreate = usuarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuario)))
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().size();
        // set the field null
        usuario.setNombre(null);

        // Create the Usuario, which fails.

        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuario)))
            .andExpect(status().isBadRequest());

        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdGitLabIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().size();
        // set the field null
        usuario.setIdGitLab(null);

        // Create the Usuario, which fails.

        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuario)))
            .andExpect(status().isBadRequest());

        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUsuarios() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].idGitLab").value(hasItem(DEFAULT_ID_GIT_LAB)))
            .andExpect(jsonPath("$.[*].tokenIdentificacion").value(hasItem(DEFAULT_TOKEN_IDENTIFICACION)));
    }

    @Test
    @Transactional
    void getUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get the usuario
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL_ID, usuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(usuario.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.idGitLab").value(DEFAULT_ID_GIT_LAB))
            .andExpect(jsonPath("$.tokenIdentificacion").value(DEFAULT_TOKEN_IDENTIFICACION));
    }

    @Test
    @Transactional
    void getUsuariosByIdFiltering() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        Long id = usuario.getId();

        defaultUsuarioShouldBeFound("id.equals=" + id);
        defaultUsuarioShouldNotBeFound("id.notEquals=" + id);

        defaultUsuarioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUsuarioShouldNotBeFound("id.greaterThan=" + id);

        defaultUsuarioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUsuarioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUsuariosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nombre equals to DEFAULT_NOMBRE
        defaultUsuarioShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the usuarioList where nombre equals to UPDATED_NOMBRE
        defaultUsuarioShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultUsuarioShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the usuarioList where nombre equals to UPDATED_NOMBRE
        defaultUsuarioShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nombre is not null
        defaultUsuarioShouldBeFound("nombre.specified=true");

        // Get all the usuarioList where nombre is null
        defaultUsuarioShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByNombreContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nombre contains DEFAULT_NOMBRE
        defaultUsuarioShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the usuarioList where nombre contains UPDATED_NOMBRE
        defaultUsuarioShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nombre does not contain DEFAULT_NOMBRE
        defaultUsuarioShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the usuarioList where nombre does not contain UPDATED_NOMBRE
        defaultUsuarioShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosByIdGitLabIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where idGitLab equals to DEFAULT_ID_GIT_LAB
        defaultUsuarioShouldBeFound("idGitLab.equals=" + DEFAULT_ID_GIT_LAB);

        // Get all the usuarioList where idGitLab equals to UPDATED_ID_GIT_LAB
        defaultUsuarioShouldNotBeFound("idGitLab.equals=" + UPDATED_ID_GIT_LAB);
    }

    @Test
    @Transactional
    void getAllUsuariosByIdGitLabIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where idGitLab in DEFAULT_ID_GIT_LAB or UPDATED_ID_GIT_LAB
        defaultUsuarioShouldBeFound("idGitLab.in=" + DEFAULT_ID_GIT_LAB + "," + UPDATED_ID_GIT_LAB);

        // Get all the usuarioList where idGitLab equals to UPDATED_ID_GIT_LAB
        defaultUsuarioShouldNotBeFound("idGitLab.in=" + UPDATED_ID_GIT_LAB);
    }

    @Test
    @Transactional
    void getAllUsuariosByIdGitLabIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where idGitLab is not null
        defaultUsuarioShouldBeFound("idGitLab.specified=true");

        // Get all the usuarioList where idGitLab is null
        defaultUsuarioShouldNotBeFound("idGitLab.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByIdGitLabContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where idGitLab contains DEFAULT_ID_GIT_LAB
        defaultUsuarioShouldBeFound("idGitLab.contains=" + DEFAULT_ID_GIT_LAB);

        // Get all the usuarioList where idGitLab contains UPDATED_ID_GIT_LAB
        defaultUsuarioShouldNotBeFound("idGitLab.contains=" + UPDATED_ID_GIT_LAB);
    }

    @Test
    @Transactional
    void getAllUsuariosByIdGitLabNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where idGitLab does not contain DEFAULT_ID_GIT_LAB
        defaultUsuarioShouldNotBeFound("idGitLab.doesNotContain=" + DEFAULT_ID_GIT_LAB);

        // Get all the usuarioList where idGitLab does not contain UPDATED_ID_GIT_LAB
        defaultUsuarioShouldBeFound("idGitLab.doesNotContain=" + UPDATED_ID_GIT_LAB);
    }

    @Test
    @Transactional
    void getAllUsuariosByTokenIdentificacionIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where tokenIdentificacion equals to DEFAULT_TOKEN_IDENTIFICACION
        defaultUsuarioShouldBeFound("tokenIdentificacion.equals=" + DEFAULT_TOKEN_IDENTIFICACION);

        // Get all the usuarioList where tokenIdentificacion equals to UPDATED_TOKEN_IDENTIFICACION
        defaultUsuarioShouldNotBeFound("tokenIdentificacion.equals=" + UPDATED_TOKEN_IDENTIFICACION);
    }

    @Test
    @Transactional
    void getAllUsuariosByTokenIdentificacionIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where tokenIdentificacion in DEFAULT_TOKEN_IDENTIFICACION or UPDATED_TOKEN_IDENTIFICACION
        defaultUsuarioShouldBeFound("tokenIdentificacion.in=" + DEFAULT_TOKEN_IDENTIFICACION + "," + UPDATED_TOKEN_IDENTIFICACION);

        // Get all the usuarioList where tokenIdentificacion equals to UPDATED_TOKEN_IDENTIFICACION
        defaultUsuarioShouldNotBeFound("tokenIdentificacion.in=" + UPDATED_TOKEN_IDENTIFICACION);
    }

    @Test
    @Transactional
    void getAllUsuariosByTokenIdentificacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where tokenIdentificacion is not null
        defaultUsuarioShouldBeFound("tokenIdentificacion.specified=true");

        // Get all the usuarioList where tokenIdentificacion is null
        defaultUsuarioShouldNotBeFound("tokenIdentificacion.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByTokenIdentificacionContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where tokenIdentificacion contains DEFAULT_TOKEN_IDENTIFICACION
        defaultUsuarioShouldBeFound("tokenIdentificacion.contains=" + DEFAULT_TOKEN_IDENTIFICACION);

        // Get all the usuarioList where tokenIdentificacion contains UPDATED_TOKEN_IDENTIFICACION
        defaultUsuarioShouldNotBeFound("tokenIdentificacion.contains=" + UPDATED_TOKEN_IDENTIFICACION);
    }

    @Test
    @Transactional
    void getAllUsuariosByTokenIdentificacionNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where tokenIdentificacion does not contain DEFAULT_TOKEN_IDENTIFICACION
        defaultUsuarioShouldNotBeFound("tokenIdentificacion.doesNotContain=" + DEFAULT_TOKEN_IDENTIFICACION);

        // Get all the usuarioList where tokenIdentificacion does not contain UPDATED_TOKEN_IDENTIFICACION
        defaultUsuarioShouldBeFound("tokenIdentificacion.doesNotContain=" + UPDATED_TOKEN_IDENTIFICACION);
    }

    @Test
    @Transactional
    void getAllUsuariosByFuncionalidadIsEqualToSomething() throws Exception {
        Funcionalidad funcionalidad;
        if (TestUtil.findAll(em, Funcionalidad.class).isEmpty()) {
            usuarioRepository.saveAndFlush(usuario);
            funcionalidad = FuncionalidadResourceIT.createEntity(em);
        } else {
            funcionalidad = TestUtil.findAll(em, Funcionalidad.class).get(0);
        }
        em.persist(funcionalidad);
        em.flush();
        usuario.setFuncionalidad(funcionalidad);
        usuarioRepository.saveAndFlush(usuario);
        Long funcionalidadId = funcionalidad.getId();

        // Get all the usuarioList where funcionalidad equals to funcionalidadId
        defaultUsuarioShouldBeFound("funcionalidadId.equals=" + funcionalidadId);

        // Get all the usuarioList where funcionalidad equals to (funcionalidadId + 1)
        defaultUsuarioShouldNotBeFound("funcionalidadId.equals=" + (funcionalidadId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByProyectoIsEqualToSomething() throws Exception {
        Proyecto proyecto;
        if (TestUtil.findAll(em, Proyecto.class).isEmpty()) {
            usuarioRepository.saveAndFlush(usuario);
            proyecto = ProyectoResourceIT.createEntity(em);
        } else {
            proyecto = TestUtil.findAll(em, Proyecto.class).get(0);
        }
        em.persist(proyecto);
        em.flush();
        usuario.setProyecto(proyecto);
        usuarioRepository.saveAndFlush(usuario);
        Long proyectoId = proyecto.getId();

        // Get all the usuarioList where proyecto equals to proyectoId
        defaultUsuarioShouldBeFound("proyectoId.equals=" + proyectoId);

        // Get all the usuarioList where proyecto equals to (proyectoId + 1)
        defaultUsuarioShouldNotBeFound("proyectoId.equals=" + (proyectoId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByRolIsEqualToSomething() throws Exception {
        Rol rol;
        if (TestUtil.findAll(em, Rol.class).isEmpty()) {
            usuarioRepository.saveAndFlush(usuario);
            rol = RolResourceIT.createEntity(em);
        } else {
            rol = TestUtil.findAll(em, Rol.class).get(0);
        }
        em.persist(rol);
        em.flush();
        usuario.setRol(rol);
        usuarioRepository.saveAndFlush(usuario);
        Long rolId = rol.getId();

        // Get all the usuarioList where rol equals to rolId
        defaultUsuarioShouldBeFound("rolId.equals=" + rolId);

        // Get all the usuarioList where rol equals to (rolId + 1)
        defaultUsuarioShouldNotBeFound("rolId.equals=" + (rolId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByBitacoraIsEqualToSomething() throws Exception {
        Bitacora bitacora;
        if (TestUtil.findAll(em, Bitacora.class).isEmpty()) {
            usuarioRepository.saveAndFlush(usuario);
            bitacora = BitacoraResourceIT.createEntity(em);
        } else {
            bitacora = TestUtil.findAll(em, Bitacora.class).get(0);
        }
        em.persist(bitacora);
        em.flush();
        usuario.addBitacora(bitacora);
        usuarioRepository.saveAndFlush(usuario);
        Long bitacoraId = bitacora.getId();

        // Get all the usuarioList where bitacora equals to bitacoraId
        defaultUsuarioShouldBeFound("bitacoraId.equals=" + bitacoraId);

        // Get all the usuarioList where bitacora equals to (bitacoraId + 1)
        defaultUsuarioShouldNotBeFound("bitacoraId.equals=" + (bitacoraId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByComentarioIsEqualToSomething() throws Exception {
        Comentario comentario;
        if (TestUtil.findAll(em, Comentario.class).isEmpty()) {
            usuarioRepository.saveAndFlush(usuario);
            comentario = ComentarioResourceIT.createEntity(em);
        } else {
            comentario = TestUtil.findAll(em, Comentario.class).get(0);
        }
        em.persist(comentario);
        em.flush();
        usuario.addComentario(comentario);
        usuarioRepository.saveAndFlush(usuario);
        Long comentarioId = comentario.getId();

        // Get all the usuarioList where comentario equals to comentarioId
        defaultUsuarioShouldBeFound("comentarioId.equals=" + comentarioId);

        // Get all the usuarioList where comentario equals to (comentarioId + 1)
        defaultUsuarioShouldNotBeFound("comentarioId.equals=" + (comentarioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUsuarioShouldBeFound(String filter) throws Exception {
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].idGitLab").value(hasItem(DEFAULT_ID_GIT_LAB)))
            .andExpect(jsonPath("$.[*].tokenIdentificacion").value(hasItem(DEFAULT_TOKEN_IDENTIFICACION)));

        // Check, that the count call also returns 1
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUsuarioShouldNotBeFound(String filter) throws Exception {
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUsuario() throws Exception {
        // Get the usuario
        restUsuarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario
        Usuario updatedUsuario = usuarioRepository.findById(usuario.getId()).get();
        // Disconnect from session so that the updates on updatedUsuario are not directly saved in db
        em.detach(updatedUsuario);
        updatedUsuario.nombre(UPDATED_NOMBRE).idGitLab(UPDATED_ID_GIT_LAB).tokenIdentificacion(UPDATED_TOKEN_IDENTIFICACION);

        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUsuario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testUsuario.getIdGitLab()).isEqualTo(UPDATED_ID_GIT_LAB);
        assertThat(testUsuario.getTokenIdentificacion()).isEqualTo(UPDATED_TOKEN_IDENTIFICACION);
    }

    @Test
    @Transactional
    void putNonExistingUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario.nombre(UPDATED_NOMBRE).idGitLab(UPDATED_ID_GIT_LAB);

        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testUsuario.getIdGitLab()).isEqualTo(UPDATED_ID_GIT_LAB);
        assertThat(testUsuario.getTokenIdentificacion()).isEqualTo(DEFAULT_TOKEN_IDENTIFICACION);
    }

    @Test
    @Transactional
    void fullUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario.nombre(UPDATED_NOMBRE).idGitLab(UPDATED_ID_GIT_LAB).tokenIdentificacion(UPDATED_TOKEN_IDENTIFICACION);

        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testUsuario.getIdGitLab()).isEqualTo(UPDATED_ID_GIT_LAB);
        assertThat(testUsuario.getTokenIdentificacion()).isEqualTo(UPDATED_TOKEN_IDENTIFICACION);
    }

    @Test
    @Transactional
    void patchNonExistingUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, usuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(usuario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeDelete = usuarioRepository.findAll().size();

        // Delete the usuario
        restUsuarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, usuario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
