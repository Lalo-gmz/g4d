package mx.lania.g4d.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import mx.lania.g4d.service.AuthGitLabService;
import mx.lania.g4d.service.mapper.Issue;
import mx.lania.g4d.service.utils.GitLabService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GItLabResource {

    private final AuthGitLabService authGitLabService;
    private final GitLabService gitLabService;

    public GItLabResource(AuthGitLabService authGitLabService, GitLabService gitLabService) {
        this.authGitLabService = authGitLabService;
        this.gitLabService = gitLabService;
    }

    private final Logger log = LoggerFactory.getLogger(GItLabResource.class);

    @GetMapping("/authenticateByToken")
    public String isAuthenticatedByToken(@RequestParam(value = "token") String token) throws JsonProcessingException {
        String s = authGitLabService.verifyToken(token);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(s, new TypeReference<Map<String, Object>>() {});

        String email = (String) map.get("email");
        log.debug("Email: {}", email);

        return "";
    }

    @GetMapping("/ValidatePersonalToken")
    public String getUserGItLabByToken() {
        return gitLabService.getUserGItLabByToken();
    }

    @PostMapping("/createGitProyect")
    public String getUserGItLabByToken(@RequestParam(value = "name") String name) {
        return gitLabService.saveProyect(name);
    }

    @GetMapping("/getAllProyectsByProyectId")
    public List<Issue> getAllProyectsByProyectId(@RequestParam(value = "proyectId") String proyectId) {
        return gitLabService.getAllIssuesByProyectoId(proyectId);
    }
}
