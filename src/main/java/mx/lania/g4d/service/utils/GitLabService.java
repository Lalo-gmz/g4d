package mx.lania.g4d.service.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.lania.g4d.service.mapper.Issue;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource("classpath:config/extraConfig/config.properties")
@Service
public class GitLabService {

    @Value("${gitlab.apikey}")
    private String privateToken;

    private static final String gitLabApiUrl = "https://gitlab.com/api/v4/";
    private static final String webUrl = "web_url";
    private static final String title = "title";
    private static final String propLabel = "labels";
    private static final String propDescripcion = "description";

    private static final String gitLabEndPointProyecto = "projects/";
    private static final String gitLabEndPointIssues = "/issues";
    private static final String gitLabEndPointMilestones = "/milestones";

    private final ApiCaller apiCaller;

    public GitLabService(ApiCaller apiCaller) {
        this.apiCaller = apiCaller;
    }

    public String getUserGItLabByToken() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + privateToken);

            HttpEntity<String> request = new HttpEntity<>(headers);
            final String servicioGitLab = "user";

            ResponseEntity<String> response = new RestTemplate()
                .exchange(gitLabApiUrl + servicioGitLab, HttpMethod.GET, request, String.class);

            return response.getBody();
        } catch (HttpClientErrorException.Unauthorized ex) {
            return "{ msg: 'token invalido'}";
        }
    }

    public String saveProyect(String name) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + privateToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            JSONObject requestObject = new JSONObject();
            requestObject.put("name", name);
            String requestBody = requestObject.toJSONString();

            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            final String servicioGitLab = "projects";

            ResponseEntity<String> response = new RestTemplate()
                .exchange(gitLabApiUrl + servicioGitLab, HttpMethod.POST, request, String.class);

            return response.getBody();
        } catch (HttpClientErrorException.Unauthorized ex) {
            return "{ msg: 'token invalido'}";
        }
    }

    public Map<String, String> findProject(String idGitLab) {
        JSONObject requestObject = new JSONObject();
        requestObject.put("id", idGitLab);
        Map<String, String> idGitLabProject = new HashMap<>();
        try {
            JSONObject res = apiCaller.httpCall(HttpMethod.GET, gitLabApiUrl, "projects/" + idGitLab, privateToken, requestObject);
            if (res != null && !res.isEmpty()) {
                String id = res.get("id").toString();
                String webUrlTemp = res.get("web_url").toString();
                idGitLabProject.put("id", id);
                idGitLabProject.put("web_url", webUrlTemp);
            } else {
                throw new GitLabProjectNotFoundException("Project not found in GitLab.");
            }
        } catch (HttpClientErrorException ex) {
            String errorMessage = "El id del Proyecto no Existe en Gitlab.";
            throw new GitLabProjectNotFoundException(errorMessage);
        } catch (Exception ex) {
            String errorMessage = "Se ha producido un error inesperado.";
            throw new GitLabProjectNotFoundException(errorMessage);
        }
        return idGitLabProject;
    }

    // hace uso de la ApiCaller
    public Map<String, String> saveProjecto(String name) {
        JSONObject requestObject = new JSONObject();
        requestObject.put("name", name);
        Map<String, String> idGitLabProject = new HashMap<>();
        try {
            JSONObject res = apiCaller.httpCall(HttpMethod.POST, gitLabApiUrl, "projects", privateToken, requestObject);
            if (res != null && !res.isEmpty()) {
                String id = (String) res.get("id");
                String webUrlTemp = (String) res.get("web_url");
                idGitLabProject.put("id", id);
                idGitLabProject.put("web_url", webUrlTemp);
            }
        } catch (HttpClientErrorException ex) {
            throw new GitLabProjectNameAlreadyExistException("El nombre del proyecto ya Existe en GitLab, intenta con otro");
        }
        return idGitLabProject;
    }

    public Map<String, String> createIssue(String title, String[] labels, String description, String milestoneId, String projectId) {
        JSONObject requestObject = new JSONObject();
        requestObject.put(GitLabService.title, title);
        requestObject.put(propDescripcion, description);
        requestObject.put(propLabel, labels);
        requestObject.put("milestone_id", milestoneId);

        String recurso = gitLabEndPointProyecto + projectId + gitLabEndPointIssues;

        JSONObject res = apiCaller.httpCall(HttpMethod.POST, gitLabApiUrl, recurso, privateToken, requestObject);

        if (!res.isEmpty()) {
            String iid = res.getAsString("iid");
            String webUrlTemp = res.getAsString(GitLabService.webUrl);

            Map<String, String> result = new HashMap<>();
            result.put("iid", iid);
            result.put(GitLabService.webUrl, webUrlTemp);

            return result;
        }

        return new HashMap<>();
    }

    public String updateIssue(String title, String[] labels, String description, int projectId, int issueIdd) {
        JSONObject requestObject = new JSONObject();
        requestObject.put(GitLabService.title, title);
        requestObject.put(propDescripcion, description);
        requestObject.put(propLabel, labels);

        String recurso = gitLabEndPointProyecto + projectId + gitLabEndPointIssues + issueIdd;

        JSONObject res = apiCaller.httpCall(HttpMethod.PUT, gitLabApiUrl, recurso, privateToken, requestObject);

        if (!res.isEmpty()) {
            return res.getAsString("id");
        }
        return "No fué posible editar el Issue";
    }

    public String updateIssieLabels(String labels, String projectId, String issueIdd) {
        JSONObject requestObject = new JSONObject();
        requestObject.put(propLabel, labels);

        String recurso = gitLabEndPointProyecto + projectId + gitLabEndPointIssues + issueIdd;
        JSONObject res = apiCaller.httpCall(HttpMethod.PUT, gitLabApiUrl, recurso, privateToken, requestObject);

        if (!res.isEmpty()) {
            return res.getAsString("id");
        }
        return "No fué posible modificar el Issue";
    }

    public String deleteIssue(int projectId, int issueId) {
        JSONObject requestObject = new JSONObject();

        String recurso = gitLabEndPointProyecto + projectId + gitLabEndPointIssues + issueId;

        JSONObject res = apiCaller.httpCall(HttpMethod.DELETE, gitLabApiUrl, recurso, privateToken, requestObject);

        if (!res.isEmpty()) {
            return res.getAsString("id");
        }
        return "No fué posible editar el Issue";
    }

    public String createMilestone(String title, String description, LocalDate dueDate, LocalDate startDate, String projectId) {
        JSONObject requestObject = new JSONObject();
        requestObject.put(GitLabService.title, title);
        requestObject.put(propDescripcion, description);
        requestObject.put("due_date", dueDate.toString());
        requestObject.put("start_date", startDate.toString());

        String recurso = gitLabEndPointProyecto + projectId + gitLabEndPointMilestones;

        JSONObject res = apiCaller.httpCall(HttpMethod.POST, gitLabApiUrl, recurso, privateToken, requestObject);

        if (!res.isEmpty()) {
            return res.getAsString("id");
        }
        return "No fué posible crear el Milestone";
    }

    public String editMilestone(String title, String description, Date dueDate, Date startDate, int projectId, int milestoneId) {
        JSONObject requestObject = new JSONObject();
        requestObject.put(GitLabService.title, title);
        requestObject.put(propDescripcion, description);
        requestObject.put("due_date", dueDate);
        requestObject.put("start_date", startDate);

        String recurso = gitLabEndPointProyecto + projectId + gitLabEndPointMilestones + milestoneId;

        JSONObject res = apiCaller.httpCall(HttpMethod.POST, gitLabApiUrl, recurso, privateToken, requestObject);

        if (!res.isEmpty()) {
            return res.getAsString("id");
        }
        return "No fué posible editar el Milestone";
    }

    public String deleteMilestoene(int projectId, int milestoneId) {
        JSONObject requestObject = new JSONObject();

        String recurso = gitLabEndPointProyecto + projectId + gitLabEndPointMilestones + milestoneId;

        JSONObject res = apiCaller.httpCall(HttpMethod.DELETE, gitLabApiUrl, recurso, privateToken, requestObject);

        if (!res.isEmpty()) {
            return res.getAsString("id");
        }
        return "No fué posible editar el Milestone";
    }

    public List<Issue> getAllIssuesByProyectoId(String gitLabProyectoId) {
        JSONObject requestObject = new JSONObject();

        String recurso = gitLabEndPointProyecto + gitLabProyectoId + gitLabEndPointIssues;

        JSONArray jsonArray = apiCaller.httpCallArray(HttpMethod.GET, gitLabApiUrl, recurso, privateToken, requestObject);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<Issue> data = null;
        try {
            data = objectMapper.readValue(jsonArray.toString(), new TypeReference<List<Issue>>() {});
        } catch (JsonProcessingException e) {
            throw new JsonParsingException("Error al procesar JSON", e);
        }

        return data;
    }
}
