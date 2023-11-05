package mx.lania.g4d.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthGitLabService {

    private static final String GITLAB_API_URL = "https://gitlab.com/api/v4/user";

    public String verifyToken(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);

            HttpEntity<?> request = new HttpEntity<>(headers); // Agregar el tipo parametrizado <?>

            ResponseEntity<String> response = new RestTemplate().exchange(GITLAB_API_URL, HttpMethod.GET, request, String.class);

            return response.getBody();
        } catch (Unauthorized ex) {
            return "{ msg: 'token invalido'}";
        }
    }
}
