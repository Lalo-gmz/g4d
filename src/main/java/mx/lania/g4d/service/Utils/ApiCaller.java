package mx.lania.g4d.service.Utils;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiCaller {

    public JSONObject httpCall(HttpMethod method, String url, String recurso, String privateToken, JSONObject data) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + privateToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            String requestBody = data.toJSONString();

            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = new RestTemplate().exchange(url + recurso, method, request, String.class);

            String jsonString = response.getBody();

            return (JSONObject) JSONValue.parse(jsonString);
        } catch (HttpClientErrorException.Unauthorized ex) {
            return (JSONObject) JSONValue.parse("jsonString");
        }
    }
}
