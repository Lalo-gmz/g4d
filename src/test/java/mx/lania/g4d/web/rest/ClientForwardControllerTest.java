package mx.lania.g4d.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Unit tests for the {@link ClientForwardController} REST controller.
 */
class ClientForwardControllerTest {

    private MockMvc restMockMvc;

    @BeforeEach
    public void setup() {
        ClientForwardController clientForwardController = new ClientForwardController();
        this.restMockMvc = MockMvcBuilders.standaloneSetup(clientForwardController, new TestController()).build();
    }

    void getBackendEndpoint() throws Exception {
        restMockMvc
            .perform(get("/test"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
            .andExpect(content().string("test"));
    }

    void getClientEndpoint() throws Exception {
        ResultActions perform = restMockMvc.perform(get("/non-existant-mapping"));
        perform.andExpect(status().isOk()).andExpect(forwardedUrl("/"));
    }

    void getNestedClientEndpoint() throws Exception {
        restMockMvc.perform(get("/admin/user-management")).andExpect(status().isOk()).andExpect(forwardedUrl("/"));
    }

    void getUnmappedDottedEndpoint() throws Exception {
        restMockMvc.perform(get("/foo.js")).andExpect(status().isNotFound());
    }

    void getUnmappedNestedDottedEndpoint() throws Exception {
        restMockMvc.perform(get("/foo/bar.js")).andExpect(status().isNotFound());
    }

    @RestController
    public static class TestController {

        @RequestMapping(value = "/test")
        public String test() {
            return "test";
        }
    }
}
