package mx.lania.g4d.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import mx.lania.g4d.domain.*;
import mx.lania.g4d.repository.CapturaRepository;
import mx.lania.g4d.service.Utils.InstantTypeAdapter;
import mx.lania.g4d.service.Utils.LocalDateTypeAdapter;
import mx.lania.g4d.service.mapper.AtributosAdicionales;
import mx.lania.g4d.service.mapper.CapturaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Captura}.
 */
@Service
@Transactional
public class CapturaService {

    private final Logger log = LoggerFactory.getLogger(CapturaService.class);

    private final CapturaRepository capturaRepository;
    private final FuncionalidadService funcionalidadService;
    private final ProyectoService proyectoService;
    private final AtributoService atributoService;

    public CapturaService(
        CapturaRepository capturaRepository,
        FuncionalidadService funcionalidadService,
        ProyectoService proyectoService,
        AtributoService atributoService
    ) {
        this.capturaRepository = capturaRepository;
        this.funcionalidadService = funcionalidadService;
        this.proyectoService = proyectoService;
        this.atributoService = atributoService;
    }

    /**
     * Save a captura.
     *
     * @param captura the entity to save.
     * @return the persisted entity.
     */
    public Captura save(Captura captura) {
        log.debug("Request to save Captura : {}", captura);
        return capturaRepository.save(captura);
    }

    public Captura create(Long proyectoId) {
        Captura captura = new Captura();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        List<Funcionalidad> funcionalidadList = funcionalidadService.findAllByProyectoId(proyectoId);

        for (Funcionalidad funcionalidad : funcionalidadList) {
            Map<String, AtributosAdicionales> atributosAdicionalesMap = new HashMap<>();
            for (AtributoFuncionalidad atributoFuncionalidad : funcionalidad.getAtributoFuncionalidads()) {
                Optional<Atributo> atributoOptional = atributoService.findOne(atributoFuncionalidad.getAtributo().getId());
                if (atributoOptional.isPresent()) {
                    Atributo atributo = atributoOptional.get();
                    atributosAdicionalesMap.put(
                        atributo.getNombre(),
                        new AtributosAdicionales(atributo.getNombre(), atributoFuncionalidad.getValor())
                    );
                }
            }
            funcionalidad.setAtributosAdicionales(atributosAdicionalesMap);
        }

        Optional<Proyecto> proyectoOptional = proyectoService.findOne(proyectoId);
        if (proyectoOptional.isPresent()) {
            Proyecto proyecto = proyectoOptional.get();

            try {
                String jsonString = objectMapper.writeValueAsString(funcionalidadList);
                System.out.println(jsonString);

                captura.setProyecto(proyecto);
                captura.setFecha(Instant.now());
                captura.setFuncionalidades(jsonString);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return captura;
    }

    /**
     * Update a captura.
     *
     * @param captura the entity to save.
     * @return the persisted entity.
     */
    public Captura update(Captura captura) {
        log.debug("Request to update Captura : {}", captura);
        return capturaRepository.save(captura);
    }

    /**
     * Partially update a captura.
     *
     * @param captura the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Captura> partialUpdate(Captura captura) {
        log.debug("Request to partially update Captura : {}", captura);

        return capturaRepository
            .findById(captura.getId())
            .map(existingCaptura -> {
                if (captura.getFuncionalidades() != null) {
                    existingCaptura.setFuncionalidades(captura.getFuncionalidades());
                }
                if (captura.getFecha() != null) {
                    existingCaptura.setFecha(captura.getFecha());
                }

                return existingCaptura;
            })
            .map(capturaRepository::save);
    }

    /**
     * Get all the capturas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Captura> findAll() {
        log.debug("Request to get all Capturas");
        return capturaRepository.findAll();
    }

    /**
     * Get one captura by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Captura> findOne(Long id) {
        log.debug("Request to get Captura : {}", id);
        return capturaRepository.findById(id);
    }

    /**
     * Delete the captura by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Captura : {}", id);
        capturaRepository.deleteById(id);
    }

    public List<CapturaResponse> createObjectByCapturaFuncionalidadJson(List<Captura> capturas) {
        List<CapturaResponse> capturaResponseList = new ArrayList<>();
        for (Captura captura : capturas) {
            String jsonString = captura.getFuncionalidades();
            System.out.println(jsonString);
            List<Funcionalidad> funcionalidadesJson = parseJsonArray(jsonString);

            CapturaResponse capturaResponse = new CapturaResponse();
            capturaResponse.setFuncionalidadList(funcionalidadesJson);
            capturaResponse.setFecha(captura.getFecha());
            capturaResponse.setProyecto(captura.getProyecto());
            capturaResponseList.add(capturaResponse);
        }
        return capturaResponseList;
    }

    public static String removeBackslashes(String jsonString) {
        return jsonString.replace("\\", "");
    }

    public static List<Funcionalidad> parseJsonArray(String jsonArrayString) {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .create();
        Type List = new TypeToken<List<Funcionalidad>>() {}.getType();
        return gson.fromJson(jsonArrayString, List);
    }

    public List<Captura> getAllCapturasByProyectId(Long proyectoId) {
        Optional<List<Captura>> capturaListOpcional = capturaRepository.getAllByProyectoId(proyectoId);
        return capturaListOpcional.orElseGet(ArrayList::new);
    }

    public List<CapturaResponse> getFuncionalidadsFromCapturaByProyectoId(Long proyectoId) {
        List<Captura> capturasByProyecto = getAllCapturasByProyectId(proyectoId);
        return createObjectByCapturaFuncionalidadJson(capturasByProyecto);
    }
}
