package mx.lania.g4d.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mx.lania.g4d.domain.Captura;
import mx.lania.g4d.domain.Funcionalidad;
import mx.lania.g4d.domain.Proyecto;
import mx.lania.g4d.repository.CapturaRepository;
import mx.lania.g4d.service.Utils.InstantTypeAdapter;
import mx.lania.g4d.service.Utils.LocalDateTypeAdapter;
import mx.lania.g4d.service.mapper.CapturaResponse;
import mx.lania.g4d.service.mapper.FuncionalidadResponse;
import net.minidev.json.JSONArray;
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

    public CapturaService(CapturaRepository capturaRepository, FuncionalidadService funcionalidadService, ProyectoService proyectoService) {
        this.capturaRepository = capturaRepository;
        this.funcionalidadService = funcionalidadService;
        this.proyectoService = proyectoService;
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
        Optional<Proyecto> proyectoOptional = proyectoService.findOne(proyectoId);
        if (proyectoOptional.isPresent()) {
            Proyecto proyecto = proyectoOptional.get();

            try {
                String json = objectMapper.writeValueAsString(funcionalidadList);
                System.out.println(json);

                captura.setProyecto(proyecto);
                captura.setFecha(Instant.now());
                captura.setFuncionalidades(json);
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
