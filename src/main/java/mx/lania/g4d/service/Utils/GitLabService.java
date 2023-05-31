package mx.lania.g4d.service.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mx.lania.g4d.service.mapper.Issue;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class GitLabService {

    private final String privateToken = "glpat-XAxKq1RRktSdNcaqWNSk";
    private static final String GITLAB_API_URL = "https://gitlab.com/api/v4/";

    private final ApiCaller apiCaller;

    public GitLabService(ApiCaller apiCaller) {
        this.apiCaller = apiCaller;
    }

    public String getUserGItLabByToken() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + privateToken);

            HttpEntity request = new HttpEntity(headers);
            final String servicioGitLab = "user";

            ResponseEntity<String> response = new RestTemplate()
                .exchange(GITLAB_API_URL + servicioGitLab, HttpMethod.GET, request, String.class);
            String json = response.getBody();
            System.out.println(json);

            return json;
        } catch (HttpClientErrorException.Unauthorized ex) {
            return "{ msg: 'token invalido'}";
        }
    }

    public String SaveProyect(String name) {
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
                .exchange(GITLAB_API_URL + servicioGitLab, HttpMethod.POST, request, String.class);
            String json = response.getBody();
            System.out.println(json);

            return json;
        } catch (HttpClientErrorException.Unauthorized ex) {
            return "{ msg: 'token invalido'}";
        }
    }

    // hace uso de la ApiCaller
    public String SaveProjecto(String name) {
        JSONObject requestObject = new JSONObject();
        requestObject.put("name", name);

        JSONObject res = apiCaller.httpCall(HttpMethod.POST, GITLAB_API_URL, "projects", privateToken, requestObject);

        if (!res.isEmpty()) {
            return res.getAsString("id");
        }
        return "No econtrado";
    }

    public String createIssue(String title, String[] labels, String description, String milestoneId, String projectId) {
        JSONObject requestObject = new JSONObject();
        requestObject.put("title", title);
        requestObject.put("description", description);
        requestObject.put("labels", labels);
        requestObject.put("milestone_id", milestoneId);

        String recurso = "projects/" + projectId + "/issues";

        JSONObject res = apiCaller.httpCall(HttpMethod.POST, GITLAB_API_URL, recurso, privateToken, requestObject);

        if (!res.isEmpty()) {
            return res.getAsString("iid");
        }
        return "No fué posible crear el Issue";
    }

    public String updateIssue(String title, String[] labels, String description, int projectId, int issue_idd) {
        JSONObject requestObject = new JSONObject();
        requestObject.put("title", title);
        requestObject.put("description", description);
        requestObject.put("labels", labels);

        String recurso = "projects/" + projectId + "/issues/" + issue_idd;

        JSONObject res = apiCaller.httpCall(HttpMethod.PUT, GITLAB_API_URL, recurso, privateToken, requestObject);

        if (!res.isEmpty()) {
            return res.getAsString("id");
        }
        return "No fué posible editar el Issue";
    }

    public String updateIssieLabels(String labels, String projectId, String issue_idd) {
        JSONObject requestObject = new JSONObject();
        requestObject.put("labels", labels);

        String recurso = "projects/" + projectId + "/issues/" + issue_idd;
        JSONObject res = apiCaller.httpCall(HttpMethod.PUT, GITLAB_API_URL, recurso, privateToken, requestObject);

        if (!res.isEmpty()) {
            return res.getAsString("id");
        }
        return "No fué posible modificar el Issue";
    }

    public String deleteIssue(int projectId, int issue_id) {
        JSONObject requestObject = new JSONObject();

        String recurso = "projects/" + projectId + "/issues/" + issue_id;

        JSONObject res = apiCaller.httpCall(HttpMethod.DELETE, GITLAB_API_URL, recurso, privateToken, requestObject);

        if (!res.isEmpty()) {
            return res.getAsString("id");
        }
        return "No fué posible editar el Issue";
    }

    public String createMilestone(String title, String description, LocalDate due_date, LocalDate start_date, String projectId) {
        JSONObject requestObject = new JSONObject();
        requestObject.put("title", title);
        requestObject.put("description", description);
        requestObject.put("due_date", due_date);
        requestObject.put("start_date", start_date);

        String recurso = "projects/" + projectId + "/milestones";

        JSONObject res = apiCaller.httpCall(HttpMethod.POST, GITLAB_API_URL, recurso, privateToken, requestObject);

        if (!res.isEmpty()) {
            return res.getAsString("id");
        }
        return "No fué posible crear el Milestone";
    }

    public String editMilestone(String title, String description, Date due_date, Date start_date, int projectId, int milestone_id) {
        JSONObject requestObject = new JSONObject();
        requestObject.put("title", title);
        requestObject.put("description", description);
        requestObject.put("due_date", due_date);
        requestObject.put("start_date", start_date);

        String recurso = "projects/" + projectId + "/milestones/" + milestone_id;

        JSONObject res = apiCaller.httpCall(HttpMethod.POST, GITLAB_API_URL, recurso, privateToken, requestObject);

        if (!res.isEmpty()) {
            return res.getAsString("id");
        }
        return "No fué posible editar el Milestone";
    }

    public String deleteMilestoene(int projectId, int milestone_id) {
        JSONObject requestObject = new JSONObject();

        String recurso = "projects/" + projectId + "/milestones/" + milestone_id;

        JSONObject res = apiCaller.httpCall(HttpMethod.DELETE, GITLAB_API_URL, recurso, privateToken, requestObject);

        if (!res.isEmpty()) {
            return res.getAsString("id");
        }
        return "No fué posible editar el Milestone";
    }

    public List<Issue> GetAllIssuesByProyectoId(String gitLabProyectoId) {
        JSONObject requestObject = new JSONObject();

        String recurso = "projects/" + gitLabProyectoId + "/issues";

        JSONArray jsonArray = apiCaller.httpCallArray(HttpMethod.GET, GITLAB_API_URL, recurso, privateToken, requestObject);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<Issue> data = null;
        try {
            data = objectMapper.readValue(jsonArray.toString(), new TypeReference<List<Issue>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        /*
        try {
            data = Converter.fromJsonString(jsonString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        */
        //System.out.println(data);

        return data;
    }
}
