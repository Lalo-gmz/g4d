package mx.lania.g4d.service.Utils;

import net.minidev.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class GitLabService {

    private final String privateToken = "glpat-XAxKq1RRktSdNcaqWNSk";
    private static final String GITLAB_API_URL = "https://gitlab.com/api/v4/";

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
}
