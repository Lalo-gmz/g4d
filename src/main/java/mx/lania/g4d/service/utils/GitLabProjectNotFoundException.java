package mx.lania.g4d.service.utils;

public class GitLabProjectNotFoundException extends RuntimeException {

    public GitLabProjectNotFoundException(String message) {
        super(message);
    }
}
