package mx.lania.g4d.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpResponse;
import java.util.Map;
import mx.lania.g4d.service.AuthGitLabService;
import mx.lania.g4d.service.Utils.GitLabService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
        log.debug("Email: " + email);

        return "";
    }

    @GetMapping("/ValidatePersonalToken")
    public String getUserGItLabByToken() {
        return gitLabService.getUserGItLabByToken();
    }

    @PostMapping("/createGitProyect")
    public String getUserGItLabByToken(@RequestParam(value = "name") String name) {
        return gitLabService.SaveProyect(name);
    }
}
